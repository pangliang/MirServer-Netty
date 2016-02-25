package com.zhaoxiaodan.mirserver.gameserver;

import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.network.packets.Packet;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.List;

public class ServerPackets {

	public static final class SendNotice extends Packet {

		public String notice;

		public SendNotice() {}

		public SendNotice(String notice) {
			super(Protocol.SM_SENDNOTICE);
			this.notice = notice;
			this.p0 = 2000;
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

		public NewMap(int id,short x, short y, short objectCount, String mapId) {
			super(Protocol.SM_NEWMAP);
			this.mapId = mapId;
			this.p0 = id;
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

	public static final class Logon extends Packet {

		public String mapId;

		public Logon() {}

		public Logon(int id,short x, short y, short objectCount, String mapId) {
			super(Protocol.SM_LOGON);
			this.mapId = mapId;
			this.p0 = id;
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

	public static final class UserName extends Packet {

		public String userName;

		public UserName() {}

		public UserName(int id, short color, String userName) {
			super(Protocol.SM_USERNAME);
			this.userName = userName;
			this.p0 = id;
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

		public MapDescription(String description) {
			super(Protocol.SM_MAPDESCRIPTION);
			this.description = description;
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

	public static final class BagItems extends Packet {

		public BagItems() {}

		public BagItems(int id, List<String> items) {
			super(Protocol.SM_BAGITEMS);
			this.p0 = id;
			this.p3 = (short)items.size();
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
		}
	}
}
