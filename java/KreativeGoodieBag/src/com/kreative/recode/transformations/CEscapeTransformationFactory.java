package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class CEscapeTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "C/C++ Escape";
	}
	
	@Override
	public String getDescription() {
		return "Encodes both non-printable and non-ASCII characters as C/C++ escape sequences.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-ce", "--ce", "-cesc", "--cesc", "-cescape", "--cescape");
	}
	
	@Override
	public int getArgumentCount() {
		return 1;
	}
	
	@Override
	public List<String> getArgumentNames() {
		return Arrays.asList("encoding");
	}
	
	@Override
	public List<String> getArgumentDescriptions() {
		return Arrays.asList("The encoding used to generate escape sequences.");
	}
	
	@Override
	public TextTransformation createTransformation(List<String> args) {
		return new CEscapeTransformation(false, args.get(0));
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return new CEscapeTransformationGUI(false, "UTF-8");
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		return new CEscapeTransformationGUI(false, args.get(0));
	}
}
