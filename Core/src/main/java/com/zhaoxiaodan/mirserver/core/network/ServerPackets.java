package com.zhaoxiaodan.mirserver.core.network;

import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import io.netty.buffer.ByteBuf;

import java.util.List;

/**
 * Created by liangwei on 16/2/19.
 */
public class ServerPackets {

	public static final class LoginSuccSelectServer extends Packet{
		public List<ServerInfo> serverInfoList;
		public LoginSuccSelectServer(List<ServerInfo> serverInfoList)
		{
			super(Protocol.SM_PASSOK_SELECTSERVER);
			this.serverInfoList = serverInfoList;
			this.p1 = (short)serverInfoList.size();
		}

		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			for(ServerInfo info : serverInfoList)
			{
				String infoStr = info.id+","+info.name;
				out.writeBytes(infoStr.getBytes());
				out.writeByte(',');
			}
		}
	}
}
