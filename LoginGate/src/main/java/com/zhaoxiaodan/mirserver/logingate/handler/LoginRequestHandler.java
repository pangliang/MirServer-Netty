package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.core.network.Request;
import com.zhaoxiaodan.mirserver.core.network.RequestHandler;
import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.logingate.request.LoginRequest;
import com.zhaoxiaodan.mirserver.logingate.response.IdNotFoundResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by liangwei on 16/2/18.
 */
public class LoginRequestHandler implements RequestHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Request request) {
		LoginRequest loginRequest = (LoginRequest) request;

		Player player = new Player();
		player.setUsername(loginRequest.getUsername());



		ctx.writeAndFlush(new IdNotFoundResponse());
	}

}
