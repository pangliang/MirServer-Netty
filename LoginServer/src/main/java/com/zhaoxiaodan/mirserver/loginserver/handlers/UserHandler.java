package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public abstract class UserHandler extends Handler {

	public final void onPacket(ClientPacket packet) throws Exception {
		User user = (User) session.get("user");
		if (null == user)
			throw new Exception("user not found for packet:" + packet.protocol);

		onPacket(packet, user);
	}

	public abstract void onPacket(ClientPacket packet, User user) throws Exception;
}
