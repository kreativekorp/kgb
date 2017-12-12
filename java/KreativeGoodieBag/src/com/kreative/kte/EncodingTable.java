package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class EncodingTable {
	private TreeMap<ByteSequence, CharacterSequence> tabR;
	private TreeMap<CharacterSequence, ByteSequence> tabW;
	private boolean bsHasPrefix;
	private boolean bsNonUnique;
	private boolean csHasPrefix;
	private boolean csNonUnique;
	private int minBytes;
	private int maxBytes;
	private int minChars;
	private int maxChars;
	
	public EncodingTable() {
		tabR = new TreeMap<ByteSequence, CharacterSequence>();
		tabW = new TreeMap<CharacterSequence, ByteSequence>();
		bsHasPrefix = false;
		bsNonUnique = false;
		csHasPrefix = false;
		csNonUnique = false;
		minBytes = Integer.MAX_VALUE;
		maxBytes = 0;
		minChars = Integer.MAX_VALUE;
		maxChars = 0;
	}
	
	public EncodingTable(boolean includeC0, boolean includeASCII, boolean includeC1, boolean includeLatin1) {
		this();
		if (includeC0) {
			for (int i = 0x00; i < 0x20; i++) {
				ByteSequence bseq = new ByteSequence((byte)i);
				CharacterSequence cseq = new CharacterSequence(i);
				tabR.put(bseq, cseq);
				tabW.put(cseq, bseq);
			}
		}
		if (includeASCII) {
			for (int i = 0x20; i < 0x80; i++) {
				ByteSequence bseq = new ByteSequence((byte)i);
				CharacterSequence cseq = new CharacterSequence(i);
				tabR.put(bseq, cseq);
				tabW.put(cseq, bseq);
			}
		}
		if (includeC1) {
			for (int i = 0x80; i < 0xA0; i++) {
				ByteSequence bseq = new ByteSequence((byte)i);
				CharacterSequence cseq = new CharacterSequence(i);
				tabR.put(bseq, cseq);
				tabW.put(cseq, bseq);
			}
		}
		if (includeLatin1) {
			for (int i = 0xA0; i < 0x100; i++) {
				ByteSequence bseq = new ByteSequence((byte)i);
				CharacterSequence cseq = new CharacterSequence(i);
				tabR.put(bseq, cseq);
				tabW.put(cseq, bseq);
			}
		}
	}
	
	public void put(ByteSequence bseq, CharacterSequence cseq, boolean owR, boolean owW) {
		putR(bseq, cseq, owR);
		putW(bseq, cseq, owW);
	}
	
	private void putR(ByteSequence bseq, CharacterSequence cseq, boolean overwrite) {
		for (ByteSequence bseq2 : tabR.keySet()) {
			if (bseq.equals(bseq2)) {
				if (!overwrite) {
					return;
				} else if (cseq.equals(tabR.get(bseq2))) {
					return;
				} else {
					bsNonUnique = true;
				}
			} else if (bseq.startsWith(bseq2) || bseq2.startsWith(bseq)) {
				bsHasPrefix = true;
			}
		}
		for (CharacterSequence cseq2 : tabR.values()) {
			if (cseq.equals(cseq2)) {
				csNonUnique = true;
			} else if (cseq.startsWith(cseq2) || cseq2.startsWith(cseq)) {
				csHasPrefix = true;
			}
		}
		if (minBytes > bseq.length()) minBytes = bseq.length();
		if (maxBytes < bseq.length()) maxBytes = bseq.length();
		if (minChars > cseq.length()) minChars = cseq.length();
		if (maxChars < cseq.length()) maxChars = cseq.length();
		tabR.put(bseq, cseq);
	}
	
	private void putW(ByteSequence bseq, CharacterSequence cseq, boolean overwrite) {
		for (CharacterSequence cseq2 : tabW.keySet()) {
			if (cseq.equals(cseq2)) {
				if (!overwrite) {
					return;
				} else if (bseq.equals(tabW.get(cseq2))) {
					return;
				} else {
					csNonUnique = true;
				}
			} else if (cseq.startsWith(cseq2) || cseq2.startsWith(cseq)) {
				csHasPrefix = true;
			}
		}
		for (ByteSequence bseq2 : tabW.values()) {
			if (bseq.equals(bseq2)) {
				bsNonUnique = true;
			} else if (bseq.startsWith(bseq2) || bseq2.startsWith(bseq)) {
				bsHasPrefix = true;
			}
		}
		if (minBytes > bseq.length()) minBytes = bseq.length();
		if (maxBytes < bseq.length()) maxBytes = bseq.length();
		if (minChars > cseq.length()) minChars = cseq.length();
		if (maxChars < cseq.length()) maxChars = cseq.length();
		tabW.put(cseq, bseq);
	}
	
	public CharacterSequence get(ByteSequence bseq) {
		return tabR.get(bseq);
	}
	
	public ByteSequence get(CharacterSequence cseq) {
		return tabW.get(cseq);
	}
	
	public boolean byteSequenceContainsAnotherAsPrefix() {
		return bsHasPrefix;
	}
	
	public boolean byteSequencesAreNonUnique() {
		return bsNonUnique;
	}
	
	public boolean characterSequenceContainsAnotherAsPrefix() {
		return csHasPrefix;
	}
	
	public boolean characterSequencesAreNonUnique() {
		return csNonUnique;
	}
	
	public int minBytes() {
		return minBytes;
	}
	
	public int maxBytes() {
		return maxBytes;
	}
	
	public int minChars() {
		return minChars;
	}
	
	public int maxChars() {
		return maxChars;
	}
	
	public int minChar() {
		return tabW.firstKey().codePointAt(0);
	}
	
	public int maxChar() {
		return tabW.lastKey().codePointAt(0);
	}
	
	public int byteToCharacterCount() {
		return tabR.size();
	}
	
	public SortedMap<ByteSequence, CharacterSequence> byteToCharacterMap() {
		return Collections.unmodifiableSortedMap(tabR);
	}
	
	public int characterToByteCount() {
		return tabW.size();
	}
	
	public SortedMap<CharacterSequence, ByteSequence> characterToByteMap() {
		return Collections.unmodifiableSortedMap(tabW);
	}
	
	private static int exp2or3exp2bound(int i) {
		if (i < 0) return 0;
		if (i < 4) return i+1;
		for (int j = 4, k = 6; j > 0 && k > 0; j <<= 1, k <<= 1) {
			if (i < j) return j;
			if (i < k) return k;
		}
		return Integer.MAX_VALUE;
	}
	
	public boolean isNice() {
		/*
		 * Properties of a "nice" encoding:
		 * - Every byte sequence is mapped to one and only one character sequence.
		 * - Every character sequence is mapped to one and only one byte sequence.
		 * (This makes the text encoding round-trippable.)
		 * - Every single-byte byte sequence from 0x00 to 0xFF (or 0x7F for a 7-bit encoding, 0x3F for a 6-bit encoding, etc.) is mapped.
		 * - No byte sequence is mapped to 0xFFFD, a surrogate, or a code point that is not a Unicode character.
		 * (This makes the text encoding accept any arbitrary byte stream.)
		 */
		if (bsNonUnique || csNonUnique) return false;
		if (minBytes < 1 || minChars < 1) return false;
		int maxb = tabR.lastKey().byteAt(0) & 0xFF;
		int maxl = exp2or3exp2bound(maxb);
		if ((maxb+1) != maxl) return false;
		for (int i = 0; i < maxl; i++) {
			if (!tabR.containsKey(new ByteSequence((byte)i))) return false;
		}
		for (CharacterSequence cs : tabW.keySet()) {
			for (int i = 0; i < cs.length(); i++) {
				int ch = cs.codePointAt(i);
				if (ch < 0 || (ch >= 0xD800 && ch < 0xE000) || ch == 0xFFFD || ((ch & 0xFFFF) >= 0xFFFE) || ch >= 0x110000) return false;
			}
		}
		return true;
	}
	
	public int optimalByteSequenceFormat(boolean forTabW) {
		if (minBytes == 1 && maxBytes == 1) {
			if (!forTabW && exp2or3exp2bound(tabR.lastKey().byteAt(0) & 0xFF) == tabR.size())
				return 1;
			else return 2;
		}
		else if (minBytes == 2 && maxBytes == 2) {
			if (!forTabW && exp2or3exp2bound(((tabR.lastKey().byteAt(0) & 0xFF) << 8) | (tabR.lastKey().byteAt(1) & 0xFF)) == tabR.size())
				return 1;
			else return 4;
		}
		else if (minBytes == 4 && maxBytes == 4) return 6;
		else if (minBytes >= 1 && maxBytes <= 2) {
			for (ByteSequence bs : (forTabW ? tabW.values() : tabR.keySet())) {
				if (bs.byteAt(0) == 0 && bs.length() > 1) return 16;
			}
			return 3;
		}
		else if (minBytes >= 1 && maxBytes <= 4) {
			for (ByteSequence bs : (forTabW ? tabW.values() : tabR.keySet())) {
				if (bs.byteAt(0) == 0 && bs.length() > 1) return 16;
			}
			return 5;
		}
		else return 16;
	}
	
	public int optimalCharacterSequenceFormat(boolean forTabW) {
		if (minChars == 1 && maxChars == 1) {
			if (minChar() < 0) return 6;
			else if (maxChar() < 0x100) return 2;
			else if (maxChar() < 0x10000) return 4;
			else return 6;
		} else {
			if (minChars >= 1 && maxChars <= 2) {
				if (minChar() >= 0 && maxChar() < 0x100) {
					boolean fmt3candidate = true;
					for (CharacterSequence cs : (forTabW ? tabW.keySet() : tabR.values())) {
						if (cs.codePointAt(0) == 0 && cs.length() > 1) { fmt3candidate = false; break; }
					}
					if (fmt3candidate) return 3;
				}
				if (minChar() >= 0 && maxChar() < 0x110000) {
					boolean fmt5candidate = true;
					for (CharacterSequence cs : (forTabW ? tabW.keySet() : tabR.values())) {
						if (cs.length() > 1 && (cs.codePointAt(0) == 0 || cs.codePointAt(0) >= 0x10000 || cs.codePointAt(1) >= 0x10000)) { fmt5candidate = false; break; }
					}
					if (fmt5candidate) return 5;
				}
			}
			if (minChar() < 0) return 18;
			else if (maxChar() < 0x100) return 16;
			else if (maxChar() < 0x10000) return 17;
			else return 18;
		}
	}
	
	public void writeTABR(DataOutput out) throws IOException {
		int bsFmt = optimalByteSequenceFormat(false);
		int csFmt = optimalCharacterSequenceFormat(false);
		out.writeByte(bsFmt);
		out.writeByte(csFmt);
		int flags = 0x10;
		if (bsHasPrefix) flags |= 0x01;
		if (csHasPrefix) flags |= 0x04;
		if (csNonUnique) flags |= 0x08;
		out.writeShort(flags);
		out.writeByte(minBytes);
		out.writeByte(maxBytes);
		out.writeByte(minChars);
		out.writeByte(maxChars);
		if (bsFmt == 1) {
			ByteSequence maxk = tabR.lastKey();
			int imaxk = 0;
			for (int i = 0; i < maxk.length(); i++) {
				imaxk = (imaxk << 8) | (maxk.byteAt(i) & 0xFF);
			}
			int tmaxk = exp2or3exp2bound(imaxk);
			out.writeInt(tmaxk);
			for (int i = 0; i < tmaxk; i++) {
				int ki = i;
				byte[] ka = new byte[maxk.length()];
				for (int j = ka.length-1; j >= 0; j--) {
					ka[j] = (byte)ki;
					ki >>= 8;
				}
				ByteSequence k = new ByteSequence(ka);
				tabR.get(k).write(out, csFmt);
			}
		} else {
			out.writeInt(tabR.size());
			for (Map.Entry<ByteSequence,CharacterSequence> e : tabR.entrySet()) {
				e.getKey().write(out, bsFmt);
				e.getValue().write(out, csFmt);
			}
		}
	}
	
	public void writeTABW(DataOutput out) throws IOException {
		int bsFmt = optimalByteSequenceFormat(true);
		int csFmt = optimalCharacterSequenceFormat(true);
		out.writeByte(bsFmt);
		out.writeByte(csFmt);
		int flags = 0x20;
		if (bsHasPrefix) flags |= 0x01;
		if (bsNonUnique) flags |= 0x02;
		if (csHasPrefix) flags |= 0x04;
		out.writeShort(flags);
		out.writeByte(minBytes);
		out.writeByte(maxBytes);
		out.writeByte(minChars);
		out.writeByte(maxChars);
		if (csFmt == 1) {
			int tmaxk = exp2or3exp2bound(tabW.lastKey().codePointAt(0));
			out.writeInt(tmaxk);
			for (int i = 0; i < tmaxk; i++) {
				CharacterSequence k = new CharacterSequence(i);
				tabW.get(k).write(out, bsFmt);
			}
		} else {
			out.writeInt(tabW.size());
			for (Map.Entry<CharacterSequence,ByteSequence> e : tabW.entrySet()) {
				e.getValue().write(out, bsFmt);
				e.getKey().write(out, csFmt);
			}
		}
	}
	
	public static EncodingTable read(DataInput in, boolean owR, boolean owW) throws IOException {
		int bsFmt = in.readUnsignedByte();
		int csFmt = in.readUnsignedByte();
		in.readUnsignedShort();
		in.readUnsignedByte();
		in.readUnsignedByte();
		in.readUnsignedByte();
		in.readUnsignedByte();
		int count = in.readInt();
		EncodingTable table = new EncodingTable();
		for (int i = 0; i < count; i++) {
			ByteSequence bs = ByteSequence.read(in, i, count, bsFmt);
			CharacterSequence cs = CharacterSequence.read(in, i, count, csFmt);
			table.put(bs, cs, owR, owW);
		}
		return table;
	}
}
