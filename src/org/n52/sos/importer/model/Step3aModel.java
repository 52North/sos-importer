package org.n52.sos.importer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Step3aModel {
	
	private final HashMap<Integer, List<String>> columnStore;
	
	private int selectedColumn;
	
	public Step3aModel() {
		selectedColumn = 0;
		columnStore = new HashMap<Integer, List<String>>();
	}
	
	public List<String> getColumnFromStore(int column) {
		return columnStore.get(column);
	}
	
	public void putColumnIntoStore(int column, List<String> selection) {
		columnStore.put(column, selection);
	}
	
	public Set<Integer> getStoredColumns() {
		return columnStore.keySet();
	}
	
	public void setSelectedColumn(int selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	public int getSelectedColumn() {
		return selectedColumn;
	}

}
