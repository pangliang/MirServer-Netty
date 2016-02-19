package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.core.network.Protocol;
import com.zhaoxiaodan.mirserver.core.network.Request;
import com.zhaoxiaodan.mirserver.core.network.RequestHandler;
import com.zhaoxiaodan.mirserver.core.network.Response;
import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.logingate.request.LoginRequest;
import io.netty.channel.ChannelHandlerContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by liangwei on 16/2/18.
 */
public class LoginRequestHandler implements RequestHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Request request) {
		LoginRequest loginRequest = (LoginRequest) request;

		Session  session  = DB.getSession();
		Criteria criteria = session.createCriteria(Player.class);

		List<Player> list   = criteria.add(Restrictions.eq("username",loginRequest.getUsername())).list();
		if(1 != list.size())
		{
			ctx.writeAndFlush(new Response(Protocol.SM_ID_NOTFOUND));
			return ;
		}else{
			Player player = list.get(0);
			if(player.getPassword().equals(loginRequest.getPassword()))
			{

			}else{
				ctx.writeAndFlush(new Response(Protocol.SM_PASSWD_FAIL));
			}
		}



	}

}
