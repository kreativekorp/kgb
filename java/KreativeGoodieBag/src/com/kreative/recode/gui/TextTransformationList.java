package com.kreative.recode.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationLibrary;

public class TextTransformationList extends JList {
	private static final long serialVersionUID = 1L;
	
	private final TextTransformationLibrary txLib;
	private final List<String> txNames;
	
	public TextTransformationList(TextTransformationLibrary txLib) {
		this.txLib = txLib;
		this.txNames = txLib.listByName();
		setListData(txNames.toArray());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(new MouseAdapter() {
			@Override public void mouseEntered(MouseEvent e) { updateToolTip(e); }
			@Override public void mouseExited(MouseEvent e) { updateToolTip(e); }
		});
		addMouseMotionListener(new MouseMotionListener() {
			@Override public void mouseMoved(MouseEvent e) { updateToolTip(e); }
			@Override public void mouseDragged(MouseEvent e) { updateToolTip(e); }
		});
	}
	
	public TextTransformationFactory getTransformation() {
		Object o = this.getSelectedValue();
		if (o == null) return null;
		else return txLib.getByName(o.toString());
	}
	
	private void updateToolTip(MouseEvent e) {
		int i = locationToIndex(e.getPoint());
		if (i < 0) {
			setToolTipText(null);
		} else {
			String txName = txNames.get(i);
			TextTransformationFactory tx = txLib.getByName(txName);
			setToolTipText(tx.getDescription());
		}
	}
}
