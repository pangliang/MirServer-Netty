package com.zhaoxiaodan.mirserver.core.network;

/**
 * 请求类, 比默认封包 在 '#'号后多了一个 index 序号
 */
public class Request extends SocketMessage {

	public byte cmdIndex;  // #号后面紧跟的序号, 响应包的序号要跟请求一直

	public Request(byte cmdIndex, int recog, short type, short p1, short p2, short p3, String body) {
		super(recog, type, p1, p2, p3, body);
		this.cmdIndex = cmdIndex;
	}

	public Request(short type, byte cmdIndex, String body) {
		this(cmdIndex, 0, type, (short) 0, (short) 0, (short) 0, body);
	}

	public Request(short type, byte cmdIndex) {
		this(type, cmdIndex, null);
	}
}
