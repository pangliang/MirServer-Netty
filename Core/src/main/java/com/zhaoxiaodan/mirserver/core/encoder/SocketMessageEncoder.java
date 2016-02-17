package com.zhaoxiaodan.mirserver.core.encoder;

import com.zhaoxiaodan.mirserver.core.SocketMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by liangwei on 16/2/17.
 */
public class SocketMessageEncoder extends MessageToMessageEncoder<SocketMessage> {

	@Override
	protected void encode(ChannelHandlerContext ctx, SocketMessage msg, List<Object> out) throws Exception {
		ByteBuf buf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

		msg.writeToByteBuf(buf);

		out.add(buf);
	}
}
