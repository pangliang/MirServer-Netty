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

			player.currMapPoint = MapEngine.getStartPoint();

			session.db.update(player);

			Map<String,Integer> initItems = (Map<String, Integer>) ScriptEngine.exce(ScriptEngine.Module.Player,"getInitItems", player);
			for(String itemName : initItems.keySet()){
				StdItem    stdItem    = ItemEngine.getStdItemByName(itemName);
				PlayerItem playerItem = new PlayerItem();
				playerItem.player = player;
				playerItem.attr = stdItem.attr;

				session.db.save(playerItem);
				player.items.add(playerItem);
			}
		}

		MapEngine.enter(player,player.currMapPoint);
		session.writeAndFlush(new ServerPacket.UserName(player.id, (short) player.nameColor.i, player.name));
		session.writeAndFlush(new ServerPacket.GameGoldName(player.gameGold, player.gamePoint, Config.GAME_GOLD_NAME, Config.GAME_POINT_NAME));
		session.writeAndFlush(new ServerPacket.VersionFail(0, 0, 0));

		session.writeAndFlush(new ServerPacket.ShowEvent(NumUtil.newAtomicId(),(short)4,(short)283,(short)612,""));
		session.writeAndFlush(new ServerPacket.ShowEvent(NumUtil.newAtomicId(),(short)4,(short)283,(short)611,""));
		session.writeAndFlush(new ServerPacket.ShowEvent(NumUtil.newAtomicId(),(short)4,(short)283,(short)613,""));
	}


}
