package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class LowercaseTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "To Lowercase";
	}
	
	@Override
	public String getDescription() {
		return "Changes all letters to lowercase.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-lc", "--lc", "-tolower", "--tolower", "-tolowercase", "--tolowercase");
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
		return new LowercaseTransformation();
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
