package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.types.Ability;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

public class BaseObject {

	public String   name;
	/**
	 * 当前地图位置
	 */
	public MapPoint currMapPoint;
	/**
	 * 能力
	 */
	public Ability ability = new Ability();

	/**
	 * 在游戏中的标志, 退出游戏后设置为false
	 * 各个系统的引用在遍历时 检查 如果为false则从各自系统的集合中删除
	 */
	public boolean inGame = false;

	public Direction direction = Direction.DOWN;

	/**
	 * 最后动作时间, 用来防止加速
	 */
	public long lastActionTime = 0;

	public boolean checkAndIncActionTime(int interval){
		long now = NumUtil.getTickCount();
		if(now - lastActionTime < interval){
			return false;
		}
		lastActionTime = now;
		return true;
	}
}
