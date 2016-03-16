package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.gameserver.entities.Config;
import com.zhaoxiaodan.mirserver.gameserver.entities.Player;
import com.zhaoxiaodan.mirserver.gameserver.GameServerPackets;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

public class LoginNoticeOkHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		if (player.Level == 0) {
			ScriptEngine.exce(Player.SCRIPT_NAME, "onCreate", player);
			if (player.Level < 1)
				player.levelUp(1);

			player.homeMapPoint = MapEngine.getStartPoint();

			DB.update(player);
		}

		if (player.hp <= 0)
			player.hp = 10;// player.maxHp;


		// 登录
		session.sendPacket(new GameServerPackets.Logon(player));
		// 玩家属性, 必须发, 不然血量是0 就闪退
		player.checkAbility();
		session.sendPacket(new GameServerPackets.PlayerAbility(player));

		player.enterMap(player.homeMapPoint);

		session.sendPacket(new GameServerPackets.SendUseItems(player.wearingItems));
		session.sendPacket(new GameServerPackets.UserName(player.inGameId, player.nameColor.id, player.name));
		session.sendPacket(new GameServerPackets.SendMyMagics(player.magics));


		session.sendPacket(new GameServerPackets.GameGoldName(player.gameGold, player.gamePoint, Config.GAME_GOLD_NAME, Config.GAME_POINT_NAME));
		session.sendPacket(new GameServerPackets.VersionFail(0, 0, 0));

		session.sendPacket(new ServerPacket(0, Protocol.SM_DAYCHANGING, (short) 1, (short) 1, (short) 0));

		session.sendPacket(new GameServerPackets.ShowEvent(NumUtil.newAtomicId(), (short) 4, (short) 283, (short) 612, ""));
		session.sendPacket(new GameServerPackets.ShowEvent(NumUtil.newAtomicId(), (short) 4, (short) 283, (short) 611, ""));
		session.sendPacket(new GameServerPackets.ShowEvent(NumUtil.newAtomicId(), (short) 4, (short) 283, (short) 613, ""));
	}


}
