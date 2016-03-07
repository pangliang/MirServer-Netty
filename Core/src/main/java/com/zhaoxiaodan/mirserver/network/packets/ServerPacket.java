package com.zhaoxiaodan.mirserver.network.packets;

import com.zhaoxiaodan.mirserver.db.entities.Player;
import com.zhaoxiaodan.mirserver.db.entities.PlayerItem;
import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import com.zhaoxiaodan.mirserver.db.objects.BaseObject;
import com.zhaoxiaodan.mirserver.db.types.*;
import com.zhaoxiaodan.mirserver.network.Protocol;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ServerPacket extends Packet {

	public ServerPacket() {
		super();
	}

	public ServerPacket(int recog, Protocol protocol, short p1, short p2, short p3) {
		super(recog, protocol, p1, p2, p3);
	}

	public ServerPacket(int recog, Protocol protocol) {
		this(recog, protocol, (short) 0, (short) 0, (short) 0);
	}

	public ServerPacket(Protocol protocol) {
		super(protocol);
	}

	public static final class LoginSuccSelectServer extends ServerPacket {

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
				out.writeBytes(infoStr.getBytes());
			}
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);

			serverInfoList = new ArrayList<>();
			String content = in.toString(Charset.defaultCharset());

			String[] parts = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length < 2)
				throw new Parcelable.WrongFormatException();
			for (int i = 0; i + 1 < parts.length; i += 2) {
				ServerInfo info = new ServerInfo();
				info.name = parts[i];
				info.id = Integer.parseInt(parts[i + 1]);
				serverInfoList.add(info);
			}
		}
	}

	public static final class LoginFail extends ServerPacket {

		public class Reason {

			/**
			 * -1: FrmDlg.DMessageDlg('密码错误！！', [mbOk]);
			 * -2: FrmDlg.DMessageDlg('密码输入错误超过3次，此帐号被暂时锁定，请稍候再登录！', [mbOk]);
			 * -3: FrmDlg.DMessageDlg('此帐号已经登录或被异常锁定，请稍候再登录！', [mbOk]);
			 * -4: FrmDlg.DMessageDlg('这个帐号访问失败！\请使用其他帐号登录，\或者申请付费注册。', [mbOk]);
			 * -5: FrmDlg.DMessageDlg('这个帐号被锁定！', [mbOk]);
			 * else FrmDlg.DMessageDlg('此帐号不存在或出现未知错误！！', [mbOk]);
			 */
			public static final int UserNotFound   = 0;
			public static final int WrongPwd       = -1;
			public static final int WrongPwd3Times = -2;
			public static final int AlreadyLogin   = -3;
			public static final int NoPay          = -4;
			public static final int BeLock         = -5;
		}

		public LoginFail(int reason) {
			super(Protocol.SM_PASSWD_FAIL);
			recog = reason;
		}
	}

	public static final class SelectServerOk extends ServerPacket {

		public String selectServerIp;
		public int    selectserverPort;
		public int    cert;

		public SelectServerOk() {}

		public SelectServerOk(String selectServerIp, int selectserverPort, int cert) {
			super(Protocol.SM_SELECTSERVER_OK);
			this.selectServerIp = selectServerIp;
			this.selectserverPort = selectserverPort;
			this.cert = cert;
			recog = cert;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			out.writeBytes(selectServerIp.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(Integer.toString(selectserverPort).getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(Integer.toString(cert).getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);

			String content = in.toString(Charset.defaultCharset()).trim();

			String[] parts = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length < 3)
				throw new Parcelable.WrongFormatException();
			this.selectServerIp = parts[0];
			this.selectserverPort = Integer.parseInt(parts[1]);
			this.cert = Short.parseShort(parts[2]);
		}
	}

	public static final class QueryCharactorOk extends ServerPacket {

		public List<Player> playerList;

		public QueryCharactorOk() {}

		public QueryCharactorOk(List<Player> playerList) {
			super(Protocol.SM_QUERYCHR);
			this.playerList = playerList;
			this.recog = playerList.size();
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			for (Player cha : this.playerList) {
				out.writeBytes(cha.name.getBytes());
				out.writeByte(CONTENT_SEPARATOR_CHAR);
				out.writeBytes(Integer.toString(cha.job.ordinal()).getBytes());
				out.writeByte(CONTENT_SEPARATOR_CHAR);
				out.writeBytes(Integer.toString(cha.hair).getBytes());
				out.writeByte(CONTENT_SEPARATOR_CHAR);
				out.writeBytes(Integer.toString(cha.ability.Level).getBytes());
				out.writeByte(CONTENT_SEPARATOR_CHAR);
				out.writeBytes(Integer.toString(cha.gender.ordinal()).getBytes());
				out.writeByte(CONTENT_SEPARATOR_CHAR);
			}
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);

			if (this.recog > 0) {
				String content = in.toString(Charset.defaultCharset()).trim();

				String[] parts = content.split(CONTENT_SEPARATOR_STR);
				if (parts.length < 5 * this.recog)
					throw new Parcelable.WrongFormatException();
				this.playerList = new ArrayList<>();
				for (int i = 0; i + 4 < parts.length; i += 5) {
					Player cha = new Player();
					cha.name = parts[i + 0];
					cha.job = Job.values()[Byte.parseByte(parts[i + 1])];
					cha.hair = Byte.parseByte(parts[i + 2]);
					cha.ability.Level = Short.parseShort(parts[i + 3]);
					cha.gender = Gender.values()[Byte.parseByte(parts[i + 4])];

					playerList.add(cha);
				}
			}
		}
	}

	public static final class StartPlay extends ServerPacket {

		public String serverIp;
		public int    serverPort;

		public StartPlay() {}

		public StartPlay(String serverIp, int serverPort) {
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
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);

			String content = in.toString(Charset.defaultCharset()).trim();

			String[] parts = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length < 2)
				throw new Parcelable.WrongFormatException();
			this.serverIp = parts[0];
			this.serverPort = Integer.parseInt(parts[1]);
		}
	}

	/********************************************************************
	 * GameServer
	 ********************************************************************/

	public static final class SendNotice extends ServerPacket {

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
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			this.notice = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class NewMap extends ServerPacket {

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
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			this.mapId = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class ChangeMap extends ServerPacket {

		public String mapId;

		public ChangeMap() {}

		public ChangeMap(int id, short x, short y, short objectCount, String mapId) {
			super(Protocol.SM_CHANGEMAP);
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
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			this.mapId = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class VersionFail extends ServerPacket {

		public int file1CRC;
		public int file2CRC;
		public int file3CRC;

		public VersionFail() {}

		public VersionFail(int file1CRC, int file2CRC, int file3CRC) {
			super(Protocol.SM_VERSION_FAIL);
			this.recog = file1CRC;
			this.p1 = NumUtil.getLowWord(file2CRC);
			this.p2 = NumUtil.getHighWord(file2CRC);

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
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			this.file3CRC = in.readInt();
			this.file1CRC = this.recog;
			this.file2CRC = NumUtil.makeLong(this.p1, this.p2);
		}
	}

	public static final class Logon extends ServerPacket {

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
			this.p3 = NumUtil.makeWord(direction, light);
		}

		public Logon(Player player) {
			this(player.inGameId,
					player.currMapPoint.x,
					player.currMapPoint.y,
					(byte) player.direction.ordinal(),
					player.light,
					player.getFeature(),
					player.getStatus(),
					(short) player.getFeatureEx());
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
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);

			this.feature = in.readInt();
			this.charStatus = in.readInt();
			this.featureEx = in.readShort();

			this.charId = this.recog;
			this.currX = p1;
			this.currY = p2;
			this.direction = NumUtil.getLowByte(p3);
			this.light = NumUtil.getHighByte(p3);
		}
	}

	public static final class FeatureChanged extends ServerPacket {

		public int charId;
		public int feature;
		public int featureEx;

		public FeatureChanged() {}

		public FeatureChanged(int charId, int feature, short featureEx) {
			super(Protocol.SM_FEATURECHANGED);
			this.charId = charId;
			this.recog = charId;
			this.p1 = NumUtil.getLowWord(feature);
			this.p2 = NumUtil.getHighWord(feature);
			this.p3 = featureEx;

			this.feature = feature;
			this.featureEx = featureEx;
		}

		public FeatureChanged(BaseObject object) {
			this(object.inGameId, object.getFeature(), (short) object.getFeatureEx());
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			this.charId = recog;
			this.feature = NumUtil.makeLong(p1, p2);
			this.featureEx = p3;
		}
	}

	public static final class UserName extends ServerPacket {

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
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			this.userName = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class MapDescription extends ServerPacket {

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
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			this.description = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class GameGoldName extends ServerPacket {

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
			this.p1 = NumUtil.getLowWord(gamePoint);
			this.p2 = NumUtil.getHighWord(gamePoint);
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(gameGoldName.getBytes());
			out.writeByte(13);
			out.writeBytes(gamePointName.getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			String   content = in.toString(Charset.defaultCharset()).trim();
			String[] parts   = content.split("" + (char) (13));
			if (parts.length > 1) {
				this.gameGoldName = parts[0];
				this.gamePointName = parts[1];
			}
		}
	}

	public static final class AddItem extends ServerPacket {

		public PlayerItem item;

		public AddItem() {}

		public AddItem(int id, PlayerItem item) {
			super(Protocol.SM_ADDITEM);
			this.recog = id;
			this.p3 = 1;
			this.item = item;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			item.writePacket(out);
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
		}
	}

	public static final class PlayerAbility extends ServerPacket {

		public Ability tAbility;

		public PlayerAbility() {}

		public PlayerAbility(int gold, int gameGold, Job job, Ability tAbility) {
			super(Protocol.SM_ABILITY);
			this.tAbility = tAbility;
			this.recog = gold;
			this.p1 = NumUtil.makeWord((byte) job.ordinal(), (byte) 99);
			this.p2 = NumUtil.getLowWord(gameGold);
			this.p3 = NumUtil.getHighWord(gameGold);
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			tAbility.writePacket(out);
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
		}
	}

	public static final class Status extends ServerPacket {

		public enum Result {
			Good("+GOOD"),
			Fail("+FAIL");

			public String content;

			private Result(String content) {
				this.content = content;
			}
		}

		public Result result;
		public long   tickCount;

		public Status() {}

		public Status(Result result) {
			this.result = result;
			this.tickCount = NumUtil.getTickCount();
		}

		@Override
		public void writePacket(ByteBuf out) {
			out.writeBytes(result.content.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(Long.toString(this.tickCount).getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			String   content = in.toString(1, in.readableBytes() - 2, Charset.defaultCharset()).trim();
			String[] parts   = content.split(CONTENT_SEPARATOR_STR);
			if (parts.length > 1) {
				for (Result result : Result.values()) {
					if (result.content.equals(parts[0])) {
						this.result = result;
						this.tickCount = Long.parseLong(parts[1]);
						return;
					}
				}
			}
			throw new WrongFormatException("content not match !! content:" + content);
		}
	}

	protected static class XYPacket extends ServerPacket {

		public short x;
		public short y;

		public XYPacket() {}

		public XYPacket(Protocol protocol, short x, short y) {
			super(protocol);
			this.x = x;
			this.y = y;
			this.p1 = x;
			this.p2 = y;
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.x = p1;
			this.y = p2;
		}
	}

	public static final class ItemShow extends XYPacket {


		public int    itemId;
		public short  itemLook;
		public String itemName;

		public ItemShow() {}

		public ItemShow(int itemId, short itemLook, short x, short y, String itemName) {
			super(Protocol.SM_ITEMSHOW, x, y);
			this.itemLook = itemLook;
			this.itemId = itemId;
			this.itemName = itemName;

			this.recog = itemId;
			this.p3 = itemLook;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(itemName.getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);

			itemLook = p3;
			itemId = recog;
			String content = in.toString(Charset.defaultCharset());
			itemName = content.trim();
		}
	}

	public static final class Turn extends XYPacket {

		public int       id;
		public Direction direction;
		public String    name;
		public int       nameColor;
		public int       feature;
		public int       status;
		public byte      light;

		public Turn() {}

		public Turn(int id, Direction direction, short x, short y, int feature, int status, byte light, String name, int nameColor) {
			super(Protocol.SM_TURN, x, y);
			this.id = id;
			this.direction = direction;
			this.name = name;
			this.recog = id;
			this.p3 = NumUtil.makeWord((byte) direction.ordinal(), light);
			this.feature = feature;
			this.status = status;
			this.nameColor = nameColor;
		}

		public Turn(BaseObject object) {
			this(object.inGameId,
					object.direction,
					object.currMapPoint.x,
					object.currMapPoint.y,
					object.getFeature(),
					object.getStatus(),
					object.light,
					object.name,
					object.nameColor.c);
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeInt(feature);
			out.writeInt(status);
			out.writeBytes(name.getBytes(Charset.defaultCharset()));
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(Integer.toString(nameColor).getBytes());
		}

		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			id = recog;
			direction = Direction.values()[NumUtil.getLowByte(p3)];
			light = NumUtil.getHighByte(p3);
			feature = in.readInt();
			status = in.readInt();
			String   content = in.toString(Charset.defaultCharset());
			String[] parts   = content.split(CONTENT_SEPARATOR_STR);
			name = parts[0];
			nameColor = parts.length > 1 ? Integer.parseInt(parts[1]) : Color.White.c;
		}
	}

	/**
	 * 这个包又是不一样的, p2 x  p3 y, 别人都是 p1 x p2 y
	 */
	public static final class ShowEvent extends ServerPacket {

		public int    id;
		public short  look;
		public String desc;
		public short  x;
		public short  y;

		public ShowEvent() {}

		public ShowEvent(int id, short look, short x, short y, String desc) {
			super(Protocol.SM_SHOWEVENT);
			this.id = id;
			this.look = look;
			this.x = x;
			this.y = y;

			this.desc = desc;
			this.recog = id;
			this.p1 = look;
			this.p2 = x;
			this.p3 = y;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(desc.getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
			super.readPacket(in);
			id = recog;
			look = p1;
			x = p2;
			y = p3;
			String content = in.toString(Charset.defaultCharset());
			desc = content.trim();
		}
	}

	public static final class SysMessage extends ServerPacket {

		public int    inGameId;
		public short  frontColor;
		public short  backgroundColor;
		public String msg;

		public SysMessage() {}

		public SysMessage(int inGameId, String msg, Color ftCorol, Color bgColor) {
			this(inGameId, msg, ftCorol.c, bgColor.c);
		}

		public SysMessage(int inGameId, String msg, short frontColor, short backgroundColor) {
			super(Protocol.SM_SYSMESSAGE);
			this.inGameId = inGameId;
			this.frontColor = frontColor;
			this.backgroundColor = backgroundColor;
			this.msg = msg;

			this.recog = inGameId;
			this.p1 = NumUtil.makeWord(frontColor, backgroundColor);
			this.p3 = 1;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(msg.getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			this.frontColor = NumUtil.getLowByte(p1);
			this.backgroundColor = NumUtil.getHighByte(p1);
			this.msg = in.toString(Charset.defaultCharset()).trim();
		}
	}

	public static final class MerchantSay extends ServerPacket {

		public int    inGameId;
		public String msg;
		public String merchantName;

		public MerchantSay() {}


		public MerchantSay(int inGameId, String msg, String merchantName) {
			super(Protocol.SM_MERCHANTSAY);
			this.inGameId = inGameId;
			this.msg = msg;
			this.merchantName = merchantName;

			this.recog = inGameId;
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			out.writeBytes(merchantName.getBytes());
			out.writeByte(CONTENT_SEPARATOR_CHAR);
			out.writeBytes(msg.getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException {
			super.readPacket(in);
			String[] parts = in.toString(Charset.defaultCharset()).trim().split(CONTENT_SEPARATOR_STR);
			if (parts.length > 1) {
				this.merchantName = parts[0];
				this.msg = parts[1].trim();
			} else {
				this.msg = parts[0].trim();
			}
		}
	}


}
