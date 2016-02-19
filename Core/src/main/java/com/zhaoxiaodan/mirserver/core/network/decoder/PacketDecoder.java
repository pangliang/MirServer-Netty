package com.zhaoxiaodan.mirserver.core.network.decoder;

import com.zhaoxiaodan.mirserver.core.network.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;

/**
 * Created by liangwei on 16/2/16.
 */
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

	private final String packetPackageName ;

	public PacketDecoder(String packetPackageName) {
		this.packetPackageName = packetPackageName;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		in = in.order(ByteOrder.LITTLE_ENDIAN);

		int size = in.readableBytes();

		if (size < Packet.DEFAULT_HEADER_SIZE)
			throw new Packet.WrongFormatException("packet size < " + Packet.DEFAULT_HEADER_SIZE);

		short  type  = in.getShort(1 + 1 + 4);

		Class<? extends Packet> packetClass;



		if (requestTypeMap.containsKey(type)) {
			packetClass = (requestTypeMap.get(type));
		} else {
			packetClass = Packet.class;
		}

		Packet packet = packetClass.newInstance();
		packet.readPacket(in);

		out.add(packet);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}
