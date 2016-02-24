package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.gameserver.ClientPackets;
import com.zhaoxiaodan.mirserver.gameserver.ServerPackets;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.Session;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;

public class LoginHandler extends Handler {

	@Override
	public void onPacket(Packet packet) throws Exception {
		ClientPackets.StartGame request = (ClientPackets.StartGame) packet;

		List<User> list = DB.query(User.class, Restrictions.eq("loginId", request.loginId));
		if (1 != list.size()) {
			session.writeAndFlush(new Packet(Protocol.SM_PASSWD_FAIL));
			return;
		} else {
			User user = list.get(0);
			if (user.password.equals(request.loginId)) {

				user.lastLoginTime = new Date();
				DB.update(user);

				session.put("user", user);
				logger.info("user {} login, loginUser count:{}", user.loginId, Session.size());

				List<ServerInfo> serverInfoList = DB.query(ServerInfo.class);
				session.writeAndFlush(new ServerPackets.LoginSuccSelectServer(serverInfoList));
			} else {
				session.writeAndFlush(new Packet(Protocol.SM_PASSWD_FAIL));
			}
		}
	}

}
