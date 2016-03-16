package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.gameserver.entities.User;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Session;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public class DisconnectHandler extends Handler {

	@Override
	public void onPacket(ClientPacket packet) throws Exception {

	}

	@Override
	public void onDisconnect() throws Exception {
		User user = (User) session.get("user");
		if(null != user){
			logger.info("user {} disconnect, online count : {}" ,user.loginId, Session.size());
		}else {
			logger.error("user no login , session : {}", session);
		}
	}
}
