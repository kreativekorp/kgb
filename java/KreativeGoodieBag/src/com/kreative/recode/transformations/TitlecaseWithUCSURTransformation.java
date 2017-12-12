package com.kreative.recode.transformations;

import com.kreative.recode.misc.ConCharacter;
import com.kreative.recode.transformation.TextTransformation;

public class TitlecaseWithUCSURTransformation extends TextTransformation {
	private boolean lastCharacterWasLetter = false;
	
	@Override
	public String getName() {
		return "To Titlecase (with UCSUR)";
	}
	
	@Override
	public String getDescription() {
		return "Changes letters, including letters in conscripts registered with the UCSUR, to titlecase if preceded by a nonletter, or lowercase if preceded by a letter.";
	}
	
	@Override
	protected void startTransformation() {
		lastCharacterWasLetter = false;
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		if (lastCharacterWasLetter) {
			append(ConCharacter.toLowerCase(codePoint));
		} else {
			append(ConCharacter.toTitleCase(codePoint));
		}
		lastCharacterWasLetter = ConCharacter.isLetter(codePoint);
	}
	
	@Override
	protected void stopTransformation() {
		// Nothing.
	}
}
