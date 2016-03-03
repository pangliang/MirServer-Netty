package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.objects.Npc;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.utils.ConfigFileLoader;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class NpcEngine {

	private static final Logger logger = LogManager.getLogger();
	private static final String NPC_CONFIG_FILE = "Envir/Npc.cfg";
	private static final String NPC_SCRIPT_DIR  = "Scripts/Npc";

	private static Map<String, Npc> npcs;


	public static synchronized void reload() throws Exception {
		Map<String, Npc> loadNpcs = new HashMap<>();
		loadConfig(loadNpcs);

		// 保证读出来的无异常再替换原有的;
		NpcEngine.npcs = loadNpcs;
	}

	private static void loadConfig(Map<String, Npc> loadNpcs) throws Exception {
		GroovyScriptEngine engine = new GroovyScriptEngine(NPC_SCRIPT_DIR);

		for (StringTokenizer tokenizer : ConfigFileLoader.load(NPC_CONFIG_FILE, 2)) {

			Npc npc = new Npc();
			npc.name = (String) tokenizer.nextElement();
			npc.mapPoint = new MapPoint();
			npc.mapPoint.mapName = (String) tokenizer.nextElement();
			npc.mapPoint.x = Short.parseShort((String) tokenizer.nextElement());
			npc.mapPoint.y = Short.parseShort((String) tokenizer.nextElement());
			npc.scriptName = tokenizer.nextElement() + ".groovy";

			logger.debug("加载NPC {} 的脚本 {}",npc.name, npc.scriptName);
			Class groovyClass = engine.loadScriptByName(npc.scriptName);
			npc.scriptInstance = (GroovyObject) groovyClass.newInstance();

			loadNpcs.put(npc.name,npc);
		}
	}

	public static Object exce(String npcName,String fn,Object...args) throws Exception{
		return npcs.get(npcName).scriptInstance.invokeMethod(fn,args);
	}

}
