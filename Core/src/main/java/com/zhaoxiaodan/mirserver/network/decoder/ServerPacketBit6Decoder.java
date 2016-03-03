package com.zhaoxiaodan.mirserver.network.decoder;

import com.zhaoxiaodan.mirserver.network.Bit6Coder;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.ByteOrder;
import java.util.List;

/**
 * 解密, request有cmdIndex , response没有
 */
public class ServerPacketBit6Decoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		if ('+' == in.getByte(1)) {
			// Status 包: 格式如: #+GOOD/1234567
			ServerPacket.Status status = new ServerPacket.Status();
			status.readPacket(in);
			out.add(status);
		} else {
			ByteBuf buf = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);
			in.readByte(); //  # , 在这里就去掉 头尾

			byte[] headerByte = new byte[(int)(ServerPacket.DEFAULT_HEADER_SIZE * 4 / 3)];
			in.readBytes(headerByte);
			buf.writeBytes(Bit6Coder.decode6BitBuf(headerByte));

			Protocol protocol = Protocol.getServerProtocol(buf.getShort(4));
			if (protocol != null && protocol.lenghtOfSections != null) {
				for (int lenght : protocol.lenghtOfSections) {
					int bit6Len = (int) (lenght * 4 / 3.0 + 0.9); // 1.0 =1 , 1.1 =2
					if (in.readableBytes() > bit6Len) {
						byte[] bodyBytes = new byte[bit6Len];
						in.readBytes(bodyBytes);
						buf.writeBytes(Bit6Coder.decode6BitBuf(bodyBytes));
					} else {
						break;
					}
				}
			}
			byte[] bodyBytes = new byte[in.readableBytes() - 1];
			in.readBytes(bodyBytes);
			buf.writeBytes(Bit6Coder.decode6BitBuf(bodyBytes));

			in.readByte(); //  !

			out.add(buf);
		}

	}
}
