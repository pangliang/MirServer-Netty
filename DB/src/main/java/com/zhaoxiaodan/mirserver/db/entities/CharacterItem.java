package com.zhaoxiaodan.mirserver.db.entities;

import javax.persistence.*;

@Entity
public class CharacterItem {

	@Id
	@GeneratedValue
	public int       id;
	@ManyToOne
	@JoinColumn(name = "characterId")
	public Character character;

	public String name;
	public short stdMode;
	public short shape;
	public short weight;
	public short anicount;
	public short source;
	public short reserved;
	public int looks;
	public int duraMax;
	public short ac;
	public short ac2;
	public short mac;
	public short mac2;
	public short dc;
	public short dc2;
	public short mc;
	public short mc2;
	public short sc;
	public short sc2;
	public short need;
	public short needLevel;
	public int  price;
	public int stock;
}
