package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.Race;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

public class Merchant extends AnimalObject {

	public String scriptName;
	public String name;

	public Merchant() {
		this.hp = Integer.MAX_VALUE;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getPower() {
		return 0;
	}

	@Override
	public int getDefend() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void kill(AnimalObject animalObject) {

	}

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

	public void sayTo(String msg, Player player) {
		player.session.sendPacket(new ServerPacket.MerchantSay(this.inGameId, msg, this.getName()));
	}

	@Override
	public boolean see(BaseObject object) {
		if (!(object instanceof Player))
			return false;
		return super.see(object);
	}
}
