package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.entities.User;
import com.zhaoxiaodan.mirserver.gameserver.GameClientPackets;
import com.zhaoxiaodan.mirserver.gameserver.GameServerPackets;
import com.zhaoxiaodan.mirserver.gameserver.engine.MessageEngine;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class GameLoginHandler extends Handler {

	@Override
	public void onPacket(ClientPacket packet) throws Exception {
		GameClientPackets.GameLogin request = (GameClientPackets.GameLogin) packet;

		if (session.get("player") == null) {
			List<User> list = DB.query(User.class, Restrictions.eq("loginId", request.loginId));
			if (list.size() == 1) {
				User user = list.get(0);

				if (user.certification == request.cert) {
					for (Player player : user.players) {
						if (player.name.equals(request.characterName)) {
							player.session = session;
							session.put("player", player);
							session.sendPacket(new GameServerPackets.SendNotice(MessageEngine.getNotice()));
							return;
						}
					}
				}
			}
		}

		session.sendPacket(new ServerPacket(Protocol.SM_CERTIFICATION_FAIL));
		return;
	}

}
