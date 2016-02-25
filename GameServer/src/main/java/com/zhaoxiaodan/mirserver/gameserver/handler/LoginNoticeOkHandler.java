package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.gameserver.ClientPackets;
import com.zhaoxiaodan.mirserver.gameserver.ServerPackets;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class LoginNoticeOkHandler extends Handler {

	@Override
	public void onPacket(Packet packet) throws Exception {
		ClientPackets.LoginNoticeOk request = (ClientPackets.LoginNoticeOk) packet;

		session.writeAndFlush(new ServerPackets.NewMap(137418480,(short)267,(short)78,(short)0,"0"));
		session.writeAndFlush(new ServerPackets.Logon(137418480,(short)267,(short)78,(short)0,"4"));
		session.writeAndFlush(new ServerPackets.UserName(137418480,(short)255,"胖梁测试英雄"));
		session.writeAndFlush(new ServerPackets.MapDescription("测试地图描述"));
	}

}
