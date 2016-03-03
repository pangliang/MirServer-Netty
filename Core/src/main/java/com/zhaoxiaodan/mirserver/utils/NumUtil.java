package com.zhaoxiaodan.mirserver.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class NumUtil {
	private static long startTime = System.currentTimeMillis();

	private static AtomicInteger atomicId = new AtomicInteger(14000000);

	public static long getTickCount(){
		return System.currentTimeMillis() - startTime;
	}

	public static int newAtomicId(){
		return atomicId.incrementAndGet();
	}

	public static short makeWord(byte low, byte high) {
		return (short) ((high << 8) | low);
	}

	public static int makeLong(short low, short high) {
		return ((high << 16) | low);
	}

	public static int makeLong(int low, int high) {
		return (((short) high << 16) | (short) low);
	}

	public static short getLowWord(int number) {
		return (short) (number);
	}

	public static short getHighWord(int number) {
		return (short) (number >> 16);
	}

	public static byte getLowByte(short number) {
		return (byte) (number);
	}

	public static byte getHighByte(short number) {
		return (byte) (number >> 8);
	}
}
