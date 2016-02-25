package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Session;

public class DisconnectHandler extends Handler {

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
