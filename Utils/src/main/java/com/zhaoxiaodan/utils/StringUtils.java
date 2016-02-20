package com.zhaoxiaodan.utils;

import java.lang.reflect.Field;

/**
 * Created by liangwei on 16/2/20.
 */
public class StringUtils {
	public static String toString(Object object) {

		final StringBuffer sb = new StringBuffer();

		sb.append(object.getClass().getSimpleName()).append("{");
		for (Field field : object.getClass().getDeclaredFields()) {
			try {
				sb.append(field.getName()).append("=").append(field.get(object)).append(",");
			} catch (Exception e) {
			}
		}
		sb.append('}');
		return sb.toString();
	}
}
