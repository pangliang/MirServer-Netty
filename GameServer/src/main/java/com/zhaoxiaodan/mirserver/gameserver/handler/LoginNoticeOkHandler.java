package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.gameserver.ClientPackets;
import com.zhaoxiaodan.mirserver.gameserver.ServerPackets;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class LoginNoticeOkHandler extends Handler {

	@Override
	public void onPacket(Packet packet) throws Exception {
		ClientPackets.LoginNoticeOk request = (ClientPackets.LoginNoticeOk) packet;

		int   charId = 143983136;
		short x      = 267;
		short y      = 78;


		int   feature   = Packet.makeLong(Packet.makeWord((byte) 2, (byte) 0), Packet.makeWord((byte) 0, (byte) 0));
		short featureEx = Packet.makeWord((byte) 0, (byte) 0);

		session.writeAndFlush(new ServerPackets.NewMap(charId, x, y, (short) 1, "0"));

		//Logon(int charId, short currX, short currY, byte direction, byte light, int feature, int charStatus, int featureEx)
		session.writeAndFlush(new ServerPackets.Logon(charId, x, y, (byte) 0, (byte) 0, 262144, 0x400, featureEx));

		// FeatureChanged(int charId, int feature, short featureEx)
		session.writeAndFlush(new ServerPackets.FeatureChanged(charId, feature, featureEx));

		session.writeAndFlush(new ServerPackets.UserName(charId, (short) 255, "pangliang"));

		session.writeAndFlush(new Packet(2, Protocol.SM_AREASTATE, (byte) 0, (byte) 0, (byte) 0));
		session.writeAndFlush(new ServerPackets.MapDescription(-1, "比奇省"));

		session.writeAndFlush(new ServerPackets.GameGoldName(1234, 5678, "游戏币", "游戏点数"));
		session.writeAndFlush(new ServerPackets.GameGoldName(1234, 5678, "游戏币", "游戏点数"));

		session.writeAndFlush(new ServerPackets.VersionFail(0, 0, 0));
	}


}
