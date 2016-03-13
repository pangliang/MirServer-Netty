package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.objects.AnimalObject;
import com.zhaoxiaodan.mirserver.db.objects.BaseObject;
import com.zhaoxiaodan.mirserver.db.objects.Monster;
import com.zhaoxiaodan.mirserver.db.types.*;
import com.zhaoxiaodan.mirserver.gameserver.engine.ItemEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.Session;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity
public class Player extends AnimalObject {

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
	@MapKeyJoinColumn(name = "stdMagic.id", nullable = false)
	public Map<Integer, PlayerMagic> magics = new HashMap<>();

	@Transient
	public Map<Integer, PlayerMagic> buffers = new ConcurrentHashMap<>();

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

	public boolean hit(Direction direction, int magicId) {

		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(this.currMapPoint.mapId);
		int               power   = getPower();
		List<BaseObject>  targets = mapInfo.getObjectsOnLine(this.currMapPoint, direction, 1, 1);

		PlayerMagic playerMagic = buffers.get(magicId);
		if(playerMagic != null){
			power += (Integer) ScriptEngine.exce(playerMagic.stdMagic.scriptName, "useBuffer", this, playerMagic, power, targets);
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

	public void learnMagic(StdMagic stdMagic) {
		if (stdMagic == null || magics.containsKey(stdMagic.id))
			return;

		PlayerMagic playerMagic = new PlayerMagic();
		playerMagic.player = this;
		playerMagic.stdMagic = stdMagic;
		session.db.save(playerMagic);

		this.magics.put(stdMagic.id, playerMagic);
		session.sendPacket(new ServerPacket.AddMagic(playerMagic));
	}

	public void deleteAllMagic() {
		Iterator<PlayerMagic> it = magics.values().iterator();
		while (it.hasNext()) {
			PlayerMagic playerMagic = it.next();
			it.remove();

			session.db.delete(playerMagic);
			session.sendPacket(new ServerPacket(playerMagic.stdMagic.id, Protocol.SM_DELMAGIC, (short) 0, (short) 0, (short) 0));
		}
	}

	public void deleteMagic(int magicId) {
		if (!this.magics.containsKey(magicId))
			return;

		PlayerMagic playerMagic = this.magics.remove(magicId);
		session.db.delete(playerMagic);

		session.sendPacket(new ServerPacket(playerMagic.stdMagic.id, Protocol.SM_DELMAGIC, (short) 0, (short) 0, (short) 0));
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
			session.db.update(wearingItem);

			items.put(wearingItem.id, wearingItem);

			session.sendPacket(new ServerPacket.AddItem(inGameId, wearingItem));
		}

		wearingItems.put(wearPosition, itemToWear);

		itemToWear.wearingPosition = wearPosition;
		itemToWear.isWearing = true;
		session.db.update(itemToWear);

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
		session.db.update(wearingItem);

		items.put(wearingItem.id, wearingItem);
		session.sendPacket(new ServerPacket.AddItem(inGameId, wearingItem));

		checkAbility();
		session.sendPacket(new ServerPacket.PlayerAbility(this));
		return true;
	}

	public void takeNewItem(StdItem stdItem) {
		PlayerItem playerItem = new PlayerItem(stdItem, this);
		session.db.save(playerItem);
		items.put(playerItem.id, playerItem);

		session.sendPacket(new ServerPacket.AddItem(this.inGameId, playerItem));
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
		this.baseAbility.Level += up;
		ScriptEngine.exce(SCRIPT_NAME, "onLevelUp", this);
		session.sendPacket(new ServerPacket(this.baseAbility.Exp, Protocol.SM_LEVELUP, this.baseAbility.Level, (short) 0, (short) 0));

		checkAbility();
		session.sendPacket(new ServerPacket.PlayerAbility(this));
	}

	private void checkLevelUp() {
		if (this.baseAbility.Exp >= this.baseAbility.MaxExp) {
			this.baseAbility.Exp = this.baseAbility.Exp - this.baseAbility.MaxExp;
			levelUp(1);
			checkLevelUp(); //可能会升很多级
		}
	}

	public void winExp(int exp) {
		this.baseAbility.Exp += exp * Config.EXP_MULTIPLE;
		session.sendPacket(new ServerPacket(this.baseAbility.Exp, Protocol.SM_WINEXP, NumUtil.getLowWord(exp), NumUtil.getHighWord(exp), (short) 0));
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
			if (object instanceof AnimalObject && !((AnimalObject) object).isAlive) {
				session.sendPacket(new ServerPacket.Death(object));
			} else {
				session.sendPacket(new ServerPacket.Turn(object));
			}
		}

		return rs;
	}

	@Override
	public void lose(BaseObject object) {
		super.lose(object);
		if (object == this)
			return;
		session.sendPacket(new ServerPacket(object.inGameId, Protocol.SM_DISAPPEAR));
	}

	public void receive(ServerPacket packet) {
		session.sendPacket(packet);
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


	public boolean checkAndIncActionTime(int interval) {
		long now = NumUtil.getTickCount();
		if (now - lastActionTime < interval) {
			return false;
		}
		lastActionTime = now;
		return true;
	}

	@Override
	public void onTick() {

	}
}
