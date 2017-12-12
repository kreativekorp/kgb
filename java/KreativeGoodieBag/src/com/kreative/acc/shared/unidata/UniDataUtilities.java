package com.kreative.acc.shared.unidata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class UniDataUtilities {
	private UniDataUtilities() {}
	
	private static String osString = null;
	
	public static boolean isMacOS() {
		if (osString == null) {
			try {
				osString = System.getProperty("os.name").toUpperCase();
			} catch (Exception e) {
				osString = "";
			}
		}
		return osString.contains("MAC OS");
	}
	
	public static boolean isWindows() {
		if (osString == null) {
			try {
				osString = System.getProperty("os.name").toUpperCase();
			} catch (Exception e) {
				osString = "";
			}
		}
		return osString.contains("WINDOWS");
	}
	
	public static File getUniDataDirectory() {
		if (isMacOS()) {
			File u = new File(System.getProperty("user.home"));
			File l = new File(u, "Library");
			if (!l.exists()) l.mkdir();
			File p = new File(l, "Preferences");
			if (!p.exists()) p.mkdir();
			File ud = new File(p, "com.kreative.acc.shared.unidata");
			if (!ud.exists()) ud.mkdir();
			return ud;
		} else if (isWindows()) {
			File u = new File(System.getProperty("user.home"));
			File a = new File(u, "Application Data");
			if (!a.exists()) a.mkdir();
			File k = new File(a, "Kreative");
			if (!k.exists()) k.mkdir();
			File ud = new File(k, "UniData");
			if (!ud.exists()) ud.mkdir();
			return ud;
		} else {
			File u = new File(System.getProperty("user.home"));
			File ud = new File(u, ".unidata");
			if (!ud.exists()) ud.mkdir();
			return ud;
		}
	}
	
	private static List<UniDataURL> uniDataURLs = null;
	
	public static List<UniDataURL> getUniDataURLs() {
		if (uniDataURLs == null) {
			uniDataURLs = new ArrayList<UniDataURL>();
			try { readUniDataURLs(); } catch (IOException ioe) {}
			if (uniDataURLs.isEmpty()) {
				uniDataURLs.add(new UniDataURL("Unicode", "http://www.unicode.org/Public/UNIDATA/"));
				uniDataURLs.add(new UniDataURL("UCSUR", "http://www.kreativekorp.com/ucsur/UNIDATA/"));
				try { writeUniDataURLs(); } catch (IOException ioe) {}
			}
		}
		return uniDataURLs;
	}
	
	private static void readUniDataURLs() throws IOException {
		File f = new File(getUniDataDirectory(), "Servers.txt");
		Scanner s = new Scanner(f, "UTF-8");
		while (s.hasNextLine()) {
			String l = s.nextLine().trim();
			if (l.length() > 0 && !l.startsWith("#")) {
				String[] ll = l.split("\t", 2);
				if (ll.length > 1) {
					uniDataURLs.add(new UniDataURL(ll[0].trim(), ll[1].trim()));
				}
			}
		}
		s.close();
	}
	
	public static void writeUniDataURLs() throws IOException {
		File f = new File(getUniDataDirectory(), "Servers.txt");
		PrintWriter p = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"), true);
		for (UniDataURL u : uniDataURLs) {
			p.println(u.getName().trim() + "\t" + u.getURL().trim());
		}
		p.flush();
		p.close();
	}
	
	public static Scanner getUniData(UniDataURL url, String table) {
		File localFile = new File(getUniDataDirectory(), url.getName() + "-" + table + ".txt");
		long then = localFile.lastModified();
		long now = System.currentTimeMillis();
		if ((now - then) >= 604800000) {
			try { readUniData(localFile, url.getURL(), table); } catch (IOException ioe) {}
		}
		try {
			return new Scanner(localFile, "UTF-8");
		} catch (IOException ioe) {
			return new Scanner("");
		}
	}
	
	private static void readUniData(File localFile, String url, String table) throws IOException {
		URL u = new URL(url + "/" + table + ".txt");
		URLConnection uc = u.openConnection();
		InputStream is = uc.getInputStream();
		byte[] cbuf = new byte[65536];
		int cblen;
		FileOutputStream os = new FileOutputStream(localFile);
		while ((cblen = is.read(cbuf)) >= 0) {
			os.write(cbuf, 0, cblen);
		}
		os.flush();
		os.close();
		is.close();
	}
	
	public static boolean isPrivateUse(int cp) {
		return (cp < 0) || (cp >= 0xE000 && cp < 0xF900) || (cp >= 0xF0000);
	}
	
	private static SortedMap<Integer,String> uniBlockCache = null;
	
	public static SortedMap<Integer,String> getUnicodeBlocks() {
		if (uniBlockCache == null) {
			uniBlockCache = new TreeMap<Integer,String>();
			uniBlockCache.put(0x000000, "Undefined");
			uniBlockCache.put(0x00E000, "Private Use Area");
			uniBlockCache.put(0x00F900, "Undefined");
			uniBlockCache.put(0x0F0000, "Private Use Area");
			uniBlockCache.put(0x110000, "End");
			for (UniDataURL url : getUniDataURLs()) {
				Scanner sc = getUniData(url, "Blocks");
				while (sc.hasNextLine()) {
					String s = sc.nextLine().trim();
					if (s.length() > 0 && s.charAt(0) != '#') {
						String[] p = s.split("\\.\\.|; ");
						if (p.length == 3) {
							int start = Integer.parseInt(p[0], 16);
							int end = Integer.parseInt(p[1], 16)+1;
							String name = p[2];
							uniBlockCache.put(start, name);
							if (!uniBlockCache.containsKey(end)) {
								uniBlockCache.put(end, isPrivateUse(end) ? "Private Use Area" : "Undefined");
							}
						}
					}
				}
				sc.close();
			}
		}
		return uniBlockCache;
	}
	
	private static SortedMap<Integer,CharacterInfo> uniPropCache = null;
	
	public static SortedMap<Integer,CharacterInfo> getUnicodeProperties() {
		if (uniPropCache == null) {
			uniPropCache = new TreeMap<Integer,CharacterInfo>();
			for (UniDataURL url : getUniDataURLs()) {
				Scanner sc = getUniData(url, "UnicodeData");
				while (sc.hasNextLine()) {
					String s = sc.nextLine().trim();
					if (s.length() > 0 && s.charAt(0) != '#') {
						String[] p = s.split(";");
						if (p.length >= 3) {
							int cp = Integer.parseInt(p[0], 16);
							uniPropCache.put(cp, new CharacterInfo(cp, p));
						}
					}
				}
			}
		}
		return uniPropCache;
	}
}
