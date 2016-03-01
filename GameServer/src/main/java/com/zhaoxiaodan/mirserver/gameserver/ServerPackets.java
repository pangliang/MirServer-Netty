package com.zhaoxiaodan.mirserver.gameserver;

import com.zhaoxiaodan.mirserver.db.entities.CharacterItem;
import com.zhaoxiaodan.mirserver.db.objects.Ability;
import com.zhaoxiaodan.mirserver.db.objects.Job;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class ServerPackets {

	public static final class SendNotice extends Packet {

		public String notice;

		public SendNotice() {}

		public SendNotice(String notice) {
			super(Protocol.SM_SENDNOTICE);
			this.notice = notice;
			this.recog = notice.length();
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(notice.getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.notice = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class NewMap extends Packet {

		public String mapId;

		public NewMap() {}

		public NewMap(int id, short x, short y, short objectCount, String mapId) {
			super(Protocol.SM_NEWMAP);
			this.mapId = mapId;
			this.recog = id;
			this.p1 = x;
			this.p2 = y;
			this.p3 = objectCount;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(mapId.getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.mapId = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class VersionFail extends Packet {

		public int file1CRC;
		public int file2CRC;
		public int file3CRC;

		public VersionFail() {}

		public VersionFail(int file1CRC, int file2CRC, int file3CRC) {
			super(Protocol.SM_VERSION_FAIL);
			this.recog = file1CRC;
			this.p1 = getLowWord(file2CRC);
			this.p2 = getHighWord(file2CRC);

			this.file1CRC = file1CRC;
			this.file2CRC = file2CRC;
			this.file3CRC = file3CRC;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeInt(file3CRC);
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.file3CRC = in.readInt();
			this.file1CRC = this.recog;
			this.file2CRC = makeLong(this.p1, this.p2);
		}
	}

	public static final class Logon extends Packet {

		public int   charId;
		public short currX;
		public short currY;
		public byte  direction;
		public byte  light;
		public int   feature;
		public int   charStatus;
		public short featureEx;

		public Logon() {}

		public Logon(int charId, short currX, short currY, byte direction, byte light, int feature, int charStatus, short featureEx) {
			super(Protocol.SM_LOGON);
			this.feature = feature;
			this.charStatus = charStatus;
			this.featureEx = featureEx;

			this.charId = charId;
			this.recog = charId;
			this.p1 = currX;
			this.p2 = currY;
			this.p3 = makeWord(direction, light);
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeInt(feature);
			out.writeInt(charStatus);
			out.writeInt(0);
			out.writeInt(0);
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);

			this.feature = in.readInt();
			this.charStatus = in.readInt();
			this.featureEx = in.readShort();

			this.charId = this.recog;
			this.currX = p1;
			this.currY = p2;
			this.direction = getLowByte(p3);
			this.light = getHighByte(p3);
		}
	}

	public static final class FeatureChanged extends Packet {

		public int charId;
		public int feature;
		public int featureEx;

		public FeatureChanged() {}

		public FeatureChanged(int charId, int feature, short featureEx) {
			super(Protocol.SM_FEATURECHANGED);
			this.charId = charId;
			this.recog = charId;
			this.p1 = 0;//getLowWord(feature);
			this.p2 = 4;//getHighWord(feature);
			this.p3 = 0;//featureEx;

			this.feature = feature;
			this.featureEx = featureEx;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.charId = recog;
			this.feature = makeLong(p1, p2);
			this.featureEx = p3;
		}
	}

	public static final class UserName extends Packet {

		public String userName;

		public UserName() {}

		public UserName(int id, short color, String userName) {
			super(Protocol.SM_USERNAME);
			this.userName = userName;
			this.recog = id;
			this.p1 = color;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(userName.getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.userName = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class MapDescription extends Packet {

		public String description;

		public MapDescription() {}

		public MapDescription(int musicId, String description) {
			super(Protocol.SM_MAPDESCRIPTION);
			this.description = description;
			this.recog = musicId;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(description.getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.description = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class GameGoldName extends Packet {

		public int    gameGold;
		public int    gamePoint;
		public String gameGoldName;
		public String gamePointName;

		public GameGoldName() {}

		public GameGoldName(int gameGold, int gamePoint, String gameGoldName, String gamePointName) {
			super(Protocol.SM_GAMEGOLDNAME);
			this.gameGold = gameGold;
			this.gamePoint = gamePoint;
			this.gameGoldName = gameGoldName;
			this.gamePointName = gamePointName;

			this.recog = gameGold;
			this.p1 = getLowWord(gamePoint);
			this.p2 = getHighWord(gamePoint);
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(gameGoldName.getBytes());
			out.writeByte(13);
			out.writeBytes(gamePointName.getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			String   content = in.toString(Charset.defaultCharset()).trim();
			String[] parts   = content.split("" + (char) (13));
			if (parts.length > 1) {
				this.gameGoldName = parts[0];
				this.gamePointName = parts[1];
			}
		}
	}

	public static final class AddItem extends Packet {

		public CharacterItem item;

		public AddItem() {}

		public AddItem(int id, CharacterItem item) {
			super(Protocol.SM_ADDITEM);
			this.recog = id;
			this.p3 = 1;
			this.item = item;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			byte[] nameBytes = item.stdItem.name.getBytes();
			out.writeByte(nameBytes.length);
			out.writeBytes(nameBytes);

			out.writeByte(item.stdItem.stdMode);
			out.writeByte(item.stdItem.shape);
			out.writeByte(item.stdItem.weight);
			out.writeByte(item.stdItem.anicount);

			out.writeByte(item.stdItem.source);

			out.writeByte(item.stdItem.reserved);
			out.writeByte(0);

			out.writeShort(item.stdItem.looks);

			out.writeInt(item.stdItem.duraMax);
			out.writeInt(item.stdItem.ac);
			out.writeInt(item.stdItem.mac);
			out.writeInt(item.stdItem.dc);
			out.writeInt(item.stdItem.mc);
			out.writeInt(item.stdItem.sc);
			out.writeInt(item.stdItem.need);
			out.writeInt(item.stdItem.needLevel);
			out.writeInt(item.stdItem.price);


			out.writeInt(12345);
			out.writeShort(item.stdItem.duraMax);
			out.writeShort(item.stdItem.duraMax);

		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
		}
	}

	public static final class CharacterAbility extends Packet {

		public Ability tAbility;

		public CharacterAbility() {}

		public CharacterAbility(int gold, int gameGold, Job job, Ability tAbility) {
			super(Protocol.SM_ABILITY);
			this.tAbility = tAbility;
			this.recog = gold;
			this.p1 = makeWord((byte) job.ordinal(), (byte) 99);
			this.p2 = getLowWord(gameGold);
			this.p3 = getHighWord(gameGold);
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			out.writeShort(tAbility.Level);
			out.writeInt(tAbility.AC);
			out.writeInt(tAbility.MAC);
			out.writeInt(tAbility.DC);
			out.writeInt(tAbility.MC);
			out.writeInt(tAbility.SC);
			out.writeShort(tAbility.HP);
			out.writeShort(tAbility.MP);
			out.writeShort(tAbility.MaxHP);
			out.writeShort(tAbility.MaxMP);
			out.writeInt(tAbility.Exp);
			out.writeInt(tAbility.MaxExp);
			out.writeShort(tAbility.Weight);
			out.writeShort(tAbility.MaxWeight);
			out.writeShort(tAbility.WearWeight);
			out.writeShort(tAbility.MaxWearWeight);
			out.writeShort(tAbility.HandWeight);
			out.writeShort(tAbility.MaxHandWeight);
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
		}
	}
}
