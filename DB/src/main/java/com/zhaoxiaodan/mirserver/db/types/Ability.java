package com.zhaoxiaodan.mirserver.db.types;

import com.zhaoxiaodan.mirserver.network.packets.Parcelable;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import io.netty.buffer.ByteBuf;

import javax.persistence.Embeddable;

@Embeddable
public class Ability implements Parcelable, Cloneable {
	
	public short Level;
	public short AC;
	public short AC2;
	public short MAC;
	public short MAC2;
	public short DC;
	public short DC2;
	public short MC;
	public short MC2;
	public short SC;
	public short SC2;
	public int   HP;
	public int   MP;
	public int   MaxHP;
	public int   MaxMP;
	public int   Exp;
	public int   MaxExp;
	public short Weight;
	public short MaxWeight;
	public int   WearWeight;
	public int   MaxWearWeight;
	public int   HandWeight;
	public int   MaxHandWeight;
	
	@Override
	public void readPacket(ByteBuf in) throws WrongFormatException {
		
	}
	
	@Override
	public void writePacket(ByteBuf out) {
		oldVersion(out);
	}
	
	private void oldVersion(ByteBuf out) {
		out.writeShort(Level);
		out.writeInt(NumUtil.makeLong(AC, AC2));
		out.writeInt(NumUtil.makeLong(MAC, MAC2));
		out.writeInt(NumUtil.makeLong(DC, DC2));
		out.writeInt(NumUtil.makeLong(MC, MC2));
		out.writeInt(NumUtil.makeLong(SC, SC2));
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
	
	public Ability clone() {
		try {
			return (Ability) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
