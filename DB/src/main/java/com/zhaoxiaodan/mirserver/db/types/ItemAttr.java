package com.zhaoxiaodan.mirserver.db.types;

import com.zhaoxiaodan.mirserver.network.packets.Parcelable;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import io.netty.buffer.ByteBuf;

import javax.persistence.Embeddable;

@Embeddable
public class ItemAttr implements Parcelable,Cloneable {

	public String name;
	public byte   stdMode;
	public short  shape;
	public byte   weight;
	public byte   anicount;
	public byte   source;
	public byte   reserved;
	public byte   needIdentify;
	public short  looks;
	/**
	 * 耐久度, 客户端显示 int(duraMax/1000) 的值
	 */
	public int    duraMax;
	public short  reserved1;
	public short  AC;
	public short  AC2;
	public short  MAC;
	public short  MAC2;
	public short  DC;
	public short  DC2;
	public short  MC;
	public short  MC2;
	public short  SC;
	public short  SC2;
	public int    need;
	public int    needLevel;
	public int    price;
	public int    stock;
	public String description;

	@Override
	public void readPacket(ByteBuf in) throws Parcelable.WrongFormatException {

	}

	public ItemAttr clone(){
		try {
			return (ItemAttr) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}

	public void writePacket(ByteBuf out) {
		oldVersion(out);
	}

	public void newVersion(ByteBuf out) {
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

		out.writeInt(NumUtil.makeLong(AC, AC2));
		out.writeInt(NumUtil.makeLong(MAC, MAC2));
		out.writeInt(NumUtil.makeLong(DC, DC2));
		out.writeInt(NumUtil.makeLong(MC, MC2));
		out.writeInt(NumUtil.makeLong(SC, SC2));

		out.writeByte(need);
		out.writeByte(needLevel);
		out.writeShort(0);
		out.writeInt(price);
		out.writeInt(stock);
	}

	private void oldVersion(ByteBuf out) {
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

		out.writeShort(NumUtil.makeWord((byte) AC, (byte) AC2));
		out.writeShort(NumUtil.makeWord((byte) MAC, (byte) MAC2));
		out.writeShort(NumUtil.makeWord((byte) DC, (byte) DC2));
		out.writeShort(NumUtil.makeWord((byte) MC, (byte) MC2));
		out.writeShort(NumUtil.makeWord((byte) SC, (byte) SC2));
		out.writeByte(need);
		out.writeByte(needLevel);
		out.writeShort(0);
		out.writeInt(price);
	}
}
