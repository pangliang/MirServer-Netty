package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class SayHandler extends PlayerHandler {


	static int   r = 0;
	static short x = 280;
	static short y = 610;

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		ClientPacket.Say request = (ClientPacket.Say) packet;

		String parts[] = request.msg.trim().split(" ");
		if (parts.length == 3) {
			MapPoint p = new MapPoint();
			p.mapName = parts[0];
			p.x = Short.parseShort(parts[1]);
			p.y = Short.parseShort(parts[2]);


			MapEngine.enter(player,p);

		}

	}
}
