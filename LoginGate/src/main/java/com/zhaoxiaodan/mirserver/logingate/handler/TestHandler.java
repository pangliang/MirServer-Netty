package com.zhaoxiaodan.mirserver.logingate.handler;

import com.zhaoxiaodan.mirserver.core.network.SocketMessage;
import com.zhaoxiaodan.mirserver.logingate.response.IdNotFoundResponse;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by liangwei on 16/2/16.
 */
public class TestHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(final ChannelHandlerContext ctx) {
		System.out.println(ctx.toString());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		SocketMessage req = (SocketMessage) msg;
		System.out.println(this.getClass().getName() + " Read >> " + req);

		ctx.writeAndFlush(new IdNotFoundResponse());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}
