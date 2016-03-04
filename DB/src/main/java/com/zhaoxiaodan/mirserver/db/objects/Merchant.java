package com.zhaoxiaodan.mirserver.db.objects;

import groovy.lang.GroovyObject;

public class Merchant extends BaseObject{

	public String       scriptName;
	public GroovyObject scriptInstance;

	@Override
	public void onTick() {

	}
}
