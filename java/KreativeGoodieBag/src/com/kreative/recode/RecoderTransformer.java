package com.kreative.recode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import com.kreative.recode.transformation.TextTransformation;

public class RecoderTransformer extends TextTransformation {
	private final List<TextTransformation> txs = new ArrayList<TextTransformation>();
	private final List<Integer> privateBuffer = new ArrayList<Integer>();
	
	@Override
	public String getName() {
		return "RecoderTransformer";
	}
	
	@Override
	public String getDescription() {
		return "Internal";
	}
	
	@Override
	protected void startTransformation() {
		privateBuffer.clear();
		for (TextTransformation tx : txs) {
			tx.reset();
			for (int cp : privateBuffer) tx.write(cp);
			privateBuffer.clear();
			while (tx.readyForRead()) privateBuffer.add(tx.read());
		}
		for (int cp : privateBuffer) append(cp);
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		privateBuffer.clear();
		privateBuffer.add(codePoint);
		for (TextTransformation tx : txs) {
			for (int cp : privateBuffer) tx.write(cp);
			privateBuffer.clear();
			while (tx.readyForRead()) privateBuffer.add(tx.read());
		}
		for (int cp : privateBuffer) append(cp);
	}
	
	@Override
	protected void stopTransformation() {
		privateBuffer.clear();
		for (TextTransformation tx : txs) {
			for (int cp : privateBuffer) tx.write(cp);
			tx.writeEOF();
			privateBuffer.clear();
			while (tx.readyForRead()) privateBuffer.add(tx.read());
		}
		for (int cp : privateBuffer) append(cp);
	}
	
	public List<TextTransformation> list() {
		return txs;
	}
	
	public String transformString(String in) {
		StringBuffer out = new StringBuffer();
		transformString(in, out);
		return out.toString();
	}
	
	public void transformString(String in, StringBuffer out) {
		reset(); while (readyForRead()) out.append(Character.toChars(read()));
		int inIndex = 0;
		while (inIndex < in.length()) {
			int inChar = in.codePointAt(inIndex);
			write(inChar); while (readyForRead()) out.append(Character.toChars(read()));
			inIndex += Character.charCount(inChar);
		}
		writeEOF(); while (readyForRead()) out.append(Character.toChars(read()));
	}
	
	public void transformStream(Reader in, Writer out) throws IOException {
		reset(); while (readyForRead()) out.write(Character.toChars(read()));
		int lastHighSurrogate = 0;
		while (true) {
			int inChar = in.read();
			if (inChar < 0) {
				if (lastHighSurrogate != 0) {
					write(lastHighSurrogate); while (readyForRead()) out.write(Character.toChars(read()));
				}
				writeEOF(); while (readyForRead()) out.write(Character.toChars(read()));
				out.flush();
				return;
			} else if (Character.isHighSurrogate((char)inChar)) {
				if (lastHighSurrogate != 0) {
					write(lastHighSurrogate); while (readyForRead()) out.write(Character.toChars(read()));
				}
				lastHighSurrogate = inChar;
			} else if (Character.isLowSurrogate((char)inChar)) {
				if (lastHighSurrogate != 0) {
					inChar = Character.toCodePoint((char)lastHighSurrogate, (char)inChar);
					lastHighSurrogate = 0;
				}
				write(inChar); while (readyForRead()) out.write(Character.toChars(read()));
			} else {
				if (lastHighSurrogate != 0) {
					write(lastHighSurrogate); while (readyForRead()) out.write(Character.toChars(read()));
					lastHighSurrogate = 0;
				}
				write(inChar); while (readyForRead()) out.write(Character.toChars(read()));
			}
		}
	}
	
	public void recodeTransformStream(InputStream in, OutputStream out, String inEncoding, String outEncoding) throws IOException {
		Reader inr = new InputStreamReader(in, inEncoding);
		Writer outw = new OutputStreamWriter(out, outEncoding);
		transformStream(inr, outw);
	}
	
	public void recodeTransformStream(InputStream in, OutputStream out, Charset inEncoding, Charset outEncoding) throws IOException {
		Reader inr = new InputStreamReader(in, inEncoding);
		Writer outw = new OutputStreamWriter(out, outEncoding);
		transformStream(inr, outw);
	}
}
