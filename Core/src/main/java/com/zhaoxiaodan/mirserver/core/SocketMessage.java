package com.zhaoxiaodan.mirserver.core;

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

/**
 * 请求封包类, 封包数据格式: * #符号开头 + 头部 + body + !符号结尾
 * <pre>
 * +-----------------------------------------------------------------------------------------+
 * |  0  |  1  |  2  3  4  5  |  6  7  |  8  9  |  10  11  |  12  13  |  14 ..... n -1 |  n  |
 * +-----------------------------------------------------------------------------------------+
 * |  #  |              header                                        |      body      |  !  |
 * +-----------------------------------------------------------------------------------------+
 * |  #  |index|    recog     |  type  |    p1  |    p2    |    p3    |      body      |  !  |
 * +-----------------------------------------------------------------------------------------+
 * </pre>
 * 不同类型的请求, body有各自的格式,比如login封包 body 用/符号分隔, 格式为:帐号/密码, 例如:
 * <pre>
 * +-------------------------------------------------+
 * |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 * +--------+-------------------------------------------------+----------------+
 * |00000000| 23 31 3c 3c 3c 3c 3c 49 40 43 3c 3c 3c 3c 3c 3c |#1<<<<<I@C<<<<<<|
 * |00000010| 3c 3c 48 4f 44 6f 47 6f 40 6e 48 6c 21          |<<HODoGo@nHl!   |
 * +--------+-------------------------------------------------+----------------+
 * 解密后得到:
 * +-------------------------------------------------+
 * |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 * +--------+-------------------------------------------------+----------------+
 * |00000000| 23 31 00 00 00 00 d1 07 00 00 00 00 00 00 31 32 |#1............12|
 * |00000010| 33 2f 31 32 00 00 00 00 00 00 00 00 21          |3/12........!   |
 * +--------+-------------------------------------------------+----------------+
 * </pre>
 * 装配后得到类:
 * <p/>
 * SocketMessage{  header=Header{cmdIndx=2, recog=0, type=2001, p1=0, p2=0, p3=0}, body='123/123'}
 */
public class SocketMessage {

	public static final int DEFAULT_HEADER_SIZE = 13;

	public Header header;
	public String body;

	public class Header {

		public final byte  cmdIndx;  // #号后面紧跟的序号, 响应包的序号要跟请求一直
		public final int   recog;     // 未知
		public final short type;      // 协议id
		public final short p1;        //
		public final short p2;
		public final short p3;

		private Header(byte cmdIndx, int recog, short type, short p1, short p2, short p3) {
			this.cmdIndx = cmdIndx;
			this.recog = recog;
			this.type = type;
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Header))
				return false;

			Header header = (Header) o;

			if (cmdIndx != header.cmdIndx)
				return false;
			if (recog != header.recog)
				return false;
			if (type != header.type)
				return false;
			if (p1 != header.p1)
				return false;
			if (p2 != header.p2)
				return false;
			return p3 == header.p3;

		}

		@Override
		public int hashCode() {
			int result = (int) cmdIndx;
			result = 31 * result + recog;
			result = 31 * result + (int) type;
			result = 31 * result + (int) p1;
			result = 31 * result + (int) p2;
			result = 31 * result + (int) p3;
			return result;
		}

		@Override
		public String toString() {
			return "Header{" +
					"cmdIndx=" + cmdIndx +
					", recog=" + recog +
					", type=" + type +
					", p1=" + p1 +
					", p2=" + p2 +
					", p3=" + p3 +
					'}';
		}
	}

	public SocketMessage() {

	}

	public SocketMessage(byte cmdIndex, int recog, short type, short p1, short p2, short p3, String body) {
		this.header = new Header(cmdIndex, recog, type, p1, p2, p3);
		this.body = body;
	}

	public SocketMessage(short type, byte cmdIndex, String body) {
		this(cmdIndex, 0, type, (short) 0, (short) 0, (short) 0, body);
	}

	public SocketMessage(short type, byte cmdIndex) {
		this(cmdIndex, 0, type, (short) 0, (short) 0, (short) 0, "");
	}

	public void fromByteBuf(ByteBuf in) throws WrongFormatException {
		int size = in.readableBytes();

		if (size < SocketMessage.DEFAULT_HEADER_SIZE + 2)
			throw new SocketMessage.WrongFormatException("packet size < " + (SocketMessage.DEFAULT_HEADER_SIZE + 2));

		if ('#' != in.getByte(0) || '!' != in.getByte(size - 1))
			throw new SocketMessage.WrongFormatException("start or end flag not found");

		in = in.order(ByteOrder.LITTLE_ENDIAN);

		byte sFlag = in.readByte();

		byte   cmdIndex = (byte) (in.readByte() - '0');
		int    recog    = in.readInt();
		short  type     = in.readShort();
		short  p1       = in.readShort();
		short  p2       = in.readShort();
		short  p3       = in.readShort();
		String body     = in.toString(in.readerIndex(), in.readableBytes() - 1, Config.DEFAULT_CHARSET).trim();

		in.readByte();

		this.header = new Header(cmdIndex, recog, type, p1, p2, p3);
		this.body = body;
	}

	public void writeToByteBuf(ByteBuf out){
		if(0 != this.header.cmdIndx)
			out.writeByte((byte)(this.header.cmdIndx + '0'));
		out.writeInt(this.header.recog);
		out.writeShort(this.header.type);
		out.writeShort(this.header.p1);
		out.writeShort(this.header.p2);
		out.writeShort(this.header.p3);
		if(!body.isEmpty()) {
			out.writeBytes(this.body.getBytes());
			out.writeByte('\0');
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof SocketMessage))
			return false;

		SocketMessage request = (SocketMessage) o;

		if (!header.equals(request.header))
			return false;
		return body.equals(request.body);

	}

	@Override
	public int hashCode() {
		int result = header.hashCode();
		result = 31 * result + body.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "SocketMessage{" +
				"header=" + header +
				", body='" + body + '\'' +
				'}';
	}

	public static class WrongFormatException extends Exception {

		public WrongFormatException(String msg) {
			super(msg);
		}
	}

}
