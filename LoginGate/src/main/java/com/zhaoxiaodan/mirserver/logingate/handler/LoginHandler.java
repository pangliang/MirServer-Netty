package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.network.*;
import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.db.entities.User;
import io.netty.channel.ChannelHandlerContext;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class LoginHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {
		ClientPackets.Login loginRequest = (ClientPackets.Login) packet;

		Session  session  = DB.getSession();

		List<User> list = session.createCriteria(User.class).add(Restrictions.eq("loginId",loginRequest.user.loginId)).list();
		if(1 != list.size())
		{
			ctx.writeAndFlush(new Packet(Protocol.SM_ID_NOTFOUND));
			return ;
		}else{
			if(list.get(0).password.equals(loginRequest.user.password))
			{
				List<ServerInfo> serverInfoList = session.createCriteria(ServerInfo.class).list();
				ctx.writeAndFlush(new ServerPackets.LoginSuccSelectServer(serverInfoList));
			}else{
				ctx.writeAndFlush(new Packet(Protocol.SM_PASSWD_FAIL));
			}
		}
	}

}
