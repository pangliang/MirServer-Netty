package com.zhaoxiaodan.mirserver.loginserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.log.Log;
import com.zhaoxiaodan.mirserver.loginserver.LoginServerPackets;
import com.zhaoxiaodan.mirserver.loginserver.Session;
import com.zhaoxiaodan.mirserver.network.*;
import io.netty.channel.ChannelHandlerContext;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class LoginHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {
		ClientPackets.Login loginRequest = (ClientPackets.Login) packet;

		List<User> list = DB.query(User.class,Restrictions.eq("loginId", loginRequest.user.loginId));
		if (1 != list.size()) {
			ctx.writeAndFlush(new Packet(Protocol.SM_PASSWD_FAIL));
			return;
		} else {
			User user = list.get(0);
			if (user.password.equals(loginRequest.user.password)) {
				Session s = new Session();
				s.user = user;
				Session.put(ctx, s);
				Log.info("user {} login, loginUser count:{}", user.loginId, Session.size());

				List<ServerInfo> serverInfoList = DB.query(ServerInfo.class);
				ctx.writeAndFlush(new LoginServerPackets.LoginSuccSelectServer(serverInfoList));
			} else {
				ctx.writeAndFlush(new Packet(Protocol.SM_PASSWD_FAIL));
			}
		}
	}

}
