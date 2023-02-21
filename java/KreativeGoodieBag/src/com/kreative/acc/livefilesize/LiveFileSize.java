package com.kreative.acc.livefilesize;

import java.awt.Toolkit;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class LiveFileSize {
	private static enum Mode { AUTO, INFO, GUI, ERROR; }
	
	public static void main(String[] args) {
		Mode mode = Mode.AUTO;
		List<File> startFiles = new ArrayList<File>();
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equalsIgnoreCase("-version") || arg.equalsIgnoreCase("--version")) {
				System.out.println("LiveFileSize 1.1.1");
				System.out.println("(c) 2012-2023 Kreative Software");
				if (mode == Mode.AUTO) mode = Mode.INFO;
			} else if (arg.equalsIgnoreCase("-help") || arg.equalsIgnoreCase("--help")) {
				System.out.println("java -jar livefilesize.jar [ <file> [ <file> [...] ] ]");
				if (mode == Mode.AUTO) mode = Mode.INFO;
			} else if (arg.startsWith("-")) {
				System.err.println("Unrecognized option " + arg + ".");
				mode = Mode.ERROR;
			} else {
				startFiles.add(new File(arg));
				if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.GUI;
			}
		}
		
		if (mode == Mode.AUTO || mode == Mode.GUI) {
			final List<File> finalStartFiles = startFiles;
			
			try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", "LiveFileSize"); } catch (Exception e) {}
			try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
			try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
			
			try {
				Method getModule = Class.class.getMethod("getModule");
				Object javaDesktop = getModule.invoke(Toolkit.getDefaultToolkit().getClass());
				Object allUnnamed = getModule.invoke(LiveFileSize.class);
				Class<?> module = Class.forName("java.lang.Module");
				Method addOpens = module.getMethod("addOpens", String.class, module);
				addOpens.invoke(javaDesktop, "sun.awt.X11", allUnnamed);
			} catch (Exception e) {}
			
			try {
				Toolkit tk = Toolkit.getDefaultToolkit();
				Field aacn = tk.getClass().getDeclaredField("awtAppClassName");
				aacn.setAccessible(true);
				aacn.set(tk, "LiveFileSize");
			} catch (Exception e) {}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					LiveFileSizeFrame l = new LiveFileSizeFrame();
					for (File f : finalStartFiles) l.addFile(f);
				}
			});
		}
	}
}
