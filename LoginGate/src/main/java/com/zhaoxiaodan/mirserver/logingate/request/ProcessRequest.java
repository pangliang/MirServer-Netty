package com.zhaoxiaodan.mirserver.logingate.request;

import com.zhaoxiaodan.mirserver.core.Protocol;
import com.zhaoxiaodan.mirserver.core.SocketMessage;

/**
 * Created by liangwei on 16/2/17.
 */
public class ProcessRequest extends SocketMessage {

	public ProcessRequest(byte cmdIndex) throws WrongFormatException {
		super(Protocol.CM_PROTOCOL, cmdIndex);
	}
}
