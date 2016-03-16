package com.zhaoxiaodan.mirserver.network.packets;

import com.zhaoxiaodan.mirserver.network.Protocol;
import io.netty.buffer.ByteBuf;

public class ClientPacket extends Packet {

	public byte cmdIndex;  // #号后面紧跟的序号, 响应包的序号要跟请求一直

	public ClientPacket() {}

	public ClientPacket(Protocol pid, byte cmdIndex) {
		super(0, pid, (short) 0, (short) 0, (short) 0);
		this.cmdIndex = cmdIndex;
	}

	@Override
	public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {
		cmdIndex = in.readByte();
		cmdIndex = (byte) (cmdIndex - '0');
		super.readPacket(in);
	}

	@Override
	public void writePacket(ByteBuf out) {
		out.writeByte(cmdIndex + '0');
		super.writePacket(out);
	}
}
