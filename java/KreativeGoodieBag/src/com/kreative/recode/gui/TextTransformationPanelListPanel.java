package com.kreative.recode.gui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationLibrary;

public class TextTransformationPanelListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final TextTransformationPanelList panelList;
	private final TextTransformationListPanel txList;
	
	public TextTransformationPanelListPanel(String caption, TextTransformationLibrary txLib) {
		this.panelList = new TextTransformationPanelList();
		JScrollPane scrollPane = new JScrollPane(panelList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		JPanel scrollPanePanel = new JPanel(new BorderLayout(4, 4));
		if (caption != null) scrollPanePanel.add(new JLabel(caption), BorderLayout.PAGE_START);
		scrollPanePanel.add(scrollPane, BorderLayout.CENTER);
		
		this.txList = new TextTransformationListPanel("Add Transformation:", txLib);
		this.txList.setVisibleRowCount(6);
		
		setLayout(new BorderLayout(8, 8));
		add(scrollPanePanel, BorderLayout.CENTER);
		add(txList, BorderLayout.PAGE_END);
		
		txList.getList().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				TextTransformationPanelListPanel self = TextTransformationPanelListPanel.this;
				TextTransformationFactory txFactory = self.txList.getTransformation();
				if (txFactory != null) {
					TextTransformationPanel txPanel = new TextTransformationPanel(txFactory, null);
					self.panelList.addTextTransformationPanel(txPanel);
					self.txList.clearSelection();
				}
			}
		});
	}
	
	public void addTextTransformationPanel(TextTransformationPanel txPanel) {
		panelList.addTextTransformationPanel(txPanel);
	}
	
	public List<TextTransformationPanel> getTextTransformationPanels() {
		return panelList.getTextTransformationPanels();
	}
}
