package com.kreative.kte;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class TextEncoding {
	private static final long MAGIC = 0xBB6B54450D0A1A0AL;
	private static final int TENC = 0x54454E43;
	private static final int NAME = 0x4E414D45;
	private static final int TABL = 0x5441424C;
	private static final int TABR = 0x54414252;
	private static final int TABW = 0x54414257;
	private static final int ALGR = 0x414C4752;
	private static final int ALGS = 0x414C4753;
	private static final int ALGW = 0x414C4757;
	private static final int TAbI = 0x54416249;
	private static final int cIte = 0x63497465;
	private static final int TEND = 0x54454E44;
	
	private int id;
	private NameTable names;
	private EncodingTable tables;
	private Algorithm algR;
	private Algorithm algS;
	private Algorithm algW;
	private TreeMap<Integer,IntLookupTable> itables;
	private ArrayList<CiteTable> cites;
	
	private TextEncoding() {}
	
	public TextEncoding(int id) {
		this.id = id;
		this.names = new NameTable();
		this.tables = new EncodingTable();
		this.algR = null;
		this.algS = null;
		this.algW = null;
		this.itables = null;
		this.cites = new ArrayList<CiteTable>();
	}
	
	public TextEncoding(int id, boolean includeC0, boolean includeASCII, boolean includeC1, boolean includeLatin1) {
		this.id = id;
		this.names = new NameTable();
		this.tables = new EncodingTable(includeC0, includeASCII, includeC1, includeLatin1);
		this.algR = null;
		this.algS = null;
		this.algW = null;
		this.itables = null;
		this.cites = new ArrayList<CiteTable>();
	}
	
	public TextEncoding(int id, Algorithm algR, Algorithm algS, Algorithm algW) {
		this.id = id;
		this.names = new NameTable();
		this.tables = null;
		this.algR = algR;
		this.algS = algS;
		this.algW = algW;
		this.itables = new TreeMap<Integer,IntLookupTable>();
		this.cites = new ArrayList<CiteTable>();
	}
	
	public int id() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public NameTable nameTable() {
		return names;
	}
	
	public EncodingTable encodingTable() {
		return tables;
	}
	
	public Algorithm readRelaxedAlgorithm() {
		return algR;
	}
	
	public Algorithm readStrictAlgorithm() {
		return algS;
	}
	
	public Algorithm writeAlgorithm() {
		return algW;
	}
	
	public void addIntLookupTable(IntLookupTable itable) {
		itables.put(itable.id(), itable);
	}
	
	public SortedMap<Integer,IntLookupTable> intLookupTables() {
		return Collections.unmodifiableSortedMap(itables);
	}
	
	public void addCiteTable(CiteTable ctable) {
		cites.add(ctable);
	}
	
	public List<CiteTable> citeTable() {
		return cites;
	}
	
	private void writeTENC(DataOutput out) throws IOException {
		out.writeByte(1);
		if (tables != null) {
			out.writeByte(2);
		} else if (itables == null || itables.size() == 0) {
			out.writeByte(3);
		} else {
			out.writeByte(4);
		}
		int flags = 0;
		if (
				(algR != null && algR.byteSequenceContainsAnotherAsPrefix()) ||
				(algS != null && algS.byteSequenceContainsAnotherAsPrefix()) ||
				(algW != null && algW.byteSequenceContainsAnotherAsPrefix()) ||
				(tables != null && tables.byteSequenceContainsAnotherAsPrefix())
		) flags |= 0x01;
		if (
				(algR != null && algR.byteSequencesAreNonUnique()) ||
				(algS != null && algS.byteSequencesAreNonUnique()) ||
				(algW != null && algW.byteSequencesAreNonUnique()) ||
				(tables != null && tables.byteSequencesAreNonUnique())
		) flags |= 0x02;
		if (
				(algR != null && algR.characterSequenceContainsAnotherAsPrefix()) ||
				(algS != null && algS.characterSequenceContainsAnotherAsPrefix()) ||
				(algW != null && algW.characterSequenceContainsAnotherAsPrefix()) ||
				(tables != null && tables.characterSequenceContainsAnotherAsPrefix())
		) flags |= 0x04;
		if (
				(algR != null && algR.characterSequencesAreNonUnique()) ||
				(algS != null && algS.characterSequencesAreNonUnique()) ||
				(algW != null && algW.characterSequencesAreNonUnique()) ||
				(tables != null && tables.characterSequencesAreNonUnique())
		) flags |= 0x08;
		if (tables != null) flags |= 0x30;
		out.writeShort(flags);
		out.writeByte(tables != null ? tables.minBytes() : algS != null ? algS.minBytes() : algR != null ? algR.minBytes() : algW != null ? algW.minBytes() : 0);
		out.writeByte(tables != null ? tables.maxBytes() : algS != null ? algS.maxBytes() : algR != null ? algR.maxBytes() : algW != null ? algW.maxBytes() : 0);
		out.writeByte(tables != null ? tables.minChars() : algS != null ? algS.minChars() : algR != null ? algR.minChars() : algW != null ? algW.minChars() : 0);
		out.writeByte(tables != null ? tables.maxChars() : algS != null ? algS.maxChars() : algR != null ? algR.maxChars() : algW != null ? algW.maxChars() : 0);
		out.writeShort(Math.max(algW != null ? algW.maxRegisterCount() : 0, Math.max(algS != null ? algS.maxRegisterCount() : 0, algR != null ? algR.maxRegisterCount() : 0)));
		out.writeShort(Math.max(algW != null ? algW.maxStackSize() : 0, Math.max(algS != null ? algS.maxStackSize() : 0, algR != null ? algR.maxStackSize() : 0)));
		out.writeInt(id);
	}
	
	public void write(DataOutput out) throws IOException {
		ByteArrayOutputStream cout;
		byte[] chunk;
		
		out.writeLong(MAGIC);
		
		cout = new ByteArrayOutputStream();
		writeTENC(new DataOutputStream(cout));
		cout.close();
		chunk = cout.toByteArray();
		out.writeInt(chunk.length);
		out.writeInt(TENC);
		out.write(chunk);
		if ((chunk.length & 3) != 0)
			out.write(new byte[4-(chunk.length & 3)]);
		out.writeInt(0);
		
		if (names != null && !names.isEmpty()) {
			cout = new ByteArrayOutputStream();
			names.write(new DataOutputStream(cout));
			cout.close();
			chunk = cout.toByteArray();
			out.writeInt(chunk.length);
			out.writeInt(NAME);
			out.write(chunk);
			if ((chunk.length & 3) != 0)
				out.write(new byte[4-(chunk.length & 3)]);
			out.writeInt(0);
		}
		
		if (tables != null) {
			cout = new ByteArrayOutputStream();
			tables.writeTABR(new DataOutputStream(cout));
			cout.close();
			chunk = cout.toByteArray();
			out.writeInt(chunk.length);
			out.writeInt(TABR);
			out.write(chunk);
			if ((chunk.length & 3) != 0)
				out.write(new byte[4-(chunk.length & 3)]);
			out.writeInt(0);
			
			cout = new ByteArrayOutputStream();
			tables.writeTABW(new DataOutputStream(cout));
			cout.close();
			chunk = cout.toByteArray();
			out.writeInt(chunk.length);
			out.writeInt(TABW);
			out.write(chunk);
			if ((chunk.length & 3) != 0)
				out.write(new byte[4-(chunk.length & 3)]);
			out.writeInt(0);
		}
		
		if (algR != null) {
			cout = new ByteArrayOutputStream();
			algR.write(new DataOutputStream(cout));
			cout.close();
			chunk = cout.toByteArray();
			out.writeInt(chunk.length);
			out.writeInt(ALGR);
			out.write(chunk);
			if ((chunk.length & 3) != 0)
				out.write(new byte[4-(chunk.length & 3)]);
			out.writeInt(0);
		}
		
		if (algS != null) {
			cout = new ByteArrayOutputStream();
			algS.write(new DataOutputStream(cout));
			cout.close();
			chunk = cout.toByteArray();
			out.writeInt(chunk.length);
			out.writeInt(ALGS);
			out.write(chunk);
			if ((chunk.length & 3) != 0)
				out.write(new byte[4-(chunk.length & 3)]);
			out.writeInt(0);
		}
		
		if (algW != null) {
			cout = new ByteArrayOutputStream();
			algW.write(new DataOutputStream(cout));
			cout.close();
			chunk = cout.toByteArray();
			out.writeInt(chunk.length);
			out.writeInt(ALGW);
			out.write(chunk);
			if ((chunk.length & 3) != 0)
				out.write(new byte[4-(chunk.length & 3)]);
			out.writeInt(0);
		}
		
		if (itables != null && !itables.isEmpty()) {
			for (IntLookupTable itable : itables.values()) {
				cout = new ByteArrayOutputStream();
				itable.write(new DataOutputStream(cout));
				cout.close();
				chunk = cout.toByteArray();
				out.writeInt(chunk.length);
				out.writeInt(TAbI);
				out.write(chunk);
				if ((chunk.length & 3) != 0)
					out.write(new byte[4-(chunk.length & 3)]);
				out.writeInt(0);
			}
		}
		
		if (cites != null && !cites.isEmpty()) {
			for (CiteTable ctable : cites) {
				cout = new ByteArrayOutputStream();
				ctable.write(new DataOutputStream(cout));
				cout.close();
				chunk = cout.toByteArray();
				out.writeInt(chunk.length);
				out.writeInt(cIte);
				out.write(chunk);
				if ((chunk.length & 3) != 0)
					out.write(new byte[4-(chunk.length & 3)]);
				out.writeInt(0);
			}
		}
		
		out.writeInt(0);
		out.writeInt(TEND);
		out.writeInt(0);
	}
	
	public void disassemble(PrintWriter out) {
		out.println("id\t" + id);
		if (names != null) {
			for (Name n : names) {
				switch (n.getNameType()) {
				case DISPLAY: out.println("dname\t"+n.toString()); break;
				case INTERNAL: out.println("iname\t"+n.toString()); break;
				default: out.println("name\t"+n.toString()); break;
				}
			}
		}
		if (tables != null) {
			out.println("table");
			for (Map.Entry<ByteSequence, CharacterSequence> e : tables.byteToCharacterMap().entrySet()) {
				out.println("maprbyte\t" + e.getKey().toString() + "\t" + e.getValue().toHexString());
			}
			for (Map.Entry<CharacterSequence, ByteSequence> e : tables.characterToByteMap().entrySet()) {
				out.println("mapwchar\t" + e.getKey().toHexString() + "\t" + e.getValue().toString());
			}
		}
		if (algR != null) {
			out.print("algr\t" + algR.minBytes() + ", " + algR.maxBytes() + ", " + algR.minChars() + ", " + algR.maxChars());
			if (algR.byteSequenceContainsAnotherAsPrefix()) out.print(", bsHasPrefix");
			if (algR.byteSequencesAreNonUnique()) out.print(", bsNonUnique");
			if (algR.characterSequenceContainsAnotherAsPrefix()) out.print(", csHasPrefix");
			if (algR.characterSequencesAreNonUnique()) out.print(", csNonUnique");
			out.println();
			// TODO disassemble algorithms
		}
		if (algS != null) {
			out.print("algr\t" + algS.minBytes() + ", " + algS.maxBytes() + ", " + algS.minChars() + ", " + algS.maxChars());
			if (algS.byteSequenceContainsAnotherAsPrefix()) out.print(", bsHasPrefix");
			if (algS.byteSequencesAreNonUnique()) out.print(", bsNonUnique");
			if (algS.characterSequenceContainsAnotherAsPrefix()) out.print(", csHasPrefix");
			if (algS.characterSequencesAreNonUnique()) out.print(", csNonUnique");
			out.println();
			// TODO disassemble algorithms
		}
		if (algW != null) {
			out.print("algr\t" + algW.minBytes() + ", " + algW.maxBytes() + ", " + algW.minChars() + ", " + algW.maxChars());
			if (algW.byteSequenceContainsAnotherAsPrefix()) out.print(", bsHasPrefix");
			if (algW.byteSequencesAreNonUnique()) out.print(", bsNonUnique");
			if (algW.characterSequenceContainsAnotherAsPrefix()) out.print(", csHasPrefix");
			if (algW.characterSequencesAreNonUnique()) out.print(", csNonUnique");
			out.println();
			// TODO disassemble algorithms
		}
		if (itables != null) {
			for (IntLookupTable itable : itables.values()) {
				out.println("itable\t" + itable.id());
				for (Map.Entry<Integer, Integer> e : itable.map().entrySet()) {
					out.println("imap\t" + e.getKey() + "\t" + e.getValue());
				}
			}
		}
		if (cites != null) {
			for (CiteTable c : cites) {
				out.println("cite");
				for (Cite ci : c) {
					switch (ci.getCiteType()) {
					case AUTHOR: out.println("author\t" + ci.toString()); break;
					case MAJOR_TITLE: out.println("title\t" + ci.toString()); break;
					case MINOR_TITLE: out.println("atitle\t" + ci.toString()); break;
					case PUBLISHER: out.println("pub\t" + ci.toString()); break;
					case DATE: out.println("date\t" + ci.toString()); break;
					case URL: out.println("url\t" + ci.toString()); break;
					case COPYRIGHT: out.println("copyright\t" + ci.toString()); break;
					}
				}
			}
		}
	}
	
	public static TextEncoding read(DataInput in) throws IOException {
		TextEncoding te = null; int teFlags = 0;
		EncodingTable tabl = null, tabr = null, tabw = null;
		if (in.readLong() != MAGIC) throw new IOException("No magic number found");
		while (true) {
			int clen = in.readInt();
			int ctype = in.readInt();
			byte[] chunk = new byte[clen];
			in.readFully(chunk);
			if ((clen & 3) != 0)
				in.readFully(new byte[4-(clen & 3)]);
			in.readInt();
			ByteArrayInputStream cin = new ByteArrayInputStream(chunk);
			DataInputStream cdin = new DataInputStream(cin);
			if (ctype == TENC) {
				if (te != null) throw new IOException("Duplicate TENC chunks");
				te = new TextEncoding();
				if (cdin.readUnsignedByte() != 1) throw new IOException("Invalid version number");
				cdin.readUnsignedByte();
				teFlags = cdin.readUnsignedShort();
				cdin.readUnsignedByte();
				cdin.readUnsignedByte();
				cdin.readUnsignedByte();
				cdin.readUnsignedByte();
				cdin.readUnsignedShort();
				cdin.readUnsignedShort();
				te.id = cdin.readInt();
			} else if (te == null) {
				throw new IOException("No TENC chunk found");
			} else if (ctype == TEND) {
				if (tabl != null) {
					te.tables = tabl;
				} else if (tabr != null && tabw != null) {
					te.tables = new EncodingTable();
					for (Map.Entry<ByteSequence,CharacterSequence> e : tabr.byteToCharacterMap().entrySet()) {
						te.tables.put(e.getKey(), e.getValue(), true, false);
					}
					for (Map.Entry<CharacterSequence,ByteSequence> e : tabw.characterToByteMap().entrySet()) {
						te.tables.put(e.getValue(), e.getKey(), false, true);
					}
				}
				if (te.names == null) te.names = new NameTable();
				if (te.cites == null) te.cites = new ArrayList<CiteTable>();
				return te;
			} else {
				switch (ctype) {
				case NAME:
					if (te.names != null) throw new IOException("Duplicate NAME chunks");
					te.names = NameTable.read(cdin);
					break;
				case TABL:
					if (tabl != null || tabr != null || tabw != null) throw new IOException("Duplicate TABL chunks");
					tabl = EncodingTable.read(cdin, true, true);
					break;
				case TABR:
					if (tabr != null || tabl != null) throw new IOException("Duplicate TABR chunks");
					tabr = EncodingTable.read(cdin, true, false);
					break;
				case TABW:
					if (tabw != null || tabl != null) throw new IOException("Duplicate TABW chunks");
					tabw = EncodingTable.read(cdin, false, true);
					break;
				case ALGR:
					if (te.algR != null) throw new IOException("Duplicate ALGR chunks");
					te.algR = Algorithm.read(cdin, teFlags);
					break;
				case ALGS:
					if (te.algS != null) throw new IOException("Duplicate ALGS chunks");
					te.algS = Algorithm.read(cdin, teFlags);
					break;
				case ALGW:
					if (te.algW != null) throw new IOException("Duplicate ALGW chunks");
					te.algW = Algorithm.read(cdin, teFlags);
					break;
				case TAbI:
					if (te.itables == null) te.itables = new TreeMap<Integer,IntLookupTable>();
					te.addIntLookupTable(IntLookupTable.read(cdin));
					break;
				case cIte:
					if (te.cites == null) te.cites = new ArrayList<CiteTable>();
					te.cites.add(CiteTable.read(cdin));
					break;
				}
			}
		}
	}
	
	public static TextEncoding assemble(Scanner scan) {
		TextEncoding te = new TextEncoding();
		te.names = new NameTable(); te.cites = new ArrayList<CiteTable>();
		List<String[]> algr = null, algs = null, algw = null, lastAlg = null;
		IntLookupTable lastITable = null;
		CiteTable lastCTable = null;
		while (scan.hasNextLine()) {
			String line = scan.nextLine().trim();
			if (line.length() > 0 && !line.startsWith("#")) try {
				String[] fields = line.split("\t+");
				String type = fields[0].trim().toLowerCase();
				if (type.equals("id")) {
					te.id = IntLookupTable.parseInt(fields[1].trim());
				} else if (type.equals("displayname") || type.equals("dispname") || type.equals("dname")) {
					te.names.add(new Name(NameType.DISPLAY, fields[1].trim()));
				} else if (type.equals("internalname") || type.equals("intname") || type.equals("iname")) {
					te.names.add(new Name(NameType.INTERNAL, fields[1].trim()));
				} else if (type.equals("name")) {
					te.names.add(new Name(NameType.BOTH, fields[1].trim()));
				} else if (type.equals("table")) {
					te.tables = new EncodingTable();
				} else if (type.equals("control0") || type.equals("c0")) {
					if (te.tables == null) te.tables = new EncodingTable();
					for (int i = 0x00; i < 0x20; i++) {
						te.tables.put(new ByteSequence((byte)i), new CharacterSequence(i), false, false);
					}
				} else if (type.equals("ascii")) {
					if (te.tables == null) te.tables = new EncodingTable();
					for (int i = 0x20; i < 0x80; i++) {
						te.tables.put(new ByteSequence((byte)i), new CharacterSequence(i), false, false);
					}
				} else if (type.equals("control1") || type.equals("c1")) {
					if (te.tables == null) te.tables = new EncodingTable();
					for (int i = 0x80; i < 0xA0; i++) {
						te.tables.put(new ByteSequence((byte)i), new CharacterSequence(i), false, false);
					}
				} else if (type.equals("latin1")) {
					if (te.tables == null) te.tables = new EncodingTable();
					for (int i = 0xA0; i < 0x100; i++) {
						te.tables.put(new ByteSequence((byte)i), new CharacterSequence(i), false, false);
					}
				} else if (type.equals("map") || type.equals("mapbyte")) {
					if (te.tables == null) te.tables = new EncodingTable();
					te.tables.put(ByteSequence.parseByteSequence(fields[1]), CharacterSequence.parseCharacterSequence(fields[2]), true, true);
				} else if (type.equals("mapchar")) {
					if (te.tables == null) te.tables = new EncodingTable();
					te.tables.put(ByteSequence.parseByteSequence(fields[2]), CharacterSequence.parseCharacterSequence(fields[1]), true, true);
				} else if (type.equals("mapread") || type.equals("mapreadbyte") || type.equals("mapr") || type.equals("maprbyte")) {
					if (te.tables == null) te.tables = new EncodingTable();
					te.tables.put(ByteSequence.parseByteSequence(fields[1]), CharacterSequence.parseCharacterSequence(fields[2]), true, false);
				} else if (type.equals("mapreadchar") || type.equals("maprchar")) {
					if (te.tables == null) te.tables = new EncodingTable();
					te.tables.put(ByteSequence.parseByteSequence(fields[2]), CharacterSequence.parseCharacterSequence(fields[1]), true, false);
				} else if (type.equals("mapwrite") || type.equals("mapwritebyte") || type.equals("mapw") || type.equals("mapwbyte")) {
					if (te.tables == null) te.tables = new EncodingTable();
					te.tables.put(ByteSequence.parseByteSequence(fields[1]), CharacterSequence.parseCharacterSequence(fields[2]), false, true);
				} else if (type.equals("mapwchar") || type.equals("mapwritechar")) {
					if (te.tables == null) te.tables = new EncodingTable();
					te.tables.put(ByteSequence.parseByteSequence(fields[2]), CharacterSequence.parseCharacterSequence(fields[1]), false, true);
				} else if (type.equals("mapalt") || type.equals("mapaltbyte") || type.equals("mapa") || type.equals("mapabyte")) {
					if (te.tables == null) te.tables = new EncodingTable();
					te.tables.put(ByteSequence.parseByteSequence(fields[1]), CharacterSequence.parseCharacterSequence(fields[2]), false, false);
				} else if (type.equals("mapaltchar") || type.equals("mapachar")) {
					if (te.tables == null) te.tables = new EncodingTable();
					te.tables.put(ByteSequence.parseByteSequence(fields[2]), CharacterSequence.parseCharacterSequence(fields[1]), false, false);
				} else if (type.equals("algr")) {
					if (algr == null) {
						algr = new ArrayList<String[]>();
						algr.add(fields);
					}
					lastAlg = algr;
				} else if (type.equals("algs")) {
					if (algs == null) {
						algs = new ArrayList<String[]>();
						algs.add(fields);
					}
					lastAlg = algs;
				} else if (type.equals("algw")) {
					if (algw == null) {
						algw = new ArrayList<String[]>();
						algw.add(fields);
					}
					lastAlg = algw;
				} else if (type.equals("itable")) {
					if (te.itables == null) te.itables = new TreeMap<Integer, IntLookupTable>();
					int id = IntLookupTable.parseInt(fields[1].trim());
					te.itables.put(id, lastITable = new IntLookupTable(id));
				} else if (type.equals("imap")) {
					if (te.itables == null) te.itables = new TreeMap<Integer, IntLookupTable>();
					if (te.itables.isEmpty()) te.itables.put(0, lastITable = new IntLookupTable(0));
					lastITable.put(IntLookupTable.parseInt(fields[1].trim()), IntLookupTable.parseInt(fields[2].trim()));
				} else if (type.equals("cite")) {
					te.cites.add(lastCTable = new CiteTable());
				} else if (type.equals("author")) {
					if (te.cites.isEmpty()) te.cites.add(lastCTable = new CiteTable());
					lastCTable.add(new Cite(CiteType.AUTHOR, fields[1].trim()));
				} else if (type.equals("title")) {
					if (te.cites.isEmpty()) te.cites.add(lastCTable = new CiteTable());
					lastCTable.add(new Cite(CiteType.MAJOR_TITLE, fields[1].trim()));
				} else if (type.equals("articletitle") || type.equals("arttitle") || type.equals("atitle")) {
					if (te.cites.isEmpty()) te.cites.add(lastCTable = new CiteTable());
					lastCTable.add(new Cite(CiteType.MINOR_TITLE, fields[1].trim()));
				} else if (type.equals("publisher") || type.equals("pub")) {
					if (te.cites.isEmpty()) te.cites.add(lastCTable = new CiteTable());
					lastCTable.add(new Cite(CiteType.PUBLISHER, fields[1].trim()));
				} else if (type.equals("date")) {
					if (te.cites.isEmpty()) te.cites.add(lastCTable = new CiteTable());
					lastCTable.add(new Cite(CiteType.DATE, fields[1].trim()));
				} else if (type.equals("url")) {
					if (te.cites.isEmpty()) te.cites.add(lastCTable = new CiteTable());
					lastCTable.add(new Cite(CiteType.URL, fields[1].trim()));
				} else if (type.equals("copyright") || type.equals("copy")) {
					if (te.cites.isEmpty()) te.cites.add(lastCTable = new CiteTable());
					lastCTable.add(new Cite(CiteType.COPYRIGHT, fields[1].trim()));
				} else {
					if (lastAlg != null) lastAlg.add(fields);
				}
			} catch (Exception e) { System.err.println(e.getMessage()); }
		}
		// TODO assemble algorithms
		return te;
	}
}
