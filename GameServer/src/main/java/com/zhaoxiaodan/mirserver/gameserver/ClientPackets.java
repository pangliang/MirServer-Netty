package com.zhaoxiaodan.mirserver.gameserver;

import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.IndexPacket;
import com.zhaoxiaodan.mirserver.network.packets.Parcelable;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

//TODO 按道理这里是网络封包模块不应该关联 DB 的 DAO, 当时包含的属性其实基本一致; 偷懒就这么用了
public class ClientPackets {

	public static final class GameLogin extends IndexPacket {

		public String loginId;
		public String characterName;
		public short    cert;
		public String clientVersion;

		public GameLogin() {
		}

		public GameLogin(byte cmdIndex, String loginId, String characterName, short cert, String clientVersion) {
			super(Protocol.CM_QUERYUSERSTATE, cmdIndex);
			this.loginId = loginId;
			this.characterName = characterName;
			this.cert = cert;
			this.clientVersion = clientVersion;
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			in.readByte();  //cmdIndex
			in.readByte();  //*
			in.readByte();  //*

			String content = in.toString(Charset.defaultCharset()).trim();
			String[] parts = content.split(CONTENT_SEPARATOR_STR);
			if(parts.length >= 5)
			{
				loginId = parts[0];
				characterName = parts[1].trim();
				cert = Short.parseShort(parts[2].trim());
				clientVersion=parts[3];
			}else{
				throw new Parcelable.WrongFormatException();
			}
		}

		@Override
		public void writePacket(ByteBuf out) {
			out.writeByte(cmdIndex+'0');
			out.writeByte('*');
			out.writeByte('*');

			out.writeBytes(loginId.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(characterName.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(Short.toString(cert).getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(clientVersion.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeByte('1');
		}
	}

	public static final class LoginNoticeOk extends IndexPacket {

		public short clientType;
		public LoginNoticeOk() {
		}

		public LoginNoticeOk(byte cmdIndex) {
			super(Protocol.CM_LOGINNOTICEOK, cmdIndex);
			this.p3 = 0;
		}
	}


}
