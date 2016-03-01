package com.zhaoxiaodan.mirserver.db.entities;

import javax.persistence.*;

@Entity
public class CharacterItem extends DAO{

	@Id
	@GeneratedValue
	public int       id;
	@ManyToOne
	@JoinColumn(name = "characterId")
	public Character character;

	@OneToOne
	@JoinColumn(name = "stdItemId")
	public StdItem stdItem;
}
