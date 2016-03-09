package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.Race;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import groovy.lang.GroovyObject;

public class Merchant extends BaseObject {

	public String       scriptName;
	public GroovyObject scriptInstance;

	@Override
	public int getFeature() {
		return NumUtil.makeLong(NumUtil.makeWord(Race.Merchant.id, (byte) 0), 12);
	}

	@Override
	public short getFeatureEx() {
		return 0;
	}

	@Override
	public int getStatus() {
		return 0;
	}

	@Override
	public void onTick() {

	}

	public void onClick(int inGameId, Player player) {
		scriptInstance.invokeMethod("main", new Object[]{this, player});
	}

	public void sayTo(String msg,Player player){
		player.session.sendPacket(new ServerPacket.MerchantSay(this.inGameId,msg,this.name));
	}
}
