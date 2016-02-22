package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.network.*;
import io.netty.channel.ChannelHandlerContext;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Random;

public class SelectServerHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {
		ClientPackets.SelectServer selectServer = (ClientPackets.SelectServer)packet;

		Session session = DB.getSession();

		List<ServerInfo> list = session.createCriteria(ServerInfo.class).add(Restrictions.eq("name",selectServer.serverName)).list();
		if(1 != list.size())
		{
			ctx.writeAndFlush(new Packet(Protocol.SM_ID_NOTFOUND));
			return ;
		}else{
			ServerInfo info = list.get(0);
			ctx.writeAndFlush(new ServerPackets.SelectServerOk(info.ip,info.port,(byte)new Random().nextInt(200)));
		}
	}

}
