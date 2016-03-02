package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class DeleteCharacterHandler extends UserHandler {

	@Override
	public void onPacket(Packet packet, User user) throws Exception {

		ClientPacket.DeleteCharacter request = (ClientPacket.DeleteCharacter) packet;

		for (Player player : user.players) {
			if (player.name.equals(request.characterName)) {
				session.db.delete(player);

				player.user.players.remove(player);
				player.user = null;

				session.writeAndFlush(new Packet(Protocol.SM_DELCHR_SUCCESS));
				return;
			}
		}

		session.writeAndFlush(new Packet(Protocol.SM_DELCHR_FAIL));
		return;
	}

}
