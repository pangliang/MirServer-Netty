/**
 * Created by liangwei on 16/2/17.
 */
public class Protocol {

	// For Login Process
	public static final short CM_PROTOCOL       = 2000;
	public static final short CM_IDPASSWORD     = 2001;
	public static final short CM_ADDNEWUSER     = 2002;
	public static final short CM_CHANGEPASSWORD = 2003;
	public static final short CM_UPDATEUSER     = 2004;

	public static final short CM_SELECTSERVER = 104;

	public static final short SM_CERTIFICATION_FAIL  = 501;
	public static final short SM_ID_NOTFOUND         = 502;
	public static final short SM_PASSWD_FAIL         = 503;
	public static final short SM_NEWID_SUCCESS       = 504;
	public static final short SM_NEWID_FAIL          = 505;
	public static final short SM_PASSOK_SELECTSERVER = 529;
	public static final short SM_SELECTSERVER_OK     = 530;

	// For Select Character Process
	public static final short CM_QUERYCHR = 100;
	public static final short CM_NEWCHR   = 101;
	public static final short CM_DELCHR   = 102;
	public static final short CM_SELCHR   = 103;

	public static final short SM_QUERYCHR       = 520;
	public static final short SM_NEWCHR_SUCCESS = 521;
	public static final short SM_NEWCHR_FAIL    = 522;
	public static final short SM_DELCHR_SUCCESS = 523;
	public static final short SM_DELCHR_FAIL    = 524;
	public static final short SM_STARTPLAY      = 525;
	public static final short SM_STARTFAIL      = 526;
	public static final short SM_QUERYCHR_FAIL  = 527;

	// For Game Gate
	public static final short GM_OPEN            = 1;
	public static final short GM_CLOSE           = 2;
	public static final short GM_CHECKSERVER     = 3;            // Send check signal to Server
	public static final short GM_CHECKCLIENT     = 4;            // Send check signal to Client
	public static final short GM_DATA            = 5;
	public static final short GM_SERVERUSERINDEX = 6;
	public static final short GM_RECEIVE_OK      = 7;
	public static final short GM_TEST            = 20;

	// For game process
	// Client To Server Commands
	public static final short CM_QUERYUSERNAME = 80;
	public static final short CM_QUERYBAGITEMS = 81;

	public static final short CM_DROPITEM       = 1000;
	public static final short CM_PICKUP         = 1001;
	public static final short CM_TAKEONITEM     = 1003;
	public static final short CM_TAKEOFFITEM    = 1004;
	public static final short CM_BUTCH          = 1007;
	public static final short CM_MAGICKEYCHANGE = 1008;
	public static final short CM_EAT            = 1006;
	public static final short CM_TURN           = 3010;
	public static final short CM_WALK           = 3011;
	public static final short CM_SITDOWN        = 3012;
	public static final short CM_RUN            = 3013;
	public static final short CM_HIT            = 3014;
	public static final short CM_HEAVYHIT       = 3015;
	public static final short CM_BIGHIT         = 3016;
	public static final short CM_SPELL          = 3017;
	public static final short CM_POWERHIT       = 3018;
	public static final short CM_LONGHIT        = 3019;
	public static final short CM_WIDEHIT        = 3024;
	public static final short CM_FIREHIT        = 3025;
	public static final short CM_SAY            = 3030;
	public static final short CM_RIDE           = 3031;

	// Server to Client Commands
	public static final short SM_RUSH      = 6;
	public static final short SM_FIREHIT   = 8;
	public static final short SM_BACKSTEP  = 9;
	public static final short SM_TURN      = 10;
	public static final short SM_WALK      = 11;
	public static final short SM_SITDOWN   = 12;
	public static final short SM_RUN       = 13;
	public static final short SM_HIT       = 14;
	public static final short SM_SPELL     = 17;
	public static final short SM_POWERHIT  = 18;
	public static final short SM_LONGHIT   = 19;
	public static final short SM_DIGUP     = 20;
	public static final short SM_DIGDOWN   = 21;
	public static final short SM_FLYAXE    = 22;
	public static final short SM_LIGHTING  = 23;
	public static final short SM_WIDEHIT   = 24;
	public static final short SM_DISAPPEAR = 30;
	public static final short SM_STRUCK    = 31;
	public static final short SM_DEATH     = 32;
	public static final short SM_SKELETON  = 33;
	public static final short SM_NOWDEATH  = 34;

	public static final short SM_HEAR               = 40;
	public static final short SM_FEATURECHANGED     = 41;
	public static final short SM_USERNAME           = 42;
	public static final short SM_WINEXP             = 44;
	public static final short SM_LEVELUP            = 45;
	public static final short SM_DAYCHANGING        = 46;
	public static final short SM_LOGON              = 50;
	public static final short SM_NEWMAP             = 51;
	public static final short SM_ABILITY            = 52;
	public static final short SM_HEALTHSPELLCHANGED = 53;
	public static final short SM_MAPDESCRIPTION     = 54;
	public static final short SM_SPELL2             = 117;

	public static final short SM_SYSMESSAGE   = 100;
	public static final short SM_GROUPMESSAGE = 101;
	public static final short SM_CRY          = 102;
	public static final short SM_WHISPER      = 103;
	public static final short SM_GUILDMESSAGE = 104;

	public static final short SM_ADDITEM     = 200;
	public static final short SM_BAGITEMS    = 201;
	public static final short SM_ADDMAGIC    = 210;
	public static final short SM_SENDMYMAGIC = 211;

	public static final short SM_DROPITEM_SUCCESS = 600;
	public static final short SM_DROPITEM_FAIL    = 601;

	public static final short SM_ITEMSHOW          = 610;
	public static final short SM_ITEMHIDE          = 611;
	public static final short SM_DOOROPEN          = 612;
	public static final short SM_TAKEON_OK         = 615;
	public static final short SM_TAKEON_FAIL       = 616;
	public static final short SM_TAKEOFF_OK        = 619;
	public static final short SM_TAKEOFF_FAIL      = 620;
	public static final short SM_SENDUSEITEMS      = 621;
	public static final short SM_WEIGHTCHANGED     = 622;
	public static final short SM_CLEAROBJECTS      = 633;
	public static final short SM_CHANGEMAP         = 634;
	public static final short SM_EAT_OK            = 635;
	public static final short SM_EAT_FAIL          = 636;
	public static final short SM_BUTCH             = 637;
	public static final short SM_MAGICFIRE         = 638;
	public static final short SM_MAGIC_LVEXP       = 640;
	public static final short SM_DURACHANGE        = 642;
	public static final short SM_MERCHANTSAY       = 643;
	public static final short SM_GOLDCHANGED       = 653;
	public static final short SM_CHANGELIGHT       = 654;
	public static final short SM_CHANGENAMECOLOR   = 656;
	public static final short SM_CHARSTATUSCHANGED = 657;

	public static final short SM_SUBABILITY = 752;

	public static final short SM_SHOWEVENT = 804;
	public static final short SM_HIDEEVENT = 805;

	public static final short SM_OPENHEALTH  = 1100;
	public static final short SM_CLOSEHEALTH = 1101;
	public static final short SM_CHANGEFACE  = 1104;

	public static final short SM_ITEMUPDATE = 1500;
	public static final short SM_MONSTERSAY = 1501;

	// Server to Server Commands
	public static final short RM_TURN            = 10001;
	public static final short RM_WALK            = 10002;
	public static final short RM_RUN             = 10003;
	public static final short RM_HIT             = 10004;
	public static final short RM_SPELL           = 10007;
	public static final short RM_SPELL2          = 10008;
	public static final short RM_POWERHIT        = 10009;
	public static final short RM_LONGHIT         = 10011;
	public static final short RM_WIDEHIT         = 10012;
	public static final short RM_PUSH            = 10013;
	public static final short RM_FIREHIT         = 10014;
	public static final short RM_RUSH            = 10015;
	public static final short RM_STRUCK          = 10020;
	public static final short RM_DEATH           = 10021;
	public static final short RM_DISAPPEAR       = 10022;
	public static final short RM_MAGSTRUCK       = 10025;
	public static final short RM_STRUCK_MAG      = 10027;
	public static final short RM_MAGSTRUCK_MINE  = 10028;
	public static final short RM_MAGHEALING      = 10026;
	public static final short RM_HEAR            = 10030;
	public static final short RM_WHISPER         = 10031;
	public static final short RM_CRY             = 10032;
	public static final short RM_RIDE            = 10033;
	public static final short RM_WINEXP          = 10044;
	public static final short RM_USERNAME        = 10043;
	public static final short RM_LEVELUP         = 10045;
	public static final short RM_CHANGENAMECOLOR = 10046;

	public static final short RM_LOGON              = 10050;
	public static final short RM_ABILITY            = 10051;
	public static final short RM_HEALTHSPELLCHANGED = 10052;
	public static final short RM_DAYCHANGING        = 10053;

	public static final short RM_SYSMESSAGE        = 10100;
	public static final short RM_GROUPMESSAGE      = 10102;
	public static final short RM_SYSMESSAGE2       = 10103;
	public static final short RM_GUILDMESSAGE      = 10104;
	public static final short RM_ITEMSHOW          = 10110;
	public static final short RM_ITEMHIDE          = 10111;
	public static final short RM_DOOROPEN          = 10112;
	public static final short RM_SENDUSEITEMS      = 10114;
	public static final short RM_WEIGHTCHANGED     = 10115;
	public static final short RM_FEATURECHANGED    = 10116;
	public static final short RM_CLEAROBJECTS      = 10117;
	public static final short RM_CHANGEMAP         = 10118;
	public static final short RM_BUTCH             = 10119;
	public static final short RM_MAGICFIRE         = 10120;
	public static final short RM_SENDMYMAGIC       = 10122;
	public static final short RM_MAGIC_LVEXP       = 10123;
	public static final short RM_SKELETON          = 10024;
	public static final short RM_DURACHANGE        = 10125;
	public static final short RM_MERCHANTSAY       = 10126;
	public static final short RM_GOLDCHANGED       = 10136;
	public static final short RM_CHANGELIGHT       = 10137;
	public static final short RM_CHARSTATUSCHANGED = 10139;
	public static final short RM_DELAYMAGIC        = 10154;

	public static final short RM_DIGUP    = 10200;
	public static final short RM_DIGDOWN  = 10201;
	public static final short RM_FLYAXE   = 10202;
	public static final short RM_LIGHTING = 10204;

	public static final short RM_SUBABILITY  = 10302;
	public static final short RM_TRANSPARENT = 10308;

	public static final short RM_SPACEMOVE_SHOW = 10331;
	public static final short RM_HIDEEVENT      = 10333;
	public static final short RM_SHOWEVENT      = 10334;
	public static final short RM_ZEN_BEE        = 10337;

	public static final short RM_OPENHEALTH   = 10410;
	public static final short RM_CLOSEHEALTH  = 10411;
	public static final short RM_DOOPENHEALTH = 10412;
	public static final short RM_CHANGEFACE   = 10415;

	public static final short RM_ITEMUPDATE = 11000;
	public static final short RM_MONSTERSAY = 11001;
	public static final short RM_MAKESLAVE  = 11002;

	// For DB Server
	public static final short DB_LOADHUMANRCD = 100;
	public static final short DB_SAVEHUMANRCD = 101;
	public static final short DB_MAKEITEMRCD  = 150;
	public static final short DB_ITEMTHROW    = 151;
	public static final short DB_MAKEITEMRCD2 = 152;

	public static final short DBR_LOADHUMANRCD  = 1100;
	public static final short DBR_LOADHUMANRCD2 = 1101;
	public static final short DBR_MAKEITEMRCD   = 1500;
	public static final short DBR_MAKEITEMRCD2  = 1501;

	public static final short DBR_FAIL = 2000;
}
