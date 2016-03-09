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

	private static Map<Integer, Merchant> inGameIdIndexMap;


	public static synchronized void reload() throws Exception {

		if (MerchantEngine.inGameIdIndexMap != null && MerchantEngine.inGameIdIndexMap.size() > 0) {
			for (Merchant merchant : MerchantEngine.inGameIdIndexMap.values()) {
				merchant.leaveMap();
			}
		}

		Map<Integer, Merchant> loadNpcs = new HashMap<>();
		loadConfig(loadNpcs);

		// 保证读出来的无异常再替换原有的;
		MerchantEngine.inGameIdIndexMap = loadNpcs;

		for (Merchant merchat : loadNpcs.values()) {
			merchat.enterMap( merchat.currMapPoint);
		}
	}

	private static void loadConfig(Map<Integer, Merchant> loadNpcs) throws Exception {
		GroovyScriptEngine engine = new GroovyScriptEngine(NPC_SCRIPT_DIR);

		for (StringTokenizer tokenizer : ConfigFileLoader.load(NPC_CONFIG_FILE, 2)) {

			Merchant merchant = new Merchant();
			merchant.name = (String) tokenizer.nextElement();
			merchant.currMapPoint = new MapPoint();
			merchant.currMapPoint.mapId = (String) tokenizer.nextElement();
			merchant.currMapPoint.x = Short.parseShort((String) tokenizer.nextElement());
			merchant.currMapPoint.y = Short.parseShort((String) tokenizer.nextElement());
			String scriptName = tokenizer.nextElement() + ".groovy";

			logger.debug("加载NPC {} 的脚本 {}", merchant.name, scriptName);
			Class groovyClass = engine.loadScriptByName(scriptName);
			merchant.scriptInstance = (GroovyObject) groovyClass.newInstance();

			loadNpcs.put(merchant.inGameId, merchant);
		}
	}

	public static Merchant getMerchant(int inGameId) {
		return inGameIdIndexMap.get(inGameId);
	}


}
