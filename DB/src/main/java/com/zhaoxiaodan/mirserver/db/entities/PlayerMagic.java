package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.network.packets.Parcelable;
import io.netty.buffer.ByteBuf;

import javax.persistence.*;

@Entity
public class PlayerMagic implements Parcelable {

	@Id
	@GeneratedValue
	public int    id;
	@ManyToOne
	@JoinColumn(name = "playerId")
	public Player player;

	@OneToOne
	public StdMagic stdMagic;

	public char key;
	public byte level;
	public int  exp;

	@Override
	public void readPacket(ByteBuf in) throws WrongFormatException {

	}

	@Override
	public void writePacket(ByteBuf out) {
		out.writeChar(key);
		out.writeByte(level + 10);
		out.writeByte(0);
		out.writeInt(exp + 8);
		stdMagic.writePacket(out);
	}
}
