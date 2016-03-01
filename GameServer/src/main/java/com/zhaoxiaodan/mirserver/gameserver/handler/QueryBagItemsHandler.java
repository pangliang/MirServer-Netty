package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.db.entities.CharacterItem;
import com.zhaoxiaodan.mirserver.gameserver.ServerPackets;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class QueryBagItemsHandler extends CharacterHandler {


	@Override
	public void onPacket(Packet packet, Character character) throws Exception {
		for (CharacterItem item : character.items) {
			session.writeAndFlush(new ServerPackets.AddItem(character.id, item));
			break;
		}
	}
}
