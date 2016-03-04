package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.objects.Merchant;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.utils.ConfigFileLoader;
import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class MerchantEngine {

	private static final Logger logger          = LogManager.getLogger();
	private static final String NPC_CONFIG_FILE = "Envir/Merchant.cfg";
	private static final String NPC_SCRIPT_DIR  = "Scripts/Merchant";

	private static Map<String, Merchant> nameIndexMap;


	public static synchronized void reload() throws Exception {
		Map<String, Merchant> loadNpcs = new HashMap<>();
		loadConfig(loadNpcs);

		// 保证读出来的无异常再替换原有的;
		MerchantEngine.nameIndexMap = loadNpcs;

		for (Merchant merchat : loadNpcs.values()) {
			MapEngine.enter(merchat, merchat.currMapPoint);
		}
	}

	private static void loadConfig(Map<String, Merchant> loadNpcs) throws Exception {
		GroovyScriptEngine engine = new GroovyScriptEngine(NPC_SCRIPT_DIR);

		for (StringTokenizer tokenizer : ConfigFileLoader.load(NPC_CONFIG_FILE, 2)) {

			Merchant merchant = new Merchant();
			merchant.name = (String) tokenizer.nextElement();
			merchant.currMapPoint = new MapPoint();
			merchant.currMapPoint.mapName = (String) tokenizer.nextElement();
			merchant.currMapPoint.x = Short.parseShort((String) tokenizer.nextElement());
			merchant.currMapPoint.y = Short.parseShort((String) tokenizer.nextElement());
			merchant.scriptName = tokenizer.nextElement() + ".groovy";

			logger.debug("加载NPC {} 的脚本 {}", merchant.name, merchant.scriptName);
			Class groovyClass = engine.loadScriptByName(merchant.scriptName);
			merchant.scriptInstance = (GroovyObject) groovyClass.newInstance();

			loadNpcs.put(merchant.name, merchant);
		}
	}

	public static Object exce(String npcName, String fn, Object... args) throws Exception {
		return nameIndexMap.get(npcName).scriptInstance.invokeMethod(fn, args);
	}

}
