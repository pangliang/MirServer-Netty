package com.zhaoxiaodan.mirserver.core.network;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;

/**
 * Created by liangwei on 16/2/16.
 */
public class RequestDispatcher extends ChannelHandlerAdapter {

	private final HashMap<Class<? extends Request>, Class<? extends RequestHandler>> requestHandlerMap;

	public RequestDispatcher(HashMap<Class<? extends Request>, Class<? extends RequestHandler>> requestHandlerMap) {
		this.requestHandlerMap = requestHandlerMap;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if(!(msg instanceof Request))
		{
			throw new Exception("Recv msg is not instance of Request");
		}

		Request req = (Request) msg;
		RequestHandler handler = requestHandlerMap.get(req.getClass()).newInstance();

		handler.exce(ctx,req);
;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
}
