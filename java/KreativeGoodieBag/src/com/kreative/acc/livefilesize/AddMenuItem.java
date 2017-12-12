package com.kreative.acc.livefilesize;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JMenuItem;

public class AddMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	private LiveFileSizeFrame owner;
	
	public AddMenuItem(LiveFileSizeFrame owner) {
		super("Add...");
		this.owner = owner;
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doAdd();
			}
		});
	}
	
	private void doAdd() {
		FileDialog fd = new FileDialog(owner, "Select File", FileDialog.LOAD);
		fd.setVisible(true);
		if (fd.getDirectory() != null && fd.getFile() != null) {
			File f = new File(fd.getDirectory(), fd.getFile());
			owner.addFile(f);
		}
	}
}
