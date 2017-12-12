package com.kreative.recode.misc;

public class CipherLetter {
	final int finalUpper;
	final int medialUpper;
	final int finalLower;
	final int medialLower;
	
	CipherLetter(int finalUpper, int medialUpper, int finalLower, int medialLower) {
		this.finalUpper = finalUpper;
		this.medialUpper = medialUpper;
		this.finalLower = finalLower;
		this.medialLower = medialLower;
	}
	
	public boolean isPositionIndependent(boolean upperCase) {
		return upperCase ? (finalUpper == medialUpper) : (finalLower == medialLower);
	}
	
	public int toCodePoint(boolean upperCase, boolean finalForm) {
		return upperCase ? (finalForm ? finalUpper : medialUpper) : (finalForm ? finalLower : medialLower);
	}
}
