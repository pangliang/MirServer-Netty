package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class QueryBagItemsHandler extends CharacterHandler {


	@Override
	public void onPacket(Packet packet, Player player) throws Exception {
		for (PlayerItem item : player.items) {
			session.writeAndFlush(new ServerPacket.AddItem(player.id, item));
		}
	}
}
