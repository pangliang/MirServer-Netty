package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class EatHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		ClientPacket.Eat request = (ClientPacket.Eat) packet;

		player.eat(request.playerItemInGameId);
	}
}
