package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class SelectCharacterHandler extends UserHandler {

	@Override
	public void onPacket(Packet packet, User user) throws Exception {

		ClientPacket.SelectCharacter request = (ClientPacket.SelectCharacter) packet;

		for(Character character:user.characters){
			if(character.name.equals(request.characterName)){
				session.writeAndFlush(new ServerPacket.StartPlay("192.168.0.166",7400));
				return;
			}
		}

		session.writeAndFlush(new Packet(Protocol.SM_STARTFAIL));
		return ;
	}

}
