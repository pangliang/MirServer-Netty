package com.zhaoxiaodan.mirserver.core.network;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by liangwei on 16/2/18.
 */
public interface RequestHandler {
	public void exce(ChannelHandlerContext ctx, Request msg);
}
