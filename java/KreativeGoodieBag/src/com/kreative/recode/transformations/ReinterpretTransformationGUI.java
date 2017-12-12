package com.kreative.recode.transformations;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JPanel;
import com.kreative.recode.gui.EncodingListPanel;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationGUI;

public class ReinterpretTransformationGUI extends JPanel implements TextTransformationGUI {
	private static final long serialVersionUID = 1L;
	
	private final EncodingListPanel interpretedAs;
	private final EncodingListPanel reinterpretAs;
	
	public ReinterpretTransformationGUI(String interpretedAs, String reinterpretAs) {
		this.interpretedAs = new EncodingListPanel("Original Encoding:");
		this.interpretedAs.setVisibleRowCount(6);
		this.reinterpretAs = new EncodingListPanel("Reinterpret As:");
		this.reinterpretAs.setVisibleRowCount(6);
		setLayout(new GridLayout(1,0,8,8));
		add(this.interpretedAs);
		add(this.reinterpretAs);
		this.interpretedAs.setEncodingName(interpretedAs);
		this.reinterpretAs.setEncodingName(reinterpretAs);
	}
	
	@Override
	public Component getComponent() {
		return this;
	}
	
	@Override
	public TextTransformation createTransformation() {
		return new ReinterpretTransformation(interpretedAs.getEncoding(), reinterpretAs.getEncoding());
	}
}
