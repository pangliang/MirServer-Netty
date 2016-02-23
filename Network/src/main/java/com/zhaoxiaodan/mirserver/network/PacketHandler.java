package com.zhaoxiaodan.mirserver.network;

import io.netty.channel.ChannelHandlerContext;

public interface PacketHandler {
	public void exce(ChannelHandlerContext ctx, Packet packet);
}
