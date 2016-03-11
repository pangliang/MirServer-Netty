package com.zhaoxiaodan.mirserver.gameserver.engine;

import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptEngine {

	private static       Logger logger                = LogManager.getLogger();
	private static final String DEFUALT_SCRIPT_DIR    = "Scripts";
	private static final String DEFUALT_SCRIPT_SUFFIX = ".groovy";

	private static Map<String, GroovyObject> scriptInstance = new ConcurrentHashMap<>();

	public static synchronized void reload() throws Exception {
		for(String scriptName : scriptInstance.keySet()){
			loadScript(scriptName, true);
		}
	}

	public static synchronized void loadScript(String scriptName) throws Exception{
		loadScript(scriptName, false);
	}

	public static synchronized void loadScript(String scriptName, boolean flush) throws Exception {
		if (!flush && scriptInstance.containsKey(scriptName))
			return;

		logger.debug("加载脚本: {}", scriptName);
		GroovyScriptEngine engine      = new GroovyScriptEngine(DEFUALT_SCRIPT_DIR);
		Class              groovyClass = engine.loadScriptByName(scriptName + DEFUALT_SCRIPT_SUFFIX);
		GroovyObject       instance    = (GroovyObject) groovyClass.newInstance();
		scriptInstance.put(scriptName, instance);
	}

	public static Object exce(String scriptName, String fn, Object... args) {
		if (!scriptInstance.containsKey(scriptName)) {
			logger.error("脚本模块 {} 不存在", scriptName);
			return null;
		}
		try {
			return scriptInstance.get(scriptName).invokeMethod(fn, args);
		} catch (Exception e) {
			logger.error("脚本执行异常: {}", scriptName, e);
			return null;
		}
	}

}
