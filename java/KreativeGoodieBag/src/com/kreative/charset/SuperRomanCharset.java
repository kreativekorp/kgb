package com.kreative.charset;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.List;

public class SuperRomanCharset extends Charset {
	private static final String CANONICAL_NAME = "X-KK-SuperRoman";
	private static final String CANONICAL_NAME_C0 = "X-KK-SuperRoman-C0";
	
	private static final String[] ALIASES = {
		"X-SuperRoman", "X-Kreative-SuperRoman"
	};
	private static final String[] ALIASES_C0 = {
		"X-SuperRoman-C0", "X-Kreative-SuperRoman-C0"
	};
	
	private static final List<String> KNOWN_SUBSETS = Arrays.asList(
			"us-ascii", "iso-8859-1", "windows-1252", "macroman", "x-nextstep",
			"x-kk-superlatin", "x-kk-superroman", "x-kk-supermultinational", "x-kk-super437",
			"x-kk-superlatin-c0", "x-kk-superroman-c0", "x-kk-supermultinational-c0", "x-kk-super437-c0"
	);
	private static final List<String> KNOWN_SUBSETS_C0 = Arrays.asList(
			"us-ascii", "macroman", "x-kk-superroman-c0"
	);
	
	private final List<String> knownSubsets;
	private final boolean overrideC0;
	
	public SuperRomanCharset(boolean overrideC0) {
		super(
				overrideC0 ? CANONICAL_NAME : CANONICAL_NAME_C0,
				overrideC0 ? ALIASES : ALIASES_C0
		);
		this.knownSubsets = overrideC0 ? KNOWN_SUBSETS : KNOWN_SUBSETS_C0;
		this.overrideC0 = overrideC0;
	}

	@Override
	public boolean contains(Charset cs) {
		return knownSubsets.contains(cs.name().toLowerCase());
	}

	@Override
	public CharsetDecoder newDecoder() {
		return new SuperRomanDecoder(this, overrideC0);
	}

	@Override
	public CharsetEncoder newEncoder() {
		return new SuperRomanEncoder(this, overrideC0);
	}
}
