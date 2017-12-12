package com.kreative.recode.transformations;

import com.kreative.recode.transformation.TextTransformation;

public class XIONEscapeTransformation extends TextTransformation {
	private final boolean ignoreASCII;
	
	public XIONEscapeTransformation(boolean ignoreASCII) {
		this.ignoreASCII = ignoreASCII;
	}
	
	@Override
	public String getName() {
		if (ignoreASCII) {
			return "XION Escape (Ignore ASCII)";
		} else {
			return "XION Escape";
		}
	}
	
	@Override
	public String getDescription() {
		if (ignoreASCII) {
			return "Encodes only non-ASCII characters as XION escape sequences.";
		} else {
			return "Encodes both non-printable and non-ASCII characters as XION escape sequences.";
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
			case '\u0000': append("\\0"); break;
			case '\u0007': append("\\a"); break;
			case '\b'    : append("\\b"); break;
			case '\t'    : append("\\t"); break;
			case '\n'    : append("\\n"); break;
			case '\u000B': append("\\v"); break;
			case '\f'    : append("\\f"); break;
			case '\r'    : append("\\r"); break;
			case '\u000E': append("\\o"); break;
			case '\u000F': append("\\i"); break;
			case '\u001A': append("\\z"); break;
			case '\u001B': append("\\e"); break;
			case '\"'    : append("\\\""); break;
			case '\''    : append("\\\'"); break;
			case '\\'    : append("\\\\"); break;
			case '\u007F': append("\\d"); break;
			default:
				if (codePoint >= 32 && codePoint < 127) {
					append(codePoint);
				} else if (codePoint < 0x10000) {
					append("\\u");
					String h = "0000" + Integer.toHexString(codePoint);
					append(h.substring(h.length()-4).toUpperCase());
				} else {
					append("\\w");
					String h = "000000" + Integer.toHexString(codePoint);
					append(h.substring(h.length()-6).toUpperCase());
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
