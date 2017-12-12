package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class CUnescapeTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "C/C++ Unescape";
	}
	
	@Override
	public String getDescription() {
		return "Decodes all C/C++ escape sequences.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-cu", "--cu", "-cunesc", "--cunesc", "-cunescape", "--cunescape");
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
		return Arrays.asList("The encoding used to decode escape sequences.");
	}
	
	@Override
	public TextTransformation createTransformation(List<String> args) {
		return new CUnescapeTransformation(false, args.get(0));
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return new CUnescapeTransformationGUI(false, "UTF-8");
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		return new CUnescapeTransformationGUI(false, args.get(0));
	}
}
