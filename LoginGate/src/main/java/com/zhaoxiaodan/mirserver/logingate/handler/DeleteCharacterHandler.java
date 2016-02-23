package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.logingate.Session;
import com.zhaoxiaodan.mirserver.network.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Packet;
import com.zhaoxiaodan.mirserver.network.PacketHandler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import io.netty.channel.ChannelHandlerContext;

public class DeleteCharacterHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {
		ClientPackets.DeleteCharacter request = (ClientPackets.DeleteCharacter) packet;

		Session session = Session.getSession(ctx);

		for (Character character : session.user.characters) {
			if (character.name.equals(request.characterName)) {
				session.user.characters.remove(character);
				character.user=null;
				DB.delete(character);
				ctx.writeAndFlush(new Packet(Protocol.SM_DELCHR_SUCCESS));
				return ;
			}
		}

		ctx.writeAndFlush(new Packet(Protocol.SM_DELCHR_FAIL));
		return;
	}

}
