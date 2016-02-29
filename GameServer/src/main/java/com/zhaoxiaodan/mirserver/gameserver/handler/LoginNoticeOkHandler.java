package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.gameserver.ClientPackets;
import com.zhaoxiaodan.mirserver.gameserver.MapManager;
import com.zhaoxiaodan.mirserver.gameserver.ServerPackets;
import com.zhaoxiaodan.mirserver.gameserver.TBaseObject;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class LoginNoticeOkHandler extends Handler {

	@Override
	public void onPacket(Packet packet) throws Exception {
		ClientPackets.LoginNoticeOk request = (ClientPackets.LoginNoticeOk) packet;

		int   charId = 143983136;
		short x      = 289;
		short y      = 611;

		MapManager.StartPoint startPoint = MapManager.getInstance().getStartPoint();
		MapManager.MapInfo startMap = MapManager.getInstance().getMapInfo(startPoint.mapName);

		session.writeAndFlush(new ServerPackets.NewMap(charId, startPoint.point.x, startPoint.point.y, (short) 1, startPoint.mapName));
		session.writeAndFlush(new ServerPackets.MapDescription(-1, startMap.mapDescription));

		int   feature   = Packet.makeLong(Packet.makeWord((byte) 2, (byte) 0), Packet.makeWord((byte) 0, (byte) 0));
		short featureEx = Packet.makeWord((byte) 0, (byte) 0);
		session.writeAndFlush(new ServerPackets.Logon(charId, x, y, (byte) 0, (byte) 0, 262144, 0x400, featureEx));

		session.writeAndFlush(new ServerPackets.FeatureChanged(charId, feature, featureEx));

		session.writeAndFlush(new ServerPackets.UserName(charId, (short) 255, "pangliang\\\\\\\0\0\0\0"));

		session.writeAndFlush(new Packet(2, Protocol.SM_AREASTATE, (byte) 0, (byte) 0, (byte) 0));

		session.writeAndFlush(new ServerPackets.GameGoldName(1234, 5678, "游戏币", "游戏点数"));

		session.writeAndFlush(new ServerPackets.VersionFail(0, 0, 0));

		TBaseObject.TAbility ability = new TBaseObject.TAbility();

		ability.Level = 4;
		ability.AC = 1;
		ability.MAC = 2;
		ability.DC = 3;
		ability.MC = 4;
		ability.SC=5;
		ability.HP = 100;
		ability.MP = 200;
		ability.MaxHP = 200;
		ability.MaxMP = 200;
		ability.Exp = 10;
		ability.MaxExp = 100;

		session.writeAndFlush(new ServerPackets.Ability(100,(short)200,ability));

	}


}
