package com.zhaoxiaodan.mirserver.network.decoder;

import com.zhaoxiaodan.mirserver.network.Packet;
import com.zhaoxiaodan.mirserver.network.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

	//封包所在的包名
	private final String packetPackageName;

	public PacketDecoder(String packetPackageName) {
		this.packetPackageName = packetPackageName;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		in = in.order(ByteOrder.LITTLE_ENDIAN);

		int    pidPos     = 4;
		short  protocolId = in.getShort(pidPos);
		Packet packet     = decodePacket(protocolId, in);
		out.add(packet);
	}

	protected Packet decodePacket(short protocolId, ByteBuf in) throws Exception {
		String protocolName = Protocol.get(protocolId).name();
		if (null == protocolName) {
			throw new Exception("unknow protocol id:" + protocolId);
		}

		Class<? extends Packet> packetClass;
		try {
			packetClass = (Class<? extends Packet>) Class.forName(packetPackageName + "$" + protocolName);
		} catch (ClassNotFoundException e) {
			System.err.println(e.getMessage());
			packetClass = Packet.class;
		}
		Packet packet = packetClass.newInstance();
		packet.readPacket(in);
		return packet;
	}
}
