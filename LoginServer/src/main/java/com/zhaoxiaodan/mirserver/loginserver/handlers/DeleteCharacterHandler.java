package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.LoginClientPackets;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class DeleteCharacterHandler extends UserHandler {

	@Override
	public void onPacket(ClientPacket packet, User user) throws Exception {

		LoginClientPackets.DeleteCharacter request = (LoginClientPackets.DeleteCharacter) packet;

		for (Player player : user.players) {
			if (player.name.equals(request.characterName)) {
				DB.delete(player);

				player.user.players.remove(player);
				player.user = null;

				session.sendPacket(new ServerPacket(Protocol.SM_DELCHR_SUCCESS));
				return;
			}
		}

		session.sendPacket(new ServerPacket(Protocol.SM_DELCHR_FAIL));
		return;
	}

}
