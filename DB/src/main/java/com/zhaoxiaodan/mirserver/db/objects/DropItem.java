package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.StdItem;

public class DropItem extends BaseObject {

	public String  scriptName;
	public StdItem stdItem;

	@Override
	public String getName() {
		return stdItem.attr.name;
	}

	@Override
	public int getFeature() {
		return 0;
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

	@Override
	public boolean see(BaseObject object) {
		if (!(object instanceof Player))
			return false;
		return super.see(object);
	}
}
