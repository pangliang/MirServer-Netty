package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.types.Color;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

public class MessageEngine {


	public static ServerPacket createMessage(int inGameId, String msg) {
		return createMessage(inGameId, msg, Color.Black, Color.White);
	}

	public static ServerPacket createMessage(int inGameid, String msg, Color ftCorol, Color bgColor) {
		return new ServerPacket.SysMessage(inGameid, msg, ftCorol, bgColor);
	}

	/**
	 * 人物进入游戏时的第一个消息弹框内容
	 *
	 * @return
	 */
	public static String getNotice() {
		// TODO: 16/3/4 读取notice文件
		return "aklsdjfkladjsfkadsfjk";
	}
}
