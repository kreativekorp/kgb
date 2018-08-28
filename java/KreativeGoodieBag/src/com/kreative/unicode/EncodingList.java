package com.kreative.unicode;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;

public class EncodingList extends AbstractList<EncodingTable> {
	private static final String[] ENCODING_NAMES = new String[]{
		"CP037", "CP437", "CP500", "CP737", "CP775", "CP850", "CP852", "CP855",
		"CP857", "CP860", "CP861", "CP862", "CP863", "CP864", "CP865", "CP866",
		"CP869", "CP874", "CP875", "CP1026", "CP1250", "CP1251", "CP1252",
		"CP1253", "CP1254", "CP1255", "CP1256", "CP1257", "CP1258",
		"ISO 8859-1", "ISO 8859-2", "ISO 8859-3", "ISO 8859-4", "ISO 8859-5",
		"ISO 8859-6", "ISO 8859-7", "ISO 8859-8", "ISO 8859-9", "ISO 8859-10",
		"ISO 8859-11", "ISO 8859-13", "ISO 8859-14", "ISO 8859-15", "ISO 8859-16",
		"Kreative SuperLatin", "Kreative SuperLatin C0",
		"Kreative SuperRoman", "Kreative SuperRoman C0",
		"Kreative Super437", "Kreative Super437 C0",
		"MacCeltic", "MacCentralEuropean", "MacCroatian", "MacCyrillic",
		"MacDingbats", "MacGaelic", "MacGreek", "MacIcelandic", "MacInuit",
		"MacRoman", "MacRomanian", "MacTurkish", "FZX"
	};
	
	private static EncodingList instance = null;
	
	public static EncodingList instance() {
		if (instance == null) instance = new EncodingList();
		return instance;
	}
	
	private final List<EncodingTable> encodings;
	private final Map<String,EncodingTable> encodingMap;
	
	private EncodingList() {
		List<EncodingTable> encodings = new ArrayList<EncodingTable>();
		Map<String,EncodingTable> encodingMap = new HashMap<String,EncodingTable>();
		for (String fileName : ENCODING_NAMES) {
			int[] codePoints = new int[256];
			for (int i = 0; i < 256; i++) {
				codePoints[i] = (i < 32 || (i >= 127 && i < 160)) ? i : -1;
			}
			Scanner scan = new Scanner(EncodingList.class.getResourceAsStream(fileName.replaceAll("\\s+", "_") + ".txt"));
			while (scan.hasNextLine()) {
				String line = scan.nextLine().trim();
				if (line.length() > 0 && line.charAt(0) != '#') {
					String[] f = line.split("\\s+");
					if (f.length >= 2) {
						String is = f[0].trim().toLowerCase();
						String cps = f[1].trim().toLowerCase();
						if (is.startsWith("0x") && cps.startsWith("0x")) {
							try {
								int i = Integer.parseInt(is.substring(2), 16);
								int cp = Integer.parseInt(cps.substring(2), 16);
								if (i >= 0 && i < 256) codePoints[i] = cp;
							} catch (NumberFormatException e) {
								continue;
							}
						}
					}
				}
			}
			scan.close();
			EncodingTable e = new EncodingTable(codePoints, fileName);
			encodings.add(e);
			String nn = fileName.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
			encodingMap.put(nn, e);
		}
		this.encodings = Collections.unmodifiableList(encodings);
		this.encodingMap = Collections.unmodifiableMap(encodingMap);
	}
	
	public boolean contains(String name) {
		String nn = name.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		return encodingMap.containsKey(nn);
	}
	
	public boolean contains(Object o) {
		return encodings.contains(o);
	}
	
	public boolean containsAll(Collection<?> c) {
		return encodings.containsAll(c);
	}
	
	public EncodingTable get(String name) {
		String nn = name.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		return encodingMap.get(nn);
	}
	
	public EncodingTable get(int index) {
		return encodings.get(index);
	}
	
	public int indexOf(String name) {
		String nn = name.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		return encodingMap.containsKey(nn) ? encodings.indexOf(encodingMap.get(nn)) : -1;
	}
	
	public int indexOf(Object o) {
		return encodings.indexOf(o);
	}
	
	public boolean isEmpty() {
		return encodings.isEmpty();
	}
	
	public Iterator<EncodingTable> iterator() {
		return encodings.iterator();
	}
	
	public int lastIndexOf(String name) {
		String nn = name.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
		return encodingMap.containsKey(nn) ? encodings.lastIndexOf(encodingMap.get(nn)) : -1;
	}
	
	public int lastIndexOf(Object o) {
		return encodings.lastIndexOf(o);
	}
	
	public ListIterator<EncodingTable> listIterator() {
		return encodings.listIterator();
	}
	
	public ListIterator<EncodingTable> listIterator(int index) {
		return encodings.listIterator(index);
	}
	
	public int size() {
		return encodings.size();
	}
	
	public List<EncodingTable> subList(int fromIndex, int toIndex) {
		return encodings.subList(fromIndex, toIndex);
	}
	
	public Object[] toArray() {
		return encodings.toArray();
	}
	
	public <T> T[] toArray(T[] a) {
		return encodings.toArray(a);
	}
}
