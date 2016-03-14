package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.entities.Config;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

public class Engine {

	private static final Logger  logger        = LogManager.getLogger();
	private static final Class[] engineClasses = new Class[]{
			CmdEngine.class,
			ScriptEngine.class,
			MessageEngine.class,
			MapEngine.class,
			MagicEngine.class,
			ItemEngine.class,
			MerchantEngine.class,
			MonsterEngine.class,
	};

	public static boolean running = true;

	public static synchronized void init() throws Exception{
		reload();
		startTickThread();
	}

	public static synchronized void reload() throws Exception {

		for (Class engineClazz : engineClasses) {
			String funName = "reload";
			try {
				Method reloadMethod = engineClazz.getDeclaredMethod(funName);
				reloadMethod.invoke(null);

				logger.debug("引擎 {} 加载完成",engineClazz.getSimpleName());

			} catch (NoSuchMethodException e) {
				logger.error("引擎 {} 没有实现 {} 方法 !", engineClazz.getSimpleName(), funName);
				throw e;
			} catch (SecurityException e) {
				logger.error("引擎 {} 没有实现 {} 方法不是public !", engineClazz.getSimpleName(), funName);
				throw e;
			}
		}


	}

	private static synchronized void startTickThread() throws Exception{
		for (Class engineClazz : engineClasses) {
			String funName = "onTick";
			try {
				Method onTickMethod = engineClazz.getDeclaredMethod(funName, long.class);
				new Thread() {
					@Override
					public void run() {
						while (running) {
							try {
								onTickMethod.invoke(null, NumUtil.getTickCount());
							} catch (Exception e) {
								logger.error("invoke onTick error",e);
							}

							try {
								Thread.sleep(Config.ENGINE_TICK_INTERVAL_TIME);
							} catch (InterruptedException e) {
								logger.error(e);
							}
						}
					}
				}.start();
				logger.debug("引擎 {} onTick 启动完成",engineClazz.getSimpleName());

			} catch (NoSuchMethodException e) {
				logger.error("引擎 {} 没有实现 {} 方法 !", engineClazz.getSimpleName(), funName);
			} catch (SecurityException e) {
				logger.error("引擎 {} 没有实现 {} 方法不是public !", engineClazz.getSimpleName(), funName);
			}
		}
	}
}
