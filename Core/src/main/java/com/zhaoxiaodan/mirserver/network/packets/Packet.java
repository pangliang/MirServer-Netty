package com.zhaoxiaodan.mirserver.network.packets;

import com.alibaba.fastjson.JSON;
import com.zhaoxiaodan.mirserver.network.Protocol;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

/**
 * 请求封包类, 封包数据格式: * #符号开头 + 头部 + body + !符号结尾
 * <pre>
 * +-----------------------------------------------------------------------------------------+
 * |  0  |  1  |  2  3  4  5  |  6  7  |  8  9  |  10  11  |  12  13  |  14 ..... n -1 |  n  |
 * +-----------------------------------------------------------------------------------------+
 * |  #  |              header                                        |      body      |  !  |
 * +-----------------------------------------------------------------------------------------+
 * |  #  |index|    recog        |protocol|    p1  |    p2    |    p3    |      body      |  !  |
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
 * <p>
 * Packet{  cmdIndx=2, recog=0, protocol=2001, p1=0, p2=0, p3=0, body='123/123'}
 */
public class Packet implements Parcelable {

	public static final int    DEFAULT_HEADER_SIZE    = 12;
	public static final char   CONTENT_SEPARATOR_CHAR = '/';
	public static final String CONTENT_SEPARATOR_STR  = new String(Character.toString(CONTENT_SEPARATOR_CHAR));

	public int      recog;     // 未知
	public Protocol protocol;      // 协议id
	public short    p1;        //
	public short    p2;
	public short    p3;

	private ByteBuf in;

	public Packet() {
	}

	public Packet(int recog, Protocol protocol, short p1, short p2, short p3) {
		this.recog = recog;
		this.protocol = protocol;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	public Packet(Protocol protocol) {
		this(0, protocol, (short) 0, (short) 0, (short) 0);
	}

	public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
		recog = in.readInt();
		in.readShort();
		p1 = in.readShort();
		p2 = in.readShort();
		p3 = in.readShort();

		this.in = in.retain();
	}

	public void writePacket(ByteBuf out) {
		out.writeInt(recog);
		out.writeShort(protocol == null ? 0 : protocol.id);
		out.writeShort(p1);
		out.writeShort(p2);
		out.writeShort(p3);
	}

	public byte[] remainBytes() {
		if (null == in)
			return null;
		byte[] buf = new byte[in.readableBytes()];
		in.readBytes(buf);
		return buf;
	}

	public String remainString() {
		byte[] buf = remainBytes();
		if (null == buf)
			return null;
		else
			return new String(buf).trim();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + JSON.toJSONString(this) + " ,remain:" + (in == null? "": in.toString(Charset.defaultCharset()));
	}


}
