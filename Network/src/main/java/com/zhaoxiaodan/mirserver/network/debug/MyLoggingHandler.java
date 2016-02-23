package com.zhaoxiaodan.mirserver.network.debug;

import io.netty.channel.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by liangwei on 16/2/18.
 */
public class MyLoggingHandler extends ChannelHandlerAdapter {

	public enum Type {
		Read, Write, Both
	}

	private LoggingHandler loggingHandler;
	private Type           type;

	public MyLoggingHandler(Type type) {
		loggingHandler = new LoggingHandler(LogLevel.INFO);
		this.type = type;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (type == Type.Read || type == Type.Both)
			loggingHandler.channelRead(ctx, msg);
		else
			ctx.fireChannelRead(msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (type == Type.Write || type == Type.Both)
			loggingHandler.write(ctx, msg, promise);
		else
			ctx.write(msg, promise);
	}
}
