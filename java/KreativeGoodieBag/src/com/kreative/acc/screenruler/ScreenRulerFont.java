package com.kreative.acc.screenruler;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ScreenRulerFont {
	private static final int K = 0xFF000000;
	
	private static final int[] ZERO_A = {
		0, K, K, K, K, 0,
		K, K, 0, 0, K, K,
		K, K, 0, 0, K, K,
		K, K, 0, 0, K, K,
		K, K, 0, 0, K, K,
		K, K, 0, 0, K, K,
		0, K, K, K, K, 0,
	};
	
	private static final int[] ONE_A = {
		0, 0, K, K, 0, 0,
		K, K, K, K, 0, 0,
		0, 0, K, K, 0, 0,
		0, 0, K, K, 0, 0,
		0, 0, K, K, 0, 0,
		0, 0, K, K, 0, 0,
		K, K, K, K, K, K,
	};
	
	private static final int[] TWO_A = {
		0, K, K, K, K, 0,
		K, K, 0, 0, K, K,
		0, 0, 0, 0, K, K,
		0, 0, 0, K, K, 0,
		0, 0, K, K, 0, 0,
		0, K, K, 0, 0, 0,
		K, K, K, K, K, K,
	};
	
	private static final int[] THREE_A = {
		K, K, K, K, K, K,
		0, 0, 0, K, K, 0,
		0, 0, K, K, 0, 0,
		0, K, K, K, K, 0,
		0, 0, 0, 0, K, K,
		K, K, 0, 0, K, K,
		0, K, K, K, K, 0,
	};
	
	private static final int[] FOUR_A = {
		0, 0, 0, K, K, 0,
		0, 0, K, K, K, 0,
		0, K, K, K, K, 0,
		K, K, 0, K, K, 0,
		K, K, K, K, K, K,
		0, 0, 0, K, K, 0,
		0, 0, 0, K, K, 0,
	};
	
	private static final int[] FIVE_A = {
		K, K, K, K, K, K,
		K, K, 0, 0, 0, 0,
		K, K, K, K, K, 0,
		0, 0, 0, 0, K, K,
		0, 0, 0, 0, K, K,
		K, K, 0, 0, K, K,
		0, K, K, K, K, 0,
	};
	
	private static final int[] SIX_A = {
		0, 0, K, K, K, 0,
		0, K, K, 0, 0, 0,
		K, K, 0, 0, 0, 0,
		K, K, K, K, K, 0,
		K, K, 0, 0, K, K,
		K, K, 0, 0, K, K,
		0, K, K, K, K, 0,
	};
	
	private static final int[] SEVEN_A = {
		K, K, K, K, K, K,
		0, 0, 0, 0, K, K,
		0, 0, 0, K, K, 0,
		0, 0, 0, K, K, 0,
		0, 0, K, K, 0, 0,
		0, 0, K, K, 0, 0,
		0, 0, K, K, 0, 0,
	};
	
	private static final int[] EIGHT_A = {
		0, K, K, K, K, 0,
		K, K, 0, 0, K, K,
		K, K, 0, 0, K, K,
		0, K, K, K, K, 0,
		K, K, 0, 0, K, K,
		K, K, 0, 0, K, K,
		0, K, K, K, K, 0,
	};
	
	private static final int[] NINE_A = {
		0, K, K, K, K, 0,
		K, K, 0, 0, K, K,
		K, K, 0, 0, K, K,
		0, K, K, K, K, K,
		0, 0, 0, 0, K, K,
		0, 0, 0, K, K, 0,
		0, K, K, K, 0, 0,
	};
	
	private static final int[] PERIOD_A = {
		K, K,
		K, K,
	};
	
	private static final int[] COMMA_A = {
		0, K, K,
		0, K, K,
		0, K, K,
		K, K, 0,
	};
	
	private static final Image ZERO = makeImage(ZERO_A, 6, 7);
	private static final Image ONE = makeImage(ONE_A, 6, 7);
	private static final Image TWO = makeImage(TWO_A, 6, 7);
	private static final Image THREE = makeImage(THREE_A, 6, 7);
	private static final Image FOUR = makeImage(FOUR_A, 6, 7);
	private static final Image FIVE = makeImage(FIVE_A, 6, 7);
	private static final Image SIX = makeImage(SIX_A, 6, 7);
	private static final Image SEVEN = makeImage(SEVEN_A, 6, 7);
	private static final Image EIGHT = makeImage(EIGHT_A, 6, 7);
	private static final Image NINE = makeImage(NINE_A, 6, 7);
	private static final Image PERIOD = makeImage(PERIOD_A, 2, 2);
	private static final Image COMMA = makeImage(COMMA_A, 3, 4);
	
	public int getAscent() {
		return 9;
	}
	
	public int getDescent() {
		return 2;
	}
	
	public int getHeight() {
		return 11;
	}
	
	public int stringWidth(String s) {
		int w = 0;
		for (char ch : s.toCharArray()) {
			switch (ch) {
			case '0': case '1': case '2': case '3': case '4':
			case '5': case '6': case '7': case '8': case '9':
				w += 7;
				break;
			case '.': case ',':
				w += 3;
				break;
			case ' ':
				w += 4;
				break;
			}
		}
		return w;
	}
	
	public void drawString(Graphics g, String s, int x, int y) {
		for (char ch : s.toCharArray()) {
			switch (ch) {
			case '0':
				g.drawImage(ZERO, x, y-7, null);
				x += 7;
				break;
			case '1':
				g.drawImage(ONE, x, y-7, null);
				x += 7;
				break;
			case '2':
				g.drawImage(TWO, x, y-7, null);
				x += 7;
				break;
			case '3':
				g.drawImage(THREE, x, y-7, null);
				x += 7;
				break;
			case '4':
				g.drawImage(FOUR, x, y-7, null);
				x += 7;
				break;
			case '5':
				g.drawImage(FIVE, x, y-7, null);
				x += 7;
				break;
			case '6':
				g.drawImage(SIX, x, y-7, null);
				x += 7;
				break;
			case '7':
				g.drawImage(SEVEN, x, y-7, null);
				x += 7;
				break;
			case '8':
				g.drawImage(EIGHT, x, y-7, null);
				x += 7;
				break;
			case '9':
				g.drawImage(NINE, x, y-7, null);
				x += 7;
				break;
			case '.':
				g.drawImage(PERIOD, x, y-2, null);
				x += 3;
				break;
			case ',':
				g.drawImage(COMMA, x-1, y-2, null);
				x += 3;
				break;
			case ' ':
				x += 4;
				break;
			}
		}
	}
	
	private static final Image makeImage(int[] rgb, int w, int h) {
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, w, h, rgb, 0, w);
		return image;
	}
}
