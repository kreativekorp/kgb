package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class TitlecaseTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "To Titlecase";
	}
	
	@Override
	public String getDescription() {
		return "Changes letters to titlecase if preceded by a nonletter, or lowercase if preceded by a letter.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-tc", "--tc", "-totitle", "--totitle", "-totitlecase", "--totitlecase");
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
		return new TitlecaseTransformation();
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
