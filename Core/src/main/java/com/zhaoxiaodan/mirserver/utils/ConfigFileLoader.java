package com.zhaoxiaodan.mirserver.utils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ConfigFileLoader {

	/**
	 * 读取配置文件, 忽略掉空行和注释行
	 * @param fileName 配置文件地址
	 * @param limitParts 每一行至少有多少列
	 * @return
	 */
	public static List<StringTokenizer> load(String fileName,int limitParts) throws Exception{
		List<StringTokenizer>   lines      = new ArrayList<>();
		BufferedReader reader     = new BufferedReader(new FileReader(fileName));
		String         line;
		int lineNumber = 0;
		while ((line = reader.readLine()) != null) {
			lineNumber++;
			line = line.trim();
			if (line.length() == 0 || '#' == line.charAt(0))       //注释行, 空行
				continue;

			StringTokenizer tokenizer = new StringTokenizer(line);
			if (tokenizer.countTokens() < limitParts)
				throw new Exception("配置文件 "+fileName + " 格式错误, 行号: " + lineNumber);
			lines.add(tokenizer);
		}
		return lines;
 	}
}
