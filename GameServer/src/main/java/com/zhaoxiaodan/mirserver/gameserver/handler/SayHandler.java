package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SayHandler extends PlayerHandler {


	static int   r = 0;
	static short x = 280;
	static short y = 610;

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		ClientPacket.Say request = (ClientPacket.Say) packet;

		String msg = request.msg.trim();
		if(msg.length() < 0)
			return;
		if('@' == msg.charAt(0) && msg.length() >1){
			StringTokenizer tokenizer = new StringTokenizer(msg);
			String cmd = ((String)tokenizer.nextElement()).substring(1);
			List<String>    args      = new ArrayList<>();
			while(tokenizer.hasMoreElements())
				args.add((String) tokenizer.nextElement());

			ScriptEngine.exce(ScriptEngine.Module.Cmd, cmd, player, args);
		}


	}
}
