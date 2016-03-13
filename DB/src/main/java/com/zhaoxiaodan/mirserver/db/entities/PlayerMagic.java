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
	@JoinColumn(name = "PLAYERID")
	public Player player;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	public StdMagic stdMagic;

	public short key;
	public byte level;
	public int  exp;

	@Override
	public void readPacket(ByteBuf in) throws WrongFormatException {

	}

	@Override
	public void writePacket(ByteBuf out) {
		out.writeShort(key);
		out.writeByte(level + 2);
		out.writeByte(0);
		out.writeInt(exp + 8);
		stdMagic.writePacket(out);
	}
}
