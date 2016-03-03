package com.zhaoxiaodan.mirserver.db.objects;

import com.zhaoxiaodan.mirserver.db.types.MapPoint;
import groovy.lang.GroovyObject;

public class Npc {

	public String       name;
	public MapPoint     mapPoint;
	public String       scriptName;
	public GroovyObject scriptInstance;
}
