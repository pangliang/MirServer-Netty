package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.Config;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class MoveActionHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {
		if(packet.p2 < 0 || packet.p2 >= Direction.values().length)
			return;

		if(!player.checkAndIncActionTime(Config.PLAYER_ACTION_INTERVAL_TIME)){
			session.writeAndFlush(new ServerPacket.Status(ServerPacket.Status.Result.Fail));
			return;
		}

		Direction direction = Direction.values()[packet.p2];

		switch (packet.protocol){
			case CM_WALK:
				player.currMapPoint.move(direction,(short)1);
				break;
			case CM_RUN:
				player.currMapPoint.move(direction,(short)2);
				break;
			case CM_TURN:
				player.direction = direction;
				ServerPacket p = new ServerPacket(Protocol.SM_TURN);
				p.recog = player.id;
				p.p1 = player.currMapPoint.x;
				p.p2 = player.currMapPoint.y;
				p.p3 = (short) player.direction.ordinal();
				session.context.writeAndFlush(p);
				break;
			default:
				break;
		}
		session.writeAndFlush(new ServerPacket.Status(ServerPacket.Status.Result.Good));
	}
}
