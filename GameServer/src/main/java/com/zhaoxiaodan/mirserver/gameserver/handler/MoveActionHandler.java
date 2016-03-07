package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class MoveActionHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {
		if(packet.p2 < 0 || packet.p2 >= Direction.values().length)
			return;

		Direction direction = Direction.values()[packet.p2];

		switch (packet.protocol){
			case CM_WALK:
				player.walk(direction);
				break;
			case CM_RUN:
				player.run(direction);
				break;
			case CM_TURN:
				player.turn(direction);
				break;
			default:
				break;
		}
	}
}
