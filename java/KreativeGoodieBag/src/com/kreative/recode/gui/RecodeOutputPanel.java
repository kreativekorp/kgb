package com.kreative.recode.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class RecodeOutputPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final TextAreaPanel textPanel;
	private final JRadioButton overwriteButton;
	private final JRadioButton outdirButton;
	private final JTextField outdirField;
	private final EncodingListPanel encodingList;
	private final JPanel filePanel;
	private final CardLayout layout;
	
	public RecodeOutputPanel() {
		this.textPanel = new TextAreaPanel("Recoded Text:");
		
		this.overwriteButton = new JRadioButton("Overwrite Originals");
		this.outdirButton = new JRadioButton("Separate Directory:");
		this.outdirField = new JTextField();
		JButton outdirBrowseButton = new JButton("Browse...");
		JPanel outputFileInnerInnerPanel = new JPanel(new BorderLayout());
		outputFileInnerInnerPanel.add(outdirBrowseButton, BorderLayout.LINE_END);
		JPanel outputFileInnerPanel = new JPanel();
		outputFileInnerPanel.setLayout(new BoxLayout(outputFileInnerPanel, BoxLayout.PAGE_AXIS));
		outputFileInnerPanel.add(wrapInAPanelSoBoxLayoutWontDoWeirdShit(overwriteButton));
		outputFileInnerPanel.add(wrapInAPanelSoBoxLayoutWontDoWeirdShit(outdirButton));
		outputFileInnerPanel.add(wrapInAPanelSoBoxLayoutWontDoWeirdShit(outdirField));
		outputFileInnerPanel.add(outputFileInnerInnerPanel);
		JPanel outputFilePanel = new JPanel(new BorderLayout());
		outputFilePanel.add(outputFileInnerPanel, BorderLayout.PAGE_START);
		JPanel outputFileOuterPanel = new JPanel(new BorderLayout(4, 4));
		outputFileOuterPanel.add(new JLabel("Destination:"), BorderLayout.PAGE_START);
		outputFileOuterPanel.add(outputFilePanel, BorderLayout.CENTER);
		
		this.encodingList = new EncodingListPanel("Output Encoding:");
		this.encodingList.setVisibleRowCount(6);
		
		this.filePanel = new JPanel(new BorderLayout(8, 8));
		this.filePanel.add(outputFileOuterPanel, BorderLayout.CENTER);
		this.filePanel.add(this.encodingList, BorderLayout.PAGE_END);
		
		this.setLayout(this.layout = new CardLayout());
		this.add(this.textPanel, "text");
		this.add(this.filePanel, "file");
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(overwriteButton);
		bg.add(outdirButton);
		
		outdirField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) { autoSelectOutdirButton(); }
			@Override public void insertUpdate(DocumentEvent e) { autoSelectOutdirButton(); }
			@Override public void removeUpdate(DocumentEvent e) { autoSelectOutdirButton(); }
		});
		
		outdirBrowseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc.setFileHidingEnabled(true);
				fc.setMultiSelectionEnabled(false);
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					autoSelectOutdirButton();
					outdirField.setText(f.getAbsolutePath());
				}
			}
		});
	}
	
	public void setText(String text) {
		textPanel.setText(text);
	}
	
	public File getOutputDirectory() {
		if (outdirButton.isSelected()) {
			return new File(outdirField.getText());
		} else {
			return null;
		}
	}
	
	public void setOutputDirectory(File file) {
		if (file == null) {
			overwriteButton.setSelected(true);
			outdirButton.setSelected(false);
		} else {
			overwriteButton.setSelected(false);
			outdirButton.setSelected(true);
			outdirField.setText(file.getAbsolutePath());
		}
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
	
	private void autoSelectOutdirButton() {
		overwriteButton.setSelected(false);
		outdirButton.setSelected(true);
	}
	
	private static JPanel wrapInAPanelSoBoxLayoutWontDoWeirdShit(Component c) {
		JPanel p = new JPanel(new BorderLayout());
		p.add(c, BorderLayout.CENTER);
		return p;
	}
}
