package com.zhaoxiaodan.mirserver.network;

import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
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

			serverInfoList = new ArrayList<>();
			String content = in.toString(Charset.defaultCharset());

			String[] parts = content.split("/");
			for(int i=0;i+1<parts.length;i+=2)
			{
				ServerInfo info = new ServerInfo();
				info.name = parts[i];
				info.id = Long.parseLong(parts[i+1]);
				serverInfoList.add(info);
			}
		}
	}


	public static final class SelectServerOk extends Packet{
		public String selectServerIp;
		public int selectserverPort;

		public SelectServerOk(){}

		public SelectServerOk(String selectServerIp,int selectserverPort)
		{
			super(Protocol.SelectServerOk);
			this.selectServerIp = selectServerIp;
			this.selectserverPort = selectserverPort;
		}
		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			out.writeBytes(selectServerIp.getBytes());
			out.writeByte('/');
			out.writeBytes(Integer.toString(selectserverPort).getBytes());
			out.writeByte('/');
		}

		@Override
		public void readPacket(ByteBuf in) {
			super.readPacket(in);

			String content = in.toString(Charset.defaultCharset()).trim();

			String[] parts = content.split("/");
			for(int i=0;i+1<parts.length;i+=2)
			{
				this.selectServerIp = parts[i];
				this.selectserverPort = Integer.parseInt(parts[i+1]);
			}
		}
	}
}
