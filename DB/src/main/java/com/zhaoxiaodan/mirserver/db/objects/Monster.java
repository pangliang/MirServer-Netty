package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.StdMonster;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import groovy.lang.GroovyObject;

public class Monster extends BaseObject {

	public GroovyObject scriptInstance;

	public StdMonster stdMonster;

	public long lastWalkTime;

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
