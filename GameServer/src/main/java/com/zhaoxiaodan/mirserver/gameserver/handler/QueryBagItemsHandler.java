package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.entities.PlayerItem;
import com.zhaoxiaodan.mirserver.gameserver.GameServerPackets;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class QueryBagItemsHandler extends PlayerHandler {


	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {
		for (PlayerItem item : player.items.values()) {
			session.sendPacket(new GameServerPackets.AddItem(player.inGameId, item));
		}
	}
}
