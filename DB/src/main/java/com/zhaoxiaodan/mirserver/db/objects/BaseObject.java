package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.entities.Config;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.Color;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.db.types.Race;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@MappedSuperclass
public abstract class BaseObject {

	public final int inGameId = NumUtil.newAtomicId();

	public String name;
	public Color nameColor = Color.White;

	public Race  race = Race.Animal;
	public short appr = 0;

	/**
	 * 亮度
	 */
	public byte light;

	@AttributeOverrides({
			@AttributeOverride(name = "mapId", column = @Column(name = "currMapName")),
			@AttributeOverride(name = "x", column = @Column(name = "currX")),
			@AttributeOverride(name = "y", column = @Column(name = "currY"))
	})
	/**
	 * 当前地图位置
	 */
	public MapPoint currMapPoint = new MapPoint();

	/**
	 * 方向, 8个方向
	 */
	public Direction direction = Direction.DOWN;

	@Transient
	public Map<Integer, BaseObject> objectsInView = new ConcurrentHashMap<>();

	public void see(BaseObject object) {
		if (object == this)
			return;
		objectsInView.put(object.inGameId, object);
	}

	public void lose(BaseObject object) {
		if (object == this)
			return;
		objectsInView.remove(object.inGameId);
	}

	public void leaveMap() {
		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(this.currMapPoint.mapId);
		mapInfo.removeObject(this);

		broadcast(new ServerPacket(this.inGameId, Protocol.SM_DISAPPEAR));
	}

	/**
	 * 广播自己的消息给视野内的人
	 *
	 * @param serverPacket
	 */
	public void broadcast(ServerPacket serverPacket) {
		for (BaseObject object : this.objectsInView.values()) {
			if (object != this && object instanceof Player) {
				((Player) object).session.sendPacket(serverPacket);
			}
		}
	}

	public void walk(Direction direction) {
		this.move(direction, (short) 1);
		broadcast(new ServerPacket.Action(Protocol.SM_WALK, this));
	}

	public void run(Direction direction) {
		this.move(direction, (short) 2);
		broadcast(new ServerPacket.Action(Protocol.SM_RUN, this));
	}

	public void turn(Direction direction) {
		this.direction = direction;
		broadcast(new ServerPacket.Turn(this));
	}

	public void move(Direction direction, short distance) {
		this.direction = direction;
		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(currMapPoint.mapId);

		MapPoint fromPoint = this.currMapPoint.clone();
		MapPoint toPoint   = this.currMapPoint.clone();
		toPoint.move(direction, distance);

		mapInfo.objectMove(this, fromPoint);

		// 移动之后新进入视野的对象
		List<BaseObject> newSeeObjects = new ArrayList<>();
		// 移动之后退出视野的对象
		List<BaseObject> disappearObjects = new ArrayList<>();

		switch (direction) {
			case UP:
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE - (distance - 1),
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				currMapPoint.y -= distance;
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE + (distance - 1)
				));
				break;
			case UPRIGHT:
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE - (distance - 1),
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE + (distance - 1),
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				currMapPoint.x += distance;
				currMapPoint.y -= distance;
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE + (distance - 1)
				));
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE - (distance - 1),
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				break;
			case RIGHT:
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE + (distance - 1),
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				currMapPoint.x += distance;
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE - (distance - 1),
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				break;
			case DOWNRIGHT:
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE + (distance - 1)
				));
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE + (distance - 1),
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				currMapPoint.x += distance;
				currMapPoint.y += distance;
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE - (distance - 1),
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE - (distance - 1),
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				break;
			case DOWN:
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE + (distance - 1)
				));
				currMapPoint.y += distance;
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE - (distance - 1),
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				break;
			case DOWNLEFT:
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE + (distance - 1)
				));
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE - (distance -1),
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				currMapPoint.x -= distance;
				currMapPoint.y += distance;
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE - (distance - 1),
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE + (distance -1),
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				break;
			case LEFT:
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE - (distance -1),
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				currMapPoint.x -= distance;
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE + (distance -1),
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				break;
			case UPLEFT:
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE - (distance - 1),
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				disappearObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE - (distance -1),
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				currMapPoint.x -= distance;
				currMapPoint.y -= distance;
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE + (distance - 1)
				));
				newSeeObjects.addAll(mapInfo.getObjects(
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE + (distance -1),
						this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
						this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE
				));
				break;
		}

		for (BaseObject object : newSeeObjects) {
			this.see(object);
			object.see(this);
		}

		for (BaseObject object : disappearObjects) {
			this.lose(object);
			object.lose(this);
		}
	}

	/**
	 * 玩家进入地图, 但是坐标根据地图情况随机安排
	 *
	 * @param mapId
	 */
	public void enterMap(String mapId) {
		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(mapId);

		int      r        = new Random().nextInt(mapInfo.allCanWalkTileXY.size());
		int      v        = mapInfo.allCanWalkTileXY.get(r);
		MapPoint mapPoint = new MapPoint();
		mapPoint.mapId = mapId;
		mapPoint.x = NumUtil.getLowWord(v);
		mapPoint.y = NumUtil.getHighWord(v);

		enterMap(mapPoint);

	}

	public void enterMap(MapPoint mapPoint) {
		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(mapPoint.mapId);
		if (mapInfo == null)
			return;

		if (!mapInfo.canWalk(mapPoint)) {
			enterMap(mapPoint.mapId);
			return;
		}

		if (!this.currMapPoint.mapId.equals(mapPoint.mapId))
			leaveMap();

		this.currMapPoint = mapPoint;

		mapInfo.putObject(this);

		//找到视野中的物品
		List<BaseObject> objectsInView = mapInfo.getObjects(
				this.currMapPoint.x - Config.DEFAULT_VIEW_DISTANCE,
				this.currMapPoint.x + Config.DEFAULT_VIEW_DISTANCE,
				this.currMapPoint.y - Config.DEFAULT_VIEW_DISTANCE,
				this.currMapPoint.y + Config.DEFAULT_VIEW_DISTANCE);
		for (BaseObject baseObject : objectsInView) {
			//我看见你
			this.see(baseObject);
			//你也看见我
			baseObject.see(this);
		}
	}

	public int getFeature() {
		return NumUtil.makeLong(NumUtil.makeWord(race.id, (byte) 0), appr);
	}

	public int getFeatureEx() {
		return 0;
	}

	public int getStatus() {
		return 0;
	}

	/**
	 * 每一秒钟引擎会触发一下, 让它获得执行机会, 处理一些自身的变化
	 * 比如Npc自己变色, 玩家在经验房里增加经验, 中毒掉血等
	 */
	public abstract void onTick();

}
