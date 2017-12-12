package com.kreative.recode.transformations;

import com.kreative.recode.transformation.TextTransformation;

public class LowercaseTransformation extends TextTransformation {
	@Override
	public String getName() {
		return "To Lowercase";
	}
	
	@Override
	public String getDescription() {
		return "Changes all letters to lowercase.";
	}
	
	@Override
	protected void startTransformation() {
		// Nothing.
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		append(Character.toLowerCase(codePoint));
	}
	
	@Override
	protected void stopTransformation() {
		// Nothing.
	}
}
