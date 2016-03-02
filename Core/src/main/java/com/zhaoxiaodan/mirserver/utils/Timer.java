package com.zhaoxiaodan.mirserver.utils;

public class Timer {
	private static long startTime = 0;
	public static void init(){
		startTime = System.currentTimeMillis();
	}

	public static long getTickCount(){
		return System.currentTimeMillis() - startTime;
	}
}
