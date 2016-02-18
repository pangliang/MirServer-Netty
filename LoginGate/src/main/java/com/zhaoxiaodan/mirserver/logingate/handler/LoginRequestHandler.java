package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.core.network.Request;
import com.zhaoxiaodan.mirserver.core.network.RequestHandler;
import com.zhaoxiaodan.mirserver.logingate.response.IdNotFoundResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by liangwei on 16/2/18.
 */
public class LoginRequestHandler implements RequestHandler {

	@Override
	public void exce(ChannelHandlerContext ctx, Request msg) {
		ctx.writeAndFlush(new IdNotFoundResponse());
	}
}
