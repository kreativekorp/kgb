package com.kreative.recode.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;

public class FileListDropTarget implements DropTargetListener {
	private final FileList owner;
	
	public FileListDropTarget(FileList owner) {
		this.owner = owner;
	}
	
	@Override
	public void drop(DropTargetDropEvent dtde) {
		try {
			int action = dtde.getDropAction();
			dtde.acceptDrop(action);
			Transferable t = dtde.getTransferable();
			if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				List<?> list = (List<?>)t.getTransferData(DataFlavor.javaFileListFlavor);
				for (Object o : list) {
					if (o instanceof File) {
						owner.addFile((File)o);
					}
				}
				dtde.dropComplete(true);
			} else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				String s = t.getTransferData(DataFlavor.stringFlavor).toString();
				for (String l : s.split("\r\n|\r|\n")) {
					l = l.trim();
					if (l.length() > 0) {
						owner.addFile(new File(l));
					}
				}
				dtde.dropComplete(true);
			} else {
				dtde.dropComplete(false);
			}
		} catch (Exception e) {
			dtde.dropComplete(false);
		}
	}
	
	@Override public void dragEnter(DropTargetDragEvent dtde) {}
	@Override public void dragExit(DropTargetEvent dte) {}
	@Override public void dragOver(DropTargetDragEvent dtde) {}
	@Override public void dropActionChanged(DropTargetDragEvent dtde) {}
}
