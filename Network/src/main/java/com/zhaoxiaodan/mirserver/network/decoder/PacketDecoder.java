package com.zhaoxiaodan.mirserver.network.decoder;

import com.zhaoxiaodan.mirserver.network.Packet;
import com.zhaoxiaodan.mirserver.network.Protocol;
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
	public final boolean isIndexPacket;

	public PacketDecoder(String packetPackageName , boolean isIndexPacket) {
		this.packetPackageName = packetPackageName;
		this.isIndexPacket = isIndexPacket;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		in = in.order(ByteOrder.LITTLE_ENDIAN);

		int size = in.readableBytes();

		if (size < Packet.DEFAULT_HEADER_SIZE)
			throw new Exception("packet size < " + Packet.DEFAULT_HEADER_SIZE);

		int pidPos = isIndexPacket ? 1 + 4 : 4;

		short protocolId = in.getShort(pidPos);

		String protocolName = Protocol.getName(protocolId);
		if (null == protocolName) {
			throw new Exception("unknow protocol id:" + protocolId);
		}

		Class<? extends Packet> packetClass;
		try{
			packetClass = (Class<? extends Packet>) Class.forName(packetPackageName + "$" + protocolName);
		}catch (ClassNotFoundException e)
		{
			System.err.println(e.getMessage());
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
