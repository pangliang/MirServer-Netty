package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.Player;
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

			player.homeMapPoint = player.currMapPoint; //todo 暂时把当前坐标放到回城坐标, 方便测试, 测试时上线就去到原坐标
			player.currMapPoint = null;
			DB.update(player);
		}
	}
}
