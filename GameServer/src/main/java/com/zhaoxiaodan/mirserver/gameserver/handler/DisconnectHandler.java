package com.zhaoxiaodan.mirserver.gameserver.handler;

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

			session.db.begin();
			try{
				player.homeMapPoint = player.currMapPoint; //todo 暂时把当前坐标放到回城坐标, 方便测试, 测试时上线就去到原坐标
				player.currMapPoint = null;
				session.db.update(player);
				session.db.commit();
			}catch (Exception e){
				session.db.rollback();
			}

		}
	}
}
