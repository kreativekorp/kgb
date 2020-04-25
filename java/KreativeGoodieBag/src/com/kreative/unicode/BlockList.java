package com.kreative.unicode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

public class BlockList extends AbstractList<Block> {
	private static final String[] BLOCK_FILES = new String[]{
		"BlocksRoadmap.txt",
		"Blocks.txt",
		"BlocksUCSUR.txt",
		"BlocksVendors.txt"
	};
	
	private static BlockList instance = null;
	
	public static BlockList instance() {
		if (instance == null) instance = new BlockList();
		return instance;
	}
	
	private final List<Block> blocks;
	
	private BlockList() {
		List<Block> blocks = new ArrayList<Block>();
		for (String fileName : BLOCK_FILES) {
			read(BlockList.class.getResourceAsStream(fileName), blocks);
		}
		read(UnicodeUtils.getTableDirectory("Blocks"), blocks);
		Collections.sort(blocks);
		this.blocks = Collections.unmodifiableList(blocks);
	}
	
	private static void read(File d, List<Block> blocks) {
		for (File f : d.listFiles()) {
			if (f.getName().startsWith(".") || f.getName().endsWith("\r")) {
				continue;
			} else if (f.isDirectory()) {
				read(f, blocks);
			} else try {
				read(new FileInputStream(f), blocks);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
	
	private static void read(InputStream in, List<Block> blocks) {
		Scanner scan = new Scanner(in);
		while (scan.hasNextLine()) {
			String line = scan.nextLine().trim();
			if (line.length() > 0 && line.charAt(0) != '#') {
				String[] f1 = line.split(";");
				if (f1.length == 2) {
					String blockName = f1[1].trim();
					String[] f2 = f1[0].split("[.]+");
					if (f2.length == 2) {
						try {
							int first = Integer.parseInt(f2[0], 16);
							int last = Integer.parseInt(f2[1], 16);
							blocks.add(new Block(first, last, blockName));
						} catch (NumberFormatException e) {
							continue;
						}
					}
				}
			}
		}
		scan.close();
	}
	
	public boolean contains(Object o) {
		return blocks.contains(o);
	}
	
	public boolean containsAll(Collection<?> c) {
		return blocks.containsAll(c);
	}
	
	public Block get(int index) {
		return blocks.get(index);
	}
	
	public int indexOf(Object o) {
		return blocks.indexOf(o);
	}
	
	public boolean isEmpty() {
		return blocks.isEmpty();
	}
	
	public Iterator<Block> iterator() {
		return blocks.iterator();
	}
	
	public int lastIndexOf(Object o) {
		return blocks.lastIndexOf(o);
	}
	
	public ListIterator<Block> listIterator() {
		return blocks.listIterator();
	}
	
	public ListIterator<Block> listIterator(int index) {
		return blocks.listIterator(index);
	}
	
	public int size() {
		return blocks.size();
	}
	
	public List<Block> subList(int fromIndex, int toIndex) {
		return blocks.subList(fromIndex, toIndex);
	}
	
	public Object[] toArray() {
		return blocks.toArray();
	}
	
	public <T> T[] toArray(T[] a) {
		return blocks.toArray(a);
	}
}
