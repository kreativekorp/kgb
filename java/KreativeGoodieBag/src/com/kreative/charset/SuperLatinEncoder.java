package com.kreative.charset;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public class SuperLatinEncoder extends CharsetEncoder {
	private final boolean overrideC0;
	
	protected SuperLatinEncoder(Charset cs, boolean overrideC0) {
		super(cs, 1, 1);
		this.overrideC0 = overrideC0;
	}
	
	protected CoderResult encodeLoop(CharBuffer in, ByteBuffer out) {
		while (in.hasRemaining()) {
			if (!out.hasRemaining()) return CoderResult.OVERFLOW;
			int ch = in.get() & 0xFFFF;
			if (ch < 0x80) out.put((byte)ch);
			else if (ch < 0xA0) return unmappable(in);
			else if (ch < 0x100) out.put((byte)ch);
			else switch (ch) {
			case 0x0131: out.put((byte)0x8F); break;
			case 0x0141: out.put((byte)0x8D); break;
			case 0x0142: out.put((byte)0x9D); break;
			case 0x0152: out.put((byte)0x8C); break;
			case 0x0153: out.put((byte)0x9C); break;
			case 0x0160: out.put((byte)0x8A); break;
			case 0x0161: out.put((byte)0x9A); break;
			case 0x0178: out.put((byte)0x9F); break;
			case 0x017D: out.put((byte)0x8E); break;
			case 0x017E: out.put((byte)0x9E); break;
			case 0x0192: out.put((byte)0x83); break;
			case 0x02C6: out.put((byte)0x88); break;
			case 0x02C7: if (overrideC0) out.put((byte)0x05); else return unmappable(in); break;
			case 0x02CB: if (overrideC0) out.put((byte)0x01); else return unmappable(in); break;
			case 0x02D8: if (overrideC0) out.put((byte)0x06); else return unmappable(in); break;
			case 0x02D9: if (overrideC0) out.put((byte)0x03); else return unmappable(in); break;
			case 0x02DA: if (overrideC0) out.put((byte)0x04); else return unmappable(in); break;
			case 0x02DB: if (overrideC0) out.put((byte)0x07); else return unmappable(in); break;
			case 0x02DC: out.put((byte)0x98); break;
			case 0x02DD: if (overrideC0) out.put((byte)0x02); else return unmappable(in); break;
			case 0x03A9: if (overrideC0) out.put((byte)0x17); else return unmappable(in); break;
			case 0x03C0: if (overrideC0) out.put((byte)0x18); else return unmappable(in); break;
			case 0x2013: out.put((byte)0x96); break;
			case 0x2014: out.put((byte)0x97); break;
			case 0x2018: out.put((byte)0x91); break;
			case 0x2019: out.put((byte)0x92); break;
			case 0x201A: out.put((byte)0x82); break;
			case 0x201C: out.put((byte)0x93); break;
			case 0x201D: out.put((byte)0x94); break;
			case 0x201E: out.put((byte)0x84); break;
			case 0x2020: out.put((byte)0x86); break;
			case 0x2021: out.put((byte)0x87); break;
			case 0x2022: out.put((byte)0x95); break;
			case 0x2026: out.put((byte)0x85); break;
			case 0x2030: out.put((byte)0x89); break;
			case 0x2039: out.put((byte)0x8B); break;
			case 0x203A: out.put((byte)0x9B); break;
			case 0x2044: if (overrideC0) out.put((byte)0x11); else return unmappable(in); break;
			case 0x20AC: out.put((byte)0x80); break;
			case 0x2122: out.put((byte)0x99); break;
			case 0x2202: if (overrideC0) out.put((byte)0x19); else return unmappable(in); break;
			case 0x2206: if (overrideC0) out.put((byte)0x16); else return unmappable(in); break;
			case 0x220F: if (overrideC0) out.put((byte)0x14); else return unmappable(in); break;
			case 0x2211: if (overrideC0) out.put((byte)0x13); else return unmappable(in); break;
			case 0x221A: if (overrideC0) out.put((byte)0x12); else return unmappable(in); break;
			case 0x221E: if (overrideC0) out.put((byte)0x1A); else return unmappable(in); break;
			case 0x222B: if (overrideC0) out.put((byte)0x15); else return unmappable(in); break;
			case 0x2248: if (overrideC0) out.put((byte)0x1F); else return unmappable(in); break;
			case 0x2260: if (overrideC0) out.put((byte)0x1D); else return unmappable(in); break;
			case 0x2264: if (overrideC0) out.put((byte)0x1C); else return unmappable(in); break;
			case 0x2265: if (overrideC0) out.put((byte)0x1E); else return unmappable(in); break;
			case 0x2318: out.put((byte)0x90); break;
			case 0x25CA: out.put((byte)0x81); break;
			case 0xF8FF: if (overrideC0) out.put((byte)0x10); else return unmappable(in); break;
			case 0xFB01: if (overrideC0) out.put((byte)0x0E); else return unmappable(in); break;
			case 0xFB02: if (overrideC0) out.put((byte)0x0F); else return unmappable(in); break;
			default: return unmappable(in);
			}
		}
		return CoderResult.UNDERFLOW;
	}
	
	private CoderResult unmappable(CharBuffer in) {
		in.position(in.position() - 1);
		return CoderResult.unmappableForLength(1);
	}
}
