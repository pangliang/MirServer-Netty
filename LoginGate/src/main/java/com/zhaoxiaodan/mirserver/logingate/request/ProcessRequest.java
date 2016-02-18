package com.zhaoxiaodan.mirserver.logingate.request;

import com.zhaoxiaodan.mirserver.core.network.Protocol;
import com.zhaoxiaodan.mirserver.core.network.Request;

/**
 * Created by liangwei on 16/2/17.
 */
public class ProcessRequest extends Request {

	public ProcessRequest(byte cmdIndex){
		super(Protocol.CM_PROTOCOL, cmdIndex);
	}
}
