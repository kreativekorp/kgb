package com.kreative.acc.javaprops;

import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
				System.out.println("Java Properties 1.1.1");
				System.out.println("(c) 2012-2023 Kreative Software");
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
			try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Java Properties"); } catch (Exception e) {}
			try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
			try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
			
			try {
				Method getModule = Class.class.getMethod("getModule");
				Object javaDesktop = getModule.invoke(Toolkit.getDefaultToolkit().getClass());
				Object allUnnamed = getModule.invoke(JavaProperties.class);
				Class<?> module = Class.forName("java.lang.Module");
				Method addOpens = module.getMethod("addOpens", String.class, module);
				addOpens.invoke(javaDesktop, "sun.awt.X11", allUnnamed);
			} catch (Exception e) {}
			
			try {
				Toolkit tk = Toolkit.getDefaultToolkit();
				Field aacn = tk.getClass().getDeclaredField("awtAppClassName");
				aacn.setAccessible(true);
				aacn.set(tk, "Java Properties");
			} catch (Exception e) {}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new JavaPropertiesFrame();
				}
			});
		}
	}
}
