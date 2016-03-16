package com.zhaoxiaodan.mirserver.gameserver.decoder;

import com.zhaoxiaodan.mirserver.gameserver.GameServerPackets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 解密, request有cmdIndex , response没有
 */
public class ActionStatusPacketBit6Decoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		if ('+' == in.getByte(1)) {
			// Status 包: 格式如: #+GOOD/1234567
			GameServerPackets.ActionStatus actionStatus = new GameServerPackets.ActionStatus();
			actionStatus.readPacket(in);
			out.add(actionStatus);
		}else{
			out.add(in);
		}

	}
}
