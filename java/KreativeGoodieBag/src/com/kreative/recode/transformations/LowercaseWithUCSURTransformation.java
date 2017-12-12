package com.kreative.recode.transformations;

import com.kreative.recode.misc.ConCharacter;
import com.kreative.recode.transformation.TextTransformation;

public class LowercaseWithUCSURTransformation extends TextTransformation {
	@Override
	public String getName() {
		return "To Lowercase (with UCSUR)";
	}
	
	@Override
	public String getDescription() {
		return "Changes all letters, including letters in conscripts registered with the UCSUR, to lowercase.";
	}
	
	@Override
	protected void startTransformation() {
		// Nothing.
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		append(ConCharacter.toLowerCase(codePoint));
	}
	
	@Override
	protected void stopTransformation() {
		// Nothing.
	}
}
