package com.zhaoxiaodan.mirserver.logingate;

import com.zhaoxiaodan.mirserver.core.network.Protocol;
import com.zhaoxiaodan.mirserver.core.network.Request;
import com.zhaoxiaodan.mirserver.core.network.RequestHandler;
import com.zhaoxiaodan.mirserver.logingate.handler.LoginRequestHandler;
import com.zhaoxiaodan.mirserver.logingate.request.LoginRequest;

import java.util.HashMap;

/**
 * Created by liangwei on 16/2/17.
 */
public class LoginGateProtocols {

	public static final HashMap<Short, Class<? extends Request>> Rquest_Type_Map = new HashMap<Short, Class<? extends Request>>(){
		{
			put(Protocol.CM_IDPASSWORD, LoginRequest.class);
		}
	};

	public static final HashMap<Class<? extends Request>, Class<? extends RequestHandler>> Rquest_Handler_Map = new HashMap<Class<? extends Request>, Class<? extends RequestHandler>>(){
		{
			put(LoginRequest.class, LoginRequestHandler.class);
		}
	};
}
