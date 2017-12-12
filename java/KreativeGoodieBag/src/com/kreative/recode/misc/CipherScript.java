package com.kreative.recode.misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum CipherScript {
	LATIN("Latin",
			ul('A','a'), ul('B','b'), ul('C','c'), ul('D','d'), ul('E','e'), ul('F','f'), ul('G','g'),
			ul('H','h'), ul('I','i'), ul('J','j'), ul('K','k'), ul('L','l'), ul('M','m'),
			ul('N','n'), ul('O','o'), ul('P','p'), ul('Q','q'), ul('R','r'), ul('S','s'), ul('T','t'),
			ul('U','u'), ul('V','v'), ul('W','w'), ul('X','x'), ul('Y','y'), ul('Z','z')
	),
	GREEK("Greek",
			ul(0x391,0x3B1), ul(0x392,0x3B2), ul(0x393,0x3B3), ul(0x394,0x3B4),
			ul(0x395,0x3B5), ul(0x396,0x3B6), ul(0x397,0x3B7), ul(0x398,0x3B8),
			ul(0x399,0x3B9), ul(0x39A,0x3BA), ul(0x39B,0x3BB), ul(0x39C,0x3BC),
			ul(0x39D,0x3BD), ul(0x39E,0x3BE), ul(0x39F,0x3BF), ul(0x3A0,0x3C0),
			ul(0x3A1,0x3C1), ufm(0x3A3,0x3C2,0x3C3), ul(0x3A4,0x3C4), ul(0x3A5,0x3C5),
			ul(0x3A6,0x3C6), ul(0x3A7,0x3C7), ul(0x3A8,0x3C8), ul(0x3A9,0x3C9)
	),
	CYRILLIC("Cyrillic",
			ul(0x410,0x430), ul(0x411,0x431), ul(0x412,0x432), ul(0x413,0x433),
			ul(0x414,0x434), ul(0x415,0x435), ul(0x416,0x436), ul(0x417,0x437),
			ul(0x418,0x438), ul(0x419,0x439), ul(0x41A,0x43A), ul(0x41B,0x43B),
			ul(0x41C,0x43C), ul(0x41D,0x43D), ul(0x41E,0x43E), ul(0x41F,0x43F),
			ul(0x420,0x440), ul(0x421,0x441), ul(0x422,0x442), ul(0x423,0x443),
			ul(0x424,0x444), ul(0x425,0x445), ul(0x426,0x446), ul(0x427,0x447),
			ul(0x428,0x448), ul(0x429,0x449), ul(0x42A,0x44A), ul(0x42B,0x44B),
			ul(0x42C,0x44C), ul(0x42D,0x44D), ul(0x42E,0x44E), ul(0x42F,0x44F)
	),
	ARMENIAN("Armenian",
			ul(0x531,0x561), ul(0x532,0x562), ul(0x533,0x563), ul(0x534,0x564),
			ul(0x535,0x565), ul(0x536,0x566), ul(0x537,0x567), ul(0x538,0x568),
			ul(0x539,0x569), ul(0x53A,0x56A), ul(0x53B,0x56B), ul(0x53C,0x56C),
			ul(0x53D,0x56D), ul(0x53E,0x56E), ul(0x53F,0x56F), ul(0x540,0x570),
			ul(0x541,0x571), ul(0x542,0x572), ul(0x543,0x573), ul(0x544,0x574),
			ul(0x545,0x575), ul(0x546,0x576), ul(0x547,0x577), ul(0x548,0x578),
			ul(0x549,0x579), ul(0x54A,0x57A), ul(0x54B,0x57B), ul(0x54C,0x57C),
			ul(0x54D,0x57D), ul(0x54E,0x57E), ul(0x54F,0x57F), ul(0x550,0x580),
			ul(0x551,0x581), ul(0x552,0x582), ul(0x553,0x583), ul(0x554,0x584),
			ul(0x555,0x585), ul(0x556,0x586)
	),
	HEBREW("Hebrew",
			l(0x5D0), l(0x5D1), l(0x5D2), l(0x5D3), l(0x5D4), l(0x5D5), l(0x5D6), l(0x5D7),
			l(0x5D8), l(0x5D9), fm(0x5DA,0x5DB), l(0x5DC), fm(0x5DD,0x5DE), fm(0x5DF,0x5E0), l(0x5E1),
			l(0x5E2), fm(0x5E3,0x5E4), fm(0x5E5,0x5E6), l(0x5E7), l(0x5E8), l(0x5E9), l(0x5EA)
	);
	
	private final String name;
	private final int size;
	private final List<CipherLetter> letters = new ArrayList<CipherLetter>();
	private final Set<Integer> codePoints = new HashSet<Integer>();
	private final Map<Integer,Integer> toOrdinal = new HashMap<Integer,Integer>();
	private final Map<Integer,Boolean> toCase = new HashMap<Integer,Boolean>();
	
	private CipherScript(String name, CipherLetter... l) {
		this.name = name;
		this.size = l.length;
		for (int i = 0; i < l.length; i++) {
			letters.add(l[i]);
			codePoints.add(l[i].finalUpper);
			codePoints.add(l[i].medialUpper);
			codePoints.add(l[i].finalLower);
			codePoints.add(l[i].medialLower);
			toOrdinal.put(l[i].finalUpper, i);
			toOrdinal.put(l[i].medialUpper, i);
			toOrdinal.put(l[i].finalLower, i);
			toOrdinal.put(l[i].medialLower, i);
			toCase.put(l[i].finalUpper, true);
			toCase.put(l[i].medialUpper, true);
			toCase.put(l[i].finalLower, false);
			toCase.put(l[i].medialLower, false);
		}
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public int size() {
		return size;
	}
	
	public CipherLetter getLetter(int ordinal) {
		return letters.get(ordinal);
	}
	
	public boolean contains(int codePoint) {
		return codePoints.contains(codePoint);
	}
	
	public int getOrdinal(int codePoint) {
		return toOrdinal.get(codePoint);
	}
	
	public boolean isUpperCase(int codePoint) {
		return toCase.get(codePoint);
	}
	
	public static final String listScripts() {
		StringBuffer sb = new StringBuffer();
		CipherScript[] scripts = values();
		for (int i = 0; i < scripts.length; i++) {
			if (i > 0) {
				if (i < scripts.length - 1) {
					sb.append(", ");
				} else {
					sb.append(", or ");
				}
			}
			sb.append(scripts[i].toString());
		}
		return sb.toString();
	}
	
	private static final CipherLetter ul(int upperCase, int lowerCase) {
		return new CipherLetter(upperCase, upperCase, lowerCase, lowerCase);
	}
	private static final CipherLetter ufm(int upperCase, int finalLower, int medialLower) {
		return new CipherLetter(upperCase, upperCase, finalLower, medialLower);
	}
	private static final CipherLetter l(int codePoint) {
		return new CipherLetter(codePoint, codePoint, codePoint, codePoint);
	}
	private static final CipherLetter fm(int finalForm, int medialForm) {
		return new CipherLetter(finalForm, medialForm, finalForm, medialForm);
	}
}
