package com.zhaoxiaodan.mirserver.network;

import io.netty.buffer.ByteBuf;

/**
 * 请求类, 比默认封包 在 '#'号后多了一个 index 序号
 */
public class IndexPacket extends Packet {

	public byte cmdIndex;  // #号后面紧跟的序号, 响应包的序号要跟请求一直

	public IndexPacket(){}

	public IndexPacket(Protocol pid, byte cmdIndex) {
		super(0, pid, (short) 0, (short) 0, (short) 0);
		this.cmdIndex = cmdIndex;
	}

	@Override
	public void readPacket(ByteBuf in) throws WrongFormatException{
		cmdIndex = in.readByte();
		cmdIndex = (byte) (cmdIndex - '0');
		super.readPacket(in);
	}

	@Override
	public void writePacket(ByteBuf out) {
		out.writeByte((byte)(cmdIndex + '0'));
		super.writePacket(out);
	}
}
