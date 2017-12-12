package com.kreative.acc.pushchar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Scrollable;

import com.kreative.acc.shared.unidata.UniDataUtilities;
import com.kreative.awt.FractionalSizeGridLayout;

public class PushCharPanel extends JPanel implements Scrollable {
	private static final long serialVersionUID = 1L;
	
	private static final Color HEADER_COLOR = new Color(0xFF4D4C67);
	private static final Color HEADER_TEXT = Color.white;
	private static final Font HEADER_FONT = new Font("Dialog", Font.BOLD, 10);
	
	private final JPanel mainPanel;
	
	public PushCharPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.PAGE_START);
		loading();
	}
	
	public synchronized void loading() {
		mainPanel.removeAll();
		
		mainPanel.add(Box.createVerticalStrut(120));
		
		JProgressBar pb = new JProgressBar(0, 10);
		pb.setValue(5);
		pb.setIndeterminate(true);
		pb.setMinimumSize(new Dimension(120, pb.getMinimumSize().height));
		pb.setPreferredSize(new Dimension(120, pb.getPreferredSize().height));
		pb.setMaximumSize(new Dimension(120, pb.getMaximumSize().height));
		mainPanel.add(pb);
		
		mainPanel.add(Box.createVerticalStrut(4));
		
		JLabel l = new JLabel("Loading...");
		l.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		mainPanel.add(l);
		
		mainPanel.add(Box.createVerticalStrut(120));
	}
	
	public synchronized void update(Font font, JLabel footerLabel) {
		mainPanel.removeAll();
		
		List<Integer> blockStartPoints = new ArrayList<Integer>();
		blockStartPoints.addAll(UniDataUtilities.getUnicodeBlocks().keySet());
		Collections.sort(blockStartPoints);
		BitSet chars = CharInFont.getInstance().allCharsInFont(font.getName());
		
		Iterator<Integer> si = blockStartPoints.iterator();
		int blockStart = si.hasNext() ? si.next() : 0;
		while (si.hasNext()) {
			int blockEnd = si.next();
			
			int blockCharCount = chars.get(blockStart,blockEnd).cardinality();
			if (blockCharCount > 0) {
				String blockName = UniDataUtilities.getUnicodeBlocks().get(blockStart);
				JLabel headerLabel = new JLabel(blockName + " (" + blockCharCount + ")");
				headerLabel.setFont(HEADER_FONT);
				headerLabel.setHorizontalAlignment(JLabel.LEFT);
				headerLabel.setOpaque(true);
				headerLabel.setBackground(HEADER_COLOR);
				headerLabel.setForeground(HEADER_TEXT);
				headerLabel.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
				JPanel headerPanel = new JPanel(new FractionalSizeGridLayout(1,1));
				headerPanel.add(headerLabel);
				mainPanel.add(headerPanel);
				
				JPanel blockPanel = new JPanel(new FractionalSizeGridLayout(0,1));
				for (int lineStart = blockStart, lineEnd = blockStart + 16; lineStart < blockEnd; lineStart += 16, lineEnd += 16) {
					if (!chars.get(lineStart,lineEnd).isEmpty()) {
						JPanel linePanel = new JPanel(new FractionalSizeGridLayout(1,16));
						for (int codePoint = lineStart; codePoint < lineEnd; codePoint++) {
							if (chars.get(codePoint)) {
								JLabel cell = new PushCharCell(font, codePoint, footerLabel);
								linePanel.add(cell);
							} else {
								JLabel cell = new JLabel();
								cell.setOpaque(true);
								cell.setBackground(Color.lightGray);
								linePanel.add(cell);
							}
						}
						blockPanel.add(linePanel);
					}
				}
				mainPanel.add(blockPanel);
			}
			
			blockStart = blockEnd;
		}
		revalidate();
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
		return 12;
	}
	
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return visibleRect.height;
	}
}
