package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class WantMiniMapHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		MapEngine.MapInfo mapInfo = MapEngine.getMapInfo(player.currMapPoint.mapId);
		if(mapInfo.miniMapId == null)
			player.session.sendPacket(new ServerPacket(0, Protocol.SM_READMINIMAP_FAIL));
		else
			player.session.sendPacket(new ServerPacket(0,Protocol.SM_READMINIMAP_OK,(short)Short.parseShort(mapInfo.miniMapId),(short)0,(short)0));
	}
}
