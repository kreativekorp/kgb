package com.kreative.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

public class SuperMultinationalDecoder extends CharsetDecoder {
	private static final char[] SUPERMULTINATIONAL_LOW = {
		'\u0000', '\u0160', '\u017D', '\u0178', '\u0161', '\u017E', '\u25CA', '\u2318',
		'\b'    , '\t'    , '\n',     '\u000B', '\f'    , '\r'    , '\u2018', '\u00AD',
		'\uF8FF', '\u2122', '\u221A', '\u2211', '\u220F', '\u222B', '\u2206', '\u03A9',
		'\u03C0', '\u2202', '\u221E', '\u001B', '\u2264', '\u2260', '\u2265', '\u2248',
	};
	private static final char[] SUPERMULTINATIONAL_HIGH = {
		'\u00a0', '\u00c0', '\u00c1', '\u00c2', '\u00c3', '\u00c4', '\u00c5', '\u00c7',
		'\u00c8', '\u00c9', '\u00ca', '\u00cb', '\u00cc', '\u00cd', '\u00ce', '\u00cf',
		'\u00d0', '\u00d1', '\u00d2', '\u00d3', '\u00d4', '\u00d5', '\u00d6', '\u00d9',
		'\u00da', '\u00db', '\u00dc', '\u00dd', '\u00de', '\u00b5', '\u00d7', '\u00f7',
		'\u00a9', '\u00a1', '\u00a2', '\u00a3', '\u2044', '\u00a5', '\u0192', '\u00a7',
		'\u00a4', '\u2019', '\u201c', '\u00ab', '\u2039', '\u203a', '\ufb01', '\ufb02',
		'\u00ae', '\u2013', '\u2020', '\u2021', '\u00b7', '\u00a6', '\u00b6', '\u2022',
		'\u201a', '\u201e', '\u201d', '\u00bb', '\u2026', '\u2030', '\u00ac', '\u00bf',
		'\u00b9', '\u02cb', '\u00b4', '\u02c6', '\u02dc', '\u00af', '\u02d8', '\u02d9',
		'\u00a8', '\u00b2', '\u02da', '\u00b8', '\u00b3', '\u02dd', '\u02db', '\u02c7',
		'\u2014', '\u00b1', '\u00bc', '\u00bd', '\u00be', '\u00e0', '\u00e1', '\u00e2',
		'\u00e3', '\u00e4', '\u00e5', '\u00e7', '\u00e8', '\u00e9', '\u00ea', '\u00eb',
		'\u00ec', '\u00c6', '\u00ed', '\u00aa', '\u00ee', '\u00ef', '\u00f0', '\u00f1',
		'\u0141', '\u00d8', '\u0152', '\u00ba', '\u00f2', '\u00f3', '\u00f4', '\u00f5',
		'\u00f6', '\u00e6', '\u00f9', '\u00fa', '\u00fb', '\u0131', '\u00fc', '\u00fd',
		'\u0142', '\u00f8', '\u0153', '\u00df', '\u00fe', '\u00ff', '\u00b0', '\u20ac',
	};
	
	private final boolean overrideC0;
	
	public SuperMultinationalDecoder(Charset cs, boolean overrideC0) {
		super(cs, 1, 1);
		this.overrideC0 = overrideC0;
	}
	
	@Override
	public CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
		while (in.hasRemaining()) {
			if (!out.hasRemaining()) return CoderResult.OVERFLOW;
			int b = in.get() & 0xFF;
			if (b < 0x20) out.put(overrideC0 ? SUPERMULTINATIONAL_LOW[b] : (char)b);
			else if (b < 0x80) out.put((char)b);
			else out.put(SUPERMULTINATIONAL_HIGH[b & 0x7F]);
		}
		return CoderResult.UNDERFLOW;
	}
}
