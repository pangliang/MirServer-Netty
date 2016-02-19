package com.zhaoxiaodan.mirserver.network;

import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangwei on 16/2/19.
 */
public class ServerPackets {

	public static final class LoginSuccSelectServer extends Packet{
		public List<ServerInfo> serverInfoList;

		public LoginSuccSelectServer(){}

		public LoginSuccSelectServer(List<ServerInfo> serverInfoList)
		{
			super(Protocol.LoginSuccSelectServer);
			this.serverInfoList = serverInfoList;
			this.p3 = (short)serverInfoList.size();
		}
		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);
			for(ServerInfo info : serverInfoList)
			{
				String infoStr = info.name + "/"+info.id+"/";
				try{
					out.writeBytes(infoStr.getBytes());
				}catch (Exception e){

				}
			}
		}

		@Override
		public void readPacket(ByteBuf in) {
			super.readPacket(in);

			in = in.order(ByteOrder.LITTLE_ENDIAN);
			serverInfoList = new ArrayList<>();
			String name = in.toString();
			System.out.println(name);
		}
	}
}
