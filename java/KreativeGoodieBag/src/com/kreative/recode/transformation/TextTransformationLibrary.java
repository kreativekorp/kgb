package com.kreative.recode.transformation;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import com.kreative.recode.transformations.BOM;

public class TextTransformationLibrary {
	private SortedSet<String> txNames;
	private Map<String,TextTransformationFactory> nameToTx;
	private SortedSet<String> txFlags;
	private Map<String,TextTransformationFactory> flagToTx;
	
	public TextTransformationLibrary() {
		txNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		nameToTx = new HashMap<String,TextTransformationFactory>();
		txFlags = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		flagToTx = new HashMap<String,TextTransformationFactory>();
	}
	
	public TextTransformationLibrary loadInternal() {
		for (TextTransformationFactory tx : BOM.getTextTransformationFactories()) {
			add(tx);
		}
		return this;
	}
	
	public void add(TextTransformationFactory tx) {
		boolean duplicateName = false;
		boolean duplicateFlag = false;
		
		String name = tx.getName();
		txNames.add(name);
		name = normalize(name);
		duplicateName |= nameToTx.containsKey(name);
		nameToTx.put(name, tx);
		
		boolean first = true;
		for (String flag : tx.getFlags()) {
			if (first) { txFlags.add(flag); first = false; }
			duplicateFlag |= flagToTx.containsKey(flag);
			flagToTx.put(flag, tx);
		}
		
		if (duplicateName) System.err.println("Warning: Duplicate transformation name in " + tx);
		if (duplicateFlag) System.err.println("Warning: Duplicate transformation flag in " + tx);
	}
	
	public TextTransformationFactory getByName(String name) {
		return nameToTx.get(normalize(name));
	}
	
	public TextTransformationFactory getByFlag(String flag) {
		return flagToTx.get(flag);
	}
	
	public List<String> listByName() {
		List<String> names = new ArrayList<String>();
		names.addAll(txNames);
		return Collections.unmodifiableList(names);
	}
	
	public List<String> listByFlag() {
		List<String> flags = new ArrayList<String>();
		flags.addAll(txFlags);
		return Collections.unmodifiableList(flags);
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
