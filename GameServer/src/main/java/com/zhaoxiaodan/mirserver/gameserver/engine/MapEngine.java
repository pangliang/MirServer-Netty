package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.gameserver.objects.BaseObject;
import com.zhaoxiaodan.mirserver.gameserver.types.Direction;
import com.zhaoxiaodan.mirserver.gameserver.types.MapPoint;
import com.zhaoxiaodan.mirserver.utils.ConfigFileLoader;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapEngine {

	private static Logger logger = LogManager.getLogger();

	private static final String MAP_CONFIG_FILE        = "Envir/MapInfo.cfg";
	private static final String STARTPOINT_CONFIG_FILE = "Envir/StartPoint.cfg";
	private static final String MAP_FILES_DIR          = "Envir/Maps";

	/**
	 * 地图文件头大小
	 */
	private static final int MAP_FILE_HEADER_SIZE = 52;
	private static final int MAP_FILE_TILE_SIZE   = 12;

	private static Map<String, MapInfo> mapList    = null;
	private static MapPoint             startPoint = null;

	public static class Tile {

		public boolean canWalk;
		public final Map<Integer, BaseObject> objects = new ConcurrentHashMap<>();
	}

	public static class MapInfo {

		public short width;
		public short height;

		public String mapId;
		public String mapFileId;
		/**
		 * 地图文件里的地图标题
		 */
		public String mapTitle;
		/**
		* 自己配置的地图名
		*/
		public String mapDescription;
		public String miniMapId;

		public boolean canThroughPlayer  = true;
		public boolean canThroughMonster = false;
		public boolean canThroughNpc     = true;

		/**
		 * 服务器只关心当前格子是否能走
		 */
		public Tile[][] tiles;
		//空间换时间, 方便取地图上可以行走的随机点
		public List<Integer> allCanWalkTileXY = new ArrayList<>();

		public void putObject(BaseObject object) {
			if (object.currMapPoint.x >= this.width ||
					object.currMapPoint.y >= this.height)
				return;

			this.tiles[object.currMapPoint.x][object.currMapPoint.y].objects.put(object.inGameId, object);
		}

		public void removeObject(BaseObject object) {
			if (object.currMapPoint.x >= this.width ||
					object.currMapPoint.y >= this.height)
				return;
			if (!this.tiles[object.currMapPoint.x][object.currMapPoint.y].objects.containsKey(object.inGameId))
				logger.error("当前tile不存在object:id:{},x:{},y:{}", object.inGameId, object.currMapPoint.x, object.currMapPoint.y);
			this.tiles[object.currMapPoint.x][object.currMapPoint.y].objects.remove(object.inGameId);
		}

		public void objectMove(BaseObject object, MapPoint from, MapPoint to) {
			if (object.currMapPoint.x >= this.width ||
					object.currMapPoint.y >= this.height)
				return;

			this.tiles[from.x][from.y].objects.remove(object.inGameId);
			this.tiles[to.x][to.y].objects.put(object.inGameId, object);
		}

		public boolean canWalk(MapPoint mapPoint) {

			if (mapPoint.x < 0 || mapPoint.x >= this.width || mapPoint.y < 0 || mapPoint.y >= this.height)
				return false;

			return tiles[mapPoint.x][mapPoint.y].canWalk;
		}

		public List<BaseObject> getObjects(int startX, int endX, int startY, int endY) {
			List<BaseObject> objects = new ArrayList<>();
			if (startX > endX || startY > endY) {
				logger.error("start > end ,{},{},{},{}", startX, endX, startY, endY);
				return objects;
			}
			startX = startX < 0 ? 0 : startX;
			startY = startY < 0 ? 0 : startY;
			endX = endX > this.width ? this.width : endX;
			endY = endY > this.height ? this.height : endY;

			for (int x = startX; x <= endX; x++) {
				for (int y = startY; y <= endY; y++) {
					objects.addAll(this.tiles[x][y].objects.values());
				}
			}

			return objects;
		}

		public List<BaseObject> getObjects(MapPoint point) {
			List<BaseObject> objects = new ArrayList<>();
			if (point.x >= this.width || point.y >= this.height)
				return objects;
			objects.addAll(this.tiles[point.x][point.y].objects.values());
			return objects;
		}

		public List<BaseObject> getObjectsOnLine(MapPoint point, Direction direction, int distance, int len) {
			List<BaseObject> objects = new ArrayList<>();
			int              diffX   = 0;
			int              diffY   = 0;
			switch (direction) {
				case UP:
					diffX = 0;
					diffY = -1;
					break;
				case UPRIGHT:
					diffX = 1;
					diffY = -1;
					break;
				case RIGHT:
					diffX = 1;
					diffY = 0;
					break;
				case DOWNRIGHT:
					diffX = 1;
					diffY = 1;
					break;
				case DOWN:
					diffX = 0;
					diffY = 1;
					break;
				case DOWNLEFT:
					diffX = -1;
					diffY = 1;
					break;
				case LEFT:
					diffX = -1;
					diffY = 0;
					break;
				case UPLEFT:
					diffX = -1;
					diffY = -1;
					break;
			}

			int x = point.x + distance * diffX;
			int y = point.y + distance * diffY;

			for (int i = 0; i < len; i++) {
				if (x < 0 || x >= width || y < 0 || y >= height)
					break;
				objects.addAll(this.tiles[x][y].objects.values());

				x += diffX;
				y += diffY;
			}

			return objects;
		}

		public void loadMapFile() throws Exception {

			FileInputStream in = null;
			try {
				String filename = MAP_FILES_DIR + "/" + mapFileId + ".map";
				try {
					in = new FileInputStream(filename);
				} catch (FileNotFoundException e) {
					throw new FileNotFoundException("地图文件找不到: " + filename);
				}
				byte[] buf = new byte[MAP_FILE_HEADER_SIZE];
				if (in.read(buf, 0, MAP_FILE_HEADER_SIZE) != MAP_FILE_HEADER_SIZE)
					throw new Exception("地图文件头大小不正确,文件:" + filename);

				width = NumUtil.readShort(buf, 0, true);
				height = NumUtil.readShort(buf, 2, true);
				mapTitle = new String(buf, 4, 16);
				logger.debug("读取地图mapId {}: {}, width:{}, height:{}", mapId, filename, width, height);
				tiles = new Tile[width][height];

				for (short x = 0; x < width; x++) {
					for (short y = 0; y < height; y++) {
						if (in.read(buf, 0, MAP_FILE_TILE_SIZE) != MAP_FILE_TILE_SIZE)
							throw new Exception("地图文件tile大小不正确,文件,x,y:" + filename + "," + x + "," + y);
						short floorImg = NumUtil.readShort(buf, 0, true);
						//最高位是1 则不能站人
						boolean canWalk = !((floorImg & 0x8000) == 0x8000);
						Tile    tile    = new Tile();
						tile.canWalk = canWalk;
						tiles[x][y] = tile;

						if (canWalk)
							allCanWalkTileXY.add(NumUtil.makeLong(x, y));
					}
				}
			} finally {
				if (null != in)
					in.close();
			}


		}
	}

	public static synchronized void onTick(long now) {
		for (MapInfo mapInfo : mapList.values()) {
			for (Tile[] tiles : mapInfo.tiles) {
				for (Tile tile : tiles) {
					for (BaseObject object : tile.objects.values())
						try {
							object.onTick(now);
						} catch (Exception e) {
							logger.error("onTick error", null, e);
						}

				}
			}
		}
	}

	public static synchronized void reload() throws Exception {
		Map<String, MapInfo> maps = new HashMap<>();
		reloadMapInfo(maps);
		MapPoint startPoint = reloadStartPoint(maps);

		// 保证读出来的无异常再替换原有的;
		MapEngine.mapList = maps;
		MapEngine.startPoint = startPoint;
	}

	private static void reloadMapInfo(Map<String, MapInfo> maps) throws Exception {

		for (StringTokenizer tokenizer : ConfigFileLoader.load(MAP_CONFIG_FILE, 3)) {

			MapInfo info = new MapInfo();
			info.mapFileId = (String) tokenizer.nextElement();
			info.mapId = (String) tokenizer.nextElement();
			info.miniMapId = (String) tokenizer.nextElement();

			if (maps.containsKey(info.mapId)) {
				throw new Exception("地图名 " + info.mapId + " 的配置已经存在, 检查是否重复.");
			}

			info.loadMapFile();

			maps.put(info.mapId, info);
		}
	}

	private static MapPoint reloadStartPoint(Map<String, MapInfo> maps) throws Exception {
		MapPoint startPoint = null;
		for (StringTokenizer tokenizer : ConfigFileLoader.load(STARTPOINT_CONFIG_FILE, 2)) {
			startPoint = new MapPoint();
			startPoint.mapId = (String) tokenizer.nextElement();
			if (!maps.containsKey(startPoint.mapId))
				throw new Exception("出生点所在地图" + startPoint.mapId + "在地图配置中不存在, 先在地图配置文件" + MAP_CONFIG_FILE + "中添加");

			startPoint.x = Short.parseShort((String) tokenizer.nextElement());
			startPoint.y = Short.parseShort((String) tokenizer.nextElement());

			break;
		}
		if (null == startPoint)
			throw new Exception("还没配置出生点, 在" + STARTPOINT_CONFIG_FILE + " 中配置");
		return startPoint;
	}

	public static MapInfo getMapInfo(String mapId) {
		return mapList.get(mapId);
	}

	public static MapPoint getStartPoint() {
		return startPoint;
	}
}
