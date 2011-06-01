package org.n52.sos.importer.controller;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.n52.sos.importer.view.TablePanel;

public class TableController {
	
	private static TableController instance = null;
	
	public static final int COLUMNS = 1;
	public static final int ROWS = 2;
	public static final int CELLS = 3;
	
	private TablePanel tableView;
	
	private JTable table;
	
	private SingleSelectionListener singleSelectionListener;
	
	private MultipleSelectionListener multipleSelectionListener;

	private TableController() {
		tableView = TablePanel.getInstance();
		table = tableView.getTable();
		table.getSelectionModel().addListSelectionListener(new RowSelectionListener());
		table.getColumnModel().getSelectionModel()
		    .addListSelectionListener(new ColumnSelectionListener());
	}

	public static TableController getInstance() {
		if (instance == null)
			instance = new TableController();
		return instance;
	}	
	
	public void setContent(Object[][] content) {
		DefaultTableModel dtm = new EditableTableModel(false);
		int columns = content.length;
		for (int i = 0; i < columns; i++) {
			dtm.addColumn("", content[i]);
		}
		table.setModel(dtm);
	}
	
	public void allowSingleSelection() {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void allowMultipleSelection() {
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	
	public void allowRowSelection() {
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(true);
	}
	
	public void allowColumnSelection() {
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(false);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(false);
	}
	
	public void allowCellSelection() {
		table.setColumnSelectionAllowed(true);
		table.setRowSelectionAllowed(true);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
	}
	
	public void selectColumn(int number) {
		table.addColumnSelectionInterval(number, number);
	}
	
	public void selectRow(int number) {
		table.addRowSelectionInterval(number, number);
	}
	
	public void deselectColumn(int number) {
		table.removeColumnSelectionInterval(number, number);
	}
	
	public void deselectRow(int number) {
		table.removeRowSelectionInterval(number, number);
	}
	
	public int[] getSelectedColumns() {
		return table.getSelectedColumns();
	}
	
	public int getSelectedColumn() {
		return table.getSelectedColumn();
	}
	
	public int[] getSelectedRows() {
		return table.getSelectedRows();
	}
	
	public int getSelectedRow() {
		return table.getSelectedRow();
	}
	
	public List<String> getSelectedValues() {
		ArrayList<String> values = new ArrayList<String>();
		
		int[] columns = table.getSelectedColumns();
		int rowCount = table.getRowCount();
		
		for (int c: columns) {
			for (int i = 0; i < rowCount; i++)
				values.add((String)table.getValueAt(i, c));
		}
		
		int[] rows = table.getSelectedRows();
		int columnCount = table.getRowCount();
		
		for (int r: rows) {
			for (int i = 0; i < columnCount; i++)
				values.add((String)table.getValueAt(r, i));
		}
		
		return values;
	}
	
	public String getSelectedCellValue() {
		int column = table.getSelectedColumn();
		int row = table.getSelectedRow();
		return (String)table.getValueAt(row, column);
	}
	
	public void colorColumn(Color color, int number) {
		table.setDefaultRenderer(Object.class, new ColoredTableCellRenderer(color, number, -1));
	}
	
	public void colorRow(Color color, int number) {
		table.setDefaultRenderer(Object.class, new ColoredTableCellRenderer(color, -1, number));
	}

	public void addSingleSelectionListener(SingleSelectionListener singleSelectionListener) {
		this.singleSelectionListener = singleSelectionListener;
	}	
	
	public void addMultipleSelectionListener(MultipleSelectionListener multipleSelectionListener) {
		this.multipleSelectionListener = multipleSelectionListener;
	}
	
	private class ColoredTableCellRenderer extends DefaultTableCellRenderer {
    
		private static final long serialVersionUID = 1L;

		private Color color;
		
		private int columnToColor;
		
		private int rowToColor;
		
		public ColoredTableCellRenderer(Color color, int columnToColor, int rowToColor) {
			this.color = color;
			this.columnToColor = columnToColor;
			this.rowToColor = rowToColor;
		}
		
		@Override
		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column) {
			setEnabled(table == null || table.isEnabled());

	        if (row == rowToColor) setBackground(color);
	        else if (column == columnToColor) setBackground(color);
	        else setBackground(null);
	        
	        super.getTableCellRendererComponent(table, value, selected, focused, row, column);
	        return this;
		}
	}
	
	private class EditableTableModel extends DefaultTableModel {
		
		private static final long serialVersionUID = 1L;

		private boolean editable;
		
		public EditableTableModel(boolean editable) {
			super();
			this.editable = editable;
		}
	
		@Override
		public boolean isCellEditable(int row, int column) {
	        return editable;
	    }
	}
	
	private class ColumnSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			if (table.getColumnSelectionAllowed() && arg0.getValueIsAdjusting()) {
				if (table.getSelectionModel().getSelectionMode() == ListSelectionModel.SINGLE_SELECTION &&
						singleSelectionListener != null) 
					singleSelectionListener.columnSelectionChanged(table.getSelectedColumn());
				else if (table.getSelectionModel().getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION &&
						multipleSelectionListener != null) 
					multipleSelectionListener.columnSelectionChanged(table.getSelectedColumns());
			}
		}
	}
	
	private class RowSelectionListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			if (table.getRowSelectionAllowed() && arg0.getValueIsAdjusting()) {
				if (table.getSelectionModel().getSelectionMode() == ListSelectionModel.SINGLE_SELECTION &&
						singleSelectionListener != null) 
					singleSelectionListener.rowSelectionChanged(table.getSelectedRow());
				else if (table.getSelectionModel().getSelectionMode() == ListSelectionModel.MULTIPLE_INTERVAL_SELECTION &&
						multipleSelectionListener != null) 
					multipleSelectionListener.rowSelectionChanged(table.getSelectedRows());
			}
		}
	}
	
	public interface SingleSelectionListener {
		public void columnSelectionChanged(int selectedColumn);

		public void rowSelectionChanged(int selectedRow);
	}
	
	public interface MultipleSelectionListener {
		public void columnSelectionChanged(int[] selectedColumns);

		public void rowSelectionChanged(int[] selectedRows);
	}
}
