package com.zhaoxiaodan.mirserver.db.entities;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class User extends DAO{

	@Id
	@GeneratedValue
	public long id;

	@Column(unique = true)
	public String loginId;
	public String password;
	public String username;
	public Date lastLoginTime;
	public byte certification;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@MapKey(name = "id")
	public List<Character> characters;


}
