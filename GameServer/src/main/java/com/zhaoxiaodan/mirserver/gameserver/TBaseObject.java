package com.zhaoxiaodan.mirserver.gameserver;

import java.util.List;

public class TBaseObject {

	String m_sMapName; // 0x04
	public String        m_sCharName; // 0x15
	public int           m_nCurrX; // 0x24  人物所在座标X(4字节)
	public int           m_nCurrY; // 0x28  人物所在座标Y(4字节)
	public byte          m_btDirection; // 人物所在方向(1字节)
	public byte          m_btGender; // 0x2D  人物的性别(1字节)
	public byte          m_btHair; // 0x2E  人物的头发(1字节)
	public byte          m_btJob; // 0x2F  人物的职业(1字节)
	public int           m_nGold; // 0x30  人物金币数(4字节)
	public TAbility      m_Abil; // 0x34 -> 0x5B
	public int           m_nCharStatus; // 0x5C
	public String        m_sHomeMap; // 0x78  //回城地图
	public int           m_nHomeX; // 0x8C  //回城座标X
	public int           m_nHomeY; // 0x90  //回城座标Y
	public boolean       bo94; // 0x94
	public boolean       m_boOnHorse; // 0x95
	public byte          m_btHorseType;
	public byte          m_btDressEffType;
	public int           n98; // 0x98
	public int           n9C; // 0x9C
	public int           nA0; // 0xA0
	public int           nA4; // 0xA4
	public int           nA8; // 0xA8
	public int           m_nPkPoint; // 0xAC  人物的PK值(4字节)
	public boolean       m_boAllowGroup; // 0xB0  允许组队
	public boolean       m_boAllowGuild; // 0xB1  允许加入行会
	public byte          btB2; // 0xB2
	public byte          btB3; // 0xB3
	public int           m_nIncHealth; // 0x0B4
	public int           m_nIncSpell; // 0x0B8
	public int           m_nIncHealing; // 0x0BC
	public int           m_nFightZoneDieCount; // 0x0C0  //在行会占争地图中死亡次数
	public int           nC4;
	public byte          btC8; // 0xC8
	public byte          btC9; // 0xC9
	public TNakedAbility m_BonusAbil; // 0x0CA TNakedAbility
	public TNakedAbility m_CurBonusAbil; // 0x0DE
	public int           m_nBonusPoint; // 0x0F4
	public int           m_nHungerStatus; // 0x0F8
	public boolean       m_boAllowGuildReCall; // 0xFC
	public byte          btFC;              //:byte;
	public byte          m_btFD;
	public byte          m_btFE;
	public byte          m_btFF;
	public Double        m_dBodyLuck; // 0x100
	public int           m_nBodyLuckLevel; // 0x108
	public short         m_wGroupRcallTime; // 0x10C
	public boolean       m_boAllowGroupReCall; // 0x10E

	public TQuestUnit m_QuestUnitOpen; // 0x10F
	public TQuestUnit m_QuestUnit; // 0x11C
	public TQuestFlag m_QuestFlag; // 0x128 129

	public int         m_nCharStatusEx;
	public int         m_dwFightExp; // 0x194   //怪物经验值
	public TAbility    m_WAbil; // 0x198
	public TAddAbility m_AddAbil; // 0x1C0
	public int         m_nViewRange; // 0x1E4   //可视范围大小
	public TStatusTime m_wStatusTimeArr; // 0x60
	public int[] m_dwStatusArrTick = new int[12]; // 0x1E8
	public boolean m_boStatus; //人物属性TRUE =上升 FALSE =下降
	public boolean m_boAC;
	public boolean m_boMAC;
	public boolean m_boDC;
	public boolean m_boMC;
	public boolean m_boSC;
	public boolean m_boHitSpeed;
	public boolean m_boMaxHP;
	public boolean m_boMaxMP;
	public short[] m_wStatusArrValue        = new short[6]; // 0x218
	public int[]   m_dwStatusArrTimeOutTick = new int[6];           //0x220
	public short        m_wAppr; // 0x238
	public byte         m_btRaceServer; // 0x23A   //角色类型
	public byte         m_btRaceImg; // 0x23B   //角色外形
	public byte         m_btHitPoint; // 0x23C   人物攻击准确度(byte)
	public short        m_nHitPlus; // 0x23D
	public short        m_nHitDouble; // 0x23E
	public int          m_dwGroupRcallTick; // 0x240  记忆使用间隔(Dshort)
	public boolean      m_boRecallSuite; // 0x244  记忆全套
	public boolean      bo245; // 0x245
	public boolean      m_boTestGa; // 0x246  //是否输入Testga 命令
	public boolean      m_boGsa; // 0x247  //是否输入gsa 命令
	public short        m_nHealthRecover; // 0x248
	public short        m_nSpellRecover; // 0x249
	public byte         m_btAntiPoison; // 0x24A
	public short        m_nPoisonRecover; // 0x24B
	public short        m_nAntiMagic; // 0x24C
	public int          m_nLuck; // 0x250  人物的幸运值Luck
	public int          m_nPerHealth; // 0x254
	public int          m_nPerHealing; // 0x258
	public int          m_nPerSpell; // 0x25C
	public int          m_dwIncHealthSpellTick; // 0x260
	public byte         m_btGreenPoisoningPoint; // 0x264  中绿毒降HP点数
	public int          m_nGoldMax; // 0x268  人物身上最多可带金币数(Dshort)
	public byte         m_btSpeedPoint; // 0x26C  人物敏捷度(byte)
	public byte         m_btPermission; // 0x26D  人物权限等级
	public short        m_nHitSpeed; // 0x26E  //1-18 更改数据类型
	public byte         m_btLifeAttrib; // 0x26F
	public byte         m_btCoolEye; // 0x270
	public TBaseObject  m_GroupOwner; // 0x274
	public List<String> m_GroupMembers; // 0x278  组成员
	public boolean      m_boHearWhisper; // 0x27C  允许私聊
	public boolean      m_boBanShout; // 0x27D  允许群聊
	public boolean      m_boBanGuildChat; // 0x27E  拒绝行会聊天
	public boolean      m_boAllowDeal; // 0x27F  是不允许交易
	public List<String> m_BlockWhisperList; // 0x280  禁止私聊人员列表
	public int          m_dwShoutMsgTick; // 0x284
	public TBaseObject  m_Master; // 0x288  是否被召唤(主人)
	public int          m_dwMasterRoyaltyTick; // 0x28C  怪物叛变时间
	public int          m_dwMasterTick; // 0x290
	public int          n294; // 0x294  杀怪计数
	public byte         m_btSlaveExpLevel; // 0x298  宝宝等级 1-7
	public byte         m_btSlaveMakeLevel; // 0x299  召唤等级
	public List         m_SlaveList; // 0x29C  下属列表
	public byte         bt2A0; // 0x2A0
	public boolean      m_boSlaveRelax; // 0x2A0  宝宝攻击状态(休息/攻击)(byte)
	public byte         m_btAttatckMode; // 0x2A1  下属攻击状态
	public byte         m_btNameColor; // 0x2A2  人物名字的颜色(byte)
	public int          m_nLight; // 0x2A4  亮度
	public boolean      m_boGuildWarArea; // 0x2A8  行会占争范围
	public Object       m_Castle; // 0x2AC //所属城堡
	public boolean      bo2B0; // 0x2B0
	public int          m_dw2B4Tick; // 0x2B4
	public boolean      m_boSuperMan; // 0x2B8  无敌模式
	public boolean      bo2B9; // 0x2B9
	public boolean      bo2BA; // 0x2BA
	public boolean      m_boAnimal; // 0x2BB
	public boolean      m_boNoItem; // 0x2BC
	public boolean      m_boFixedHideMode; // 0x2BD
	public boolean      m_boStickMode; // 0x2BE
	public boolean      bo2BF; // 0x2BF
	public boolean      m_boNoAttackMode; // 0x2C0
	public boolean      bo2C1; // 0x2C1
	public boolean      m_boSkeleton; // 0x2C2
	public int          m_nMeatQuality; // 0x2C4
	public int          m_nBodyLeathery; // 0x2C8
	public boolean      m_boHolySeize; // 0x2CC
	public int          m_dwHolySeizeTick; // 0x2D0
	public int          m_dwHolySeizeInterval; // 0x2D4
	public boolean      m_boCrazyMode; // 0x2D8
	public int          m_dwCrazyModeTick; // 0x2DC
	public int          m_dwCrazyModeInterval; // 0x2E0
	public boolean      m_boShowHP; // 0x2E4
	public int          nC2E6;        //          :int;      //0x2E6
	public int          m_dwShowHPTick; // 0x2E8  心灵启示检查时间(Dshort)
	public int          m_dwShowHPInterval; // 0x2EC  心灵启示有效时长(Dshort)
	public boolean      bo2F0; // 0x2F0
	public int          m_dwDupObjTick; // 0x2F4
	public Object       m_PEnvir; // 0x2F8
	public boolean      m_boGhost; // 0x2FC
	public int          m_dwGhostTick; // 0x300
	public boolean      m_boDeath; // 0x304
	public int          m_dwDeathTick; // 0x308
	public byte         m_btMonsterWeapon; // 0x30C 怪物所拿的武器
	public int          m_dwStruckTick; // 0x310
	public boolean      m_boWantRefMsg; // 0x314
	public boolean      m_boAddtoMapSuccess; // 0x315
	public boolean      m_bo316; // 0x316
	public boolean      m_boDealing; // 0x317
	public int          m_DealLastTick; // 0x318 交易最后操作时间
	public TBaseObject  m_DealCreat; // 0x31C
	public Object       m_MyGuild; // 0x320
	public int          m_nGuildRankNo; // 0x324
	public String       m_sGuildRankName; // 0x328
	public String       m_sScriptLable; // 0x32C
	public byte         m_btAttackSkillCount; // 0x330
	public byte         bt331;
	public byte         bt332;
	public byte         bt333;
	public byte         m_btAttackSkillPointCount; // 0x334
	public boolean      bo335; // 0x335
	public boolean      bo336; // 0x336
	public boolean      bo337; // 0x337
	public boolean      m_boMission; // 0x338
	public int          m_nMissionX; // 0x33C
	public int          m_nMissionY; // 0x340
	public boolean      m_boHideMode; // 0x344  隐身戒指(byte)
	public boolean      m_boStoneMode; // 0x345
	public boolean      m_boCoolEye; // 0x346  //是否可以看到隐身人物
	public boolean      m_boUserUnLockDurg; // 0x347  //是否用了神水
	public boolean      m_boTransparent; // 0x348  //魔法隐身了
	public boolean      m_boAdminMode; // 0x349  管理模式(byte)
	public boolean      m_boObMode; // 0x34A  隐身模式(byte)
	public boolean      m_boTeleport; // 0x34B  传送戒指(byte)
	public boolean      m_boParalysis; // 0x34C  麻痹戒指(byte)
	public boolean      m_boUnParalysis;
	public boolean      m_boRevival; // 0x34D  复活戒指(byte)
	public boolean      m_boUnRevival; // 防复活
	public boolean      bo34E;
	public boolean      bo34F;
	public int          m_dwRevivalTick; // 0x350  复活戒指使用间隔计数(Dshort)
	public boolean      m_boFlameRing; // 0x354  火焰戒指(byte)
	public boolean      m_boRecoveryRing; // 0x355  治愈戒指(byte)
	public boolean      m_boAngryRing; // 0x356  未知戒指(byte)
	public boolean      m_boMagicShield; // 0x357  护身戒指(byte)
	public boolean      m_boUnMagicShield; // 防护身
	public boolean      m_boMuscleRing; // 0x358  活力戒指(byte)
	public boolean      m_boFastTrain; // 0x359  技巧项链(byte)
	public boolean      m_boProbeNecklace; // 0x35A  探测项链(byte)
	public boolean      m_boGuildMove; // 行会传送
	public boolean      m_boSupermanItem;
	public boolean      m_bopirit; // 祈祷

	public boolean m_boNoDropItem;
	public boolean m_boNoDropUseItem;
	public boolean m_boExpItem;
	public boolean m_boPowerItem;

	public boolean m_rExpItem;
	public boolean m_rPowerItem;
	public int     m_dwPKDieLostExp; // PK 死亡掉经验，不够经验就掉等级
	public int     m_nPKDieLostLevel; // PK 死亡掉等级

	public boolean        m_boAbilSeeHealGauge; // 0x35B  //心灵启示
	public boolean        m_boAbilMagBubbleDefence; // 0x35C  //魔法盾
	public byte           m_btMagBubbleDefenceLevel; // 0x35D
	public int            m_dwSearchTime; // 0x360
	public int            m_dwSearchTick; // 0x364
	public int            m_dwRunTick; // 0x368
	public int            m_nRunTime; // 0x36C
	public int            m_nHealthTick; // 0x370    //特别指定为 此类型  此处用到 004C7CF8
	public int            m_nSpellTick; // 0x374
	public TBaseObject    m_TargetCret; // 0x378
	public int            m_dwTargetFocusTick; // 0x37C
	public TBaseObject    m_LastHiter; // 0x380  人物被对方杀害时对方指针(Dshort)
	public int            m_LastHiterTick; // 0x384
	public TBaseObject    m_ExpHitter; // 0x388
	public int            m_ExpHitterTick; // 0x38C
	public int            m_dwTeleportTick; // 0x390  传送戒指使用间隔(Dshort)
	public int            m_dwProbeTick; // 0x394  探测项链使用间隔(Dshort)
	public int            m_dwMapMoveTick; // 0x398
	public boolean        m_boPKFlag; // 0x39C  人物攻击变色标志(byte)
	public int            m_dwPKTick; // 0x3A0  人物攻击变色时间长度(Dshort)
	public int            m_nMoXieSuite; // 0x3A4  魔血一套(Dshort)
	public int            m_nHongMoSuite; // 0x3A8 虹魔一套(Dshort)
	public int            m_n3AC; // 0x3AC
	public Double         m_db3B0; // 0x3B0
	public int            m_dwPoisoningTick; // 0x3B8 中毒处理间隔时间(Dshort)
	public int            m_dwDecPkPointTick; // 0x3BC  减PK值时间(Dshort)
	public int            m_DecLightItemDrugTick; // 0x3C0
	public int            m_dwVerifyTick; // 0x3C4
	public int            m_dwCheckRoyaltyTick; // 0x3C8
	public int            m_dwDecHungerPointTick; // 0x3CC
	public int            m_dwHPMPTick; // 0x3D0
	public List           m_MsgList; // 0x3D4
	public List           m_VisibleHumanList; // 0x3D8
	public List           m_VisibleItems; // 0x3DC
	public List           m_VisibleEvents; // 0x3E0
	public int            m_SendRefMsgTick; // 0x3E4
	public boolean        m_boInFreePKArea; // 0x3E8  是否在开行会战(byte)
	public List           LIst_3EC; // 0x3EC
	public int            dwTick3F0; // 0x3F0
	public int            dwTick3F4; // 0x3F4
	public int            m_dwHitTick; // 0x3F8
	public int            m_dwWalkTick; // 0x3FC
	public int            m_dwSearchEnemyTick; // 0x400
	public boolean        m_boNameColorChanged; // 0x404
	public boolean        m_boIsVisibleActive; // 是否在可视范围内有人物,及宝宝
	public byte           m_nProcessRunCount;
	public List           m_VisibleActors; // 0x408
	public List           m_ItemList; // 0x40C  人物背包(Dshort)数量
	public List           m_DealItemList; // 0x410
	public int            m_nDealGolds; // 0x414  交易的金币数量(Dshort)
	public boolean        m_boDealOK; // 0x418  确认交易标志(byte
	public List           m_MagicList; // 0x41C  技能表
	public THumanUseItems m_UseItems; // 这个是身上装备的定义
	public List           m_SayMsgList;
	public List           m_StorageItemList; // 0x4F8
	public int            m_nWalkSpeed; // 0x4FC
	public int            m_nWalkStep; // 0x500
	public int            m_nWalkCount; // 0x504
	public int            m_dwWalkWait; // 0x508
	public int            m_dwWalkWaitTick; // 0x50C
	public boolean        m_boWalkWaitLocked; // 0x510
	public int            m_nNextHitTime; // 0x514
	public TUserMagic     m_MagicOneSshortSkill; // 0x518
	public TUserMagic     m_MagicPowerHitSkill; // 0x51C
	public TUserMagic     m_MagicErgumSkill; // 0x520 刺杀剑法
	public TUserMagic     m_MagicBanwolSkill; // 0x524 半月弯刀
	public TUserMagic     m_MagicFireSshortSkill; // 0x528
	public TUserMagic     m_MagicCrsSkill; // 0x528
	public TUserMagic     m_Magic41Skill; // 0x528
	public TUserMagic     m_Magic42Skill; // 0x528
	public TUserMagic     m_Magic43Skill; // 0x528
	public boolean        m_boPowerHit; // 0x52C
	public boolean        m_boUseThrusting; // 0x52D
	public boolean        m_boUseHalfMoon; // 0x52E
	public boolean        m_boFireHitSkill; // 0x52F
	public boolean        m_boCrsHitkill; // 0x52F
	public boolean        m_bo41kill; // 0x52F
	public boolean        m_bo42kill; // 0x52F
	public boolean        m_bo43kill; // 0x52F
	public int            m_dwLatestFireHitTick; // 0x530
	public int            m_dwDoMotaeboTick; // 0x534
	public boolean        m_boDenyRefStatus; // 是否刷新在地图上信息；
	public boolean        m_boAddToMaped; // 是否增加地图计数
	public boolean        m_boDelFormMaped; // 是否从地图中删除计数
	public boolean        m_boAutoChangeColor;
	public int            m_dwAutoChangeColorTick;
	public int            m_nAutoChangeIdx;

	public boolean m_boFixColor; // 固定颜色
	public int     m_nFixColorIdx;
	public int     m_nFixStatus;
	public boolean m_boFastParalysis; // 快速麻痹，受攻击后麻痹立即消失

	public Object m_DefMsg; // 0x550
	public int    m_nSocket; // 0x59C nSocket
	public int    m_nGSocketIdx; // 0x5A0 wGateIndex 人物连接到游戏网关SOCKET ID
	public int    m_nGateIdx; // 0x5A8 nGateIdx   人物所在网关号
	public int    m_nSoftVersionDate; // 0x5AC

	public int m_nCopyHumanLevel; // 复制人辈分等级
	public int m_dwStationTick; // 增加检测人物站立不动时间
	public int m_dwSayMyInfoTick;

	public class TNakedAbility {

		public short DC;
		public short MC;
		public short SC;
		public short AC;
		public short MAC;
		public short HP;
		public short MP;
		public short Hit;
		public short Speed;
		public short X2;
	}

	public static class TAbility {

		public short Level; //0x198  //0x34  0x00
		public int   AC; //0x19A  //0x36  0x02
		public int   MAC; //0x19C  //0x38  0x04
		public int   DC; //0x19E  //0x3A  0x06
		public int   MC; //0x1A0  //0x3C  0x08
		public int   SC; //0x1A2  //0x3E  0x0A
		public short HP; //0x1A4  //0x40  0x0C
		public short MP; //0x1A6  //0x42  0x0E
		public short MaxHP; //0x1A8  //0x44  0x10
		public short MaxMP; //0x1AA  //0x46  0x12
		public int   Exp; //0x1B0  //0x4C 0x18
		public int   MaxExp; //0x1B4  //0x50 0x1C
		public short Weight; //0x1B8   //0x54 0x20
		public short MaxWeight; //0x1BA   //0x56 0x22  背包
		public short WearWeight; //0x1BC   //0x58 0x24
		public short MaxWearWeight; //0x1BD   //0x59 0x25  负重
		public short HandWeight; //0x1BE   //0x5A 0x26
		public short MaxHandWeight; //0x1BF   //0x5B 0x27  腕力
	}

	public class TAddAbility {

		public short wHP;
		public short wMP;
		public short wHitPoint;
		public short wSpeedPoint;
		public int   wAC;
		public int   wMAC;
		public int   wDC;
		public int   wMC;
		public int   wSC;
		public byte  bt1DF; //神圣
		public byte  bt035;
		public short wAntiPoison;
		public short wPoisonRecover;
		public short wHealthRecover;
		public short wSpellRecover;
		public short wAntiMagic;
		public byte  btLuck;
		public byte  btUnLuck;
		public int   nHitSpeed;
		public byte  btWeaponStrong;
	}

	public class TQuestUnit {

		public final byte[] content = new byte[61];
	}

	public class TQuestFlag {

		public final byte[] content = new byte[128];
	}

	public class TStatusTime {

		public final byte[] content = new byte[12];
	}

	public class THumanUseItems {

		public final byte[] d = new byte[13];
	}

	public class TUserMagic {

		public TMagic MagicInfo;
		public short  wMagIdx;
		public byte   btLevel;
		public byte   btKey;
		public int    nTranPoint;
	}

	public class TMagic {

		public short wMagicId;
		public String[] sMagicName = new String[12];
		public byte  btEffectType;
		public byte  btEffect;
		public byte  bt11;
		public short wSpell;
		public short wPower;
		public byte[] TrainLevel = new byte[4];
		public short w02;
		public int[] MaxTrain = new int[4];
		public byte  btTrainLv;
		public byte  btJob;
		public short wMagicIdx;
		public int   dwDelayTime;
		public byte  btDefSpell;
		public byte  btDefPower;
		public short wMaxPower;
		public byte  btDefMaxPower;
		public String[] sDescr = new String[18];
	}

}
