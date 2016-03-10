package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.alibaba.fastjson.JSON;
import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.StdMonster;
import com.zhaoxiaodan.mirserver.db.objects.Monster;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.utils.ConfigFileLoader;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class MonsterEngine {

	private static final Logger logger             = LogManager.getLogger();
	private static final String MONSTER_GEN_CONFIG = "Envir/MonsterGen.cfg";

	private static Map<String, StdMonster> stdMonsterNames = new HashMap<>();
	private static List<RefreshGroup>      refreshGroups   = new ArrayList<>();

	private static class RefreshGroup {

		public int      refreshInterval;
		public long     lastRefreshTime;
		public MapPoint mapPoint;
		public String   monsterName;
		public int      scope;
		public int      amount;

		protected List<Monster> monsters = new ArrayList<>();
	}

	private static boolean running = false;

	public synchronized static void start() {
		if (running)
			return;
		running = true;
		new Thread() {
			@Override
			public void run() {
				while (running) {
					try {
						onTick();
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}


	private static void onTick() {
		for (RefreshGroup group : refreshGroups) {
			long now = NumUtil.getTickCount();
			if (now > group.lastRefreshTime + group.refreshInterval) {
				group.lastRefreshTime = now;
				try {
					refresh(group);
				} catch (Exception e) {
					logger.error("RefreshGroup error", e);
				}
			}

			for (Monster monster : group.monsters) {
				if (monster.objectsInView.size() > 0) {
					try {
						ScriptEngine.exce(monster.stdMonster.scriptName, "onTick", monster);
					} catch (Exception e) {
						logger.error("monster onTime error", e);
					}

				}
			}
		}


	}

	public synchronized static void refresh(String mapId) throws Exception {
		for (RefreshGroup group : refreshGroups) {
			if (group.mapPoint.mapId.equals(mapId))
				refresh(group);
		}
	}

	public synchronized static void reload() throws Exception {
		reloadStdMonster();
		reloadMonsterGenConfig();
		for (RefreshGroup group : refreshGroups) {
			refresh(group);
		}
	}

	private static void reloadStdMonster() throws Exception {
		List<StdMonster>        monsters     = new DB().begin().query(StdMonster.class);
		Map<String, StdMonster> monsterNames = new HashMap<>();
		for (StdMonster monster : monsters) {
			monsterNames.put(monster.name, monster);
		}

		MonsterEngine.stdMonsterNames = monsterNames;
	}

	private static void reloadMonsterGenConfig() throws Exception {

		for (RefreshGroup group : refreshGroups) {
			for (Monster monster : group.monsters)
				MonsterEngine.remove(monster);
		}

		List<RefreshGroup> groups = new ArrayList<>();
		for (StringTokenizer tokenizer : ConfigFileLoader.load(MONSTER_GEN_CONFIG, 7)) {
			RefreshGroup group = new RefreshGroup();
			groups.add(group);

			group.mapPoint = new MapPoint();
			group.mapPoint.mapId = (String) tokenizer.nextElement();
			group.mapPoint.x = Short.parseShort((String) tokenizer.nextElement());
			group.mapPoint.y = Short.parseShort((String) tokenizer.nextElement());
			group.monsterName = (String) tokenizer.nextElement();
			group.scope = Integer.parseInt((String) tokenizer.nextElement());
			group.amount = Integer.parseInt((String) tokenizer.nextElement());
			group.refreshInterval = Integer.parseInt((String) tokenizer.nextElement()) * 60 * 1000;  //分钟
		}

		refreshGroups = groups;
	}

	private static void refresh(RefreshGroup group) throws Exception {
		logger.debug("刷怪,{}", JSON.toJSONString(group));
		for (Monster monster : group.monsters)
			MonsterEngine.remove(monster);

		group.monsters = new ArrayList<>();

		Random random = new Random();
		for (int i = 0; i < group.amount; i++) {
			Monster monster;
			if (group.mapPoint.x == 0 && group.mapPoint.y == 0) {
				monster = createMonster(group.monsterName);
				monster.enterMap(group.mapPoint.mapId);
			} else {
				MapPoint mapPoint = new MapPoint();
				mapPoint.mapId = group.mapPoint.mapId;
				mapPoint.x = (short) (group.mapPoint.x - (group.scope / 2) + random.nextInt(group.scope));
				mapPoint.y = (short) (group.mapPoint.y - (group.scope / 2) + random.nextInt(group.scope));

				monster = createMonster(group.monsterName);
				monster.enterMap(mapPoint);
			}

			group.monsters.add(monster);
		}
	}

	private static void remove(Monster monster) {
		monster.leaveMap();
	}

	public static Monster createMonster(String monsterName) throws Exception {
		StdMonster stdMonster = stdMonsterNames.get(monsterName);
		if (null != stdMonster) {
			ScriptEngine.loadScript(stdMonster.scriptName);
			Monster monster = new Monster(stdMonster);
			return monster;
		} else {
			throw new Exception("StdMonster 不存在 : " + monsterName);
		}

	}
}
