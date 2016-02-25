package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.ClientPackets;
import com.zhaoxiaodan.mirserver.loginserver.ServerPackets;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Random;

public class SelectServerHandler extends UserHandler {

	@Override
	public void onPacket(Packet packet, User user) throws Exception {
		ClientPackets.SelectServer selectServer = (ClientPackets.SelectServer)packet;

		List<ServerInfo> list = DB.query(ServerInfo.class,Restrictions.eq("name",selectServer.serverName));
		if(1 != list.size())
		{
			session.writeAndFlush(new Packet(Protocol.SM_ID_NOTFOUND));
			return ;
		}else{
			user.certification = (byte)new Random().nextInt(200);
			DB.update(user);

			ServerInfo info = list.get(0);
			session.writeAndFlush(new ServerPackets.SelectServerOk(info.ip,info.port, user.certification));
		}
	}

}