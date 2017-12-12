package com.kreative.recode.transformations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.kreative.recode.misc.CipherScript;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class AtbashCipherTransformationFactory extends TextTransformationFactory {
	@Override
	public String getName() {
		return "Atbash Cipher";
	}
	
	@Override
	public String getDescription() {
		return "Performs an Atbash cipher on plain " + CipherScript.listScripts() + " letters.";
	}
	
	@Override
	public Collection<String> getFlags() {
		return Arrays.asList("-ac", "--ac", "-atbash", "--atbash", "-atbashcipher", "--atbashcipher");
	}
	
	@Override
	public int getArgumentCount() {
		return 1;
	}
	
	@Override
	public List<String> getArgumentNames() {
		return Arrays.asList("alphabet");
	}
	
	@Override
	public List<String> getArgumentDescriptions() {
		return Arrays.asList("The alphabet to flip. One of " + CipherScript.listScripts() + ".");
	}
	
	@Override
	public TextTransformation createTransformation(List<String> args) {
		CipherScript script;
		try {
			script = CipherScript.valueOf(args.get(0).toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Error: Alphabet for Atbash Cipher must be one of " + CipherScript.listScripts() + ".");
		}
		return new AtbashCipherTransformation(script);
	}
	
	@Override
	public TextTransformationGUI createGUI() {
		return new AtbashCipherTransformationGUI(CipherScript.LATIN);
	}
	
	@Override
	public TextTransformationGUI createGUI(List<String> args) {
		CipherScript script;
		try {
			script = CipherScript.valueOf(args.get(0).toUpperCase());
		} catch (IllegalArgumentException e) {
			script = CipherScript.LATIN;
		}
		return new AtbashCipherTransformationGUI(script);
	}
}
