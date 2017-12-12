package com.kreative.recode.transformations;

import com.kreative.recode.transformation.TextTransformation;

public class UppercaseTransformation extends TextTransformation {
	@Override
	public String getName() {
		return "To Uppercase";
	}
	
	@Override
	public String getDescription() {
		return "Changes all letters to uppercase.";
	}
	
	@Override
	protected void startTransformation() {
		// Nothing.
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		append(Character.toUpperCase(codePoint));
	}
	
	@Override
	protected void stopTransformation() {
		// Nothing.
	}
}
