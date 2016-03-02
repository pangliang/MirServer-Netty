package com.zhaoxiaodan.mirserver.network.packets;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.db.entities.User;
import com.zhaoxiaodan.mirserver.db.objects.Gender;
import com.zhaoxiaodan.mirserver.db.objects.Job;
import com.zhaoxiaodan.mirserver.network.Protocol;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class ClientPacket extends Packet{

	public byte cmdIndex;  // #号后面紧跟的序号, 响应包的序号要跟请求一直

	public ClientPacket(){}

	public ClientPacket(Protocol pid, byte cmdIndex) {
		super(0, pid, (short) 0, (short) 0, (short) 0);
		this.cmdIndex = cmdIndex;
	}

	@Override
	public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
		cmdIndex = in.readByte();
		cmdIndex = (byte) (cmdIndex - '0');
		super.readPacket(in);
	}

	@Override
	public void writePacket(ByteBuf out) {
		out.writeByte(cmdIndex + '0');
		super.writePacket(out);
	}

	public static final class Process extends ClientPacket {

		public Process(byte cmdIndex) {
			super(Protocol.CM_PROTOCOL, cmdIndex);
		}
	}

	public static final class Login extends ClientPacket {

		public User user;

		public Login() {
		}

		public Login(byte cmdIndex, User user) {
			super(Protocol.CM_IDPASSWORD, cmdIndex);
			this.user = user;
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);

			user = new User();
			String   conten = in.toString(Charset.defaultCharset()).trim();
			String[] parts  = conten.split(CONTENT_SEPARATOR_STR);
			if (parts.length > 1) {
				user.loginId = parts[0];
				user.password = parts[1];
			} else {
				throw new Parcelable.WrongFormatException();
			}
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			out.writeBytes(user.loginId.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(user.password.getBytes());
		}
	}

	public static final class NewUser extends ClientPacket {

		public User user;

		public NewUser() {
		}

		public NewUser(byte cmdIndex, User user) {
			super(Protocol.CM_ADDNEWUSER, cmdIndex);
			this.user = user;
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);

			user = new User();
			user.loginId = readString(in);
			user.password = readString(in);
			user.username = readString(in);
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(user.loginId.getBytes());
			out.writeBytes(new byte[]{0, 0, 0, 0});
			out.writeBytes(user.password.getBytes());
			out.writeBytes(new byte[]{0, 0, 0, 0});
			out.writeBytes(user.username.getBytes());
			out.writeBytes(new byte[]{0, 0, 0, 0});

		}
	}

	public static final class SelectServer extends ClientPacket {

		public String serverName;

		public SelectServer() {}

		public SelectServer(byte cmdIndex, String serverName) {
			super(Protocol.CM_SELECTSERVER, cmdIndex);
			this.serverName = serverName;
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			serverName = in.toString(Charset.defaultCharset()).trim();
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(serverName.getBytes());
		}
	}

	public static final class NewCharacter extends ClientPacket {

		public Character character;

		public NewCharacter() {}

		public NewCharacter(byte cmdIndex, Character character) {
			super(Protocol.CM_NEWCHR, cmdIndex);
			this.character = character;
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			String   content = in.toString(Charset.defaultCharset()).trim();
			String[] parts   = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length < 5)
				throw new Parcelable.WrongFormatException();
			User user = new User();
			user.loginId = parts[0];
			character = new Character();
			character.user = user;
			character.name = parts[1];
			character.hair = Byte.parseByte(parts[2]);
			character.job = Job.values()[Byte.parseByte(parts[3])];
			character.gender = Gender.values()[Byte.parseByte(parts[4])];
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(character.user.loginId.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(character.name.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeByte(character.hair + '0');
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeByte(character.job.ordinal() + '0');
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeByte(character.gender.ordinal() + '0');
			out.writeBytes(new byte[10]);
		}
	}

	public static final class QueryCharacter extends ClientPacket {

		public String loginId;
		public short  cert;

		public QueryCharacter() {}

		public QueryCharacter(byte cmdIndex, String loginId, short cert) {
			super(Protocol.CM_QUERYCHR, cmdIndex);
			this.loginId = loginId;
			this.cert = cert;
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			String   content = in.toString(Charset.defaultCharset()).trim();
			String[] parts   = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length >= 2) {
				loginId = parts[0];
				cert = Short.parseShort(parts[1]);
			} else {
				throw new Parcelable.WrongFormatException();
			}
		}


		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(loginId.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(Short.toString(cert).getBytes());
		}
	}

	public static final class DeleteCharacter extends ClientPacket {

		public String characterName;

		public DeleteCharacter() {}

		public DeleteCharacter(byte cmdIndex, String characterName) {
			super(Protocol.CM_QUERYCHR, cmdIndex);
			this.characterName = characterName;
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			this.characterName = in.toString(Charset.defaultCharset()).trim();
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(this.characterName.getBytes());
		}
	}

	public static final class SelectCharacter extends ClientPacket {

		public String loginId;
		public String characterName;

		public SelectCharacter() {}

		public SelectCharacter(byte cmdIndex, String loginId, String characterName) {
			super(Protocol.CM_SELCHR, cmdIndex);
			this.loginId = loginId;
			this.characterName = characterName;
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			String   content = in.toString(Charset.defaultCharset()).trim();
			String[] parts   = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length >= 2) {
				loginId = parts[0];
				characterName = parts[1];
			} else {
				throw new Parcelable.WrongFormatException();
			}
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(this.loginId.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(this.characterName.getBytes());
		}
	}

	/********************************************************************
	 * GameServer
	 ********************************************************************/

	public static final class GameLogin extends ClientPacket {

		public String loginId;
		public String characterName;
		public short  cert;
		public String clientVersion;
		public String gateIndex;

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
			this.cmdIndex = (byte) (in.readByte() - '0');  //cmdIndex
			in.readByte();  //*
			in.readByte();  //*

			String   content = in.toString(Charset.defaultCharset()).trim();
			String[] parts   = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length >= 5) {
				loginId = parts[0];
				characterName = parts[1].trim();
				cert = Short.parseShort(parts[2].trim());
				clientVersion = parts[3];
				gateIndex = parts[4];
			} else {
				throw new Parcelable.WrongFormatException();
			}
		}

		@Override
		public void writePacket(ByteBuf out) {
			out.writeByte(cmdIndex + '0');
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
			out.writeBytes(gateIndex.getBytes());
		}
	}
}
