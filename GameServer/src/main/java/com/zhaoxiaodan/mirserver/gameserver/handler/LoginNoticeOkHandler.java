package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.db.entities.CharacterItem;
import com.zhaoxiaodan.mirserver.db.entities.StdItem;
import com.zhaoxiaodan.mirserver.gameserver.engine.ItemEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.MapEngine;
import com.zhaoxiaodan.mirserver.gameserver.engine.ScriptEngine;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

import java.util.Map;

public class LoginNoticeOkHandler extends CharacterHandler {

	@Override
	public void onPacket(Packet packet, Character character) throws Exception {

		if(character.ability.Level == 0){
			ScriptEngine.exce(ScriptEngine.Module.Character,"onCreate",character);
			session.db.update(character);

			Map<String,Integer> initItems = (Map<String, Integer>) ScriptEngine.exce(ScriptEngine.Module.Character,"getInitItems",character);
			for(String itemName : initItems.keySet()){
				StdItem       stdItem       = ItemEngine.getStdItemByName(itemName);
				CharacterItem characterItem = new CharacterItem();
				characterItem.character = character;
				characterItem.stdItem = stdItem;

				session.db.save(characterItem);
				character.items.add(characterItem);
			}
		}

		character.inGame = true;

		MapEngine.MapInfo currMap = MapEngine.getMapInfo(character.currMapPoint.mapName);

		session.writeAndFlush(new ServerPacket.NewMap(character.id, character.currMapPoint.x, character.currMapPoint.y, (short) 0, character.currMapPoint.mapName));
		session.writeAndFlush(new ServerPacket.MapDescription(-1, currMap.mapDescription));

		int   feature   = Packet.makeLong(Packet.makeWord((byte) 0, (byte) 0), Packet.makeWord((byte) 0, (byte) 0));
		short featureEx = Packet.makeWord((byte) 0, (byte) 0);
		session.writeAndFlush(new ServerPacket.Logon(character.id, character.currMapPoint.x, character.currMapPoint.y, (byte) 0, (byte) 0, feature, 0x400, featureEx));

		session.writeAndFlush(new ServerPacket.FeatureChanged(character.id, feature, featureEx));

		session.writeAndFlush(new ServerPacket.UserName(character.id, (short) 255, character.name));

		session.writeAndFlush(new ServerPacket(2, Protocol.SM_AREASTATE, (byte) 0, (byte) 0, (byte) 0));

		session.writeAndFlush(new ServerPacket.GameGoldName(character.gameGold, character.gamePoint, "游戏币", "游戏点数"));

		session.writeAndFlush(new ServerPacket.VersionFail(0, 0, 0));

		session.writeAndFlush(new ServerPacket.CharacterAbility(character.gold, character.gameGold, character.job, character.ability));

	}


}
