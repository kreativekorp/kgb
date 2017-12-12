package com.kreative.kte;

public class TEncMain {
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage: java -jar kte.jar (assemble|disassemble|dump) [file] [file] [...]");
		} else {
			String command = args[0];
			String[] cargs = new String[args.length-1];
			for (int i = 1; i < args.length; i++) {
				cargs[i-1] = args[i];
			}
			if (command.equalsIgnoreCase("assemble") || command.equalsIgnoreCase("asm") || command.equalsIgnoreCase("a")) {
				TEncAssembler.main(cargs);
			} else if (command.equalsIgnoreCase("disassemble") || command.equalsIgnoreCase("disasm") || command.equalsIgnoreCase("d")) {
				TEncDisassembler.main(cargs);
			} else if (command.equalsIgnoreCase("dump") || command.equalsIgnoreCase("x")) {
				TEncDumper.main(cargs);
			} else if (command.equalsIgnoreCase("hack") || command.equalsIgnoreCase("h")) {
				TEncHacker.main(cargs);
			} else {
				System.out.println("Usage: java -jar kte.jar (assemble|disassemble|dump) [file] [file] [...]");
			}
		}
	}
}
