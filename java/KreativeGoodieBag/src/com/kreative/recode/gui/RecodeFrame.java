package com.kreative.recode.gui;

import javax.swing.JFrame;
import com.kreative.recode.RecodeArgumentParser;

public class RecodeFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public RecodeFrame(RecodeArgumentParser args) {
		super("Kreative Recode");
		setContentPane(new RecodePanel(args));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
