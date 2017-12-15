package com.kreative.ponyfix;

import javax.swing.JFrame;

public class PonyFix {
	public static void main(String[] args) {
		JFrame f = new JFrame("PonyFix");
		f.setContentPane(new PonyFixPanel());
		f.pack();
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
