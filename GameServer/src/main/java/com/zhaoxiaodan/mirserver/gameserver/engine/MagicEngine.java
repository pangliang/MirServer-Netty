package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.StdMagic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagicEngine {

	private static Map<String, StdMagic> magicNames = new HashMap<>();

	public synchronized static void reload() throws Exception {
		List<StdMagic>        list       = new DB().begin().query(StdMagic.class);
		Map<String, StdMagic> magicNames = new HashMap<>();
		for (StdMagic magic : list) {
			magicNames.put(magic.name, magic);

			ScriptEngine.loadScript(magic.scriptName);
		}

		MagicEngine.magicNames = magicNames;
	}

	public static StdMagic getStdMagicByName(String name) {
		return magicNames.get(name);
	}
}
