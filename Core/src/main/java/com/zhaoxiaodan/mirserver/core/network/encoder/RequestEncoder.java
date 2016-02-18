package com.zhaoxiaodan.mirserver.core.network.encoder;

import com.zhaoxiaodan.mirserver.core.network.Request;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by liangwei on 16/2/17.
 */
public class RequestEncoder extends MessageToMessageEncoder<Request> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Request request, List<Object> out) throws Exception {

		ByteBuf buf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

		buf.writeByte(request.cmdIndex + '0');
		buf.writeInt(request.header.recog);
		buf.writeShort(request.header.type);
		buf.writeShort(request.header.p1);
		buf.writeShort(request.header.p2);
		buf.writeShort(request.header.p3);
		if (request.body != null && !request.body.isEmpty()) {
			buf.writeBytes(request.body.getBytes());
//			buf.writeByte('\0');
		}

		out.add(buf);
	}
}
