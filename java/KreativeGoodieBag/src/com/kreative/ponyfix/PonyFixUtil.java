package com.kreative.ponyfix;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JComponent;

public class PonyFixUtil {
	public static File findPonyIni(File f) {
		if (f.isDirectory()) {
			for (File child : f.listFiles()) {
				if (child.getName().equalsIgnoreCase("pony.ini")) {
					return child;
				}
			}
			return null;
		} else if (f.getName().equalsIgnoreCase("pony.ini")) {
			return f;
		} else {
			for (File sibling : f.getParentFile().listFiles()) {
				if (sibling.getName().equalsIgnoreCase("pony.ini")) {
					return sibling;
				}
			}
			return null;
		}
	}
	
	private static final String[] CHARSETS = {
		"UTF-32LE", "UTF-32BE", "UTF-16LE", "UTF-16BE",
		"UTF-8", "CP1252", "ISO-8859-1", "US-ASCII"
	};
	
	public static String detectCharset(File f) {
		for (String charset : CHARSETS) {
			try {
				Scanner s = new Scanner(f, charset);
				boolean foundKnownString = false;
				boolean foundEncodingError = false;
				while (s.hasNextLine()) {
					String line = s.nextLine().toLowerCase();
					if (line.contains("behavior")) foundKnownString = true;
					if (line.contains("\uFFFD")) foundEncodingError = true;
				}
				s.close();
				if (foundEncodingError) continue;
				if (foundKnownString) return charset;
			} catch (IOException e) {
				continue;
			}
		}
		return null;
	}
	
	public static Boolean detectBOM(File f, String charset) {
		try {
			Reader r = new InputStreamReader(new FileInputStream(f), charset);
			boolean bom = (r.read() == '\uFEFF');
			r.close();
			return bom;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static String detectLineEnding(File f, String charset) {
		try {
			Reader r = new InputStreamReader(new FileInputStream(f), charset);
			int cr = 0, lf = 0;
			while (true) {
				int ch = r.read();
				if (ch < 0) break;
				else if (ch == '\r') cr++;
				else if (ch == '\n') lf++;
			}
			r.close();
			if (cr == lf) return "\r\n";
			else if (cr > lf * 2) return "\r";
			else if (lf > cr * 2) return "\n";
			else return "\r\n";
		} catch (IOException e) {
			return null;
		}
	}
	
	private static String sanitize(String in) {
		StringBuffer out = new StringBuffer(in.length());
		CharacterIterator it = new StringCharacterIterator(in);
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			if (ch == '\uFEFF') continue;
			else if (ch == '\t') out.append(ch);
			else if (ch < 0x20) continue;
			else if (ch < 0x7F) out.append(ch);
			else if (ch < 0xA0) continue;
			else out.append(ch);
		}
		return out.toString();
	}
	
	public static List<String> readFile(File f, String charset) {
		try {
			List<String> lines = new ArrayList<String>();
			Scanner s = new Scanner(f, charset);
			while (s.hasNextLine()) lines.add(sanitize(s.nextLine()));
			s.close();
			return lines;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static String findPonyName(List<String> lines) {
		if (lines != null) {
			for (String line : lines) {
				String[] fields = line.split(",", 2);
				if (fields[0].trim().equalsIgnoreCase("name") && fields.length > 1) {
					String name = fields[1].trim();
					if (name.startsWith("\"") && name.endsWith("\"")) {
						name = name.substring(1, name.length() - 1);
					}
					return name;
				}
			}
		}
		return null;
	}
	
	public static boolean writeFile(File f, String charset, boolean bom, String lineEnding, List<String> lines) {
		try {
			Writer w = new OutputStreamWriter(new FileOutputStream(f), charset);
			if (bom) w.write('\uFEFF');
			for (String line : lines) {
				w.write(line);
				w.write(lineEnding);
			}
			w.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static void setTransparent(JComponent c) {
		c.setOpaque(false);
		for (int i = 0, n = c.getComponentCount(); i < n; i++) {
			Component child = c.getComponent(i);
			if (child instanceof JComponent) {
				setTransparent((JComponent)child);
			}
		}
	}
}
