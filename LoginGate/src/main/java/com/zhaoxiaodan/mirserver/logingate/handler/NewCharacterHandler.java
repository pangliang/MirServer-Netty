package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Packet;
import com.zhaoxiaodan.mirserver.network.PacketHandler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import io.netty.channel.ChannelHandlerContext;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class NewCharacterHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {
		ClientPackets.NewCharacter newCharacter = (ClientPackets.NewCharacter) packet;

		Session  session  = DB.getSession();

		List<User> list = session.createCriteria(User.class).add(Restrictions.eq("loginId", newCharacter.character.user.loginId)).list();
		if(list.size() != 1)
		{
			ctx.writeAndFlush(new Packet(Protocol.SM_NEWCHR_FAIL));
			return ;
		}

		User user = list.get(0);
		if(user.characters.size() >= 2)
		{
			ctx.writeAndFlush(new Packet(Protocol.SM_NEWCHR_FAIL));
			return ;
		}
		else{
			try{
				newCharacter.character.user = user;
				session.save(newCharacter.character);
				session.flush();

				ctx.writeAndFlush(new Packet(Protocol.SM_NEWCHR_SUCCESS));
			}catch (Exception e)
			{
				ctx.writeAndFlush(new Packet(Protocol.SM_NEWCHR_FAIL));
				return ;
			}
		}
	}

}
