package com.zhaoxiaodan.mirserver.network.decoder;

import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class ServerPacketDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		int    pidPos     = 4;
		short  protocolId = in.getShort(pidPos);

		Protocol protocol = Protocol.getServerProtocol(protocolId);

		Class<? extends ServerPacket> packetClass;
		if (null == protocol) {
			LogManager.getLogger().error("unknow protocol id {}",protocolId);
			packetClass = ServerPacket.class;
		} else {
			try {
				packetClass = (Class<? extends ServerPacket>) Class.forName(ServerPacket.class.getCanonicalName() + "$" + protocol.name);
			} catch (ClassNotFoundException e) {
				packetClass = ServerPacket.class;
			}
		}
		ServerPacket packet = packetClass.newInstance();
		packet.readPacket(in);
		packet.protocol = protocol;

		out.add(packet);
	}
}
