package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem;
import com.zhaoxiaodan.mirserver.db.entities.StdItem;
import com.zhaoxiaodan.mirserver.db.types.Ability;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemEngine {

	private static Map<Integer, StdItem> itemIds   = new HashMap<>();
	private static Map<String, StdItem>  itemNames = new HashMap<>();

	public synchronized static void reload() throws Exception {
		List<StdItem>         itemList  = new DB().begin().query(StdItem.class);
		Map<Integer, StdItem> itemIds   = new HashMap<>();
		Map<String, StdItem>  itemNames = new HashMap<>();
		for (StdItem item : itemList) {
			itemIds.put(item.id, item);
			itemNames.put(item.attr.name, item);

			ScriptEngine.loadScript(item.scriptName);
		}

		ItemEngine.itemIds = itemIds;
		ItemEngine.itemNames = itemNames;
	}

	public static StdItem getStdItemById(int id) {
		return itemIds.get(id);
	}

	public static StdItem getStdItemByName(String name) {
		return itemNames.get(name);
	}

	public static void checkAbility(Ability ability, PlayerItem playerItem) {
		ScriptEngine.exce(playerItem.stdItem.scriptName, "checkAbility", ability, playerItem);
	}
}
