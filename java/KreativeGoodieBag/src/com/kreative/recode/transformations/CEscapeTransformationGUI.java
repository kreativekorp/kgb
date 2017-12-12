package com.kreative.recode.transformations;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JPanel;
import com.kreative.recode.gui.EncodingListPanel;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationGUI;

public class CEscapeTransformationGUI extends JPanel implements TextTransformationGUI {
	private static final long serialVersionUID = 1L;
	
	private final boolean ignoreASCII;
	private final EncodingListPanel encoding;
	
	public CEscapeTransformationGUI(boolean ignoreASCII, String encoding) {
		this.ignoreASCII = ignoreASCII;
		this.encoding = new EncodingListPanel("Encoding:");
		this.encoding.setVisibleRowCount(6);
		setLayout(new GridLayout(1,0,8,8));
		add(this.encoding);
		this.encoding.setEncodingName(encoding);
	}
	
	@Override
	public Component getComponent() {
		return this;
	}
	
	@Override
	public TextTransformation createTransformation() {
		return new CEscapeTransformation(ignoreASCII, encoding.getEncoding());
	}
}
