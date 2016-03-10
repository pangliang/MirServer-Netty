package com.zhaoxiaodan.mirserver.db.entities;

import com.zhaoxiaodan.mirserver.db.types.ItemAttr;
import groovy.lang.GroovyObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class StdItem {

	@Id
	@GeneratedValue
	public int id;

	public ItemAttr attr;

	public String scriptName;

	@Transient
	public GroovyObject scriptInstance;
}
