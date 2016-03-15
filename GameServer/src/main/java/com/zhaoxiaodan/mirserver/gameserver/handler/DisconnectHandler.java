package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.network.Handler;

public class DisconnectHandler extends Handler {

	@Override
	public void onDisconnect() throws Exception {
		Player player = (Player) session.get("player");
		if(null != player){
			try {
				player.leaveMap();
			}catch (Exception e){
				logger.error(e);
			}

			player.homeMapPoint = MapEngine.getStartPoint();
			DB.update(player);
		}
	}
}
