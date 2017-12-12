package com.kreative.recode.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import com.kreative.recode.map.CharacterSequenceMap;
import com.kreative.recode.map.CharacterSequenceMapLibrary;

public class CharacterSequenceMapListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final CharacterSequenceMapList list;
	private final JLabel errorMessage;
	private final boolean forOutput;
	
	public CharacterSequenceMapListPanel(String caption, CharacterSequenceMapLibrary mapLib, boolean forOutput) {
		this.list = new CharacterSequenceMapList(mapLib);
		JScrollPane listPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.errorMessage = new JLabel("Error: " + (forOutput ? "Output" : "Input") + " mappings overlap.");
		errorMessage.setForeground(new Color(0xFFCC0000));
		this.forOutput = forOutput;
		setLayout(new BorderLayout(4,4));
		if (caption != null) add(new JLabel(caption), BorderLayout.PAGE_START);
		add(listPane, BorderLayout.CENTER);
		add(errorMessage, BorderLayout.PAGE_END);
		errorMessage.setVisible(hasError());
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				errorMessage.setVisible(hasError());
			}
		});
	}
	
	public Collection<CharacterSequenceMap> getMaps() {
		return list.getMaps();
	}
	
	public void setMapNames(Collection<String> names) {
		list.setMapNames(names);
	}
	
	public void setVisibleRowCount(int rowCount) {
		list.setVisibleRowCount(rowCount);
	}
	
	private boolean hasError() {
		if (forOutput) {
			return outputMapsOverlap(list.getMaps());
		} else {
			return inputMapsOverlap(list.getMaps());
		}
	}
	
	private boolean inputMapsOverlap(Collection<CharacterSequenceMap> maps) {
		Map<List<Integer>,String> inputMap = new HashMap<List<Integer>,String>();
		for (CharacterSequenceMap map : maps) {
			for (Map.Entry<List<Integer>,String> e : map.inputMap().entrySet()) {
				if (inputMap.containsKey(e.getKey())) {
					String oldValue = inputMap.get(e.getKey());
					String newValue = e.getValue();
					if (!oldValue.equals(newValue)) return true;
				} else {
					inputMap.put(e.getKey(), e.getValue());
				}
			}
		}
		return false;
	}
	
	private boolean outputMapsOverlap(Collection<CharacterSequenceMap> maps) {
		Map<String,List<Integer>> outputMap = new HashMap<String,List<Integer>>();
		for (CharacterSequenceMap map : maps) {
			for (Map.Entry<String,List<Integer>> e : map.outputMap().entrySet()) {
				if (outputMap.containsKey(e.getKey())) {
					List<Integer> oldValue = outputMap.get(e.getKey());
					List<Integer> newValue = e.getValue();
					if (!oldValue.equals(newValue)) return true;
				} else {
					outputMap.put(e.getKey(), e.getValue());
				}
			}
		}
		return false;
	}
}
