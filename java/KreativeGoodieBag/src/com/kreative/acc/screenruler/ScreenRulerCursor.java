package com.kreative.acc.screenruler;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class ScreenRulerCursor {
	private static final int K = 0xFF000000;
	
	private static final int[] CURSOR_A = {
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		K, K, K, K, K, K, K, K, K, K, K, K, K, K, K, K, K, K, K,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 0, 0, K, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	};
	
	public static final Cursor CURSOR = makeCursor(CURSOR_A, 19, 19, 9, 9, "ThinCrosshair");
	
	private ScreenRulerCursor() {}
	
	private static final Cursor makeCursor(int[] rgb, int width, int height, int hotx, int hoty, String name) {
		try {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension d = tk.getBestCursorSize(width, height);
			if (d.width <= 0 || d.height <= 0) {
				return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
			}
			if (d.width < width || d.height < height) {
				d = tk.getBestCursorSize(width*2, height*2);
				if (d.width < width || d.height < height) {
					return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
				}
			}
			BufferedImage img = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
			img.setRGB(0, 0, width, height, rgb, 0, width);
			return tk.createCustomCursor(img, new Point(hotx, hoty), name);
		} catch (Exception e) {
			return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
		}
	}
}
