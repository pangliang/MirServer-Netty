package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;

public abstract class PlayerHandler extends Handler {

	public final void onPacket(ClientPacket packet) throws Exception {
		Player player = (Player) session.get("player");
		if (null == player)
			throw new Exception("player not found for packet:" + packet.protocol);

		if (!player.isAlive) {
			player.sendAlarmMsg("人物已经死亡, 无法操作, 小退后重新登录!!");
			return;
		}
		onPacket(packet, player);
	}

	public abstract void onPacket(ClientPacket packet, Player player) throws Exception;
}
