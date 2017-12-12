package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ByteSequence implements Comparable<ByteSequence> {
	private byte[] sequence;
	
	public static ByteSequence parseByteSequence(String s) {
		s = s.trim();
		String[] cs = s.split("\\s*[.,:;+-]\\s*");
		ByteSequence b = new ByteSequence();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i].startsWith("0x") || cs[i].startsWith("0X")) {
				b = b.concat(new ByteSequence(cs[i].substring(2).trim()));
			} else if (cs[i].startsWith("$")) {
				b = b.concat(new ByteSequence(cs[i].substring(1).trim()));
			} else {
				b = b.concat(new ByteSequence(cs[i]));
			}
		}
		return b;
	}
	
	public ByteSequence() {
		this.sequence = new byte[0];
	}
	
	public ByteSequence(byte a) {
		this.sequence = new byte[]{a};
	}
	
	public ByteSequence(byte a, byte b) {
		this.sequence = new byte[]{a,b};
	}
	
	public ByteSequence(byte a, byte b, byte c) {
		this.sequence = new byte[]{a,b,c};
	}
	
	public ByteSequence(byte a, byte b, byte c, byte d) {
		this.sequence = new byte[]{a,b,c,d};
	}
	
	public ByteSequence(byte[] b) {
		this.sequence = new byte[b.length];
		for (int i = 0; i < b.length; i++) {
			this.sequence[i] = b[i];
		}
	}
	
	public ByteSequence(byte[] b, int off, int len) {
		this.sequence = new byte[len];
		for (int ti = 0, bi = off; ti < len; ti++, bi++) {
			this.sequence[ti] = b[bi];
		}
	}
	
	public ByteSequence(byte[] a, byte[] b) {
		this.sequence = new byte[a.length + b.length];
		int si = 0;
		for (int i = 0; i < a.length; i++) {
			this.sequence[si++] = a[i];
		}
		for (int i = 0; i < b.length; i++) {
			this.sequence[si++] = b[i];
		}
	}
	
	public ByteSequence(byte[] a, int aoff, int alen, byte[] b, int boff, int blen) {
		this.sequence = new byte[alen + blen];
		int si = 0;
		for (int ti = 0, ai = aoff; ti < alen; ti++, ai++) {
			this.sequence[si++] = a[ai];
		}
		for (int ti = 0, bi = boff; ti < blen; ti++, bi++) {
			this.sequence[si++] = b[bi];
		}
	}
	
	public ByteSequence(String s) {
		ArrayList<Integer> a = new ArrayList<Integer>(s.length());
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (ch >= '0' && ch <= '9') a.add(ch - '0' + 0);
			if (ch >= 'A' && ch <= 'F') a.add(ch - 'A' + 10);
			if (ch >= 'a' && ch <= 'f') a.add(ch - 'a' + 10);
		}
		if ((a.size() & 1) != 0) {
			a.add(0, 0);
		}
		this.sequence = new byte[a.size() >> 1];
		for (int ai = 0, ti = 0; ai < a.size() && ti < this.sequence.length; ai += 2, ti++) {
			this.sequence[ti] = (byte)((a.get(ai).intValue() << 4) | a.get(ai+1).intValue());
		}
	}
	
	public byte[] toByteArray() {
		byte[] b = new byte[this.sequence.length];
		for (int i = 0; i < this.sequence.length; i++) {
			b[i] = this.sequence[i];
		}
		return b;
	}
	
	public byte[] toByteArray(int start, int end) {
		byte[] b = new byte[end-start];
		for (int bi = 0, ti = start; ti < end; bi++, ti++) {
			b[bi] = this.sequence[ti];
		}
		return b;
	}
	
	public byte byteAt(int i) {
		return this.sequence[i];
	}
	
	public int length() {
		return this.sequence.length;
	}
	
	public ByteSequence concat(ByteSequence bseq) {
		return new ByteSequence(this.sequence, bseq.sequence);
	}
	
	public ByteSequence concat(byte[] b) {
		return new ByteSequence(this.sequence, b);
	}
	
	public ByteSequence concat(byte[] b, int off, int len) {
		return new ByteSequence(this.sequence, 0, this.sequence.length, b, off, len);
	}
	
	public ByteSequence subsequence(int start, int end) {
		return new ByteSequence(this.sequence, start, end-start);
	}
	
	public boolean startsWith(ByteSequence other) {
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
	
	public int compareTo(ByteSequence other) {
		for (int i = 0; i < this.sequence.length && i < other.sequence.length; i++) {
			if (this.sequence[i] != other.sequence[i]) {
				return (this.sequence[i] & 0xFF) - (other.sequence[i] & 0xFF);
			}
		}
		return other.sequence.length - this.sequence.length;
	}
	
	public boolean equals(Object o) {
		if (o instanceof ByteSequence) {
			ByteSequence other = (ByteSequence)o;
			return Arrays.equals(this.sequence, other.sequence);
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return Arrays.hashCode(this.sequence);
	}
	
	private static final char[] TOSTRING_HEX = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < this.sequence.length; i++) {
			s.append(TOSTRING_HEX[(this.sequence[i] >> 4) & 0x0F]);
			s.append(TOSTRING_HEX[this.sequence[i] & 0x0F]);
		}
		return s.toString();
	}
	
	public void write(DataOutput out, int bsFmt) throws IOException {
		switch (bsFmt) {
		case 1:
			// indexed - don't write anything
			break;
		case 2:
			if (sequence.length != 1) throw new IOException("Inappropriate format for this byte sequence");
			out.writeByte(sequence[0]);
			break;
		case 3:
			if (sequence.length < 1 || sequence.length > 2) throw new IOException("Inappropriate format for this byte sequence");
			if (sequence.length > 1) {
				out.writeByte(sequence[0]);
				out.writeByte(sequence[1]);
			} else {
				out.writeByte(0);
				out.writeByte(sequence[0]);
			}
			break;
		case 4:
			if (sequence.length != 2) throw new IOException("Inappropriate format for this byte sequence");
			out.writeByte(sequence[0]);
			out.writeByte(sequence[1]);
			break;
		case 5:
			if (sequence.length < 1 || sequence.length > 4) throw new IOException("Inappropriate format for this byte sequence");
			if (sequence.length > 3) {
				out.writeByte(sequence[0]);
				out.writeByte(sequence[1]);
				out.writeByte(sequence[2]);
				out.writeByte(sequence[3]);
			} else if (sequence.length > 2) {
				out.writeByte(0);
				out.writeByte(sequence[0]);
				out.writeByte(sequence[1]);
				out.writeByte(sequence[2]);
			} else if (sequence.length > 1) {
				out.writeByte(0);
				out.writeByte(0);
				out.writeByte(sequence[0]);
				out.writeByte(sequence[1]);
			} else {
				out.writeByte(0);
				out.writeByte(0);
				out.writeByte(0);
				out.writeByte(sequence[0]);
			}
			break;
		case 6:
			if (sequence.length != 4) throw new IOException("Inappropriate format for this byte sequence");
			out.writeByte(sequence[0]);
			out.writeByte(sequence[1]);
			out.writeByte(sequence[2]);
			out.writeByte(sequence[3]);
			break;
		case 16:
			out.writeByte(sequence.length);
			for (int j = 0; j < sequence.length; j++) {
				out.writeByte(sequence[j]);
			}
			break;
		case 17:
			if ((sequence.length & 1) != 0) throw new IOException("Inappropriate format for this byte sequence");
			out.writeByte(sequence.length >> 1);
			for (int j = 0; j < sequence.length; j++) {
				out.writeByte(sequence[j]);
			}
			break;
		case 18:
			if ((sequence.length & 3) != 0) throw new IOException("Inappropriate format for this byte sequence");
			out.writeByte(sequence.length >> 2);
			for (int j = 0; j < sequence.length; j++) {
				out.writeByte(sequence[j]);
			}
			break;
		default:
			throw new IOException("Invalid byte sequence format: " + bsFmt);
		}
	}
	
	public static ByteSequence read(DataInput in, int i, int count, int bsFmt) throws IOException {
		int tmp; byte[] vbs;
		switch (bsFmt) {
		case 1:
			if (count <= 0x100) return new ByteSequence((byte)i);
			else if (count <= 0x10000) return new ByteSequence((byte)(i >> 8), (byte)i);
			else if (count <= 0x1000000) return new ByteSequence((byte)(i >> 16), (byte)(i >> 8), (byte)i);
			else return new ByteSequence((byte)(i >> 24), (byte)(i >> 16), (byte)(i >> 8), (byte)i);
		case 2:
			return new ByteSequence(in.readByte());
		case 3:
			tmp = in.readUnsignedShort();
			if (tmp < 0x100) return new ByteSequence((byte)tmp);
			else return new ByteSequence((byte)(tmp >> 8), (byte)tmp);
		case 4:
			vbs = new byte[2];
			in.readFully(vbs);
			return new ByteSequence(vbs);
		case 5:
			tmp = in.readInt();
			if (tmp >= 0 && tmp < 0x100) return new ByteSequence((byte)tmp);
			else if (tmp >= 0 && tmp < 0x10000) return new ByteSequence((byte)(tmp >> 8), (byte)tmp);
			else if (tmp >= 0 && tmp < 0x1000000) return new ByteSequence((byte)(tmp >> 16), (byte)(tmp >> 8), (byte)tmp);
			else return new ByteSequence((byte)(tmp >> 24), (byte)(tmp >> 16), (byte)(tmp >> 8), (byte)tmp);
		case 6:
			vbs = new byte[4];
			in.readFully(vbs);
			return new ByteSequence(vbs);
		case 16:
			tmp = in.readUnsignedByte();
			vbs = new byte[tmp];
			in.readFully(vbs);
			return new ByteSequence(vbs);
		case 17:
			tmp = in.readUnsignedByte() << 1;
			vbs = new byte[tmp];
			in.readFully(vbs);
			return new ByteSequence(vbs);
		case 18:
			tmp = in.readUnsignedByte() << 2;
			vbs = new byte[tmp];
			in.readFully(vbs);
			return new ByteSequence(vbs);
		default:
			throw new IOException("Invalid byte sequence format: " + bsFmt);
		}
	}
}
