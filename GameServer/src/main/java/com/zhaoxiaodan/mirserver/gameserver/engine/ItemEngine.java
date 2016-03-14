package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem;
import com.zhaoxiaodan.mirserver.db.entities.StdItem;
import com.zhaoxiaodan.mirserver.db.objects.DropItem;
import com.zhaoxiaodan.mirserver.db.types.Ability;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemEngine {

	private static final Logger               logger    = LogManager.getLogger();
	private static       Map<String, StdItem> itemNames = new HashMap<>();

	public synchronized static void reload() throws Exception {
		List<StdItem>         itemList  = DB.query(StdItem.class);
		Map<Integer, StdItem> itemIds   = new HashMap<>();
		Map<String, StdItem>  itemNames = new HashMap<>();
		for (StdItem item : itemList) {
			itemIds.put(item.id, item);
			itemNames.put(item.attr.name, item);

			ScriptEngine.loadScript(item.scriptName);
		}

		ItemEngine.itemNames = itemNames;
	}

	public static StdItem getStdItemByName(String name) {
		return itemNames.get(name);
	}

	public static void checkAbility(Ability ability, PlayerItem playerItem) {
		ScriptEngine.exce(playerItem.stdItem.scriptName, "checkAbility", ability, playerItem);
	}

	/**
	 * 掉落物品, 顺着周围摆放
	 *
	 * @param dropItemNames
	 * @param centerPoint
	 */
	public static void createDropItems(List<String> dropItemNames, MapPoint centerPoint, Player belongTo) {
		int       distance  = 1;
		Direction direction = Direction.RIGHT;
		for (String itemName : dropItemNames) {
			if (!itemNames.containsKey(itemName)) {
				logger.error("生成掉落物品错误, 物品名 {} 在标准物品StdItem中不存在", itemName);
				continue;
			}
			DropItem dropItem = new DropItem();
			dropItem.stdItem = itemNames.get(itemName);
			dropItem.bolongTo = belongTo;

			MapPoint dropPoint = centerPoint.clone();
			dropPoint.move(direction, (short) 1);

			dropItem.enterMap(dropPoint);

			direction = direction.turn(1);
			if (direction == Direction.RIGHT) // 转了一圈, 往外一格
				distance += 1;
		}
	}
}
