package org.n52.sos.importer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Step3Model {
	
	private final HashMap<Integer, List<String>> columnStore = new HashMap<Integer, List<String>>();
	private final HashMap<Integer, List<String>> rowStore = new HashMap<Integer, List<String>>();
	
	private int selectedColumn;
	
	private int selectedRow;
	
	public List<String> getColumnFromStore(int column) {
		return columnStore.get(column);
	}
	
	public void putColumnIntoStore(int column, List<String> selection) {
		columnStore.put(column, selection);
	}
	
	public Set<Integer> getStoredColumns() {
		return columnStore.keySet();
	}
	
	public List<String> getRowFromStore(int row) {
		return rowStore.get(row);
	}
	
	public void putRowIntoStore(int row, List<String> selection) {
		rowStore.put(row, selection);
	}

	/**
	 * @param selectedColumn the selectedColumn to set
	 */
	public void setSelectedColumn(int selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	/**
	 * @return the selectedColumn
	 */
	public int getSelectedColumn() {
		return selectedColumn;
	}

	/**
	 * @param selectedRow the selectedRow to set
	 */
	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}

	/**
	 * @return the selectedRow
	 */
	public int getSelectedRow() {
		return selectedRow;
	}

}
