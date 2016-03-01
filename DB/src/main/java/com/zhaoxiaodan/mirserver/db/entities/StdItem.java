package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.network.packets.Packet;
import com.zhaoxiaodan.mirserver.network.packets.Parcelable;
import io.netty.buffer.ByteBuf;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StdItem extends DAO implements Parcelable{

	@Id
	@GeneratedValue
	public int    id;
	public String name;
	public byte   stdMode;
	public short   shape;
	public byte   weight;
	public byte   anicount;
	public byte   source;
	public byte   reserved;
	public byte   needIdentify;
	public short  looks;
	public int  duraMax;
	public short  reserved1;
	public short  ac;
	public short  ac2;
	public short  mac;
	public short  mac2;
	public short  dc;
	public short  dc2;
	public short  mc;
	public short  mc2;
	public short  sc;
	public short  sc2;
	public int    need;
	public int    needLevel;
	public int    price;
	public int    stock;
	public String description;

	@Override
	public void readPacket(ByteBuf in) throws WrongFormatException {

	}

	public void writePacket(ByteBuf out){
		byte[] nameBytes = name.getBytes();
		out.writeByte(nameBytes.length);
		out.writeBytes(nameBytes);

		out.writeBytes(new byte[14 - nameBytes.length]);
		
		out.writeByte(stdMode);
		out.writeByte(shape);
		out.writeByte(weight);
		out.writeByte(anicount);
		out.writeByte(source);
		out.writeByte(reserved);
		out.writeByte(needIdentify);
		out.writeShort(looks);
		out.writeShort(duraMax);

		out.writeShort(Packet.makeWord((byte)ac,(byte)ac2));
		out.writeShort(Packet.makeWord((byte)mac,(byte)mac2));
		out.writeShort(Packet.makeWord((byte)dc,(byte)dc2));
		out.writeShort(Packet.makeWord((byte)mc,(byte)mc2));
		out.writeShort(Packet.makeWord((byte)sc,(byte)sc2));
		out.writeByte(need);
		out.writeByte(needLevel);
		out.writeShort(0);
		out.writeInt(price);
	}
}
