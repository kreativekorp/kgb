package com.kreative.acc.screenruler;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScreenRulerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	public ScreenRulerFrame(ScreenRulerParameters[] units, String[] unitNames, int unitIndex) {
		super("Screen Ruler");
		if (isMacOS()) {
			setBackground(new Color(255, 255, 200, 128));
		}
		
		ScreenRulerComponent ruler = new ScreenRulerComponent(units, unitNames, unitIndex);
		if (!isMacOS()) {
			ruler.setOpaque(true);
			ruler.setBackground(new Color(255, 255, 200));
		}
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(ruler, BorderLayout.CENTER);
		setContentPane(main);
		
		addComponentListener(new ScreenRulerResizeListener(this, ruler));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(384, 50);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private static String osString = null;
	private static boolean isMacOS() {
		if (osString == null) {
			try {
				osString = System.getProperty("os.name").toUpperCase();
			} catch (Exception e) {
				osString = "";
			}
		}
		return osString.contains("MAC OS");
	}
}
