package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public class NewCharacterHandler extends UserHandler {

	@Override
	public void onPacket(Packet packet, User user) throws Exception {
		ClientPackets.NewCharacter request = (ClientPackets.NewCharacter) packet;
		if(user.characters.size() >= 2){
			session.writeAndFlush(new Packet(Protocol.SM_NEWCHR_FAIL));
			return;
		}
		try {
			request.character.user = user;
			DB.save(request.character);
			user.characters.add(request.character);
			session.writeAndFlush(new Packet(Protocol.SM_NEWCHR_SUCCESS));
		}catch (Exception e){
			session.writeAndFlush(new Packet(Protocol.SM_NEWCHR_FAIL));
			throw e;
		}
	}
}
