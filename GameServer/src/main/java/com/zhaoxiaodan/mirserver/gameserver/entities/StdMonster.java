package com.zhaoxiaodan.mirserver.gameserver.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StdMonster {
	@Id
	@GeneratedValue
	public int id;
	public String name;
	public byte race;
	public byte raceImg;
	public short appr;
	public short level;
	public boolean undead;
	public byte coolEye;
	public int exp;
	public int hp;
	public int mp;
	public int ac;
	public int mac;
	public int dc;
	public int dcMax;
	public int mc;
	public int sc;
	public int speed;
	public int hit;
	public int walkSpeed;
	public int walkStep;
	public int walkWait;
	public int attackSpeed;
	public String scriptName;
}
