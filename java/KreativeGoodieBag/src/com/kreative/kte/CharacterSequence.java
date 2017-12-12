package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CharacterSequence implements Comparable<CharacterSequence> {
	private int[] sequence;
	
	public static CharacterSequence parseCharacterSequence(String s) {
		s = s.trim();
		if (s.startsWith("\"") && s.endsWith("\"")) {
			return new CharacterSequence(s.substring(1, s.length()-1));
		}
		if (s.startsWith("'") && s.endsWith("'")) {
			return new CharacterSequence(s.substring(1, s.length()-1));
		}
		String[] cs = s.split("\\s*[.,:;+-]\\s*");
		int[] ci = new int[cs.length];
		for (int i = 0; i < cs.length; i++) {
			if (cs[i].startsWith("0x") || cs[i].startsWith("0X") || cs[i].startsWith("U+") || cs[i].startsWith("U-") || cs[i].startsWith("u+") || cs[i].startsWith("u-")) {
				ci[i] = Integer.parseInt(cs[i].substring(2).trim(), 16);
			} else if (s.startsWith("$")) {
				ci[i] = Integer.parseInt(cs[i].substring(1).trim(), 16);
			} else {
				ci[i] = Integer.parseInt(cs[i], 16);
			}
		}
		return new CharacterSequence(ci);
	}
	
	public CharacterSequence() {
		this.sequence = new int[0];
	}
	
	public CharacterSequence(int a) {
		this.sequence = new int[]{a};
	}
	
	public CharacterSequence(int a, int b) {
		this.sequence = new int[]{a,b};
	}
	
	public CharacterSequence(int a, int b, int c) {
		this.sequence = new int[]{a,b,c};
	}
	
	public CharacterSequence(int a, int b, int c, int d) {
		this.sequence = new int[]{a,b,c,d};
	}
	
	public CharacterSequence(int[] b) {
		this.sequence = new int[b.length];
		for (int i = 0; i < b.length; i++) {
			this.sequence[i] = b[i];
		}
	}
	
	public CharacterSequence(int[] b, int off, int len) {
		this.sequence = new int[len];
		for (int ti = 0, bi = off; ti < len; ti++, bi++) {
			this.sequence[ti] = b[bi];
		}
	}
	
	public CharacterSequence(int[] a, int[] b) {
		this.sequence = new int[a.length + b.length];
		int si = 0;
		for (int i = 0; i < a.length; i++) {
			this.sequence[si++] = a[i];
		}
		for (int i = 0; i < b.length; i++) {
			this.sequence[si++] = b[i];
		}
	}
	
	public CharacterSequence(int[] a, int aoff, int alen, int[] b, int boff, int blen) {
		this.sequence = new int[alen + blen];
		int si = 0;
		for (int ti = 0, ai = aoff; ti < alen; ti++, ai++) {
			this.sequence[si++] = a[ai];
		}
		for (int ti = 0, bi = boff; ti < blen; ti++, bi++) {
			this.sequence[si++] = b[bi];
		}
	}
	
	public CharacterSequence(char a) {
		this.sequence = new int[]{a & 0xFFFF};
	}
	
	public CharacterSequence(char hi, char lo) {
		this.sequence = new int[]{Character.toCodePoint(hi, lo)};
	}
	
	public CharacterSequence(char[] ca) {
		ArrayList<Integer> a = new ArrayList<Integer>(ca.length);
		int i = 0;
		while (i < ca.length) {
			if (i+1 < ca.length && Character.isSurrogatePair(ca[i], ca[i+1])) {
				a.add(Character.toCodePoint(ca[i], ca[i+1]));
				i += 2;
			} else {
				a.add(ca[i] & 0xFFFF);
				i++;
			}
		}
		Integer[] ia = a.toArray(new Integer[0]);
		this.sequence = new int[ia.length];
		for (i = 0; i < ia.length; i++) {
			this.sequence[i] = ia[i].intValue();
		}
	}
	
	public CharacterSequence(char[] ca, char[] cb) {
		ArrayList<Integer> a = new ArrayList<Integer>(ca.length);
		int i;
		i = 0;
		while (i < ca.length) {
			if (i+1 < ca.length && Character.isSurrogatePair(ca[i], ca[i+1])) {
				a.add(Character.toCodePoint(ca[i], ca[i+1]));
				i += 2;
			} else {
				a.add(ca[i] & 0xFFFF);
				i++;
			}
		}
		i = 0;
		while (i < cb.length) {
			if (i+1 < cb.length && Character.isSurrogatePair(cb[i], cb[i+1])) {
				a.add(Character.toCodePoint(cb[i], cb[i+1]));
				i += 2;
			} else {
				a.add(cb[i] & 0xFFFF);
				i++;
			}
		}
		Integer[] ia = a.toArray(new Integer[0]);
		this.sequence = new int[ia.length];
		for (i = 0; i < ia.length; i++) {
			this.sequence[i] = ia[i].intValue();
		}
	}
	
	public CharacterSequence(char[] ca, int off, int len) {
		ArrayList<Integer> a = new ArrayList<Integer>(len);
		int i = 0;
		while (i < len) {
			if (i+1 < len && Character.isSurrogatePair(ca[off+i], ca[off+i+1])) {
				a.add(Character.toCodePoint(ca[off+i], ca[off+i+1]));
				i += 2;
			} else {
				a.add(ca[off+i] & 0xFFFF);
				i++;
			}
		}
		Integer[] ia = a.toArray(new Integer[0]);
		this.sequence = new int[ia.length];
		for (i = 0; i < ia.length; i++) {
			this.sequence[i] = ia[i].intValue();
		}
	}
	
	public CharacterSequence(char[] ca, int aoff, int alen, char[] cb, int boff, int blen) {
		ArrayList<Integer> a = new ArrayList<Integer>(alen+blen);
		int i;
		i = 0;
		while (i < alen) {
			if (i+1 < alen && Character.isSurrogatePair(ca[aoff+i], ca[aoff+i+1])) {
				a.add(Character.toCodePoint(ca[aoff+i], ca[aoff+i+1]));
				i += 2;
			} else {
				a.add(ca[aoff+i] & 0xFFFF);
				i++;
			}
		}
		i = 0;
		while (i < blen) {
			if (i+1 < blen && Character.isSurrogatePair(cb[boff+i], cb[boff+i+1])) {
				a.add(Character.toCodePoint(cb[boff+i], cb[boff+i+1]));
				i += 2;
			} else {
				a.add(cb[boff+i] & 0xFFFF);
				i++;
			}
		}
		Integer[] ia = a.toArray(new Integer[0]);
		this.sequence = new int[ia.length];
		for (i = 0; i < ia.length; i++) {
			this.sequence[i] = ia[i].intValue();
		}
	}
	
	public CharacterSequence(String s) {
		ArrayList<Integer> a = new ArrayList<Integer>(s.length());
		int i = 0;
		while (i < s.length()) {
			int ch = s.codePointAt(i);
			a.add(ch);
			if (ch >= 0x10000) i += 2;
			else i++;
		}
		Integer[] ia = a.toArray(new Integer[0]);
		this.sequence = new int[ia.length];
		for (i = 0; i < ia.length; i++) {
			this.sequence[i] = ia[i].intValue();
		}
	}
	
	public CharacterSequence(String s, int off, int len) {
		ArrayList<Integer> a = new ArrayList<Integer>(len);
		int i = 0;
		while (i < len) {
			int ch = s.codePointAt(off+i);
			a.add(ch);
			if (ch >= 0x10000) i += 2;
			else i++;
		}
		Integer[] ia = a.toArray(new Integer[0]);
		this.sequence = new int[ia.length];
		for (i = 0; i < ia.length; i++) {
			this.sequence[i] = ia[i].intValue();
		}
	}
	
	public int[] toIntArray() {
		int[] b = new int[this.sequence.length];
		for (int i = 0; i < this.sequence.length; i++) {
			b[i] = this.sequence[i];
		}
		return b;
	}
	
	public int[] toIntArray(int start, int end) {
		int[] b = new int[end-start];
		for (int bi = 0, ti = start; ti < end; bi++, ti++) {
			b[bi] = this.sequence[ti];
		}
		return b;
	}
	
	public int codePointAt(int i) {
		return this.sequence[i];
	}
	
	public int length() {
		return this.sequence.length;
	}
	
	public CharacterSequence concat(CharacterSequence cseq) {
		return new CharacterSequence(this.sequence, cseq.sequence);
	}
	
	public CharacterSequence concat(int[] b) {
		return new CharacterSequence(this.sequence, b);
	}
	
	public CharacterSequence concat(int[] b, int off, int len) {
		return new CharacterSequence(this.sequence, 0, this.sequence.length, b, off, len);
	}
	
	public CharacterSequence subsequence(int start, int end) {
		return new CharacterSequence(this.sequence, start, end-start);
	}
	
	public boolean startsWith(CharacterSequence other) {
		if (other.sequence.length <= this.sequence.length) {
			for (int i = 0; i < this.sequence.length && i < other.sequence.length; i++) {
				if (this.sequence[i] != other.sequence[i]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean startsWithIgnoreCase(CharacterSequence other) {
		if (other.sequence.length <= this.sequence.length) {
			for (int i = 0; i < this.sequence.length && i < other.sequence.length; i++) {
				if (Character.toLowerCase(this.sequence[i]) != Character.toLowerCase(other.sequence[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public int compareTo(CharacterSequence other) {
		for (int i = 0; i < this.sequence.length && i < other.sequence.length; i++) {
			if (this.sequence[i] != other.sequence[i]) {
				return this.sequence[i] - other.sequence[i];
			}
		}
		return other.sequence.length - this.sequence.length;
	}
	
	public int compareToIgnoreCase(CharacterSequence other) {
		for (int i = 0; i < this.sequence.length && i < other.sequence.length; i++) {
			if (Character.toLowerCase(this.sequence[i]) != Character.toLowerCase(other.sequence[i])) {
				return this.sequence[i] - other.sequence[i];
			}
		}
		return other.sequence.length - this.sequence.length;
	}
	
	public boolean equals(Object o) {
		if (o instanceof CharacterSequence) {
			CharacterSequence other = (CharacterSequence)o;
			return Arrays.equals(this.sequence, other.sequence);
		} else {
			return false;
		}
	}
	
	public boolean equalsIgnoreCase(CharacterSequence other) {
		if (this.sequence.length == other.sequence.length) {
			for (int i = 0; i < this.sequence.length && i < other.sequence.length; i++) {
				if (Character.toLowerCase(this.sequence[i]) != Character.toLowerCase(other.sequence[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return Arrays.hashCode(this.sequence);
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < this.sequence.length; i++) {
			s.append(Character.toChars(this.sequence[i]));
		}
		return s.toString();
	}
	
	public String toHexString() {
		StringBuffer s = new StringBuffer();
		for (int i : this.sequence) {
			s.append('+');
			int lp = s.length();
			s.append(Integer.toHexString(i).toUpperCase());
			while (s.length()-lp < 4) s.insert(lp, '0');
		}
		if (s.length() > 0) s.deleteCharAt(0);
		return s.toString();
	}
	
	public void write(DataOutput out, int csFmt) throws IOException {
		switch (csFmt) {
		case 1:
			// indexed - don't write anything
			break;
		case 2:
			if (sequence.length != 1) throw new IOException("Inappropriate format for this character sequence");
			if (sequence[0] < 0 || sequence[0] > 255) throw new IOException("Inappropriate format for this character sequence");
			out.writeByte(sequence[0]);
			break;
		case 3:
			if (sequence.length < 1 || sequence.length > 2) throw new IOException("Inappropriate format for this character sequence");
			if (sequence.length > 1) {
				if (sequence[0] < 0 || sequence[0] > 255) throw new IOException("Inappropriate format for this character sequence");
				if (sequence[1] < 0 || sequence[1] > 255) throw new IOException("Inappropriate format for this character sequence");
				out.writeByte(sequence[0]);
				out.writeByte(sequence[1]);
			} else {
				if (sequence[0] < 0 || sequence[0] > 65535) throw new IOException("Inappropriate format for this character sequence");
				out.writeShort(sequence[0]);
			}
			break;
		case 4:
			if (sequence.length != 1) throw new IOException("Inappropriate format for this character sequence");
			if (sequence[0] < 0 || sequence[0] > 65535) throw new IOException("Inappropriate format for this character sequence");
			out.writeShort(sequence[0]);
			break;
		case 5:
			if (sequence.length < 1 || sequence.length > 2) throw new IOException("Inappropriate format for this character sequence");
			if (sequence.length > 1) {
				if (sequence[0] < 0 || sequence[0] > 65535) throw new IOException("Inappropriate format for this character sequence");
				if (sequence[1] < 0 || sequence[1] > 65535) throw new IOException("Inappropriate format for this character sequence");
				out.writeShort(sequence[0]);
				out.writeShort(sequence[1]);
			} else {
				if (sequence[0] < 0 || sequence[0] > 0x1FFFFF) throw new IOException("Inappropriate format for this character sequence");
				out.writeInt(sequence[0]);
			}
			break;
		case 6:
			if (sequence.length != 1) throw new IOException("Inappropriate format for this character sequence");
			out.writeInt(sequence[0]);
			break;
		case 16:
			for (int i : sequence) if (i < 0 || i > 255) throw new IOException("Inappropriate format for this character sequence");
			out.writeByte(sequence.length);
			for (int j = 0; j < sequence.length; j++) {
				out.writeByte(sequence[j]);
			}
			break;
		case 17:
			for (int i : sequence) if (i < 0 || i > 65535) throw new IOException("Inappropriate format for this character sequence");
			out.writeByte(sequence.length);
			for (int j = 0; j < sequence.length; j++) {
				out.writeShort(sequence[j]);
			}
			break;
		case 18:
			out.writeByte(sequence.length);
			for (int j = 0; j < sequence.length; j++) {
				out.writeInt(sequence[j]);
			}
			break;
		default:
			throw new IOException("Invalid character sequence format: " + csFmt);
		}
	}
	
	public static CharacterSequence read(DataInput in, int i, int count, int csFmt) throws IOException {
		int tmp; int[] vcs;
		switch (csFmt) {
		case 1:
			return new CharacterSequence(i);
		case 2:
			return new CharacterSequence(in.readUnsignedByte());
		case 3:
			tmp = in.readUnsignedShort();
			if (tmp < 0x100) return new CharacterSequence(tmp);
			else return new CharacterSequence((tmp >> 8) & 0xFF, tmp & 0xFF);
		case 4:
			return new CharacterSequence(in.readUnsignedShort());
		case 5:
			tmp = in.readInt();
			if (tmp >= 0 && tmp < 0x200000) return new CharacterSequence(tmp);
			else return new CharacterSequence((tmp >> 16) & 0xFFFF, tmp & 0xFFFF);
		case 6:
			return new CharacterSequence(in.readInt());
		case 16:
			tmp = in.readUnsignedByte();
			vcs = new int[tmp];
			for (int j = 0; j < tmp; j++) {
				vcs[j] = in.readUnsignedByte();
			}
			return new CharacterSequence(vcs);
		case 17:
			tmp = in.readUnsignedByte();
			vcs = new int[tmp];
			for (int j = 0; j < tmp; j++) {
				vcs[j] = in.readUnsignedShort();
			}
			return new CharacterSequence(vcs);
		case 18:
			tmp = in.readUnsignedByte();
			vcs = new int[tmp];
			for (int j = 0; j < tmp; j++) {
				vcs[j] = in.readInt();
			}
			return new CharacterSequence(vcs);
		default:
			throw new IOException("Invalid character sequence format: " + csFmt);
		}
	}
}
