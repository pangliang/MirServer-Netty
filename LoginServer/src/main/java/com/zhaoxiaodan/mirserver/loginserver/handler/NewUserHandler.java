package com.zhaoxiaodan.mirserver.loginserver.handler;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Packet;
import com.zhaoxiaodan.mirserver.network.PacketHandler;
import com.zhaoxiaodan.mirserver.network.Protocol;
import io.netty.channel.ChannelHandlerContext;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class NewUserHandler implements PacketHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Packet packet) {
		ClientPackets.NewUser newUser = (ClientPackets.NewUser) packet;

		List<User> list = DB.query(User.class,Restrictions.eq("loginId", newUser.user.loginId));
		if(list.size() > 0)
		{
			ctx.writeAndFlush(new Packet(Protocol.SM_NEWID_FAIL));
			return ;
		}else{
			try{
				DB.saveOrUpdate(newUser.user);
				ctx.writeAndFlush(new Packet(Protocol.SM_NEWID_SUCCESS));
			}catch (Exception e)
			{
				ctx.writeAndFlush(new Packet(Protocol.SM_NEWID_FAIL));
				return ;
			}
		}
	}

}
