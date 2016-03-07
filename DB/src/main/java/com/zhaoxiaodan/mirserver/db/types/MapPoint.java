package com.zhaoxiaodan.mirserver.db.types;

import javax.persistence.Embeddable;

@Embeddable
public class MapPoint {
	public String mapId = "0";
	public short x;
	public short y;
}
