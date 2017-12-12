package com.kreative.recode.transformations;

import com.kreative.recode.misc.CipherScript;
import com.kreative.recode.transformation.TextTransformation;

public class AtbashCipherTransformation extends TextTransformation {
	private final CipherScript script;
	private boolean hasLast;
	private int lastOrdinal;
	private boolean lastCase;
	
	public AtbashCipherTransformation(CipherScript script) {
		this.script = script;
		this.hasLast = false;
		this.lastOrdinal = 0;
		this.lastCase = false;
	}
	
	@Override
	public String getName() {
		return "Atbash Cipher on " + script;
	}
	
	@Override
	public String getDescription() {
		return "Performs an Atbash cipher on plain " + script + " letters.";
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
			lastOrdinal = (script.size() - 1) - lastOrdinal;
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
