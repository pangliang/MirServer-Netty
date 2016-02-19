package com.zhaoxiaodan.mirserver.core.network;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by liangwei on 16/2/16.
 */
public class PacketDispatcher extends ChannelHandlerAdapter {

	//handler处理器所在的包名
	private final String handlerPackageName;

	public PacketDispatcher(String handlerPackageName) {
		this.handlerPackageName = handlerPackageName;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object pakcet) throws Exception {

		if (!(pakcet instanceof Packet)) {
			throw new Exception("Recv msg is not instance of Packet");
		}

		String                         protocolName = pakcet.getClass().getSimpleName();   //Packet 就是通过 protocol id 反射出来的 name
		Class<? extends PacketHandler> handlerClass = (Class<? extends PacketHandler>) Class.forName(handlerPackageName + "." + protocolName + "Handler");
		PacketHandler                  handler      = handlerClass.newInstance();
		if (null != handler)
			handler.exce(ctx, (Packet)pakcet);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
	}
}
