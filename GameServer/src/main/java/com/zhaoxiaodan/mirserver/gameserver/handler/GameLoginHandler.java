package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.gameserver.ClientPackets;
import com.zhaoxiaodan.mirserver.gameserver.ServerPackets;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class GameLoginHandler extends Handler {

	@Override
	public void onPacket(Packet packet) throws Exception {
		ClientPackets.GameLogin request = (ClientPackets.GameLogin) packet;

		User                         user;
		if ((user = (User)session.get("user")) == null) {
			List<User> list = DB.query(User.class, Restrictions.eq("loginId", request.loginId));
			if (list.size() == 0) {
				session.writeAndFlush(new Packet(Protocol.SM_CERTIFICATION_FAIL));
				return;
			}
			user = list.get(0);
		}
		if (user.certification == request.cert) {
			session.put("user", user);
			session.writeAndFlush(new ServerPackets.SendNotice("欢迎来到胖梁测试服务器"));
		} else {
			session.writeAndFlush(new Packet(Protocol.SM_CERTIFICATION_FAIL));
			return;
		}
	}

}
