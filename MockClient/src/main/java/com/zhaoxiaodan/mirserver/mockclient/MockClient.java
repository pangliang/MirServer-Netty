package com.zhaoxiaodan.mirserver.mockclient;

import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.debug.ExceptionHandler;
import com.zhaoxiaodan.mirserver.network.debug.MyLoggingHandler;
import com.zhaoxiaodan.mirserver.network.decoder.ServerPacketDecoder;
import com.zhaoxiaodan.mirserver.network.encoder.ServerPacketEncoder;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MockClient {

	static final String HOST = "222.187.225.55";
	static final int    PORT = 7000;

	static short certification = 0;
	static int charId;

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
											new ExceptionHandler(),

											new MyLoggingHandler(MyLoggingHandler.Type.Read),
											new DelimiterBasedFrameDecoder(2048, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
											new ServerPacketDecoder(),
											new MyLoggingHandler(MyLoggingHandler.Type.Read),

											new ServerPacketEncoder(),
											new MyLoggingHandler(MyLoggingHandler.Type.Write),
											new MyLoggingHandler(MyLoggingHandler.Type.Read),
											new ChannelHandlerAdapter() {
												@Override
												public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
													ServerPacket packet = (ServerPacket)msg;
													if (packet.protocol == Protocol.SM_SELECTSERVER_OK) {
														certification = (short) packet.recog;
													}else if(packet.protocol == Protocol.SM_LOGON){
														charId = packet.recog;
													}
												}
											},
											new ExceptionHandler()
									);
								}
							}
					);
			Channel ch = b.connect(HOST, PORT).sync().channel();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

			byte   cmdIndex = 1;
			ClientPacket packet;
			User   user     = new User();
			user.loginId = "liang1";
			user.password = "liang1";
			user.username = "pangliang";

			// new user
//			packet = new ClientPacket.CM_ADDNEWUSER(cmdIndex,user);
//			ch.sendPacket(packet);
//			in.readLine();
//			cmdIndex = cmdIndex == 9?0:++cmdIndex;

//			ch.sendPacket("#"+cmdIndex+"PROCESS!");
//			in.readLine();
//			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			// login
			packet = new ClientPacket.Login(cmdIndex, user);
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			//select server
			packet = new ClientPacket.SelectServer(cmdIndex, "功夫明星一区");
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;


			//****************   select server
			ch.closeFuture();
			ch = b.connect(HOST, 7100).sync().channel();

			String charName = "pangliang";

			// new player
//			Player player = new Player();
//			player.user = user;
//			player.name = charName;
//			player.hair = 1;
//			player.job = Job.Warrior;
//			player.gender = Gender.MALE;
//			packet = new ClientPacket.CM_NEWCHR(cmdIndex,player);
//			ch.sendPacket(packet);
//			in.readLine();
//			cmdIndex = cmdIndex == 9?0:++cmdIndex;

			// query player
			packet = new ClientPacket.QueryCharacter(cmdIndex, user.loginId, certification);
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			//select player
			packet = new ClientPacket.SelectCharacter(cmdIndex, user.loginId, charName);
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			// ************ game server
			ch.closeFuture();
			ch = b.connect(HOST, 7200).sync().channel();
			packet = new ClientPacket.GameLogin(cmdIndex,user.loginId,charName,certification,"2020522");
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			packet = new ClientPacket(Protocol.CM_LOGINNOTICEOK, cmdIndex);
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			cmdIndex = 1;
			packet = new ClientPacket(Protocol.CM_WALK, cmdIndex);
			packet.recog = charId;
			packet.p1 = 266;
			packet.p2 = 594;
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			in.readLine();
			in.readLine();

		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new MockClient().run();
	}
}
