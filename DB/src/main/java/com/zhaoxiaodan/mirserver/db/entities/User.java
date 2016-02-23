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
	public String question1;
	public String answer1;
	public String question2;
	public String answer2;
	public Date   birthday;
	public String email;
	public String phone;
	public String mobilePhone;
	public byte certification;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	@MapKey(name = "id")
	public List<Character> characters;


}
