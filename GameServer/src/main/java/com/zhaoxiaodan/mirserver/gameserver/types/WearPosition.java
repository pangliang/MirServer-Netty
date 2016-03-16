package com.zhaoxiaodan.mirserver.gameserver.types;

import java.util.HashMap;
import java.util.Map;

public enum WearPosition {
	Dress(0),
	Weapon(1),
	RightHand(2),
	NeckLace(3),
	Helmet(4),
	LeftBangle(5),
	RightBangle(6),
	LeftRing(7),
	RightRing(8),
	Bujuk(9),
	Belt(10),
	Boots(11),
	Charm(12);

	public final byte id;
	WearPosition(int i){
		this.id = (byte)i;
	}
	private static final Map<Byte,WearPosition> idMaps = new HashMap<>();
	static {
		for(WearPosition p : WearPosition.values())
			idMaps.put(p.id,p);
	}

	public static WearPosition get(int i){
		return idMaps.get((byte)i);
	}
}
