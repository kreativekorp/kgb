package com.kreative.recode;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import com.kreative.recode.map.CharacterSequenceMapLibrary;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationLibrary;
import com.kreative.recode.transformations.SequenceRemapTransformationFactory;

public class RecodeArgumentParser {
	public static enum ArgumentType {
		PLAIN, // A flag or file name.
		TRANSFORMATION_TO_LOAD, // The class name of a TextTransformationFactory to add to the library.
		MAP_TO_LOAD, // The file path of a CharacterSequenceMap or directory of CharacterSequenceMaps to add to the library.
		INPUT_ENCODING, // The name of the text encoding used for input.
		OUTPUT_ENCODING, // The name of the text encoding used for output.
		BOTH_ENCODING, // The name of the text encoding used for both input and output.
		TRANSFORMATION_NAME, // The name of a TextTransformation to apply to the text.
		TRANSFORMATION_ARG, // An argument passed to a TextTransformation.
		INPUT_MAP_NAME, // The name of a CharacterSequenceMap used on input.
		OUTPUT_MAP_NAME, // The name of a CharacterSequenceMap used on output.
		INPUT_FILE, // The file path of a text file to read.
		OUTPUT_FILE, // The file path of the recoded file.
		OUTPUT_DIRECTORY, // The default directory in which to put the recoded files.
	}
	
	public static enum Mode {
		AUTO, // Start the GUI if no other arguments are specified.
		INFO, // Exit if no other arguments are specified.
		STDIO, // Recode standard input to standard output.
		FILES, // Recode files.
		CLIPBOARD, // Recode clipboard.
		GUI, // Start the GUI after processing arguments.
		ERROR, // Exit after processing arguments.
	}
	
	private ArgumentType argType;
	private Mode mode;
	private TextTransformationLibrary txLibrary;
	private SequenceRemapTransformationFactory remap;
	private CharacterSequenceMapLibrary mapLibrary;
	private Charset inputEncoding;
	private Charset outputEncoding;
	private TextTransformationFactory txFactory;
	private List<String> txArgs;
	private List<TextTransformationFactory> txFactoryList;
	private List<List<String>> txArgsList;
	private File outputDirectory;
	private File outputFile;
	private List<File> inputFiles;
	private List<File> outputFiles;
	private boolean stackTrace;
	private boolean debugArgs;
	
	public RecodeArgumentParser() {
		argType = ArgumentType.PLAIN;
		mode = Mode.AUTO;
		txLibrary = new TextTransformationLibrary().loadInternal();
		remap = (SequenceRemapTransformationFactory)txLibrary.getByName("Remap");
		mapLibrary = remap.getMapLibrary();
		inputEncoding = Charset.forName("UTF-8");
		outputEncoding = Charset.forName("UTF-8");
		txFactory = null;
		txArgs = new ArrayList<String>();
		txFactoryList = new ArrayList<TextTransformationFactory>();
		txArgsList = new ArrayList<List<String>>();
		outputDirectory = null;
		outputFile = null;
		inputFiles = new ArrayList<File>();
		outputFiles = new ArrayList<File>();
		stackTrace = false;
		debugArgs = false;
	}
	
	public void parseArgument(String arg) {
		switch (argType) {
		case PLAIN: // A flag or file name.
			if (arg.startsWith("-")) {
				if (arg.equalsIgnoreCase("-v") || arg.equalsIgnoreCase("--v") || arg.equalsIgnoreCase("-version") || arg.equalsIgnoreCase("--version")) {
					printVersion();
					if (mode == Mode.AUTO) mode = Mode.INFO;
				} else if (arg.equalsIgnoreCase("-h") || arg.equalsIgnoreCase("--h") || arg.equalsIgnoreCase("-help") || arg.equalsIgnoreCase("--help")) {
					printHelp();
					if (mode == Mode.AUTO) mode = Mode.INFO;
				} else if (arg.equalsIgnoreCase("-le") || arg.equalsIgnoreCase("--le") || arg.equalsIgnoreCase("-listenc") || arg.equalsIgnoreCase("--listenc") || arg.equalsIgnoreCase("-listencodings") || arg.equalsIgnoreCase("--listencodings")) {
					printEncodings();
					if (mode == Mode.AUTO) mode = Mode.INFO;
				} else if (arg.equalsIgnoreCase("-lt") || arg.equalsIgnoreCase("--lt") || arg.equalsIgnoreCase("-listtx") || arg.equalsIgnoreCase("--listtx") || arg.equalsIgnoreCase("-listtransformations") || arg.equalsIgnoreCase("--listtransformations")) {
					printTransformations();
					if (mode == Mode.AUTO) mode = Mode.INFO;
				} else if (arg.equalsIgnoreCase("-lm") || arg.equalsIgnoreCase("--lm") || arg.equalsIgnoreCase("-listmap") || arg.equalsIgnoreCase("--listmap") || arg.equalsIgnoreCase("-listmaps") || arg.equalsIgnoreCase("--listmaps")) {
					printMaps();
					if (mode == Mode.AUTO) mode = Mode.INFO;
				} else if (arg.equalsIgnoreCase("-f") || arg.equalsIgnoreCase("--f") || arg.equalsIgnoreCase("-files") || arg.equalsIgnoreCase("--files")) {
					if (mode != Mode.ERROR) mode = Mode.FILES;
				} else if (arg.equalsIgnoreCase("-s") || arg.equalsIgnoreCase("--s") || arg.equalsIgnoreCase("-stdio") || arg.equalsIgnoreCase("--stdio")) {
					if (mode != Mode.ERROR) mode = Mode.STDIO;
				} else if (arg.equalsIgnoreCase("-c") || arg.equalsIgnoreCase("--c") || arg.equalsIgnoreCase("-cb") || arg.equalsIgnoreCase("--cb") || arg.equalsIgnoreCase("-clipboard") || arg.equalsIgnoreCase("--clipboard")) {
					if (mode != Mode.ERROR) mode = Mode.CLIPBOARD;
				} else if (arg.equalsIgnoreCase("-g") || arg.equalsIgnoreCase("--g") || arg.equalsIgnoreCase("-gui") || arg.equalsIgnoreCase("--gui")) {
					if (mode != Mode.ERROR) mode = Mode.GUI;
				} else if (arg.equalsIgnoreCase("-txc") || arg.equalsIgnoreCase("--txc") || arg.equalsIgnoreCase("-txclass") || arg.equalsIgnoreCase("--txclass") || arg.equalsIgnoreCase("-transformclass") || arg.equalsIgnoreCase("--transformclass")) {
					argType = ArgumentType.TRANSFORMATION_TO_LOAD;
				} else if (arg.equalsIgnoreCase("-m") || arg.equalsIgnoreCase("--m") || arg.equalsIgnoreCase("-md") || arg.equalsIgnoreCase("--md") || arg.equalsIgnoreCase("-mapdir") || arg.equalsIgnoreCase("--mapdir") || arg.equalsIgnoreCase("-mapdirectory") || arg.equalsIgnoreCase("--mapdirectory")) {
					argType = ArgumentType.MAP_TO_LOAD;
				} else if (arg.equalsIgnoreCase("-ie") || arg.equalsIgnoreCase("--ie") || arg.equalsIgnoreCase("-inenc") || arg.equalsIgnoreCase("--inenc") || arg.equalsIgnoreCase("-inputencoding") || arg.equalsIgnoreCase("--inputencoding")) {
					argType = ArgumentType.INPUT_ENCODING;
				} else if (arg.equalsIgnoreCase("-oe") || arg.equalsIgnoreCase("--oe") || arg.equalsIgnoreCase("-outenc") || arg.equalsIgnoreCase("--outenc") || arg.equalsIgnoreCase("-outputencoding") || arg.equalsIgnoreCase("--outputencoding")) {
					argType = ArgumentType.OUTPUT_ENCODING;
				} else if (arg.equalsIgnoreCase("-e") || arg.equalsIgnoreCase("--e") || arg.equalsIgnoreCase("-enc") || arg.equalsIgnoreCase("--enc") || arg.equalsIgnoreCase("-encoding") || arg.equalsIgnoreCase("--encoding")) {
					argType = ArgumentType.BOTH_ENCODING;
				} else if (arg.equalsIgnoreCase("-t") || arg.equalsIgnoreCase("--t") || arg.equalsIgnoreCase("-tx") || arg.equalsIgnoreCase("--tx") || arg.equalsIgnoreCase("-transform") || arg.equalsIgnoreCase("--transform") || arg.equalsIgnoreCase("-transformation") || arg.equalsIgnoreCase("--transformation")) {
					argType = ArgumentType.TRANSFORMATION_NAME;
				} else if (arg.equalsIgnoreCase("-im") || arg.equalsIgnoreCase("--im") || arg.equalsIgnoreCase("-inmap") || arg.equalsIgnoreCase("--inmap") || arg.equalsIgnoreCase("-inputmap") || arg.equalsIgnoreCase("--inputmap")) {
					argType = ArgumentType.INPUT_MAP_NAME;
				} else if (arg.equalsIgnoreCase("-om") || arg.equalsIgnoreCase("--om") || arg.equalsIgnoreCase("-outmap") || arg.equalsIgnoreCase("--outmap") || arg.equalsIgnoreCase("-outputmap") || arg.equalsIgnoreCase("--outputmap")) {
					argType = ArgumentType.OUTPUT_MAP_NAME;
				} else if (arg.equalsIgnoreCase("-od") || arg.equalsIgnoreCase("--od") || arg.equalsIgnoreCase("-outdir") || arg.equalsIgnoreCase("--outdir") || arg.equalsIgnoreCase("-outputdirectory") || arg.equalsIgnoreCase("--outputdirectory")) {
					argType = ArgumentType.OUTPUT_DIRECTORY;
				} else if (arg.equalsIgnoreCase("-o") || arg.equalsIgnoreCase("--o") || arg.equalsIgnoreCase("-of") || arg.equalsIgnoreCase("--of") || arg.equalsIgnoreCase("-outfile") || arg.equalsIgnoreCase("--outfile") || arg.equalsIgnoreCase("-outputfile") || arg.equalsIgnoreCase("--outputfile")) {
					argType = ArgumentType.OUTPUT_FILE;
				} else if (arg.equalsIgnoreCase("-i") || arg.equalsIgnoreCase("--i") || arg.equalsIgnoreCase("-if") || arg.equalsIgnoreCase("--if") || arg.equalsIgnoreCase("-infile") || arg.equalsIgnoreCase("--infile") || arg.equalsIgnoreCase("-inputfile") || arg.equalsIgnoreCase("--inputfile")) {
					argType = ArgumentType.INPUT_FILE;
				} else if (arg.equalsIgnoreCase("-st") || arg.equalsIgnoreCase("--st") || arg.equalsIgnoreCase("-trace") || arg.equalsIgnoreCase("--trace") || arg.equalsIgnoreCase("-stacktrace") || arg.equalsIgnoreCase("--stacktrace")) {
					stackTrace = true;
				} else if (arg.equalsIgnoreCase("-debugargs") || arg.equalsIgnoreCase("--debugargs")) {
					debugArgs = true;
				} else {
					txFactory = txLibrary.getByFlag(arg);
					if (txFactory != null) {
						checkTxArgsEnd();
					} else {
						System.err.println("Error: Unrecognized option \"" + arg + "\".");
						mode = Mode.ERROR;
						argType = ArgumentType.PLAIN;
					}
				}
			} else {
				addFile(new File(arg));
				if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
				argType = ArgumentType.PLAIN;
			}
			break;
		case TRANSFORMATION_TO_LOAD: // The class name of a TextTransformationFactory to add to the library.
			try {
				Class<?> genClass = Class.forName(arg);
				Class<? extends TextTransformationFactory> txClass = genClass.asSubclass(TextTransformationFactory.class);
				TextTransformationFactory txInstance = txClass.newInstance();
				txLibrary.add(txInstance);
				if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			} catch (Exception e) {
				System.err.println("Error: Unknown transformation factory class \"" + arg + "\".");
				if (stackTrace) e.printStackTrace();
				mode = Mode.ERROR;
			}
			argType = ArgumentType.PLAIN;
			break;
		case MAP_TO_LOAD: // The file path of a CharacterSequenceMap or directory of CharacterSequenceMaps to add to the library.
			try {
				mapLibrary.load(new File(arg));
				if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			} catch (IOException e) {
				System.err.println("Error: Couldn't read character maps from \"" + arg + "\".");
				if (stackTrace) e.printStackTrace();
				mode = Mode.ERROR;
			}
			argType = ArgumentType.PLAIN;
			break;
		case INPUT_ENCODING: // The name of the text encoding used for input.
			try {
				inputEncoding = Charset.forName(arg);
				if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			} catch (UnsupportedCharsetException e) {
				System.err.println("Error: Unknown text encoding \"" + arg + "\".");
				if (stackTrace) e.printStackTrace();
				mode = Mode.ERROR;
			}
			argType = ArgumentType.PLAIN;
			break;
		case OUTPUT_ENCODING: // The name of the text encoding used for output.
			try {
				outputEncoding = Charset.forName(arg);
				if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			} catch (UnsupportedCharsetException e) {
				System.err.println("Error: Unknown text encoding \"" + arg + "\".");
				if (stackTrace) e.printStackTrace();
				mode = Mode.ERROR;
			}
			argType = ArgumentType.PLAIN;
			break;
		case BOTH_ENCODING: // The name of the text encoding used for both input and output.
			try {
				inputEncoding = outputEncoding = Charset.forName(arg);
				if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			} catch (UnsupportedCharsetException e) {
				System.err.println("Error: Unknown text encoding \"" + arg + "\".");
				if (stackTrace) e.printStackTrace();
				mode = Mode.ERROR;
			}
			argType = ArgumentType.PLAIN;
			break;
		case TRANSFORMATION_NAME: // The name of a TextTransformation to apply to the text.
			txFactory = txLibrary.getByName(arg);
			if (txFactory != null) {
				checkTxArgsEnd();
			} else {
				System.err.println("Error: Unknown text transformation \"" + arg + "\".");
				mode = Mode.ERROR;
				argType = ArgumentType.PLAIN;
			}
			break;
		case TRANSFORMATION_ARG: // An argument passed to a TextTransformation.
			txArgs.add(arg);
			checkTxArgsEnd();
			break;
		case INPUT_MAP_NAME: // The name of a CharacterSequenceMap used on input.
			TextTransformationFactory intxf;
			if (txFactoryList.isEmpty()) intxf = null;
			else intxf = txFactoryList.get(txFactoryList.size() - 1);
			if (intxf instanceof SequenceRemapTransformationFactory) {
				List<String> txArgs = txArgsList.get(txArgsList.size() - 1);
				if (txArgs.get(0).length() == 0) txArgs.set(0, arg);
				else txArgs.set(0, txArgs.get(0) + "," + arg);
			} else {
				List<String> txArgs = new ArrayList<String>();
				txArgs.add(arg);
				txArgs.add("");
				txFactoryList.add(remap);
				txArgsList.add(txArgs);
			}
			if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			argType = ArgumentType.PLAIN;
			break;
		case OUTPUT_MAP_NAME: // The name of a CharacterSequenceMap used on output.
			TextTransformationFactory outtxf;
			if (txFactoryList.isEmpty()) outtxf = null;
			else outtxf = txFactoryList.get(txFactoryList.size() - 1);
			if (outtxf instanceof SequenceRemapTransformationFactory) {
				List<String> txArgs = txArgsList.get(txArgsList.size() - 1);
				if (txArgs.get(1).length() == 0) txArgs.set(1, arg);
				else txArgs.set(1, txArgs.get(1) + "," + arg);
			} else {
				List<String> txArgs = new ArrayList<String>();
				txArgs.add("");
				txArgs.add(arg);
				txFactoryList.add(remap);
				txArgsList.add(txArgs);
			}
			if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			argType = ArgumentType.PLAIN;
			break;
		case INPUT_FILE: // The file path of a text file to read.
			addFile(new File(arg));
			if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			argType = ArgumentType.PLAIN;
			break;
		case OUTPUT_FILE: // The file path of the recoded file.
			outputFile = new File(arg);
			if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			argType = ArgumentType.PLAIN;
			break;
		case OUTPUT_DIRECTORY: // The default directory in which to put the recoded files.
			outputDirectory = new File(arg);
			if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			argType = ArgumentType.PLAIN;
			break;
		}
	}
	
	public void finishParsing() {
		if (argType != ArgumentType.PLAIN) {
			System.err.println("Error: Missing argument for " + argType + ".");
			mode = Mode.ERROR;
			argType = ArgumentType.PLAIN;
		}
		if (debugArgs) {
			printDebugArgs();
			mode = Mode.INFO;
		}
	}
	
	public Mode getMode() {
		return mode;
	}
	
	public TextTransformationLibrary getTransformationLibrary() {
		return txLibrary;
	}
	
	public Charset getInputEncoding() {
		return inputEncoding;
	}
	
	public Charset getOutputEncoding() {
		return outputEncoding;
	}
	
	public List<TextTransformationFactory> getTransformationFactories() {
		return Collections.unmodifiableList(txFactoryList);
	}
	
	public List<List<String>> getTransformationArguments() {
		return Collections.unmodifiableList(txArgsList);
	}
	
	public List<TextTransformation> getTransformations() {
		List<TextTransformation> txs = new ArrayList<TextTransformation>();
		Iterator<TextTransformationFactory> fi = txFactoryList.iterator();
		Iterator<List<String>> ai = txArgsList.iterator();
		while (fi.hasNext() && ai.hasNext()) {
			TextTransformationFactory f = fi.next();
			List<String> a = ai.next();
			try {
				txs.add(f.createTransformation(a));
			} catch (IllegalArgumentException e) {
				System.err.println(e.getMessage());
				if (stackTrace) e.printStackTrace();
				mode = Mode.ERROR;
			}
		}
		return txs;
	}
	
	public List<File> getInputFiles() {
		return Collections.unmodifiableList(inputFiles);
	}
	
	public List<File> getOutputFiles() {
		return Collections.unmodifiableList(outputFiles);
	}
	
	public File getOutputDirectory() {
		return outputDirectory;
	}
	
	public boolean stackTrace() {
		return stackTrace;
	}
	
	private void printVersion() {
		System.out.println("Kreative Recode 1.3");
		System.out.println("(c) 2012-2013 Kreative Software");
	}
	
	private void printHelp() {
		List<HelpItem> items = new ArrayList<HelpItem>();
		items.add(new HelpItem(
			new String[] { "-f", "--f", "-files", "--files" },
			new String[0],
			"The files listed on the command line will be recoded and/or transformed." +
			" This is the default when options or file paths are specified."
		));
		items.add(new HelpItem(
			new String[] { "-s", "--s", "-stdio", "--stdio" },
			new String[0],
			"Standard input will be recoded and/or transformed, then written to standard output."
		));
		items.add(new HelpItem(
			new String[] { "-c", "--c", "-cb", "--cb", "-clipboard", "--clipboard" },
			new String[0],
			"The contents of the system clipboard will be transformed."
		));
		items.add(new HelpItem(
			new String[] { "-g", "--g", "-gui", "--gui" },
			new String[0],
			"Launch the graphical user interface. This is the default" +
			" when no options or file paths are specified."
		));
		items.add(new HelpItem(
			new String[] { "-i", "--i", "-if", "--if", "-infile", "--infile", "-inputfile", "--inputfile" },
			new String[] { "path" },
			"Specifies a source file that is to be recoded."
		));
		items.add(new HelpItem(
			new String[] { "-o", "--o", "-of", "--of", "-outfile", "--outfile", "-outputfile", "--outputfile" },
			new String[] { "path" },
			"The recoded version of the next source file specified will be written" +
			" to the specified path. This option overrides any -od option in effect."
		));
		items.add(new HelpItem(
			new String[] { "-od", "--od", "-outdir", "--outdir", "-outputdirectory", "--outputdirectory" },
			new String[] { "path" },
			"Recoded versions of all source files will be written to the specified" +
			" directory unless the -o option is specified for a given file."
		));
		items.add(new HelpItem(
			new String[] { "-ie", "--ie", "-inenc", "--inenc", "-inputencoding", "--inputencoding" },
			new String[] { "encoding" },
			"Specifies the text encoding of the source files or standard input."
		));
		items.add(new HelpItem(
			new String[] { "-oe", "--oe", "-outenc", "--outenc", "-outputencoding", "--outputencoding" },
			new String[] { "encoding" },
			"Specifies the text encoding of the destination files or standard output."
		));
		items.add(new HelpItem(
			new String[] { "-e", "--e", "-enc", "--enc", "-encoding", "--encoding" },
			new String[] { "encoding" },
			"Specifies the text encoding of both input and output."
		));
		items.add(new HelpItem(
			new String[] { "-txc", "--txc", "-txclass", "--txclass", "-transformclass", "--transformclass" },
			new String[] { "classname" },
			"Loads an additional text transformation factory."
		));
		items.add(new HelpItem(
			new String[] { "-t", "--t", "-tx", "--tx", "-transform", "--transform", "-transformation", "--transformation" },
			new String[] { "name", "args" },
			"Applies the specified text transformation."
		));
		items.add(new HelpItem(
			new String[] { "-m", "--m", "-md", "--md", "-mapdir", "--mapdir", "-mapdirectory", "--mapdirectory" },
			new String[] { "path" },
			"Loads external mapping files from the specified directory."
		));
		items.add(new HelpItem(
			new String[] { "-im", "--im", "-inmap", "--inmap", "-inputmap", "--inputmap" },
			new String[] { "name" },
			"Adds an input mapping to apply to the text."
		));
		items.add(new HelpItem(
			new String[] { "-om", "--om", "-outmap", "--outmap", "-outputmap", "--outputmap" },
			new String[] { "name" },
			"Adds an output mapping to apply to the text."
		));
		for (String flag : txLibrary.listByFlag()) {
			TextTransformationFactory tx = txLibrary.getByFlag(flag);
			items.add(new HelpItem(tx.getFlags(), tx.getArgumentNames(), tx.getDescription()));
		}
		items.add(new HelpItem(
			new String[] { "-st", "--st", "-trace", "--trace", "-stacktrace", "--stacktrace" },
			new String[0],
			"Print a stack trace for any exceptions that occur."
		));
		items.add(new HelpItem(
			new String[] { "-v", "--v", "-version", "--version" },
			new String[0],
			"Prints the program name, version number, and copyright notice," +
			" then exits unless other options are specified."
		));
		items.add(new HelpItem(
			new String[] { "-h", "--h", "-help", "--help" },
			new String[0],
			"Prints a summary of all options that can be specified," +
			" then exits unless other options are specified."
		));
		items.add(new HelpItem(
			new String[] { "-le", "--le", "-listenc", "--listenc", "-listencodings", "--listencodings" },
			new String[0],
			"Prints a list of all supported text encodings," +
			" then exits unless other options are specified."
		));
		items.add(new HelpItem(
			new String[] { "-lt", "--lt", "-listtx", "--listtx", "-listtransformations", "--listtransformations" },
			new String[0],
			"Prints a list of all built-in text transformations," +
			" then exits unless other options are specified."
		));
		items.add(new HelpItem(
			new String[] { "-lm", "--lm", "-listmap", "--listmap", "-listmaps", "--listmaps" },
			new String[0],
			"Prints a list of all built-in mappings," +
			" then exits unless other options are specified."
		));
		System.out.println("java -jar recode.jar [ options ] [ files ]");
		for (HelpItem item : items) {
			System.out.println();
			item.print(System.out);
		}
	}
	
	private void printEncodings() {
		SortedSet<SortedSet<String>> encodings = new TreeSet<SortedSet<String>>(
			new Comparator<SortedSet<String>>() {
				@Override
				public int compare(SortedSet<String> a, SortedSet<String> b) {
					return a.first().compareToIgnoreCase(b.first());
				}
			}
		);
		for (Charset charset : Charset.availableCharsets().values()) {
			SortedSet<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			names.add(charset.displayName());
			names.add(charset.name());
			names.addAll(charset.aliases());
			encodings.add(names);
		}
		for (SortedSet<String> names : encodings) {
			boolean first = true;
			for (String name : names) {
				if (first) first = false;
				else System.out.print(", ");
				System.out.print(name);
			}
			System.out.println();
		}
	}
	
	private void printTransformations() {
		for (String name : txLibrary.listByName()) {
			System.out.print(name);
			for (String flag : txLibrary.getByName(name).getFlags()) {
				System.out.print(", ");
				System.out.print(flag);
			}
			System.out.println();
		}
	}
	
	private void printMaps() {
		SortedSet<SortedSet<String>> maps = new TreeSet<SortedSet<String>>(
			new Comparator<SortedSet<String>>() {
				@Override
				public int compare(SortedSet<String> a, SortedSet<String> b) {
					return a.first().compareToIgnoreCase(b.first());
				}
			}
		);
		for (String name : mapLibrary.list()) {
			SortedSet<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
			names.add(name);
			names.addAll(mapLibrary.get(name).getAliases());
			maps.add(names);
		}
		for (SortedSet<String> names : maps) {
			boolean first = true;
			for (String name : names) {
				if (first) first = false;
				else System.out.print(", ");
				System.out.print(name);
			}
			System.out.println();
		}
	}
	
	private void addFile(File file) {
		inputFiles.add(file);
		if (outputFile != null) {
			outputFiles.add(outputFile);
			outputFile = null;
		} else if (outputDirectory != null) {
			outputFiles.add(new File(outputDirectory, file.getName()));
		} else {
			outputFiles.add(file);
		}
	}
	
	private void checkTxArgsEnd() {
		if (txFactory.getArgumentCount() <= txArgs.size()) {
			txArgsList.add(txArgs);
			txFactoryList.add(txFactory);
			txArgs = new ArrayList<String>();
			txFactory = null;
			if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			argType = ArgumentType.PLAIN;
		} else {
			if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.FILES;
			argType = ArgumentType.TRANSFORMATION_ARG;
		}
	}
	
	private void printDebugArgs() {
		System.out.println("==== START RECODE ARGUMENT SUMMARY ====");
		System.out.println("ArgType: " + argType);
		System.out.println("Mode: " + mode);
		System.out.println("TxLibrary: " + collectionToString(txLibrary.listByName()));
		System.out.println("Remap: " + remap);
		System.out.println("MapLibrary: " + collectionToString(mapLibrary.list()));
		System.out.println("InputEncoding: " + inputEncoding);
		System.out.println("OutputEncoding: " + outputEncoding);
		System.out.println("TxFactory: " + txFactory);
		System.out.println("TxArgs: " + collectionToString(txArgs));
		System.out.println("TxFactoryList: " + collectionToString(txFactoryList));
		System.out.println("TxArgsList: " + collectionToString(txArgsList));
		System.out.println("OutputDirectory: " + outputDirectory);
		System.out.println("OutputFile: " + outputFile);
		System.out.println("InputFiles: " + collectionToString(inputFiles));
		System.out.println("OutputFiles: " + collectionToString(outputFiles));
		System.out.println("StackTrace: " + stackTrace);
		System.out.println("DebugArgs: " + debugArgs);
		System.out.println("==== END RECODE ARGUMENT SUMMARY ====");
	}
	
	private String collectionToString(Collection<?> c) {
		if (c == null) return "null";
		StringBuffer sb = new StringBuffer("{ ");
		boolean first = true;
		for (Object o : c) {
			if (first) first = false;
			else sb.append(", ");
			if (o == null) sb.append("null");
			else if (o instanceof Collection) sb.append(collectionToString((Collection<?>)o));
			else sb.append(o.toString());
		}
		if (first) sb.append("}");
		else sb.append(" }");
		return sb.toString();
	}
	
	private static class HelpItem {
		private String[] syntax;
		private String[] description;
		
		public HelpItem(Collection<String> flags, Collection<String> args, String description) {
			this(flags.toArray(new String[0]), args.toArray(new String[0]), description);
		}
		
		public HelpItem(String[] flags, String[] args, String description) {
			StringBuffer syntax = new StringBuffer();
			boolean first = true;
			for (String flag : flags) {
				if (first) first = false;
				else syntax.append(", ");
				syntax.append(flag);
			}
			for (String arg : args) {
				syntax.append(" <");
				syntax.append(arg);
				syntax.append(">");
			}
			this.syntax = syntax.toString().trim().split("\\s+");
			this.description = description.trim().split("\\s+");
		}
		
		public void print(PrintStream out) {
			int pos = 0;
			for (String word : syntax) {
				if (pos == 0) {
					out.print("   "); pos += 3;
					out.print(word); pos += word.length();
				} else if (pos + 1 + word.length() <= 78) {
					out.print(" "); pos += 1;
					out.print(word); pos += word.length();
				} else {
					out.println(); pos = 0;
					out.print("   "); pos += 3;
					out.print(word); pos += word.length();
				}
			}
			if (pos > 0) {
				out.println(); pos = 0;
			}
			for (String word : description) {
				if (pos == 0) {
					out.print("      "); pos += 6;
					out.print(word); pos += word.length();
				} else if (pos + 1 + word.length() <= 78) {
					out.print(" "); pos += 1;
					out.print(word); pos += word.length();
				} else {
					out.println(); pos = 0;
					out.print("      "); pos += 6;
					out.print(word); pos += word.length();
				}
			}
			if (pos > 0) {
				out.println(); pos = 0;
			}
		}
	}
}
