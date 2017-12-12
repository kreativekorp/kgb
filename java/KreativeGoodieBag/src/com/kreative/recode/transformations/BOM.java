package com.kreative.recode.transformations;

import java.util.ArrayList;
import java.util.List;
import com.kreative.recode.transformation.TextTransformationFactory;

public class BOM {
	private BOM() {}
	
	public static List<TextTransformationFactory> getTextTransformationFactories() {
		List<TextTransformationFactory> contents = new ArrayList<TextTransformationFactory>();
		contents.add(new AtbashCipherTransformationFactory());
		contents.add(new CaesarCipherTransformationFactory());
		contents.add(new CEscapeTransformationFactory());
		contents.add(new CEscapeIgnoreASCIITransformationFactory());
		contents.add(new CUnescapeTransformationFactory());
		contents.add(new CUnescapeIgnoreASCIITransformationFactory());
		contents.add(new CharacterFrequencyTransformationFactory());
		contents.add(new HTMLDecodeTransformationFactory());
		contents.add(new HTMLDecodeIgnoreASCIITransformationFactory());
		contents.add(new HTMLEncodeTransformationFactory());
		contents.add(new HTMLEncodeIgnoreASCIITransformationFactory());
		contents.add(new JavaEscapeTransformationFactory());
		contents.add(new JavaEscapeIgnoreASCIITransformationFactory());
		contents.add(new JavaUnescapeTransformationFactory());
		contents.add(new JavaUnescapeIgnoreASCIITransformationFactory());
		contents.add(new LowercaseTransformationFactory());
		contents.add(new LowercaseWithUCSURTransformationFactory());
		contents.add(new ReinterpretTransformationFactory());
		contents.add(new SequenceRemapTransformationFactory());
		contents.add(new TitlecaseTransformationFactory());
		contents.add(new TitlecaseWithUCSURTransformationFactory());
		contents.add(new UppercaseTransformationFactory());
		contents.add(new UppercaseWithUCSURTransformationFactory());
		contents.add(new URLDecodeTransformationFactory());
		contents.add(new URLEncodeTransformationFactory());
		contents.add(new XIONEscapeTransformationFactory());
		contents.add(new XIONEscapeIgnoreASCIITransformationFactory());
		contents.add(new XIONUnescapeTransformationFactory());
		contents.add(new XIONUnescapeIgnoreASCIITransformationFactory());
		return contents;
	}
}
