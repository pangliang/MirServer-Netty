package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.objects.Merchant;
import com.zhaoxiaodan.mirserver.gameserver.engine.MerchantEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class SellItemHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		int npcInGameId = packet.recog;

		Merchant merchant = MerchantEngine.getMerchant(npcInGameId);
		if (merchant == null) {
			logger.error("点击了npc, 但是npc不存在. npcInGameId: {}", npcInGameId);
			return;
		}

		int itemId = packet.p1;

		Boolean rs = (Boolean) ScriptEngine.exce(merchant.scriptName, "sell", merchant, player, itemId);
		if (rs == null || !rs) {
			player.session.sendPacket(new ServerPacket(Protocol.SM_USERSELLITEM_FAIL));
		}else {
			player.session.sendPacket(new ServerPacket(player.gold,Protocol.SM_USERSELLITEM_OK));
		}

	}
}
