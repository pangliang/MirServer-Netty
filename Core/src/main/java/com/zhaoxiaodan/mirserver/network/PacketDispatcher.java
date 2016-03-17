package com.zhaoxiaodan.mirserver.network;

import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import io.netty.channel.*;

public class PacketDispatcher extends ChannelHandlerAdapter {

	//handler处理器所在的包名
	private final String handlerPackageName;

	public PacketDispatcher(String handlerPackageName) {
		this.handlerPackageName = handlerPackageName;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object pakcet) throws Exception {

		if (!(pakcet instanceof ClientPacket)) {
			throw new Exception("Recv msg is not instance of Packet");
		}

		String                   protocolName = ((ClientPacket) pakcet).protocol.name;   //Packet 就是通过 protocol id 反射出来的 name
		Class<? extends Handler> handlerClass;
		try {
			handlerClass = (Class<? extends Handler>) Class.forName(handlerPackageName + "." + protocolName + "Handler");
		}catch(ClassNotFoundException e)
		{
			handlerClass = Handler.class;
		}
		Handler handler = handlerClass.newInstance();
		handler.exce(ctx, (ClientPacket) pakcet);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		//TODO 这里不是很清楚到底 使用哪个事件来处理 '连接' 和 '断开', 在哪里加入session, 哪里移出session比较合适 ?
		super.channelUnregistered(ctx);
		Class<? extends Handler> handlerClass = (Class<? extends Handler>) Class.forName(handlerPackageName + ".DisconnectHandler");
		Handler handler = handlerClass.newInstance();
		handler.onDisconnect(ctx);
	}

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
