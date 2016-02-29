package com.zhaoxiaodan.mirserver.db.objects;

import javax.persistence.Embeddable;

@Embeddable
public class MapPoint {
	public String mapName;
	public short x;
	public short y;
}
