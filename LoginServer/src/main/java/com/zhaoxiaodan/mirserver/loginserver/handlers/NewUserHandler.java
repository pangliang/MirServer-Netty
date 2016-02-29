package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class NewUserHandler extends Handler {

	@Override
	public void onPacket(Packet packet) throws Exception {
		ClientPackets.NewUser newUser = (ClientPackets.NewUser) packet;

		List<User> list = DB.query(User.class,Restrictions.eq("loginId", newUser.user.loginId));
		if(list.size() > 0)
		{
			session.writeAndFlush(new Packet(Protocol.SM_NEWID_FAIL));
			return ;
		}else{
			try{
				session.db.save(newUser.user);
				session.writeAndFlush(new Packet(Protocol.SM_NEWID_SUCCESS));
			}catch (Exception e)
			{
				session.writeAndFlush(new Packet(Protocol.SM_NEWID_FAIL));
				return ;
			}
		}
	}

}
