package com.kreative.kte;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class TEncDisassembler {
	public static void main(String[] args) {
		for (String arg : args) {
			System.out.println(arg);
			try {
				File inf = new File(arg);
				FileInputStream ins = new FileInputStream(inf);
				DataInputStream ind = new DataInputStream(ins);
				TextEncoding te = TextEncoding.read(ind);
				ind.close();
				ins.close();
				String outp = (arg.toLowerCase().endsWith(".kte") ? arg.substring(0, arg.length()-4) : arg) + ".txt";
				File outf = new File(outp);
				FileOutputStream outs = new FileOutputStream(outf);
				OutputStreamWriter outw = new OutputStreamWriter(outs, "UTF-8");
				PrintWriter out = new PrintWriter(outw, true);
				te.disassemble(out);
				out.flush();
				outw.flush();
				outs.flush();
				out.close();
				outw.close();
				outs.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
