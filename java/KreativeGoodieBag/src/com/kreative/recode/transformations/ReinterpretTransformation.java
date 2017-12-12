package com.kreative.recode.transformations;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;

public class ReinterpretTransformation extends TextTransformation {
	private final Charset interpretedAs;
	private final Charset reinterpretAs;
	private CharsetEncoder encoder;
	private CharsetDecoder decoder;
	private List<Character> encoderInputBuffer;
	private List<Byte> decoderInputBuffer;
	private char lastHighSurrogate;
	
	public ReinterpretTransformation(String interpretedAs, String reinterpretAs) {
		try {
			this.interpretedAs = Charset.forName(interpretedAs);
			this.reinterpretAs = Charset.forName(reinterpretAs);
		} catch (UnsupportedCharsetException e) {
			throw new IllegalArgumentException("Error: Unknown encoding \"" + e.getCharsetName() + "\" specified for Reinterpret.");
		}
		startTransformation();
	}
	
	public ReinterpretTransformation(Charset interpretedAs, Charset reinterpretAs) {
		if (interpretedAs == null || reinterpretAs == null) {
			throw new IllegalArgumentException("Error: Unknown encoding specified for Reinterpret.");
		} else {
			this.interpretedAs = interpretedAs;
			this.reinterpretAs = reinterpretAs;
		}
		startTransformation();
	}
	
	@Override
	public String getName() {
		return "Reinterpret " + interpretedAs.displayName() + " as " + reinterpretAs.displayName();
	}
	
	@Override
	public String getDescription() {
		return "Reinterprets text in " + interpretedAs.displayName() + " as text in " + reinterpretAs.displayName() + ".";
	}
	
	@Override
	protected void startTransformation() {
		encoder = interpretedAs.newEncoder();
		encoder.onMalformedInput(CodingErrorAction.REPLACE);
		encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		decoder = reinterpretAs.newDecoder();
		decoder.onMalformedInput(CodingErrorAction.REPLACE);
		decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		encoderInputBuffer = new ArrayList<Character>();
		decoderInputBuffer = new ArrayList<Byte>();
		lastHighSurrogate = 0;
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		for (char ch : Character.toChars(codePoint)) {
			encoderInputBuffer.add(ch);
		}
		encode(false);
		decode(false);
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
			decoderInputBuffer.add(encoderOutput.get(i));
		}
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
		encode(true);
		decode(true);
		if (lastHighSurrogate != 0) {
			append(lastHighSurrogate);
		}
	}
}
