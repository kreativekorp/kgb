package com.kreative.recode.transformations;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;

public class URLDecodeTransformation extends TextTransformation {
	private final Charset encoding;
	private CharsetDecoder decoder;
	private List<Byte> decoderInputBuffer;
	private boolean inPercent;
	private StringBuffer cps;
	private char lastHighSurrogate;
	
	public URLDecodeTransformation(String encoding) {
		try {
			this.encoding = Charset.forName(encoding);
		} catch (UnsupportedCharsetException e) {
			throw new IllegalArgumentException("Error: Unknown encoding \"" + e.getCharsetName() + "\" specified for URL Decode.");
		}
		startTransformation();
	}
	
	public URLDecodeTransformation(Charset encoding) {
		if (encoding == null) {
			throw new IllegalArgumentException("Error: Unknown encoding specified for URL Decode.");
		} else {
			this.encoding = encoding;
		}
		startTransformation();
	}
	
	@Override
	public String getName() {
		return "URL Decode in " + encoding.displayName();
	}
	
	@Override
	public String getDescription() {
		return "Decodes text in the percent encoding used in URLs using " + encoding.displayName() + ".";
	}
	
	@Override
	protected void startTransformation() {
		decoder = encoding.newDecoder();
		decoder.onMalformedInput(CodingErrorAction.REPLACE);
		decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		decoderInputBuffer = new ArrayList<Byte>();
		inPercent = false;
		cps = new StringBuffer();
		lastHighSurrogate = 0;
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		if (inPercent) {
			if ((codePoint >= '0' && codePoint <= '9') || (codePoint >= 'A' && codePoint <= 'F') || (codePoint >= 'a' && codePoint <= 'f')) {
				cps.append((char)codePoint);
				if (cps.length() >= 2) {
					decoderInputBuffer.add((byte)Integer.parseInt(cps.toString(), 16));
					inPercent = false;
					cps = new StringBuffer();
				}
			} else {
				decoderInputBuffer.add((byte)'%');
				for (char ch : cps.toString().toCharArray()) {
					decoderInputBuffer.add((byte)ch);
				}
				inPercent = false;
				cps = new StringBuffer();
				if (codePoint == '%') {
					inPercent = true;
				} else if (codePoint == '+') {
					decoderInputBuffer.add((byte)' ');
				} else if (codePoint < 128) {
					decoderInputBuffer.add((byte)codePoint);
				} else {
					for (byte b : new String(Character.toChars(codePoint)).getBytes(encoding)) {
						decoderInputBuffer.add(b);
					}
				}
			}
		} else {
			if (codePoint == '%') {
				inPercent = true;
			} else if (codePoint == '+') {
				decoderInputBuffer.add((byte)' ');
			} else if (codePoint < 128) {
				decoderInputBuffer.add((byte)codePoint);
			} else {
				for (byte b : new String(Character.toChars(codePoint)).getBytes(encoding)) {
					decoderInputBuffer.add(b);
				}
			}
		}
		decode(false);
	}
	
	private void decode(boolean endOfInput) {
		int decoderInputLength = decoderInputBuffer.size();
		int decoderOutputLength = (int)Math.ceil(decoder.maxCharsPerByte() * decoderInputLength);
		ByteBuffer decoderInput = ByteBuffer.allocate(decoderInputLength);
		for (int i = 0; i < decoderInputLength; i++) {
			decoderInput.put(i, decoderInputBuffer.get(i));
		}
		CharBuffer decoderOutput = CharBuffer.allocate(decoderOutputLength);
		decoder.decode(decoderInput, decoderOutput, endOfInput);
		decoderInputBuffer.subList(0, decoderInput.position()).clear();
		for (int i = 0, m = decoderOutput.position(); i < m; i++) {
			appendChar(decoderOutput.get(i));
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
		if (inPercent) {
			decoderInputBuffer.add((byte)'%');
			for (char ch : cps.toString().toCharArray()) {
				decoderInputBuffer.add((byte)ch);
			}
		}
		decode(true);
		if (lastHighSurrogate != 0) {
			append(lastHighSurrogate);
		}
	}
}
