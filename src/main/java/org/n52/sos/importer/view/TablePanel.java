package org.n52.sos.importer.view;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * contains the table which is used by step panels 3, 4 and 5
 * @author Raimund
 *
 */
public class TablePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static TablePanel instance = null;
	
	private final JTable table;
	
	private TablePanel() { 
		super();
		this.table = new JTable();
		table.setPreferredScrollableViewportSize(new Dimension(700, 150));
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane);
	}
	
	public static TablePanel getInstance() {
		if (instance == null) 
			instance = new TablePanel();
		return instance;
	}

	public JTable getTable() {
		return table;
	}
}
