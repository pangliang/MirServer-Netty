package com.zhaoxiaodan.mirserver.logingate;

import com.zhaoxiaodan.mirserver.db.DB;
import com.zhaoxiaodan.mirserver.logingate.handler.LoginHandler;
import com.zhaoxiaodan.mirserver.network.ClientPackets;
import com.zhaoxiaodan.mirserver.network.PacketDispatcher;
import com.zhaoxiaodan.mirserver.network.debug.ExceptionHandler;
import com.zhaoxiaodan.mirserver.network.debug.MyLoggingHandler;
import com.zhaoxiaodan.mirserver.network.decoder.ClientPacketBit6Decoder;
import com.zhaoxiaodan.mirserver.network.decoder.ClientPacketDecoder;
import com.zhaoxiaodan.mirserver.network.encoder.PacketBit6Encoder;
import com.zhaoxiaodan.mirserver.network.encoder.PacketEncoder;
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

public class LoginGate {

	private int port;

	public LoginGate(int port) {
		this.port = port;
	}

	public void run() throws Exception {

		// db init
		DB.init();

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

									new ExceptionHandler(),

									//编码
									new MyLoggingHandler(MyLoggingHandler.Type.Read),
									new DelimiterBasedFrameDecoder(Config.REQUEST_MAX_FRAME_LENGTH, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
									new ClientPacketBit6Decoder(),
									new MyLoggingHandler(MyLoggingHandler.Type.Read),
									new ClientPacketDecoder(ClientPackets.class.getCanonicalName()),
									new MyLoggingHandler(MyLoggingHandler.Type.Read),

									//解码
//									new MyLoggingHandler(MyLoggingHandler.Type.Write),
									new PacketBit6Encoder(),
//									new MyLoggingHandler(MyLoggingHandler.Type.Write),
									new PacketEncoder(),
									new MyLoggingHandler(MyLoggingHandler.Type.Write),

									//分包分发
									new PacketDispatcher(LoginHandler.class.getPackage().getName()),

									//处理连接
									new ConnectHandler(),

									new ExceptionHandler()
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
