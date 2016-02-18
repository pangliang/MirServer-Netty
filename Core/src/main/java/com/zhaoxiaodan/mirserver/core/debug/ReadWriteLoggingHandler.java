package com.zhaoxiaodan.mirserver.core.debug;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * Created by liangwei on 16/2/18.
 */
public class ReadWriteLoggingHandler extends ChannelHandlerAdapter {
	private LoggingHandler loggingHandler;

	public ReadWriteLoggingHandler(LogLevel logLevel) {
		loggingHandler = new LoggingHandler(logLevel);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		loggingHandler.channelRead(ctx, msg);
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		loggingHandler.write(ctx, msg, promise);
	}
}
