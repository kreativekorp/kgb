package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Algorithm {
	private boolean bsHasPrefix, bsNonUnique, csHasPrefix, csNonUnique;
	private int minBytes, maxBytes, minChars, maxChars, maxRegisterCount, maxStackSize;
	private int[] instructions;
	
	public Algorithm(
			boolean bsHasPrefix, boolean bsNonUnique, boolean csHasPrefix, boolean csNonUnique,
			int minBytes, int maxBytes, int minChars, int maxChars, int maxRegisterCount, int maxStackSize,
			int[] instructions
	) {
		this.bsHasPrefix = bsHasPrefix;
		this.bsNonUnique = bsNonUnique;
		this.csHasPrefix = csHasPrefix;
		this.csNonUnique = csNonUnique;
		this.minBytes = minBytes;
		this.maxBytes = maxBytes;
		this.minChars = minChars;
		this.maxChars = maxChars;
		this.maxRegisterCount = maxRegisterCount;
		this.maxStackSize = maxStackSize;
		this.instructions = instructions;
	}
	
	public boolean byteSequenceContainsAnotherAsPrefix() {
		return bsHasPrefix;
	}
	
	public boolean byteSequencesAreNonUnique() {
		return bsNonUnique;
	}
	
	public boolean characterSequenceContainsAnotherAsPrefix() {
		return csHasPrefix;
	}
	
	public boolean characterSequencesAreNonUnique() {
		return csNonUnique;
	}
	
	public int minBytes() {
		return minBytes;
	}
	
	public int maxBytes() {
		return maxBytes;
	}
	
	public int minChars() {
		return minChars;
	}
	
	public int maxChars() {
		return maxChars;
	}
	
	public int maxRegisterCount() {
		return maxRegisterCount;
	}
	
	public int maxStackSize() {
		return maxStackSize;
	}
	
	public int[] instructions() {
		return instructions;
	}
	
	public void write(DataOutput out) throws IOException {
		out.writeByte(minBytes);
		out.writeByte(maxBytes);
		out.writeByte(minChars);
		out.writeByte(maxChars);
		out.writeShort(maxRegisterCount);
		out.writeShort(maxStackSize);
		out.writeInt(instructions.length);
		for (int i : instructions) {
			out.writeInt(i);
		}
	}
	
	public static Algorithm read(DataInput in, int tencFlags) throws IOException {
		int mb = in.readUnsignedByte();
		int xb = in.readUnsignedByte();
		int mc = in.readUnsignedByte();
		int xc = in.readUnsignedByte();
		int mr = in.readUnsignedShort();
		int ms = in.readUnsignedShort();
		int count = in.readInt();
		int[] inst = new int[count];
		for (int i = 0; i < count; i++) {
			inst[i] = in.readInt();
		}
		return new Algorithm(
				(tencFlags & 0x01) != 0, (tencFlags & 0x02) != 0, (tencFlags & 0x04) != 0, (tencFlags & 0x08) != 0,
				mb, xb, mc, xc, mr, ms, inst
		);
	}
}
