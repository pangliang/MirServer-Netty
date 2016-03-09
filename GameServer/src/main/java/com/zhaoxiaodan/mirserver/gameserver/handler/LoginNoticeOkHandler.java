package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Config;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem;
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

		if(player.ability.Level == 0){
			ScriptEngine.exce(ScriptEngine.Module.Player,"onCreate", player);
			if(player.ability.Level < 1)
				player.ability.Level = 1;

			player.homeMapPoint = MapEngine.getStartPoint();

			session.db.update(player);

			Map<String,Integer> initItems = (Map<String, Integer>) ScriptEngine.exce(ScriptEngine.Module.Player,"getInitItems", player);
			for(String itemName : initItems.keySet()){
				StdItem    stdItem    = ItemEngine.getStdItemByName(itemName);
				PlayerItem playerItem = new PlayerItem();
				playerItem.player = player;
				playerItem.attr = stdItem.attr;
				playerItem.dura = stdItem.attr.duraMax;

				session.db.save(playerItem);
				player.items.put(playerItem.id,playerItem);
			}
		}


		// 登录
		player.session.sendPacket(new ServerPacket.Logon(player));
		// 玩家属性, 必须发, 不然血量是0 就闪退
		player.session.sendPacket(new ServerPacket.PlayerAbility(player.gold, player.gameGold, player.job, player.ability));

		player.enterMap(player.homeMapPoint);

		player.session.sendPacket(new ServerPacket.SendUseItems(player.wearingItems));
		player.session.sendPacket(new ServerPacket.UserName(player.inGameId, (short) player.nameColor.c, player.name));

		session.sendPacket(new ServerPacket.GameGoldName(player.gameGold, player.gamePoint, Config.GAME_GOLD_NAME, Config.GAME_POINT_NAME));
		session.sendPacket(new ServerPacket.VersionFail(0, 0, 0));

		session.sendPacket(new ServerPacket.ShowEvent(NumUtil.newAtomicId(),(short)4,(short)283,(short)612,""));
		session.sendPacket(new ServerPacket.ShowEvent(NumUtil.newAtomicId(),(short)4,(short)283,(short)611,""));
		session.sendPacket(new ServerPacket.ShowEvent(NumUtil.newAtomicId(),(short)4,(short)283,(short)613,""));
	}


}
