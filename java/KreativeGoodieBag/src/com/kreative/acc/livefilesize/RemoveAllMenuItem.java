package com.kreative.acc.livefilesize;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

public class RemoveAllMenuItem extends JMenuItem {
	private static final long serialVersionUID = 1L;
	
	private LiveFileSizeFrame owner;
	
	public RemoveAllMenuItem(LiveFileSizeFrame owner) {
		super("Remove All");
		this.owner = owner;
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doRemoveAll();
			}
		});
	}
	
	private void doRemoveAll() {
		owner.removeAllFiles();
	}
}
