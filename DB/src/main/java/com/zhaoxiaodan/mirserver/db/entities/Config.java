package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.DB;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

public class Config {

	/**
	 * 用户动作间隔, 防止加速
	 */
	public static int    PLAYER_ACTION_INTERVAL_TIME            = 200;
	public static int    OBJECT_SPEED_BASE                      = 3000;
	public static String GAME_GOLD_NAME                         = "游戏金币";
	public static String GAME_POINT_NAME                        = "游戏点数";
	public static int    DEFAULT_VIEW_DISTANCE                  = 5;
	public static int    EXP_MULTIPLE                           = 10;
	public static int    MONSTER_DROP_RATE_BASE                 = 10000;
	public static int    MONSTER_BONES_DISAPPEAR_TIME           = 10 * 1000;
	public static int    ENGINE_TICK_INTERVAL_TIME              = 1000;
	public static int    PLAYER_CHECK_PICKUP_ITEM_INTERVAL_TIME = 1000;
	public static int    DROP_ITEM_PROTECT_TIME                 = 30 * 1000;
	public static int    DROP_ITEM_LIFE_TIME                    = 3 * 60 * 1000;
	public static int    COMPOSE_REQURE_NUMBER                  = 3;
	public static int    COMPOSE_REQURE_GOLD                    = 10000;

	@Entity
	@Table(name = "Config")
	public static class ConfigEntity {

		public String key;
		public Object value;
	}

	public static void reload() throws Exception {

		List configEntityList = DB.query(ConfigEntity.class);

		// TODO: 读取配置
	}

}
