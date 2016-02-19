package com.zhaoxiaodan.mirserver.core.network.decoder;

import com.zhaoxiaodan.mirserver.core.network.Packet;
import com.zhaoxiaodan.mirserver.core.network.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * Created by liangwei on 16/2/16.
 */
public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

	//封包所在的包名
	private final String packetPackageName;

	public PacketDecoder(String packetPackageName) {
		this.packetPackageName = packetPackageName;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		in = in.order(ByteOrder.LITTLE_ENDIAN);

		int size = in.readableBytes();

		if (size < Packet.DEFAULT_HEADER_SIZE)
			throw new Packet.WrongFormatException("packet size < " + Packet.DEFAULT_HEADER_SIZE);

		short protocolId = in.getShort(1 + 4);  // cmdIndex + regoc

		String protocolName = Protocol.getName(protocolId);
		if (null == protocolName) {
			throw new Exception("unknow protocol id:" + protocolId);
		}

		Class<? extends Packet> packetClass;
		try{
			packetClass = (Class<? extends Packet>) Class.forName(packetPackageName + "$" + protocolName);
		}catch (ClassNotFoundException e)
		{
			packetClass = Packet.class;
		}
		Packet                  packet      = packetClass.newInstance();
		packet.readPacket(in);

		out.add(packet);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
	}
}
