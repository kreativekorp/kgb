package com.kreative.recode.maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.kreative.recode.map.CharacterSequenceMap;

public class BOM {
	private BOM() {}
	
	public static List<CharacterSequenceMap> getCharacterSequenceMaps() {
		List<CharacterSequenceMap> contents = new ArrayList<CharacterSequenceMap>();
		Scanner indexScanner = new Scanner(BOM.class.getResourceAsStream("maps.index"), "UTF-8");
		while (indexScanner.hasNextLine()) {
			String mapName = indexScanner.nextLine().trim();
			if ((mapName.length() > 0) && !mapName.startsWith("#")) {
				CharacterSequenceMap map = new CharacterSequenceMap();
				Scanner mapScanner = new Scanner(BOM.class.getResourceAsStream(mapName), "UTF-8");
				map.read(mapScanner);
				mapScanner.close();
				if (!map.isEmpty()) contents.add(map);
			}
		}
		indexScanner.close();
		return contents;
	}
}
