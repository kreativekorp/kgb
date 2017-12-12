package com.kreative.acc.screenruler;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

public class ScreenRulerComponent extends JComponent implements ComponentListener, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	private final ScreenRulerFont font;
	private final ScreenRulerParameters[] units;
	private final String[] unitNames;
	private int unitIndex;
	
	private boolean showTop = false;
	private boolean showLeft = false;
	private boolean showBottom = true;
	private boolean showRight = false;
	
	private int mouseX = -1;
	private int mouseY = -1;
	private boolean mouseLocked = false;
	private boolean isPopup = false;
	
	public ScreenRulerComponent(ScreenRulerParameters[] units, String[] unitNames, int unitIndex) {
		this.font = new ScreenRulerFont();
		this.units = units;
		this.unitNames = unitNames;
		this.unitIndex = unitIndex;
		setCursor(ScreenRulerCursor.CURSOR);
		addComponentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		ScreenRulerParameters parameters = units[unitIndex];
		double pixelsPerUnit = parameters.getPixelsPerUnit();
		
		g.clearRect(0, 0, w, h);
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, w, h);
		}
		g.setColor(getForeground());
		
		for (int i = 0, m = parameters.getTickCount(); i < m; i++) {
			int tickLength = parameters.getTickLength(i);
			double ticksPerUnit = parameters.getTicksPerUnit(i);
			double pixelsPerTick = pixelsPerUnit / ticksPerUnit;
			if (showTop || showBottom) {
				for (int j = 0, n = (int)Math.ceil(w / pixelsPerTick); j <= n; j++) {
					int tickPosition = (int)Math.ceil(j * pixelsPerTick);
					if (showTop) g.drawLine(tickPosition, 0, tickPosition, tickLength-1);
					if (showBottom) g.drawLine(tickPosition, h-tickLength, tickPosition, h-1);
				}
			}
			if (showLeft || showRight) {
				for (int j = 0, n = (int)Math.ceil(h / pixelsPerTick); j <= n; j++) {
					int tickPosition = (int)Math.ceil(j * pixelsPerTick);
					if (showLeft) g.drawLine(0, tickPosition, tickLength-1, tickPosition);
					if (showRight) g.drawLine(w-tickLength, tickPosition, w-1, tickPosition);
				}
			}
		}
		
		int tickLength = parameters.getMaxTickLength();
		double labelsPerUnit = parameters.getLabelsPerUnit();
		double pixelsPerLabel = pixelsPerUnit / labelsPerUnit;
		if (showTop || showBottom) {
			for (int j = 1, n = (int)Math.ceil(w / pixelsPerLabel); j <= n; j++) {
				int labelPosition = (int)Math.ceil(j * pixelsPerLabel);
				String label = myDoubleToString(j / labelsPerUnit);
				int labelWidth = font.stringWidth(label);
				if (showTop) font.drawString(g, label, labelPosition - labelWidth + 2, tickLength + font.getAscent());
				if (showBottom) font.drawString(g, label, labelPosition - labelWidth + 2, h - tickLength - font.getDescent());
			}
		}
		if (showLeft || showRight) {
			for (int j = 1, n = (int)Math.ceil(h / pixelsPerLabel); j <= n; j++) {
				int labelPosition = (int)Math.ceil(j * pixelsPerLabel);
				String label = myDoubleToString(j / labelsPerUnit);
				int labelWidth = font.stringWidth(label);
				if (showLeft) font.drawString(g, label, tickLength + 2, labelPosition + 1);
				if (showRight) font.drawString(g, label, w - tickLength - labelWidth - 1, labelPosition + 1);
			}
		}
		
		if (mouseX >= 0 && mouseX < w && mouseY >= 0 && mouseY < h) {
			g.setColor(new Color(0, 0, 0, 80));
			if (showTop || showBottom) g.drawLine(mouseX, 0, mouseX, h-1);
			if (showLeft || showRight) g.drawLine(0, mouseY, w-1, mouseY);
			
			StringBuffer sb = new StringBuffer();
			if (showTop || showBottom) sb.append(myDoubleToString(mouseX / parameters.getPixelsPerUnit()));
			if ((showTop || showBottom) && (showLeft || showRight)) sb.append(", ");
			if (showLeft || showRight) sb.append(myDoubleToString(mouseY / parameters.getPixelsPerUnit()));
			String s = sb.toString();
			
			int sw = font.stringWidth(s);
			int sh = font.getHeight();
			g.setColor(Color.yellow);
			g.fillRect((w-sw)/2 - 4, (h-sh)/2 - 1, sw + 7, sh + 2);
			g.setColor(Color.black);
			font.drawString(g, s, (w-sw)/2, (h-sh)/2 + font.getAscent());
		}
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
		int w = getWidth();
		int h = getHeight();
		showBottom = (w > 50);
		showTop = showBottom && (h > 30);
		showRight = (h > 50);
		showLeft = showRight && (w > 30);
		repaint();
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		int w = getWidth();
		int h = getHeight();
		showBottom = (w > 50);
		showTop = showBottom && (h > 30);
		showRight = (h > 50);
		showLeft = showRight && (w > 30);
		repaint();
	}
	
	@Override public void componentMoved(ComponentEvent e) {}
	@Override public void componentHidden(ComponentEvent e) {}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (!mouseLocked) {
			mouseX = e.getX();
			mouseY = e.getY();
			repaint();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (!mouseLocked) {
			mouseX = e.getX();
			mouseY = e.getY();
			repaint();
		}
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		if (!mouseLocked) {
			mouseX = -1;
			mouseY = -1;
			repaint();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			isPopup = true;
			makeRulerMenu().show(e.getComponent(), e.getX(), e.getY());
		} else {
			isPopup = false;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (!mouseLocked) {
			mouseX = e.getX();
			mouseY = e.getY();
			repaint();
		}
		isPopup = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			isPopup = false;
			makeRulerMenu().show(e.getComponent(), e.getX(), e.getY());
		} else if (isPopup) {
			isPopup = false;
		} else {
			mouseX = e.getX();
			mouseY = e.getY();
			mouseLocked = !mouseLocked;
			repaint();
		}
	}
	
	@Override public void mouseClicked(MouseEvent e) {}
	
	private JPopupMenu makeRulerMenu() {
		JPopupMenu rulerMenu = new JPopupMenu();
		for (int i = 0, m = Math.min(units.length, unitNames.length); i < m; i++) {
			JRadioButtonMenuItem rulerMenuItem = new JRadioButtonMenuItem(unitNames[i]);
			rulerMenuItem.setSelected(unitIndex == i);
			rulerMenu.add(rulerMenuItem);
			final int j = i;
			rulerMenuItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					unitIndex = j;
					repaint();
				}
			});
		}
		return rulerMenu;
	}
	
	private static final String myDoubleToString(double d) {
		if (d == (long)d) {
			return Long.toString((long)d);
		} else {
			String s = Double.toString(d);
			if (s.contains(".")) {
				return (s + "000").substring(0, s.indexOf(".") + 4);
			} else {
				return s;
			}
		}
	}
}
