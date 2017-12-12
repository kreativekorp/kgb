package com.kreative.recode.transformations;

import com.kreative.recode.transformation.TextTransformation;

public class JavaUnescapeTransformation extends TextTransformation {
	private final boolean ignoreASCII;
	private boolean inEscape;
	private boolean inUnicode;
	private StringBuffer cps;
	private char lastHighSurrogate;
	
	public JavaUnescapeTransformation(boolean ignoreASCII) {
		this.ignoreASCII = ignoreASCII;
		this.inEscape = false;
		this.inUnicode = false;
		this.cps = new StringBuffer();
		this.lastHighSurrogate = 0;
	}
	
	@Override
	public String getName() {
		if (ignoreASCII) {
			return "Java Unescape (Ignore ASCII)";
		} else {
			return "Java Unescape";
		}
	}
	
	@Override
	public String getDescription() {
		if (ignoreASCII) {
			return "Decodes only Java escape sequences that represent non-ASCII characters.";
		} else {
			return "Decodes all Java escape sequences.";
		}
	}
	
	@Override
	protected void startTransformation() {
		inEscape = false;
		inUnicode = false;
		cps = new StringBuffer();
		lastHighSurrogate = 0;
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		for (char ch : Character.toChars(codePoint)) {
			if (inUnicode) {
				if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f')) {
					cps.append(ch);
					if (cps.length() >= 4) {
						int cp = Integer.parseInt(cps.toString(), 16);
						if (ignoreASCII && cp < 0x80) {
							appendChar('\\');
							appendChar('u');
							for (char cpch : cps.toString().toCharArray()) {
								appendChar(cpch);
							}
						} else {
							appendChar((char)cp);
						}
						inEscape = false;
						inUnicode = false;
						cps = new StringBuffer();
					}
				} else {
					appendChar('\\');
					appendChar('u');
					for (char cpch : cps.toString().toCharArray()) {
						appendChar(cpch);
					}
					inEscape = false;
					inUnicode = false;
					cps = new StringBuffer();
					if (ch == '\\') {
						inEscape = true;
					} else {
						appendChar(ch);
					}
				}
			} else if (inEscape) {
				if (ignoreASCII) {
					if (ch == 'u') {
						inUnicode = true;
					} else {
						appendChar('\\');
						appendChar(ch);
						inEscape = false;
					}
				} else {
					switch (ch) {
					case 'b': appendChar('\b'); inEscape = false; break;
					case 't': appendChar('\t'); inEscape = false; break;
					case 'n': appendChar('\n'); inEscape = false; break;
					case 'f': appendChar('\f'); inEscape = false; break;
					case 'r': appendChar('\r'); inEscape = false; break;
					case '\"': appendChar('\"'); inEscape = false; break;
					case '\'': appendChar('\''); inEscape = false; break;
					case '\\': appendChar('\\'); inEscape = false; break;
					case 'u': inUnicode = true; break;
					default: appendChar('\\'); appendChar(ch); inEscape = false; break;
					}
				}
			} else {
				if (ch == '\\') {
					inEscape = true;
				} else {
					appendChar(ch);
				}
			}
		}
	}
	
	private void appendChar(char ch) {
		if (Character.isHighSurrogate(ch)) {
			if (lastHighSurrogate != 0) {
				append(lastHighSurrogate);
			}
			lastHighSurrogate = ch;
		} else if (Character.isLowSurrogate(ch)) {
			if (lastHighSurrogate != 0) {
				append(Character.toCodePoint(lastHighSurrogate, ch));
				lastHighSurrogate = 0;
			} else {
				append(ch);
			}
		} else {
			if (lastHighSurrogate != 0) {
				append(lastHighSurrogate);
				lastHighSurrogate = 0;
			}
			append(ch);
		}
	}
	
	@Override
	protected void stopTransformation() {
		if (inUnicode) {
			appendChar('\\');
			appendChar('u');
			for (char cpch : cps.toString().toCharArray()) {
				appendChar(cpch);
			}
		} else if (inEscape) {
			appendChar('\\');
		}
		if (lastHighSurrogate != 0) {
			append(lastHighSurrogate);
		}
	}
}
