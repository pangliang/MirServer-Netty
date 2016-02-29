package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public abstract class CharacterHandler extends Handler {
	public final void onPacket(Packet packet) throws Exception {
		Character character = (Character) session.get("character");
		if (null == character)
			throw new Exception("character not found for packet:" + packet.protocol);

		onPacket(packet, character);
	}

	public abstract void onPacket(Packet packet, Character character) throws Exception;
}
