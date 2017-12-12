package com.kreative.recode.transformations;

import com.kreative.recode.misc.ConCharacter;
import com.kreative.recode.transformation.TextTransformation;

public class UppercaseWithUCSURTransformation extends TextTransformation {
	@Override
	public String getName() {
		return "To Uppercase (with UCSUR)";
	}
	
	@Override
	public String getDescription() {
		return "Changes all letters, including letters in conscripts registered with the UCSUR, to uppercase.";
	}
	
	@Override
	protected void startTransformation() {
		// Nothing.
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		append(ConCharacter.toUpperCase(codePoint));
	}
	
	@Override
	protected void stopTransformation() {
		// Nothing.
	}
}
