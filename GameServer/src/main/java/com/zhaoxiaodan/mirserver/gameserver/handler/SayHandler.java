package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

import java.util.Random;

public class SayHandler extends PlayerHandler {


	static int r = 0;
	static short x = 289;
	static short y = 618;

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		x = (short) (x > 289+10 ? 289:x+1);
		y = x == 289 ? (short) (y + 1) :y;

		short i = (short) (5 - new Random().nextInt(10));
		int id = NumUtil.newAtomicId();
		session.writeAndFlush(new ServerPacket.Turn(id, Direction.LEFT, x, y, 2,0,(byte)0,"HelloWord/255"));
//		session.writeAndFlush(new ServerPacket.FeatureChanged(id, NumUtil.makeLong(0, r), (short) 0));

	}
}
