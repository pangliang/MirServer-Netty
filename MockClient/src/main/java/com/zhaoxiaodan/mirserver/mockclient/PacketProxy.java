package com.zhaoxiaodan.mirserver.mockclient;

import com.zhaoxiaodan.mirserver.loginserver.LoginClientPackets;
import com.zhaoxiaodan.mirserver.loginserver.LoginServerPackets;
import com.zhaoxiaodan.mirserver.network.debug.ExceptionHandler;
import com.zhaoxiaodan.mirserver.network.debug.MyLoggingHandler;
import com.zhaoxiaodan.mirserver.network.decoder.ClientPacketBit6Decoder;
import com.zhaoxiaodan.mirserver.network.decoder.ClientPacketDecoder;
import com.zhaoxiaodan.mirserver.network.decoder.ServerPacketBit6Decoder;
import com.zhaoxiaodan.mirserver.network.decoder.ServerPacketDecoder;
import com.zhaoxiaodan.mirserver.network.encoder.ClientPacketBit6Encoder;
import com.zhaoxiaodan.mirserver.network.encoder.ServerPacketBit6Encoder;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.logging.log4j.LogManager;

import java.nio.ByteOrder;

/**
 * 代理, 通过这个东西中转 服务器和客户端之间的封包;中间都转成 Packet 对象进行传输, 目的是 查看 交互的封包 格式
 * 服务器发给客户端:
 * 服务器原封包 -> 服务器连接端 -> Decoder -> ServerPacket -> Handler -> 客户端连接端 -> Encoder -> 还原的封包 -> 客户端
 * <p>
 * 客户端发给服务器
 * 客户端封包 -> 客户端连接端 -> Decoder -> ClientPacket -> Handler -> 服务器连接端 -> Encoder -> 还原的封包 ->服务器
 */
public class PacketProxy {

	static final String PROXY_HOST = "192.168.0.166";
	static final int    PROXY_PORT = 7000;

	static final String HOST = "61.147.247.106";
	static final int    PORT = 7000;

	static ChannelHandlerContext clientChannel = null;
	static Channel               serverChannel = null;

	public void run() throws Exception {
		try {

			final Bootstrap server = new Bootstrap();
			server.group(new NioEventLoopGroup())
					.channel(NioSocketChannel.class)
					.handler(
							new ChannelInitializer<SocketChannel>() {
								@Override
								public void initChannel(SocketChannel ch) throws Exception {
									ch.pipeline().addLast(
											new ExceptionHandler(),

//											new MyLoggingHandler(MyLoggingHandler.Type.Read),
											//这里是服务器的连接端
											//来自服务器的封包, 解码成Packet
											new DelimiterBasedFrameDecoder(4096, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
											new MyLoggingHandler(MyLoggingHandler.Type.Read),
											new ServerPacketBit6Decoder(),
											new MyLoggingHandler(MyLoggingHandler.Type.Read),
											new ServerPacketDecoder(),

											//来自客户端的封包, 发给服务器
//											new MyLoggingHandler(MyLoggingHandler.Type.Write),
											new ClientPacketBit6Encoder(),
//											new MyLoggingHandler(MyLoggingHandler.Type.Write),
											new ChannelHandlerAdapter() {
												@Override
												public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
													ServerPacket  packet = (ServerPacket) msg;
													ByteBuf out    = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

													// 把角色服游戏服ip地址封包改写成当前代理地址
													if (packet instanceof LoginServerPackets.SelectServerOk) {
														LoginServerPackets.SelectServerOk selectServerOk = (LoginServerPackets.SelectServerOk) packet;
														serverChannel.close().sync();
														serverChannel = server.connect(HOST, selectServerOk.selectserverPort).sync().channel();
														packet = new LoginServerPackets.SelectServerOk(PROXY_HOST, PROXY_PORT, selectServerOk.cert);
													} else if (packet instanceof LoginServerPackets.StartPlay) {
														LoginServerPackets.StartPlay p1 = (LoginServerPackets.StartPlay) packet;
														serverChannel.close().sync();
														serverChannel = server.connect(HOST, p1.serverPort).sync().channel();
														packet = new LoginServerPackets.StartPlay(PROXY_HOST, PROXY_PORT);
													}

													LogManager.getLogger().debug("server packet => {}", packet.toString());
													packet.writePacket(out);
													if (packet.getClass().getSimpleName().equals(ServerPacket.class.getSimpleName())) {
														// 未知的 包结构, body没读取
														out.writeBytes(packet.remainBytes());
													}

													// 来自服务器的封包, 通过 client 连接端 发给客户端
													clientChannel.writeAndFlush(out);
												}
											},
											new ExceptionHandler()
									);
								}
							}
					);
			serverChannel = server.connect(HOST, PORT).sync().channel();


			ServerBootstrap b = new ServerBootstrap();
			b.group(new NioEventLoopGroup(), new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(
									//这里是客户端的连接端

									//客户端发来的封包, 解码成 Packet
									new DelimiterBasedFrameDecoder(4096, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
									new ClientPacketBit6Decoder(),
//									new MyLoggingHandler(MyLoggingHandler.Type.Read),
									new ClientPacketDecoder(LoginClientPackets.class.getCanonicalName()),

									new ExceptionHandler(),

									//发给客户端的封包, 从Packet还原成加密封包
									new ServerPacketBit6Encoder(),
//									new MyLoggingHandler(MyLoggingHandler.Type.Write),
									new ChannelHandlerAdapter() {
										@Override
										public void channelActive(ChannelHandlerContext ctx) throws Exception {
											super.channelActive(ctx);
											clientChannel = ctx;
										}

										@Override
										public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

											ClientPacket packet = (ClientPacket) msg;
											ByteBuf             out    = Unpooled.buffer().order(ByteOrder.LITTLE_ENDIAN);

											LogManager.getLogger().debug("client packet => {}", packet.toString());
											packet.writePacket(out);
											if (packet.getClass().equals(ClientPacket.class)) {
												// 未知的 包结构, body没读取
												out.writeBytes(packet.remainBytes());
											}

											serverChannel.writeAndFlush(out);
										}
									}
							);
						}
					})
					.option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(7000).sync();

			f.channel().closeFuture().sync();

		} finally {
		}
	}

	public static void main(String[] args) throws Exception {
		new PacketProxy().run();
	}
}
