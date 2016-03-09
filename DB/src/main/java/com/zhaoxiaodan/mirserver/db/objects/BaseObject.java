package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.entities.Config;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.Color;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseObject {

	public final int inGameId = NumUtil.newAtomicId();

	public  short  hp;
	public  short  maxHp;
	public Color nameColor = Color.White;
	public byte light;
	public MapPoint                 currMapPoint  = new MapPoint();
	public Direction                direction     = Direction.DOWN;
	public Map<Integer, BaseObject> objectsInView = new ConcurrentHashMap<>();

	public abstract String getName();

	public boolean see(BaseObject object) {
		if (object == this)
			return false;

		if (!this.objectsInView.containsKey(object.inGameId)) {
			objectsInView.put(object.inGameId, object);
			return true;
		} else {
			return false;
		}
	}

	public void lose(BaseObject object) {
		if (object == this)
			return;
		objectsInView.remove(object.inGameId);
	}

	public void leaveMap() {
		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(this.currMapPoint.mapId);
		mapInfo.removeObject(this);

		for (BaseObject object : this.objectsInView.values()) {
			object.lose(this);
		}
	}

	public void broadcast(ServerPacket serverPacket) {
		for (BaseObject object : this.objectsInView.values()) {
			if (object != this && object instanceof Player) {
				((Player) object).session.sendPacket(serverPacket);
			}
		}
	}

	public boolean walk(Direction direction) {
		if (this.move(direction, (short) 1)) {
			broadcast(new ServerPacket.Action(Protocol.SM_WALK, this));
			return true;
		} else
			return false;
	}

	public boolean run(Direction direction) {
		if (this.move(direction, (short) 2)) {
			broadcast(new ServerPacket.Action(Protocol.SM_RUN, this));
			return true;
		} else
			return false;
	}

	public boolean turn(Direction direction) {
		this.direction = direction;
		broadcast(new ServerPacket.Turn(this));
		return true;
	}

	public boolean move(Direction direction, short distance) {
		this.direction = direction;
		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(currMapPoint.mapId);

		MapPoint fromPoint = this.currMapPoint.clone();

		if (mapInfo.getObjectsOnLine(fromPoint, direction, 1, distance).size() > 0)
			return false;

		MapPoint toPoint = this.currMapPoint.clone();
		toPoint.move(direction, distance);
		mapInfo.objectMove(this, fromPoint, toPoint);

		//TODO 这个地方有优化的空间, 没必要整个屏幕扫描, 扫描边缘就可以了
		List<BaseObject> objectsInView = mapInfo.getObjects(
				toPoint.x - Config.DEFAULT_VIEW_DISTANCE,
				toPoint.x + Config.DEFAULT_VIEW_DISTANCE,
				toPoint.y - Config.DEFAULT_VIEW_DISTANCE,
				toPoint.y + Config.DEFAULT_VIEW_DISTANCE
		);

		for (BaseObject object : objectsInView) {
			this.see(object);
			object.see(this);
		}

		for (BaseObject object : this.objectsInView.values()) {
			if (object.currMapPoint == null
					|| (!toPoint.mapId.equals(object.currMapPoint.mapId))
					|| Math.abs(object.currMapPoint.x - toPoint.x) > Config.DEFAULT_VIEW_DISTANCE
					|| Math.abs(object.currMapPoint.y - toPoint.y) > Config.DEFAULT_VIEW_DISTANCE
					) {
				this.lose(object);
				object.lose(this);
			}
		}

		this.currMapPoint = toPoint;

		return true;
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

	public boolean hit(Direction direction) {

		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(this.currMapPoint.mapId);
		for (BaseObject object : mapInfo.getObjectsOnLine(this.currMapPoint, direction, 1, 1)) {
			object.damage(this, getPower());
		}

		broadcast(new ServerPacket(this.inGameId, Protocol.SM_HIT, this.currMapPoint.x, this.currMapPoint.y, (short) direction.ordinal()));
		return true;
	}

	public void damage(BaseObject source, short power) {
		this.hp = (short) (this.hp - power);
		if (this.hp <= 0) {
			broadcast(new ServerPacket(this.inGameId, Protocol.SM_DEATH, this.currMapPoint.x, this.currMapPoint.y, (short) direction.ordinal()));
		} else {
			ServerPacket packet = new ServerPacket.Struck(this.inGameId, this.hp, this.maxHp, power);
			broadcast(packet);
		}
	}

	public abstract int getFeature();

	public abstract short getFeatureEx();

	public abstract int getStatus();

	public abstract short getPower();

	/**
	 * 每一秒钟引擎会触发一下, 让它获得执行机会, 处理一些自身的变化
	 * 比如Npc自己变色, 玩家在经验房里增加经验, 中毒掉血等
	 */
	public abstract void onTick();
}
