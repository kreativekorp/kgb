package com.kreative.recode.transformations;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.kreative.recode.misc.CipherScript;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationGUI;

public class AtbashCipherTransformationGUI extends JPanel implements TextTransformationGUI {
	private static final long serialVersionUID = 1L;
	
	private final JComboBox selector;
	
	public AtbashCipherTransformationGUI(CipherScript script) {
		this.selector = new JComboBox(CipherScript.values());
		this.selector.setEditable(false);
		this.selector.setMaximumRowCount(CipherScript.values().length);
		this.selector.setSelectedItem(script);
		
		JPanel inner = new JPanel(new BorderLayout(4,4));
		inner.add(new JLabel("Alphabet:"), BorderLayout.LINE_START);
		inner.add(selector, BorderLayout.CENTER);
		
		setLayout(new FlowLayout());
		add(inner);
	}
	
	@Override
	public Component getComponent() {
		return this;
	}
	
	@Override
	public TextTransformation createTransformation() {
		CipherScript script = (CipherScript)selector.getSelectedItem();
		return new AtbashCipherTransformation(script);
	}
}
