package com.zhaoxiaodan.mirserver.logingate;

import com.zhaoxiaodan.mirserver.core.network.Protocol;
import com.zhaoxiaodan.mirserver.core.network.Request;
import com.zhaoxiaodan.mirserver.logingate.request.LoginRequest;

import java.util.HashMap;

/**
 * Created by liangwei on 16/2/17.
 */
public class LoginGateProtocols extends HashMap<Short, Class<? extends Request>> {

	public LoginGateProtocols() {
		put(Protocol.CM_IDPASSWORD, LoginRequest.class);
	}
}
