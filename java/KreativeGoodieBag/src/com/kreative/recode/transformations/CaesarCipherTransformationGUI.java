package com.kreative.recode.transformations;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import com.kreative.recode.misc.CipherScript;
import com.kreative.recode.transformation.TextTransformation;
import com.kreative.recode.transformation.TextTransformationGUI;

public class CaesarCipherTransformationGUI extends JPanel implements TextTransformationGUI {
	private static final long serialVersionUID = 1L;
	
	private final JComboBox selector;
	private final SpinnerNumberModel spinner;
	
	public CaesarCipherTransformationGUI(CipherScript script, int shift) {
		this.selector = new JComboBox(CipherScript.values());
		this.selector.setEditable(false);
		this.selector.setMaximumRowCount(CipherScript.values().length);
		this.selector.setSelectedItem(script);
		this.spinner = new SpinnerNumberModel(shift, -script.size(), script.size(), 1);
		
		JPanel labels = new JPanel(new GridLayout(0,1,4,4));
		labels.add(new JLabel("Alphabet:"));
		labels.add(new JLabel("Shift:"));
		
		JPanel fields = new JPanel(new GridLayout(0,1,4,4));
		fields.add(selector);
		fields.add(new JSpinner(spinner));
		
		JPanel inner = new JPanel(new BorderLayout(4,4));
		inner.add(labels, BorderLayout.LINE_START);
		inner.add(fields, BorderLayout.CENTER);
		
		setLayout(new FlowLayout());
		add(inner);
		
		selector.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				CipherScript script = (CipherScript)selector.getSelectedItem();
				spinner.setMinimum(-script.size());
				spinner.setMaximum(script.size());
				spinner.setValue(script.size() / 2);
			}
		});
	}
	
	@Override
	public Component getComponent() {
		return this;
	}
	
	@Override
	public TextTransformation createTransformation() {
		CipherScript script = (CipherScript)selector.getSelectedItem();
		int shift = spinner.getNumber().intValue();
		return new CaesarCipherTransformation(script, shift);
	}
}
