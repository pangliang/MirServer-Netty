package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.ClientPackets;
import com.zhaoxiaodan.mirserver.loginserver.ServerPackets;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class SelectCharacterHandler extends UserHandler {

	@Override
	public void onPacket(Packet packet, User user) throws Exception {

		ClientPackets.SelectCharacter request = (ClientPackets.SelectCharacter) packet;

		for(Character character:user.characters){
			if(character.name.equals(request.characterName)){
				session.writeAndFlush(new ServerPackets.SelectCharacterOk("192.168.1.106",7400));
				return;
			}
		}

		session.writeAndFlush(new Packet(Protocol.SM_STARTFAIL));
		return ;
	}

}
