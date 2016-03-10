package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Config;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.StdItem;
import com.zhaoxiaodan.mirserver.gameserver.engine.ItemEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

import java.util.Map;

public class LoginNoticeOkHandler extends PlayerHandler {

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		if (player.currentAbility().Level == 0) {
			ScriptEngine.exce(Player.SCRIPT_NAME, "onCreate", player);
			if (player.currentAbility().Level < 1)
				player.levelUp(1);

			player.homeMapPoint = MapEngine.getStartPoint();

			session.db.update(player);

			Map<String, Integer> initItems = (Map<String, Integer>) ScriptEngine.exce(Player.SCRIPT_NAME, "getInitItems", player);
			for (String itemName : initItems.keySet()) {
				StdItem    stdItem    = ItemEngine.getStdItemByName(itemName);
				player.takeNewItem(stdItem);
			}
		}


		// 登录
		player.session.sendPacket(new ServerPacket.Logon(player));
		// 玩家属性, 必须发, 不然血量是0 就闪退
		player.checkAbility();
		session.sendPacket(new ServerPacket.PlayerAbility(player));

		player.enterMap(player.homeMapPoint);

		player.session.sendPacket(new ServerPacket.SendUseItems(player.wearingItems));
		player.session.sendPacket(new ServerPacket.UserName(player.inGameId, (short) player.nameColor.c, player.name));

		session.sendPacket(new ServerPacket.GameGoldName(player.gameGold, player.gamePoint, Config.GAME_GOLD_NAME, Config.GAME_POINT_NAME));
		session.sendPacket(new ServerPacket.VersionFail(0, 0, 0));

		session.sendPacket(new ServerPacket.ShowEvent(NumUtil.newAtomicId(), (short) 4, (short) 283, (short) 612, ""));
		session.sendPacket(new ServerPacket.ShowEvent(NumUtil.newAtomicId(), (short) 4, (short) 283, (short) 611, ""));
		session.sendPacket(new ServerPacket.ShowEvent(NumUtil.newAtomicId(), (short) 4, (short) 283, (short) 613, ""));
	}


}
