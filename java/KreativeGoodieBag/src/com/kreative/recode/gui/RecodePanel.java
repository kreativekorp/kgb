package com.kreative.recode.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import com.kreative.recode.RecodeArgumentParser;
import com.kreative.recode.RecoderTransformer;
import com.kreative.recode.transformation.TextTransformationFactory;

public class RecodePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final JRadioButton textButton;
	private final JRadioButton fileButton;
	private final RecodeInputPanel inputPanel;
	private final TextTransformationPanelListPanel txListPanel;
	private final RecodeOutputPanel outputPanel;
	private final JProgressBar progress;
	private final JButton recodeButton;
	
	public RecodePanel(RecodeArgumentParser args) {
		JLabel selectorLabel = new JLabel("Recode:");
		this.textButton = new JRadioButton("Pasted Text");
		this.fileButton = new JRadioButton("Text Files");
		this.inputPanel = fixWidth(new RecodeInputPanel(), 240);
		this.txListPanel = new TextTransformationPanelListPanel("Transformations:", args.getTransformationLibrary());
		this.outputPanel = fixWidth(new RecodeOutputPanel(), 240);
		this.progress = new JProgressBar(0, 1);
		this.recodeButton = new JButton("Recode");
		
		JPanel selectorInnerPanel = new JPanel(new FlowLayout());
		selectorInnerPanel.add(selectorLabel);
		selectorInnerPanel.add(textButton);
		selectorInnerPanel.add(fileButton);
		JPanel selectorPanel = new JPanel(new BorderLayout());
		selectorPanel.add(selectorInnerPanel, BorderLayout.LINE_START);
		
		JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
		mainPanel.add(inputPanel, BorderLayout.LINE_START);
		mainPanel.add(txListPanel, BorderLayout.CENTER);
		mainPanel.add(outputPanel, BorderLayout.LINE_END);
		
		JPanel progressPanel = new JPanel(new BorderLayout(8, 8));
		progressPanel.add(progress, BorderLayout.CENTER);
		progressPanel.add(recodeButton, BorderLayout.LINE_END);
		
		setLayout(new BorderLayout(12, 12));
		add(selectorPanel, BorderLayout.PAGE_START);
		add(mainPanel, BorderLayout.CENTER);
		add(progressPanel, BorderLayout.PAGE_END);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		textButton.setSelected(args.getInputFiles().isEmpty());
		fileButton.setSelected(!args.getInputFiles().isEmpty());
		
		inputPanel.setFiles(args.getInputFiles());
		inputPanel.setEncoding(args.getInputEncoding());
		
		for (int i = 0, m = args.getTransformationFactories().size(); i < m; i++) {
			TextTransformationFactory txFactory = args.getTransformationFactories().get(i);
			List<String> txArgs = args.getTransformationArguments().get(i);
			TextTransformationPanel txPanel = new TextTransformationPanel(txFactory, txArgs);
			txListPanel.addTextTransformationPanel(txPanel);
		}
		
		outputPanel.setOutputDirectory(args.getOutputDirectory());
		outputPanel.setEncoding(args.getOutputEncoding());
		updateVisibility();
		
		ButtonGroup bg = new ButtonGroup();
		bg.add(textButton);
		bg.add(fileButton);
		textButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { updateVisibility(); }
		});
		fileButton.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) { updateVisibility(); }
		});
		
		recodeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doRecode();
			}
		});
	}
	
	private void updateVisibility() {
		if (textButton.isSelected()) {
			inputPanel.showTextPanel();
			outputPanel.showTextPanel();
		}
		if (fileButton.isSelected()) {
			inputPanel.showFilePanel();
			outputPanel.showFilePanel();
		}
		progress.setVisible(false);
		revalidate();
	}
	
	private void doRecode() {
		new Thread() {
			@Override
			public void run() {
				recodeButton.setEnabled(false);
				
				Charset inputEncoding = inputPanel.getEncoding();
				if (inputEncoding == null) {
					String msg = "Error: No input encoding selected.";
					JOptionPane.showMessageDialog(null, msg, "Kreative Recode", JOptionPane.ERROR_MESSAGE);
					recodeButton.setEnabled(true);
					return;
				}
				
				Charset outputEncoding = outputPanel.getEncoding();
				if (outputEncoding == null) {
					String msg = "Error: No output encoding selected.";
					JOptionPane.showMessageDialog(null, msg, "Kreative Recode", JOptionPane.ERROR_MESSAGE);
					recodeButton.setEnabled(true);
					return;
				}
				
				RecoderTransformer tx = new RecoderTransformer();
				try {
					for (TextTransformationPanel txPanel : txListPanel.getTextTransformationPanels()) {
						tx.list().add(txPanel.createTransformation());
					}
				} catch (IllegalArgumentException e) {
					String msg = e.getMessage();
					JOptionPane.showMessageDialog(null, msg, "Kreative Recode", JOptionPane.ERROR_MESSAGE);
					recodeButton.setEnabled(true);
					return;
				}
				
				if (textButton.isSelected()) {
					progress.setIndeterminate(true);
					progress.setVisible(true);
					revalidate();
					outputPanel.setText(tx.transformString(inputPanel.getText()));
					progress.setVisible(false);
					revalidate();
				}
				
				if (fileButton.isSelected()) {
					List<File> files = inputPanel.getFiles();
					if (!files.isEmpty()) {
						progress.setIndeterminate(false);
						progress.setValue(0);
						progress.setMaximum(files.size());
						progress.setVisible(true);
						revalidate();
						
						File outdir = outputPanel.getOutputDirectory();
						for (File inputFile : files) {
							File outputFile = (outdir == null) ? inputFile : new File(outdir, inputFile.getName());
							recodeFile(tx, inputFile, outputFile, inputEncoding, outputEncoding);
							progress.setValue(progress.getValue() + 1);
							revalidate();
						}
						
						progress.setVisible(false);
						revalidate();
					}
				}
				
				recodeButton.setEnabled(true);
				return;
			}
		}.start();
	}
	
	private static void recodeFile(RecoderTransformer tx, File inputFile, File outputFile, Charset inputEncoding, Charset outputEncoding) {
		try {
			File tempFile = File.createTempFile("krecode-", ".tmp");
			
			InputStream in = new FileInputStream(inputFile);
			OutputStream out = new FileOutputStream(tempFile);
			tx.recodeTransformStream(in, out, inputEncoding, outputEncoding);
			out.flush();
			out.close();
			in.close();
			
			in = new FileInputStream(tempFile);
			out = new FileOutputStream(outputFile);
			byte[] buffer = new byte[0x10000];
			int l;
			while ((l = in.read(buffer)) > 0) {
				out.write(buffer, 0, l);
			}
			out.flush();
			out.close();
			in.close();
			
			tempFile.delete();
		} catch (IOException e) {
			String msg = "Error: Failed to recode " + inputFile.getName() + ".";
			JOptionPane.showMessageDialog(null, msg, "Kreative Recode", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static <C extends Component> C fixWidth(C c, int w) {
		c.setMinimumSize(new Dimension(w, c.getMinimumSize().height));
		c.setPreferredSize(new Dimension(w, c.getPreferredSize().height));
		c.setMaximumSize(new Dimension(w, c.getMaximumSize().height));
		return c;
	}
}
