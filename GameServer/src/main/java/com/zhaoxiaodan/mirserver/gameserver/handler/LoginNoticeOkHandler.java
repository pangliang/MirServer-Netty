package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem;
import com.zhaoxiaodan.mirserver.db.entities.StdItem;
import com.zhaoxiaodan.mirserver.gameserver.engine.ItemEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
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
			if(null == player.currMapPoint)
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

		player.inGame = true;

		MapEngine.MapInfo currMap = MapEngine.getMapInfo(player.currMapPoint.mapName);

		session.writeAndFlush(new ServerPacket.NewMap(player.id, player.currMapPoint.x, player.currMapPoint.y, (short) 0, player.currMapPoint.mapName));
		session.writeAndFlush(new ServerPacket.MapDescription(-1, currMap.mapDescription));

		int   feature   = NumUtil.makeLong(NumUtil.makeWord((byte) 0, (byte) 0), NumUtil.makeWord((byte) 0, (byte) 0));
		short featureEx = NumUtil.makeWord((byte) 0, (byte) 0);
		session.writeAndFlush(new ServerPacket.Logon(player.id, player.currMapPoint.x, player.currMapPoint.y, (byte) 0, (byte) 0, feature, 0x400, featureEx));

		session.writeAndFlush(new ServerPacket.FeatureChanged(player.id, feature, featureEx));

		session.writeAndFlush(new ServerPacket.UserName(player.id, (short) 255, player.name));

		session.writeAndFlush(new ServerPacket(2, Protocol.SM_AREASTATE, (byte) 0, (byte) 0, (byte) 0));

		session.writeAndFlush(new ServerPacket.GameGoldName(player.gameGold, player.gamePoint, "游戏币", "游戏点数"));

		session.writeAndFlush(new ServerPacket.VersionFail(0, 0, 0));

		session.writeAndFlush(new ServerPacket.PlayerAbility(player.gold, player.gameGold, player.job, player.ability));

		session.writeAndFlush(new ServerPacket.ShowEvent(NumUtil.newAtomicId(),(short)4,(short)283,(short)612,""));
		session.writeAndFlush(new ServerPacket.ShowEvent(NumUtil.newAtomicId(),(short)4,(short)283,(short)611,""));
		session.writeAndFlush(new ServerPacket.ShowEvent(NumUtil.newAtomicId(),(short)4,(short)283,(short)613,""));
	}


}
