package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class CUnescapeIgnoreASCIITransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "C/C++ Unescape (Ignore ASCII)";
	}
	
	@Override
	public String getDescription() {
		return "Decodes only C/C++ escape sequences that represent non-ASCII characters.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-cui", "--cui", "-cunescia", "--cunescia", "-cunescapeignoreascii", "--cunescapeignoreascii");
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
		return new CUnescapeTransformation(true, args.get(0));
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return new CUnescapeTransformationGUI(true, "UTF-8");
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		return new CUnescapeTransformationGUI(true, args.get(0));
	}
}
