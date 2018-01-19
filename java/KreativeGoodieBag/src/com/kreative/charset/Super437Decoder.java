package com.kreative.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

public class Super437Decoder extends CharsetDecoder {
	private static final char[] SUPER437_LOW = {
		'\u0000', '\u02CB', '\u02DD', '\u02D9', '\u02DA', '\u02C7', '\u02D8', '\u02DB',
		'\b',     '\t',     '\n',     '\u000B', '\f',     '\r',     '\uFB01', '\uFB02',
		'\uF8FF', '\u2020', '\u2021', '\u02C6', '\u02DC', '\u00A8', '\u00B4', '\u00B8',
		'\u201A', '\u201E', '\u2026', '\u001B', '\u2018', '\u2019', '\u201C', '\u201D',
	};
	private static final char[] SUPER437_HIGH = {
		'\u00C7', '\u00FC', '\u00E9', '\u00E2', '\u00E4', '\u00E0', '\u00E5', '\u00E7',
		'\u00EA', '\u00EB', '\u00E8', '\u00EF', '\u00EE', '\u00EC', '\u00C4', '\u00C5',
		'\u00C9', '\u00E6', '\u00C6', '\u00F4', '\u00F6', '\u00F2', '\u00FB', '\u00F9',
		'\u00FF', '\u00D6', '\u00DC', '\u00A2', '\u00A3', '\u00A5', '\u00A4', '\u0192',
		'\u00E1', '\u00ED', '\u00F3', '\u00FA', '\u00F1', '\u00D1', '\u00AA', '\u00BA',
		'\u00BF', '\u00AD', '\u00AC', '\u00BD', '\u00BC', '\u00A1', '\u00AB', '\u00BB',
		'\u0161', '\u00DD', '\u0141', '\u00C2', '\u00C3', '\u00C0', '\u00D8', '\u0160',
		'\u00CA', '\u00CB', '\u00C8', '\u00CF', '\u00CE', '\u00CC', '\u00E3', '\u00F8',
		'\u0142', '\u0152', '\u0153', '\u00D4', '\u00D5', '\u00D2', '\u00DB', '\u00D9',
		'\u0178', '\u00F5', '\u00FD', '\u00F0', '\u00D0', '\u00DE', '\u00FE', '\u0131',
		'\u00C1', '\u00CD', '\u00D3', '\u00DA', '\u017D', '\u017E', '\u00A9', '\u00AE',
		'\u2122', '\u2013', '\u2014', '\u2030', '\u00BE', '\u00A6', '\u2039', '\u203A',
		'\u2318', '\u00DF', '\u220F', '\u03C0', '\u2211', '\u00A7', '\u00B5', '\u00D7',
		'\u25CA', '\u2206', '\u03A9', '\u2202', '\u221E', '\u00B6', '\u20AC', '\u00AF',
		'\u2260', '\u00B1', '\u2265', '\u2264', '\u222B', '\u2044', '\u00F7', '\u2248',
		'\u00B0', '\u2022', '\u00B7', '\u221A', '\u00B9', '\u00B2', '\u00B3', '\u00A0',
	};
	
	private final boolean overrideC0;
	
	public Super437Decoder(Charset cs, boolean overrideC0) {
		super(cs, 1, 1);
		this.overrideC0 = overrideC0;
	}
	
	@Override
	public CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
		while (in.hasRemaining()) {
			if (!out.hasRemaining()) return CoderResult.OVERFLOW;
			int b = in.get() & 0xFF;
			if (b < 0x20) out.put(overrideC0 ? SUPER437_LOW[b] : (char)b);
			else if (b < 0x80) out.put((char)b);
			else out.put(SUPER437_HIGH[b & 0x7F]);
		}
		return CoderResult.UNDERFLOW;
	}
}
