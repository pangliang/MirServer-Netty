package com.zhaoxiaodan.mirserver.gameserver.engine;

import groovy.lang.GroovyObject;
import groovy.util.GroovyScriptEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ScriptEngine {

	private static Logger logger = LogManager.getLogger();
	private static final String DEFUALT_SCRIPT_DIR = "Scripts";

	private static Map<Module, GroovyObject> modules     = new HashMap<>();

	public enum Module {
		Character("Player.groovy");

		String scriptName;
		Module(String scriptName) {
			this.scriptName = scriptName;
		}
	}

	public static synchronized void reload() throws Exception {
		GroovyScriptEngine engine = new GroovyScriptEngine(DEFUALT_SCRIPT_DIR);
		for (Module module : Module.values()) {
			logger.debug("load script {}", module.scriptName);
			Class groovyClass = engine.loadScriptByName(module.scriptName);
			GroovyObject instance = (GroovyObject) groovyClass.newInstance();
			modules.put(module,instance);
		}
	}

	public static Object exce(Module module,String fn,Object...args) throws Exception{
		if(!modules.containsKey(module))
			throw new Exception("脚本模块 "+module.name()+" 不存在");

		return modules.get(module).invokeMethod(fn,args);
	}

}
