package com.zhaoxiaodan.mirserver.logingate;

import com.zhaoxiaodan.mirserver.network.debug.ReadWriteLoggingHandler;
import com.zhaoxiaodan.mirserver.network.ClientPackets;
import com.zhaoxiaodan.mirserver.network.PacketDispatcher;
import com.zhaoxiaodan.mirserver.network.decoder.Bit6BufDecoder;
import com.zhaoxiaodan.mirserver.network.decoder.PacketDecoder;
import com.zhaoxiaodan.mirserver.network.encoder.Bit6BufEncoder;
import com.zhaoxiaodan.mirserver.network.encoder.PacketEncoder;
import com.zhaoxiaodan.mirserver.logingate.handler.LoginHandler;
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
									//编码
									new ReadWriteLoggingHandler(ReadWriteLoggingHandler.Type.Read),
									new DelimiterBasedFrameDecoder(Config.REQUEST_MAX_FRAME_LENGTH, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
									new Bit6BufDecoder(true),
									new ReadWriteLoggingHandler(ReadWriteLoggingHandler.Type.Read),
									new PacketDecoder(ClientPackets.class.getCanonicalName(), true),

									//解码
									new ReadWriteLoggingHandler(ReadWriteLoggingHandler.Type.Write),
									new Bit6BufEncoder(false),
									new ReadWriteLoggingHandler(ReadWriteLoggingHandler.Type.Write),
									new PacketEncoder(),

									//分包分发
									new PacketDispatcher(LoginHandler.class.getPackage().getName())
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
