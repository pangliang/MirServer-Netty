package com.zhaoxiaodan.mirserver.network;

import java.util.HashMap;
import java.util.Map;

public enum Protocol {

	// For CM_IDPASSWORD Process
	CM_PROTOCOL(2000,null,null),
	CM_IDPASSWORD(2001,"Login",null),
	CM_IDPASSWORD_2(22001,"Login",null),

	CM_ADDNEWUSER(2002,"NewUser",null),
	CM_CHANGEPASSWORD(2003,null,null),
	CM_UPDATEUSER(2004,null,null),
	CM_SELECTSERVER(104,"SelectServer",null),

	SM_CERTIFICATION_FAIL(501,null,null),
	SM_ID_NOTFOUND(502,null,null),
	SM_PASSWD_FAIL(503,null,null),
	SM_NEWID_SUCCESS(504,null,null),
	SM_NEWID_FAIL(505,null,null),
	SM_PASSOK_SELECTSERVER(529,"LoginSuccSelectServer",null),
	SM_SELECTSERVER_OK(530,"SelectServerOk",null),

	// For Select Player Process
	CM_QUERYCHR(100,"QueryCharacter",null),
	CM_NEWCHR(101,"NewCharacter",null),
	CM_DELCHR(102,"DeleteCharacter",null),
	CM_SELCHR(103,"SelectCharacter",null),

	SM_QUERYCHR(520,"QueryCharactorOk",null),
	SM_NEWCHR_SUCCESS(521,null,null),
	SM_NEWCHR_FAIL(522,null,null),
	SM_DELCHR_SUCCESS(523,null,null),
	SM_DELCHR_FAIL(524,null,null),
	SM_STARTPLAY(525,"StartPlay",null),
	SM_STARTFAIL(526,null,null),
	SM_QUERYCHR_FAIL(527,null,null),

	// For Game Gate
	GM_OPEN(1,null,null),
	GM_CLOSE(2,null,null),
	GM_CHECKSERVER(3,null,null),            // Send check signal to Server
	GM_CHECKCLIENT(4,null,null),            // Send check signal to Client
	GM_DATA(5,null,null),
	GM_SERVERUSERINDEX(6,null,null),
	GM_RECEIVE_OK(7,null,null),
	GM_TEST(20,null,null),

	// For game process
	// Client To Server Commands
	CM_GAMELOGIN(65001,"GameLogin",null),
	CM_QUERYUSERNAME(80,null,null),
	CM_QUERYBAGITEMS(81,"QueryBagItems",null),
	CM_QUERYUSERSTATE(82,"QueryUserState",null),

	CM_DROPITEM(1000,null,null),
	CM_PICKUP(1001,null,null),
	CM_TAKEONITEM(1003,null,null),
	CM_TAKEOFFITEM(1004,null,null),
	CM_BUTCH(1007,null,null),
	CM_MAGICKEYCHANGE(1008,null,null),
	CM_LOGINNOTICEOK(1018,"LoginNoticeOk",null),


	CM_EAT(1006,null,null),

	CM_CLICKNPC(1010,"Merchant",null),
	CM_MERCHANTDLGSELECT(1011,"Merchant",null),

	CM_TURN(3010,"Action",null),
	CM_WALK(3011,"Action",null),
	CM_SITDOWN(3012,"Action",null),
	CM_RUN(3013,"Action",null),
	CM_HIT(3014,"Action",null),
	CM_HEAVYHIT(3015,"Action",null),
	CM_BIGHIT(3016,"Action",null),
	CM_SPELL(3017,"Action",null),
	CM_POWERHIT(3018,"Action",null),
	CM_LONGHIT(3019,"Action",null),
	CM_WIDEHIT(3024,"Action",null),
	CM_FIREHIT(3025,"Action",null),
	CM_SAY(3030,"Say",null),
	CM_RIDE(3031,null,null),

	// Server to Client Commands
	SM_RUSH(6,null,null),
	SM_FIREHIT(8,null,null),
	SM_BACKSTEP(9,null,null),
	SM_TURN(10,"Turn",new int[]{8}),
	SM_WALK(11,null,null),
	SM_SITDOWN(12,null,null),
	SM_RUN(13,null,null),
	SM_HIT(14,null,null),
	SM_SPELL(17,null,null),
	SM_POWERHIT(18,null,null),
	SM_LONGHIT(19,null,null),
	SM_DIGUP(20,null,null),
	SM_DIGDOWN(21,null,null),
	SM_FLYAXE(22,null,null),
	SM_LIGHTING(23,null,null),
	SM_WIDEHIT(24,null,null),
	SM_DISAPPEAR(30,null,null),
	SM_STRUCK(31,null,null),
	SM_DEATH(32,null,null),
	SM_SKELETON(33,null,null),
	SM_NOWDEATH(34,null,null),

	SM_HEAR(40,null,null),
	SM_FEATURECHANGED(41,"FeatureChanged",null),
	SM_USERNAME(42,"UserName",null),
	SM_WINEXP(44,null,null),
	SM_LEVELUP(45,null,null),
	SM_DAYCHANGING(46,null,null),
	SM_LOGON(50,"",null),
	SM_NEWMAP(51,"NewMap",null),
	SM_ABILITY(52,null,null),
	SM_HEALTHSPELLCHANGED(53,null,null),
	SM_MAPDESCRIPTION(54,"MapDescription",null),
	SM_GAMEGOLDNAME(55,"GameGoldName",null),
	SM_SPELL2(117,null,null),

	SM_SYSMESSAGE(100,"SysMessage",null),
	SM_GROUPMESSAGE(101,null,null),
	SM_CRY(102,null,null),
	SM_WHISPER(103,null,null),
	SM_GUILDMESSAGE(104,null,null),

	SM_ADDITEM(200,null,null),
	SM_BAGITEMS(201,"BagItems",null),
	SM_ADDMAGIC(210,null,null),
	SM_SENDMYMAGIC(211,null,null),

	SM_DROPITEM_SUCCESS(600,null,null),
	SM_DROPITEM_FAIL(601,null,null),

	SM_ITEMSHOW(610,"ItemShow",null),
	SM_ITEMHIDE(611,null,null),
	SM_DOOROPEN(612,null,null),
	SM_TAKEON_OK(615,null,null),
	SM_TAKEON_FAIL(616,null,null),
	SM_TAKEOFF_OK(619,null,null),
	SM_TAKEOFF_FAIL(620,null,null),
	SM_SENDUSEITEMS(621,null,null),
	SM_WEIGHTCHANGED(622,null,null),
	SM_CLEAROBJECTS(633,null,null),
	SM_CHANGEMAP(634,null,null),
	SM_EAT_OK(635,null,null),
	SM_EAT_FAIL(636,null,null),
	SM_BUTCH(637,null,null),
	SM_MAGICFIRE(638,null,null),
	SM_MAGIC_LVEXP(640,null,null),
	SM_DURACHANGE(642,null,null),
	SM_MERCHANTSAY(643,"MerchantSay",null),
	SM_GOLDCHANGED(653,null,null),
	SM_CHANGELIGHT(654,null,null),
	SM_CHANGENAMECOLOR(656,null,null),
	SM_CHARSTATUSCHANGED(657,null,null),
	SM_SENDNOTICE(658,"SendNotice",null),
	SM_AREASTATE(708,"AreaState",null),
	SM_SUBABILITY(752,null,null),

	SM_SHOWEVENT(804,"ShowEvent",null),
	SM_HIDEEVENT(805,null,null),

	SM_OPENHEALTH(1100,null,null),
	SM_CLOSEHEALTH(1101,null,null),
	SM_CHANGEFACE(1104,null,null),
	SM_VERSION_FAIL(1106,"VersionFail",null),

	SM_ITEMUPDATE(1500,null,null),
	SM_MONSTERSAY(1501,null,null),

	// Server to Server Commands
	RM_TURN(10001,null,null),
	RM_WALK(10002,null,null),
	RM_RUN(10003,null,null),
	RM_HIT(10004,null,null),
	RM_SPELL(10007,null,null),
	RM_SPELL2(10008,null,null),
	RM_POWERHIT(10009,null,null),
	RM_LONGHIT(10011,null,null),
	RM_WIDEHIT(10012,null,null),
	RM_PUSH(10013,null,null),
	RM_FIREHIT(10014,null,null),
	RM_RUSH(10015,null,null),
	RM_STRUCK(10020,null,null),
	RM_DEATH(10021,null,null),
	RM_DISAPPEAR(10022,null,null),
	RM_MAGSTRUCK(10025,null,null),
	RM_STRUCK_MAG(10027,null,null),
	RM_MAGSTRUCK_MINE(10028,null,null),
	RM_MAGHEALING(10026,null,null),
	RM_HEAR(10030,null,null),
	RM_WHISPER(10031,null,null),
	RM_CRY(10032,null,null),
	RM_RIDE(10033,null,null),
	RM_WINEXP(10044,null,null),
	RM_USERNAME(10043,null,null),
	RM_LEVELUP(10045,null,null),
	RM_CHANGENAMECOLOR(10046,null,null),

	RM_LOGON(10050,null,null),
	RM_ABILITY(10051,null,null),
	RM_HEALTHSPELLCHANGED(10052,null,null),
	RM_DAYCHANGING(10053,null,null),

	RM_SYSMESSAGE(10100,null,null),
	RM_GROUPMESSAGE(10102,null,null),
	RM_SYSMESSAGE2(10103,null,null),
	RM_GUILDMESSAGE(10104,null,null),
	RM_ITEMSHOW(10110,null,null),
	RM_ITEMHIDE(10111,null,null),
	RM_DOOROPEN(10112,null,null),
	RM_SENDUSEITEMS(10114,null,null),
	RM_WEIGHTCHANGED(10115,null,null),
	RM_FEATURECHANGED(10116,null,null),
	RM_CLEAROBJECTS(10117,null,null),
	RM_CHANGEMAP(10118,null,null),
	RM_BUTCH(10119,null,null),
	RM_MAGICFIRE(10120,null,null),
	RM_SENDMYMAGIC(10122,null,null),
	RM_MAGIC_LVEXP(10123,null,null),
	RM_SKELETON(10024,null,null),
	RM_DURACHANGE(10125,null,null),
	RM_MERCHANTSAY(10126,null,null),
	RM_GOLDCHANGED(10136,null,null),
	RM_CHANGELIGHT(10137,null,null),
	RM_CHARSTATUSCHANGED(10139,null,null),
	RM_DELAYMAGIC(10154,null,null),

	RM_DIGUP(10200,null,null),
	RM_DIGDOWN(10201,null,null),
	RM_FLYAXE(10202,null,null),
	RM_LIGHTING(10204,null,null),

	RM_SUBABILITY(10302,null,null),
	RM_TRANSPARENT(10308,null,null),

	RM_SPACEMOVE_SHOW(10331,null,null),
	RM_HIDEEVENT(10333,null,null),
	RM_SHOWEVENT(10334,null,null),
	RM_ZEN_BEE(10337,null,null),

	RM_OPENHEALTH(10410,null,null),
	RM_CLOSEHEALTH(10411,null,null),
	RM_DOOPENHEALTH(10412,null,null),
	RM_CHANGEFACE(10415,null,null),

	RM_ITEMUPDATE(11000,null,null),
	RM_MONSTERSAY(11001,null,null),
	RM_MAKESLAVE(11002,null,null),

	// For DB Server
	DB_LOADHUMANRCD(100,null,null),
	DB_SAVEHUMANRCD(101,null,null),
	DB_MAKEITEMRCD(150,null,null),
	DB_ITEMTHROW(151,null,null),
	DB_MAKEITEMRCD2(152,null,null),

	DBR_LOADHUMANRCD(1100,null,null),
	DBR_LOADHUMANRCD2(1101,null,null),
	DBR_MAKEITEMRCD(1500,null,null),
	DBR_MAKEITEMRCD2(1501,null,null),

	DBR_FAIL(2000,null,null),

	UNKNOWN_1000(11088,null,null);

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
	public final int[] lenghtOfSections;

	Protocol(int id,String name, int[] lenghtOfSections) {
		this.id = (short)id;
		this.name = name;
		this.lenghtOfSections = lenghtOfSections;
	}


}
