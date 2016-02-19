package com.zhaoxiaodan.mirserver.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangwei on 16/2/19.
 */
public class StringUtils {

	/**
	 * 把body 分段
	 *
	 * @return
	 */
	public List<String> splitBody(String content) {
		List<String> result = new ArrayList<String>();

		StringBuilder sb = new StringBuilder();
		for (byte b : content.getBytes()) {
			if (b < 0x30) {
				if (0 == sb.length()) // 开头就是 空
					continue;
				else {
					result.add(sb.toString());
					sb = new StringBuilder();
				}
			} else {
				sb.append((char) b);
			}
		}

		if (0 != sb.length())
			result.add(sb.toString());

		return result;
	}
}
