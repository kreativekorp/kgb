package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.misc.CipherScript;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class CaesarCipherTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "Caesar Cipher";
	}
	
	@Override
	public String getDescription() {
		return "Performs a Caesar cipher on plain " + CipherScript.listScripts() + " letters.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-cc", "--cc", "-rot", "--rot", "-caesar", "--caesar", "-caesarcipher", "--caesarcipher");
	}
	
	@Override
	public int getArgumentCount() {
		return 2;
	}
	
	@Override
	public List<String> getArgumentNames() {
		return Arrays.asList("alphabet", "shift");
	}
	
	@Override
	public List<String> getArgumentDescriptions() {
		return Arrays.asList("The alphabet to shift. One of " + CipherScript.listScripts() + ".", "The shift to be used.");
	}
	
	@Override
	public TextTransformation createTransformation(List<String> args) {
		CipherScript script;
		try {
			script = CipherScript.valueOf(args.get(0).toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error: Alphabet for Caesar Cipher must be one of " + CipherScript.listScripts() + ".");
		}
		int shift;
		try {
			shift = Integer.parseInt(args.get(1));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Error: Shift for Caesar Cipher must be a number.");
		}
		return new CaesarCipherTransformation(script, shift);
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return new CaesarCipherTransformationGUI(CipherScript.LATIN, 13);
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		CipherScript script;
		try {
			script = CipherScript.valueOf(args.get(0).toUpperCase());
		} catch (IllegalArgumentException e) {
			script = CipherScript.LATIN;
		}
		int shift;
		try {
			shift = Integer.parseInt(args.get(1));
		} catch (NumberFormatException e) {
			shift = script.size() / 2;
		}
		return new CaesarCipherTransformationGUI(script, shift);
	}
}
