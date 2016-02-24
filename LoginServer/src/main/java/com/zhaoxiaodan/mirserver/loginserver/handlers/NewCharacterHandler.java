package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class NewCharacterHandler extends UserHandler {

	@Override
	public void onPacket(Packet packet, User user) throws Exception {
		ClientPackets.NewCharacter newCharacter = (ClientPackets.NewCharacter) packet;
		newCharacter.character.user = user;
		DB.merge(newCharacter.character);
		user.characters.add(newCharacter.character);
		session.writeAndFlush(new Packet(Protocol.SM_NEWCHR_SUCCESS));
	}

}
