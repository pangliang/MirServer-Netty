package com.zhaoxiaodan.mirserver.logingate;

import com.zhaoxiaodan.mirserver.core.Config;
import com.zhaoxiaodan.mirserver.core.debug.ReadWriteLoggingHandler;
import com.zhaoxiaodan.mirserver.core.network.RequestDispatcher;
import com.zhaoxiaodan.mirserver.core.network.decoder.Bit6BufDecoder;
import com.zhaoxiaodan.mirserver.core.network.decoder.RequestDecoder;
import com.zhaoxiaodan.mirserver.core.network.encoder.Bit6BufEncoder;
import com.zhaoxiaodan.mirserver.core.network.encoder.ResponseEncoder;
import com.zhaoxiaodan.mirserver.logingate.decoder.ProcessRequestDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

/**
 * Created by liangwei on 16/2/16.
 */
public class LoginGate {

	private int port;

	public LoginGate(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup   = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(
									new ReadWriteLoggingHandler(LogLevel.INFO),
									new DelimiterBasedFrameDecoder(Config.REQUEST_MAX_FRAME_LENGTH, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
									new ProcessRequestDecoder(CharsetUtil.UTF_8),
									new Bit6BufDecoder(true),       //服务器, 解码request, 编码response
									new ReadWriteLoggingHandler(LogLevel.INFO),
									new RequestDecoder(LoginGateProtocols.Rquest_Type_Map),

									new ReadWriteLoggingHandler(LogLevel.INFO),
									new Bit6BufEncoder(false),           //服务器, 解码request, 编码response
									new ReadWriteLoggingHandler(LogLevel.INFO),
									new ResponseEncoder(),

									new RequestDispatcher(LoginGateProtocols.Rquest_Handler_Map)
							);
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(port).sync();

			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = Config.DEFAULT_LOGIN_GATE_PORT;
		}
		new LoginGate(port).run();
	}
}
