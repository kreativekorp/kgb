package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class IntLookupTable {
	private int id;
	private TreeMap<Integer,Integer> map;
	private TreeSet<Integer> values;
	private boolean valuesNonUnique;
	
	public IntLookupTable(int id) {
		this.id = id;
		this.map = new TreeMap<Integer,Integer>();
		this.values = new TreeSet<Integer>();
		this.valuesNonUnique = false;
	}
	
	public int id() {
		return id;
	}
	
	public void put(int key, int value) {
		if (values.contains(value)) {
			valuesNonUnique = true;
		}
		map.put(key, value);
		values.add(value);
	}
	
	public int get(int key) {
		return map.get(key);
	}
	
	public boolean valuesAreNonUnique() {
		return valuesNonUnique;
	}
	
	public int minKey() {
		return map.firstKey();
	}
	
	public int maxKey() {
		return map.lastKey();
	}
	
	public int minValue() {
		return values.first();
	}
	
	public int maxValue() {
		return values.last();
	}
	
	public int count() {
		return map.size();
	}
	
	public SortedMap<Integer,Integer> map() {
		return Collections.unmodifiableSortedMap(map);
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
	
	public int optimalKeyFormat() {
		int mink = map.firstKey();
		int maxk = map.lastKey();
		if (mink < 0) {
			if (mink >= Byte.MIN_VALUE && maxk <= Byte.MAX_VALUE) return 2;
			else if (mink >= Short.MIN_VALUE && maxk <= Short.MAX_VALUE) return 4;
			else return 6;
		} else if (exp2or3exp2bound(maxk) == map.size()) {
			return 1;
		} else {
			if (maxk < 0x100) return 3;
			else if (maxk < 0x10000) return 5;
			else return 7;
		}
	}
	
	public int optimalValueFormat() {
		int minv = values.first();
		int maxv = values.last();
		if (minv < 0) {
			if (minv >= Byte.MIN_VALUE && maxv <= Byte.MAX_VALUE) return 2;
			else if (minv >= Short.MIN_VALUE && maxv <= Short.MAX_VALUE) return 4;
			else return 6;
		} else {
			if (maxv < 0x100) return 3;
			else if (maxv < 0x10000) return 5;
			else return 7;
		}
	}
	
	public void write(DataOutput out) throws IOException {
		int keyFmt = optimalKeyFormat();
		int valFmt = optimalValueFormat();
		out.writeByte(keyFmt);
		out.writeByte(valFmt);
		out.writeShort(valuesNonUnique ? 0x18 : 0x10);
		out.writeInt(id);
		if (keyFmt == 1) {
			int tmaxk = exp2or3exp2bound(map.lastKey());
			out.writeInt(tmaxk);
			for (int i = 0; i < tmaxk; i++) {
				int v = map.get(i);
				switch (valFmt) {
				case 2: case 3: out.writeByte(v); break;
				case 4: case 5: out.writeShort(v); break;
				case 6: case 7: out.writeInt(v); break;
				}
			}
		} else {
			out.writeInt(map.size());
			for (Map.Entry<Integer,Integer> e : map.entrySet()) {
				int k = e.getKey();
				int v = e.getValue();
				switch (keyFmt) {
				case 2: case 3: out.writeByte(k); break;
				case 4: case 5: out.writeShort(k); break;
				case 6: case 7: out.writeInt(k); break;
				}
				switch (valFmt) {
				case 2: case 3: out.writeByte(v); break;
				case 4: case 5: out.writeShort(v); break;
				case 6: case 7: out.writeInt(v); break;
				}
			}
		}
	}
	
	public static IntLookupTable read(DataInput in) throws IOException {
		int keyFmt = in.readUnsignedByte();
		int valFmt = in.readUnsignedByte();
		in.readUnsignedShort();
		int id = in.readInt();
		int count = in.readInt();
		IntLookupTable table = new IntLookupTable(id);
		for (int i = 0; i < count; i++) {
			int k, v;
			switch (keyFmt) {
			case 1: k = i; break;
			case 2: k = in.readByte(); break;
			case 3: k = in.readUnsignedByte(); break;
			case 4: k = in.readShort(); break;
			case 5: k = in.readUnsignedShort(); break;
			case 6: k = in.readInt(); break;
			case 7: k = in.readInt(); break;
			default: throw new IOException("Invalid key format: " + keyFmt);
			}
			switch (valFmt) {
			case 1: v = i; break;
			case 2: v = in.readByte(); break;
			case 3: v = in.readUnsignedByte(); break;
			case 4: v = in.readShort(); break;
			case 5: v = in.readUnsignedShort(); break;
			case 6: v = in.readInt(); break;
			case 7: v = in.readInt(); break;
			default: throw new IOException("Invalid value format: " + valFmt);
			}
			table.put(k, v);
		}
		return table;
	}
	
	private static int parseInt(String s, int i, int base, List<Integer> components) {
		if (i < s.length()) {
			int ch = s.codePointAt(i);
			int d = Character.getNumericValue(ch);
			if (d >= 0 && d < base) {
				i += (ch < 0x10000) ? 1 : 2;
				int v = d;
				while (i < s.length()) {
					ch = s.codePointAt(i);
					d = Character.getNumericValue(ch);
					if (d < 0) {
						break;
					} else if (d < base) {
						i += (ch < 0x10000) ? 1 : 2;
						v = v * base + d;
					} else {
						throw new NumberFormatException(s);
					}
				}
				components.add(v);
				return i;
			}
		}
		throw new NumberFormatException(s);
	}
	
	public static int parseInt(String s) {
		List<Integer> components = new ArrayList<Integer>();
		int i = 0;
		while (i < s.length()) {
			int ch = s.codePointAt(i);
			if (ch == '\"' || ch == '\'' || ch == '`') {
				i++;
				while (i < s.length()) {
					int v = s.codePointAt(i);
					if (v == ch) {
						break;
					} else {
						i += (v < 0x10000) ? 1 : 2;
						components.add(v);
					}
				}
				if (i < s.length()) {
					i++;
				} else {
					throw new NumberFormatException(s);
				}
			} else if (ch == 'U' || ch == 'u') {
				i++;
				if (i < s.length()) {
					ch = s.codePointAt(i);
					if (ch == '+' || ch == '-') {
						i++;
					}
				}
				i = parseInt(s, i, 16, components);
			} else if (ch == '$') {
				i = parseInt(s, i+1, 16, components);
			} else if (ch == '%') {
				i = parseInt(s, i+1, 2, components);
			} else if (ch == '0') {
				i++;
				int base = 8;
				if (i < s.length()) {
					ch = s.codePointAt(i);
					switch (ch) {
					case 'V': case 'v': base = 20; i++; break;
					case 'H': case 'h': base = 16; i++; break;
					case 'X': case 'x': base = 16; i++; break;
					case 'D': case 'd': base = 12; i++; break;
					case 'O': case 'o': base = 8; i++; break;
					case 'B': case 'b': base = 2; i++; break;
					default: i--; break;
					}
				} else {
					i--;
				}
				i = parseInt(s, i, base, components);
			} else {
				int d = Character.getNumericValue(ch);
				if (d >= 0 && d < 10) {
					i = parseInt(s, i, 10, components);
				} else if (Character.isLetterOrDigit(ch)) {
					throw new NumberFormatException(s);
				} else {
					i += (ch < 0x10000) ? 1 : 2;
				}
			}
		}
		switch (components.size()) {
		case 1: return components.get(0);
		case 2: return ((components.get(0) & 0xFFFF) << 16) | (components.get(1) & 0xFFFF);
		case 3: return ((components.get(0) & 0xFFFF) << 16) | ((components.get(1) & 0xFF) << 8) | (components.get(2) & 0xFF);
		case 4: return ((components.get(0) & 0xFF) << 24) | ((components.get(1) & 0xFF) << 16) | ((components.get(2) & 0xFF) << 8) | (components.get(3) & 0xFF);
		default: throw new NumberFormatException(s);
		}
	}
}
