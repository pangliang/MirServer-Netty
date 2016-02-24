package com.zhaoxiaodan.mirserver.loginserver;

import com.zhaoxiaodan.mirserver.log.Log;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

//TODO 这里不是很清楚到底 使用哪个事件来处理 '连接' 和 '断开', 在哪里加入session, 哪里移出session比较合适 ?
public class ConnectHandler extends ChannelHandlerAdapter {


	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		Session session = Session.remove(ctx);
		if (session != null)
			Log.info("user {} unreg, login user count : {}", session.user.loginId, Session.size());
	}
}
