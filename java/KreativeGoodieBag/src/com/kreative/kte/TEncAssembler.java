package com.kreative.kte;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TEncAssembler {
	public static void main(String[] args) {
		Set<Integer> ids = new HashSet<Integer>();
		for (String arg : args) {
			System.out.print(arg + "...");
			try {
				File inf = new File(arg);
				FileInputStream ins = new FileInputStream(inf);
				Scanner in = new Scanner(ins, "UTF-8");
				TextEncoding te = TextEncoding.assemble(in);
				in.close();
				ins.close();
				String outp = (arg.toLowerCase().endsWith(".txt") ? arg.substring(0, arg.length()-4) : arg) + ".kte";
				File outf = new File(outp);
				FileOutputStream outs = new FileOutputStream(outf);
				DataOutputStream outd = new DataOutputStream(outs);
				te.write(outd);
				outd.flush();
				outs.flush();
				outd.close();
				outs.close();
				System.out.print(" ID#" + te.id());
				System.out.println(te.encodingTable() == null ? " ALGORITHMIC" : te.encodingTable().isNice() ? " NICE" : " NOT NICE");
				if (te.id() == 0) {
					System.err.println("WARNING: No assigned ID number.");
				} else if (ids.contains(te.id())) {
					System.err.println("ERROR: Duplicate ID number " + te.id() + ".");
				} else {
					ids.add(te.id());
				}
			} catch (IOException e) {
				System.out.println(" FAILED");
				System.err.println(e.getMessage());
			}
		}
	}
}
