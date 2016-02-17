package com.zhaoxiaodan.mirserver.logingate.decoder;

import com.zhaoxiaodan.mirserver.logingate.request.ProcessRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by liangwei on 16/2/16.
 */
public class ProcessRequestDecoder extends MessageToMessageDecoder<ByteBuf> {

	private final Charset charset;

	public ProcessRequestDecoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		int size = in.readableBytes();

		if (size == 10 && "PROCESS".equals(in.toString(in.readerIndex() + 2, in.readableBytes() - 3, charset).trim())) {
			out.add(new ProcessRequest((byte) (in.getByte(1) - '0')));
		} else {
			in.retain();
			ctx.fireChannelRead(in);
		}
	}
}
