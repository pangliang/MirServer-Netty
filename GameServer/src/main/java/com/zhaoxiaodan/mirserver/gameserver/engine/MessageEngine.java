package com.zhaoxiaodan.mirserver.gameserver.engine;

import com.zhaoxiaodan.mirserver.db.types.Color;
import com.zhaoxiaodan.mirserver.network.packets.ServerPacket;

import java.io.FileReader;

public class MessageEngine {

	private static final String NOTICE_FILE = "Envir/Notice/Notice.cfg";
	private static       String notice      = "";

	public static ServerPacket createMessage(int inGameId, String msg) {
		return createMessage(inGameId, msg, Color.Black, Color.White);
	}

	public static ServerPacket createMessage(int inGameid, String msg, Color ftCorol, Color bgColor) {
		return new ServerPacket.SysMessage(inGameid, msg, ftCorol, bgColor);
	}

	public static synchronized void reload() throws Exception {
		reloadNotice();
	}

	public static synchronized void reloadNotice() throws Exception {
		FileReader reader = null;
		try {
			StringBuffer sb = new StringBuffer();
			reader = new FileReader(NOTICE_FILE);
			char[] buf = new char[1024];
			int    len = 0;
			while ((len = reader.read(buf)) > 0) {
				sb.append(buf, 0, len);
			}

			notice = sb.toString();
			notice = notice.replaceAll("\r\n", "\\\\").replaceAll("\n", "\\\\");
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public static String getNotice() {
		return notice;
	}
}
