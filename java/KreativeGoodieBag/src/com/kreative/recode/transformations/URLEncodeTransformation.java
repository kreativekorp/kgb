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

public class URLEncodeTransformation extends TextTransformation {
	private final Charset encoding;
	private CharsetEncoder encoder;
	private List<Character> encoderInputBuffer;
	
	public URLEncodeTransformation(String encoding) {
		try {
			this.encoding = Charset.forName(encoding);
		} catch (UnsupportedCharsetException e) {
			throw new IllegalArgumentException("Error: Unknown encoding \"" + e.getCharsetName() + "\" specified for URL Encode.");
		}
		startTransformation();
	}
	
	public URLEncodeTransformation(Charset encoding) {
		if (encoding == null) {
			throw new IllegalArgumentException("Error: Unknown encoding specified for URL Encode.");
		} else {
			this.encoding = encoding;
		}
		startTransformation();
	}
	
	@Override
	public String getName() {
		return "URL Encode in " + encoding.displayName();
	}
	
	@Override
	public String getDescription() {
		return "Encodes text in " + encoding.displayName() + " using the percent encoding used in URLs.";
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
			byte b = encoderOutput.get(i);
			if (b == ' ') append("+");
			else if (b >= '0' && b <= '9') append((char)b);
			else if (b >= 'A' && b <= 'Z') append((char)b);
			else if (b >= 'a' && b <= 'z') append((char)b);
			else if (b == '-' || b == '.' || b == '_' || b == '~') append((char)b);
			else {
				append("%");
				String h = "00" + Integer.toHexString(b);
				append(h.substring(h.length()-2).toUpperCase());
			}
		}
	}
}
