package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.types.Job;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StdMagic {

	@Id
	@GeneratedValue
	public int id;

	public int    magicId;
	public String name;
	public short  effectType;
	public short  effect;
	public short  spell;
	public short  power;
	public short  maxPower;
	public short  upSpell;
	public short  upPower;
	public short  upMaxPower;
	public Job    job;
	public int    delay;
	public String scriptName;
}
