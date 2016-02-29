package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.ClientPackets;
import com.zhaoxiaodan.mirserver.loginserver.ServerPackets;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Session;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

public class LoginHandler extends Handler {

	@Override
	public void onPacket(Packet packet) throws Exception {
		ClientPackets.Login loginRequest = (ClientPackets.Login) packet;

		List<User> list = DB.query(User.class, Restrictions.eq("loginId", loginRequest.user.loginId));
		if (1 != list.size()) {
			session.writeAndFlush(new ServerPackets.LoginFail(ServerPackets.LoginFail.Reason.UserNotFound));
			return;
		} else {
			User user = list.get(0);
			if (user.password.equals(loginRequest.user.password)) {

				user.lastLoginTime = new Date();
				session.db.update(user);
				session.put("user", user);
				logger.info("user {} login, login user count:{}", user.loginId, Session.size());

				List<ServerInfo> serverInfoList = DB.query(ServerInfo.class);
				session.writeAndFlush(new ServerPackets.LoginSuccSelectServer(serverInfoList));
			} else {
				session.writeAndFlush(new ServerPackets.LoginFail(ServerPackets.LoginFail.Reason.WrongPwd));
			}
		}
	}

}
