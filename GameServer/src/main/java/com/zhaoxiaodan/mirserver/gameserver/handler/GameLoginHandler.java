package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class GameLoginHandler extends Handler {

	@Override
	public void onPacket(ClientPacket packet) throws Exception {
		ClientPacket.GameLogin request = (ClientPacket.GameLogin) packet;

		if (session.get("player") == null) {
			List<User> list = DB.query(User.class, Restrictions.eq("loginId", request.loginId));
			if (list.size() == 1) {
				User user = list.get(0);

				if (user.certification == request.cert) {
					for (Player c : user.players) {
						if (c.name.equals(request.characterName)) {
							session.put("player", c);
							session.writeAndFlush(new ServerPacket.SendNotice("欢迎来到胖梁测试服务器"));
							return;
						}
					}
				}
			}
		}

		session.writeAndFlush(new ServerPacket(Protocol.SM_CERTIFICATION_FAIL));
		return;
	}

}
