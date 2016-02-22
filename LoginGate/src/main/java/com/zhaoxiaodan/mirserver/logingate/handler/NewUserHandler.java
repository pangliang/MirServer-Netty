package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.network.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Packet;
import com.zhaoxiaodan.mirserver.network.PacketHandler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import io.netty.channel.ChannelHandlerContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class NewUserHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {
		ClientPackets.NewUser newUser = (ClientPackets.NewUser) packet;

		Session  session  = DB.getSession();
		Criteria criteria = session.createCriteria(User.class);

		List<User> list = criteria.add(Restrictions.eq("loginId", newUser.user.loginId)).list();
		if(list.size() > 0)
		{
			ctx.writeAndFlush(new Packet(Protocol.SM_NEWID_FAIL));
			return ;
		}else{
			try{
				session.save(newUser.user);
				session.flush();

				ctx.writeAndFlush(new Packet(Protocol.SM_NEWID_SUCCESS));
			}catch (Exception e)
			{
				ctx.writeAndFlush(new Packet(Protocol.SM_NEWID_FAIL));
				return ;
			}
		}
	}

}
