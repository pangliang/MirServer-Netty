package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.Config;
import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.db.objects.Direction;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class MoveActionHandler extends CharacterHandler {

	@Override
	public void onPacket(Packet packet, Character character) throws Exception {
		if(packet.p2 < 0 || packet.p2 >= Direction.values().length)
			return;

		if(!character.checkAndIncActionTime(Config.PLAYER_ACTION_INTERVAL_TIME)){
			session.writeAndFlush(new ServerPacket.Status(ServerPacket.Status.Result.Fail));
			return;
		}

		Direction direction = Direction.values()[packet.p2];

		switch (packet.protocol){
			case CM_WALK:
				character.currMapPoint.move(direction,(short)1);
				break;
			case CM_RUN:
				character.currMapPoint.move(direction,(short)2);
				break;
			case CM_TURN:
				character.direction = direction;
				ServerPacket p = new ServerPacket(Protocol.SM_TURN);
				p.recog = character.id;
				p.p1 = character.currMapPoint.x;
				p.p2 = character.currMapPoint.y;
				p.p3 = (short) character.direction.ordinal();
				session.context.writeAndFlush(p);
				break;
			default:
				break;
		}
		session.writeAndFlush(new ServerPacket.Status(ServerPacket.Status.Result.Good));
	}
}
