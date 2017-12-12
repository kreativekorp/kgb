package com.kreative.recode.transformations;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Collection;
import javax.swing.JPanel;
import com.kreative.recode.gui.CharacterSequenceMapListPanel;
import com.kreative.recode.map.CharacterSequenceMapLibrary;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationGUI;

public class SequenceRemapTransformationGUI extends JPanel implements TextTransformationGUI {
	private static final long serialVersionUID = 1L;
	
	private final CharacterSequenceMapListPanel inputMapList;
	private final CharacterSequenceMapListPanel outputMapList;
	
	public SequenceRemapTransformationGUI(CharacterSequenceMapLibrary mapLib, Collection<String> inputMaps, Collection<String> outputMaps) {
		this.inputMapList = new CharacterSequenceMapListPanel("Input Mappings:", mapLib, false);
		this.inputMapList.setVisibleRowCount(6);
		this.outputMapList = new CharacterSequenceMapListPanel("Output Mappings:", mapLib, false);
		this.outputMapList.setVisibleRowCount(6);
		setLayout(new GridLayout(1,0,8,8));
		add(this.inputMapList);
		add(this.outputMapList);
		this.inputMapList.setMapNames(inputMaps);
		this.outputMapList.setMapNames(outputMaps);
	}
	
	@Override
	public Component getComponent() {
		return this;
	}
	
	@Override
	public TextTransformation createTransformation() {
		return new SequenceRemapTransformation(inputMapList.getMaps(), outputMapList.getMaps());
	}
}
