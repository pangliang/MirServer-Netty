package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class SpellHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		ClientPacket.Spell request = (ClientPacket.Spell) packet;

		boolean isSucc = false;
		isSucc = player.spell(request.magicId);

		if (isSucc)
			session.sendPacket(new ServerPacket.ActionStatus(ServerPacket.ActionStatus.Result.Good));
		else
			session.sendPacket(new ServerPacket.ActionStatus(ServerPacket.ActionStatus.Result.Fail));
	}
}
