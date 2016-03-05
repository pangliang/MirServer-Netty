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

	public static short makeWord(short low, short high) {
		return (short) ((high << 8) | low);
	}

	public static int makeLong(short low, short high) {
		return ((high << 16) | low);
	}

	public static int makeLong(int low, int high) {
		return (( high << 16) | low);
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

	/**
	 * 从字节数组中读取整形数据
	 *
	 * @param bytes
	 * 	数据来源
	 * @param index
	 * 	数组中获取数据的起始位置(索引从0开始，数据还原将包含给定位置) 具体规则示例参见{@link #readShort(byte[], int, boolean) readShort}
	 * @param reverse
	 * 	是否需要反转字节(只针对包含当前数据的字节数组)
	 * @return
	 * 	从字节数组指定位置4个字节还原出的短整形数值
	 */
	public static int readInt(byte[] bytes, int index, boolean reverse) {
		if(reverse)
			return (int) (((bytes[index + 3] & 0xff) << 24)
					| ((bytes[index + 2] & 0xff) << 16)
					| ((bytes[index + 1] & 0xff) << 8)
					| (bytes[index] & 0xff));
		else
			return (int) (((bytes[index] & 0xff) << 24)
					| ((bytes[index + 1] & 0xff) << 16)
					| ((bytes[index + 2] & 0xff) << 8)
					| (bytes[index + 3] & 0xff));
	}

	/**
	 * 从字节数组中读取断整形数据
	 *
	 * @param bytes
	 * 	数据来源
	 * @param index
	 * 	数组中获取数据的起始位置(索引从0开始，数据还原将包含给定位置)
	 * <br>
	 * 	例如给定[0x01,0x02,0x03,0x04],2,true作为参数则函数会将[0x04,0x03]还原为短整形数值并返回
	 * @param reverse
	 * 	是否需要反转字节(只针对包含当前数据的字节数组)
	 * @return
	 * 	从字节数组指定位置2个字节还原出的短整形数值
	 */
	public static short readShort(byte[] bytes, int index, boolean reverse) {
		if(reverse)
			return (short) ((bytes[index + 1] << 8) | (bytes[index] & 0xff));
		else
			return (short) ((bytes[index] << 8) | (bytes[index + 1] & 0xff));
	}
}
