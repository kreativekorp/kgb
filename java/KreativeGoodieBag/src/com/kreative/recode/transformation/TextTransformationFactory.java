package com.kreative.recode.transformation;

import java.util.Collection;
import java.util.List;

public abstract class TextTransformationFactory {
	public abstract String getName();
	public abstract String getDescription();
	public abstract Collection<String> getFlags();
	public abstract int getArgumentCount();
	public abstract List<String> getArgumentNames();
	public abstract List<String> getArgumentDescriptions();
	public abstract TextTransformation createTransformation(List<String> args);
	public abstract TextTransformationGUI createGUI();
	public abstract TextTransformationGUI createGUI(List<String> args);
	
	@Override
	public final String toString() {
		return getName();
	}
}
