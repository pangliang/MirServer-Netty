package com.zhaoxiaodan.mirserver.network.debug;

import io.netty.buffer.ByteBuf;
import io.netty.util.internal.StringUtil;

public class ByteBufDumper {
	private static final String NEWLINE = StringUtil.NEWLINE;
	private static final String[] BYTE2HEX = new String[256];
	private static final String[] HEXPADDING = new String[16];
	private static final String[] BYTEPADDING = new String[16];
	private static final char[] BYTE2CHAR = new char[256];
	private static final String[] HEXDUMP_ROWPREFIXES = new String[65536 >>> 4];

	static {
		int i;

		// Generate the lookup table for byte-to-hex-dump conversion
		for (i = 0; i < BYTE2HEX.length; i ++) {
			BYTE2HEX[i] = ' ' + StringUtil.byteToHexStringPadded(i);
		}

		// Generate the lookup table for hex dump paddings
		for (i = 0; i < HEXPADDING.length; i ++) {
			int padding = HEXPADDING.length - i;
			StringBuilder buf = new StringBuilder(padding * 3);
			for (int j = 0; j < padding; j ++) {
				buf.append("   ");
			}
			HEXPADDING[i] = buf.toString();
		}

		// Generate the lookup table for byte dump paddings
		for (i = 0; i < BYTEPADDING.length; i ++) {
			int padding = BYTEPADDING.length - i;
			StringBuilder buf = new StringBuilder(padding);
			for (int j = 0; j < padding; j ++) {
				buf.append(' ');
			}
			BYTEPADDING[i] = buf.toString();
		}

		// Generate the lookup table for byte-to-char conversion
		for (i = 0; i < BYTE2CHAR.length; i ++) {
			if (i <= 0x1f || i >= 0x7f) {
				BYTE2CHAR[i] = '.';
			} else {
				BYTE2CHAR[i] = (char) i;
			}
		}

		// Generate the lookup table for the start-offset header in each row (up to 64KiB).
		for (i = 0; i < HEXDUMP_ROWPREFIXES.length; i ++) {
			StringBuilder buf = new StringBuilder(12);
			buf.append(NEWLINE);
			buf.append(Long.toHexString(i << 4 & 0xFFFFFFFFL | 0x100000000L));
			buf.setCharAt(buf.length() - 9, '|');
			buf.append('|');
			HEXDUMP_ROWPREFIXES[i] = buf.toString();
		}
	}

	public static String formatByteBuf(ByteBuf msg) {
		int length = msg.readableBytes();
		if (length == 0) {
			StringBuilder buf = new StringBuilder(1  + 4);
			buf.append(' ').append(": 0B");
			return buf.toString();
		} else {
			int rows = length / 16 + (length % 15 == 0? 0 : 1) + 4;
			StringBuilder buf = new StringBuilder(1 + 2 + 10 + 1 + 2 + rows * 80);

			buf.append(' ').append(": ").append(length).append('B');
			appendHexDump(buf, msg);

			return buf.toString();
		}
	}

	private static void appendHexDump(StringBuilder dump, ByteBuf buf) {
		dump.append(
				NEWLINE + "         +-------------------------------------------------+" +
						NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" +
						NEWLINE + "+--------+-------------------------------------------------+----------------+");

		final int startIndex = buf.readerIndex();
		final int endIndex = buf.writerIndex();
		final int length = endIndex - startIndex;
		final int fullRows = length >>> 4;
		final int remainder = length & 0xF;

		// Dump the rows which have 16 bytes.
		for (int row = 0; row < fullRows; row ++) {
			int rowStartIndex = row << 4;

			// Per-row prefix.
			appendHexDumpRowPrefix(dump, row, rowStartIndex);

			// Hex dump
			int rowEndIndex = rowStartIndex + 16;
			for (int j = rowStartIndex; j < rowEndIndex; j ++) {
				dump.append(BYTE2HEX[buf.getUnsignedByte(j)]);
			}
			dump.append(" |");

			// ASCII dump
			for (int j = rowStartIndex; j < rowEndIndex; j ++) {
				dump.append(BYTE2CHAR[buf.getUnsignedByte(j)]);
			}
			dump.append('|');
		}

		// Dump the last row which has less than 16 bytes.
		if (remainder != 0) {
			int rowStartIndex = fullRows << 4;
			appendHexDumpRowPrefix(dump, fullRows, rowStartIndex);

			// Hex dump
			int rowEndIndex = rowStartIndex + remainder;
			for (int j = rowStartIndex; j < rowEndIndex; j ++) {
				dump.append(BYTE2HEX[buf.getUnsignedByte(j)]);
			}
			dump.append(HEXPADDING[remainder]);
			dump.append(" |");

			// Ascii dump
			for (int j = rowStartIndex; j < rowEndIndex; j ++) {
				dump.append(BYTE2CHAR[buf.getUnsignedByte(j)]);
			}
			dump.append(BYTEPADDING[remainder]);
			dump.append('|');
		}

		dump.append(NEWLINE + "+--------+-------------------------------------------------+----------------+");
	}

	private static void appendHexDumpRowPrefix(StringBuilder dump, int row, int rowStartIndex) {
		if (row < HEXDUMP_ROWPREFIXES.length) {
			dump.append(HEXDUMP_ROWPREFIXES[row]);
		} else {
			dump.append(NEWLINE);
			dump.append(Long.toHexString(rowStartIndex & 0xFFFFFFFFL | 0x100000000L));
			dump.setCharAt(dump.length() - 9, '|');
			dump.append('|');
		}
	}
}
