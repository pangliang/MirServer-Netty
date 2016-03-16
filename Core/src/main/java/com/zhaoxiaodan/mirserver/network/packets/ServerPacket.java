package com.zhaoxiaodan.mirserver.network.packets;

import com.zhaoxiaodan.mirserver.network.Protocol;

public class ServerPacket extends Packet {

	public ServerPacket() {
		super();
	}

	public ServerPacket(int recog, Protocol protocol, short p1, short p2, short p3) {
		super(recog, protocol, p1, p2, p3);
	}

	public ServerPacket(int recog, Protocol protocol) {
		this(recog, protocol, (short) 0, (short) 0, (short) 0);
	}

	public ServerPacket(Protocol protocol) {
		super(protocol);
	}
}
