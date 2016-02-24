package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.logingate.Session;
import com.zhaoxiaodan.mirserver.network.*;
import io.netty.channel.ChannelHandlerContext;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Random;

public class SelectServerHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {
		Session session = Session.getSession(ctx);
		if(session == null){
			ctx.writeAndFlush(new Packet(Protocol.SM_CERTIFICATION_FAIL));
			return ;
		}

		ClientPackets.SelectServer selectServer = (ClientPackets.SelectServer)packet;

		List<ServerInfo> list = DB.query(ServerInfo.class,Restrictions.eq("name",selectServer.serverName));
		if(1 != list.size())
		{
			ctx.writeAndFlush(new Packet(Protocol.SM_ID_NOTFOUND));
			return ;
		}else{
			session.user.certification = (byte)new Random().nextInt(200);
			DB.saveOrUpdate(session.user);

			ServerInfo info = list.get(0);
			ctx.writeAndFlush(new LoginServerPackets.SelectServerOk(info.ip,info.port, session.user.certification));
		}
	}

}
