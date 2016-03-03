package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.objects.ItemAttr;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class StdItem {

	@Id
	@GeneratedValue
	public int id;

	public ItemAttr attr;
}
