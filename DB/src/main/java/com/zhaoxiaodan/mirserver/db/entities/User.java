package com.zhaoxiaodan.mirserver.db.entities;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class User extends DAO{

	@Id
	@GeneratedValue
	public Long id;

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

	@OneToMany
	@JoinColumn(name = "loginId")
	public List<Character> characters;

}
