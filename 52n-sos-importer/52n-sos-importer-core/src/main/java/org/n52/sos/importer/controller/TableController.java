/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.controller;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.model.table.Row;
import org.n52.sos.importer.view.TablePanel;
import org.n52.sos.importer.view.i18n.Lang;

/**
 * provides all necessary functions for modifying the table
 * @author Raimund
 *
 */
public class TableController {
	
	private static TableController instance = null;
	
	public static final int COLUMNS = 1;
	public static final int ROWS = 2;
	public static final int CELLS = 3;
	
	private TablePanel tableView;
	
	private JTable table;
	
	private SingleSelectionListener singleSelectionListener;
	
	private MultipleSelectionListener multipleSelectionListener;
	
	private ColoredTableCellRenderer tableMarker;
	
	private int tableSelectionMode;
	
	private int orientation = COLUMNS;
	
	private static int firstLineWithData = -1;
	
	private final Color markingColor = Color.lightGray;

	private TableController(int firstLineWithData) {
		TableController.firstLineWithData = firstLineWithData;
		this.tableView = TablePanel.getInstance();
		this.table = tableView.getTable();
		this.tableMarker = new ColoredTableCellRenderer();
		//
		table.setDefaultRenderer(Object.class, null);
		table.setDefaultRenderer(Object.class, tableMarker);
		table.getSelectionModel().addListSelectionListener(new RowSelectionListener());
		table.getColumnModel().getSelectionModel()
		    .addListSelectionListener(new ColumnSelectionListener());
		this.allowMultipleSelection();
	}

	public static TableController getInstance() {
		if (instance == null)
			instance = new TableController(-1);
		return instance;
	}	
	
	public void setContent(Object[][] content) {
		DefaultTableModel dtm = new EditableTableModel(false);

		int rows = content.length;
		int columns = content[0].length;
		dtm.setColumnCount(columns);
		
		String[] columnIdentifiers = new String[columns];
		for (int i = 0; i < columnIdentifiers.length; i++)
			columnIdentifiers[i] = "n/a";
		dtm.setColumnIdentifiers(columnIdentifiers);

		for (int i = 0; i < rows; i++) {
			dtm.addRow(content[i]);
		}
		table.setModel(dtm);
	}
	
	public void setColumnHeading(int column, String heading) {
		table.getColumnModel().getColumn(column).setHeaderValue(heading);
	}
	
	public void allowSingleSelection() {
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	public void allowMultipleSelection() {
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}
	
	public void setTableSelectionMode(int tableSelectionMode) {
		this.tableSelectionMode = tableSelectionMode;
		
		switch(tableSelectionMode) {
		case ROWS:
			table.setColumnSelectionAllowed(false);
			table.setRowSelectionAllowed(true);
			table.setShowVerticalLines(false);
			table.setShowHorizontalLines(true);
			break;
		case COLUMNS:
			table.setColumnSelectionAllowed(true);
			table.setRowSelectionAllowed(false);
			table.setShowVerticalLines(true);
			table.setShowHorizontalLines(false);
			break;
		case CELLS:
			table.setColumnSelectionAllowed(true);
			table.setRowSelectionAllowed(true);
			table.setShowVerticalLines(true);
			table.setShowHorizontalLines(true);
			break;
		}
	}
	
	public int getTableSelectionMode() {
		return tableSelectionMode;
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
	
	public void deselectAllColumns() {
		int columns = table.getColumnCount() - 1;
		table.removeColumnSelectionInterval(0, columns);
	}
	
	public void deselectAllRows() {
		int rows = table.getRowCount() - 1;
		table.removeColumnSelectionInterval(0, rows);
	}
	
	public void turnSelectionOff() {
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(false);
		table.setCellSelectionEnabled(false);
		table.setFocusable(false);
	}
	
	public void turnSelectionOn() {
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setFocusable(true);
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
	
	public List<String> getMarkedValues() {
		ArrayList<String> values = new ArrayList<String>();
		
		switch(tableSelectionMode) {
		case COLUMNS:	
			int rowCount = table.getRowCount();
			
			for (Column c: tableMarker.getColumns())
				for (int i = 0; i < rowCount; i++)
					values.add((String)table.getValueAt(i, c.getNumber()));
		
			break;
		case ROWS:		
			int columnCount = table.getColumnCount();
			
			for (Row r: tableMarker.getRows())
				for (int i = 0; i < columnCount; i++)
					values.add((String)table.getValueAt(r.getNumber(), i));

			break;
		case CELLS:
			for (Cell c: tableMarker.getCells())
				values.add(getValueAt(c));
			break;
		}
		return values;
	}
	
	public String getSelectedCellValue() {
		int column = table.getSelectedColumn();
		int row = table.getSelectedRow();
		return (String)table.getValueAt(row, column);
	}
	
	public String getValueAt(Cell c) {
		return (String) table.getValueAt(c.getRow(), c.getColumn());
	}
	
	public String getValueAt(int row, int column) {
		return (String) table.getValueAt(row, column);
	}
	
	public int getRowCount() {
		return table.getRowCount();
	}
	
	public int getColumnCount() {
		return table.getColumnCount();
	}
	
	public void mark(Column c) {
		tableMarker.addColumn(c);
	}
	
	public void mark(Row r) {
		tableMarker.addRow(r);
	}
	
	public void mark(Cell c) {
		tableMarker.addCell(c);
	}
	
	public void clearMarkedTableElements() {
		tableMarker.removeTableElements();
	}

	public void addSingleSelectionListener(SingleSelectionListener singleSelectionListener) {
		this.singleSelectionListener = singleSelectionListener;
	}	
	
	public void addMultipleSelectionListener(MultipleSelectionListener multipleSelectionListener) {
		this.multipleSelectionListener = multipleSelectionListener;
	}
	
	public void removeMultipleSelectionListener() {
		this.multipleSelectionListener = null;
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getOrientation() {
		return orientation;
	}	
	
	public String getOrientationString() {
		switch(orientation) {
			case COLUMNS: return Lang.l().column(); //"column";
			case ROWS: return Lang.l().row(); //"row";
			default: return Lang.l().column(); // is default mode
		}
	}

	private class ColoredTableCellRenderer extends DefaultTableCellRenderer {
    
		private static final long serialVersionUID = 1L;
		
		private HashSet<Column> columns;
		
		private HashSet<Row> rows;
		
		private HashSet<Cell> cells;
		
		public ColoredTableCellRenderer() {
			columns = new HashSet<Column>();
			rows = new HashSet<Row>();
			cells = new HashSet<Cell>();
		}
		
		public void addColumn(Column c) {
			columns.add(c);
		}
		
		public HashSet<Column> getColumns() {
			return columns;
		}
		
		public void addRow(Row r) {
			rows.add(r);
		}
		
		public HashSet<Row> getRows() {
			return rows;
		}
		
		public void addCell(Cell c) {
			cells.add(c);
		}
		
		public HashSet<Cell> getCells() {
			return cells;
		}
		
		public void removeTableElements() {
			columns.clear();
			rows.clear();
			cells.clear();
		}
		
		@Override
		public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, int column) {
			setEnabled(table == null || table.isEnabled());
			
	        if (rows.contains(new Row(row)) || 
	        	columns.contains(new Column(column,firstLineWithData)) ||
	        	cells.contains(new Cell(row, column))) {
        		setBackground(markingColor);
	        }
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

	/**
	 * @return the firstLineWithData
	 */
	public int getFirstLineWithData() {
		return firstLineWithData;
	}

	/**
	 * @param firstLineWithData the firstLineWithData to set
	 */
	public void setFirstLineWithData(int firstLineWithData) {
		TableController.firstLineWithData = firstLineWithData;
	}
	
	public String[] getAllColumnHeadings(){
		return this.getColumnHeadingsFiltered(null,false);
	}
	
	public String[] getUsedColumnHeadings() {
		return this.getColumnHeadingsFiltered(Lang.l().step3ColTypeDoNotExport(),false);
	}
	
	public String[] getColumnHeadingsFiltered(String typeToLeaveOut, boolean withColId) {
		int colCount = this.table.getColumnCount();
		TableColumnModel tcm = this.table.getColumnModel();
		// Check for null and empty strings 
		boolean filter = (typeToLeaveOut == null? false:
				typeToLeaveOut.equalsIgnoreCase("")? false : true
				);
		ArrayList<String> headings = new ArrayList<String>(colCount);
		if (!filter) {
			for (int i = 0; i < colCount; i++) {
				String tmp = tcm.getColumn(i).getHeaderValue().toString();
				if (withColId) {
					tmp = i + ": " + tmp;
				}
				headings.add(tmp);
			}
		} else {
			for (int i = 0; i < colCount; i++) {
				String tmp = tcm.getColumn(i).getHeaderValue().toString();
				if (!tmp.equalsIgnoreCase(typeToLeaveOut)) {
					if (withColId) {
						tmp = i + ": " + tmp;
					}
					headings.add(tmp);
				}
			}
		}
		headings.trimToSize();		
		return headings.toArray(new String[headings.size()]);
	}

	public String[] getUsedColumnHeadingsWithId() {
		return this.getColumnHeadingsFiltered(Lang.l().step3ColTypeDoNotExport(),true);
	}
}
