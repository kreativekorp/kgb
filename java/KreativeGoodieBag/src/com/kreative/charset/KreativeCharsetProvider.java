package com.kreative.charset;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class KreativeCharsetProvider extends CharsetProvider {
	private final List<Charset> charsets;
	
	public KreativeCharsetProvider() {
		charsets = Arrays.asList(
				new SuperLatinCharset(true), new SuperLatinCharset(false),
				new SuperMultinationalCharset(true), new SuperMultinationalCharset(false),
				new SuperRomanCharset(true), new SuperRomanCharset(false),
				new Super437Charset(true), new Super437Charset(false)
		);
	}
	
	@Override
	public Charset charsetForName(String name) {
		for (Charset charset : charsets) {
			if (charset.name().equalsIgnoreCase(name)) {
				return charset;
			}
			for (String alias : charset.aliases()) {
				if (alias.equalsIgnoreCase(name)) {
					return charset;
				}
			}
		}
		return null;
	}
	
	@Override
	public Iterator<Charset> charsets() {
		return charsets.iterator();
	}
}
