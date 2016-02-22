package com.zhaoxiaodan.mirserver.db.entities;

import javax.persistence.*;

@Entity
public class Character extends DAO{
	@Id
	@GeneratedValue
	public Long id;
	@ManyToOne
	@JoinColumn(name = "loginId")
	public User user;
	@Column(unique = true)
	public String name;
	public byte hair;
	public Job job;
	public Gender gender;
	public int level;
}
