package com.kreative.recode.transformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.kreative.recode.map.CharacterSequenceMap;
import com.kreative.recode.map.CharacterSequencePrefixTrie;
import com.kreative.recode.map.CharacterSequencePrefixTrie.Node;
import com.kreative.recode.transformation.TextTransformation;

public class SequenceRemapTransformation extends TextTransformation {
	private final CharacterSequencePrefixTrie<List<Integer>> trie;
	private Node<List<Integer>> current;
	
	public SequenceRemapTransformation(Map<List<Integer>,List<Integer>> map) {
		this.trie = new CharacterSequencePrefixTrie<List<Integer>>();
		this.trie.putAll(map);
		this.current = this.trie.getRoot();
	}
	
	public <T> SequenceRemapTransformation(Map<List<Integer>,T> inputMap, Map<T,List<Integer>> outputMap) {
		this.trie = new CharacterSequencePrefixTrie<List<Integer>>();
		for (Map.Entry<List<Integer>,T> e : inputMap.entrySet()) {
			List<Integer> input = e.getKey();
			T interchange = e.getValue();
			List<Integer> output;
			if (interchange == null) {
				output = new ArrayList<Integer>();
				output.add(0xFFFD);
			} else if (outputMap.containsKey(interchange)) {
				output = outputMap.get(interchange);
			} else {
				output = new ArrayList<Integer>();
				output.add(0xFFFD);
			}
			this.trie.put(input, output);
		}
		this.current = this.trie.getRoot();
	}
	
	public SequenceRemapTransformation(Collection<CharacterSequenceMap> inputMaps, Collection<CharacterSequenceMap> outputMaps) {
		Map<List<Integer>,String> inputMap = new HashMap<List<Integer>,String>();
		Map<String,List<Integer>> outputMap = new HashMap<String,List<Integer>>();
		for (CharacterSequenceMap map : inputMaps) {
			for (Map.Entry<List<Integer>,String> e : map.inputMap().entrySet()) {
				if (inputMap.containsKey(e.getKey())) {
					String oldValue = inputMap.get(e.getKey());
					String newValue = e.getValue();
					if (!oldValue.equals(newValue)) {
						throw new IllegalArgumentException("Error: Input mappings overlap for Remap.");
					}
				} else {
					inputMap.put(e.getKey(), e.getValue());
				}
			}
		}
		for (CharacterSequenceMap map : outputMaps) {
			for (Map.Entry<String,List<Integer>> e : map.outputMap().entrySet()) {
				if (outputMap.containsKey(e.getKey())) {
					List<Integer> oldValue = outputMap.get(e.getKey());
					List<Integer> newValue = e.getValue();
					if (!oldValue.equals(newValue)) {
						throw new IllegalArgumentException("Error: Output mappings overlap for Remap.");
					}
				} else {
					outputMap.put(e.getKey(), e.getValue());
				}
			}
		}
		this.trie = new CharacterSequencePrefixTrie<List<Integer>>();
		for (Map.Entry<List<Integer>,String> e : inputMap.entrySet()) {
			List<Integer> input = e.getKey();
			String interchange = e.getValue();
			List<Integer> output;
			if (interchange == null) {
				output = new ArrayList<Integer>();
				output.add(0xFFFD);
			} else if (outputMap.containsKey(interchange)) {
				output = outputMap.get(interchange);
			} else {
				output = new ArrayList<Integer>();
				output.add(0xFFFD);
			}
			this.trie.put(input, output);
		}
		this.current = this.trie.getRoot();
	}
	
	@Override
	public String getName() {
		return "Remap";
	}
	
	@Override
	public String getDescription() {
		return "Remaps sequences of characters, often from one set of code points to another.";
	}
	
	@Override
	protected void startTransformation() {
		this.current = this.trie.getRoot();
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		if (current.hasChild(codePoint)) {
			current = current.getChild(codePoint);
			if (!current.hasChildren()) {
				List<Integer> danglingCodePoints = new ArrayList<Integer>();
				if (writeValue(danglingCodePoints)) {
					for (int danglingCodePoint : danglingCodePoints) {
						transformCodePoint(danglingCodePoint);
					}
				} else if (!danglingCodePoints.isEmpty()) {
					append(danglingCodePoints.remove(0));
					for (int danglingCodePoint : danglingCodePoints) {
						transformCodePoint(danglingCodePoint);
					}
				}
			}
		} else {
			List<Integer> danglingCodePoints = new ArrayList<Integer>();
			if (writeValue(danglingCodePoints)) {
				for (int danglingCodePoint : danglingCodePoints) {
					transformCodePoint(danglingCodePoint);
				}
				transformCodePoint(codePoint);
			} else if (!danglingCodePoints.isEmpty()) {
				append(danglingCodePoints.remove(0));
				for (int danglingCodePoint : danglingCodePoints) {
					transformCodePoint(danglingCodePoint);
				}
				transformCodePoint(codePoint);
			} else {
				append(codePoint);
			}
		}
	}
	
	@Override
	protected void stopTransformation() {
		while (true) {
			List<Integer> danglingCodePoints = new ArrayList<Integer>();
			if (writeValue(danglingCodePoints)) {
				if (danglingCodePoints.isEmpty()) {
					return;
				} else {
					for (int danglingCodePoint : danglingCodePoints) {
						transformCodePoint(danglingCodePoint);
					}
					continue;
				}
			} else if (!danglingCodePoints.isEmpty()) {
				append(danglingCodePoints.remove(0));
				if (danglingCodePoints.isEmpty()) {
					return;
				} else {
					for (int danglingCodePoint : danglingCodePoints) {
						transformCodePoint(danglingCodePoint);
					}
					continue;
				}
			} else {
				return;
			}
		}
	}
	
	private boolean writeValue(List<Integer> danglingCodePoints) {
		while (true) {
			if (current.hasValue()) {
				append(current.getValue());
				current = trie.getRoot();
				return true;
			} else if (current.hasParent()) {
				danglingCodePoints.add(0, current.getParentKey());
				current = current.getParent();
				continue;
			} else {
				return false;
			}
		}
	}
}
