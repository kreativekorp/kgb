package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class JavaUnescapeIgnoreASCIITransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "Java Unescape (Ignore ASCII)";
	}
	
	@Override
	public String getDescription() {
		return "Decodes only Java escape sequences that represent non-ASCII characters.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-jui", "--jui", "-javaunescia", "--javaunescia", "-javaunescapeignoreascii", "--javaunescapeignoreascii");
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
		return new JavaUnescapeTransformation(true);
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
