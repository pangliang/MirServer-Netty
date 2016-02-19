package com.zhaoxiaodan.mirserver.core.network;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;

/**
 * Created by liangwei on 16/2/16.
 */
public class PacketDispatcher extends ChannelHandlerAdapter {

	private final HashMap<Class<? extends IndexPacket>, Class<? extends PacketHanlder>> requestHandlerMap;

	public PacketDispatcher(HashMap<Class<? extends IndexPacket>, Class<? extends PacketHanlder>> requestHandlerMap) {
		this.requestHandlerMap = requestHandlerMap;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (!(msg instanceof IndexPacket)) {
			throw new Exception("Recv msg is not instance of IndexPacket");
		}

		IndexPacket   req     = (IndexPacket) msg;
		PacketHanlder handler = requestHandlerMap.get(req.getClass()).newInstance();
		if (null != handler)
			handler.exce(ctx, req);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
	}
}
