package com.zhaoxiaodan.mirserver.gameserver.objects;

import com.zhaoxiaodan.mirserver.gameserver.entities.Config;
import com.zhaoxiaodan.mirserver.gameserver.types.Direction;
import com.zhaoxiaodan.mirserver.gameserver.types.MapPoint;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseObject {

	public final int inGameId = NumUtil.newAtomicId();

	public MapPoint currMapPoint;

	public abstract String getName();

	public Map<Integer, BaseObject> objectsInView  = new ConcurrentHashMap<>();
	public Map<String, Long>        lastActionTime = new ConcurrentHashMap<>();

	public Direction directionTo(BaseObject target) {
		int diffX = target.currMapPoint.x - this.currMapPoint.x;
		int diffY = target.currMapPoint.y - this.currMapPoint.y;

		if (diffX == 0 && diffY > 0)
			return Direction.DOWN;
		else if (diffX == 0 && diffY < 0)
			return Direction.UP;
		else if (diffX > 0 && diffY == 0)
			return Direction.RIGHT;
		else if (diffX < 0 && diffY == 0)
			return Direction.LEFT;
		else if (diffX > 0 && diffY > 0)
			return Direction.DOWNRIGHT;
		else if (diffX > 0 && diffY < 0)
			return Direction.UPRIGHT;
		else if (diffX < 0 && diffY > 0)
			return Direction.DOWNLEFT;
		else if (diffX < 0 && diffY < 0)
			return Direction.UPLEFT;
		else
			return Direction.LEFT;
	}

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

		if (this.currMapPoint != null && this.currMapPoint.mapId != null && !this.currMapPoint.mapId.equals(mapPoint.mapId))
			this.leaveMap();

		if (!mapInfo.canWalk(mapPoint)) {
			enterMap(mapPoint.mapId);
			return;
		}

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

	public boolean checkLastActionTime(String actionName, int actionSpeed, int addTime) {
		long now = NumUtil.getTickCount();
		if (!this.lastActionTime.containsKey(actionName)
				|| now > (this.lastActionTime.get(actionName) + (Config.OBJECT_SPEED_BASE * 1000 / actionSpeed))) {
			this.lastActionTime.put(actionName, now + NumUtil.nextRandomInt(addTime));
			return true;
		}

		return false;
	}

	/**
	 * 每一秒钟引擎会触发一下, 让它获得执行机会, 处理一些自身的变化
	 * 比如Npc自己变色, 玩家在经验房里增加经验, 中毒掉血等
	 *
	 * @param now
	 */
	public abstract void onTick(long now);
}
