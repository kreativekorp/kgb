package com.kreative.acc.pushchar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.BitSet;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Scrollable;

import com.kreative.awt.FractionalSizeGridLayout;
import com.kreative.unicode.Block;
import com.kreative.unicode.BlockList;

public class PushCharPanel extends JPanel implements Scrollable {
	private static final long serialVersionUID = 1L;
	
	private static final Color HEADER_COLOR = new Color(0xFF4D4C67);
	private static final Color HEADER_TEXT = Color.white;
	private static final Font HEADER_FONT = new Font("Dialog", Font.BOLD, 10);
	
	private final BlockList blockList;
	private final SortedMap<Integer,Block> blockMap;
	private final JPanel mainPanel;
	
	public PushCharPanel() {
		blockList = BlockList.instance();
		blockMap = new TreeMap<Integer,Block>();
		for (Block block : blockList) {
			for (Map.Entry<Integer,Block> e : blockMap.entrySet()) {
				Block oldBlock = e.getValue();
				if (block.firstCodePoint >= oldBlock.firstCodePoint && block.lastCodePoint <= oldBlock.lastCodePoint) {
					if (block.firstCodePoint > oldBlock.firstCodePoint) {
						Block leftBlock = new Block(oldBlock.firstCodePoint, block.firstCodePoint - 1, oldBlock.name);
						blockMap.put(leftBlock.firstCodePoint, leftBlock);
					}
					if (block.lastCodePoint < oldBlock.lastCodePoint) {
						Block rightBlock = new Block(block.lastCodePoint + 1, oldBlock.lastCodePoint, oldBlock.name);
						blockMap.put(rightBlock.firstCodePoint, rightBlock);
					}
					break;
				}
			}
			blockMap.put(block.firstCodePoint, block);
		}
		
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
		
		BitSet chars = CharInFont.getInstance().allCharsInFont(font.getName());
		for (Block block : blockMap.values()) {
			int blockCharCount = chars.get(block.firstCodePoint, block.lastCodePoint + 1).cardinality();
			if (blockCharCount > 0) {
				JLabel headerLabel = new JLabel(block.name + " (" + blockCharCount + ")");
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
				for (int lineStart = block.firstCodePoint, lineEnd = block.firstCodePoint + 16; lineStart <= block.lastCodePoint; lineStart += 16, lineEnd += 16) {
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
