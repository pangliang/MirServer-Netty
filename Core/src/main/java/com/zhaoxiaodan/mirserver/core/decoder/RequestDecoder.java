package com.zhaoxiaodan.mirserver.core.decoder;

import com.zhaoxiaodan.mirserver.core.SocketMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;

/**
 * Created by liangwei on 16/2/16.
 */
public class RequestDecoder extends MessageToMessageDecoder<ByteBuf> {

	private final Map<Short, Class<? extends SocketMessage>> protocols;

	public RequestDecoder(Map<Short, Class<? extends SocketMessage>> protocols) {
		this.protocols = protocols;
	}


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		in = in.order(ByteOrder.LITTLE_ENDIAN);

		short type = in.getShort(2 + 4 - 1);

		SocketMessage request;
		if (protocols.containsKey(type)) {
			request = (protocols.get(type)).newInstance();
		} else {
			request = new SocketMessage();
		}
		request.fromByteBuf(in);
		out.add(request);
	}
}
