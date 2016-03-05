package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class QueryBagItemsHandler extends PlayerHandler {


	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {
		for (PlayerItem item : player.items) {
			session.sendPacket(new ServerPacket.AddItem(player.inGameId, item));
		}
	}
}
