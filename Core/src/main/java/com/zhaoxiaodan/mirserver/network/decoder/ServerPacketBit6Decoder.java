package com.zhaoxiaodan.mirserver.network.decoder;

import com.zhaoxiaodan.mirserver.network.Bit6Coder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 解密, request有cmdIndex , response没有
 */
public class ServerPacketBit6Decoder extends MessageToMessageDecoder<ByteBuf> {
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		ByteBuf buf = Unpooled.buffer();
		in.readByte(); //  # , 在这里就去掉 头尾

		byte[] bodyBytes = new byte[in.readableBytes() - 1];
		in.readBytes(bodyBytes);
		buf.writeBytes(Bit6Coder.decode6BitBuf(bodyBytes));

		in.readByte(); //  !

		out.add(buf);
	}
}
