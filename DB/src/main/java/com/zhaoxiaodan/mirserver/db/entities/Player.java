package com.zhaoxiaodan.mirserver.db.entities;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;

/**
 * Created by liangwei on 16/2/18.
 */

@NamedQueries(
		@NamedQuery(
				name = "get_player_by_username",
				query = "select p from Player p where username = :username"
		)
)

@Entity
public class Player {
	@Id
	@GeneratedValue
	private Long id;
	private String username;
	private String password;
	private String nickName;
	private String question1;
	private String answer1;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
