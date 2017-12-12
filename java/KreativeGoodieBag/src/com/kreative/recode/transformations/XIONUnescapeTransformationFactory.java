package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class XIONUnescapeTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "XION Unescape";
	}
	
	@Override
	public String getDescription() {
		return "Decodes all XION escape sequences. (Note: Does not support \\x sequences.)";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-xu", "--xu", "-xionunesc", "--xionunesc", "-xionunescape", "--xionunescape");
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
		return new XIONUnescapeTransformation(false);
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
