package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class URLEncodeTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "URL Encode";
	}
	
	@Override
	public String getDescription() {
		return "Encodes text using the percent encoding used in URLs.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-ue", "--ue", "-urlenc", "--urlenc", "-urlencode", "--urlencode");
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
		return Arrays.asList("The encoding used to generate percent escape sequences.");
	}
	
	@Override
	public TextTransformation createTransformation(List<String> args) {
		return new URLEncodeTransformation(args.get(0));
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return new URLEncodeTransformationGUI("UTF-8");
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		return new URLEncodeTransformationGUI(args.get(0));
	}
}
