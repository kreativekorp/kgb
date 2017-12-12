package com.kreative.recode.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import com.kreative.recode.map.CharacterSequenceMap;
import com.kreative.recode.map.CharacterSequenceMapLibrary;

public class CharacterSequenceMapList extends JList {
	private static final long serialVersionUID = 1L;
	
	private final CharacterSequenceMapLibrary mapLib;
	private final List<String> mapNames;
	
	public CharacterSequenceMapList(CharacterSequenceMapLibrary mapLib) {
		this.mapLib = mapLib;
		this.mapNames = mapLib.list();
		setListData(mapNames.toArray());
		addMouseListener(new MouseAdapter() {
			@Override public void mouseEntered(MouseEvent e) { updateToolTip(e); }
			@Override public void mouseExited(MouseEvent e) { updateToolTip(e); }
		});
		addMouseMotionListener(new MouseMotionListener() {
			@Override public void mouseMoved(MouseEvent e) { updateToolTip(e); }
			@Override public void mouseDragged(MouseEvent e) { updateToolTip(e); }
		});
	}
	
	public Collection<CharacterSequenceMap> getMaps() {
		Set<CharacterSequenceMap> maps = new HashSet<CharacterSequenceMap>();
		for (Object o : this.getSelectedValues()) {
			maps.add(mapLib.get(o.toString()));
		}
		return maps;
	}
	
	public void setMapNames(Collection<String> names) {
		ListSelectionModel selection = this.getSelectionModel();
		selection.clearSelection();
		for (String name : names) {
			int o = mapNames.indexOf(mapLib.get(name).getName());
			if (o >= 0) selection.addSelectionInterval(o, o);
		}
	}
	
	private void updateToolTip(MouseEvent e) {
		int i = locationToIndex(e.getPoint());
		if (i < 0) {
			setToolTipText(null);
		} else {
			String mapName = mapNames.get(i);
			CharacterSequenceMap map = mapLib.get(mapName);
			setToolTipText(map.getDescription());
		}
	}
}
