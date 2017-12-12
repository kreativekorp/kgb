package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class UppercaseWithUCSURTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "To Uppercase (with UCSUR)";
	}
	
	@Override
	public String getDescription() {
		return "Changes all letters, including letters in conscripts registered with the UCSUR, to uppercase.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-ucu", "--ucu", "-toupperucsur", "--toupperucsur", "-touppercasewithucsur", "--touppercasewithucsur");
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
		return new UppercaseWithUCSURTransformation();
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
