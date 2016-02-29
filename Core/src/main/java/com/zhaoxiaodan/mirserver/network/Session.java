package com.zhaoxiaodan.mirserver.network;

import com.zhaoxiaodan.mirserver.db.DB;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

	private static Logger logger = LogManager.getLogger(Session.class);
	private static final Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

	private final Map<String, Object> values = new ConcurrentHashMap<String, Object>();
	public final ChannelHandlerContext context;
	public final DB db;

	private Session(ChannelHandlerContext ctx){
		this.context = ctx;
		this.db = new DB();
	}

	public void remove() {
		logger.debug("remove session for {}", context.toString());
		sessions.remove(context.channel().remoteAddress().toString());
	}

	public void writeAndFlush(Object msg){
		context.writeAndFlush(msg);
	}

	public void put(String key, Object value) {
		values.put(key, value);
	}

	public Object get(String key) {
		return values.get(key);
	}

	public static Session getSession(ChannelHandlerContext ctx) {
		return sessions.get(ctx.channel().remoteAddress().toString());
	}

	public static Session create(ChannelHandlerContext ctx) {
		logger.debug("create session for {}", ctx.toString());
		Session session = new Session(ctx);
		sessions.put(ctx.channel().remoteAddress().toString(), session);
		return session;
	}

	public static int size() {
		return sessions.size();
	}

	public String toString(){
		return context.toString();
	}
}
