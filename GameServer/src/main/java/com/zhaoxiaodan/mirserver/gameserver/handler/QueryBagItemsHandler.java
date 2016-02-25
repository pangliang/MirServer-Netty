package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.gameserver.ServerPackets;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

import java.util.ArrayList;

public class QueryBagItemsHandler extends Handler {

	@Override
	public void onPacket(Packet packet) throws Exception {

		session.writeAndFlush(new ServerPackets.BagItems(137418480,new ArrayList<String>()));
	}

}
