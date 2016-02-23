package com.zhaoxiaodan.mirserver.network.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 解密, request有cmdIndex , response没有
 */
public class PacketBit6Decoder extends MessageToMessageDecoder<ByteBuf> {

	public byte[] decode6BitBuf(byte[] src) {
		final byte[] Decode6BitMask = {(byte) 0xfc, (byte) 0xf8, (byte) 0xf0, (byte) 0xe0, (byte) 0xc0};

		int    len  = src.length;
		byte[] dest = new byte[len];

		int destPos = 0;
		int bitPos  = 2;
		int madeBit = 0;

		byte ch = 0, chCode = 0, tmp = 0;

		for (int i = 0; i < len; i++) {
			if ((src[i] - 0x3c) >= 0) {
				ch = (byte) (src[i] - 0x3c);
			} else {
				destPos = 0;
				break;
			}

			if (destPos >= len)
				break;

			if (madeBit + 6 >= 8) {
				chCode = (byte) (tmp | ((ch & 0x3f) >> (6 - bitPos)));
				dest[destPos++] = chCode;

				madeBit = 0;
				if (bitPos < 6) {
					bitPos += 2;
				} else {
					bitPos = 2;
					continue;
				}
			}

			tmp = (byte) ((ch << bitPos) & Decode6BitMask[bitPos - 2]);

			madeBit += 8 - bitPos;
		}

		return dest;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		ByteBuf buf = Unpooled.buffer();

		in.readByte(); //  # , 在这里就去掉 头尾

		byte[] content = new byte[in.readableBytes() - 1];
		in.readBytes(content);
		byte[] bit6DecodeContent = decode6BitBuf(content);
		buf.writeBytes(bit6DecodeContent);

		in.readByte(); //  !

		out.add(buf);
	}
}
