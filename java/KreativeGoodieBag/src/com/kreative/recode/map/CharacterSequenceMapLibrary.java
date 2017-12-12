package com.kreative.recode.map;

import java.io.File;
import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import com.kreative.recode.maps.BOM;

public class CharacterSequenceMapLibrary {
	private SortedSet<String> mapNames;
	private Map<String,CharacterSequenceMap> nameToMap;
	
	public CharacterSequenceMapLibrary() {
		mapNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		nameToMap = new HashMap<String,CharacterSequenceMap>();
	}
	
	public CharacterSequenceMapLibrary loadInternal() {
		for (CharacterSequenceMap map : BOM.getCharacterSequenceMaps()) {
			add(map);
		}
		return this;
	}
	
	public void add(CharacterSequenceMap map) {
		boolean duplicateName = false;
		
		String name = map.getName();
		mapNames.add(name);
		name = normalize(name);
		duplicateName |= nameToMap.containsKey(name);
		nameToMap.put(name, map);
		
		for (String alias : map.getAliases()) {
			alias = normalize(alias);
			duplicateName |= nameToMap.containsKey(alias);
			nameToMap.put(alias, map);
		}
		
		if (duplicateName) System.err.println("Warning: Duplicate map name in " + map + ".");
	}
	
	public CharacterSequenceMap get(String name) {
		return nameToMap.get(normalize(name));
	}
	
	public List<String> list() {
		List<String> names = new ArrayList<String>();
		names.addAll(mapNames);
		return Collections.unmodifiableList(names);
	}
	
	public void load(File file) throws IOException {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				if (!child.getName().startsWith(".")) {
					if (child.isDirectory() || child.getName().toLowerCase().endsWith(".map")) {
						load(child);
					}
				}
			}
		} else {
			CharacterSequenceMap map = new CharacterSequenceMap();
			Scanner scanner = new Scanner(file, "UTF-8");
			map.read(scanner);
			scanner.close();
			if (!map.isEmpty()) add(map);
		}
	}
	
	private static String normalize(String name) {
		StringBuffer normalizedName = new StringBuffer();
		CharacterIterator nameIter = new StringCharacterIterator(name);
		for (char ch = nameIter.first(); ch != CharacterIterator.DONE; ch = nameIter.next()) {
			if (Character.isLetterOrDigit(ch)) {
				normalizedName.append(Character.toLowerCase(ch));
			}
		}
		return normalizedName.toString();
	}
}
