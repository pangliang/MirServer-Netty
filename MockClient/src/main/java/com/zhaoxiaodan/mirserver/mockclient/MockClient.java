package com.zhaoxiaodan.mirserver.mockclient;

import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.loginserver.ClientPackets;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.debug.ExceptionHandler;
import com.zhaoxiaodan.mirserver.network.debug.MyLoggingHandler;
import com.zhaoxiaodan.mirserver.network.decoder.PacketBit6Decoder;
import com.zhaoxiaodan.mirserver.network.decoder.PacketDecoder;
import com.zhaoxiaodan.mirserver.network.encoder.ClientPacketBit6Encoder;
import com.zhaoxiaodan.mirserver.network.encoder.PacketEncoder;
import com.zhaoxiaodan.mirserver.network.packets.IndexPacket;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
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
											//编码
											new MyLoggingHandler(MyLoggingHandler.Type.Read),
											new DelimiterBasedFrameDecoder(2048, false, Unpooled.wrappedBuffer(new byte[]{'!'})),
											new PacketBit6Decoder(),
											new MyLoggingHandler(MyLoggingHandler.Type.Read),
											new PacketDecoder(com.zhaoxiaodan.mirserver.gameserver.ServerPackets.class.getName()),

											//解码
//											new MyLoggingHandler(MyLoggingHandler.Type.Write),
											new ClientPacketBit6Encoder(),
//											new MyLoggingHandler(MyLoggingHandler.Type.Write),
											new PacketEncoder(),
											new MyLoggingHandler(MyLoggingHandler.Type.Write),
											new MyLoggingHandler(MyLoggingHandler.Type.Read),
											new ChannelHandlerAdapter() {
												@Override
												public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
													Packet packet = (Packet)msg;
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

			byte   cmdIndex = 0;
			Packet packet;
			User   user     = new User();
			user.loginId = "liang1";
			user.password = "liang1";
			user.username = "pangliang";

			// new user
//			packet = new ClientPackets.CM_ADDNEWUSER(cmdIndex,user);
//			ch.writeAndFlush(packet);
//			in.readLine();
//			cmdIndex = cmdIndex == 9?0:++cmdIndex;

			// login
			packet = new ClientPackets.Login(cmdIndex, user);
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			//select server
			packet = new ClientPackets.SelectServer(cmdIndex, "功夫明星一区");
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;


			//****************   select server
			ch.closeFuture();
			ch = b.connect(HOST, 7100).sync().channel();

			String charName = "pangliang";

			// new character
//			Character character = new Character();
//			character.user = user;
//			character.name = charName;
//			character.hair = 1;
//			character.job = Job.Warrior;
//			character.gender = Gender.MALE;
//			packet = new ClientPackets.CM_NEWCHR(cmdIndex,character);
//			ch.writeAndFlush(packet);
//			in.readLine();
//			cmdIndex = cmdIndex == 9?0:++cmdIndex;

			// query character
			packet = new ClientPackets.QueryCharacter(cmdIndex, user.loginId, certification);
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			//select character
			packet = new ClientPackets.SelectCharacter(cmdIndex, user.loginId, charName);
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			// ************ game server
			ch.closeFuture();
			ch = b.connect(HOST, 7200).sync().channel();
			packet = new com.zhaoxiaodan.mirserver.gameserver.ClientPackets.GameLogin(cmdIndex,user.loginId,charName,certification,"2020522");
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			packet = new com.zhaoxiaodan.mirserver.gameserver.ClientPackets.LoginNoticeOk(cmdIndex);
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

			packet = new IndexPacket(Protocol.CM_QUERYBAGITEMS, cmdIndex);
			packet.recog = charId;
			ch.writeAndFlush(packet);
			in.readLine();
			cmdIndex = cmdIndex == 9 ? 0 : ++cmdIndex;

		} finally {
			group.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		new MockClient().run();
	}
}
