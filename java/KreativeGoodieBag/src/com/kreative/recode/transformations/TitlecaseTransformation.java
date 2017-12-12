package com.kreative.recode.transformations;

import com.kreative.recode.transformation.TextTransformation;

public class TitlecaseTransformation extends TextTransformation {
	private boolean lastCharacterWasLetter = false;
	
	@Override
	public String getName() {
		return "To Titlecase";
	}
	
	@Override
	public String getDescription() {
		return "Changes letters to titlecase if preceded by a nonletter, or lowercase if preceded by a letter.";
	}
	
	@Override
	protected void startTransformation() {
		lastCharacterWasLetter = false;
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		if (lastCharacterWasLetter) {
			append(Character.toLowerCase(codePoint));
		} else {
			append(Character.toTitleCase(codePoint));
		}
		lastCharacterWasLetter = Character.isLetter(codePoint);
	}
	
	@Override
	protected void stopTransformation() {
		// Nothing.
	}
}
