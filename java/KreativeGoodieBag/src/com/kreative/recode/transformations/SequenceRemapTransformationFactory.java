package com.kreative.recode.transformations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.map.CharacterSequenceMap;
import com.kreative.recode.map.CharacterSequenceMapLibrary;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class SequenceRemapTransformationFactory extends TextTransformationFactory {
	private CharacterSequenceMapLibrary mapLib;
	
	public SequenceRemapTransformationFactory() {
		this(new CharacterSequenceMapLibrary().loadInternal());
	}
	
	public SequenceRemapTransformationFactory(CharacterSequenceMapLibrary mapLib) {
		this.mapLib = mapLib;
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
	public Collection<String> getFlags() {
		return Arrays.asList("-rm", "--rm", "-remap", "--remap");
	}
	
	@Override
	public int getArgumentCount() {
		return 2;
	}
	
	@Override
	public List<String> getArgumentNames() {
		return Arrays.asList("inputmaps", "outputmaps");
	}
	
	@Override
	public List<String> getArgumentDescriptions() {
		return Arrays.asList("A comma-delimited list of mappings to apply on input.", "A comma-delimited list of mappings to apply on output.");
	}
	
	public CharacterSequenceMapLibrary getMapLibrary() {
		return mapLib;
	}
	
	@Override
	public TextTransformation createTransformation(List<String> args) {
		String inputMapString = args.get(0).trim();
		String outputMapString = args.get(1).trim();
		List<String> inputMapNames = (inputMapString.length() == 0) ? new ArrayList<String>() : Arrays.asList(inputMapString.split(","));
		List<String> outputMapNames = (outputMapString.length() == 0) ? new ArrayList<String>() : Arrays.asList(outputMapString.split(","));
		List<CharacterSequenceMap> inputMaps = new ArrayList<CharacterSequenceMap>();
		List<CharacterSequenceMap> outputMaps = new ArrayList<CharacterSequenceMap>();
		for (String inputMapName : inputMapNames) {
			CharacterSequenceMap inputMap = mapLib.get(inputMapName);
			if (inputMap == null) {
				throw new IllegalArgumentException("Error: Unknown mapping \"" + inputMapName + "\" specified for Remap.");
			}
			inputMaps.add(inputMap);
		}
		for (String outputMapName : outputMapNames) {
			CharacterSequenceMap outputMap = mapLib.get(outputMapName);
			if (outputMap == null) {
				throw new IllegalArgumentException("Error: Unknown mapping \"" + outputMapName + "\" specified for Remap.");
			}
			outputMaps.add(outputMap);
		}
		return new SequenceRemapTransformation(inputMaps, outputMaps);
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return new SequenceRemapTransformationGUI(mapLib, new ArrayList<String>(), new ArrayList<String>());
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		String inputMapString = args.get(0).trim();
		String outputMapString = args.get(1).trim();
		List<String> inputMapNames = (inputMapString.length() == 0) ? new ArrayList<String>() : Arrays.asList(inputMapString.split(","));
		List<String> outputMapNames = (outputMapString.length() == 0) ? new ArrayList<String>() : Arrays.asList(outputMapString.split(","));
		return new SequenceRemapTransformationGUI(mapLib, inputMapNames, outputMapNames);
	}
}
