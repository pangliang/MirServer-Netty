package com.zhaoxiaodan.mirserver.db.types;

import com.zhaoxiaodan.mirserver.network.packets.Parcelable;
import com.zhaoxiaodan.mirserver.utils.NumUtil;
import io.netty.buffer.ByteBuf;

import javax.persistence.Embeddable;

@Embeddable
public class Ability implements Parcelable, Cloneable {
	

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
	
	@Override
	public void readPacket(ByteBuf in) throws WrongFormatException {
		
	}
	
	@Override
	public void writePacket(ByteBuf out) {
		oldVersion(out);
	}
	
	private void oldVersion(ByteBuf out) {
		out.writeInt(NumUtil.makeLong(AC, AC2));
		out.writeInt(NumUtil.makeLong(MAC, MAC2));
		out.writeInt(NumUtil.makeLong(DC, DC2));
		out.writeInt(NumUtil.makeLong(MC, MC2));
		out.writeInt(NumUtil.makeLong(SC, SC2));
	}
	
	public Ability clone() {
		try {
			return (Ability) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
