package com.kreative.recode.transformations;

import com.kreative.recode.transformation.TextTransformation;

public class HTMLEncodeTransformation extends TextTransformation {
	private final boolean ignoreASCII;
	
	public HTMLEncodeTransformation(boolean ignoreASCII) {
		this.ignoreASCII = ignoreASCII;
	}
	
	@Override
	public String getName() {
		if (ignoreASCII) {
			return "HTML Encode (Ignore ASCII)";
		} else {
			return "HTML Encode";
		}
	}
	
	@Override
	public String getDescription() {
		if (ignoreASCII) {
			return "Encodes only non-ASCII characters as HTML entities.";
		} else {
			return "Encodes both non-printable and non-ASCII characters as HTML entities.";
		}
	}
	
	@Override
	protected void startTransformation() {
		// Nothing.
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		if (ignoreASCII && codePoint < 0x80) {
			append(codePoint);
		} else {
			switch (codePoint) {
			case '&': append("&amp;"); break;
			case '<': append("&lt;"); break;
			case '>': append("&gt;"); break;
			case '\"': append("&quot;"); break;
			case '\'': append("&#39;"); break;
			case '\u00A0': append("&nbsp;"); break;
			default:
				if (codePoint >= 32 && codePoint < 127) {
					append(codePoint);
				} else {
					append("&#");
					append(Integer.toString(codePoint));
					append(";");
				}
				break;
			}
		}
	}
	
	@Override
	protected void stopTransformation() {
		// Nothing.
	}
}
