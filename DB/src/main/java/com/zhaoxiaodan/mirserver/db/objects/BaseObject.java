package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.types.Color;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.db.types.Race;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseObject {

	public final int inGameId = NumUtil.newAtomicId();

	public String name      ;
	public Color  nameColor = Color.White;

	public Race race = Race.Animal;
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
