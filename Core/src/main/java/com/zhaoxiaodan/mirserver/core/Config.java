package com.zhaoxiaodan.mirserver.core;

import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * Created by liangwei on 16/2/17.
 */
public class Config {

	public static final Charset DEFAULT_CHARSET          = CharsetUtil.UTF_8;
	public static final int     REQUEST_MAX_FRAME_LENGTH = 1024;                    // 封包每一帧的最大大小
	public static final int     DEFAULT_LOGIN_GATE_PORT  = 7000;                    // 登录网关默认端口号

}
