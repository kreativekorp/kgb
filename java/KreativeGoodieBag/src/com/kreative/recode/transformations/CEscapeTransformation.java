package com.kreative.recode.transformations;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;

public class CEscapeTransformation extends TextTransformation {
	private final boolean ignoreASCII;
	private final Charset encoding;
	private CharsetEncoder encoder;
	private List<Character> encoderInputBuffer;
	
	public CEscapeTransformation(boolean ignoreASCII, String encoding) {
		this.ignoreASCII = ignoreASCII;
		try {
			this.encoding = Charset.forName(encoding);
		} catch (UnsupportedCharsetException e) {
			throw new IllegalArgumentException("Error: Unknown encoding \"" + e.getCharsetName() + "\" specified for C/C++ Escape.");
		}
		startTransformation();
	}
	
	public CEscapeTransformation(boolean ignoreASCII, Charset encoding) {
		this.ignoreASCII = ignoreASCII;
		if (encoding == null) {
			throw new IllegalArgumentException("Error: Unknown encoding specified for C/C++ Escape.");
		} else {
			this.encoding = encoding;
		}
		startTransformation();
	}
	
	@Override
	public String getName() {
		if (ignoreASCII) {
			return "C/C++ Escape (Ignore ASCII) using " + encoding.displayName();
		} else {
			return "C/C++ Escape using " + encoding.displayName();
		}
	}
	
	@Override
	public String getDescription() {
		if (ignoreASCII) {
			return "Encodes only non-ASCII characters as C/C++ escape sequences using " + encoding.displayName() + ".";
		} else {
			return "Encodes both non-printable and non-ASCII characters as C/C++ escape sequences using " + encoding.displayName() + ".";
		}
	}
	
	@Override
	protected void startTransformation() {
		encoder = encoding.newEncoder();
		encoder.onMalformedInput(CodingErrorAction.REPLACE);
		encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		encoderInputBuffer = new ArrayList<Character>();
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		for (char ch : Character.toChars(codePoint)) {
			encoderInputBuffer.add(ch);
		}
		encode(false);
	}
	
	@Override
	protected void stopTransformation() {
		encode(true);
	}
	
	private void encode(boolean endOfInput) {
		int encoderInputLength = encoderInputBuffer.size();
		int encoderOutputLength = (int)Math.ceil(encoder.maxBytesPerChar() * encoderInputLength);
		CharBuffer encoderInput = CharBuffer.allocate(encoderInputLength);
		for (int i = 0; i < encoderInputLength; i++) {
			encoderInput.put(i, encoderInputBuffer.get(i));
		}
		ByteBuffer encoderOutput = ByteBuffer.allocate(encoderOutputLength);
		encoder.encode(encoderInput, encoderOutput, endOfInput);
		encoderInputBuffer.subList(0, encoderInput.position()).clear();
		for (int i = 0, m = encoderOutput.position(); i < m; i++) {
			int b = encoderOutput.get(i) & 0xFF;
			if (ignoreASCII && b < 0x80) {
				append(b);
			} else {
				switch (b) {
				case '\u0000': append("\\0"); break;
				case '\u0007': append("\\a"); break;
				case '\b'    : append("\\b"); break;
				case '\t'    : append("\\t"); break;
				case '\n'    : append("\\n"); break;
				case '\u000B': append("\\v"); break;
				case '\f'    : append("\\f"); break;
				case '\r'    : append("\\r"); break;
				case '\"'    : append("\\\""); break;
				case '\''    : append("\\\'"); break;
				case '\\'    : append("\\\\"); break;
				default:
					if (b >= 32 && b < 127) {
						append(b);
					} else {
						append("\\x");
						String h = "00" + Integer.toHexString(b);
						append(h.substring(h.length()-2).toUpperCase());
					}
					break;
				}
			}
		}
	}
}
