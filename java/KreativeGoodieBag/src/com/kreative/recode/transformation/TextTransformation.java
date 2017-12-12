package com.kreative.recode.transformation;

import java.util.ArrayList;
import java.util.List;

public abstract class TextTransformation {
	private List<Integer> buffer = new ArrayList<Integer>();
	private boolean reachedEOF = false;
	
	// EXTERNAL API
	
	public abstract String getName();
	public abstract String getDescription();
	
	public final synchronized void reset() {
		buffer = new ArrayList<Integer>();
		reachedEOF = false;
		startTransformation();
	}
	
	public final synchronized void write(int codePoint) {
		if (!reachedEOF) {
			if (codePoint >= 0) {
				transformCodePoint(codePoint);
			} else {
				stopTransformation();
				reachedEOF = true;
			}
		}
	}
	
	public final synchronized void writeEOF() {
		if (!reachedEOF) {
			stopTransformation();
			reachedEOF = true;
		}
	}
	
	public final synchronized boolean readyForRead() {
		return !buffer.isEmpty();
	}
	
	public final synchronized int read() {
		if (buffer.isEmpty()) {
			return reachedEOF ? -1 : -2;
		} else {
			return buffer.remove(0);
		}
	}
	
	@Override
	public final String toString() {
		return getName();
	}
	
	// INTERNAL DOWNSTREAM API (FROM BASE CLASS DOWN TO IMPLEMENTATION)
	
	protected abstract void startTransformation();
	protected abstract void transformCodePoint(int codePoint);
	protected abstract void stopTransformation();
	
	// INTERNAL UPSTREAM API (FROM IMPLEMENTATION UP TO BASE CLASS)
	
	protected final void append(int codePoint) {
		buffer.add(codePoint);
	}
	
	protected final void append(List<Integer> codePoints) {
		buffer.addAll(codePoints);
	}
	
	protected final void append(String s) {
		int i = 0, m = s.length();
		while (i < m) {
			int codePoint = s.codePointAt(i);
			buffer.add(codePoint);
			i += Character.charCount(codePoint);
		}
	}
}
