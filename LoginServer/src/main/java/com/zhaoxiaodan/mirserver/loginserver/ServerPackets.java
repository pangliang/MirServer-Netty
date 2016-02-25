package com.zhaoxiaodan.mirserver.loginserver;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.db.entities.Gender;
import com.zhaoxiaodan.mirserver.db.entities.Job;
import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ServerPackets {

	public static final class LoginSuccSelectServer extends Packet {

		public List<ServerInfo> serverInfoList;

		public LoginSuccSelectServer() {}

		public LoginSuccSelectServer(List<ServerInfo> serverInfoList) {
			super(Protocol.SM_PASSOK_SELECTSERVER);
			this.serverInfoList = serverInfoList;
			this.p3 = (short) serverInfoList.size();
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			for (ServerInfo info : serverInfoList) {
				String infoStr = info.name + CONTENT_SEPARATOR_STR + info.id + CONTENT_SEPARATOR_STR;
				try {
					out.writeBytes(infoStr.getBytes());
				} catch (Exception e) {

				}
			}
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);

			serverInfoList = new ArrayList<>();
			String content = in.toString(Charset.defaultCharset());

			String[] parts = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length < 2)
				throw new WrongFormatException();
			for (int i = 0; i + 1 < parts.length; i += 2) {
				ServerInfo info = new ServerInfo();
				info.name = parts[i];
				info.id = Long.parseLong(parts[i + 1]);
				serverInfoList.add(info);
			}
		}
	}

	public static final class LoginFail extends Packet {

		public class Reason {
			public static final int UserNotFound   = 0;     //帐号不存在
			public static final int WrongPwd       = -1;     //密码错
			public static final int WrongPwd3Times = -2;    //密码错误3次
			public static final int AlreadyLogin   = -3;   //已经登录, 需要踢掉原来的
		}

		public LoginFail(int reason) {
			super(Protocol.SM_PASSWD_FAIL);
			p0 = reason;
		}
	}

	public static final class SelectServerOk extends Packet {

		public String selectServerIp;
		public int    selectserverPort;
		public short  certification;

		public SelectServerOk() {}

		public SelectServerOk(String selectServerIp, int selectserverPort, byte certification) {
			super(Protocol.SM_SELECTSERVER_OK);
			this.selectServerIp = selectServerIp;
			this.selectserverPort = selectserverPort;
			this.certification = certification;
			p0 = certification;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			out.writeBytes(selectServerIp.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(Integer.toString(selectserverPort).getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(Short.toString(certification).getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);

			String content = in.toString(Charset.defaultCharset()).trim();

			String[] parts = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length < 3)
				throw new WrongFormatException();
			this.selectServerIp = parts[0];
			this.selectserverPort = Integer.parseInt(parts[1]);
			this.certification = Short.parseShort(parts[2]);
		}
	}

	public static final class QueryCharactorOk extends Packet {

		public List<Character> characterList;

		public QueryCharactorOk() {}

		public QueryCharactorOk(List<Character> characterList) {
			super(Protocol.SM_QUERYCHR);
			this.characterList = characterList;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			for (Character cha : this.characterList) {
				out.writeBytes(cha.name.getBytes());
				out.writeByte(CONTENT_SEPARATOR_CHAR);
				out.writeBytes(Integer.toString(cha.job.ordinal()).getBytes());
				out.writeByte(CONTENT_SEPARATOR_CHAR);
				out.writeBytes(Integer.toString(cha.hair).getBytes());
				out.writeByte(CONTENT_SEPARATOR_CHAR);
				out.writeBytes(Integer.toString(cha.level).getBytes());
				out.writeByte(CONTENT_SEPARATOR_CHAR);
				out.writeBytes(Integer.toString(cha.gender.ordinal()).getBytes());
				out.writeByte(CONTENT_SEPARATOR_CHAR);
			}
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);

			String content = in.toString(Charset.defaultCharset()).trim();

			String[] parts = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length < 5)
				throw new WrongFormatException();
			this.characterList = new ArrayList<>();
			for (int i = 0; i + 4 < parts.length; i += 5) {
				Character cha = new Character();
				cha.name = parts[i + 0];
				cha.job = Job.values()[Byte.parseByte(parts[i + 1])];
				cha.hair = Byte.parseByte(parts[i + 2]);
				cha.level = Integer.parseInt(parts[i + 3]);
				cha.gender = Gender.values()[Byte.parseByte(parts[i + 4])];

				characterList.add(cha);
			}
		}
	}

	public static final class SelectCharacterOk extends Packet {

		public String serverIp;
		public int    serverPort;

		public SelectCharacterOk() {}

		public SelectCharacterOk(String serverIp, int serverPort) {
			super(Protocol.SM_STARTPLAY);
			this.serverIp = serverIp;
			this.serverPort = serverPort;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			out.writeBytes(serverIp.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(Integer.toString(serverPort).getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);

			String content = in.toString(Charset.defaultCharset()).trim();

			String[] parts = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length < 2)
				throw new WrongFormatException();
			this.serverIp = parts[0];
			this.serverPort = Integer.parseInt(parts[1]);
		}
	}
}
