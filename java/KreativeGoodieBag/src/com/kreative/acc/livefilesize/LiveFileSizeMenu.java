package com.kreative.acc.livefilesize;

import javax.swing.JPopupMenu;

public class LiveFileSizeMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	
	public LiveFileSizeMenu(LiveFileSizeFrame owner) {
		add(new AddMenuItem(owner));
		add(new PasteMenuItem(owner));
		add(new RemoveAllMenuItem(owner));
	}
}
