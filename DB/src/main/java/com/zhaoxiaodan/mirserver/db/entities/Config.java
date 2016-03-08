package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.DB;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

public class Config {

	/**
	 * 用户动作间隔, 防止加速
	 */
	public static int PLAYER_ACTION_INTERVAL_TIME = 500;

	public static String GAME_GOLD_NAME = "游戏金币";
	public static String GAME_POINT_NAME = "游戏点数";

	public static int DEFAULT_VIEW_DISTANCE = 5;

	@Entity
	@Table(name = "Config")
	public static class ConfigEntity{
		public String key;
		public Object value;
	}

	public static void reload() throws Exception{
		DB db = new DB();
		db.begin();

		List configEntityList = db.query(ConfigEntity.class);

		// TODO: 读取配置
	}

}
