package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class SelectCharacterHandler extends UserHandler {

	@Override
	public void onPacket(ClientPacket packet, User user) throws Exception {

		ClientPacket.SelectCharacter request = (ClientPacket.SelectCharacter) packet;

		for(Player player :user.players){
			if(player.name.equals(request.characterName)){
				session.writeAndFlush(new ServerPacket.StartPlay("192.168.0.166",7400));
				return;
			}
		}

		session.writeAndFlush(new ServerPacket(Protocol.SM_STARTFAIL));
		return ;
	}

}
