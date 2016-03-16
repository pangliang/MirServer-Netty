package com.zhaoxiaodan.mirserver.network.decoder;

import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;

import java.nio.ByteOrder;
import java.util.List;

public class ClientPacketDecoder extends MessageToMessageDecoder<ByteBuf> {

	private final String packetPackageName;

	public ClientPacketDecoder(String packetPackageName) {
		this.packetPackageName = packetPackageName;
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

		Protocol protocol = Protocol.getClientProtocol(protocolId);

		Class<? extends ClientPacket> packetClass;
		if (null == protocol) {
			LogManager.getLogger().error("unknow protocol id {}",protocolId);
			return ;
		} else {
			try {
				packetClass = (Class<? extends ClientPacket>) Class.forName(packetPackageName + "$" + protocol.name);
			} catch (ClassNotFoundException e) {
				packetClass = ClientPacket.class;
			}
		}
		ClientPacket packet = packetClass.newInstance();
		packet.readPacket(in);
		packet.protocol = protocol;


		out.add(packet);
	}
}
