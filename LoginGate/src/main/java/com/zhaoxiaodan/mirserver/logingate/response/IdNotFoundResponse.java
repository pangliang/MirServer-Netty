package com.zhaoxiaodan.mirserver.logingate.response;

import com.zhaoxiaodan.mirserver.core.network.Protocol;
import com.zhaoxiaodan.mirserver.core.network.Response;

/**
 * Created by liangwei on 16/2/17.
 */
public class IdNotFoundResponse extends Response {
	
	public IdNotFoundResponse() {
		super(Protocol.SM_PASSWD_FAIL);
	}
}
