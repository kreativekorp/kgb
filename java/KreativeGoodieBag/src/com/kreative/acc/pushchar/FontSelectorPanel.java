package com.kreative.acc.pushchar;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FontSelectorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JList fontNameList;
	private final JScrollPane fontNameComponent;
	private final JLabel fontNameLabel;
	private final JToggleButton fontNameButton;
	private final SpinnerNumberModel fontSizeSpinner;
	private final JSpinner fontSizeComponent;
	private final JToggleButton fontBoldButton;
	private final JToggleButton fontItalicButton;
	private final List<ActionListener> listeners;
	private boolean settingFont;
	
	public FontSelectorPanel() {
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		fontNameList = new JList(fonts);
		fontNameComponent = new JScrollPane(fontNameList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		fontNameLabel = new JLabel();
		fontNameButton = new JToggleButton(" A ");
		fontNameButton.setToolTipText("Show Font List");
		fontNameButton.setFont(fontNameButton.getFont().deriveFont(Font.ITALIC));
		fontNameButton.putClientProperty("JButton.buttonType", "square");
		fontSizeSpinner = new SpinnerNumberModel(12, 4, 256, 1);
		fontSizeComponent = new JSpinner(fontSizeSpinner);
		fontSizeComponent.setToolTipText("Font Size");
		fontBoldButton = new JToggleButton(" B ");
		fontBoldButton.setToolTipText("Bold");
		fontBoldButton.setFont(fontBoldButton.getFont().deriveFont(Font.BOLD));
		fontBoldButton.putClientProperty("JButton.buttonType", "square");
		fontItalicButton = new JToggleButton(" i ");
		fontItalicButton.setToolTipText("Italic");
		fontItalicButton.setFont(fontItalicButton.getFont().deriveFont(Font.ITALIC));
		fontItalicButton.putClientProperty("JButton.buttonType", "square");
		listeners = new ArrayList<ActionListener>();
		settingFont = false;
		
		JPanel fontStylePanel = new JPanel(new GridLayout(1,0,-1,-1));
		fontStylePanel.add(fontBoldButton);
		fontStylePanel.add(fontItalicButton);
		
		JPanel fontSizeStylePanel = new JPanel(new BorderLayout(12,12));
		fontSizeStylePanel.add(fontSizeComponent, BorderLayout.CENTER);
		fontSizeStylePanel.add(fontStylePanel, BorderLayout.LINE_END);
		
		JPanel fontNamePanel = new JPanel(new BorderLayout(8,8));
		fontNamePanel.add(fontNameLabel, BorderLayout.CENTER);
		fontNamePanel.add(fontNameButton, BorderLayout.LINE_START);
		
		setLayout(new BorderLayout(12,12));
		add(fontNamePanel, BorderLayout.CENTER);
		add(fontSizeStylePanel, BorderLayout.LINE_END);
		setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
		
		fontNameList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				String oldValue = fontNameLabel.getText();
				String newValue = fontNameList.getSelectedValue().toString();
				if (!oldValue.equals(newValue)) {
					fontNameLabel.setText(newValue);
					if (!settingFont) fireActionListeners();
				}
			}
		});
		fontNameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fontNameComponent.setVisible(fontNameButton.isSelected());
				revalidate();
			}
		});
		fontSizeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (!settingFont) fireActionListeners();
			}
		});
		fontBoldButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!settingFont) fireActionListeners();
			}
		});
		fontItalicButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!settingFont) fireActionListeners();
			}
		});
	}
	
	public JComponent getFontNameComponent() {
		return fontNameComponent;
	}
	
	public synchronized Font getSelectedFont() {
		String fontName = fontNameLabel.getText();
		int fontSize = ((Number)fontSizeSpinner.getValue()).intValue();
		int fontStyle = Font.PLAIN;
		if (fontBoldButton.isSelected()) fontStyle |= Font.BOLD;
		if (fontItalicButton.isSelected()) fontStyle |= Font.ITALIC;
		return new Font(fontName, fontStyle, fontSize);
	}
	
	public synchronized void setSelectedFont(Font font) {
		settingFont = true;
		fontNameList.setSelectedValue(font.getName(), true);
		fontNameLabel.setText(font.getName());
		fontNameButton.setSelected(fontNameComponent.isVisible());
		fontSizeSpinner.setValue(font.getSize());
		fontBoldButton.setSelected((font.getStyle() & Font.BOLD) != 0);
		fontItalicButton.setSelected((font.getStyle() & Font.ITALIC) != 0);
		settingFont = false;
	}
	
	public synchronized void addActionListener(ActionListener listener) {
		listeners.add(listener);
	}
	
	public synchronized void removeActionListener(ActionListener listener) {
		listeners.remove(listener);
	}
	
	private synchronized void fireActionListeners() {
		ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "actionPerformed");
		for (ActionListener listener : listeners) listener.actionPerformed(e);
	}
}
