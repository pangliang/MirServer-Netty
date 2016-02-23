package com.zhaoxiaodan.mirserver.logingate;

import com.zhaoxiaodan.mirserver.db.entities.User;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
	public static final Map<String,Session> users = new ConcurrentHashMap<>();

	public User user;

	public static Session getSession(ChannelHandlerContext ctx){
		return users.get(ctx.channel().remoteAddress().toString());
	}

	public static void put(ChannelHandlerContext ctx, Session session){
		users.put(ctx.channel().remoteAddress().toString(),session);
	}

	public static Session remove(ChannelHandlerContext ctx){
		return users.remove(ctx.channel().remoteAddress().toString());
	}

	public static int size(){
		return users.size();
	}
}
