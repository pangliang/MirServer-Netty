package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.gameserver.entities.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CmdEngine {

	public static final String SCRIPT_NAME = "Cmd";

	public static void reload() throws Exception {
		ScriptEngine.loadScript(SCRIPT_NAME);
	}

	public static void exce(String msg, Player player) throws Exception {
		StringTokenizer tokenizer = new StringTokenizer(msg);
		String          cmd       = ((String) tokenizer.nextElement()).substring(1);
		List<String>    args      = new ArrayList<>();
		while (tokenizer.hasMoreElements())
			args.add((String) tokenizer.nextElement());

		ScriptEngine.exce(SCRIPT_NAME, cmd, player, args);
	}
}
