package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.gameserver.ClientPackets;
import com.zhaoxiaodan.mirserver.gameserver.ServerPackets;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class LoginNoticeOkHandler extends CharacterHandler {

	@Override
	public void onPacket(Packet packet, Character character) throws Exception {
		ClientPackets.LoginNoticeOk request = (ClientPackets.LoginNoticeOk) packet;

		MapEngine.MapInfo currMap = MapEngine.getInstance().getMapInfo(character.currMapPoint.mapName);

		session.writeAndFlush(new ServerPackets.NewMap(character.id, character.currMapPoint.x, character.currMapPoint.y, (short) 0, character.currMapPoint.mapName));
		session.writeAndFlush(new ServerPackets.MapDescription(-1, currMap.mapDescription));

		int   feature   = Packet.makeLong(Packet.makeWord((byte) 2, (byte) 0), Packet.makeWord((byte) 0, (byte) 0));
		short featureEx = Packet.makeWord((byte) 0, (byte) 0);
		session.writeAndFlush(new ServerPackets.Logon(character.id, character.currMapPoint.x, character.currMapPoint.y, (byte) 0, (byte) 0, feature, 0x400, featureEx));

		session.writeAndFlush(new ServerPackets.FeatureChanged(character.id, feature, featureEx));

		session.writeAndFlush(new ServerPackets.UserName(character.id, (short) 255, character.name));

		session.writeAndFlush(new Packet(2, Protocol.SM_AREASTATE, (byte) 0, (byte) 0, (byte) 0));

		session.writeAndFlush(new ServerPackets.GameGoldName(1234, 5678, "游戏币", "游戏点数"));

		session.writeAndFlush(new ServerPackets.VersionFail(0, 0, 0));

		session.writeAndFlush(new ServerPackets.CharacterAbility(100,(short)200,character.alility));

	}


}
