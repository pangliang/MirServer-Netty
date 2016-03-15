package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.alibaba.fastjson.JSON;
import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.Config;
import com.zhaoxiaodan.mirserver.db.entities.StdMonster;
import com.zhaoxiaodan.mirserver.db.objects.Monster;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.utils.ConfigFileLoader;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MonsterEngine {

	private static final Logger logger              = LogManager.getLogger();
	private static final String MONSTER_GEN_CONFIG  = "Envir/MonsterGen.cfg";
	private static final String MONSTER_DROP_CONFIG = "Envir/MonsterDrop.cfg";

	private static Map<String, StdMonster>        stdMonsterNames;
	private static List<RefreshGroup>             refreshGroups;
	private static Map<String, List<MonsterDrop>> monsterDrops;

	private static class RefreshGroup {

		public int      refreshInterval;
		public long     lastRefreshTime;
		public MapPoint mapPoint;
		public String   monsterName;
		public int      scope;
		public int      amount;

		protected Map<Integer, Monster> monsters = new ConcurrentHashMap<>();
	}

	private static class MonsterDrop {

		public final String stdItemName;
		public final int    rate;

		private MonsterDrop(String stdItemName, int rate) {
			this.stdItemName = stdItemName;
			this.rate = rate;
		}
	}

	public static void onTick(long now) {
		for (RefreshGroup group : refreshGroups) {
			if (now > group.lastRefreshTime + group.refreshInterval) {
				group.lastRefreshTime = now;
				try {
					refresh(group);
				} catch (Exception e) {
					logger.error("RefreshGroup error", e);
				}
			}
		}
	}

	public synchronized static void refresh(String mapId) throws Exception {
		for (RefreshGroup group : refreshGroups) {
			if (group.mapPoint.mapId.equals(mapId)) {
				refresh(group);
			}

		}
	}

	public synchronized static void reload() throws Exception {
		reloadStdMonster();
		reloadMonsterGenConfig();
		for (RefreshGroup group : refreshGroups) {
			refresh(group);
		}

		reloadMonsterDropConfig();
	}

	private static void reloadStdMonster() throws Exception {
		List<StdMonster>        monsters     = DB.query(StdMonster.class);
		Map<String, StdMonster> monsterNames = new HashMap<>();
		for (StdMonster monster : monsters) {
			monsterNames.put(monster.name, monster);
		}

		MonsterEngine.stdMonsterNames = monsterNames;
	}

	private static void reloadMonsterGenConfig() throws Exception {

		if(refreshGroups != null){
			for(RefreshGroup group: refreshGroups){
				for (Monster monster : group.monsters.values()) {
					monster.leaveMap();
				}
			}
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

		for (Monster monster : group.monsters.values()) {
			monster.leaveMap();
		}
		group.monsters.clear();

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

			group.monsters.put(monster.inGameId, monster);
		}
	}

	public static void reloadMonsterDropConfig() throws Exception {
		Map<String, List<MonsterDrop>>     map        = new HashMap<>();
		Map<String, List<StringTokenizer>> fileResult = ConfigFileLoader.loadIni(MONSTER_DROP_CONFIG, 2);
		for (String sectionName : fileResult.keySet()) {
			List<MonsterDrop> monsterDrops = new ArrayList<>();
			map.put(sectionName, monsterDrops);
			for (StringTokenizer stringTokenizer : fileResult.get(sectionName)) {
				MonsterDrop monsterDrop = new MonsterDrop((String) stringTokenizer.nextElement(), Integer.parseInt((String) stringTokenizer.nextElement()));
				monsterDrops.add(monsterDrop);
			}
		}

		MonsterEngine.monsterDrops = map;
	}

	public static List<String> getMonsterDrops(String monsterName) {
		List<String> itemNames = new ArrayList<>();

		List<MonsterDrop> drops = monsterDrops.get(monsterName);
		if (drops != null) {
			for (MonsterDrop drop : drops) {
				int r = NumUtil.nextRandomInt(Config.MONSTER_DROP_RATE_BASE);
				if (r <= drop.rate)
					itemNames.add(drop.stdItemName);
			}
		}

		return itemNames;
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
