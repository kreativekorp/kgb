package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public enum CiteType {
	AUTHOR(1),
	MAJOR_TITLE(2),
	MINOR_TITLE(3),
	PUBLISHER(4),
	DATE(5),
	URL(6),
	COPYRIGHT(7);
	
	private int value;
	
	private CiteType(int value) {
		this.value = value;
	}
	
	public int value() {
		return this.value;
	}
	
	public static CiteType forValue(int v) {
		for (CiteType ct : values()) {
			if (ct.value == v) return ct;
		}
		return null;
	}
	
	public void write(DataOutput out) throws IOException {
		out.writeByte(value);
	}
	
	public static CiteType read(DataInput in) throws IOException {
		int v = in.readUnsignedByte();
		for (CiteType ct : values()) {
			if (ct.value == v) return ct;
		}
		throw new IOException("Invalid cite type: " + v);
	}
}
