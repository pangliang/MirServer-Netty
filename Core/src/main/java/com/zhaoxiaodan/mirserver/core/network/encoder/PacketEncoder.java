package com.zhaoxiaodan.mirserver.core.network.encoder;

import com.zhaoxiaodan.mirserver.core.network.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by liangwei on 16/2/17.
 */
public class PacketEncoder extends MessageToMessageEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
		ByteBuf buf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
		packet.writePacket(buf);
		out.add(buf);
	}
}
