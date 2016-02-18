package com.zhaoxiaodan.mirserver.core.network.decoder;

import com.zhaoxiaodan.mirserver.core.Config;
import com.zhaoxiaodan.mirserver.core.network.Request;
import com.zhaoxiaodan.mirserver.core.network.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.lang.reflect.Constructor;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;

/**
 * Created by liangwei on 16/2/16.
 */
public class ResponseDecoder extends MessageToMessageDecoder<ByteBuf> {

	private final Map<Short, Class<? extends Response>> responseTypeMap;

	public ResponseDecoder(Map<Short, Class<? extends Response>> protocols) {
		this.responseTypeMap = protocols;
	}


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		in = in.order(ByteOrder.LITTLE_ENDIAN);

		int size = in.readableBytes();

		if (size < Request.DEFAULT_HEADER_SIZE + 2)
			throw new Request.WrongFormatException("packet size < " + (Request.DEFAULT_HEADER_SIZE + 2));

		if ('#' != in.getByte(0) || '!' != in.getByte(size - 1))
			throw new Request.WrongFormatException("start or end flag not found");


		byte sFlag    = in.readByte();
		byte cmdIndex = in.readByte();
		cmdIndex = (byte) (cmdIndex - '0');

		int    recog = in.readInt();
		short  type  = in.readShort();
		short  p1    = in.readShort();
		short  p2    = in.readShort();
		short  p3    = in.readShort();
		String body  = in.toString(in.readerIndex(), in.readableBytes() - 1, Config.DEFAULT_CHARSET).trim();

		Class<? extends Response> requestClass;
		if (responseTypeMap.containsKey(type)) {
			requestClass = (responseTypeMap.get(type));
		} else {
			requestClass = Response.class;
		}

		// public Response(int recog, short type, short p1, short p2, short p3, String body)
		Constructor<? extends Response> constructor = requestClass.getConstructor(int.class, short.class, short.class, short.class, short.class, String.class);

		Response msg = constructor.newInstance(cmdIndex, recog, type, p1, p2, p3, body);
		out.add(msg);
	}
}
