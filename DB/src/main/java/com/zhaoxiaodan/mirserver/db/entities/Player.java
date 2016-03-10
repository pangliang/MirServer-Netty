package com.zhaoxiaodan.mirserver.db.entities;

import com.alibaba.fastjson.JSON;
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
import java.util.HashMap;
import java.util.Map;

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
	;

	@OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@Where(clause = "isWearing=true")
	@MapKey(name = "wearingPosition")
	public Map<WearPosition, PlayerItem> wearingItems = new HashMap<>();

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

	public boolean takeOn(PlayerItem itemToWear, WearPosition wearPosition){
		if(itemToWear.player != this){
			itemToWear.player = this;
		}

		items.get(itemToWear.id);

		// 原来位置穿有装备
		PlayerItem wearingItem = wearingItems.get(wearPosition);
		if(null != wearingItem){
			wearingItems.remove(wearingItem.wearingPosition);
			wearingItem.wearingPosition = null;
			wearingItem.isWearing = false;
			session.db.update(wearingItem);

			items.put(wearingItem.id,wearingItem);

			session.sendPacket(new ServerPacket.AddItem(inGameId, wearingItem));
		}

		wearingItems.put(wearPosition,itemToWear);

		itemToWear.wearingPosition = wearPosition;
		itemToWear.isWearing = true;
		session.db.update(itemToWear);

		checkAbility();
		session.sendPacket(new ServerPacket.PlayerAbility(this));

		return true;
	}

	public boolean takeOff(WearPosition wearPosition){
		PlayerItem wearingItem = wearingItems.remove(wearPosition);
		if(wearingItem == null)
		{
			return false;
		}

		wearingItem.isWearing = false;
		wearingItem.wearingPosition = null;
		session.db.update(wearingItem);

		items.put(wearingItem.id,wearingItem);
		session.sendPacket(new ServerPacket.AddItem(inGameId, wearingItem));

		checkAbility();
		session.sendPacket(new ServerPacket.PlayerAbility(this));
		return true;
	}

	public void takeNewItem(StdItem stdItem){
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

	public void levelUp(int up){
		this.baseAbility.Level += 1;
		ScriptEngine.exce(SCRIPT_NAME, "onLevelUp", this);
		session.sendPacket(new ServerPacket(this.baseAbility.Exp, Protocol.SM_LEVELUP, this.baseAbility.Level, (short) 0, (short) 0));

		checkAbility();
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

	public void kill(Monster monster) {
		winExp(monster.stdMonster.exp);
	}

	@Override
	public boolean hit(Direction direction) {
		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(this.currMapPoint.mapId);
		for (BaseObject object : mapInfo.getObjectsOnLine(this.currMapPoint, direction, 1, 1)) {
			if (!(object instanceof Monster))
				continue;
			Monster monster = (Monster) object;
			if (monster.damage(this, getPower())) {
				kill(monster);
			}
		}
		return super.hit(direction);
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
	public short getPower() {
		return 100;
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
