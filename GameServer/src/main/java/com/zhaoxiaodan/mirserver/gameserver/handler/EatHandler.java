package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class EatHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		ClientPacket.Eat request = (ClientPacket.Eat) packet;

		if(player.eat(request.playerItemInGameId))
			player.session.sendPacket(new ServerPacket(Protocol.SM_EAT_OK));
		else
			player.session.sendPacket(new ServerPacket(Protocol.SM_EAT_FAIL));
	}
}
