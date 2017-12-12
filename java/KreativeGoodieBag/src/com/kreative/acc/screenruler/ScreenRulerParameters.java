package com.kreative.acc.screenruler;

public class ScreenRulerParameters {
	private final double pixelsPerUnit;
	private final double labelsPerUnit;
	private final int[] tickLengths;
	private final double[] ticksPerUnit;
	
	protected ScreenRulerParameters(double pixelsPerUnit, double labelsPerUnit, int[] tickLengths, double[] ticksPerUnit) {
		this.pixelsPerUnit = pixelsPerUnit;
		this.labelsPerUnit = labelsPerUnit;
		this.tickLengths = tickLengths;
		this.ticksPerUnit = ticksPerUnit;
	}
	
	public static ScreenRulerParameters forPixels() {
		return new ScreenRulerParameters(
				1, 1.0/32.0,
				new int[] { 5, 4, 3, 2, 1 },
				new double[] { 1.0/32.0, 1.0/16.0, 1.0/8.0, 1.0/4.0, 1.0/2.0 }
		);
	}
	
	public static ScreenRulerParameters forInches(double ppu) {
		return new ScreenRulerParameters(
				ppu, 1,
				new int[] { 6, 5, 4, 3, 2, 1 },
				new double[] { 1, 2, 4, 8, 16, 32 }
		);
	}
	
	public static ScreenRulerParameters forCentimeters(double ppu) {
		return new ScreenRulerParameters(
				ppu, 1,
				new int[] { 6, 4, 2 },
				new double[] { 1, 2, 10 }
		);
	}
	
	public double getPixelsPerUnit() {
		return pixelsPerUnit;
	}
	
	public double getLabelsPerUnit() {
		return labelsPerUnit;
	}
	
	public int getTickCount() {
		return Math.min(tickLengths.length, ticksPerUnit.length);
	}
	
	public int getTickLength(int index) {
		return tickLengths[index];
	}
	
	public double getTicksPerUnit(int index) {
		return ticksPerUnit[index];
	}
	
	public int getMaxTickLength() {
		int maxTickLength = 0;
		for (int tickLength : tickLengths) {
			if (tickLength > maxTickLength) {
				maxTickLength = tickLength;
			}
		}
		return maxTickLength;
	}
}
