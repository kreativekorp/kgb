package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class JavaUnescapeTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "Java Unescape";
	}
	
	@Override
	public String getDescription() {
		return "Decodes all Java escape sequences.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-ju", "--ju", "-javaunesc", "--javaunesc", "-javaunescape", "--javaunescape");
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
		return new JavaUnescapeTransformation(false);
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
