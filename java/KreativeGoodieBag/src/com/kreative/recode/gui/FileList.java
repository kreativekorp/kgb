package com.kreative.recode.gui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;

public class FileList extends JList {
	private static final long serialVersionUID = 1L;
	
	private final DefaultListModel model;
	
	public FileList() {
		this.model = new DefaultListModel();
		setModel(model);
		setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean selected, boolean focus) {
				if (value instanceof File) {
					value = ((File)value).getName();
				}
				return super.getListCellRendererComponent(list, value, index, selected, focus);
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override public void mouseEntered(MouseEvent e) { updateToolTip(e); }
			@Override public void mouseExited(MouseEvent e) { updateToolTip(e); }
		});
		addMouseMotionListener(new MouseMotionListener() {
			@Override public void mouseMoved(MouseEvent e) { updateToolTip(e); }
			@Override public void mouseDragged(MouseEvent e) { updateToolTip(e); }
		});
	}
	
	public void addFile(File file) {
		if (!model.contains(file)) {
			model.addElement(file);
		}
	}
	
	public void removeSelectedFiles() {
		int[] indices = this.getSelectedIndices();
		for (int i = indices.length - 1; i >= 0; i--) {
			model.remove(indices[i]);
		}
	}
	
	public List<File> getFiles() {
		List<File> files = new ArrayList<File>();
		for (Object o : model.toArray()) {
			if (o instanceof File) {
				files.add((File)o);
			}
		}
		return files;
	}
	
	public void setFiles(List<File> files) {
		model.removeAllElements();
		for (File file : files) {
			if (!model.contains(file)) {
				model.addElement(file);
			}
		}
	}
	
	private void updateToolTip(MouseEvent e) {
		int i = locationToIndex(e.getPoint());
		if (i < 0) {
			setToolTipText(null);
		} else {
			Object o = model.get(i);
			if (o instanceof File) {
				setToolTipText(((File)o).getAbsolutePath());
			} else {
				setToolTipText(null);
			}
		}
	}
}
