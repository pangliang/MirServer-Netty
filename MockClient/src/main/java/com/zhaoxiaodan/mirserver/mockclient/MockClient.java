package com.zhaoxiaodan.mirserver.mockclient;

import com.zhaoxiaodan.mirserver.network.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Packet;
import com.zhaoxiaodan.mirserver.network.ServerPackets;
import com.zhaoxiaodan.mirserver.network.debug.ReadWriteLoggingHandler;
import com.zhaoxiaodan.mirserver.network.decoder.Bit6BufDecoder;
import com.zhaoxiaodan.mirserver.network.decoder.PacketDecoder;
import com.zhaoxiaodan.mirserver.network.encoder.Bit6BufEncoder;
import com.zhaoxiaodan.mirserver.network.encoder.PacketEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by liangwei on 16/2/19.
 */
public class MockClient {

	static final String HOST = "121.42.150.110";
	static final int    PORT = 7000;

	Packet[] tests = {
			new ClientPackets.Login((byte) 1, "liang1", "liang1"),
			new ClientPackets.SelectServer((byte) 2, "横行霸道二区")
	};

	public void run() throws Exception {

		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
					.channel(NioSocketChannel.class)
					.handler(
							new ChannelInitializer<SocketChannel>() {
								@Override
								public void initChannel(SocketChannel ch) throws Exception {
									ch.pipeline().addLast(
											//编码
											new ReadWriteLoggingHandler(ReadWriteLoggingHandler.Type.Read),
											new DelimiterBasedFrameDecoder(1024, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
											new Bit6BufDecoder(false),
											new ReadWriteLoggingHandler(ReadWriteLoggingHandler.Type.Read),
											new PacketDecoder(ServerPackets.class.getCanonicalName(), false),

											//解码
											new ReadWriteLoggingHandler(ReadWriteLoggingHandler.Type.Write),
											new Bit6BufEncoder(true),
											new ReadWriteLoggingHandler(ReadWriteLoggingHandler.Type.Write),
											new PacketEncoder(),

											new ReadWriteLoggingHandler(ReadWriteLoggingHandler.Type.Read)
									);
								}
							}
					);

			// Start the connection attempt.
			Channel ch = b.connect(HOST, PORT).sync().channel();

			// run test

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			for (int i = 0; i < tests.length; i++) {

				Packet packet = tests[i];
				ch.writeAndFlush(packet);

				String line = in.readLine();

				if ("bye".equals(line.toLowerCase())) {
					ch.closeFuture().sync();
					break;
				} else if (tests.length - 1 == i) {
					i = 0;
				}
			}

//			ch.closeFuture().sync();

		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new MockClient().run();
	}
}
