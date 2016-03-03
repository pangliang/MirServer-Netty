package com.zhaoxiaodan.mirserver.network;

import java.util.Arrays;

public class Bit6Coder {
	public static byte[] encoder6BitBuf(byte[] src) {
		int    len     = src.length;
		int destLen = (len/3)*4+10;
		byte[] dest    = new byte[destLen];
		int    destPos = 0, resetCount = 0;
		byte   chMade  = 0, chRest = 0;

		for (int i = 0; i < len ; i++) {
			if (destPos >= destLen)
				break;

			chMade = (byte) ((chRest | ((src[i] & 0xff) >> (2 + resetCount))) & 0x3f);
			chRest = (byte) ((((src[i] & 0xff) << (8 - (2 + resetCount))) >> 2) & 0x3f);

			resetCount += 2;
			if(resetCount < 6)
			{
				dest[destPos++] = (byte)(chMade + 0x3c);
			}else{
				if(destPos < destLen - 1)
				{
					dest[destPos++] = (byte)(chMade + 0x3c);
					dest[destPos++] = (byte)(chRest + 0x3c);
				}else {
					dest[destPos++] = (byte)(chMade + 0x3c);
				}

				resetCount = 0;
				chRest = 0;
			}
		}
		if(resetCount > 0 )
			dest[destPos++] = (byte)(chRest + 0x3c);

		dest[destPos] = 0;
		return Arrays.copyOfRange(dest,0,destPos);

	}

	public static byte[] decode6BitBuf(byte[] src) {
		final byte[] Decode6BitMask = {(byte) 0xfc, (byte) 0xf8, (byte) 0xf0, (byte) 0xe0, (byte) 0xc0};

		int    len  = src.length;
		byte[] dest = new byte[len*3/4];

		int destPos = 0;
		int bitPos  = 2;
		int madeBit = 0;

		byte ch = 0, chCode = 0, tmp = 0;

		for (int i = 0; i < len; i++) {
			if ((src[i] - 0x3c) >= 0) {
				ch = (byte) (src[i] - 0x3c);
			} else {
				destPos = 0;
				break;
			}

			if (destPos >= dest.length)
				break;

			if (madeBit + 6 >= 8) {
				chCode = (byte) (tmp | ((ch & 0x3f) >> (6 - bitPos)));
				dest[destPos++] = chCode;

				madeBit = 0;
				if (bitPos < 6) {
					bitPos += 2;
				} else {
					bitPos = 2;
					continue;
				}
			}

			tmp = (byte) ((ch << bitPos) & Decode6BitMask[bitPos - 2]);

			madeBit += 8 - bitPos;
		}

		return dest;

	}
}
