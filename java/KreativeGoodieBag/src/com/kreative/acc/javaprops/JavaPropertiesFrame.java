package com.kreative.acc.javaprops;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class JavaPropertiesFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private final DefaultTableModel tableModel;
	private final JTable tableView;
	
	public JavaPropertiesFrame() {
		super("Java Properties");
		
		Object[] header = new Object[] { "Key", "Value" };
		
		Properties properties = System.getProperties();
		List<Object[]> dataList = new ArrayList<Object[]>();
		for (Map.Entry<Object,Object> e : properties.entrySet()) {
			dataList.add(new Object[] { e.getKey(), e.getValue() });
		}
		Object[][] data = dataList.toArray(new Object[0][]);
		
		tableModel = new DefaultTableModel(data, header);
		
		tableView = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableView.setColumnSelectionAllowed(false);
		tableView.setRowSelectionAllowed(true);
		tableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableView.setIntercellSpacing(new Dimension(0,0));
		tableView.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int column = tableView.getTableHeader().columnAtPoint(e.getPoint());
				if (column >= 0 && column < 2) {
					sortTable(column, true);
				}
			}
		});
		sortTable(0, true);
		
		JScrollPane tableScrollPane = new JScrollPane(tableView, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JLabel label = new JLabel(data.length + " properties");
		label.setFont(label.getFont().deriveFont(11.0f));
		label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		
		JPanel main = new JPanel(new BorderLayout());
		main.add(tableScrollPane, BorderLayout.CENTER);
		main.add(label, BorderLayout.PAGE_END);
		setContentPane(main);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 300);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void sortTable(final int column, final boolean ascending) {
		Vector<?> tableData = tableModel.getDataVector();
		Collections.sort(tableData, new Comparator<Object>() {
			@Override
			public int compare(Object a, Object b) {
				a = ((Vector<?>)a).get(column);
				b = ((Vector<?>)b).get(column);
				if (a == null) a = "";
				if (b == null) b = "";
				if (ascending) {
					return a.toString().compareToIgnoreCase(b.toString());
				} else {
					return b.toString().compareToIgnoreCase(a.toString());
				}
			}
		});
		tableModel.fireTableDataChanged();
	}
}
