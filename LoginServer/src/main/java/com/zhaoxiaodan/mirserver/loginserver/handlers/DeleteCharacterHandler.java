package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class DeleteCharacterHandler extends UserHandler {

	@Override
	public void onPacket(Packet packet, User user) throws Exception {

		ClientPacket.DeleteCharacter request = (ClientPacket.DeleteCharacter) packet;

		for (Character character : user.characters) {
			if (character.name.equals(request.characterName)) {
				session.db.delete(character);

				character.user.characters.remove(character);
				character.user = null;

				session.writeAndFlush(new Packet(Protocol.SM_DELCHR_SUCCESS));
				return;
			}
		}

		session.writeAndFlush(new Packet(Protocol.SM_DELCHR_FAIL));
		return;
	}

}
