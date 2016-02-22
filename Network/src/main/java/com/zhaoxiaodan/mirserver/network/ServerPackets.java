package com.zhaoxiaodan.mirserver.network;

import com.zhaoxiaodan.mirserver.db.entities.Character;
import com.zhaoxiaodan.mirserver.db.entities.ServerInfo;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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
		public void readPacket(ByteBuf in) throws WrongFormatException{
			super.readPacket(in);

			serverInfoList = new ArrayList<>();
			String content = in.toString(Charset.defaultCharset());

			String[] parts = content.split("/");
			if(parts.length <2)
				throw new WrongFormatException();
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
		public short certification;

		public SelectServerOk(){}

		public SelectServerOk(String selectServerIp,int selectserverPort,byte certification)
		{
			super(Protocol.SelectServerOk);
			this.selectServerIp = selectServerIp;
			this.selectserverPort = selectserverPort;
			this.certification = certification;
		}
		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			out.writeBytes(selectServerIp.getBytes());
			out.writeByte('/');
			out.writeBytes(Integer.toString(selectserverPort).getBytes());
			out.writeByte('/');
			out.writeBytes(Short.toString(certification).getBytes());
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException{
			super.readPacket(in);

			String content = in.toString(Charset.defaultCharset()).trim();

			String[] parts = content.split("/");
			if(parts.length <3)
				throw new WrongFormatException();
			this.selectServerIp = parts[0];
			this.selectserverPort = Integer.parseInt(parts[1]);
			this.certification = Short.parseShort(parts[2]);
		}
	}

	public static final class CharacterList extends Packet{
		public List<Character> characterList;

		public CharacterList(){}

		public CharacterList(List<Character> characterList)
		{
			super(Protocol.CharacterList);
			this.characterList = characterList;
		}
		@Override
		public void writePacket(ByteBuf out) {
			super.writePacket(out);

			for(Character cha : this.characterList){
				out.writeBytes(cha.name.getBytes());
				out.writeByte('/');
				out.writeBytes(Integer.toString(cha.job.ordinal()).getBytes());
				out.writeByte('/');
				out.writeBytes(Byte.toString(cha.hair).getBytes());
				out.writeByte('/');
				out.writeBytes(Integer.toString(cha.gender.ordinal()).getBytes());
				out.writeByte('/');
			}
		}

		@Override
		public void readPacket(ByteBuf in) throws WrongFormatException{
			super.readPacket(in);

			String content = in.toString(Charset.defaultCharset()).trim();

			String[] parts = content.split("/");
			if(parts.length <3)
				throw new WrongFormatException();
		}
	}
}
