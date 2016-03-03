package com.zhaoxiaodan.mirserver.db.types;

import com.zhaoxiaodan.mirserver.network.packets.Parcelable;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import io.netty.buffer.ByteBuf;

import javax.persistence.Embeddable;

@Embeddable
public class ItemAttr implements Parcelable{
	public String name;
	public byte   stdMode;
	public short   shape;
	public byte   weight;
	public byte   anicount;
	public byte   source;
	public byte   reserved;
	public byte   needIdentify;
	public short  looks;
	/**
	 * 耐久度, 客户端显示 int(duraMax/1000) 的值
	 */
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
	public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {

	}

	public void writePacket(ByteBuf out){
		oldVersion(out);
	}

	private void newVersion(ByteBuf out){
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

		out.writeShort(reserved1);

		out.writeInt(NumUtil.makeLong(ac,ac2));
		out.writeInt(NumUtil.makeLong(mac,mac2));
		out.writeInt(NumUtil.makeLong(dc,dc2));
		out.writeInt(NumUtil.makeLong(mc,mc2));
		out.writeInt(NumUtil.makeLong(sc,sc2));

		out.writeByte(need);
		out.writeByte(needLevel);
		out.writeShort(0);
		out.writeInt(price);
		out.writeInt(stock);
	}

	private void oldVersion(ByteBuf out){
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

		out.writeShort(NumUtil.makeWord((byte)ac,(byte)ac2));
		out.writeShort(NumUtil.makeWord((byte)mac,(byte)mac2));
		out.writeShort(NumUtil.makeWord((byte)dc,(byte)dc2));
		out.writeShort(NumUtil.makeWord((byte)mc,(byte)mc2));
		out.writeShort(NumUtil.makeWord((byte)sc,(byte)sc2));
		out.writeByte(need);
		out.writeByte(needLevel);
		out.writeShort(0);
		out.writeInt(price);
	}
}
