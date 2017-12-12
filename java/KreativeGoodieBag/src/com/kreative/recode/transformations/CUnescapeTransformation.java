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

public class CUnescapeTransformation extends TextTransformation {
	private final boolean ignoreASCII;
	private final Charset encoding;
	private CharsetDecoder decoder;
	private List<Byte> decoderInputBuffer;
	private boolean inEscape;
	private boolean inOctal;
	private boolean inHex;
	private StringBuffer cps;
	private char lastHighSurrogate;
	
	public CUnescapeTransformation(boolean ignoreASCII, String encoding) {
		this.ignoreASCII = ignoreASCII;
		try {
			this.encoding = Charset.forName(encoding);
		} catch (UnsupportedCharsetException e) {
			throw new IllegalArgumentException("Error: Unknown encoding \"" + e.getCharsetName() + "\" specified for C/C++ Unescape.");
		}
		startTransformation();
	}
	
	public CUnescapeTransformation(boolean ignoreASCII, Charset encoding) {
		this.ignoreASCII = ignoreASCII;
		if (encoding == null) {
			throw new IllegalArgumentException("Error: Unknown encoding specified for C/C++ Unescape.");
		} else {
			this.encoding = encoding;
		}
		startTransformation();
	}
	
	@Override
	public String getName() {
		if (ignoreASCII) {
			return "C/C++ Unescape (Ignore ASCII) using " + encoding.displayName();
		} else {
			return "C/C++ Unescape using " + encoding.displayName();
		}
	}
	
	@Override
	public String getDescription() {
		if (ignoreASCII) {
			return "Decodes only C/C++ escape sequences that represent non-ASCII characters using " + encoding.displayName() + ".";
		} else {
			return "Decodes all C/C++ escape sequences using " + encoding.displayName() + ".";
		}
	}
	
	@Override
	protected void startTransformation() {
		decoder = encoding.newDecoder();
		decoder.onMalformedInput(CodingErrorAction.REPLACE);
		decoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
		decoderInputBuffer = new ArrayList<Byte>();
		inEscape = false;
		inOctal = false;
		inHex = false;
		cps = new StringBuffer();
		lastHighSurrogate = 0;
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		if (inHex) {
			if ((codePoint >= '0' && codePoint <= '9') || (codePoint >= 'A' && codePoint <= 'F') || (codePoint >= 'a' && codePoint <= 'f')) {
				cps.append((char)codePoint);
				if (cps.length() >= 2) {
					int v = Integer.parseInt(cps.toString(), 16);
					if (ignoreASCII && v < 0x80) {
						decoderInputBuffer.add((byte)'\\');
						decoderInputBuffer.add((byte)'x');
						for (char ch : cps.toString().toCharArray()) {
							decoderInputBuffer.add((byte)ch);
						}
					} else {
						decoderInputBuffer.add((byte)v);
					}
					inEscape = false;
					inOctal = false;
					inHex = false;
					cps = new StringBuffer();
				}
			} else {
				decoderInputBuffer.add((byte)'\\');
				decoderInputBuffer.add((byte)'x');
				for (char ch : cps.toString().toCharArray()) {
					decoderInputBuffer.add((byte)ch);
				}
				inEscape = false;
				inOctal = false;
				inHex = false;
				cps = new StringBuffer();
				if (codePoint == '\\') {
					inEscape = true;
				} else if (codePoint < 0x80) {
					decoderInputBuffer.add((byte)codePoint);
				} else {
					for (byte b : new String(Character.toChars(codePoint)).getBytes(encoding)) {
						decoderInputBuffer.add(b);
					}
				}
			}
		} else if (inOctal) {
			if (codePoint >= '0' && codePoint <= '7') {
				cps.append((char)codePoint);
				if (cps.length() >= 3) {
					int v = Integer.parseInt(cps.toString(), 8);
					if (ignoreASCII && v < 0x80) {
						decoderInputBuffer.add((byte)'\\');
						for (char ch : cps.toString().toCharArray()) {
							decoderInputBuffer.add((byte)ch);
						}
					} else {
						decoderInputBuffer.add((byte)v);
					}
					inEscape = false;
					inOctal = false;
					inHex = false;
					cps = new StringBuffer();
				}
			} else {
				int v = Integer.parseInt(cps.toString(), 8);
				if (ignoreASCII && v < 0x80) {
					decoderInputBuffer.add((byte)'\\');
					for (char ch : cps.toString().toCharArray()) {
						decoderInputBuffer.add((byte)ch);
					}
				} else {
					decoderInputBuffer.add((byte)v);
				}
				inEscape = false;
				inOctal = false;
				inHex = false;
				cps = new StringBuffer();
				if (codePoint == '\\') {
					inEscape = true;
				} else if (codePoint < 0x80) {
					decoderInputBuffer.add((byte)codePoint);
				} else {
					for (byte b : new String(Character.toChars(codePoint)).getBytes(encoding)) {
						decoderInputBuffer.add(b);
					}
				}
			}
		} else if (inEscape) {
			if (ignoreASCII) {
				switch (codePoint) {
				case '0': case '1': case '2': case '3':
				case '4': case '5': case '6': case '7':
					inOctal = true;
					cps.append((char)codePoint);
					break;
				case 'x':
					inHex = true;
					break;
				default:
					decoderInputBuffer.add((byte)'\\');
					if (codePoint < 0x80) {
						decoderInputBuffer.add((byte)codePoint);
					} else {
						for (byte b : new String(Character.toChars(codePoint)).getBytes(encoding)) {
							decoderInputBuffer.add(b);
						}
					}
					inEscape = false;
					break;
				}
			} else {
				switch (codePoint) {
				case 'a': decoderInputBuffer.add((byte)'\u0007'); inEscape = false; break;
				case 'b': decoderInputBuffer.add((byte)'\b'); inEscape = false; break;
				case 't': decoderInputBuffer.add((byte)'\t'); inEscape = false; break;
				case 'n': decoderInputBuffer.add((byte)'\n'); inEscape = false; break;
				case 'v': decoderInputBuffer.add((byte)'\u000B'); inEscape = false; break;
				case 'f': decoderInputBuffer.add((byte)'\f'); inEscape = false; break;
				case 'r': decoderInputBuffer.add((byte)'\r'); inEscape = false; break;
				case '\"': decoderInputBuffer.add((byte)'\"'); inEscape = false; break;
				case '\'': decoderInputBuffer.add((byte)'\''); inEscape = false; break;
				case '?': decoderInputBuffer.add((byte)'?'); inEscape = false; break;
				case '\\': decoderInputBuffer.add((byte)'\\'); inEscape = false; break;
				case '0': case '1': case '2': case '3':
				case '4': case '5': case '6': case '7':
					inOctal = true;
					cps.append((char)codePoint);
					break;
				case 'x':
					inHex = true;
					break;
				default:
					decoderInputBuffer.add((byte)'\\');
					if (codePoint < 0x80) {
						decoderInputBuffer.add((byte)codePoint);
					} else {
						for (byte b : new String(Character.toChars(codePoint)).getBytes(encoding)) {
							decoderInputBuffer.add(b);
						}
					}
					inEscape = false;
					break;
				}
			}
		} else {
			if (codePoint == '\\') {
				inEscape = true;
			} else if (codePoint < 0x80) {
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
		if (inHex) {
			decoderInputBuffer.add((byte)'\\');
			decoderInputBuffer.add((byte)'x');
			for (char ch : cps.toString().toCharArray()) {
				decoderInputBuffer.add((byte)ch);
			}
		} else if (inOctal) {
			int v = Integer.parseInt(cps.toString(), 8);
			if (ignoreASCII && v < 0x80) {
				decoderInputBuffer.add((byte)'\\');
				for (char ch : cps.toString().toCharArray()) {
					decoderInputBuffer.add((byte)ch);
				}
			} else {
				decoderInputBuffer.add((byte)v);
			}
		} else if (inEscape) {
			decoderInputBuffer.add((byte)'\\');
		}
		decode(true);
		if (lastHighSurrogate != 0) {
			append(lastHighSurrogate);
		}
	}
}
