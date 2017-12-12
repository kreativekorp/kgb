package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public enum NameType {
	DISPLAY(2),
	INTERNAL(3),
	BOTH(1);
	
	private int value;
	
	private NameType(int value) {
		this.value = value;
	}
	
	public int value() {
		return this.value;
	}
	
	public static NameType forValue(int v) {
		for (NameType nt : values()) {
			if (nt.value == v) return nt;
		}
		return null;
	}
	
	public void write(DataOutput out) throws IOException {
		out.writeByte(value);
	}
	
	public static NameType read(DataInput in) throws IOException {
		int v = in.readUnsignedByte();
		for (NameType nt : values()) {
			if (nt.value == v) return nt;
		}
		throw new IOException("Invalid name type: " + v);
	}
}
