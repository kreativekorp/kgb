package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Name {
	private NameType type;
	private String name;
	
	public Name(NameType type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public NameType getNameType() {
		return this.type;
	}
	
	public String toString() {
		return this.name;
	}
	
	public void write(DataOutput out) throws IOException {
		type.write(out);
		out.writeByte(name.length());
		out.writeBytes(name);
	}
	
	public static Name read(DataInput in) throws IOException {
		NameType t = NameType.read(in);
		int l = in.readUnsignedByte();
		char[] s = new char[l];
		for (int i = 0; i < l; i++) {
			s[i] = (char)in.readUnsignedByte();
		}
		return new Name(t, String.valueOf(s));
	}
}
