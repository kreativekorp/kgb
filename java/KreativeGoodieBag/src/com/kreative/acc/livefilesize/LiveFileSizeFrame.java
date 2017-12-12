package com.kreative.acc.livefilesize;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LiveFileSizeFrame extends JFrame {
	private static final long serialVersionUID = 1;
	
	private static final String[] B_LEVELS = { "B", "KiB", "MiB", "GiB", "TiB", "PiB", "EiB", "ZiB", "YiB" };
	private static final String[] M_LEVELS = { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
	
	private List<File> fileList;
	private List<JLabel> nameLabelList;
	private List<JLabel> sizeLabelList;
	private List<JLabel> msizeLabelList;
	private List<JLabel> bsizeLabelList;
	private List<JButton> removeButtonList;
	
	private JPanel namePanel;
	private JPanel sizePanel;
	private JPanel msizePanel;
	private JPanel bsizePanel;
	private JPanel removePanel;
	
	private FileSizeUpdateThread updater;
	
	public LiveFileSizeFrame() {
		super("LiveFileSize");
		
		fileList = new ArrayList<File>();
		nameLabelList = new ArrayList<JLabel>();
		sizeLabelList = new ArrayList<JLabel>();
		msizeLabelList = new ArrayList<JLabel>();
		bsizeLabelList = new ArrayList<JLabel>();
		removeButtonList = new ArrayList<JButton>();
		
		namePanel = new JPanel(new GridLayout(0,1,20,4));
		sizePanel = new JPanel(new GridLayout(0,1,20,4));
		msizePanel = new JPanel(new GridLayout(0,1,20,4));
		bsizePanel = new JPanel(new GridLayout(0,1,20,4));
		removePanel = new JPanel(new GridLayout(0,1,20,4));
		
		JButton addButton = new JButton("Remove");
		addButton.setMinimumSize(addButton.getMinimumSize());
		addButton.setPreferredSize(addButton.getPreferredSize());
		addButton.setMaximumSize(addButton.getMaximumSize());
		addButton.setText("Add...");
		
		JPanel footer = new JPanel(new BorderLayout(20,4));
		footer.add(new JLabel("Drop file here or click Add."), BorderLayout.LINE_START);
		footer.add(addButton, BorderLayout.LINE_END);
		
		JPanel center = new JPanel(new GridLayout(1,0,20,4));
		center.add(sizePanel);
		center.add(msizePanel);
		center.add(bsizePanel);
		
		JPanel main = new JPanel(new BorderLayout(20,4));
		main.add(namePanel, BorderLayout.LINE_START);
		main.add(center, BorderLayout.CENTER);
		main.add(removePanel, BorderLayout.LINE_END);
		main.add(footer, BorderLayout.PAGE_END);
		main.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		addButton.addActionListener(new AddFileActionListener());
		new DropTarget(main, new AddDropTarget(this));
		main.setComponentPopupMenu(new LiveFileSizeMenu(this));
		
		setContentPane(main);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		
		updater = new FileSizeUpdateThread();
		updater.start();
	}
	
	@Override
	public void dispose() {
		updater.interrupt();
		super.dispose();
	}
	
	public synchronized void addFile(File f) {
		JLabel nameLabel = new JLabel(f.getName());
		JLabel sizeLabel = new JLabel();
		JLabel msizeLabel = new JLabel();
		JLabel bsizeLabel = new JLabel();
		final JButton removeButton = new JButton("Remove");
		
		fileList.add(f);
		nameLabelList.add(nameLabel);
		sizeLabelList.add(sizeLabel);
		msizeLabelList.add(msizeLabel);
		bsizeLabelList.add(bsizeLabel);
		removeButtonList.add(removeButton);
		
		namePanel.add(nameLabel);
		sizePanel.add(sizeLabel);
		msizePanel.add(msizeLabel);
		bsizePanel.add(bsizeLabel);
		removePanel.add(removeButton);
		
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeFile(removeButtonList.indexOf(removeButton));
			}
		});
		
		pack();
	}
	
	public synchronized void removeFile(int index) {
		fileList.remove(index);
		nameLabelList.remove(index);
		sizeLabelList.remove(index);
		msizeLabelList.remove(index);
		bsizeLabelList.remove(index);
		removeButtonList.remove(index);
		
		namePanel.remove(index);
		sizePanel.remove(index);
		msizePanel.remove(index);
		bsizePanel.remove(index);
		removePanel.remove(index);
		
		pack();
	}
	
	public synchronized void removeAllFiles() {
		fileList.clear();
		nameLabelList.clear();
		sizeLabelList.clear();
		msizeLabelList.clear();
		bsizeLabelList.clear();
		removeButtonList.clear();
		
		namePanel.removeAll();
		sizePanel.removeAll();
		msizePanel.removeAll();
		bsizePanel.removeAll();
		removePanel.removeAll();
		
		pack();
	}
	
	public synchronized void updateFiles() {
		for (int i = 0; i < fileList.size(); i++) {
			File f = fileList.get(i);
			long l = f.length();
			bsizeLabelList.get(i).setText(Long.toString(l));
			
			int bLvl = 0;
			double bAmt = l;
			while (bAmt >= 1024.0) {
				bLvl++;
				bAmt /= 1024.0;
			}
			String b = ((bLvl==0)?Integer.toString((int)bAmt):Double.toString(Math.round(bAmt*10.0)/10.0)) + B_LEVELS[bLvl];
			sizeLabelList.get(i).setText(b);
			
			int mLvl = 0;
			double mAmt = l;
			while (mAmt >= 1000.0) {
				mLvl++;
				mAmt /= 1000.0;
			}
			String m = ((mLvl==0)?Integer.toString((int)mAmt):Double.toString(Math.round(mAmt*10.0)/10.0)) + M_LEVELS[mLvl];
			msizeLabelList.get(i).setText(m);
		}
		
		pack();
	}
	
	private class AddFileActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			FileDialog fd = new FileDialog(LiveFileSizeFrame.this, "Select File", FileDialog.LOAD);
			fd.setVisible(true);
			if (fd.getDirectory() != null && fd.getFile() != null) {
				File f = new File(fd.getDirectory(), fd.getFile());
				addFile(f);
			}
		}
	};
	
	private class FileSizeUpdateThread extends Thread {
		public void run() {
			while (!Thread.interrupted()) {
				try {
					updateFiles();
					Thread.sleep(500);
				} catch (InterruptedException ie) {
					break;
				}
			}
		}
	}
}
