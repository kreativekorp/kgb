package com.kreative.recode.misc;

public class ConCharacter {
	private ConCharacter() {}
	
	public static boolean isLetter(int codePoint) {
		if (codePoint >= 0xE000 && codePoint <= 0xF8FF) {
			if (codePoint >= 0xE000 && codePoint <= 0xE02E) return true;
			if (codePoint >= 0xE030 && codePoint <= 0xE03D) return true;
			if (codePoint >= 0xE040 && codePoint <= 0xE05A) return true;
			if (codePoint >= 0xE080 && codePoint <= 0xE0C0) return true;
			if (codePoint >= 0xE0C2 && codePoint <= 0xE0E6) return true;
			if (codePoint >= 0xE100 && codePoint <= 0xE128) return true;
			if (codePoint == 0xE12E) return true;
			if (codePoint >= 0xE130 && codePoint <= 0xE138) return true;
			if (codePoint == 0xE13F) return true;
			if (codePoint >= 0xE150 && codePoint <= 0xE16C) return true;
			if (codePoint >= 0xE170 && codePoint <= 0xE18C) return true;
			if (codePoint >= 0xE190 && codePoint <= 0xE19C) return true;
			if (codePoint >= 0xE1B0 && codePoint <= 0xE1CB) return true;
			if (codePoint >= 0xE1D0 && codePoint <= 0xE1EA) return true;
			if (codePoint >= 0xE200 && codePoint <= 0xE225) return true;
			if (codePoint >= 0xE230 && codePoint <= 0xE259) return true;
			if (codePoint >= 0xE270 && codePoint <= 0xE28D) return true;
			if (codePoint >= 0xE290 && codePoint <= 0xE295) return true;
			if (codePoint >= 0xE298 && codePoint <= 0xE29A) return true;
			if (codePoint >= 0xE29C && codePoint <= 0xE2A1) return true;
			if (codePoint >= 0xE2A4 && codePoint <= 0xE2A5) return true;
			if (codePoint >= 0xE2A8 && codePoint <= 0xE2AA) return true;
			if (codePoint >= 0xE2B0 && codePoint <= 0xE2B9) return true;
			if (codePoint >= 0xE2C0 && codePoint <= 0xE2CA) return true;
			if (codePoint >= 0xE2D0 && codePoint <= 0xE2F3) return true;
			if (codePoint >= 0xE300 && codePoint <= 0xE313) return true;
			if (codePoint >= 0xE321 && codePoint <= 0xE330) return true;
			if (codePoint >= 0xE340 && codePoint <= 0xE353) return true;
			if (codePoint >= 0xE360 && codePoint <= 0xE374) return true;
			if (codePoint >= 0xE380 && codePoint <= 0xE399) return true;
			if (codePoint >= 0xE3B0 && codePoint <= 0xE3E8) return true;
			if (codePoint >= 0xE400 && codePoint <= 0xE423) return true;
			if (codePoint >= 0xE430 && codePoint <= 0xE463) return true;
			if (codePoint >= 0xE470 && codePoint <= 0xE48B) return true;
			if (codePoint >= 0xE490 && codePoint <= 0xE4B4) return true;
			if (codePoint >= 0xE4C0 && codePoint <= 0xE4E8) return true;
			if (codePoint >= 0xE4F0 && codePoint <= 0xE51B) return true;
			if (codePoint >= 0xE520 && codePoint <= 0xE548) return true;
			if (codePoint >= 0xE550 && codePoint <= 0xE572) return true;
			if (codePoint >= 0xE580 && codePoint <= 0xE593) return true;
			if (codePoint >= 0xE5A0 && codePoint <= 0xE5BC) return true;
			if (codePoint >= 0xE5C0 && codePoint <= 0xE5DD) return true;
			if (codePoint >= 0xE5E0 && codePoint <= 0xE5F9) return true;
			if (codePoint >= 0xE611 && codePoint <= 0xE643) return true;
			if (codePoint >= 0xE650 && codePoint <= 0xE677) return true;
			if (codePoint >= 0xE684 && codePoint <= 0xE685) return true;
			if (codePoint == 0xE687) return true;
			if (codePoint == 0xE68E) return true;
			if (codePoint >= 0xE690 && codePoint <= 0xE6AB) return true;
			if (codePoint >= 0xE6AF && codePoint <= 0xE6BF) return true;
			if (codePoint >= 0xE6D0 && codePoint <= 0xE6D8) return true;
			if (codePoint >= 0xE6DA && codePoint <= 0xE6E5) return true;
			if (codePoint == 0xE6E7) return true;
			if (codePoint >= 0xE700 && codePoint <= 0xE72F) return true;
			if (codePoint >= 0xE740 && codePoint <= 0xE767) return true;
			if (codePoint >= 0xE770 && codePoint <= 0xE776) return true;
			if (codePoint >= 0xE780 && codePoint <= 0xE7F7) return true;
			if (codePoint >= 0xE800 && codePoint <= 0xE857) return true;
			if (codePoint >= 0xE860 && codePoint <= 0xE887) return true;
			if (codePoint >= 0xE920 && codePoint <= 0xE97F) return true;
			if (codePoint >= 0xE990 && codePoint <= 0xE9DF) return true;
			if (codePoint >= 0xEA00 && codePoint <= 0xEA07) return true;
			if (codePoint >= 0xEA09 && codePoint <= 0xEA0C) return true;
			if (codePoint >= 0xEA0E && codePoint <= 0xEA11) return true;
			if (codePoint >= 0xEA13 && codePoint <= 0xEA16) return true;
			if (codePoint >= 0xEA18 && codePoint <= 0xEA1B) return true;
			if (codePoint >= 0xEA1D && codePoint <= 0xEA20) return true;
			if (codePoint >= 0xEA22 && codePoint <= 0xEA25) return true;
			if (codePoint >= 0xEA27 && codePoint <= 0xEA2A) return true;
			if (codePoint >= 0xEA2C && codePoint <= 0xEA2F) return true;
			if (codePoint >= 0xEA31 && codePoint <= 0xEA34) return true;
			if (codePoint >= 0xEA36 && codePoint <= 0xEA39) return true;
			if (codePoint >= 0xEA3B && codePoint <= 0xEA6C) return true;
			if (codePoint >= 0xEA76 && codePoint <= 0xEA79) return true;
			if (codePoint >= 0xEAA0 && codePoint <= 0xEAAE) return true;
			if (codePoint >= 0xEAB0 && codePoint <= 0xEABE) return true;
			if (codePoint >= 0xEAC0 && codePoint <= 0xEACE) return true;
			if (codePoint >= 0xEAD0 && codePoint <= 0xEADE) return true;
			if (codePoint >= 0xEAE0 && codePoint <= 0xEAEE) return true;
			if (codePoint >= 0xED11 && codePoint <= 0xED2F) return true;
			if (codePoint >= 0xF8A0 && codePoint <= 0xF8BC) return true;
			if (codePoint >= 0xF8D0 && codePoint <= 0xF8E9) return true;
			return false;
		} else if (codePoint >= 0xF0000 && codePoint < 0x110000) {
			if (codePoint >= 0xF0000 && codePoint <= 0xF16A4) return true;
			if (codePoint >= 0xF16B1 && codePoint <= 0xF16BA) return true;
			if (codePoint >= 0xF16C6 && codePoint <= 0xF16C7) return true;
			if (codePoint == 0xF16C9) return true;
			if (codePoint >= 0xF16CB && codePoint <= 0xF16D4) return true;
			if (codePoint >= 0xF16D6 && codePoint <= 0xF16DD) return true;
			if (codePoint == 0xF16DF) return true;
			if (codePoint >= 0xF16E1 && codePoint <= 0xF16EA) return true;
			if (codePoint >= 0xF16EC && codePoint <= 0xF16F3) return true;
			if (codePoint == 0xF16F5) return true;
			if (codePoint >= 0xF16F7 && codePoint <= 0xF1700) return true;
			if (codePoint >= 0xF1702 && codePoint <= 0xF1709) return true;
			if (codePoint == 0xF170B) return true;
			if (codePoint >= 0xF170D && codePoint <= 0xF1716) return true;
			if (codePoint >= 0xF1718 && codePoint <= 0xF171F) return true;
			if (codePoint == 0xF1721) return true;
			if (codePoint >= 0xF1723 && codePoint <= 0xF172C) return true;
			if (codePoint >= 0xF172E && codePoint <= 0xF1737) return true;
			if (codePoint >= 0xF1739 && codePoint <= 0xF173E) return true;
			if (codePoint >= 0xF174A && codePoint <= 0xF174B) return true;
			if (codePoint == 0xF174D) return true;
			if (codePoint >= 0xF174F && codePoint <= 0xF1758) return true;
			if (codePoint >= 0xF175A && codePoint <= 0xF1763) return true;
			if (codePoint >= 0xF1765 && codePoint <= 0xF176A) return true;
			if (codePoint >= 0xF1776 && codePoint <= 0xF1777) return true;
			if (codePoint == 0xF1779) return true;
			if (codePoint >= 0xF177B && codePoint <= 0xF1784) return true;
			if (codePoint >= 0xF1786 && codePoint <= 0xF178F) return true;
			if (codePoint >= 0xF1791 && codePoint <= 0xF1796) return true;
			if (codePoint >= 0xF17A2 && codePoint <= 0xF17A3) return true;
			if (codePoint == 0xF17A5) return true;
			if (codePoint >= 0xF17A7 && codePoint <= 0xF17B0) return true;
			if (codePoint >= 0xF17B2 && codePoint <= 0xF17BB) return true;
			if (codePoint >= 0xF17BD && codePoint <= 0xF17C2) return true;
			if (codePoint >= 0xF17CE && codePoint <= 0xF17D9) return true;
			if (codePoint == 0xF17DB) return true;
			if (codePoint >= 0xF17DE && codePoint <= 0xF17E5) return true;
			if (codePoint == 0xF17E7) return true;
			if (codePoint >= 0xF17E9 && codePoint <= 0xF17F2) return true;
			if (codePoint >= 0xF17F4 && codePoint <= 0xF1805) return true;
			if (codePoint == 0xF1807) return true;
			if (codePoint >= 0xF180A && codePoint <= 0xF1811) return true;
			if (codePoint == 0xF1813) return true;
			if (codePoint >= 0xF1815 && codePoint <= 0xF181E) return true;
			if (codePoint >= 0xF1820 && codePoint <= 0xF1827) return true;
			if (codePoint == 0xF1829) return true;
			if (codePoint >= 0xF182B && codePoint <= 0xF1834) return true;
			if (codePoint >= 0xF1836 && codePoint <= 0xF183D) return true;
			if (codePoint == 0xF183F) return true;
			if (codePoint >= 0xF1841 && codePoint <= 0xF184A) return true;
			if (codePoint >= 0xF184C && codePoint <= 0xF1853) return true;
			if (codePoint == 0xF1855) return true;
			if (codePoint >= 0xF1857 && codePoint <= 0xF1860) return true;
			if (codePoint >= 0xF1862 && codePoint <= 0xF1869) return true;
			if (codePoint == 0xF186B) return true;
			if (codePoint >= 0xF186D && codePoint <= 0xF1876) return true;
			if (codePoint >= 0xF1878 && codePoint <= 0xF187F) return true;
			if (codePoint == 0xF1881) return true;
			if (codePoint >= 0xF1883 && codePoint <= 0xF188C) return true;
			if (codePoint >= 0xF188E && codePoint <= 0xF1895) return true;
			if (codePoint == 0xF1897) return true;
			if (codePoint >= 0xF1899 && codePoint <= 0xF18A2) return true;
			if (codePoint >= 0xF18A4 && codePoint <= 0xF18AB) return true;
			if (codePoint == 0xF18AD) return true;
			if (codePoint >= 0xF18AF && codePoint <= 0xF18B8) return true;
			if (codePoint >= 0xF18BA && codePoint <= 0xF18C1) return true;
			if (codePoint == 0xF18C3) return true;
			if (codePoint >= 0xF18C5 && codePoint <= 0xF18CE) return true;
			if (codePoint >= 0xF18D0 && codePoint <= 0xF18D7) return true;
			if (codePoint == 0xF18D9) return true;
			if (codePoint >= 0xF18DB && codePoint <= 0xF18E4) return true;
			if (codePoint >= 0xF18E6 && codePoint <= 0xF18ED) return true;
			if (codePoint == 0xF18EF) return true;
			if (codePoint >= 0xF18F1 && codePoint <= 0xF18FA) return true;
			if (codePoint >= 0xF18FC && codePoint <= 0xF1903) return true;
			if (codePoint == 0xF1905) return true;
			if (codePoint >= 0xF1907 && codePoint <= 0xF1910) return true;
			if (codePoint >= 0xF1912 && codePoint <= 0xF191B) return true;
			if (codePoint >= 0xF191D && codePoint <= 0xF1923) return true;
			if (codePoint >= 0xF1925 && codePoint <= 0xF1926) return true;
			if (codePoint >= 0xF1928 && codePoint <= 0xF1931) return true;
			if (codePoint >= 0xF1933 && codePoint <= 0xF1938) return true;
			return false;
		} else {
			return Character.isLetter(codePoint);
		}
	}
	
	public static int toUpperCase(int codePoint) {
		if (codePoint >= 0xE000 && codePoint <= 0xF8FF) {
			// Verdurian
			if (codePoint >= 0xE230 && codePoint <= 0xE255) {
				return codePoint - 0x30;
			}
			// Deseret
			if (codePoint >= 0xE860 && codePoint <= 0xE88F) {
				return codePoint - 0x30;
			}
			// Glaitha-A
			if (codePoint >= 0xE950 && codePoint <= 0xE97F) {
				return codePoint - 0x30;
			}
			// Wanya
			if (codePoint >= 0xEAC0 && codePoint <= 0xEACE) {
				return codePoint - 0x20;
			}
			if (codePoint >= 0xEAD0 && codePoint <= 0xEADE) {
				return codePoint - 0x20;
			}
			if (codePoint >= 0xEAE6 && codePoint <= 0xEAEB) {
				return codePoint - 0x6;
			}
			// Others
			return codePoint;
		} else {
			return Character.toUpperCase(codePoint);
		}
	}
	
	public static int toTitleCase(int codePoint) {
		if (codePoint >= 0xE000 && codePoint <= 0xF8FF) {
			// Verdurian
			if (codePoint >= 0xE230 && codePoint <= 0xE255) {
				return codePoint - 0x30;
			}
			// Deseret
			if (codePoint >= 0xE860 && codePoint <= 0xE88F) {
				return codePoint - 0x30;
			}
			// Glaitha-A
			if (codePoint >= 0xE950 && codePoint <= 0xE97F) {
				return codePoint - 0x30;
			}
			// Wanya
			if (codePoint >= 0xEAC0 && codePoint <= 0xEACE) {
				return codePoint - 0x20;
			}
			if (codePoint >= 0xEAD0 && codePoint <= 0xEADE) {
				return codePoint - 0x20;
			}
			if (codePoint >= 0xEAE6 && codePoint <= 0xEAEB) {
				return codePoint - 0x6;
			}
			// Others
			return codePoint;
		} else {
			return Character.toTitleCase(codePoint);
		}
	}
	
	public static int toLowerCase(int codePoint) {
		if (codePoint >= 0xE000 && codePoint <= 0xF8FF) {
			// Verdurian
			if (codePoint >= 0xE200 && codePoint <= 0xE225) {
				return codePoint + 0x30;
			}
			// Deseret
			if (codePoint >= 0xE830 && codePoint <= 0xE85F) {
				return codePoint + 0x30;
			}
			// Glaitha-A
			if (codePoint >= 0xE920 && codePoint <= 0xE94F) {
				return codePoint + 0x30;
			}
			// Wanya
			if (codePoint >= 0xEAA0 && codePoint <= 0xEAAE) {
				return codePoint + 0x20;
			}
			if (codePoint >= 0xEAB0 && codePoint <= 0xEABE) {
				return codePoint + 0x20;
			}
			if (codePoint >= 0xEAE0 && codePoint <= 0xEAE5) {
				return codePoint + 0x6;
			}
			// Others
			return codePoint;
		} else {
			return Character.toLowerCase(codePoint);
		}
	}
}
