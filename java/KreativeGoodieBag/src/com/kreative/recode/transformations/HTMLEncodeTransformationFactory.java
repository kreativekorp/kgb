package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class HTMLEncodeTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "HTML Encode";
	}
	
	@Override
	public String getDescription() {
		return "Encodes both non-printable and non-ASCII characters as HTML entities.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-he", "--he", "-htmlenc", "--htmlenc", "-htmlencode", "--htmlencode");
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
		return new HTMLEncodeTransformation(false);
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
