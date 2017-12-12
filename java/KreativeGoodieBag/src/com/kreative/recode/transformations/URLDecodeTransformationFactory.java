package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class URLDecodeTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "URL Decode";
	}
	
	@Override
	public String getDescription() {
		return "Decodes text in the percent encoding used in URLs.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-ud", "--ud", "-urldec", "--urldec", "-urldecode", "--urldecode");
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
		return Arrays.asList("The encoding used to decode percent escape sequences.");
	}
	
	@Override
	public TextTransformation createTransformation(List<String> args) {
		return new URLDecodeTransformation(args.get(0));
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return new URLDecodeTransformationGUI("UTF-8");
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		return new URLDecodeTransformationGUI(args.get(0));
	}
}
