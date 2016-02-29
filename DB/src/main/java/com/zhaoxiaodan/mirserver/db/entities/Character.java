package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.objects.Gender;
import com.zhaoxiaodan.mirserver.objects.Job;

import javax.persistence.*;

@Entity
public class Character extends DAO{
	@Id
	@GeneratedValue
	public long   id;
	@ManyToOne
	@JoinColumn(name = "userId")
	public User   user;
	@Column(unique = true)
	public String name;
	public byte   hair;
	public Job    job;
	public Gender gender;
	public int    level;
}
