package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.objects.MapPoint;
import com.zhaoxiaodan.mirserver.utils.ConfigFileLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MapEngine {

	private static final String MAP_CONFIG_FILE        = "Envir/MapInfo.cfg";
	private static final String MINIMAP_CONFIG_FILE    = "Envir/MiniMap.cfg";
	private static final String STARTPOINT_CONFIG_FILE = "Envir/StartPoint.cfg";

	private        Map<String, MapInfo> maps       = null;
	private        MapPoint             startPoint = null;
	private static MapEngine            instance   = null;

	public class MapInfo {

		public String mapName;
		public String mapDescription;
		public String miniMapName;
	}

	private MapEngine() {

	}

	public synchronized static MapEngine getInstance() {
		if (null == instance) {
			instance = new MapEngine();
		}
		return instance;
	}

	public synchronized void reload() throws Exception {
		Map<String, MapInfo> maps = new HashMap<>();
		reloadMapInfo(maps);
		reloadMiniMap(maps);
		MapPoint startPoint = reloadStartPoint();

		// 保证读出来的无异常再替换原有的;
		this.maps = maps;
		this.startPoint = startPoint;
	}

	private void reloadMapInfo(Map<String, MapInfo> maps) throws Exception {

		for (StringTokenizer tokenizer : ConfigFileLoader.load(MAP_CONFIG_FILE, 2)) {

			MapInfo info = new MapInfo();
			info.mapName = (String) tokenizer.nextElement();
			info.mapDescription = (String) tokenizer.nextElement();

			if (maps.containsKey(info.mapName)) {
				throw new Exception("地图名 " + info.mapName + " 的配置已经存在, 检查是否重复.");
			}

			maps.put(info.mapName, info);
		}

		this.maps = maps;
	}

	private void reloadMiniMap(Map<String, MapInfo> maps) throws Exception {
		for (StringTokenizer tokenizer : ConfigFileLoader.load(MINIMAP_CONFIG_FILE, 2)) {
			String fileName = (String) tokenizer.nextElement();
			if (!maps.containsKey(fileName))
				throw new Exception("小地图对应的地图" + fileName + " 在地图配置中不存在, 先在地图配置文件" + MAP_CONFIG_FILE + "中添加");

			maps.get(fileName).miniMapName = (String) tokenizer.nextElement();
		}
	}

	private MapPoint reloadStartPoint() throws Exception {
		MapPoint startPoint = null;
		for (StringTokenizer tokenizer : ConfigFileLoader.load(STARTPOINT_CONFIG_FILE, 2)) {
			startPoint = new MapPoint();
			startPoint.mapName = (String) tokenizer.nextElement();
			if (!maps.containsKey(startPoint.mapName))
				throw new Exception("出生点所在地图" + startPoint.mapName + "在地图配置中不存在, 先在地图配置文件" + MAP_CONFIG_FILE + "中添加");

			startPoint.x = Short.parseShort((String) tokenizer.nextElement());
			startPoint.y = Short.parseShort((String) tokenizer.nextElement());

			break;
		}
		if (null == startPoint)
			throw new Exception("还没配置出生点, 在" + STARTPOINT_CONFIG_FILE + " 中配置");
		return startPoint;
	}

	public MapInfo getMapInfo(String mapFileNamp) {
		return maps.get(mapFileNamp);
	}

	public MapPoint getStartPoint() {
		return startPoint;
	}
}
