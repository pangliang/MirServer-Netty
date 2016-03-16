package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.objects.Merchant;
import com.zhaoxiaodan.mirserver.gameserver.GameClientPackets;
import com.zhaoxiaodan.mirserver.gameserver.engine.MerchantEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class MerchantHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		GameClientPackets.Merchant request = (GameClientPackets.Merchant) packet;

		Merchant merchant = MerchantEngine.getMerchant(request.npcInGameId);
		if (merchant == null) {
			logger.error("点击了npc, 但是npc不存在. npcInGameId: {}", request.npcInGameId);
			return;
		}


		String functionName = "main";
		if (request.msg.length() > 0 && '@' == request.msg.charAt(0)) {
			functionName = request.msg.substring(1);
		}

		ScriptEngine.exce(merchant.scriptName, functionName, merchant, player);

		session.sendPacket(new ServerPacket(player.inGameId, Protocol.SM_MENU_OK));

	}
}
