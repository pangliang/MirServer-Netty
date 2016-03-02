package com.zhaoxiaodan.mirserver.network;

import java.util.HashMap;
import java.util.Map;

public enum Protocol {

	// For CM_IDPASSWORD Process
	CM_PROTOCOL(2000,null),
	CM_IDPASSWORD(2001,"Login"),
	CM_IDPASSWORD_2(22001,"Login"),

	CM_ADDNEWUSER(2002,"NewUser"),
	CM_CHANGEPASSWORD(2003,null),
	CM_UPDATEUSER(2004,null),
	CM_SELECTSERVER(104,"SelectServer"),

	SM_CERTIFICATION_FAIL(501,null),
	SM_ID_NOTFOUND(502,null),
	SM_PASSWD_FAIL(503,null),
	SM_NEWID_SUCCESS(504,null),
	SM_NEWID_FAIL(505,null),
	SM_PASSOK_SELECTSERVER(529,"LoginSuccSelectServer"),
	SM_SELECTSERVER_OK(530,"SelectServerOk"),

	// For Select Character Process
	CM_QUERYCHR(100,"QueryCharacter"),
	CM_NEWCHR(101,"NewCharacter"),
	CM_DELCHR(102,"DeleteCharacter"),
	CM_SELCHR(103,"SelectCharacter"),

	SM_QUERYCHR(520,"QueryCharactorOk"),
	SM_NEWCHR_SUCCESS(521,null),
	SM_NEWCHR_FAIL(522,null),
	SM_DELCHR_SUCCESS(523,null),
	SM_DELCHR_FAIL(524,null),
	SM_STARTPLAY(525,"StartPlay"),
	SM_STARTFAIL(526,null),
	SM_QUERYCHR_FAIL(527,null),

	// For Game Gate
	GM_OPEN(1,null),
	GM_CLOSE(2,null),
	GM_CHECKSERVER(3,null),            // Send check signal to Server
	GM_CHECKCLIENT(4,null),            // Send check signal to Client
	GM_DATA(5,null),
	GM_SERVERUSERINDEX(6,null),
	GM_RECEIVE_OK(7,null),
	GM_TEST(20,null),

	// For game process
	// Client To Server Commands
	CM_GAMELOGIN(65001,"GameLogin"),
	CM_QUERYUSERNAME(80,null),
	CM_QUERYBAGITEMS(81,"QueryBagItems"),
	CM_QUERYUSERSTATE(82,"QueryUserState"),

	CM_DROPITEM(1000,null),
	CM_PICKUP(1001,null),
	CM_TAKEONITEM(1003,null),
	CM_TAKEOFFITEM(1004,null),
	CM_BUTCH(1007,null),
	CM_MAGICKEYCHANGE(1008,null),
	CM_LOGINNOTICEOK(1018,"LoginNoticeOk"),


	CM_EAT(1006,null),

	CM_CLICKNPC(1010,null),

	CM_TURN(3010,"MoveAction"),
	CM_WALK(3011,"MoveAction"),
	CM_SITDOWN(3012,null),
	CM_RUN(3013,"MoveAction"),
	CM_HIT(3014,null),
	CM_HEAVYHIT(3015,null),
	CM_BIGHIT(3016,null),
	CM_SPELL(3017,null),
	CM_POWERHIT(3018,null),
	CM_LONGHIT(3019,null),
	CM_WIDEHIT(3024,null),
	CM_FIREHIT(3025,null),
	CM_SAY(3030,null),
	CM_RIDE(3031,null),

	// Server to Client Commands
	SM_RUSH(6,null),
	SM_FIREHIT(8,null),
	SM_BACKSTEP(9,null),
	SM_TURN(10,null),
	SM_WALK(11,null),
	SM_SITDOWN(12,null),
	SM_RUN(13,null),
	SM_HIT(14,null),
	SM_SPELL(17,null),
	SM_POWERHIT(18,null),
	SM_LONGHIT(19,null),
	SM_DIGUP(20,null),
	SM_DIGDOWN(21,null),
	SM_FLYAXE(22,null),
	SM_LIGHTING(23,null),
	SM_WIDEHIT(24,null),
	SM_DISAPPEAR(30,null),
	SM_STRUCK(31,null),
	SM_DEATH(32,null),
	SM_SKELETON(33,null),
	SM_NOWDEATH(34,null),

	SM_HEAR(40,null),
	SM_FEATURECHANGED(41,"FeatureChanged"),
	SM_USERNAME(42,"UserName"),
	SM_WINEXP(44,null),
	SM_LEVELUP(45,null),
	SM_DAYCHANGING(46,null),
	SM_LOGON(50,""),
	SM_NEWMAP(51,"NewMap"),
	SM_ABILITY(52,null),
	SM_HEALTHSPELLCHANGED(53,null),
	SM_MAPDESCRIPTION(54,"MapDescription"),
	SM_GAMEGOLDNAME(55,"GameGoldName"),
	SM_SPELL2(117,null),

	SM_SYSMESSAGE(100,null),
	SM_GROUPMESSAGE(101,null),
	SM_CRY(102,null),
	SM_WHISPER(103,null),
	SM_GUILDMESSAGE(104,null),

	SM_ADDITEM(200,null),
	SM_BAGITEMS(201,"BagItems"),
	SM_ADDMAGIC(210,null),
	SM_SENDMYMAGIC(211,null),

	SM_DROPITEM_SUCCESS(600,null),
	SM_DROPITEM_FAIL(601,null),

	SM_ITEMSHOW(610,null),
	SM_ITEMHIDE(611,null),
	SM_DOOROPEN(612,null),
	SM_TAKEON_OK(615,null),
	SM_TAKEON_FAIL(616,null),
	SM_TAKEOFF_OK(619,null),
	SM_TAKEOFF_FAIL(620,null),
	SM_SENDUSEITEMS(621,null),
	SM_WEIGHTCHANGED(622,null),
	SM_CLEAROBJECTS(633,null),
	SM_CHANGEMAP(634,null),
	SM_EAT_OK(635,null),
	SM_EAT_FAIL(636,null),
	SM_BUTCH(637,null),
	SM_MAGICFIRE(638,null),
	SM_MAGIC_LVEXP(640,null),
	SM_DURACHANGE(642,null),
	SM_MERCHANTSAY(643,null),
	SM_GOLDCHANGED(653,null),
	SM_CHANGELIGHT(654,null),
	SM_CHANGENAMECOLOR(656,null),
	SM_CHARSTATUSCHANGED(657,null),
	SM_SENDNOTICE(658,"SendNotice"),
	SM_AREASTATE(708,"AreaState"),
	SM_SUBABILITY(752,null),

	SM_SHOWEVENT(804,null),
	SM_HIDEEVENT(805,null),

	SM_OPENHEALTH(1100,null),
	SM_CLOSEHEALTH(1101,null),
	SM_CHANGEFACE(1104,null),
	SM_VERSION_FAIL(1106,"VersionFail"),

	SM_ITEMUPDATE(1500,null),
	SM_MONSTERSAY(1501,null),

	// Server to Server Commands
	RM_TURN(10001,null),
	RM_WALK(10002,null),
	RM_RUN(10003,null),
	RM_HIT(10004,null),
	RM_SPELL(10007,null),
	RM_SPELL2(10008,null),
	RM_POWERHIT(10009,null),
	RM_LONGHIT(10011,null),
	RM_WIDEHIT(10012,null),
	RM_PUSH(10013,null),
	RM_FIREHIT(10014,null),
	RM_RUSH(10015,null),
	RM_STRUCK(10020,null),
	RM_DEATH(10021,null),
	RM_DISAPPEAR(10022,null),
	RM_MAGSTRUCK(10025,null),
	RM_STRUCK_MAG(10027,null),
	RM_MAGSTRUCK_MINE(10028,null),
	RM_MAGHEALING(10026,null),
	RM_HEAR(10030,null),
	RM_WHISPER(10031,null),
	RM_CRY(10032,null),
	RM_RIDE(10033,null),
	RM_WINEXP(10044,null),
	RM_USERNAME(10043,null),
	RM_LEVELUP(10045,null),
	RM_CHANGENAMECOLOR(10046,null),

	RM_LOGON(10050,null),
	RM_ABILITY(10051,null),
	RM_HEALTHSPELLCHANGED(10052,null),
	RM_DAYCHANGING(10053,null),

	RM_SYSMESSAGE(10100,null),
	RM_GROUPMESSAGE(10102,null),
	RM_SYSMESSAGE2(10103,null),
	RM_GUILDMESSAGE(10104,null),
	RM_ITEMSHOW(10110,null),
	RM_ITEMHIDE(10111,null),
	RM_DOOROPEN(10112,null),
	RM_SENDUSEITEMS(10114,null),
	RM_WEIGHTCHANGED(10115,null),
	RM_FEATURECHANGED(10116,null),
	RM_CLEAROBJECTS(10117,null),
	RM_CHANGEMAP(10118,null),
	RM_BUTCH(10119,null),
	RM_MAGICFIRE(10120,null),
	RM_SENDMYMAGIC(10122,null),
	RM_MAGIC_LVEXP(10123,null),
	RM_SKELETON(10024,null),
	RM_DURACHANGE(10125,null),
	RM_MERCHANTSAY(10126,null),
	RM_GOLDCHANGED(10136,null),
	RM_CHANGELIGHT(10137,null),
	RM_CHARSTATUSCHANGED(10139,null),
	RM_DELAYMAGIC(10154,null),

	RM_DIGUP(10200,null),
	RM_DIGDOWN(10201,null),
	RM_FLYAXE(10202,null),
	RM_LIGHTING(10204,null),

	RM_SUBABILITY(10302,null),
	RM_TRANSPARENT(10308,null),

	RM_SPACEMOVE_SHOW(10331,null),
	RM_HIDEEVENT(10333,null),
	RM_SHOWEVENT(10334,null),
	RM_ZEN_BEE(10337,null),

	RM_OPENHEALTH(10410,null),
	RM_CLOSEHEALTH(10411,null),
	RM_DOOPENHEALTH(10412,null),
	RM_CHANGEFACE(10415,null),

	RM_ITEMUPDATE(11000,null),
	RM_MONSTERSAY(11001,null),
	RM_MAKESLAVE(11002,null),

	// For DB Server
	DB_LOADHUMANRCD(100,null),
	DB_SAVEHUMANRCD(101,null),
	DB_MAKEITEMRCD(150,null),
	DB_ITEMTHROW(151,null),
	DB_MAKEITEMRCD2(152,null),

	DBR_LOADHUMANRCD(1100,null),
	DBR_LOADHUMANRCD2(1101,null),
	DBR_MAKEITEMRCD(1500,null),
	DBR_MAKEITEMRCD2(1501,null),

	DBR_FAIL(2000,null),

	UNKNOWN_1000(11088,null);

	private static final Map<Short, Protocol> clientProtocols;
	private static final Map<Short, Protocol> serverProtocols;

	static {
		clientProtocols = new HashMap<>();
		serverProtocols = new HashMap<>();
		for (Protocol p : Protocol.values()) {
			if(p.name().charAt(0) == 'C'){
				if(clientProtocols.containsKey(p.id)) {
					System.out.println(("Protocol id " + p.id + " exsit by " + clientProtocols.get(p.id).name()));
				}else{
					clientProtocols.put(p.id, p);
				}
			}else {
				if(serverProtocols.containsKey(p.id)) {
					System.out.println(("Protocol id " + p.id + " exsit by " + serverProtocols.get(p.id).name()));
				}else{
					serverProtocols.put(p.id, p);
				}
			}

		}
	}

	public static Protocol getClientProtocol(short id) {
		if (clientProtocols.containsKey(id))
			return clientProtocols.get(id);
		else
			return null;
	}

	public static Protocol getServerProtocol(short id) {
		if (serverProtocols.containsKey(id))
			return serverProtocols.get(id);
		else
			return null;
	}

	public final short id;
	public final String name;

	Protocol(int id,String name) {
		this.id = (short)id;
		this.name = name;
	}


}
