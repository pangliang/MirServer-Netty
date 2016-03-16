package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.engine.CmdEngine;
import com.zhaoxiaodan.mirserver.gameserver.GameClientPackets;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class SayHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		GameClientPackets.Say request = (GameClientPackets.Say) packet;

		String msg = request.msg.trim();
		if(msg.length() <= 0)
			return;
		if('@' == msg.charAt(0) && msg.length() >1){
			CmdEngine.exce(msg,player);
		}


	}
}
