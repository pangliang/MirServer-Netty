package com.zhaoxiaodan.mirserver.db.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class User extends DAO{

	@Id
	@GeneratedValue
	public Long id;

	@Column(unique = true)
	public String userId;
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

}
