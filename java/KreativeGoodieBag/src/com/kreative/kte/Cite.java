package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Cite {
	private CiteType type;
	private String cite;
	
	public Cite(CiteType type, String cite) {
		this.type = type;
		this.cite = cite;
	}
	
	public CiteType getCiteType() {
		return this.type;
	}
	
	public String toString() {
		return this.cite;
	}
	
	public void write(DataOutput out) throws IOException {
		type.write(out);
		out.writeByte(cite.length());
		out.writeBytes(cite);
	}
	
	public static Cite read(DataInput in) throws IOException {
		CiteType t = CiteType.read(in);
		int l = in.readUnsignedByte();
		char[] s = new char[l];
		for (int i = 0; i < l; i++) {
			s[i] = (char)in.readUnsignedByte();
		}
		return new Cite(t, String.valueOf(s));
	}
}
