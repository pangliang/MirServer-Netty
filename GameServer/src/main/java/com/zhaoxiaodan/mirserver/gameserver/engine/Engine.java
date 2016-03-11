package com.zhaoxiaodan.mirserver.gameserver.engine;

public class Engine {

	public static synchronized void init() throws Exception {
		CmdEngine.init();
		MessageEngine.reload();
		MapEngine.reload();
		MagicEngine.reload();
		ItemEngine.reload();
		MerchantEngine.reload();
		MonsterEngine.reload();
		MonsterEngine.start();
	}

	public static synchronized void reload() throws Exception {
		CmdEngine.init();
		MessageEngine.reload();
		MapEngine.reload();
		MagicEngine.reload();
		ItemEngine.reload();
		MerchantEngine.reload();
		MonsterEngine.reload();
	}
}
