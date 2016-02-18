package com.zhaoxiaodan.mirserver.core.network;

/**
 * Created by liangwei on 16/2/18.
 */
public class Response extends SocketMessage {

	public Response(short type, String body) {
		super(0, type, (short) 0, (short) 0, (short) 0, body);
	}

	public Response(short type)
	{
		this(type,null);
	}
}
