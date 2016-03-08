package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Config;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class ActionHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {
		if(packet.p2 < 0 || packet.p2 >= Direction.values().length)
			return;

		Direction direction = Direction.values()[packet.p2];

		if(!player.checkAndIncActionTime(Config.PLAYER_ACTION_INTERVAL_TIME)){
			session.sendPacket(new ServerPacket.ActionStatus(ServerPacket.ActionStatus.Result.Fail));
			return;
		}

		boolean isSucc = false;
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

		if(isSucc)
			session.sendPacket(new ServerPacket.ActionStatus(ServerPacket.ActionStatus.Result.Good));
		else
			session.sendPacket(new ServerPacket.ActionStatus(ServerPacket.ActionStatus.Result.Fail));
	}
}
