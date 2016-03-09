package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Config;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class ActionHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		ClientPacket.Action request = (ClientPacket.Action)packet;


		if(!player.checkAndIncActionTime(Config.PLAYER_ACTION_INTERVAL_TIME)){
			session.sendPacket(new ServerPacket.ActionStatus(ServerPacket.ActionStatus.Result.Fail));
			return;
		}

		boolean isSucc = false;
		switch (packet.protocol){
			case CM_WALK:
				isSucc = player.walk(request.direction);
				break;
			case CM_RUN:
				isSucc = player.run(request.direction);
				break;
			case CM_TURN:
				isSucc = player.turn(request.direction);
				break;
			case CM_HIT:
			case CM_BIGHIT:
			case CM_HEAVYHIT:
			case CM_POWERHIT:
			case CM_LONGHIT:
				isSucc = player.hit(request.direction);
				break;
			default:
				isSucc = true;
				break;
		}

		if(isSucc)
			session.sendPacket(new ServerPacket.ActionStatus(ServerPacket.ActionStatus.Result.Good));
		else
			session.sendPacket(new ServerPacket.ActionStatus(ServerPacket.ActionStatus.Result.Fail));
	}
}
