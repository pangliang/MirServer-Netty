package com.zhaoxiaodan.mirserver.logingate.response;

import com.zhaoxiaodan.mirserver.core.Protocol;
import com.zhaoxiaodan.mirserver.core.SocketMessage;

/**
 * Created by liangwei on 16/2/17.
 */
public class IdNotFoundResponse extends SocketMessage {
	
	public IdNotFoundResponse(byte cmdIndex) {
		super(Protocol.SM_ID_NOTFOUND, cmdIndex, "");
	}
}
