package com.kreative.recode.transformations;

import com.kreative.recode.misc.CipherScript;
import com.kreative.recode.transformation.TextTransformation;

public class CaesarCipherTransformation extends TextTransformation {
	private final CipherScript script;
	private final int shift;
	private boolean hasLast;
	private int lastOrdinal;
	private boolean lastCase;
	
	public CaesarCipherTransformation(CipherScript script, int shift) {
		this.script = script;
		this.shift = shift;
		this.hasLast = false;
		this.lastOrdinal = 0;
		this.lastCase = false;
	}
	
	@Override
	public String getName() {
		return "Caesar Cipher with Shift " + shift + " on " + script;
	}
	
	@Override
	public String getDescription() {
		return "Performs a Caesar cipher with shift " + shift + " on plain " + script + " letters.";
	}
	
	@Override
	protected void startTransformation() {
		this.hasLast = false;
		this.lastOrdinal = 0;
		this.lastCase = false;
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		if (script.contains(codePoint)) {
			appendLast(false);
			hasLast = true;
			lastOrdinal = script.getOrdinal(codePoint);
			lastOrdinal += shift;
			while (lastOrdinal < 0) lastOrdinal += script.size();
			while (lastOrdinal >= script.size()) lastOrdinal -= script.size();
			lastCase = script.isUpperCase(codePoint);
			if (script.getLetter(lastOrdinal).isPositionIndependent(lastCase)) {
				appendLast(false);
			}
		} else {
			appendLast(true);
			append(codePoint);
		}
	}
	
	@Override
	protected void stopTransformation() {
		appendLast(true);
	}
	
	private void appendLast(boolean finalForm) {
		if (hasLast) {
			append(script.getLetter(lastOrdinal).toCodePoint(lastCase, finalForm));
			hasLast = false;
		}
	}
}
