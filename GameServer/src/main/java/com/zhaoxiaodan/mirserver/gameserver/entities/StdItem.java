package com.zhaoxiaodan.mirserver.gameserver.entities;

import com.zhaoxiaodan.mirserver.gameserver.types.ItemAttr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StdItem {

	@Id
	@GeneratedValue
	public int id;

	public ItemAttr attr;

	public String scriptName;
}
