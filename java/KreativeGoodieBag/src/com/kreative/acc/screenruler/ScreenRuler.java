package com.kreative.acc.screenruler;

import java.awt.Toolkit;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ScreenRuler {
	private static enum Mode { AUTO, INFO, GUI, ERROR; }
	private static enum UnitOption { IN, CM; }
	private static enum PpOption { PPI, PPCM; }
	
	public static void main(String[] args) {
		Mode mode = Mode.AUTO;
		UnitOption unitOption = null;
		PpOption ppOption = null;
		double ppValue = 0;
		
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if (arg.equalsIgnoreCase("-version") || arg.equalsIgnoreCase("--version")) {
				System.out.println("Screen Ruler 1.1.1");
				System.out.println("(c) 2012-2023 Kreative Software");
				if (mode == Mode.AUTO) mode = Mode.INFO;
			} else if (arg.equalsIgnoreCase("-help") || arg.equalsIgnoreCase("--help")) {
				System.out.println("java -jar screenruler.jar [ -px | -in | -cm ] [ ( -ppi | -ppcm ) <n> ]");
				if (mode == Mode.AUTO) mode = Mode.INFO;
			} else if (arg.equalsIgnoreCase("-px") || arg.equalsIgnoreCase("--px")) {
				unitOption = null;
				if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.GUI;
			} else if (arg.equalsIgnoreCase("-in") || arg.equalsIgnoreCase("--in")) {
				unitOption = UnitOption.IN;
				if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.GUI;
			} else if (arg.equalsIgnoreCase("-cm") || arg.equalsIgnoreCase("--cm")) {
				unitOption = UnitOption.CM;
				if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.GUI;
			} else if (arg.equalsIgnoreCase("-ppi") || arg.equalsIgnoreCase("--ppi")) {
				i++;
				if (i < args.length) {
					arg = args[i];
					ppOption = PpOption.PPI;
					ppValue = Double.parseDouble(arg);
					if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.GUI;
				} else {
					System.err.println("Missing parameter for option " + arg + ".");
					mode = Mode.ERROR;
				}
			} else if (arg.equalsIgnoreCase("-ppcm") || arg.equalsIgnoreCase("--ppcm")) {
				i++;
				if (i < args.length) {
					arg = args[i];
					ppOption = PpOption.PPCM;
					ppValue = Double.parseDouble(arg);
					if (mode == Mode.AUTO || mode == Mode.INFO) mode = Mode.GUI;
				} else {
					System.err.println("Missing parameter for option " + arg + ".");
					mode = Mode.ERROR;
				}
			} else {
				System.err.println("Unrecognized option " + arg + ".");
				mode = Mode.ERROR;
			}
		}
		
		if (mode == Mode.AUTO || mode == Mode.GUI) {
			ScreenRulerParameters[] units;
			String[] unitNames;
			int unitIndex;
			
			if (ppOption == null) {
				units = new ScreenRulerParameters[] {
						ScreenRulerParameters.forPixels(),
						ScreenRulerParameters.forInches(72),
						ScreenRulerParameters.forInches(96),
						ScreenRulerParameters.forInches(100),
						ScreenRulerParameters.forCentimeters(72/2.54),
						ScreenRulerParameters.forCentimeters(96/2.54),
						ScreenRulerParameters.forCentimeters(100/2.54),
				};
				unitNames = new String[] {
						"Pixels",
						"Inches (72ppi)",
						"Inches (96ppi)",
						"Inches (100ppi)",
						"Centimeters (72ppi)",
						"Centimeters (96ppi)",
						"Centimeters (100ppi)",
				};
				if (unitOption == null) {
					unitIndex = 0;
				} else {
					switch (unitOption) {
					case IN: unitIndex = 1; break;
					case CM: unitIndex = 4; break;
					default: throw new IllegalArgumentException();
					}
				}
			} else {
				switch (ppOption) {
				case PPI:
					units = new ScreenRulerParameters[] {
							ScreenRulerParameters.forPixels(),
							ScreenRulerParameters.forInches(ppValue),
							ScreenRulerParameters.forCentimeters(ppValue / 2.54),
					};
					unitNames = new String[] {
							"Pixels",
							"Inches (" + myDoubleToString(ppValue) + "ppi)",
							"Centimeters (" + myDoubleToString(ppValue) + "ppi)",
					};
					break;
				case PPCM:
					units = new ScreenRulerParameters[] {
							ScreenRulerParameters.forPixels(),
							ScreenRulerParameters.forInches(ppValue * 2.54),
							ScreenRulerParameters.forCentimeters(ppValue),
					};
					unitNames = new String[] {
							"Pixels",
							"Inches (" + myDoubleToString(ppValue) + "ppcm)",
							"Centimeters (" + myDoubleToString(ppValue) + "ppcm)",
					};
					break;
				default:
					throw new IllegalArgumentException();
				}
				if (unitOption == null) {
					unitIndex = 0;
				} else {
					switch (unitOption) {
					case IN: unitIndex = 1; break;
					case CM: unitIndex = 2; break;
					default: throw new IllegalArgumentException();
					}
				}
			}
			
			final ScreenRulerParameters[] finalUnits = units;
			final String[] finalUnitNames = unitNames;
			final int finalUnitIndex = unitIndex;
			
			try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Screen Ruler"); } catch (Exception e) {}
			try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
			try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
			
			try {
				Method getModule = Class.class.getMethod("getModule");
				Object javaDesktop = getModule.invoke(Toolkit.getDefaultToolkit().getClass());
				Object allUnnamed = getModule.invoke(ScreenRuler.class);
				Class<?> module = Class.forName("java.lang.Module");
				Method addOpens = module.getMethod("addOpens", String.class, module);
				addOpens.invoke(javaDesktop, "sun.awt.X11", allUnnamed);
			} catch (Exception e) {}
			
			try {
				Toolkit tk = Toolkit.getDefaultToolkit();
				Field aacn = tk.getClass().getDeclaredField("awtAppClassName");
				aacn.setAccessible(true);
				aacn.set(tk, "Screen Ruler");
			} catch (Exception e) {}
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new ScreenRulerFrame(finalUnits, finalUnitNames, finalUnitIndex);
				}
			});
		}
	}
	
	private static final String myDoubleToString(double d) {
		if (d == (long)d) {
			return Long.toString((long)d);
		} else {
			return Double.toString(d);
		}
	}
}
