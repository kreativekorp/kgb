package com.kreative.kte;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;

public class TEncDumper {
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
	private static final int tIME = 0x74494D45;
	private static final int tExt = 0x74457874;
	private static final int iTxt = 0x69547874;
	private static final int TEND = 0x54454E44;
	
	public static void main(String[] args) {
		PrintWriter out;
		try {
			out = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"), true);
		} catch (Exception e) {
			out = new PrintWriter(new OutputStreamWriter(System.out), true);
		}
		for (String arg : args) {
			out.println(arg);
			try {
				DataInputStream in = new DataInputStream(new FileInputStream(new File(arg)));
				if (in.readLong() != MAGIC) { in.close(); throw new IOException("No magic number found"); }
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
					out.println("\t" + (char)((ctype >> 24) & 0xFF) + (char)((ctype >> 16) & 0xFF) + (char)((ctype >> 8) & 0xFF) + (char)(ctype & 0xFF));
					if (ctype == TENC) {
						int version = cdin.readUnsignedByte();
						int encodingType = cdin.readUnsignedByte();
						int flags = cdin.readUnsignedShort();
						int minb = cdin.readUnsignedByte();
						int maxb = cdin.readUnsignedByte();
						int minc = cdin.readUnsignedByte();
						int maxc = cdin.readUnsignedByte();
						int maxr = cdin.readUnsignedShort();
						int maxs = cdin.readUnsignedShort();
						int id = cdin.readInt();
						out.println("\t\tVersion: " + version);
						out.print("\t\tEncoding Type: " + encodingType);
						switch (encodingType) {
						case 1: out.println(" - Round-Trip (TABL)"); break;
						case 2: out.println(" - Two-Way-Trip (TABR/TABW)"); break;
						case 3: out.println(" - Algorithmic (ALGR/ALGS/ALGW)"); break;
						case 4: out.println(" - Algorithmic+Table (ALGR/ALGS/ALGW/TAbI)"); break;
						default: out.println(); break;
						}
						out.print("\t\tFlags: " + Integer.toHexString(flags));
						boolean flagsFirst = true;
						if ((flags & 0x01) != 0) { if (flagsFirst) { out.print(" - bsHasPfx"); flagsFirst = false; } else out.print(", bsHasPfx"); }
						if ((flags & 0x02) != 0) { if (flagsFirst) { out.print(" - bsNonUnq"); flagsFirst = false; } else out.print(", bsNonUnq"); }
						if ((flags & 0x04) != 0) { if (flagsFirst) { out.print(" - csHasPfx"); flagsFirst = false; } else out.print(", csHasPfx"); }
						if ((flags & 0x08) != 0) { if (flagsFirst) { out.print(" - csNonUnq"); flagsFirst = false; } else out.print(", csNonUnq"); }
						if ((flags & 0x10) != 0) { if (flagsFirst) { out.print(" - bsSorted"); flagsFirst = false; } else out.print(", bsSorted"); }
						if ((flags & 0x20) != 0) { if (flagsFirst) { out.print(" - csSorted"); flagsFirst = false; } else out.print(", csSorted"); }
						if ((flags & 0x8000) != 0) { if (flagsFirst) { out.print(" - critical"); flagsFirst = false; } else out.print(", critical"); }
						out.println();
						out.println("\t\tShortest Byte Sequence: " + minb);
						out.println("\t\tLongest Byte Sequence: " + maxb);
						out.println("\t\tShortest Character Sequence: " + minc);
						out.println("\t\tLongest Character Sequence: " + maxc);
						out.println("\t\tRegisters Required: " + maxr);
						out.println("\t\tStack Space Required: " + maxs);
						out.println("\t\tID: " + id);
					} else if (ctype == TEND) {
						break;
					} else {
						int count; int type; int len; byte[] stra; String str;
						int kfmt; int vfmt; int flags; boolean flagsFirst; int id;
						int minb; int maxb; int minc; int maxc; int maxr; int maxs;
						switch (ctype) {
						case NAME:
							count = cdin.readUnsignedByte();
							out.println("\t\tNumber of Names: " + count);
							for (int i = 0; i < count; i++) {
								type = cdin.readUnsignedByte();
								len = cdin.readUnsignedByte();
								stra = new byte[len]; cdin.readFully(stra);
								str = new String(stra);
								out.print("\t\t\tType: " + type);
								switch (type) {
								case 1: out.println(" - General Purpose"); break;
								case 2: out.println(" - Display Only"); break;
								case 3: out.println(" - Internal Only"); break;
								default: out.println(); break;
								}
								out.println("\t\t\tName: " + str);
							}
							break;
						case TABL:
						case TABR:
						case TABW:
							kfmt = cdin.readUnsignedByte();
							vfmt = cdin.readUnsignedByte();
							flags = cdin.readUnsignedShort();
							minb = cdin.readUnsignedByte();
							maxb = cdin.readUnsignedByte();
							minc = cdin.readUnsignedByte();
							maxc = cdin.readUnsignedByte();
							count = cdin.readInt();
							out.print("\t\tByte Sequence Format: " + kfmt);
							switch (kfmt) {
							case 1: out.println(" - indexed"); break;
							case 2: out.println(" - byte[1]"); break;
							case 3: out.println(" - byte[1..2]"); break;
							case 4: out.println(" - byte[2]"); break;
							case 5: out.println(" - byte[1..4]"); break;
							case 6: out.println(" - byte[4]"); break;
							case 16: out.println(" - byte[n]"); break;
							case 17: out.println(" - byte[n*2]"); break;
							case 18: out.println(" - byte[n*4]"); break;
							default: out.println(); break;
							}
							out.print("\t\tCharacter Sequence Format: " + vfmt);
							switch (vfmt) {
							case 1: out.println(" - indexed"); break;
							case 2: out.println(" - uint8[1]"); break;
							case 3: out.println(" - uint8[1..2]"); break;
							case 4: out.println(" - uint16[1]"); break;
							case 5: out.println(" - uint16[1..2]"); break;
							case 6: out.println(" - uint32"); break;
							case 16: out.println(" - uint8[n]"); break;
							case 17: out.println(" - uint16[n]"); break;
							case 18: out.println(" - uint32[n]"); break;
							default: out.println(); break;
							}
							out.print("\t\tFlags: " + Integer.toHexString(flags));
							flagsFirst = true;
							if ((flags & 0x01) != 0) { if (flagsFirst) { out.print(" - bsHasPfx"); flagsFirst = false; } else out.print(", bsHasPfx"); }
							if ((flags & 0x02) != 0) { if (flagsFirst) { out.print(" - bsNonUnq"); flagsFirst = false; } else out.print(", bsNonUnq"); }
							if ((flags & 0x04) != 0) { if (flagsFirst) { out.print(" - csHasPfx"); flagsFirst = false; } else out.print(", csHasPfx"); }
							if ((flags & 0x08) != 0) { if (flagsFirst) { out.print(" - csNonUnq"); flagsFirst = false; } else out.print(", csNonUnq"); }
							if ((flags & 0x10) != 0) { if (flagsFirst) { out.print(" - bsSorted"); flagsFirst = false; } else out.print(", bsSorted"); }
							if ((flags & 0x20) != 0) { if (flagsFirst) { out.print(" - csSorted"); flagsFirst = false; } else out.print(", csSorted"); }
							out.println();
							out.println("\t\tShortest Byte Sequence: " + minb);
							out.println("\t\tLongest Byte Sequence: " + maxb);
							out.println("\t\tShortest Character Sequence: " + minc);
							out.println("\t\tLongest Character Sequence: " + maxc);
							out.println("\t\tNumber of Entries: " + count);
							for (int i = 0; i < count; i++) {
								ByteSequence bs = ByteSequence.read(cdin, i, count, kfmt);
								CharacterSequence cs = CharacterSequence.read(cdin, i, count, vfmt);
								switch (ctype) {
								case TABR: out.println("\t\t\t" + bs.toString() + " -> " + cs.toString().replaceAll("[\\x00-\\x1F\\x7F-\\x9F]", " ")); break;
								case TABW: out.println("\t\t\t" + cs.toString().replaceAll("[\\x00-\\x1F\\x7F-\\x9F]", " ") + " -> " + bs.toString()); break;
								case TABL: out.println("\t\t\t" + bs.toString() + " <-> " + cs.toString().replaceAll("[\\x00-\\x1F\\x7F-\\x9F]", " ")); break;
								}
							}
							break;
						case ALGR:
						case ALGS:
						case ALGW:
							minb = cdin.readUnsignedByte();
							maxb = cdin.readUnsignedByte();
							minc = cdin.readUnsignedByte();
							maxc = cdin.readUnsignedByte();
							maxr = cdin.readUnsignedShort();
							maxs = cdin.readUnsignedShort();
							count = cdin.readInt();
							out.println("\t\tShortest Byte Sequence: " + minb);
							out.println("\t\tLongest Byte Sequence: " + maxb);
							out.println("\t\tShortest Character Sequence: " + minc);
							out.println("\t\tLongest Character Sequence: " + maxc);
							out.println("\t\tRegisters Required: " + maxr);
							out.println("\t\tStack Space Required: " + maxs);
							out.println("\t\tNumber of Instructions: " + count);
							// TODO disassemble algorithms
							for (int i = 0; i < count; i++) {
								out.println("\t\t\t" + Integer.toHexString(cdin.readInt()));
							}
							break;
						case TAbI:
							kfmt = cdin.readUnsignedByte();
							vfmt = cdin.readUnsignedByte();
							flags = cdin.readUnsignedShort();
							id = cdin.readInt();
							count = cdin.readInt();
							out.print("\t\tKey Format: " + kfmt);
							switch (kfmt) {
							case 1: out.println(" - indexed"); break;
							case 2: out.println(" - sint8"); break;
							case 3: out.println(" - uint8"); break;
							case 4: out.println(" - sint16"); break;
							case 5: out.println(" - uint16"); break;
							case 6: out.println(" - sint32"); break;
							case 7: out.println(" - uint32"); break;
							default: out.println(); break;
							}
							out.print("\t\tValue Format: " + vfmt);
							switch (vfmt) {
							case 1: out.println(" - indexed"); break;
							case 2: out.println(" - sint8"); break;
							case 3: out.println(" - uint8"); break;
							case 4: out.println(" - sint16"); break;
							case 5: out.println(" - uint16"); break;
							case 6: out.println(" - sint32"); break;
							case 7: out.println(" - uint32"); break;
							default: out.println(); break;
							}
							out.print("\t\tFlags: " + Integer.toHexString(flags));
							flagsFirst = true;
							if ((flags & 0x01) != 0) { if (flagsFirst) { out.print(" - kHasPfx"); flagsFirst = false; } else out.print(", kHasPfx"); }
							if ((flags & 0x02) != 0) { if (flagsFirst) { out.print(" - kNonUnq"); flagsFirst = false; } else out.print(", kNonUnq"); }
							if ((flags & 0x04) != 0) { if (flagsFirst) { out.print(" - vHasPfx"); flagsFirst = false; } else out.print(", vHasPfx"); }
							if ((flags & 0x08) != 0) { if (flagsFirst) { out.print(" - vNonUnq"); flagsFirst = false; } else out.print(", vNonUnq"); }
							if ((flags & 0x10) != 0) { if (flagsFirst) { out.print(" - kSorted"); flagsFirst = false; } else out.print(", kSorted"); }
							if ((flags & 0x20) != 0) { if (flagsFirst) { out.print(" - vSorted"); flagsFirst = false; } else out.print(", vSorted"); }
							out.println();
							out.println("\t\tID: " + id);
							out.println("\t\tNumber of Entries: " + count);
							for (int i = 0; i < count; i++) {
								int k = 0, v = 0;
								switch (kfmt) {
								case 1: k = i; break;
								case 2: k = cdin.readByte(); break;
								case 3: k = cdin.readUnsignedByte(); break;
								case 4: k = cdin.readShort(); break;
								case 5: k = cdin.readUnsignedShort(); break;
								case 6: k = cdin.readInt(); break;
								case 7: k = cdin.readInt(); break;
								}
								switch (vfmt) {
								case 1: v = i; break;
								case 2: v = cdin.readByte(); break;
								case 3: v = cdin.readUnsignedByte(); break;
								case 4: v = cdin.readShort(); break;
								case 5: v = cdin.readUnsignedShort(); break;
								case 6: v = cdin.readInt(); break;
								case 7: v = cdin.readInt(); break;
								}
								out.println("\t\t\t" + k + " -> " + v);
							}
							break;
						case cIte:
							count = cdin.readUnsignedByte();
							out.println("\t\tNumber of Fields: " + count);
							for (int i = 0; i < count; i++) {
								type = cdin.readUnsignedByte();
								len = cdin.readUnsignedByte();
								stra = new byte[len]; cdin.readFully(stra);
								str = new String(stra);
								out.print("\t\t\tType: " + type);
								switch (type) {
								case 1: out.println(" - Author"); break;
								case 2: out.println(" - Major Title"); break;
								case 3: out.println(" - Minor Title"); break;
								case 4: out.println(" - Publisher"); break;
								case 5: out.println(" - Date"); break;
								case 6: out.println(" - URL"); break;
								case 7: out.println(" - Copyright"); break;
								default: out.println(); break;
								}
								out.println("\t\t\tValue: " + str);
							}
							break;
						case tIME:
							Date created = new Date(cdin.readLong());
							Date modified = new Date(cdin.readLong());
							DateFormat df = DateFormat.getDateTimeInstance();
							out.println("\t\tDate Created: " + df.format(created));
							out.println("\t\tDate Last Modified: " + df.format(modified));
							break;
						case tExt:
							len = cdin.readInt();
							stra = new byte[len]; cdin.readFully(stra);
							str = new String(stra, "ISO-8859-1");
							out.println("\t\tKeyword: " + str);
							len = cdin.readInt();
							stra = new byte[len]; cdin.readFully(stra);
							str = new String(stra, "ISO-8859-1");
							out.println("\t\tContent: " + str);
							break;
						case iTxt:
							len = cdin.readInt();
							stra = new byte[len]; cdin.readFully(stra);
							str = new String(stra, "UTF-8");
							out.println("\t\tKeyword: " + str);
							len = cdin.readInt();
							stra = new byte[len]; cdin.readFully(stra);
							str = new String(stra, "UTF-8");
							out.println("\t\tContent: " + str);
							break;
						default:
							out.println("\t\t<<unrecognized>>");
							break;
						}
					}
				}
				in.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
	}
}
