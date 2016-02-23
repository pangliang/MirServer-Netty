package com.zhaoxiaodan.mirserver.network.decoder;

import com.zhaoxiaodan.mirserver.network.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteOrder;
import java.util.List;

public class ClientPacketDecoder extends PacketDecoder {


	public ClientPacketDecoder(String packetPackageName) {
		super(packetPackageName);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		in = in.order(ByteOrder.LITTLE_ENDIAN);

		int pidPos = 1 + 4;

		short  protocolId = in.getShort(pidPos);
		Packet packet     = decodePacket(protocolId, in);
		out.add(packet);
	}
}
