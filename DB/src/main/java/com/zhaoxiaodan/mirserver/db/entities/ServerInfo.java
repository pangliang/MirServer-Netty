package com.zhaoxiaodan.mirserver.db.entities;

import javax.persistence.*;

@Entity
public class ServerInfo extends DAO{
	@Id
	@GeneratedValue
	public Long id;
	@OrderColumn
	@Column(unique = true)
	public String name;
	public String ip;
	public Integer port;
}
