package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class WalkHandler extends CharacterHandler {

	@Override
	public void onPacket(Packet packet, Character character) throws Exception {
		session.writeAndFlush(new ServerPacket.Status(ServerPacket.Status.Result.Good));
	}
}
