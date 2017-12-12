package com.kreative.recode.transformations;

import com.kreative.recode.transformation.TextTransformation;

public class XIONUnescapeTransformation extends TextTransformation {
	private final boolean ignoreASCII;
	private boolean inEscape;
	private boolean inUnicode;
	private boolean inWideUnicode;
	private StringBuffer cps;
	
	public XIONUnescapeTransformation(boolean ignoreASCII) {
		this.ignoreASCII = ignoreASCII;
		this.inEscape = false;
		this.inUnicode = false;
		this.inWideUnicode = false;
		this.cps = new StringBuffer();
	}
	
	@Override
	public String getName() {
		if (ignoreASCII) {
			return "XION Unescape (Ignore ASCII)";
		} else {
			return "XION Unescape";
		}
	}
	
	@Override
	public String getDescription() {
		if (ignoreASCII) {
			return "Decodes only XION escape sequences that represent non-ASCII characters. (Note: Does not support \\x sequences.)";
		} else {
			return "Decodes all XION escape sequences. (Note: Does not support \\x sequences.)";
		}
	}
	
	@Override
	protected void startTransformation() {
		inEscape = false;
		inUnicode = false;
		inWideUnicode = false;
		cps = new StringBuffer();
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		if (inWideUnicode) {
			if ((codePoint >= '0' && codePoint <= '9') || (codePoint >= 'A' && codePoint <= 'F') || (codePoint >= 'a' && codePoint <= 'f')) {
				cps.append((char)codePoint);
				if (cps.length() >= 6) {
					int cp = Integer.parseInt(cps.toString(), 16);
					if (ignoreASCII && cp < 0x80) {
						append("\\w");
						append(cps.toString());
					} else {
						append(cp);
					}
					inEscape = false;
					inUnicode = false;
					inWideUnicode = false;
					cps = new StringBuffer();
				}
			} else {
				append("\\w");
				append(cps.toString());
				inEscape = false;
				inUnicode = false;
				inWideUnicode = false;
				cps = new StringBuffer();
				if (codePoint == '\\') {
					inEscape = true;
				} else {
					append(codePoint);
				}
			}
		} else if (inUnicode) {
			if ((codePoint >= '0' && codePoint <= '9') || (codePoint >= 'A' && codePoint <= 'F') || (codePoint >= 'a' && codePoint <= 'f')) {
				cps.append((char)codePoint);
				if (cps.length() >= 4) {
					int cp = Integer.parseInt(cps.toString(), 16);
					if (ignoreASCII && cp < 0x80) {
						append("\\u");
						append(cps.toString());
					} else {
						append(cp);
					}
					inEscape = false;
					inUnicode = false;
					inWideUnicode = false;
					cps = new StringBuffer();
				}
			} else {
				append("\\u");
				append(cps.toString());
				inEscape = false;
				inUnicode = false;
				inWideUnicode = false;
				cps = new StringBuffer();
				if (codePoint == '\\') {
					inEscape = true;
				} else {
					append(codePoint);
				}
			}
		} else if (inEscape) {
			if (ignoreASCII) {
				switch (codePoint) {
				case 'C': append("\uFFF0"); inEscape = false; break;
				case 'R': append("\uFFF1"); inEscape = false; break;
				case 'P': append("\uFFF2"); inEscape = false; break;
				case 'S': append("\uFFF3"); inEscape = false; break;
				case 'E': append("\uFFFF"); inEscape = false; break;
				case 'u': inUnicode = true; break;
				case 'w': inWideUnicode = true; break;
				default: append("\\"); append(codePoint); inEscape = false; break;
				}
			} else {
				switch (codePoint) {
				case '0': append("\u0000"); inEscape = false; break;
				case 'a': append("\u0007"); inEscape = false; break;
				case 'b': append("\b"); inEscape = false; break;
				case 't': append("\t"); inEscape = false; break;
				case 'n': append("\n"); inEscape = false; break;
				case 'v': append("\u000B"); inEscape = false; break;
				case 'f': append("\f"); inEscape = false; break;
				case 'r': append("\r"); inEscape = false; break;
				case 'l': append("\r\n"); inEscape = false; break;
				case 'o': append("\u000E"); inEscape = false; break;
				case 'i': append("\u000F"); inEscape = false; break;
				case 'z': append("\u001A"); inEscape = false; break;
				case 'e': append("\u001B"); inEscape = false; break;
				case ' ': append(" "); inEscape = false; break;
				case '\"': append("\""); inEscape = false; break;
				case '\'': append("\'"); inEscape = false; break;
				case '\\': append("\\"); inEscape = false; break;
				case 'd': append("\u007F"); inEscape = false; break;
				case 'C': append("\uFFF0"); inEscape = false; break;
				case 'R': append("\uFFF1"); inEscape = false; break;
				case 'P': append("\uFFF2"); inEscape = false; break;
				case 'S': append("\uFFF3"); inEscape = false; break;
				case 'E': append("\uFFFF"); inEscape = false; break;
				case 'u': inUnicode = true; break;
				case 'w': inWideUnicode = true; break;
				default: append("\\"); append(codePoint); inEscape = false; break;
				}
			}
		} else {
			if (codePoint == '\\') {
				inEscape = true;
			} else {
				append(codePoint);
			}
		}
	}
	
	@Override
	protected void stopTransformation() {
		if (inWideUnicode) {
			append("\\w");
			append(cps.toString());
		} else if (inUnicode) {
			append("\\u");
			append(cps.toString());
		} else if (inEscape) {
			append("\\");
		}
	}
}
