package com.zhaoxiaodan.mirserver.network;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Handler {
	protected Logger logger = LogManager.getLogger(this.getClass().getName());
	protected Session session;

	protected void exce(ChannelHandlerContext ctx, ClientPacket packet) throws Exception {
		session = Session.getSession(ctx);
		if(null == session){
			logger.debug("new session for {}",ctx);
			session = Session.create(ctx);
		}
		DB.getSession().getTransaction().begin();
		try{
			onPacket(packet);
			DB.getSession().getTransaction().commit();
		}catch(Exception e){
			if(DB.getSession().isOpen())
				DB.getSession().getTransaction().rollback();
		}

	}

	protected void onDisconnect(ChannelHandlerContext ctx) throws Exception{
		session = Session.getSession(ctx);
		if(null == session){
			logger.error("session already remove for {}",ctx);
			return;
		}

		session.remove();
		DB.getSession().getTransaction().begin();
		try{
			onDisconnect();
			DB.getSession().getTransaction().commit();
		}catch(Exception e){
			if(DB.getSession().isOpen())
				DB.getSession().getTransaction().rollback();
		}
	}

	public void onPacket(ClientPacket packet) throws Exception{
		logger.error("overwrite it !!");
	}
	public void onDisconnect() throws Exception{
		logger.error("overwrite it !!");
	}
}
