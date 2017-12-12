package com.kreative.kte;

import java.io.*;
import java.util.*;

public class TEncHacker {
	private static enum ArgumentType {
		INFILE_ISO, INFILE_ADOBE, OUTFILE,
		BTOC_BYTESEQ, BTOC_CHARSEQ,
		CTOB_BYTESEQ, CTOB_CHARSEQ,
		ID, NAME_ADD, NAME_REMOVE, CITE_ADD, CITE_REMOVE
	}
	
	public static void main(String[] args) {
		boolean processOptions = true;
		ArgumentType argumentType = ArgumentType.INFILE_ISO;
		boolean includeC0 = true, includeASCII = true, includeC1 = false, includeLatin1 = false;
		TextEncoding te = null;
		ByteSequence bseq = null;
		CharacterSequence cseq = null;
		for (String arg : args) {
			if (processOptions && (arg.startsWith("-") || arg.startsWith("+"))) {
				if (arg.equals("--")) processOptions = false;
				else if (arg.equals("-i") || arg.equals("+i")) argumentType = ArgumentType.INFILE_ISO;
				else if (arg.equals("-I") || arg.equals("+I")) argumentType = ArgumentType.INFILE_ADOBE;
				else if (arg.equals("-o") || arg.equals("+o")) argumentType = ArgumentType.OUTFILE;
				else if (arg.equals("-O") || arg.equals("+O")) argumentType = ArgumentType.OUTFILE;
				else if (arg.equals("-s") || arg.equals("+s")) argumentType = ArgumentType.BTOC_BYTESEQ;
				else if (arg.equals("-S") || arg.equals("+S")) argumentType = ArgumentType.CTOB_CHARSEQ;
				else if (arg.equals("-N") || arg.equals("+N")) argumentType = ArgumentType.ID;
				else if (arg.equals("+n")) argumentType = ArgumentType.NAME_ADD;
				else if (arg.equals("-n")) argumentType = ArgumentType.NAME_REMOVE;
				else if (arg.equals("+b")) argumentType = ArgumentType.CITE_ADD;
				else if (arg.equals("-b")) argumentType = ArgumentType.CITE_REMOVE;
				else if (arg.equals("-c")) includeC0 = false;
				else if (arg.equals("+c")) includeC0 = true;
				else if (arg.equals("-a")) includeASCII = false;
				else if (arg.equals("+a")) includeASCII = true;
				else if (arg.equals("-C")) includeC1 = false;
				else if (arg.equals("+C")) includeC1 = true;
				else if (arg.equals("-A")) includeLatin1 = false;
				else if (arg.equals("+A")) includeLatin1 = true;
				else System.err.println("Unknown option: " + arg);
			} else switch (argumentType) {
			case INFILE_ISO:
				if (te != null) printEncodingTable(te.encodingTable());
				te = new TextEncoding(0, includeC0, includeASCII, includeC1, includeLatin1);
				try {
					Scanner scan = new Scanner(new File(arg));
					while (scan.hasNextLine()) {
						String[] fields = scan.nextLine().replaceAll("#.*?$", "").trim().split("\\s+");
						if (fields.length >= 2) {
							ByteSequence b = ByteSequence.parseByteSequence(fields[0]);
							CharacterSequence c = CharacterSequence.parseCharacterSequence(fields[1]);
							te.encodingTable().put(b, c, true, false);
						}
					}
					scan.close();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				break;
			case INFILE_ADOBE:
				if (te != null) printEncodingTable(te.encodingTable());
				te = new TextEncoding(0, includeC0, includeASCII, includeC1, includeLatin1);
				try {
					Scanner scan = new Scanner(new File(arg));
					while (scan.hasNextLine()) {
						String[] fields = scan.nextLine().replaceAll("#.*?$", "").trim().split("\\s+");
						if (fields.length >= 2) {
							ByteSequence b = ByteSequence.parseByteSequence(fields[1]);
							CharacterSequence c = CharacterSequence.parseCharacterSequence(fields[0]);
							te.encodingTable().put(b, c, false, true);
						}
					}
					scan.close();
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
				argumentType = ArgumentType.INFILE_ISO;
				break;
			case OUTFILE:
				try {
					FileOutputStream fout = new FileOutputStream(new File(arg));
					DataOutputStream dout = new DataOutputStream(fout);
					te.write(dout);
					dout.flush();
					dout.close();
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
				te = null;
				argumentType = ArgumentType.INFILE_ISO;
				break;
			case BTOC_BYTESEQ:
				bseq = ByteSequence.parseByteSequence(arg);
				argumentType = ArgumentType.BTOC_CHARSEQ;
				break;
			case BTOC_CHARSEQ:
				cseq = CharacterSequence.parseCharacterSequence(arg);
				te.encodingTable().put(bseq, cseq, true, false);
				cseq = null;
				bseq = null;
				argumentType = ArgumentType.INFILE_ISO;
				break;
			case CTOB_CHARSEQ:
				cseq = CharacterSequence.parseCharacterSequence(arg);
				argumentType = ArgumentType.CTOB_BYTESEQ;
				break;
			case CTOB_BYTESEQ:
				bseq = ByteSequence.parseByteSequence(arg);
				te.encodingTable().put(bseq, cseq, false, true);
				cseq = null;
				bseq = null;
				argumentType = ArgumentType.INFILE_ISO;
				break;
			case ID:
				te.setId(IntLookupTable.parseInt(arg));
				argumentType = ArgumentType.INFILE_ISO;
				break;
			case NAME_ADD:
				if (arg.startsWith("d:")) te.nameTable().add(new Name(NameType.DISPLAY, arg.substring(2)));
				else if (arg.startsWith("i:")) te.nameTable().add(new Name(NameType.INTERNAL, arg.substring(2)));
				else te.nameTable().add(new Name(NameType.BOTH, arg));
				argumentType = ArgumentType.INFILE_ISO;
				break;
			case NAME_REMOVE:
				Iterator<Name> itn = te.nameTable().iterator();
				while (itn.hasNext()) {
					if (itn.next().toString().equalsIgnoreCase(arg)) {
						itn.remove();
					}
				}
				argumentType = ArgumentType.INFILE_ISO;
				break;
			case CITE_ADD:
				CiteTable citeTable = new CiteTable();
				String[] citeInfo = arg.split(";");
				for (String cite : citeInfo) {
					String[] citeComp = cite.split(":", 2);
					if (citeComp.length < 2) citeTable.add(new Cite(CiteType.MAJOR_TITLE, citeComp[0]));
					else if (citeComp[0].equalsIgnoreCase("a")) citeTable.add(new Cite(CiteType.AUTHOR, citeComp[1]));
					else if (citeComp[0].equals("T")) citeTable.add(new Cite(CiteType.MAJOR_TITLE, citeComp[1]));
					else if (citeComp[0].equals("t")) citeTable.add(new Cite(CiteType.MINOR_TITLE, citeComp[1]));
					else if (citeComp[0].equalsIgnoreCase("p")) citeTable.add(new Cite(CiteType.PUBLISHER, citeComp[1]));
					else if (citeComp[0].equalsIgnoreCase("d")) citeTable.add(new Cite(CiteType.DATE, citeComp[1]));
					else if (citeComp[0].equalsIgnoreCase("u")) citeTable.add(new Cite(CiteType.URL, citeComp[1]));
					else if (citeComp[0].equalsIgnoreCase("c")) citeTable.add(new Cite(CiteType.COPYRIGHT, citeComp[1]));
					else citeTable.add(new Cite(CiteType.MAJOR_TITLE, citeComp[1]));
				}
				if (!citeTable.isEmpty()) {
					te.addCiteTable(citeTable);
				}
				argumentType = ArgumentType.INFILE_ISO;
				break;
			case CITE_REMOVE:
				Iterator<CiteTable> itc = te.citeTable().iterator();
				while (itc.hasNext()) {
					CiteTable ct = itc.next();
					Iterator<Cite> itct = ct.iterator();
					while (itct.hasNext()) {
						if (itct.next().toString().equalsIgnoreCase(arg)) {
							itc.remove();
							break;
						}
					}
				}
				argumentType = ArgumentType.INFILE_ISO;
				break;
			}
		}
		if (te != null) printEncodingTable(te.encodingTable());
	}
	
	public static void printEncodingTable(EncodingTable et) {
		PrintWriter out;
		try {
			out = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true);
		} catch (Exception e) {
			out = new PrintWriter(new OutputStreamWriter(System.out), true);
		}
		out.println("Byte Seq Contains Prefix: " + (et.byteSequenceContainsAnotherAsPrefix() ? "YES" : "NO"));
		out.println("Byte Seqs Are Not Unique: " + (et.byteSequencesAreNonUnique() ? "YES" : "NO"));
		out.println("Char Seq Contains Prefix: " + (et.characterSequenceContainsAnotherAsPrefix() ? "YES" : "NO"));
		out.println("Char Seqs Are Not Unique: " + (et.characterSequencesAreNonUnique() ? "YES" : "NO"));
		out.println("Minimum Bytes In Sequence: " + et.minBytes());
		out.println("Maximum Bytes In Sequence: " + et.maxBytes());
		out.println("Minimum Chars In Sequence: " + et.minChars());
		out.println("Maximum Chars In Sequence: " + et.maxChars());
		out.println("Reading Table:");
		for (Map.Entry<ByteSequence, CharacterSequence> e : et.byteToCharacterMap().entrySet()) {
			out.println("\t" + e.getKey().toString() + " -> " + e.getValue().toString().replaceAll("[\\x00-\\x1F\\x7F-\\x9F]", " "));
		}
		out.println("Writing Table:");
		for (Map.Entry<CharacterSequence, ByteSequence> e : et.characterToByteMap().entrySet()) {
			out.println("\t" + e.getKey().toString().replaceAll("[\\x00-\\x1F\\x7F-\\x9F]", " ") + " -> " + e.getValue().toString());
		}
	}
}
