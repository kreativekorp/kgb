package com.kreative.recode.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TextAreaPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final String[] UNICODE_FONT_NAMES = {
		"Abydos",
		"Aegean",
		"Aegyptus",
		"Andale Mono",
		"Apple Color Emoji",
		"Apple LiGothic",
		"Apple LiSung",
		"Apple Symbols",
		"AppleGothic",
		"AppleMyungjo",
		"Arial",
		"Arial Narrow",
		"Arial Unicode MS",
		"Bitstream CyberBase",
		"Bitstream Cyberbit",
		"Bitstream CyberCJK",
		"Caliban",
		"Calibri",
		"Cambria",
		"Candara",
		"Cardo",
		"Caslon",
		"Charis SIL",
		"Chrysanthi Unicode",
		"ClearlyU",
		"Code2000",
		"Code2001",
		"Code2002",
		"Conlang Unicode",
		"Consolas",
		"Constantia",
		"Constructium",
		"Corbel",
		"Courier",
		"Courier New",
		"DejaVu Sans",
		"DejaVu Sans Mono",
		"DejaVu Serif",
		"Doulos SIL",
		"Droid Sans",
		"Droid Sans Mono",
		"Droid Serif",
		"Everson Mono",
		"Fairfax",
		"Fairfax HD",
		"FreeMono",
		"FreeSans",
		"FreeSerif",
		"Futura",
		"Geneva",
		"Gentium",
		"Gentium Basic",
		"Gentium Plus",
		"Georgia",
		"Gill Sans",
		"Gulim",
		"HAN NOM A",
		"HAN HOM B",
		"Helvetica",
		"Helvetica Neue",
		"Junicode",
		"Kreative Square",
		"Liberation Mono",
		"Liberation Sans",
		"Liberation Serif",
		"Linux Biolinum",
		"Linux Libertine",
		"Lucida Console",
		"Lucida Grande",
		"Lucida Sans",
		"Lucida Sans Unicode",
		"Menlo",
		"Microsoft JhengHei",
		"Microsoft Sans Serif",
		"Microsoft YaHei",
		"Monaco",
		"MPH 2B Damase",
		"MS Gothic",
		"MS Mincho",
		"MS PGothic",
		"MS PMincho",
		"Musica",
		"New Athena Unicode",
		"New Gulim",
		"Nishiki-teki",
		"Open Sans",
		"Palatino",
		"PCMyungjo",
		"Quivira",
		"Segoe Print",
		"Segoe Script",
		"Segoe UI",
		"Segoe UI Symbol",
		"Source Code Pro",
		"Source Sans Pro",
		"STIXGeneral",
		"Sun-ExtA",
		"Sun-ExtB",
		"Symbola",
		"Tahoma",
		"Times",
		"Times New Roman",
		"TITUS Cyberbit Basic",
		"Trebuchet MS",
		"Ubuntu",
		"Ubuntu Mono",
		"Unifont",
		"Unifont CSUR",
		"Unifont Upper",
		"Verdana",
		"WenQuanYi Bitmap Song",
		"WenQuanYi Micro Hei",
		"WenQuanYi Zen Hei",
		"XITS",
		"YOzFontN",
	};
	
	private final JTextArea textArea;
	
	public TextAreaPanel(String caption) {
		this.textArea = new JTextArea() {
			private static final long serialVersionUID = 1L;
			// The following hack makes pasting supplementary Unicode characters into a JTextArea possible.
			// Without this hack, pasted text stops at the first supplementary character.
			// Don't ask me how or why this works but the base implementation doesn't.
			@Override
			public void paste() {
				Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
				if (cb.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
					try {
						replaceSelection(cb.getData(DataFlavor.stringFlavor).toString());
					} catch (UnsupportedFlavorException e) {
						// Ignore
					} catch (IOException e) {
						// Ignore
					}
				}
			}
		};
		this.textArea.setLineWrap(true);
		this.textArea.setWrapStyleWord(true);
		
		final Font textAreaFont = this.textArea.getFont();
		final String textAreaFontName = textAreaFont.getFamily();
		final int textAreaFontStyle = textAreaFont.getStyle();
		final int textAreaFontSize = textAreaFont.getSize();
		
		List<String> fontNames = new ArrayList<String>();
		fontNames.addAll(Arrays.asList(UNICODE_FONT_NAMES));
		fontNames.retainAll(Arrays.asList(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
		if (!fontNames.contains(textAreaFontName)) fontNames.add(textAreaFontName);
		Collections.sort(fontNames);
		final JComboBox fontPopup = new JComboBox(fontNames.toArray());
		fontPopup.setEditable(false);
		fontPopup.setMaximumRowCount(32);
		fontPopup.setSelectedItem(textAreaFontName);
		fontPopup.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String textAreaFontName = fontPopup.getSelectedItem().toString();
				textArea.setFont(new Font(textAreaFontName, textAreaFontStyle, textAreaFontSize));
			}
		});
		
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JPanel fontPanel = new JPanel(new BorderLayout(4, 4));
		fontPanel.add(new JLabel("Font:"), BorderLayout.LINE_START);
		fontPanel.add(fontPopup, BorderLayout.CENTER);
		
		setLayout(new BorderLayout(4, 4));
		if (caption != null) add(new JLabel(caption), BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		add(fontPanel, BorderLayout.PAGE_END);
	}
	
	public String getText() {
		return textArea.getText();
	}
	
	public void setText(String text) {
		textArea.setText(text);
	}
}
