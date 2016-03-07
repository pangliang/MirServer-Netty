package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Config;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class MoveActionHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {
		if(packet.p2 < 0 || packet.p2 >= Direction.values().length)
			return;

		if(!player.checkAndIncActionTime(Config.PLAYER_ACTION_INTERVAL_TIME)){
			session.sendPacket(new ServerPacket.ActionStatus(ServerPacket.ActionStatus.Result.Fail));
			return;
		}

		Direction direction = Direction.values()[packet.p2];
		player.direction = direction;

		ServerPacket broadcast = null;

		switch (packet.protocol){
			case CM_WALK:
				player.currMapPoint.move(direction,(short)1);
				broadcast = new ServerPacket.Action(Protocol.SM_WALK,player);
				break;
			case CM_RUN:
				player.currMapPoint.move(direction,(short)2);
				broadcast = new ServerPacket.Action(Protocol.SM_RUN,player);
				break;
			case CM_TURN:
				player.direction = direction;
				broadcast = new ServerPacket.Turn(player);
				session.sendPacket(broadcast);
				break;
			default:
				break;
		}
		session.sendPacket(new ServerPacket.ActionStatus(ServerPacket.ActionStatus.Result.Good));
		MapEngine.broadcast(player.currMapPoint,broadcast);
	}
}
