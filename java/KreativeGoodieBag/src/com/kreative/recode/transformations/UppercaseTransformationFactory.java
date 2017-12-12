package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class UppercaseTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "To Uppercase";
	}
	
	@Override
	public String getDescription() {
		return "Changes all letters to uppercase.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-uc", "--uc", "-toupper", "--toupper", "-touppercase", "--touppercase");
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
		return new UppercaseTransformation();
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
