package org.n52.sos.importer;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TablePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	public static final int COLUMNS = 1;
	public static final int ROWS = 2;
	public static final int CELLS = 3;
	
	private int currentSelectionMode;
	
	private TableSelectionListener tableSelectionListener;
	
	private JTable table = new JTable();	
	
	public void setContent(Object[][] content) {
		removeAll();
		//initialize blank table headers
		int columns = content[0].length;
		String[] columnHeaders = new String[columns];
		for (int i = 0; i < columns; i++) {
			columnHeaders[i] = "";
		}
		table = new JTable(content, columnHeaders) {  
			private static final long serialVersionUID = 1L;
			//turn editing of cells off
			public boolean isCellEditable(int row, int col) {  
                return false;  
            }  
        };
        
        allowSingleSelection();
		setSelectionMode(TablePanel.COLUMNS);
		//select first column
		table.setColumnSelectionInterval(0, 0);
		
		SelectionListener listener = new SelectionListener(table);
		table.getSelectionModel().addListSelectionListener(listener);
		table.getColumnModel().getSelectionModel()
		    .addListSelectionListener(listener);
		
		table.setPreferredScrollableViewportSize(new Dimension(700, 150));
		
		JScrollPane scrollPane = new JScrollPane(table);
		this.add(scrollPane);	
	}
	
	public void setSelectionMode(int sm) {
		currentSelectionMode = sm;
		
		if (sm == COLUMNS) {
			table.setColumnSelectionAllowed(true);
			table.setRowSelectionAllowed(false);
			table.setShowVerticalLines(true);
			table.setShowHorizontalLines(false);
		} else if (sm == ROWS) {
			table.setColumnSelectionAllowed(false);
			table.setRowSelectionAllowed(true);
			table.setShowVerticalLines(false);
			table.setShowHorizontalLines(true);
		} else if (sm == CELLS) {
			table.setColumnSelectionAllowed(true);
			table.setRowSelectionAllowed(true);
			table.setShowVerticalLines(true);
			table.setShowHorizontalLines(true);
		}
	}
	
	public int getSelectionMode() {
		return currentSelectionMode;
	}
	
	public void allowSingleSelection() {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void allowMultipleSelection() {
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	
	public List<String> getSelectedValues() {
		//column selection
		//TODO
		ArrayList<String> values = new ArrayList<String>();
		int column = table.getSelectedColumn();
		int rows = table.getRowCount();
		for (int i = 0; i < rows; i++)
			values.add((String)table.getValueAt(i, column));
		return values;
	}
	
	public int[] getSelectedColumns() {
		return table.getSelectedColumns();
	}
	
	
	public void addSelectionListener(TableSelectionListener tableSelectionListner) {
		this.tableSelectionListener = tableSelectionListner;
	}
	
	
	public class SelectionListener implements ListSelectionListener {
	    JTable table;
	    int oldSelection;
	    // It is necessary to keep the table since it is not possible
	    // to determine the table from the event's source
	    SelectionListener(JTable table) {
	        this.table = table;
	    }
	    public void valueChanged(ListSelectionEvent e) {
	        // If cell selection is enabled, both row and column change events are fired
            // Row selection changed
	        if (e.getSource() == table.getSelectionModel()
	              && table.getRowSelectionAllowed()) {
				int newSelection = table.getSelectedRow();
				if (tableSelectionListener != null)
					tableSelectionListener.rowSelectionChanged(oldSelection, newSelection);
				oldSelection = newSelection;
	            
	        } else if (e.getSource() == table.getColumnModel().getSelectionModel()
	               && table.getColumnSelectionAllowed() ){
				int newSelection = table.getSelectedColumn();
				if (tableSelectionListener != null)
					tableSelectionListener.columnSelectionChanged(oldSelection, newSelection);
				oldSelection = newSelection;
	        }

	        if (e.getValueIsAdjusting()) {
	            // The mouse button has not yet been released
	        }
	    }
	}
}
