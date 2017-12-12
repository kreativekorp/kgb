package com.kreative.recode.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationGUI;

public class TextTransformationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final Color HEADER_BACKGROUND_COLOR = new Color(0xFF4D4C67);
	private static final Color HEADER_TEXT_COLOR = new Color(0xFFFFFFFF);
	
	private static final Color WIDGET_BORDER_COLOR = new Color(0xFF333333);
	private static final Color WIDGET_PRESSED_COLOR = new Color(0xFF999999);
	private static final Color WIDGET_RELEASED_COLOR = new Color(0xFFEEEEEE);
	
	private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();
	private static final Image X_IMAGE = TOOLKIT.createImage(TextTransformationPanel.class.getResource("x.png"));
	private static final Image UP_IMAGE = TOOLKIT.createImage(TextTransformationPanel.class.getResource("up.png"));
	private static final Image DOWN_IMAGE = TOOLKIT.createImage(TextTransformationPanel.class.getResource("dn.png"));
	
	private final TextTransformationFactory txFactory;
	private final List<String> txArgs;
	private final TextTransformationGUI txGUI;
	
	public TextTransformationPanel(TextTransformationFactory txFactory, List<String> txArgs) {
		this.txFactory = txFactory;
		if (txArgs == null) {
			this.txArgs = new ArrayList<String>();
			this.txGUI = txFactory.createGUI();
		} else {
			this.txArgs = txArgs;
			this.txGUI = txFactory.createGUI(txArgs);
		}
		
		JButton upButton = new JButton(new ImageIcon(UP_IMAGE));
		upButton.setToolTipText("Move Up");
		upButton.putClientProperty("JButton.buttonType", "square");
		JButton downButton = new JButton(new ImageIcon(DOWN_IMAGE));
		downButton.setToolTipText("Move Down");
		downButton.putClientProperty("JButton.buttonType", "square");
		JButton removeButton = new JButton(new ImageIcon(X_IMAGE));
		removeButton.setToolTipText("Remove");
		removeButton.putClientProperty("JButton.buttonType", "square");
		JPanel buttonPanel = new JPanel(new GridLayout(1, 0, 2, 2));
		buttonPanel.add(modifyButtonToStopAppleFromAddingTheirStupidPadding(upButton));
		buttonPanel.add(modifyButtonToStopAppleFromAddingTheirStupidPadding(downButton));
		buttonPanel.add(modifyButtonToStopAppleFromAddingTheirStupidPadding(removeButton));
		buttonPanel.setOpaque(false);
		
		JLabel titleLabel = new JLabel(txFactory.getName());
		titleLabel.setToolTipText(txFactory.getDescription());
		titleLabel.setOpaque(false);
		titleLabel.setForeground(HEADER_TEXT_COLOR);
		titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
		
		JPanel headerPanel = new JPanel(new BorderLayout(2, 2));
		headerPanel.add(titleLabel, BorderLayout.CENTER);
		headerPanel.add(buttonPanel, BorderLayout.LINE_END);
		headerPanel.setOpaque(true);
		headerPanel.setBackground(HEADER_BACKGROUND_COLOR);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		JPanel mainPanel = new JPanel(new BorderLayout(4, 4));
		if (txGUI == null) {
			mainPanel.add(headerPanel, BorderLayout.CENTER);
			mainPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		} else {
			mainPanel.add(headerPanel, BorderLayout.NORTH);
			mainPanel.add(txGUI.getComponent(), BorderLayout.CENTER);
			mainPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 8, 4));
		}
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		
		upButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveUpInParent();
			}
		});
		downButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDownInParent();
			}
		});
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeFromParent();
			}
		});
	}
	
	public TextTransformation createTransformation() {
		if (txGUI == null) {
			return txFactory.createTransformation(txArgs);
		} else {
			return txGUI.createTransformation();
		}
	}
	
	public void moveUpInParent() {
		Container p = this.getParent();
		int i = getComponentIndex(p, this);
		if (i > 0) {
			p.remove(i);
			p.add(this, i-1);
			revalidate(p);
		}
	}
	
	public void moveDownInParent() {
		Container p = this.getParent();
		int i = getComponentIndex(p, this);
		if (i >= 0 && i < p.getComponentCount() - 1) {
			p.remove(i);
			p.add(this, i+1);
			revalidate(p);
		}
	}
	
	public void removeFromParent() {
		Container p = this.getParent();
		int i = getComponentIndex(p, this);
		if (i >= 0) {
			p.remove(i);
			revalidate(p);
		}
	}
	
	private static int getComponentIndex(Container p, Component c) {
		for (int i = 0, m = p.getComponentCount(); i < m; i++) {
			if (p.getComponent(i) == c) {
				return i;
			}
		}
		return -1;
	}
	
	private static void revalidate(Container p) {
		if (p instanceof JComponent) {
			((JComponent)p).revalidate();
		} else {
			p.validate();
		}
	}
	
	private static <C extends JComponent> C modifyButtonToStopAppleFromAddingTheirStupidPadding(final C c) {
		c.setOpaque(true);
		c.setBackground(WIDGET_RELEASED_COLOR);
		c.setBorder(BorderFactory.createLineBorder(WIDGET_BORDER_COLOR));
		c.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				c.setBackground(WIDGET_PRESSED_COLOR);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				c.setBackground(WIDGET_RELEASED_COLOR);
			}
		});
		return c;
	}
}
