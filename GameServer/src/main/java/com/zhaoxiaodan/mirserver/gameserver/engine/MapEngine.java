package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.objects.BaseObject;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.ConfigFileLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class MapEngine {

	private static Logger logger = LogManager.getLogger();

	private static final String MAP_CONFIG_FILE        = "Envir/MapInfo.cfg";
	private static final String MINIMAP_CONFIG_FILE    = "Envir/MiniMap.cfg";
	private static final String STARTPOINT_CONFIG_FILE = "Envir/StartPoint.cfg";

	private        static Map<String, MapInfo> mapList    = null;
	private        static MapPoint             startPoint = null;

	private static class MapInfo {
		public String mapName;
		public String mapDescription;
		public String miniMapName;

		public final Map<Integer,BaseObject> objects = new ConcurrentHashMap<>();
		public final Map<Integer,Player> players = new ConcurrentHashMap<>();
	}

	/**
	 * 放置除玩家以外的对象
	 * @param object
	 * @param mapPoint
	 */
	public static void enter(BaseObject object, MapPoint mapPoint){
		MapInfo mapInfo = mapList.get(mapPoint.mapName);
		if(mapInfo == null){
			logger.error("{} 进入地图, 但是地图 {} 的信息不存在!",object.name,mapPoint.mapName);
			return;
		}

		object.currMapPoint = mapPoint;
		broadcast(object.currMapPoint,new ServerPacket.Turn(object));
		mapInfo.objects.put(object.inGameId,object);
	}

	/**
	 * 玩家进入地图, 但是坐标根据地图情况随机安排
	 * @param player
	 * @param mapName
	 */
	public static void enter(Player player, String mapName){
		//Todo 地图情况读取
	}

	/**
	 * 往家进入地图
	 * @param player
	 * @param mapPoint
	 */
	public static void enter(Player player, MapPoint mapPoint ){

		MapInfo mapInfo = mapList.get(mapPoint.mapName);
		if(mapInfo == null)
			return;

		player.currMapPoint = mapPoint;

		// 新图信息发给玩家
		player.session.writeAndFlush(new ServerPacket.NewMap(player.inGameId, player.currMapPoint.x, player.currMapPoint.y, (short) 0, player.currMapPoint.mapName));
		player.session.writeAndFlush(new ServerPacket.MapDescription(-1, mapInfo.mapDescription));

		player.session.writeAndFlush(new ServerPacket.Logon(player));
		player.session.writeAndFlush(new ServerPacket.FeatureChanged(player));

		player.session.writeAndFlush(new ServerPacket.PlayerAbility(player.gold, player.gameGold, player.job, player.ability));

		// 是否安全区
		player.session.writeAndFlush(new ServerPacket(2, Protocol.SM_AREASTATE, (byte) 0, (byte) 0, (byte) 0));

		// 广播进入地图
		broadcast(mapPoint,new ServerPacket.Turn(player));

		//地图上的object 发给玩家
		for(BaseObject objInMap : mapInfo.objects.values()){
			player.session.writeAndFlush(new ServerPacket.Turn(objInMap));
		}

		mapInfo.players.put(player.inGameId,player);
	}

	/**
	 * 离开原来的地图
	 * @param object
	 */
	private static void leave(BaseObject object){
		//删掉原来在的地图
		MapInfo currMapInfo;
		if(object.currMapPoint.mapName != null && (currMapInfo = mapList.get(object.currMapPoint.mapName)) != null){
			if(object instanceof Player)
				currMapInfo.players.remove(object.inGameId);
			else
				currMapInfo.objects.remove(object.inGameId);

			//TODO 广播离开地图
		}
	}

	/**
	 * 广播消息给当前地图的玩家
	 * @param mapPoint
	 * @param serverPacket
	 */
	public static void broadcast(MapPoint mapPoint, ServerPacket serverPacket){
		MapInfo mapInfo = mapList.get(mapPoint);
		if(mapInfo == null)
			return;

		for(Player player: mapInfo.players.values()){
			player.session.writeAndFlush(serverPacket);
		}
	}

	public static synchronized void reload() throws Exception {
		Map<String, MapInfo> maps = new HashMap<>();
		reloadMapInfo(maps);
		reloadMiniMap(maps);
		MapPoint startPoint = reloadStartPoint(maps);

		// 保证读出来的无异常再替换原有的;
		MapEngine.mapList = maps;
		MapEngine.startPoint = startPoint;
	}

	private static void reloadMapInfo(Map<String, MapInfo> maps) throws Exception {

		for (StringTokenizer tokenizer : ConfigFileLoader.load(MAP_CONFIG_FILE, 2)) {

			MapInfo info = new MapInfo();
			info.mapName = (String) tokenizer.nextElement();
			info.mapDescription = (String) tokenizer.nextElement();

			if (maps.containsKey(info.mapName)) {
				throw new Exception("地图名 " + info.mapName + " 的配置已经存在, 检查是否重复.");
			}

			maps.put(info.mapName, info);
		}
	}

	private static void reloadMiniMap(Map<String, MapInfo> maps) throws Exception {
		for (StringTokenizer tokenizer : ConfigFileLoader.load(MINIMAP_CONFIG_FILE, 2)) {
			String fileName = (String) tokenizer.nextElement();
			if (!maps.containsKey(fileName))
				throw new Exception("小地图对应的地图" + fileName + " 在地图配置中不存在, 先在地图配置文件" + MAP_CONFIG_FILE + "中添加");

			maps.get(fileName).miniMapName = (String) tokenizer.nextElement();
		}
	}

	private static MapPoint reloadStartPoint(Map<String, MapInfo> maps) throws Exception {
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

	public static MapInfo getMapInfo(String mapFileNamp) {
		return mapList.get(mapFileNamp);
	}

	public static MapPoint getStartPoint() {
		return startPoint;
	}
}
