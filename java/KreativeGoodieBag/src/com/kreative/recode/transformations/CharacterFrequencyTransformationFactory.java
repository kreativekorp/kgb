package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class CharacterFrequencyTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "Character Frequency";
	}
	
	@Override
	public String getDescription() {
		return "Counts the number of occurrences of each character and produces a list of character frequencies.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-cf", "--cf", "-charfreq", "--charfreq", "-characterfrequency", "--characterfrequency");
	}
	
	@Override
	public int getArgumentCount() {
		return 0;
	}
	
	@Override
	public List<String> getArgumentNames() {
		return Arrays.asList();
	}
	
	@Override
	public List<String> getArgumentDescriptions() {
		return Arrays.asList();
	}
	
	@Override
	public TextTransformation createTransformation(List<String> args) {
		return new CharacterFrequencyTransformation();
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return null;
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		return null;
	}
}
