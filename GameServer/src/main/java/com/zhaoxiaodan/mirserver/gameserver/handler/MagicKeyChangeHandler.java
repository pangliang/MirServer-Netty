package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.entities.PlayerMagic;
import com.zhaoxiaodan.mirserver.gameserver.GameClientPackets;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class MagicKeyChangeHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		GameClientPackets.MagicKeyChange request = (GameClientPackets.MagicKeyChange) packet;

		if (player.magics.containsKey(request.magicId)) {
			PlayerMagic playerMagic = player.magics.get(request.magicId);
			playerMagic.key = request.key;
			DB.update(playerMagic);
		}
	}
}
