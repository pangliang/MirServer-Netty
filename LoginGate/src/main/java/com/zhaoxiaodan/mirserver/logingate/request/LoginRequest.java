package com.zhaoxiaodan.mirserver.logingate.request;

import com.zhaoxiaodan.mirserver.core.network.Protocol;
import com.zhaoxiaodan.mirserver.core.network.Request;

/**
 * Created by liangwei on 16/2/17.
 */
public class LoginRequest extends Request {

	private static final char PARAM_SPLIT_CHAR = '/';

	private String username;
	private String password;

	public LoginRequest(byte cmdIndex, int recog, short type, short p1, short p2, short p3, String body) {
		super(cmdIndex, recog, type, p1, p2, p3, body);

		int pos = 0;
		if (body != null && (pos = body.indexOf(PARAM_SPLIT_CHAR)) > 0) {
			username = body.substring(0, pos);
			password = body.substring(pos + 1);
		}
	}

	public LoginRequest(byte cmdIndex, String username, String password) throws WrongFormatException {
		super(Protocol.CM_IDPASSWORD, cmdIndex, username + PARAM_SPLIT_CHAR + password);

		this.username = username;
		this.password = password;
	}
}
