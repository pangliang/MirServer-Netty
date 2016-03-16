package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.gameserver.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.gameserver.entities.User;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Session;
import com.zhaoxiaodan.mirserver.loginserver.LoginClientPackets;
import com.zhaoxiaodan.mirserver.loginserver.LoginServerPackets;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

public class LoginHandler extends Handler {

	@Override
	public void onPacket(ClientPacket packet) throws Exception {
		LoginClientPackets.Login loginRequest = (LoginClientPackets.Login) packet;

		List<User> list = DB.query(User.class, Restrictions.eq("loginId", loginRequest.user.loginId));
		if (1 != list.size()) {
			session.sendPacket(new LoginServerPackets.LoginFail(LoginServerPackets.LoginFail.Reason.UserNotFound));
			return;
		} else {
			User user = list.get(0);
			if (user.password.equals(loginRequest.user.password)) {

				user.lastLoginTime = new Date();
				DB.update(user);
				session.put("user", user);
				logger.info("user {} login, login user count:{}", user.loginId, Session.size());

				List<ServerInfo> serverInfoList = DB.query(ServerInfo.class);
				session.sendPacket(new LoginServerPackets.LoginSuccSelectServer(serverInfoList));
			} else {
				session.sendPacket(new LoginServerPackets.LoginFail(LoginServerPackets.LoginFail.Reason.WrongPwd));
			}
		}
	}

}
