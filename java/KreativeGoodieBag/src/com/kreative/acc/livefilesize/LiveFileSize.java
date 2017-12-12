package com.kreative.acc.livefilesize;

import java.io.File;
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
				System.out.println("LiveFileSize 1.1");
				System.out.println("(c) 2012-2013 Kreative Software");
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
			try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
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
