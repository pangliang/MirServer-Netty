package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.objects.AnimalObject;
import com.zhaoxiaodan.mirserver.db.objects.BaseObject;
import com.zhaoxiaodan.mirserver.db.objects.DropItem;
import com.zhaoxiaodan.mirserver.db.objects.Monster;
import com.zhaoxiaodan.mirserver.db.types.*;
import com.zhaoxiaodan.mirserver.gameserver.engine.ItemEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.Session;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Entity
public class Player extends AnimalObject {

	private static final Logger logger = LogManager.getLogger();

	public static final String SCRIPT_NAME = "PlayerScript";

	static {
		try {
			ScriptEngine.loadScript(SCRIPT_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Id
	@GeneratedValue
	private int  id;
	@ManyToOne
	@JoinColumn(name = "userId")
	public  User user;

	public String name;
	/**
	 * 头发
	 */
	public byte   hair;
	public Job    job;
	public Ability baseAbility = new Ability();

	public short Level;
	public int   mp;
	public int   maxMp;
	public int   Exp;
	public int   MaxExp;
	public short Weight;
	public short MaxWeight;
	public int   WearWeight;
	public int   MaxWearWeight;
	public int   HandWeight;
	public int   MaxHandWeight;

	@Transient
	private Ability currentAbility = null;
	/**
	 * 性别
	 */
	public Gender gender;

	/**
	 * 回城点
	 */
	@AttributeOverrides({
			@AttributeOverride(name = "mapId", column = @Column(name = "homeMapName")),
			@AttributeOverride(name = "x", column = @Column(name = "homeX")),
			@AttributeOverride(name = "y", column = @Column(name = "homeY"))
	})
	public MapPoint homeMapPoint;

	/**
	 * 金币
	 */
	public int gold;
	/**
	 * 游戏币
	 */
	public int gameGold;
	/**
	 * 游戏点数
	 */
	public int gamePoint;

	@OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@Where(clause = "isWearing=false")
	@MapKey(name = "id")
	public Map<Integer, PlayerItem> items = new HashMap<>();

	@OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@Where(clause = "isWearing=true")
	@MapKey(name = "wearingPosition")
	public Map<WearPosition, PlayerItem> wearingItems = new HashMap<>();

	@OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@MapKeyColumn(name = "stdMagicId")
	public Map<Integer, PlayerMagic> magics = new HashMap<>();

	/**
	 * 最后动作时间, 用来防止加速
	 */
	@Transient
	public long lastActionTime = 0;

	@Transient
	public Session session;

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean hit(Direction direction) {
		broadcast(new ServerPacket(this.inGameId, Protocol.SM_HIT, this.currMapPoint.x, this.currMapPoint.y, (short) direction.ordinal()));
		return this.hit(direction, 0);
	}

	@Override
	public void damage(AnimalObject source, int power) {
		super.damage(source, power);

		session.sendPacket(new ServerPacket.Struck(this.inGameId, this.hp, this.maxHp, power));
	}

	@Override
	public void beKilled(AnimalObject source) {
		super.beKilled(source);

		session.sendPacket(new ServerPacket(this.inGameId, Protocol.SM_NOWDEATH, this.currMapPoint.x, this.currMapPoint.y, (short) direction.ordinal()));
	}

	public boolean hit(Direction direction, int magicId) {

		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(this.currMapPoint.mapId);
		int               power   = getPower();
		List<BaseObject>  targets = mapInfo.getObjectsOnLine(this.currMapPoint, direction, 1, 1);

		PlayerMagic playerMagic = magics.get(magicId);
		if (playerMagic != null) {
			Integer addPower = (Integer) ScriptEngine.exce(playerMagic.stdMagic.scriptName, "useMagic", this, playerMagic, power, targets);
			if (addPower != null)
				power += addPower;
		}

		for (BaseObject object : targets) {
			if (!(object instanceof Monster))
				continue;
			Monster monster = (Monster) object;
			monster.damage(this, power);
		}
		return true;
	}

	public boolean spell(int magicId) {
		if (!magics.containsKey(magicId))
			return false;

		PlayerMagic playerMagic = magics.get(magicId);
		ScriptEngine.exce(playerMagic.stdMagic.scriptName, "spell", this, playerMagic);

		return true;
	}

	public boolean eat(int playerItemId) {
		PlayerItem playerItem = items.get(playerItemId);
		if (null == playerItem)
			return false;

		playerItem.player = null;
		DB.delete(playerItem);

		Boolean rs = (Boolean) ScriptEngine.exce(playerItem.stdItem.scriptName, "onEat", this, playerItem);
		if (rs == null || !rs)
			return true;
		return false;
	}

	public void healthSpellChange(int healthChange, int spellChange) {
		this.hp = this.hp + healthChange > this.maxHp ? this.maxHp : this.hp + healthChange;
		this.mp = this.mp + spellChange > this.maxMp ? this.maxMp : this.mp + spellChange;

		this.hp = this.hp < 0 ? 0 : this.hp;
		this.mp = this.mp < 0 ? 0 : this.mp;

		session.sendPacket(new ServerPacket(this.inGameId, Protocol.SM_HEALTHSPELLCHANGED, (short) this.hp, (short) this.mp, (short) this.maxHp));
	}

	public void learnMagic(StdMagic stdMagic) {
		if (stdMagic == null || magics.containsKey(stdMagic.id))
			return;

		PlayerMagic playerMagic = new PlayerMagic();
		playerMagic.player = this;
		playerMagic.stdMagic = stdMagic;
		DB.save(playerMagic);

		this.magics.put(stdMagic.id, playerMagic);
		session.sendPacket(new ServerPacket.AddMagic(playerMagic));
	}

	public void deleteAllMagic() {
		Iterator<PlayerMagic> it = magics.values().iterator();
		while (it.hasNext()) {
			PlayerMagic playerMagic = it.next();
			it.remove();

			DB.delete(playerMagic);
			session.sendPacket(new ServerPacket(playerMagic.stdMagic.id, Protocol.SM_DELMAGIC, (short) 0, (short) 0, (short) 0));
		}
	}

	public boolean takeOn(PlayerItem itemToWear, WearPosition wearPosition) {
		if (itemToWear.player != this) {
			itemToWear.player = this;
		}

		items.get(itemToWear.id);

		// 原来位置穿有装备
		PlayerItem wearingItem = wearingItems.get(wearPosition);
		if (null != wearingItem) {
			wearingItems.remove(wearingItem.wearingPosition);
			wearingItem.wearingPosition = null;
			wearingItem.isWearing = false;
			DB.update(wearingItem);

			items.put(wearingItem.id, wearingItem);

			session.sendPacket(new ServerPacket.AddItem(inGameId, wearingItem));
		}

		wearingItems.put(wearPosition, itemToWear);

		itemToWear.wearingPosition = wearPosition;
		itemToWear.isWearing = true;
		DB.update(itemToWear);

		checkAbility();
		session.sendPacket(new ServerPacket.PlayerAbility(this));

		return true;
	}

	public boolean takeOff(WearPosition wearPosition) {
		PlayerItem wearingItem = wearingItems.remove(wearPosition);
		if (wearingItem == null) {
			return false;
		}

		wearingItem.isWearing = false;
		wearingItem.wearingPosition = null;
		DB.update(wearingItem);

		items.put(wearingItem.id, wearingItem);
		session.sendPacket(new ServerPacket.AddItem(inGameId, wearingItem));

		checkAbility();
		session.sendPacket(new ServerPacket.PlayerAbility(this));
		return true;
	}

	public void takeNewItem(String itemName) {
		StdItem stdItem = ItemEngine.getStdItemByName(itemName);
		this.takeNewItem(stdItem);
	}

	public void takeNewItem(StdItem stdItem) {
		if (stdItem == null)
			return;
		PlayerItem playerItem = new PlayerItem(stdItem, this);
		DB.save(playerItem);
		items.put(playerItem.id, playerItem);

		session.sendPacket(new ServerPacket.AddItem(this.inGameId, playerItem));
	}

	public void takeNewItem(PlayerItem playerItem) {
		if (playerItem == null)
			return;
		DB.getSession().saveOrUpdate(playerItem);
		items.put(playerItem.id, playerItem);

		session.sendPacket(new ServerPacket.AddItem(this.inGameId, playerItem));
	}

	public boolean deleteItems(List<PlayerItem> itemList) {
		if (itemList == null)
			return false;

		for (PlayerItem playerItem : itemList) {
			if (!items.containsKey(playerItem.id)) {
				DB.getSession().getTransaction().rollback();
				return false;
			}

			DB.delete(playerItem);
			items.remove(playerItem.id);

		}

		session.sendPacket(new ServerPacket.DeleteItems(itemList));
		return true;
	}

	public synchronized boolean goldChange(int gold, int gameGold, int gamePoint) {
		if (this.gold + gold < 0
				|| this.gameGold + gameGold < 0
				|| this.gamePoint + gamePoint < 0)
			return false;

		this.gold += gold;
		this.gameGold += gameGold;
		this.gamePoint += gamePoint;

		if (gold != 0)
			session.sendPacket(new ServerPacket(this.gold, Protocol.SM_GOLDCHANGED, NumUtil.getLowWord(this.gameGold), NumUtil.getHighWord(this.gameGold), (short) 0));

		if (gamePoint != 0)
			session.sendPacket(new ServerPacket.GameGoldName(this.gameGold, this.gamePoint, Config.GAME_GOLD_NAME, Config.GAME_POINT_NAME));

		return true;

	}

	public Ability currentAbility() {
		if (this.currentAbility == null)
			checkAbility();

		return this.currentAbility;
	}

	public void checkAbility() {
		Ability ability = baseAbility.clone();
		for (PlayerItem item : this.wearingItems.values()) {
			ItemEngine.checkAbility(ability, item);
		}
		this.currentAbility = ability;
	}

	public void levelUp(int up) {
		this.Level += up;
		ScriptEngine.exce(SCRIPT_NAME, "onLevelUp", this);
		session.sendPacket(new ServerPacket(this.Exp, Protocol.SM_LEVELUP, this.Level, (short) 0, (short) 0));

		checkAbility();
		session.sendPacket(new ServerPacket.PlayerAbility(this));
	}

	private void checkLevelUp() {
		if (this.Exp >= this.MaxExp) {
			this.Exp = this.Exp - this.MaxExp;
			levelUp(1);
			checkLevelUp(); //可能会升很多级
		}
	}

	public void winExp(int exp) {
		this.Exp += exp * Config.EXP_MULTIPLE;
		session.sendPacket(new ServerPacket(this.Exp, Protocol.SM_WINEXP, NumUtil.getLowWord(exp), NumUtil.getHighWord(exp), (short) 0));
		checkLevelUp();
		checkAbility();
	}

	public void kill(AnimalObject animalObject) {
		if (animalObject instanceof Monster)
			winExp(((Monster) animalObject).stdMonster.exp);
	}

	@Override
	public boolean see(BaseObject object) {
		boolean rs;
		if (rs = super.see(object)) {
			if (object instanceof AnimalObject) {
				if (((AnimalObject) object).isAlive)
					session.sendPacket(new ServerPacket.Turn((AnimalObject) object));
				else
					session.sendPacket(new ServerPacket.Death((AnimalObject) object));
			} else if (object instanceof DropItem) {
				session.sendPacket(new ServerPacket.ItemShow((DropItem) object));
			}
		}

		return rs;
	}

	@Override
	public void lose(BaseObject object) {
		super.lose(object);
		if (object == this)
			return;
		if (object instanceof AnimalObject)
			session.sendPacket(new ServerPacket(object.inGameId, Protocol.SM_DISAPPEAR));
		else if (object instanceof DropItem)
			session.sendPacket(new ServerPacket.ItemHide((DropItem) object));
	}

	@Override
	public int getFeature() {
		int        dressShape  = 0;
		int        weaponShape = 0;
		int        hairShape   = 0;
		PlayerItem item;
		if ((item = this.wearingItems.get(WearPosition.Dress)) != null)
			dressShape = item.attr.shape * 2;
		if ((item = this.wearingItems.get(WearPosition.Weapon)) != null)
			weaponShape = item.attr.shape * 2;

		hairShape = this.hair * 2;

		dressShape += this.gender.ordinal();
		weaponShape += this.gender.ordinal();
		hairShape += this.gender.ordinal();

		return NumUtil.makeLong(NumUtil.makeWord(Race.Player.id, (byte) weaponShape), NumUtil.makeWord((byte) hairShape, (byte) dressShape));
	}

	@Override
	public short getFeatureEx() {
		return 0;
	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public int getPower() {
		int power = NumUtil.randomRang(currentAbility().DC, currentAbility().DC2);

		return power;
	}

	@Override
	public int getDefend() {
		return NumUtil.randomRang(currentAbility().AC, currentAbility().AC2);
	}

	@Override
	public void enterMap(MapPoint mapPoint) {

		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(mapPoint.mapId);

		// 清除物品
		session.sendPacket(new ServerPacket(Protocol.SM_CLEAROBJECTS));

		// 新图信息发给玩家
		session.sendPacket(new ServerPacket.ChangeMap(this.inGameId, mapPoint.x, mapPoint.y, (short) 0, mapPoint.mapId));
		session.sendPacket(new ServerPacket.MapDescription(-1, mapInfo.mapDescription));

		// 是否安全区
		session.sendPacket(new ServerPacket(2, Protocol.SM_AREASTATE, (byte) 0, (byte) 0, (byte) 0));

		super.enterMap(mapPoint);
	}

	public void sendSysMsg(String msg) {
		sendSysMsg(msg, Color.Black, Color.White);
	}

	public void sendSysMsg(String msg, Color ftCorol, Color bgColor) {
		session.sendPacket(new ServerPacket.SysMessage(this.inGameId, msg, ftCorol, bgColor));
	}

	public void sendAlarmMsg(String msg) {
		sendSysMsg(msg, Color.Yellow, Color.Red);
	}


	public boolean checkAndIncActionTime(int interval) {
		long now = NumUtil.getTickCount();
		if (now - lastActionTime < interval) {
			return false;
		}
		lastActionTime = now;
		return true;
	}

	@Transient
	private long lastCheckPickUpItemTime = 0;

	private void checkPickUpItem(long now) {
		if (now < lastCheckPickUpItemTime + Config.PLAYER_CHECK_PICKUP_ITEM_INTERVAL_TIME)
			return;

		lastCheckPickUpItemTime = now;
		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(this.currMapPoint.mapId);
		for (BaseObject object : mapInfo.getObjects(this.currMapPoint)) {
			if (object instanceof DropItem) {
				DropItem dropItem = (DropItem) object;
				if (dropItem.canPickUp(this)) {
					dropItem.leaveMap();
					this.takeNewItem(dropItem.stdItem);
				} else {
					sendSysMsg(dropItem.getName() + " 在一定时间内不能拾取!", Color.Yellow, Color.Red);
				}
			}
		}
	}

	public void writeAbilityPacket(ByteBuf out) {
		out.writeShort(Level);
		this.currentAbility().writePacket(out);
		out.writeShort(hp);
		out.writeShort(mp);
		out.writeShort(maxHp);
		out.writeShort(maxMp);
		out.writeInt(Exp);
		out.writeInt(MaxExp);
		out.writeShort(Weight);
		out.writeShort(MaxWeight);
		out.writeShort(WearWeight);
		out.writeShort(MaxWearWeight);
		out.writeShort(HandWeight);
		out.writeShort(MaxHandWeight);
	}

	@Override
	public void onTick(long now) {
		checkPickUpItem(now);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Player))
			return false;
		if (this.id == ((Player) obj).id)
			return true;

		return false;
	}
}
