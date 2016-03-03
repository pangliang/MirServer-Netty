package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.StdItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemEngine {

	private static Map<Integer, StdItem> itemIds   = new HashMap<>();
	private static Map<String, StdItem>  itemNames = new HashMap<>();

	public synchronized static void reload() {
		List<StdItem>         itemList  = DB.query(StdItem.class);
		Map<Integer, StdItem> itemIds   = new HashMap<>();
		Map<String, StdItem>  itemNames = new HashMap<>();
		for (StdItem item : itemList) {
			itemIds.put(item.id, item);
			itemNames.put(item.attr.name, item);
		}

		ItemEngine.itemIds = itemIds;
		ItemEngine.itemNames = itemNames;
	}

	public static StdItem getStdItemById(int id){
		return itemIds.get(id);
	}

	public static StdItem getStdItemByName(String name){
		return itemNames.get(name);
	}
}
