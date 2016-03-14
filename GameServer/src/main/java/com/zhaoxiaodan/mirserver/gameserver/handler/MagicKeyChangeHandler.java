package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.PlayerMagic;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class MagicKeyChangeHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		ClientPacket.MagicKeyChange request = (ClientPacket.MagicKeyChange) packet;

		if (player.magics.containsKey(request.magicId)) {
			PlayerMagic playerMagic = player.magics.get(request.magicId);
			playerMagic.key = request.key;
			DB.update(playerMagic);
		}
	}
}
