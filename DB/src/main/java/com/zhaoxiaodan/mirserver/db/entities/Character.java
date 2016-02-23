package com.zhaoxiaodan.mirserver.db.entities;

import javax.persistence.*;

@Entity
public class Character extends DAO{
	@Id
	@GeneratedValue
	public long id;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	public User user;
	@Column(unique = true)
	public String name;
	public byte hair;
	public Job job;
	public Gender gender;
	public int level;
}
