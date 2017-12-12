package com.kreative.recode.transformations;

import com.kreative.recode.transformation.TextTransformation;

public class JavaEscapeTransformation extends TextTransformation {
	private final boolean ignoreASCII;
	
	public JavaEscapeTransformation(boolean ignoreASCII) {
		this.ignoreASCII = ignoreASCII;
	}
	
	@Override
	public String getName() {
		if (ignoreASCII) {
			return "Java Escape (Ignore ASCII)";
		} else {
			return "Java Escape";
		}
	}
	
	@Override
	public String getDescription() {
		if (ignoreASCII) {
			return "Encodes only non-ASCII characters as Java escape sequences.";
		} else {
			return "Encodes both non-printable and non-ASCII characters as Java escape sequences.";
		}
	}
	
	@Override
	protected void startTransformation() {
		// Nothing.
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		for (char ch : Character.toChars(codePoint)) {
			if (ignoreASCII && ch < 0x80) {
				append(ch);
			} else {
				switch (ch) {
				case '\b': append("\\b"); break;
				case '\t': append("\\t"); break;
				case '\n': append("\\n"); break;
				case '\f': append("\\f"); break;
				case '\r': append("\\r"); break;
				case '\"': append("\\\""); break;
				case '\'': append("\\\'"); break;
				case '\\': append("\\\\"); break;
				default:
					if (ch >= 32 && ch < 127) {
						append(ch);
					} else {
						append("\\u");
						String h = "0000" + Integer.toHexString(ch);
						append(h.substring(h.length()-4).toUpperCase());
					}
					break;
				}
			}
		}
	}
	
	@Override
	protected void stopTransformation() {
		// Nothing.
	}
}
