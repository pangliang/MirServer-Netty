package com.zhaoxiaodan.mirserver.db.types;

import java.util.HashMap;
import java.util.Map;

public enum Color {

	Black(0),
	Gray(230),
	Red(249),
	Green(250),
	Yellow(251),
	Blue(252),
	White(255),
	Default(Color.Black.id);
	public short id;

	Color(int id) {
		this.id = (short) id;
	}

	private static final Map<Short, Color> colors = new HashMap<>();

	static {
		for (Color color : Color.values()) {
			colors.put(color.id, color);
		}
	}

	public static Color get(short id) {
		if (colors.containsKey(id))
			return colors.get(id);
		else
			return Default;
	}
}
