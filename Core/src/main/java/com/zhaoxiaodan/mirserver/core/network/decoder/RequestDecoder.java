package com.zhaoxiaodan.mirserver.core.network.decoder;

import com.zhaoxiaodan.mirserver.core.Config;
import com.zhaoxiaodan.mirserver.core.network.Request;
import com.zhaoxiaodan.mirserver.core.network.SocketMessage;
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
public class RequestDecoder extends MessageToMessageDecoder<ByteBuf> {

	private final Map<Short, Class<? extends Request>> protocols;

	public RequestDecoder(Map<Short, Class<? extends Request>> protocols) {
		this.protocols = protocols;
	}


	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		in.order(ByteOrder.LITTLE_ENDIAN);

		int size = in.readableBytes();

		if (size < SocketMessage.DEFAULT_HEADER_SIZE + 2)
			throw new SocketMessage.WrongFormatException("packet size < " + (SocketMessage.DEFAULT_HEADER_SIZE + 2));

		if ('#' != in.getByte(0) || '!' != in.getByte(size - 1))
			throw new SocketMessage.WrongFormatException("start or end flag not found");


		byte sFlag    = in.readByte();
		byte cmdIndex = in.readByte();
		cmdIndex = (byte) (cmdIndex - '0');

		int    recog = in.readInt();
		short  type  = in.readShort();
		short  p1    = in.readShort();
		short  p2    = in.readShort();
		short  p3    = in.readShort();
		String body  = in.toString(in.readerIndex(), in.readableBytes() - 1, Config.DEFAULT_CHARSET).trim();

		Class<? extends Request> requestClass;
		if (protocols.containsKey(type)) {
			requestClass = (protocols.get(type));
		} else {
			requestClass = Request.class;
		}

		// public Request(byte cmdIndex, int recog, short type, short p1, short p2, short p3, String body)
		Constructor<? extends Request> constructor = requestClass.getConstructor(byte.class, int.class, short.class, short.class, short.class, short.class, String.class);

		Request request = constructor.newInstance(cmdIndex, recog, type, p1, p2, p3, body);
		out.add(request);
	}
}
