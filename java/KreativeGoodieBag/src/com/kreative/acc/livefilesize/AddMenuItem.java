package com.kreative.acc.livefilesize;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

public class AddMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	public AddMenuItem(final LiveFileSizeFrame owner) {
		super("Add...");
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				owner.addFile();
			}
		});
	}
}
