package com.zhaoxiaodan.mirserver.network.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * 解密, request有cmdIndex , response没有
 */
public class ClientPacketBit6Decoder extends PacketBit6Decoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		ByteBuf buf = Unpooled.buffer();
		in.readByte(); //  # , 在这里就去掉 头尾
		buf.writeByte(in.readByte());  //cmdIndex
		decode(in,buf);
		in.readByte(); //  !

		out.add(buf);
	}
}
