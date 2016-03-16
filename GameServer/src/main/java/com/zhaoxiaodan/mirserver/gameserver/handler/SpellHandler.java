package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.GameClientPackets;
import com.zhaoxiaodan.mirserver.gameserver.GameServerPackets;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class SpellHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		GameClientPackets.Spell request = (GameClientPackets.Spell) packet;

		boolean isSucc = false;
		isSucc = player.spell(request.magicId);

		if (isSucc)
			session.sendPacket(new GameServerPackets.ActionStatus(GameServerPackets.ActionStatus.Result.Good));
		else
			session.sendPacket(new GameServerPackets.ActionStatus(GameServerPackets.ActionStatus.Result.Fail));
	}
}
