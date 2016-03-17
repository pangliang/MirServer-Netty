package com.zhaoxiaodan.mirserver.network.debug;

import io.netty.channel.*;

public class ExceptionHandler extends ChannelHandlerAdapter {

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, final ChannelPromise promise) throws Exception {
		promise.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (!future.isSuccess()) {
					promise.cause().printStackTrace();
				}
			}
		});
		ctx.write(msg, promise);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}
