package com.kreative.acc.javaprops;

import java.util.Comparator;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JavaProperties {
	private static enum Mode { AUTO, INFO, STDIO, GUI, ERROR; }
	
	public static void main(String[] args) {
		Mode mode = Mode.AUTO;
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equalsIgnoreCase("-version") || arg.equalsIgnoreCase("--version")) {
				System.out.println("Java Properties 1.1");
				System.out.println("(c) 2012-2013 Kreative Software");
				if (mode == Mode.AUTO) mode = Mode.INFO;
			} else if (arg.equalsIgnoreCase("-help") || arg.equalsIgnoreCase("--help")) {
				System.out.println("java -jar javaprops.jar [ -nogui | -gui ]");
				if (mode == Mode.AUTO) mode = Mode.INFO;
			} else if (arg.equalsIgnoreCase("-nogui") || arg.equalsIgnoreCase("--nogui")) {
				if (mode != Mode.ERROR) mode = Mode.STDIO;
			} else if (arg.equalsIgnoreCase("-gui") || arg.equalsIgnoreCase("--gui")) {
				if (mode != Mode.ERROR) mode = Mode.GUI;
			} else {
				System.err.println("Unrecognized option " + arg + ".");
				mode = Mode.ERROR;
			}
		}
		
		if (mode == Mode.STDIO) {
			Properties properties = System.getProperties();
			SortedSet<Object> keys = new TreeSet<Object>(new Comparator<Object>() {
				@Override
				public int compare(Object a, Object b) {
					if (a == null) a = "";
					if (b == null) b = "";
					return a.toString().compareToIgnoreCase(b.toString());
				}
			});
			keys.addAll(properties.keySet());
			for (Object key : keys) {
				System.out.println(key + " = " + properties.get(key));
			}
		} else if (mode == Mode.AUTO || mode == Mode.GUI) {
			try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new JavaPropertiesFrame();
				}
			});
		}
	}
}
