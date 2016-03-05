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
	Default(Color.Black.c);
	public short c;

	Color(int c) {
		this.c = (short)c;
	}

	private static final Map<Short, Color> colors = new HashMap<>();

	static {
		for (Color color : Color.values()) {
			colors.put(color.c, color);
		}
	}

	public static Color get(short c) {
		if (colors.containsKey(c))
			return colors.get(c);
		else
			return Default;
	}
}
