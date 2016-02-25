package com.zhaoxiaodan.mirserver.network.decoder;

import com.zhaoxiaodan.mirserver.network.packets.Packet;
import com.zhaoxiaodan.mirserver.network.Protocol;
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

		short  protocolId;

		if('*' == in.getByte(1) && '*' == in.getByte(2)){
			//connect to gameserver packet, format : cmdIndex**loginId/characterName/clientVersion/cert
			protocolId = Protocol.CM_GAMELOGIN.id;
		}else{
			//normal index packet
			int pidPos = 1 + 4;
			protocolId = in.getShort(pidPos);
		}
		Packet packet     = decodePacket(protocolId, in);
		out.add(packet);
	}
}
