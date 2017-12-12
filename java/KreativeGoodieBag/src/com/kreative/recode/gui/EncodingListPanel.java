package com.kreative.recode.gui;

import java.awt.BorderLayout;
import java.nio.charset.Charset;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class EncodingListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final EncodingList list;
	
	public EncodingListPanel(String caption) {
		this.list = new EncodingList();
		JScrollPane listPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		setLayout(new BorderLayout(4, 4));
		if (caption != null) add(new JLabel(caption), BorderLayout.PAGE_START);
		add(listPane, BorderLayout.CENTER);
	}
	
	public Charset getEncoding() {
		return list.getEncoding();
	}
	
	public void setEncoding(Charset charset) {
		list.setEncoding(charset);
	}
	
	public void setEncodingName(String charset) {
		list.setEncodingName(charset);
	}
	
	public void setVisibleRowCount(int rowCount) {
		list.setVisibleRowCount(rowCount);
	}
}
