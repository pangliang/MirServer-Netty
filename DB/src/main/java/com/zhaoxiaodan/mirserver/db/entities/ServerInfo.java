package com.zhaoxiaodan.mirserver.db.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by liangwei on 16/2/19.
 */
@Entity
public class ServerInfo {
	@Id
	@GeneratedValue
	public Long id;
	public String name;
}
