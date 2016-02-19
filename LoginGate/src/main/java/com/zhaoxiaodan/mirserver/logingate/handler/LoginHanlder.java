package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.core.network.*;
import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import io.netty.channel.ChannelHandlerContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by liangwei on 16/2/18.
 */
public class LoginHanlder implements PacketHanlder {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet indexPacket) {
		ClientPackets.Login loginRequest = (ClientPackets.Login) indexPacket;

		Session  session  = DB.getSession();
		Criteria criteria = session.createCriteria(User.class);

		List<User> list = criteria.add(Restrictions.eq("userId",loginRequest.user.userId)).list();
		if(1 != list.size())
		{
			ctx.writeAndFlush(new Packet(Protocol.SM_ID_NOTFOUND));
			return ;
		}else{
			if(list.get(0).password.equals(loginRequest.user.password))
			{

			}else{
				ctx.writeAndFlush(new Packet(Protocol.SM_PASSWD_FAIL));
			}
		}
	}

}
