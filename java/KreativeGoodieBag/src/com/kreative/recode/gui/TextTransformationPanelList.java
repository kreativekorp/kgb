package com.kreative.recode.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Scrollable;

public class TextTransformationPanelList extends JPanel implements Scrollable {
	private static final long serialVersionUID = 1L;
	
	private final JPanel innerPanel;
	
	public TextTransformationPanelList() {
		innerPanel = new JPanel();
		innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
		innerPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setLayout(new BorderLayout());
		add(innerPanel, BorderLayout.NORTH);
	}
	
	public void addTextTransformationPanel(TextTransformationPanel txPanel) {
		innerPanel.add(txPanel);
		revalidate();
	}
	
	public List<TextTransformationPanel> getTextTransformationPanels() {
		List<TextTransformationPanel> txPanels = new ArrayList<TextTransformationPanel>();
		for (Component c : innerPanel.getComponents()) {
			txPanels.add((TextTransformationPanel)c);
		}
		return txPanels;
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}
	
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 16;
	}
	
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return visibleRect.height;
	}
}
