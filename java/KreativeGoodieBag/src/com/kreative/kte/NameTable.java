package com.kreative.kte;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;

public class NameTable extends ArrayList<Name> {
	private static final long serialVersionUID = 1L;
	
	public void write(DataOutput out) throws IOException {
		out.writeByte(size());
		for (Name n : this) {
			n.write(out);
		}
	}
	
	public static NameTable read(DataInput in) throws IOException {
		int count = in.readUnsignedByte();
		NameTable nt = new NameTable();
		for (int i = 0; i < count; i++) {
			nt.add(Name.read(in));
		}
		return nt;
	}
}
