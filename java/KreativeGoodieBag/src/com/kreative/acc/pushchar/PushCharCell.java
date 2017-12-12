package com.kreative.acc.pushchar;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import com.kreative.acc.shared.unidata.CharacterInfo;
import com.kreative.acc.shared.unidata.UniDataUtilities;

public class PushCharCell extends JLabel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	private final int codePoint;
	private final JLabel footerLabel;
	
	public PushCharCell(Font font, int codePoint, JLabel footerLabel) {
		super(new String(Character.toChars(codePoint)));
		setFont(font);
		setHorizontalAlignment(JLabel.CENTER);
		setOpaque(true);
		setBackground(Color.white);
		setForeground(Color.black);
		setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		addMouseListener(this);
		addMouseMotionListener(this);
		this.codePoint = codePoint;
		this.footerLabel = footerLabel;
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		setBackground(SystemColor.textHighlight);
		setForeground(SystemColor.textHighlightText);
		StringBuffer infoString = new StringBuffer();
		String h = "000000" + Integer.toHexString(codePoint);
		h = h.substring(h.length() - ((codePoint >= 0x10000) ? 6 : 4)).toUpperCase();
		infoString.append("U+");
		infoString.append(h);
		infoString.append("    #");
		infoString.append(Integer.toString(codePoint));
		CharacterInfo charInfo = UniDataUtilities.getUnicodeProperties().get(codePoint);
		if (charInfo != null) {
			infoString.append("    ");
			infoString.append(charInfo.getCharacterName());
			infoString.append("    ");
			infoString.append(charInfo.getCharacterClassDescription());
		}
		footerLabel.setText(infoString.toString());
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		setBackground(Color.white);
		setForeground(Color.black);
		footerLabel.setText(" ");
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger()) {
			new CopyMenu(codePoint).show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			new CopyMenu(codePoint).show(e.getComponent(), e.getX(), e.getY());
		}
	}
	
	@Override public void mouseClicked(MouseEvent e) {}
	@Override public void mouseMoved(MouseEvent e) {}
	@Override public void mouseDragged(MouseEvent e) {}
}
