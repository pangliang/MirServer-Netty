package com.zhaoxiaodan.mirserver.gameserver;

import com.zhaoxiaodan.mirserver.gameserver.types.Direction;
import com.zhaoxiaodan.mirserver.gameserver.types.WearPosition;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.ClientPacket;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class GameClientPackets{

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
		public void readPacket(ByteBuf in) throws WrongFormatException {
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
				throw new WrongFormatException();
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

	public static final class Say extends ClientPacket {

		public String msg;

		public Say() {}

		public Say(byte cmdIndex, String msg) {
			super(Protocol.CM_SAY, cmdIndex);
			this.msg = msg;
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.msg = in.toString(Charset.defaultCharset()).trim();
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(this.msg.getBytes());
		}
	}

	public static final class Merchant extends ClientPacket {

		public int    npcInGameId;
		public String msg;

		public Merchant() {}

		public Merchant(byte cmdIndex, Protocol protocol, int npcInGameId, String msg) {
			super(protocol, cmdIndex);
			this.npcInGameId = npcInGameId;
			this.recog = npcInGameId;
			this.msg = msg;
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.npcInGameId = this.recog;
			this.msg = in.toString(Charset.defaultCharset()).trim();
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			out.writeBytes(msg.getBytes());
		}
	}

	public static final class TakeOnOffItem extends ClientPacket {

		public int          playerItemId;
		public String       itemName;
		public WearPosition wearPosition;

		public TakeOnOffItem() {}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.playerItemId = this.recog;
			this.wearPosition = WearPosition.get(this.p1);
			this.itemName = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class Action extends ClientPacket {

		public short     x;
		public short     y;
		public Direction direction;

		public Action() {}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			direction = Direction.values()[p2];
			x = NumUtil.getLowWord(recog);
			y = NumUtil.getHighWord(recog);
		}
	}

	public static final class Spell extends ClientPacket {

		public int       magicId;
		public Direction direction;

		public Spell() {}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			direction = Direction.values()[recog];
			magicId = p2;
		}
	}

	public static final class MagicKeyChange extends ClientPacket {

		public int   magicId;
		public short key;

		public MagicKeyChange() {}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			magicId = recog;
			key = p1;
		}
	}

	public static final class Eat extends ClientPacket {

		public int    playerItemInGameId;
		public String itemName;

		public Eat() {}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			playerItemInGameId = recog;
			this.itemName = in.toString(Charset.defaultCharset()).trim();
		}
	}
}
