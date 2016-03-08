package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.objects.BaseObject;
import com.zhaoxiaodan.mirserver.db.types.*;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.Session;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Player extends BaseObject {

	public Player() {
		race = Race.Player;
	}

	@Id
	@GeneratedValue
	private int  id;
	@ManyToOne
	@JoinColumn(name = "userId")
	public  User user;

	/**
	 * 头发
	 */
	public byte hair;
	public Job  job;
	public Ability ability = new Ability();
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
	@Where(clause="isWearing=false")
	@MapKey(name = "id")
	public Map<Integer,PlayerItem> items = new HashMap<>();;

	@OneToMany(mappedBy = "player", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@Where(clause="isWearing=true")
	@MapKey(name = "wearingPosition")
	public Map<WearPosition,PlayerItem> wearingItems = new HashMap<>();

	/**
	 * 最后动作时间, 用来防止加速
	 */
	@Transient
	public long lastActionTime = 0;

	@Transient
	public Session session;

	@Override
	public void see(BaseObject object) {
		super.see(object);
		if (object == this)
			return;
		session.sendPacket(new ServerPacket.Turn(object));
	}

	@Override
	public void lose(BaseObject object) {
		super.lose(object);
		if(object == this)
			return;
		session.sendPacket(new ServerPacket(object.inGameId, Protocol.SM_DISAPPEAR));
	}

	@Override
	public int getFeature() {
		int dressShape = 0;
		int weaponShape = 0;
		int hairShape = 0;
		PlayerItem item;
		if((item = this.wearingItems.get(WearPosition.Dress)) != null)
			dressShape = item.attr.shape * 2;
		if((item = this.wearingItems.get(WearPosition.Weapon)) != null)
			weaponShape = item.attr.shape * 2;

		hairShape = this.hair * 2;

		dressShape+=this.gender.ordinal();
		weaponShape+=this.gender.ordinal();
		hairShape+=this.gender.ordinal();

		return NumUtil.makeLong(NumUtil.makeWord(Race.Player.id,(byte)weaponShape), NumUtil.makeWord((byte)hairShape, (byte)dressShape));
	}

	@Override
	public void enterMap(MapPoint mapPoint) {

		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(mapPoint.mapId);

		// 清除物品
		session.sendPacket(new ServerPacket(Protocol.SM_CLEAROBJECTS));

		// 新图信息发给玩家
		session.sendPacket(new ServerPacket.ChangeMap(this.inGameId, mapPoint.x, mapPoint.y, (short) 0, this.currMapPoint.mapId));
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
