package com.kreative.recode.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import javax.swing.JPanel;

public class RecodeInputPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final TextAreaPanel textPanel;
	private final FileListPanel fileList;
	private final EncodingListPanel encodingList;
	private final JPanel filePanel;
	private final CardLayout layout;
	
	public RecodeInputPanel() {
		this.textPanel = new TextAreaPanel("Source Text:");
		this.fileList = new FileListPanel("Source Files:");
		this.encodingList = new EncodingListPanel("Input Encoding:");
		this.encodingList.setVisibleRowCount(6);
		this.filePanel = new JPanel(new BorderLayout(8, 8));
		this.filePanel.add(this.fileList, BorderLayout.CENTER);
		this.filePanel.add(this.encodingList, BorderLayout.PAGE_END);
		this.setLayout(this.layout = new CardLayout());
		this.add(this.textPanel, "text");
		this.add(this.filePanel, "file");
	}
	
	public String getText() {
		return textPanel.getText();
	}
	
	public List<File> getFiles() {
		return fileList.getFiles();
	}
	
	public void setFiles(List<File> files) {
		fileList.setFiles(files);
	}
	
	public Charset getEncoding() {
		return encodingList.getEncoding();
	}
	
	public void setEncoding(Charset encoding) {
		encodingList.setEncoding(encoding);
	}
	
	public void showTextPanel() {
		layout.show(this, "text");
	}
	
	public void showFilePanel() {
		layout.show(this, "file");
	}
}
