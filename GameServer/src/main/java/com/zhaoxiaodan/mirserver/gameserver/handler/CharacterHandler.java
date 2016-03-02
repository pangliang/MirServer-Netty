package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public abstract class CharacterHandler extends Handler {
	public final void onPacket(Packet packet) throws Exception {
		Player player = (Player) session.get("player");
		if (null == player)
			throw new Exception("player not found for packet:" + packet.protocol);

		onPacket(packet, player);
	}

	public abstract void onPacket(Packet packet, Player player) throws Exception;
}
