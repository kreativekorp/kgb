package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class ReinterpretTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "Reinterpret";
	}
	
	@Override
	public String getDescription() {
		return "Reinterprets text in one encoding as text in a different encoding.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-ri", "--ri", "-reinterp", "--reinterp", "-reinterpret", "--reinterpret");
	}
	
	@Override
	public int getArgumentCount() {
		return 2;
	}
	
	@Override
	public List<String> getArgumentNames() {
		return Arrays.asList("originalencoding", "newencoding");
	}
	
	@Override
	public List<String> getArgumentDescriptions() {
		return Arrays.asList("The original text encoding.", "The text encoding to reinterpret as.");
	}
	
	@Override
	public TextTransformation createTransformation(List<String> args) {
		return new ReinterpretTransformation(args.get(0), args.get(1));
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return new ReinterpretTransformationGUI("ISO-8859-1", "UTF-8");
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		return new ReinterpretTransformationGUI(args.get(0), args.get(1));
	}
}
