package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class TakeOnOffItemHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		if(packet.protocol == Protocol.CM_TAKEONITEM)
			takeOn((ClientPacket.TakeOnOffItem)packet,player);
		else if( packet.protocol == Protocol.CM_TAKEOFFITEM)
			takeOff((ClientPacket.TakeOnOffItem)packet,player);
		else
			logger.error("TakeOnOffItemHandler packet protocol unkown : {} " , packet.protocol);


	}

	private void takeOff(ClientPacket.TakeOnOffItem request, Player player) throws Exception {
		if(!player.takeOff(request.wearPosition))
		{
			player.session.sendPacket(new ServerPacket(-1, Protocol.SM_TAKEOFF_FAIL));
			return ;
		}else {
			player.session.sendPacket(new ServerPacket(player.getFeature(), Protocol.SM_TAKEOFF_OK, player.getFeatureEx(),(short)0,(short)0));
		}
	}

	private void takeOn(ClientPacket.TakeOnOffItem request, Player player) throws Exception {
		PlayerItem itemToWear = player.items.get(request.playerItemId);
		if(itemToWear == null)
		{
			player.session.sendPacket(new ServerPacket(-1, Protocol.SM_TAKEON_FAIL));
			return ;
		}

		if(player.takeOn(itemToWear, request.wearPosition)){
			player.session.sendPacket(new ServerPacket(player.getFeature(), Protocol.SM_TAKEON_OK, player.getFeatureEx(),(short)0,(short)0));
			return ;
		}else {
			player.session.sendPacket(new ServerPacket(-1, Protocol.SM_TAKEON_FAIL));
			return ;
		}
	}
}
