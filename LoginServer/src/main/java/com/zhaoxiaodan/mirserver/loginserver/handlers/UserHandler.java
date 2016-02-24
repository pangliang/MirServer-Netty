package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.packets.Packet;

public abstract class UserHandler extends Handler {

	public void onPacket(Packet packet) throws Exception {
		User user = (User) session.get("user");
		if (null == user)
			throw new Exception("user not found for packet:" + packet.protocol);

		DB.merge(user);
		onPacket(packet, user);
	}

	public abstract void onPacket(Packet packet, User user) throws Exception;
}
