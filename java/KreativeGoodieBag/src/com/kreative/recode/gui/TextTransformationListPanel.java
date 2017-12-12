package com.kreative.recode.gui;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationLibrary;

public class TextTransformationListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final TextTransformationList list;
	
	public TextTransformationListPanel(String caption, TextTransformationLibrary txLib) {
		this.list = new TextTransformationList(txLib);
		JScrollPane listPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setLayout(new BorderLayout(4, 4));
		if (caption != null) add(new JLabel(caption), BorderLayout.PAGE_START);
		add(listPane, BorderLayout.CENTER);
	}
	
	public TextTransformationList getList() {
		return list;
	}
	
	public void clearSelection() {
		list.clearSelection();
	}
	
	public TextTransformationFactory getTransformation() {
		return list.getTransformation();
	}
	
	public void setVisibleRowCount(int rowCount) {
		list.setVisibleRowCount(rowCount);
	}
}
