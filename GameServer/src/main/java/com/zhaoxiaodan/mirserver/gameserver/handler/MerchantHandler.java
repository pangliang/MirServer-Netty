package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.objects.Merchant;
import com.zhaoxiaodan.mirserver.gameserver.engine.MerchantEngine;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class MerchantHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		ClientPacket.Merchant request = (ClientPacket.Merchant) packet;

		Merchant merchant = MerchantEngine.getMerchant(request.npcInGameId);
		if(merchant == null){
			logger.error("点击了npc, 但是npc不存在. npcInGameId: {}", request.npcInGameId);
			return ;
		}


		String functionName = "main";
		if(request.msg.length() >0 && '@' == request.msg.charAt(0))
		{
			functionName = request.msg.substring(1);
		}

		merchant.scriptInstance.invokeMethod(functionName,new Object[]{merchant,player});

	}
}
