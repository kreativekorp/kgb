package com.kreative.acc.livefilesize;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.JMenuItem;

public class PasteMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	private LiveFileSizeFrame owner;
	
	public PasteMenuItem(LiveFileSizeFrame owner) {
		super("Paste");
		this.owner = owner;
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doPaste();
			}
		});
	}
	
	private void doPaste() {
		try {
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			if (cb.isDataFlavorAvailable(DataFlavor.javaFileListFlavor)) {
				List<?> list = (List<?>)cb.getData(DataFlavor.javaFileListFlavor);
				for (Object o : list) {
					if (o instanceof File) {
						owner.addFile((File)o);
					}
				}
			} else if (cb.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
				String s = cb.getData(DataFlavor.stringFlavor).toString();
				for (String l : s.split("\r\n|\r|\n")) {
					l = l.trim();
					if (l.length() > 0) {
						owner.addFile(new File(l));
					}
				}
			}
		} catch (Exception e) {
			// Ignored.
		}
	}
}
