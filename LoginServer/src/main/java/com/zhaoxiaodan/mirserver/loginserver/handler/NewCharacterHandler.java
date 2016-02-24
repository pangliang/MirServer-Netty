package com.zhaoxiaodan.mirserver.loginserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.loginserver.Session;
import com.zhaoxiaodan.mirserver.network.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Packet;
import com.zhaoxiaodan.mirserver.network.PacketHandler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import io.netty.channel.ChannelHandlerContext;

public class NewCharacterHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {

		ClientPackets.NewCharacter newCharacter = (ClientPackets.NewCharacter) packet;

		Session session = Session.getSession(ctx);

		newCharacter.character.user = session.user;
		DB.saveOrUpdate(newCharacter.character);
		session.user.characters.add(newCharacter.character);
		ctx.writeAndFlush(new Packet(Protocol.SM_NEWCHR_SUCCESS));
	}

}
