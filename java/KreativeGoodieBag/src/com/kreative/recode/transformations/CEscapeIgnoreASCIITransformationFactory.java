package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class CEscapeIgnoreASCIITransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "C/C++ Escape (Ignore ASCII)";
	}
	
	@Override
	public String getDescription() {
		return "Encodes only non-ASCII characters as C/C++ escape sequences.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-cei", "--cei", "-cescia", "--cescia", "-cescapeignoreascii", "--cescapeignoreascii");
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
		return new CEscapeTransformation(true, args.get(0));
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return new CEscapeTransformationGUI(true, "UTF-8");
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		return new CEscapeTransformationGUI(true, args.get(0));
	}
}
