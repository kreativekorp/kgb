package com.kreative.recode.transformations;

import java.util.SortedMap;
import java.util.TreeMap;
import com.kreative.recode.transformation.TextTransformation;

public class CharacterFrequencyTransformation extends TextTransformation {
	private SortedMap<Integer,Long> histogram = new TreeMap<Integer,Long>();
	private long totalChars = 0;
	
	@Override
	public String getName() {
		return "Character Frequency";
	}
	
	@Override
	public String getDescription() {
		return "Counts the number of occurrences of each character and produces a list of character frequencies.";
	}
	
	@Override
	protected void startTransformation() {
		histogram = new TreeMap<Integer,Long>();
		totalChars = 0L;
	}
	
	@Override
	protected void transformCodePoint(int codePoint) {
		if (histogram.containsKey(codePoint)) {
			histogram.put(codePoint, histogram.get(codePoint) + 1L);
		} else {
			histogram.put(codePoint, 1L);
		}
		totalChars++;
	}
	
	@Override
	protected void stopTransformation() {
		for (SortedMap.Entry<Integer,Long> e : histogram.entrySet()) {
			int ch = e.getKey();
			long count = e.getValue();
			double percent = (double)count / (double)totalChars * 100.0;
			append(ch);
			append(" : ");
			append(Long.toString(count));
			append(" (");
			append(Double.toString(Math.round(percent * 100.0) / 100.0));
			append("%)");
			append("\n");
		}
	}
}
