package com.zhaoxiaodan.mirserver.gameserver.entities;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class User{

	@Id
	@GeneratedValue
	public int id;

	@Column(unique = true)
	public String loginId;
	public String password;
	public String username;
	public Date lastLoginTime;
	public byte certification;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	public List<Player> players;
}
