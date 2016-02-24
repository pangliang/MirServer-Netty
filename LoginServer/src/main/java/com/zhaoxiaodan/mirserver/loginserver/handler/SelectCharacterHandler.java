package com.zhaoxiaodan.mirserver.loginserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.loginserver.LoginServerPackets;
import com.zhaoxiaodan.mirserver.loginserver.Session;
import com.zhaoxiaodan.mirserver.network.*;
import io.netty.channel.ChannelHandlerContext;

public class SelectCharacterHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {
		Session session = Session.getSession(ctx);
		if(session == null){
			ctx.writeAndFlush(new Packet(Protocol.SM_SELCHR_FAIL));
			return ;
		}

		ClientPackets.SelectCharacter request = (ClientPackets.SelectCharacter)packet;

		for(Character character:session.user.characters){
			if(character.name.equals(request.characterName)){
				ctx.writeAndFlush(new LoginServerPackets.SelectCharacterOk("192.168.0.166",7000));
				return;
			}
		}

		ctx.writeAndFlush(new Packet(Protocol.SM_SELCHR_FAIL));
		return ;
	}

}
