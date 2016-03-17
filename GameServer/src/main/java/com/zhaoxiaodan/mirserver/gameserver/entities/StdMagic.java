package com.zhaoxiaodan.mirserver.gameserver.entities;

import com.zhaoxiaodan.mirserver.gameserver.types.Job;
import com.zhaoxiaodan.mirserver.network.packets.Parcelable;
import io.netty.buffer.ByteBuf;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StdMagic implements Parcelable {

    public static final int NAME_BYTE_SIZE = 12;

    @Id
    @GeneratedValue
    public int id;

    public int magicId;
    public String name;
    public short effectType;
    public short effect;
    public short spell;
    public short power;
    public short maxPower;
    public short upSpell;
    public short upPower;
    public short upMaxPower;
    public Job job;
    public int delay;
    public String scriptName;

    @Override
    public void readPacket(ByteBuf in) throws WrongFormatException {

    }

    @Override
    public void writePacket(ByteBuf out) {
        out.writeShort(id);
        byte[] nameBytes = name.getBytes();
        out.writeByte(nameBytes.length);
        out.writeBytes(nameBytes, 0, nameBytes.length > NAME_BYTE_SIZE ? NAME_BYTE_SIZE : nameBytes.length);
        out.writeBytes(new byte[NAME_BYTE_SIZE - nameBytes.length]);

        out.writeByte(effectType);
        out.writeByte(effect);
        out.writeByte(0);
        out.writeShort(spell);
        out.writeShort(power);
        out.writeBytes(new byte[]{(byte) 11, (byte) 12, (byte) 13, (byte) 14});
        out.writeShort(0);

        // 各技能等级最高修炼点
        out.writeInt(10000);
        out.writeInt(20000);
        out.writeInt(30000);
        out.writeInt(40000);

        out.writeByte(2);

        out.writeByte(job.ordinal());
        out.writeShort(id);
        out.writeInt(delay);

        out.writeByte(upSpell);
        out.writeByte(upPower);
        out.writeShort(maxPower);
        out.writeByte(upMaxPower);

        nameBytes = name.getBytes();
        out.writeByte(nameBytes.length);
        out.writeBytes(nameBytes, 0, nameBytes.length > 18 ? 18 : nameBytes.length);
        out.writeBytes(new byte[18 - nameBytes.length]);
    }
}
