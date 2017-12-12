package com.kreative.acc.screenruler;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class ScreenRulerResizeListener implements ComponentListener {
	private final JFrame rulerFrame;
	private final JComponent rulerComponent;
	
	public ScreenRulerResizeListener(JFrame rulerFrame, JComponent rulerComponent) {
		this.rulerFrame = rulerFrame;
		this.rulerComponent = rulerComponent;
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		int frameWidth = rulerFrame.getWidth();
		int frameHeight = rulerFrame.getHeight();
		int widthPadding = frameWidth - rulerComponent.getWidth();
		int heightPadding = frameHeight - rulerComponent.getHeight();
		if (frameWidth - widthPadding < 30) {
			if (frameWidth - widthPadding != 24) {
				rulerFrame.setSize(24 + widthPadding, frameHeight);
			}
		} else if (frameWidth - widthPadding < 50) {
			if (frameWidth - widthPadding != 48) {
				rulerFrame.setSize(48 + widthPadding, frameHeight);
			}
		} else if (frameHeight - heightPadding < 30) {
			if (frameHeight - heightPadding != 16) {
				rulerFrame.setSize(frameWidth, 16 + heightPadding);
			}
		} else if (frameHeight - heightPadding < 50) {
			if (frameHeight - heightPadding != 32) {
				rulerFrame.setSize(frameWidth, 32 + heightPadding);
			}
		}
	}
	
	@Override public void componentShown(ComponentEvent e) {}
	@Override public void componentMoved(ComponentEvent e) {}
	@Override public void componentHidden(ComponentEvent e) {}
}
