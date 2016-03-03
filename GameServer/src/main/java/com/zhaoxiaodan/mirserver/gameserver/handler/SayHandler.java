package com.zhaoxiaodan.mirserver.gameserver.handler;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.types.Direction;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;

public class SayHandler extends PlayerHandler {


	static int r = 0;
	static short x = 280;
	static short y = 610;

	@Override
	public void onPacket(ClientPacket packet, Player player) throws Exception {

		r=0;
		x = (short) (player.currMapPoint.x+1);
		y = player.currMapPoint.y;
		for(int i=0 ;i<100;i++){
//			x = (short) (280 + i % 10);
//			y = (short) (610 + i/10);

			x++;

			int id = NumUtil.newAtomicId();

			int f =  NumUtil.makeLong(NumUtil.makeWord((byte)50, (byte)0), r++);

			session.writeAndFlush(new ServerPacket.Turn(id, Direction.LEFT, x, y, f,0,(byte)3,r+"/255"));
//			session.writeAndFlush(new ServerPacket.FeatureChanged(1000, f, (short) 0));
		}


	}
}
