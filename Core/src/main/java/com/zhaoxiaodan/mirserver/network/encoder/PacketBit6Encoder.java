package com.zhaoxiaodan.mirserver.network.encoder;

import com.zhaoxiaodan.mirserver.network.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.Arrays;
import java.util.List;

public class PacketBit6Encoder extends MessageToMessageEncoder<ByteBuf> {

	public byte[] encoder6BitBuf(byte[] src) {
		int    len     = src.length;
		int destLen = (len/3)*4+10;
		byte[] dest    = new byte[destLen];
		int    destPos = 0, resetCount = 0;
		byte   chMade  = 0, chRest = 0;

		for (int i = 0; i < len ; i++) {
			if (destPos >= destLen)
				break;

			chMade = (byte) ((chRest | ((src[i] & 0xff) >> (2 + resetCount))) & 0x3f);
			chRest = (byte) ((((src[i] & 0xff) << (8 - (2 + resetCount))) >> 2) & 0x3f);

			resetCount += 2;
			if(resetCount < 6)
			{
				dest[destPos++] = (byte)(chMade + 0x3c);
			}else{
				if(destPos < destLen - 1)
				{
					dest[destPos++] = (byte)(chMade + 0x3c);
					dest[destPos++] = (byte)(chRest + 0x3c);
				}else {
					dest[destPos++] = (byte)(chMade + 0x3c);
				}

				resetCount = 0;
				chRest = 0;
			}
		}
		if(resetCount > 0 )
			dest[destPos++] = (byte)(chRest + 0x3c);

		dest[destPos] = 0;
		return Arrays.copyOfRange(dest,0,destPos);

	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		ByteBuf buf = Unpooled.buffer();

		buf.writeByte('#');     // 加上 头分割符

		byte[] header = new byte[Packet.DEFAULT_HEADER_SIZE];
		in.readBytes(header);
		buf.writeBytes(encoder6BitBuf(header));

		byte[] body = new byte[in.readableBytes()];
		in.readBytes(body);
		buf.writeBytes(encoder6BitBuf(body));

		buf.writeByte('!');     // 加上 尾分割符

		out.add(buf);
	}
}
