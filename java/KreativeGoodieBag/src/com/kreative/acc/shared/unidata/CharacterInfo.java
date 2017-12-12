package com.kreative.acc.shared.unidata;

public class CharacterInfo {
	private int codePoint; // 0
	private String characterName; // 1
	private String characterClass; // 2
	private int combiningClass; // 3
	private String bidiClass; // 4
	private String decomposition; // 5
	private Number numericValue; // 8
	private boolean bidiMirrored; // 9
	private String alternateName; // 10
	private int upperCase; // 12
	private int lowerCase; // 13
	private int titleCase; // 14
	
	public CharacterInfo(int cp, String[] data) {
		codePoint = cp;
		characterName = (data.length > 1) ? data[1].trim() : "";
		characterClass = (data.length > 2) ? data[2].trim() : "So";
		combiningClass = (data.length > 3) ? parseInt(data[3].trim(), 0) : 0;
		bidiClass = (data.length > 4) ? data[4].trim() : "ON";
		decomposition = (data.length > 5) ? data[5].trim() : "";
		numericValue = (data.length > 8) ? parseNumber(data[8].trim()) : null;
		bidiMirrored = (data.length > 9) ? data[9].trim().equalsIgnoreCase("Y") : false;
		alternateName = (data.length > 10) ? data[10].trim() : "";
		upperCase = (data.length > 12) ? parseInt16(data[12].trim(), codePoint) : codePoint;
		lowerCase = (data.length > 13) ? parseInt16(data[13].trim(), codePoint) : codePoint;
		titleCase = (data.length > 14) ? parseInt16(data[14].trim(), upperCase) : upperCase;
	}
	
	public int getCodePoint() {
		return codePoint;
	}
	
	public String getCharacterName() {
		return characterName;
	}
	
	public byte getCharacterClass() {
		if (characterClass.equalsIgnoreCase("Mc")) return Character.COMBINING_SPACING_MARK;
		if (characterClass.equalsIgnoreCase("Pc")) return Character.CONNECTOR_PUNCTUATION;
		if (characterClass.equalsIgnoreCase("Cc")) return Character.CONTROL;
		if (characterClass.equalsIgnoreCase("Sc")) return Character.CURRENCY_SYMBOL;
		if (characterClass.equalsIgnoreCase("Pd")) return Character.DASH_PUNCTUATION;
		if (characterClass.equalsIgnoreCase("Nd")) return Character.DECIMAL_DIGIT_NUMBER;
		if (characterClass.equalsIgnoreCase("Me")) return Character.ENCLOSING_MARK;
		if (characterClass.equalsIgnoreCase("Pe")) return Character.END_PUNCTUATION;
		if (characterClass.equalsIgnoreCase("Pf")) return Character.FINAL_QUOTE_PUNCTUATION;
		if (characterClass.equalsIgnoreCase("Cf")) return Character.FORMAT;
		if (characterClass.equalsIgnoreCase("Pi")) return Character.INITIAL_QUOTE_PUNCTUATION;
		if (characterClass.equalsIgnoreCase("Nl")) return Character.LETTER_NUMBER;
		if (characterClass.equalsIgnoreCase("Zl")) return Character.LINE_SEPARATOR;
		if (characterClass.equalsIgnoreCase("Ll")) return Character.LOWERCASE_LETTER;
		if (characterClass.equalsIgnoreCase("Sm")) return Character.MATH_SYMBOL;
		if (characterClass.equalsIgnoreCase("Lm")) return Character.MODIFIER_LETTER;
		if (characterClass.equalsIgnoreCase("Sk")) return Character.MODIFIER_SYMBOL;
		if (characterClass.equalsIgnoreCase("Mn")) return Character.NON_SPACING_MARK;
		if (characterClass.equalsIgnoreCase("Lo")) return Character.OTHER_LETTER;
		if (characterClass.equalsIgnoreCase("No")) return Character.OTHER_NUMBER;
		if (characterClass.equalsIgnoreCase("Po")) return Character.OTHER_PUNCTUATION;
		if (characterClass.equalsIgnoreCase("So")) return Character.OTHER_SYMBOL;
		if (characterClass.equalsIgnoreCase("Zp")) return Character.PARAGRAPH_SEPARATOR;
		if (characterClass.equalsIgnoreCase("Co")) return Character.PRIVATE_USE;
		if (characterClass.equalsIgnoreCase("Zs")) return Character.SPACE_SEPARATOR;
		if (characterClass.equalsIgnoreCase("Ps")) return Character.START_PUNCTUATION;
		if (characterClass.equalsIgnoreCase("Cs")) return Character.SURROGATE;
		if (characterClass.equalsIgnoreCase("Lt")) return Character.TITLECASE_LETTER;
		if (characterClass.equalsIgnoreCase("Cn")) return Character.UNASSIGNED;
		if (characterClass.equalsIgnoreCase("Lu")) return Character.UPPERCASE_LETTER;
		return Character.UNASSIGNED;
	}
	
	public String getCharacterClassString() {
		return characterClass;
	}
	
	public String getCharacterClassDescription() {
		if (characterClass.equalsIgnoreCase("Mc")) return "Combining Spacing Mark";
		if (characterClass.equalsIgnoreCase("Pc")) return "Connecting Punctuation";
		if (characterClass.equalsIgnoreCase("Cc")) return "Control Character";
		if (characterClass.equalsIgnoreCase("Sc")) return "Currency Symbol";
		if (characterClass.equalsIgnoreCase("Pd")) return "Dash Punctuation";
		if (characterClass.equalsIgnoreCase("Nd")) return "Decimal Digit";
		if (characterClass.equalsIgnoreCase("Me")) return "Enclosing Mark";
		if (characterClass.equalsIgnoreCase("Pe")) return "Closing Punctuation";
		if (characterClass.equalsIgnoreCase("Pf")) return "Final Quote Punctuation";
		if (characterClass.equalsIgnoreCase("Cf")) return "Format Character";
		if (characterClass.equalsIgnoreCase("Pi")) return "Initial Quote Punctuation";
		if (characterClass.equalsIgnoreCase("Nl")) return "Letterlike Number";
		if (characterClass.equalsIgnoreCase("Zl")) return "Line Separator";
		if (characterClass.equalsIgnoreCase("Ll")) return "Lowercase Letter";
		if (characterClass.equalsIgnoreCase("Sm")) return "Math Symbol";
		if (characterClass.equalsIgnoreCase("Lm")) return "Modifier Letter";
		if (characterClass.equalsIgnoreCase("Sk")) return "Modifier Symbol";
		if (characterClass.equalsIgnoreCase("Mn")) return "Non-Spacing Mark";
		if (characterClass.equalsIgnoreCase("Lo")) return "Other Letter";
		if (characterClass.equalsIgnoreCase("No")) return "Other Number";
		if (characterClass.equalsIgnoreCase("Po")) return "Other Punctuation";
		if (characterClass.equalsIgnoreCase("So")) return "Other Symbol";
		if (characterClass.equalsIgnoreCase("Zp")) return "Paragraph Separator";
		if (characterClass.equalsIgnoreCase("Co")) return "Private Use";
		if (characterClass.equalsIgnoreCase("Zs")) return "Space Separator";
		if (characterClass.equalsIgnoreCase("Ps")) return "Opening Punctuation";
		if (characterClass.equalsIgnoreCase("Cs")) return "Surrogate";
		if (characterClass.equalsIgnoreCase("Lt")) return "Titlecase Letter";
		if (characterClass.equalsIgnoreCase("Cn")) return "Unassigned";
		if (characterClass.equalsIgnoreCase("Lu")) return "Uppercase Letter";
		return characterClass;
	}
	
	public int getCombiningClass() {
		return combiningClass;
	}
	
	public String getCombiningClassDescription() {
		switch (combiningClass) {
		case 0: return "Spacing and Enclosing Marks";
		case 1: return "Overlay";
		case 7: return "Nukta";
		case 8: return "Kana Voicing";
		case 9: return "Virama";
		case 200: return "Attached Below Left";
		case 202: return "Attached Below";
		case 204: return "Attached Below Right";
		case 208: return "Attached Left";
		case 210: return "Attached Right";
		case 212: return "Attached Above Left";
		case 214: return "Attached Above";
		case 216: return "Attached Above Right";
		case 218: return "Below Left";
		case 220: return "Below";
		case 222: return "Below Right";
		case 224: return "Left";
		case 226: return "Right";
		case 228: return "Above Left";
		case 230: return "Above";
		case 232: return "Above Right";
		case 233: return "Double Below";
		case 234: return "Double Above";
		case 240: return "Greek Iota Subscript";
		default: return Integer.toString(combiningClass);
		}
	}
	
	public byte getBidiClass() {
		if (bidiClass.equalsIgnoreCase("L")) return Character.DIRECTIONALITY_LEFT_TO_RIGHT;
		if (bidiClass.equalsIgnoreCase("LRE")) return Character.DIRECTIONALITY_LEFT_TO_RIGHT_EMBEDDING;
		if (bidiClass.equalsIgnoreCase("LRO")) return Character.DIRECTIONALITY_LEFT_TO_RIGHT_OVERRIDE;
		if (bidiClass.equalsIgnoreCase("R")) return Character.DIRECTIONALITY_RIGHT_TO_LEFT;
		if (bidiClass.equalsIgnoreCase("AL")) return Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
		if (bidiClass.equalsIgnoreCase("RLE")) return Character.DIRECTIONALITY_RIGHT_TO_LEFT_EMBEDDING;
		if (bidiClass.equalsIgnoreCase("RLO")) return Character.DIRECTIONALITY_RIGHT_TO_LEFT_OVERRIDE;
		if (bidiClass.equalsIgnoreCase("PDF")) return Character.DIRECTIONALITY_POP_DIRECTIONAL_FORMAT;
		if (bidiClass.equalsIgnoreCase("EN")) return Character.DIRECTIONALITY_EUROPEAN_NUMBER;
		if (bidiClass.equalsIgnoreCase("ES")) return Character.DIRECTIONALITY_EUROPEAN_NUMBER_SEPARATOR;
		if (bidiClass.equalsIgnoreCase("ET")) return Character.DIRECTIONALITY_EUROPEAN_NUMBER_TERMINATOR;
		if (bidiClass.equalsIgnoreCase("AN")) return Character.DIRECTIONALITY_ARABIC_NUMBER;
		if (bidiClass.equalsIgnoreCase("CS")) return Character.DIRECTIONALITY_COMMON_NUMBER_SEPARATOR;
		if (bidiClass.equalsIgnoreCase("NSM")) return Character.DIRECTIONALITY_NONSPACING_MARK;
		if (bidiClass.equalsIgnoreCase("BN")) return Character.DIRECTIONALITY_BOUNDARY_NEUTRAL;
		if (bidiClass.equalsIgnoreCase("B")) return Character.DIRECTIONALITY_PARAGRAPH_SEPARATOR;
		if (bidiClass.equalsIgnoreCase("S")) return Character.DIRECTIONALITY_SEGMENT_SEPARATOR;
		if (bidiClass.equalsIgnoreCase("WS")) return Character.DIRECTIONALITY_WHITESPACE;
		if (bidiClass.equalsIgnoreCase("ON")) return Character.DIRECTIONALITY_OTHER_NEUTRALS;
		return Character.DIRECTIONALITY_UNDEFINED;
	}
	
	public String getBidiClassString() {
		return bidiClass;
	}
	
	public String getBidiClassDescription() {
		if (bidiClass.equalsIgnoreCase("L")) return "Left-to-Right";
		if (bidiClass.equalsIgnoreCase("LRE")) return "LR Embedding";
		if (bidiClass.equalsIgnoreCase("LRO")) return "LR Override";
		if (bidiClass.equalsIgnoreCase("R")) return "Right-to-Left";
		if (bidiClass.equalsIgnoreCase("AL")) return "Arabic Letter";
		if (bidiClass.equalsIgnoreCase("RLE")) return "RL Embedding";
		if (bidiClass.equalsIgnoreCase("RLO")) return "RL Override";
		if (bidiClass.equalsIgnoreCase("PDF")) return "Pop Directional Format";
		if (bidiClass.equalsIgnoreCase("EN")) return "European Number";
		if (bidiClass.equalsIgnoreCase("ES")) return "European Separator";
		if (bidiClass.equalsIgnoreCase("ET")) return "European Terminator";
		if (bidiClass.equalsIgnoreCase("AN")) return "Arabic Number";
		if (bidiClass.equalsIgnoreCase("CS")) return "Common Separator";
		if (bidiClass.equalsIgnoreCase("NSM")) return "Non-Spacing Mark";
		if (bidiClass.equalsIgnoreCase("BN")) return "Boundary-Neutral";
		if (bidiClass.equalsIgnoreCase("B")) return "Paragraph Separator";
		if (bidiClass.equalsIgnoreCase("S")) return "Segment Separator";
		if (bidiClass.equalsIgnoreCase("WS")) return "Whitespace";
		if (bidiClass.equalsIgnoreCase("ON")) return "Neutral";
		return bidiClass;
	}
	
	public String getDecomposition() {
		return decomposition;
	}
	
	public Number getNumericValue() {
		return numericValue;
	}
	
	public boolean isBidiMirrored() {
		return bidiMirrored;
	}
	
	public String getAlternateName() {
		return alternateName;
	}
	
	public int getUpperCase() {
		return upperCase;
	}
	
	public int getLowerCase() {
		return lowerCase;
	}
	
	public int getTitleCase() {
		return titleCase;
	}
	
	private static int parseInt(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return def;
		}
	}
	
	private static Number parseNumber(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException nfe1) {
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException nfe2) {
				try {
					return Double.parseDouble(s);
				} catch (NumberFormatException nfe3) {
					return null;
				}
			}
		}
	}
	
	private static int parseInt16(String s, int def) {
		try {
			return Integer.parseInt(s, 16);
		} catch (NumberFormatException nfe) {
			return def;
		}
	}
}
