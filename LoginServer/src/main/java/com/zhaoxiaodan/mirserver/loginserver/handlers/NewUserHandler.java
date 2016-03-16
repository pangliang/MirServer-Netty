package com.zhaoxiaodan.mirserver.loginserver.handlers;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.gameserver.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.LoginClientPackets;
import com.zhaoxiaodan.mirserver.network.Handler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class NewUserHandler extends Handler {

	@Override
	public void onPacket(ClientPacket packet) throws Exception {
		LoginClientPackets.NewUser newUser = (LoginClientPackets.NewUser) packet;

		List<User> list = DB.query(User.class,Restrictions.eq("loginId", newUser.user.loginId));
		if(list.size() > 0)
		{
			session.sendPacket(new ServerPacket(Protocol.SM_NEWID_FAIL));
			return ;
		}else{
			try{
				DB.save(newUser.user);
				session.sendPacket(new ServerPacket(Protocol.SM_NEWID_SUCCESS));
			}catch (Exception e)
			{
				session.sendPacket(new ServerPacket(Protocol.SM_NEWID_FAIL));
				return ;
			}
		}
	}

}
