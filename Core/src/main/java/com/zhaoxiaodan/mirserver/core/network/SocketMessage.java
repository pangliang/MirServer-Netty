package com.zhaoxiaodan.mirserver.core.network;

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
public abstract class SocketMessage {

	public static final int DEFAULT_HEADER_SIZE = 13;

	public Header header;
	public String body;

	public class Header {

		public final int   recog;     // 未知
		public final short type;      // 协议id
		public final short p1;        //
		public final short p2;
		public final short p3;

		protected Header(int recog, short type, short p1, short p2, short p3) {
			this.recog = recog;
			this.type = type;
			this.p1 = p1;
			this.p2 = p2;
			this.p3 = p3;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Header)) return false;

			Header header = (Header) o;

			if (recog != header.recog) return false;
			if (type != header.type) return false;
			if (p1 != header.p1) return false;
			if (p2 != header.p2) return false;
			return p3 == header.p3;

		}

		@Override
		public int hashCode() {
			int result = recog;
			result = 31 * result + (int) type;
			result = 31 * result + (int) p1;
			result = 31 * result + (int) p2;
			result = 31 * result + (int) p3;
			return result;
		}

		@Override
		public String toString() {
			return "Header{" +
					"recog=" + recog +
					", type=" + type +
					", p1=" + p1 +
					", p2=" + p2 +
					", p3=" + p3 +
					'}';
		}
	}

	public SocketMessage(int recog, short type, short p1, short p2, short p3, String body) {
		this.header = new Header(recog, type, p1, p2, p3);
		this.body = body;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SocketMessage)) return false;

		SocketMessage that = (SocketMessage) o;

		if (header != null ? !header.equals(that.header) : that.header != null) return false;
		return body != null ? body.equals(that.body) : that.body == null;

	}

	@Override
	public int hashCode() {
		int result = header != null ? header.hashCode() : 0;
		result = 31 * result + (body != null ? body.hashCode() : 0);
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
