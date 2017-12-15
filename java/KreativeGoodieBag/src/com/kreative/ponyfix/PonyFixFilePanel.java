package com.kreative.ponyfix;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PonyFixFilePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private File file = null;
	private List<String> fileContents = null;
	private String ponyName = null;
	private String charset = null;
	private Boolean bom = null;
	private String lineEnding = null;
	
	private final JLabel ponyNameLabel = new JLabel("\u00A0");
	private final JLabel charsetLabel = new JLabel("\u00A0");
	private final JLabel bomLabel = new JLabel("\u00A0");
	private final JLabel lineEndingLabel = new JLabel("\u00A0");
	private final JLabel statusLabel = new JLabel("\u00A0");
	
	public PonyFixFilePanel(String title) {
		final JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));
		
		final JPanel titlePanel = new JPanel(new GridLayout(0, 1, 4, 4));
		titlePanel.add(new JLabel("Name:"));
		titlePanel.add(new JLabel("Encoding:"));
		titlePanel.add(new JLabel("BOM:"));
		titlePanel.add(new JLabel("Line Ending:"));
		
		final JPanel labelPanel = new JPanel(new GridLayout(0, 1, 4, 4));
		labelPanel.add(ponyNameLabel);
		labelPanel.add(charsetLabel);
		labelPanel.add(bomLabel);
		labelPanel.add(lineEndingLabel);
		
		final JPanel contentPanel = new JPanel(new BorderLayout(8, 8));
		contentPanel.add(titlePanel, BorderLayout.LINE_START);
		contentPanel.add(labelPanel, BorderLayout.CENTER);
		
		final JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
		mainPanel.add(titleLabel, BorderLayout.PAGE_START);
		mainPanel.add(contentPanel, BorderLayout.CENTER);
		mainPanel.add(statusLabel, BorderLayout.PAGE_END);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.PAGE_START);
	}
	
	private void setError(String error) {
		file = null;
		fileContents = null;
		ponyName = null;
		charset = null;
		bom = null;
		lineEnding = null;
		
		ponyNameLabel.setText("\u00A0");
		charsetLabel.setText("\u00A0");
		bomLabel.setText("\u00A0");
		lineEndingLabel.setText("\u00A0");
		statusLabel.setText(error);
	}
	
	private void setStatus(String status) {
		ponyNameLabel.setText(ponyName);
		charsetLabel.setText(charset);
		bomLabel.setText(bom ? "Present" : "Not Present");
		lineEndingLabel.setText(
			lineEnding.equals("\r\n") ? "Windows" :
			lineEnding.equals("\n") ? "Unix" :
			lineEnding.equals("\r") ? "Macintosh" : "?"
		);
		statusLabel.setText(status);
	}
	
	public void setFile(File f) {
		file = PonyFixUtil.findPonyIni(f);
		if (file == null) {
			setError("Error: Could not find pony.ini.");
			return;
		}
		charset = PonyFixUtil.detectCharset(file);
		if (charset == null) {
			setError("Error: Could not read pony.ini.");
			return;
		}
		bom = PonyFixUtil.detectBOM(file, charset);
		if (bom == null) {
			setError("Error: Could not read pony.ini.");
			return;
		}
		lineEnding = PonyFixUtil.detectLineEnding(file, charset);
		if (lineEnding == null) {
			setError("Error: Could not read pony.ini.");
			return;
		}
		fileContents = PonyFixUtil.readFile(file, charset);
		if (fileContents == null) {
			setError("Error: Could not read pony.ini.");
			return;
		}
		ponyName = PonyFixUtil.findPonyName(fileContents);
		if (ponyName == null) ponyName = "<Unknown>";
		setStatus("Read successfully.");
	}
	
	public void fixFile(PonyFixFilePanel working) {
		if (this.file == null || working.file == null) {
			statusLabel.setText("Error: No Desktop Pony loaded.");
			return;
		}
		if (!this.file.getName().equals(working.file.getName())) {
			File newFile = new File(this.file.getParentFile(), working.file.getName());
			if (!this.file.renameTo(newFile)) {
				statusLabel.setText("Error: Could not rename pony.ini.");
				return;
			}
			this.file = newFile;
		}
		if (!PonyFixUtil.writeFile(this.file, working.charset, working.bom, working.lineEnding, this.fileContents)) {
			statusLabel.setText("Error: Could not rewrite pony.ini.");
			return;
		}
		this.charset = working.charset;
		this.bom = working.bom;
		this.lineEnding = working.lineEnding;
		setStatus("Fixed successfully.");
	}
}
