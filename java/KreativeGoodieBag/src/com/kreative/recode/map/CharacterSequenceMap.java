package com.kreative.recode.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CharacterSequenceMap {
	private String name;
	private final Set<String> aliases;
	private String description;
	private final Map<List<Integer>, String> inputMap;
	private final Map<String, List<Integer>> outputMap;
	
	public CharacterSequenceMap() {
		this.name = "";
		this.aliases = new HashSet<String>();
		this.description = "";
		this.inputMap = new HashMap<List<Integer>, String>();
		this.outputMap = new HashMap<String, List<Integer>>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (name != null) {
			this.name = name;
		}
	}
	
	public Set<String> getAliases() {
		return Collections.unmodifiableSet(aliases);
	}
	
	public void addAlias(String name) {
		if (name != null) {
			this.aliases.add(name);
		}
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		if (description != null) {
			this.description = description;
		}
	}
	
	public void addMapping(List<Integer> codePoints, String name) {
		if (!inputMap.containsKey(codePoints)) {
			inputMap.put(codePoints, name);
		}
		if ((name != null) && !outputMap.containsKey(name)) {
			outputMap.put(name, codePoints);
		}
	}
	
	public Map<List<Integer>, String> inputMap() {
		return Collections.unmodifiableMap(inputMap);
	}
	
	public Map<String, List<Integer>> outputMap() {
		return Collections.unmodifiableMap(outputMap);
	}
	
	public boolean isEmpty() {
		return inputMap.isEmpty() && outputMap.isEmpty();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void read(Scanner scanner) {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			if ((line.length() > 0) && !line.startsWith("#")) {
				String[] fields = line.split("\t+");
				String field0 = ((fields.length > 0) ? fields[0].trim() : null);
				String field1 = ((fields.length > 1) ? fields[1].trim() : null);
				String field2 = ((fields.length > 2) ? fields[2].trim() : null);
				if (field0.equalsIgnoreCase("NAME")) {
					setName(field1);
				} else if (field0.equalsIgnoreCase("ALIAS")) {
					addAlias(field1);
				} else if (field0.equalsIgnoreCase("COMMENT")) {
					setDescription(field1);
				} else if (field0.equalsIgnoreCase("MAP")) {
					if (field1 != null && field1.length() > 0) {
						String[] parts = field1.split("\\s+");
						List<Integer> codePoints = new ArrayList<Integer>();
						for (String part : parts) {
							if (part.length() > 0) {
								if (part.length() > 2 && part.startsWith("'") && part.endsWith("'")) {
									codePoints.addAll(strToCp(part.substring(1, part.length() - 1)));
								} else if (part.length() > 2 && part.startsWith("\"") && part.endsWith("\"")) {
									codePoints.addAll(strToCp(part.substring(1, part.length() - 1)));
								} else try {
									int codePoint = Integer.parseInt(part, 16);
									codePoints.add(codePoint);
								} catch (NumberFormatException nfe) {
									System.err.println("Warning: Invalid particle in mapping: " + part);
								}
							}
						}
						if (!codePoints.isEmpty()) {
							addMapping(codePoints, field2);
						}
					}
				} else try {
					int codePoint = Integer.parseInt(field0, 16);
					List<Integer> codePoints = new ArrayList<Integer>();
					codePoints.add(codePoint);
					addMapping(codePoints, field1);
				} catch (NumberFormatException nfe) {
					// Ignored.
				}
			}
		}
	}
	
	private static List<Integer> strToCp(String string) {
		List<Integer> codePoints = new ArrayList<Integer>();
		int i = 0, m = string.length();
		while (i < m) {
			int codePoint = string.codePointAt(i);
			codePoints.add(codePoint);
			i += Character.charCount(codePoint);
		}
		return codePoints;
	}
}
