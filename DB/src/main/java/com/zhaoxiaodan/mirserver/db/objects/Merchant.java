package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import groovy.lang.GroovyObject;

public class Merchant extends BaseObject {

	public String       scriptName;
	public GroovyObject scriptInstance;

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
