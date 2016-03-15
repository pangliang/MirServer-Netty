package com.zhaoxiaodan.mirserver.gameserver.engine;

import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptEngine {

	private static       Logger logger                = LogManager.getLogger();
	private static final String DEFUALT_SCRIPT_DIR    = "Scripts";
	private static final String DEFUALT_SCRIPT_SUFFIX = ".groovy";
	private static GroovyScriptEngine engine;

	private static Map<String, GroovyObject> scriptInstance = new ConcurrentHashMap<>();

	static {
		try {
			engine = new GroovyScriptEngine(DEFUALT_SCRIPT_DIR);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized void reload() throws Exception {
		engine = new GroovyScriptEngine(DEFUALT_SCRIPT_DIR);
		scriptInstance.clear();
	}

	public static synchronized void loadScript(String scriptName) throws Exception {
		loadScript(scriptName, false);
	}

	public static synchronized void loadScript(String scriptName, boolean flush) throws Exception {
		if (!flush && scriptInstance.containsKey(scriptName))
			return;

		logger.debug("加载脚本: {}", scriptName);

		Class        groovyClass = engine.loadScriptByName(scriptName + DEFUALT_SCRIPT_SUFFIX);
		GroovyObject instance    = (GroovyObject) groovyClass.newInstance();
		scriptInstance.put(scriptName, instance);
	}

	public static Object exce(String scriptName, String fn, Object... args) {

		try {
			if (!scriptInstance.containsKey(scriptName)) {
				logger.error("脚本模块 {} 不存在", scriptName);
				loadScript(scriptName);
			}

			return scriptInstance.get(scriptName).invokeMethod(fn, args);
		} catch (Exception e) {
			logger.error("脚本执行异常: {}", scriptName, e);
			return null;
		}
	}

}
