package com.zhaoxiaodan.mirserver.db.types;

import com.zhaoxiaodan.mirserver.network.packets.Parcelable;
import io.netty.buffer.ByteBuf;

import javax.persistence.Embeddable;

@Embeddable
public class Ability implements Parcelable, Cloneable{
	public short Level; //0x198  //0x34  0x00
	public int   AC; //0x19A  //0x36  0x02
	public int   MAC; //0x19C  //0x38  0x04
	public int   DC; //0x19E  //0x3A  0x06
	public int   MC; //0x1A0  //0x3C  0x08
	public int   SC; //0x1A2  //0x3E  0x0A
	public int HP; //0x1A4  //0x40  0x0C
	public int MP; //0x1A6  //0x42  0x0E
	public int MaxHP; //0x1A8  //0x44  0x10
	public int MaxMP; //0x1AA  //0x46  0x12
	public int   Exp; //0x1B0  //0x4C 0x18
	public int   MaxExp; //0x1B4  //0x50 0x1C
	public short Weight; //0x1B8   //0x54 0x20
	public short MaxWeight; //0x1BA   //0x56 0x22  背包
	public int WearWeight; //0x1BC   //0x58 0x24
	public int MaxWearWeight; //0x1BD   //0x59 0x25  负重
	public int HandWeight; //0x1BE   //0x5A 0x26
	public int MaxHandWeight; //0x1BF   //0x5B 0x27  腕力

	@Override
	public void readPacket(ByteBuf in) throws WrongFormatException {

	}

	@Override
	public void writePacket(ByteBuf out) {
			oldVersion(out);
	}
	
	private void oldVersion(ByteBuf out){
		out.writeShort(Level);
		out.writeInt(AC);
		out.writeInt(MAC);
		out.writeInt(DC);
		out.writeInt(MC);
		out.writeInt(SC);
		out.writeShort(HP);
		out.writeShort(MP);
		out.writeShort(MaxHP);
		out.writeShort(MaxMP);
		out.writeInt(Exp);
		out.writeInt(MaxExp);
		out.writeShort(Weight);
		out.writeShort(MaxWeight);
		out.writeShort(WearWeight);
		out.writeShort(MaxWearWeight);
		out.writeShort(HandWeight);
		out.writeShort(MaxHandWeight);
	}

	public Ability clone(){
		try {
			return (Ability) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
