package com.kreative.recode.transformations;

import java.util.HashMap;
import java.util.Map;
import com.kreative.recode.transformation.TextTransformation;

public class HTMLDecodeTransformation extends TextTransformation {
	private final Map<String,String> entityMap;
	private final boolean ignoreASCII;
	private StringBuffer entityName;
	
	public HTMLDecodeTransformation(boolean ignoreASCII) {
		this.entityMap = new HashMap<String,String>();
		this.entityMap.put("amp", "&");
		this.entityMap.put("lt", "<");
		this.entityMap.put("gt", ">");
		this.entityMap.put("quot", "\"");
		this.entityMap.put("apos", "\'");
		this.entityMap.put("nbsp", "\u00A0");
		this.entityMap.put("iexcl", "\u00A1");
		this.entityMap.put("cent", "\u00A2");
		this.entityMap.put("pound", "\u00A3");
		this.entityMap.put("curren", "\u00A4");
		this.entityMap.put("yen", "\u00A5");
		this.entityMap.put("brvbar", "\u00A6");
		this.entityMap.put("sect", "\u00A7");
		this.entityMap.put("uml", "\u00A8");
		this.entityMap.put("copy", "\u00A9");
		this.entityMap.put("ordf", "\u00AA");
		this.entityMap.put("laquo", "\u00AB");
		this.entityMap.put("not", "\u00AC");
		this.entityMap.put("shy", "\u00AD");
		this.entityMap.put("reg", "\u00AE");
		this.entityMap.put("macr", "\u00AF");
		this.entityMap.put("deg", "\u00B0");
		this.entityMap.put("plusmn", "\u00B1");
		this.entityMap.put("sup2", "\u00B2");
		this.entityMap.put("sup3", "\u00B3");
		this.entityMap.put("acute", "\u00B4");
		this.entityMap.put("micro", "\u00B5");
		this.entityMap.put("para", "\u00B6");
		this.entityMap.put("middot", "\u00B7");
		this.entityMap.put("cedil", "\u00B8");
		this.entityMap.put("sup1", "\u00B9");
		this.entityMap.put("ordm", "\u00BA");
		this.entityMap.put("raquo", "\u00BB");
		this.entityMap.put("frac14", "\u00BC");
		this.entityMap.put("frac12", "\u00BD");
		this.entityMap.put("frac34", "\u00BE");
		this.entityMap.put("iquest", "\u00BF");
		this.entityMap.put("Agrave", "\u00C0");
		this.entityMap.put("Aacute", "\u00C1");
		this.entityMap.put("Acirc", "\u00C2");
		this.entityMap.put("Atilde", "\u00C3");
		this.entityMap.put("Auml", "\u00C4");
		this.entityMap.put("Aring", "\u00C5");
		this.entityMap.put("AElig", "\u00C6");
		this.entityMap.put("Ccedil", "\u00C7");
		this.entityMap.put("Egrave", "\u00C8");
		this.entityMap.put("Eacute", "\u00C9");
		this.entityMap.put("Ecirc", "\u00CA");
		this.entityMap.put("Euml", "\u00CB");
		this.entityMap.put("Igrave", "\u00CC");
		this.entityMap.put("Iacute", "\u00CD");
		this.entityMap.put("Icirc", "\u00CE");
		this.entityMap.put("Iuml", "\u00CF");
		this.entityMap.put("ETH", "\u00D0");
		this.entityMap.put("Ntilde", "\u00D1");
		this.entityMap.put("Ograve", "\u00D2");
		this.entityMap.put("Oacute", "\u00D3");
		this.entityMap.put("Ocirc", "\u00D4");
		this.entityMap.put("Otilde", "\u00D5");
		this.entityMap.put("Ouml", "\u00D6");
		this.entityMap.put("times", "\u00D7");
		this.entityMap.put("Oslash", "\u00D8");
		this.entityMap.put("Ugrave", "\u00D9");
		this.entityMap.put("Uacute", "\u00DA");
		this.entityMap.put("Ucirc", "\u00DB");
		this.entityMap.put("Uuml", "\u00DC");
		this.entityMap.put("Yacute", "\u00DD");
		this.entityMap.put("THORN", "\u00DE");
		this.entityMap.put("szlig", "\u00DF");
		this.entityMap.put("agrave", "\u00E0");
		this.entityMap.put("aacute", "\u00E1");
		this.entityMap.put("acirc", "\u00E2");
		this.entityMap.put("atilde", "\u00E3");
		this.entityMap.put("auml", "\u00E4");
		this.entityMap.put("aring", "\u00E5");
		this.entityMap.put("aelig", "\u00E6");
		this.entityMap.put("ccedil", "\u00E7");
		this.entityMap.put("egrave", "\u00E8");
		this.entityMap.put("eacute", "\u00E9");
		this.entityMap.put("ecirc", "\u00EA");
		this.entityMap.put("euml", "\u00EB");
		this.entityMap.put("igrave", "\u00EC");
		this.entityMap.put("iacute", "\u00ED");
		this.entityMap.put("icirc", "\u00EE");
		this.entityMap.put("iuml", "\u00EF");
		this.entityMap.put("eth", "\u00F0");
		this.entityMap.put("ntilde", "\u00F1");
		this.entityMap.put("ograve", "\u00F2");
		this.entityMap.put("oacute", "\u00F3");
		this.entityMap.put("ocirc", "\u00F4");
		this.entityMap.put("otilde", "\u00F5");
		this.entityMap.put("ouml", "\u00F6");
		this.entityMap.put("divide", "\u00F7");
		this.entityMap.put("oslash", "\u00F8");
		this.entityMap.put("ugrave", "\u00F9");
		this.entityMap.put("uacute", "\u00FA");
		this.entityMap.put("ucirc", "\u00FB");
		this.entityMap.put("uuml", "\u00FC");
		this.entityMap.put("yacute", "\u00FD");
		this.entityMap.put("thorn", "\u00FE");
		this.entityMap.put("yuml", "\u00FF");
		this.entityMap.put("OElig", "\u0152");
		this.entityMap.put("oelig", "\u0153");
		this.entityMap.put("Scaron", "\u0160");
		this.entityMap.put("scaron", "\u0161");
		this.entityMap.put("Yuml", "\u0178");
		this.entityMap.put("fnof", "\u0192");
		this.entityMap.put("circ", "\u02C6");
		this.entityMap.put("tilde", "\u02DC");
		this.entityMap.put("Alpha", "\u0391");
		this.entityMap.put("Beta", "\u0392");
		this.entityMap.put("Gamma", "\u0393");
		this.entityMap.put("Delta", "\u0394");
		this.entityMap.put("Epsilon", "\u0395");
		this.entityMap.put("Zeta", "\u0396");
		this.entityMap.put("Eta", "\u0397");
		this.entityMap.put("Theta", "\u0398");
		this.entityMap.put("Iota", "\u0399");
		this.entityMap.put("Kappa", "\u039A");
		this.entityMap.put("Lambda", "\u039B");
		this.entityMap.put("Mu", "\u039C");
		this.entityMap.put("Nu", "\u039D");
		this.entityMap.put("Xi", "\u039E");
		this.entityMap.put("Omicron", "\u039F");
		this.entityMap.put("Pi", "\u03A0");
		this.entityMap.put("Rho", "\u03A1");
		this.entityMap.put("Sigma", "\u03A3");
		this.entityMap.put("Tau", "\u03A4");
		this.entityMap.put("Upsilon", "\u03A5");
		this.entityMap.put("Phi", "\u03A6");
		this.entityMap.put("Chi", "\u03A7");
		this.entityMap.put("Psi", "\u03A8");
		this.entityMap.put("Omega", "\u03A9");
		this.entityMap.put("alpha", "\u03B1");
		this.entityMap.put("beta", "\u03B2");
		this.entityMap.put("gamma", "\u03B3");
		this.entityMap.put("delta", "\u03B4");
		this.entityMap.put("epsilon", "\u03B5");
		this.entityMap.put("zeta", "\u03B6");
		this.entityMap.put("eta", "\u03B7");
		this.entityMap.put("theta", "\u03B8");
		this.entityMap.put("iota", "\u03B9");
		this.entityMap.put("kappa", "\u03BA");
		this.entityMap.put("lambda", "\u03BB");
		this.entityMap.put("mu", "\u03BC");
		this.entityMap.put("nu", "\u03BD");
		this.entityMap.put("xi", "\u03BE");
		this.entityMap.put("omicron", "\u03BF");
		this.entityMap.put("pi", "\u03C0");
		this.entityMap.put("rho", "\u03C1");
		this.entityMap.put("sigmaf", "\u03C2");
		this.entityMap.put("sigma", "\u03C3");
		this.entityMap.put("tau", "\u03C4");
		this.entityMap.put("upsilon", "\u03C5");
		this.entityMap.put("phi", "\u03C6");
		this.entityMap.put("chi", "\u03C7");
		this.entityMap.put("psi", "\u03C8");
		this.entityMap.put("omega", "\u03C9");
		this.entityMap.put("thetasym", "\u03D1");
		this.entityMap.put("upsih", "\u03D2");
		this.entityMap.put("piv", "\u03D6");
		this.entityMap.put("ensp", "\u2002");
		this.entityMap.put("emsp", "\u2003");
		this.entityMap.put("thinsp", "\u2009");
		this.entityMap.put("zwnj", "\u200C");
		this.entityMap.put("zwj", "\u200D");
		this.entityMap.put("lrm", "\u200E");
		this.entityMap.put("rlm", "\u200F");
		this.entityMap.put("ndash", "\u2013");
		this.entityMap.put("mdash", "\u2014");
		this.entityMap.put("lsquo", "\u2018");
		this.entityMap.put("rsquo", "\u2019");
		this.entityMap.put("sbquo", "\u201A");
		this.entityMap.put("ldquo", "\u201C");
		this.entityMap.put("rdquo", "\u201D");
		this.entityMap.put("bdquo", "\u201E");
		this.entityMap.put("dagger", "\u2020");
		this.entityMap.put("Dagger", "\u2021");
		this.entityMap.put("bull", "\u2022");
		this.entityMap.put("hellip", "\u2026");
		this.entityMap.put("permil", "\u2030");
		this.entityMap.put("prime", "\u2032");
		this.entityMap.put("Prime", "\u2033");
		this.entityMap.put("lsaquo", "\u2039");
		this.entityMap.put("rsaquo", "\u203A");
		this.entityMap.put("oline", "\u203E");
		this.entityMap.put("frasl", "\u2044");
		this.entityMap.put("euro", "\u20AC");
		this.entityMap.put("image", "\u2111");
		this.entityMap.put("weierp", "\u2118");
		this.entityMap.put("real", "\u211C");
		this.entityMap.put("trade", "\u2122");
		this.entityMap.put("alefsym", "\u2135");
		this.entityMap.put("larr", "\u2190");
		this.entityMap.put("uarr", "\u2191");
		this.entityMap.put("rarr", "\u2192");
		this.entityMap.put("darr", "\u2193");
		this.entityMap.put("harr", "\u2194");
		this.entityMap.put("crarr", "\u21B5");
		this.entityMap.put("lArr", "\u21D0");
		this.entityMap.put("uArr", "\u21D1");
		this.entityMap.put("rArr", "\u21D2");
		this.entityMap.put("dArr", "\u21D3");
		this.entityMap.put("hArr", "\u21D4");
		this.entityMap.put("forall", "\u2200");
		this.entityMap.put("part", "\u2202");
		this.entityMap.put("exist", "\u2203");
		this.entityMap.put("empty", "\u2205");
		this.entityMap.put("nabla", "\u2207");
		this.entityMap.put("isin", "\u2208");
		this.entityMap.put("notin", "\u2209");
		this.entityMap.put("ni", "\u220B");
		this.entityMap.put("prod", "\u220F");
		this.entityMap.put("sum", "\u2211");
		this.entityMap.put("minus", "\u2212");
		this.entityMap.put("lowast", "\u2217");
		this.entityMap.put("radic", "\u221A");
		this.entityMap.put("prop", "\u221D");
		this.entityMap.put("infin", "\u221E");
		this.entityMap.put("ang", "\u2220");
		this.entityMap.put("and", "\u2227");
		this.entityMap.put("or", "\u2228");
		this.entityMap.put("cap", "\u2229");
		this.entityMap.put("cup", "\u222A");
		this.entityMap.put("int", "\u222B");
		this.entityMap.put("there4", "\u2234");
		this.entityMap.put("sim", "\u223C");
		this.entityMap.put("cong", "\u2245");
		this.entityMap.put("asymp", "\u2248");
		this.entityMap.put("ne", "\u2260");
		this.entityMap.put("equiv", "\u2261");
		this.entityMap.put("le", "\u2264");
		this.entityMap.put("ge", "\u2265");
		this.entityMap.put("sub", "\u2282");
		this.entityMap.put("sup", "\u2283");
		this.entityMap.put("nsub", "\u2284");
		this.entityMap.put("sube", "\u2286");
		this.entityMap.put("supe", "\u2287");
		this.entityMap.put("oplus", "\u2295");
		this.entityMap.put("otimes", "\u2297");
		this.entityMap.put("perp", "\u22A5");
		this.entityMap.put("sdot", "\u22C5");
		this.entityMap.put("lceil", "\u2308");
		this.entityMap.put("rceil", "\u2309");
		this.entityMap.put("lfloor", "\u230A");
		this.entityMap.put("rfloor", "\u230B");
		this.entityMap.put("lang", "\u2329");
		this.entityMap.put("rang", "\u232A");
		this.entityMap.put("loz", "\u25CA");
		this.entityMap.put("spades", "\u2660");
		this.entityMap.put("clubs", "\u2663");
		this.entityMap.put("hearts", "\u2665");
		this.entityMap.put("diams", "\u2666");
		this.ignoreASCII = ignoreASCII;
		this.entityName = new StringBuffer();
	}
	
	@Override
	public String getName() {
		if (ignoreASCII) {
			return "HTML Decode (Ignore ASCII)";
		} else {
			return "HTML Decode";
		}
	}
	
	@Override
	public String getDescription() {
		if (ignoreASCII) {
			return "Decodes only HTML entities that represent non-ASCII characters.";
		} else {
			return "Decodes all HTML entities.";
		}
	}
	
	@Override
	protected void startTransformation() {
		entityName = new StringBuffer();
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		if (codePoint == '&') {
			if (entityName.length() > 0) {
				append(entityName.toString());
				entityName = new StringBuffer();
			}
			entityName.append(Character.toChars(codePoint));
		} else if (codePoint <= 32 || codePoint >= 127) {
			if (entityName.length() > 0) {
				append(entityName.toString());
				entityName = new StringBuffer();
			}
			append(codePoint);
		} else {
			if (entityName.length() > 0) {
				entityName.append(Character.toChars(codePoint));
				if (codePoint == ';') {
					String en = entityName.toString();
					if (en.startsWith("&#x") || en.startsWith("&#X")) {
						try {
							int cp = Integer.parseInt(en.substring(3, en.length()-1), 16);
							if (ignoreASCII && cp < 0x80) append(en);
							else append(cp);
						} catch (NumberFormatException nfe) {
							append(en);
						}
					} else if (en.startsWith("&#")) {
						try {
							int cp = Integer.parseInt(en.substring(2, en.length()-1));
							if (ignoreASCII && cp < 0x80) append(en);
							else append(cp);
						} catch (NumberFormatException nfe) {
							append(en);
						}
					} else {
						String k = en.substring(1, en.length()-1);
						if (entityMap.containsKey(k)) {
							String v = entityMap.get(k);
							if (ignoreASCII && v.charAt(0) < 0x80) append(en);
							else append(v);
						} else {
							append(en);
						}
					}
					entityName = new StringBuffer();
				}
			} else {
				append(codePoint);
			}
		}
	}
	
	@Override
	protected void stopTransformation() {
		if (entityName.length() > 0) {
			append(entityName.toString());
		}
	}
}
