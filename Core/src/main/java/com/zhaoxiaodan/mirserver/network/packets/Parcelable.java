package com.zhaoxiaodan.mirserver.network.packets;

import io.netty.buffer.ByteBuf;

public interface Parcelable {

	public void readPacket(ByteBuf in) throws WrongFormatException;

	public void writePacket(ByteBuf out);

	public class WrongFormatException extends Exception {

		public WrongFormatException() {

		}

		public WrongFormatException(String msg) {
			super(msg);
		}
	}
}
