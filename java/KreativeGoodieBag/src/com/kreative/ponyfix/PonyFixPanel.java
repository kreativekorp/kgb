package com.kreative.ponyfix;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class PonyFixPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final Color dragColor = new Color(0xFFFFFFCC);
	
	private final PonyFixFilePanel leftPanel;
	private final PonyFixFilePanel rightPanel;
	private final JCheckBox autoFixCheckbox;
	private final JButton manualFixButton;
	
	public PonyFixPanel() {
		leftPanel = new PonyFixFilePanel("Drop WORKING Desktop Pony Here");
		rightPanel = new PonyFixFilePanel("Drop NON-WORKING Desktop Pony Here");
		autoFixCheckbox = new JCheckBox("Fix Automatically");
		manualFixButton = new JButton("Fix Now");
		
		PonyFixUtil.setTransparent(leftPanel);
		PonyFixUtil.setTransparent(rightPanel);
		
		final JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(autoFixCheckbox);
		buttonPanel.add(manualFixButton);
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.add(leftPanel);
		mainPanel.add(new JSeparator());
		mainPanel.add(rightPanel);
		mainPanel.add(new JSeparator());
		mainPanel.add(buttonPanel);
		
		setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.PAGE_START);
		
		new DropTarget(leftPanel, new PonyFixDropTarget() {
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				leftPanel.setOpaque(true);
				leftPanel.setBackground(dragColor);
			}
			@Override
			public void dragExit(DropTargetEvent dte) {
				leftPanel.setOpaque(false);
				leftPanel.setBackground(Color.white);
			}
			@Override
			public void dropped(File f) {
				leftPanel.setOpaque(false);
				leftPanel.setBackground(Color.white);
				leftPanel.setFile(f);
			}
		});
		
		new DropTarget(rightPanel, new PonyFixDropTarget() {
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				rightPanel.setOpaque(true);
				rightPanel.setBackground(dragColor);
			}
			@Override
			public void dragExit(DropTargetEvent dte) {
				rightPanel.setOpaque(false);
				rightPanel.setBackground(Color.white);
			}
			@Override
			public void dropped(File f) {
				rightPanel.setOpaque(false);
				rightPanel.setBackground(Color.white);
				rightPanel.setFile(f);
				if (autoFixCheckbox.isSelected()) {
					rightPanel.fixFile(leftPanel);
				}
			}
		});
		
		manualFixButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rightPanel.fixFile(leftPanel);
			}
		});
	}
}
