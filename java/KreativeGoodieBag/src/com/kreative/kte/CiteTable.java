package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

public class CiteTable extends ArrayList<Cite> {
	private static final long serialVersionUID = 1L;
	
	public void write(DataOutput out) throws IOException {
		out.writeByte(size());
		for (Cite c : this) {
			c.write(out);
		}
	}
	
	public static CiteTable read(DataInput in) throws IOException {
		int count = in.readUnsignedByte();
		CiteTable ct = new CiteTable();
		for (int i = 0; i < count; i++) {
			ct.add(Cite.read(in));
		}
		return ct;
	}
}
