package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.entities.Config;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.StdItem;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

public class DropItem extends BaseObject {

	public StdItem stdItem;
	public Player  bolongTo;
	public long createTime = NumUtil.getTickCount();

	@Override
	public String getName() {
		return stdItem.attr.name;
	}

	@Override
	public void onTick(long now) {
		if (now > createTime + Config.DROP_ITEM_LIFE_TIME)
			leaveMap();
	}

	public boolean canPickUp(Player player) {
		if (this.bolongTo == null
				|| this.bolongTo.equals(player)
				|| (NumUtil.getTickCount() > createTime + Config.DROP_ITEM_PROTECT_TIME))
			return true;

		return false;
	}

	@Override
	public boolean see(BaseObject object) {
		if (!(object instanceof Player))
			return false;
		return super.see(object);
	}
}
