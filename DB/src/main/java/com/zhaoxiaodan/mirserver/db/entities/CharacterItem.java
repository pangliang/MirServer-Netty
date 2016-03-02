package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.network.packets.Parcelable;
import io.netty.buffer.ByteBuf;

import javax.persistence.*;

@Entity
public class CharacterItem implements Parcelable{

	@Id
	@GeneratedValue
	public int       id;
	@ManyToOne
	@JoinColumn(name = "characterId")
	public Character character;

	@OneToOne
	@JoinColumn(name = "stdItemId")
	public StdItem stdItem;

	public short dura;
	public short duraMax;

	@Override
	public void readPacket(ByteBuf in) throws WrongFormatException {

	}

	@Override
	public void writePacket(ByteBuf out) {
		stdItem.writePacket(out);
		out.writeInt(id);
		out.writeShort(dura);
		out.writeShort(duraMax);
	}
}
