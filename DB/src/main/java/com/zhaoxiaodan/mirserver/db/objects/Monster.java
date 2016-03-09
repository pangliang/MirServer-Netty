package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.StdMonster;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import groovy.lang.GroovyObject;

public class Monster extends BaseObject {

	public GroovyObject scriptInstance;

	public StdMonster stdMonster;

	public long lastWalkTime;

	public short hp;

	public void damage(BaseObject source ,short power){
		this.hp = (short) (this.hp - power);
		ServerPacket packet = new ServerPacket.Struck(this.inGameId,this.hp,this.getMaxHp(),100);
		broadcast(packet);
	}

	public short getMaxHp(){
		return stdMonster.hp;
	}

	@Override
	public String getName() {
		return stdMonster.name;
	}

	@Override
	public boolean see(BaseObject object) {

		// 怪物只管看到的Player
		if(!(object instanceof Player))
			return false;

		return super.see(object);
	}

	@Override
	public int getFeature() {
		return NumUtil.makeLong(NumUtil.makeWord(stdMonster.raceImg,(byte)0), stdMonster.appr);
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
}
