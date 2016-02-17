package com.zhaoxiaodan.mirserver.logingate.request;

import com.zhaoxiaodan.mirserver.core.Protocol;
import com.zhaoxiaodan.mirserver.core.SocketMessage;
import io.netty.buffer.ByteBuf;

/**
 * Created by liangwei on 16/2/17.
 */
public class LoginRequest extends SocketMessage {

	private static final char PARAM_SPLIT_CHAR = '/';

	private String username;
	private String password;

	@Override
	public void fromByteBuf(ByteBuf in) throws WrongFormatException {
		super.fromByteBuf(in);

		int pos = 0;
		if ((pos = body.indexOf(PARAM_SPLIT_CHAR)) > 0) {
			username = body.substring(0, pos);
			password = body.substring(pos + 1);
		} else {
			throw new WrongFormatException("PARAM_SPLIT_CHAR not found");
		}
	}

	public LoginRequest(byte cmdIndex, String username, String password) throws WrongFormatException {
		super(Protocol.CM_IDPASSWORD, cmdIndex, username + PARAM_SPLIT_CHAR + password);
	}
}
