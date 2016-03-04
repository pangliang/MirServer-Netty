package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class SayHandler extends PlayerHandler {


	static int r = 0;
	static short x = 280;
	static short y = 610;

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		ClientPacket.Say request = (ClientPacket.Say)packet;

		MapPoint p = new MapPoint();
		p.mapName = request.msg.trim();
		p.x = 10;
		p.y = 20;
		MapEngine.enter(player,p);
	}
}
