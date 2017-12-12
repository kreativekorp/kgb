package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class HTMLDecodeTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "HTML Decode";
	}
	
	@Override
	public String getDescription() {
		return "Decodes all HTML entities.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-hd", "--hd", "-htmldec", "--htmldec", "-htmldecode", "--htmldecode");
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
		return new HTMLDecodeTransformation(false);
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
