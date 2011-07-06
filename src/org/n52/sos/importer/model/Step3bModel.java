package org.n52.sos.importer.model;

import java.util.HashMap;
import java.util.List;

public class Step3bModel {
	
	private int selectedRow;
	
	private final HashMap<Integer, List<String>> rowStore = new HashMap<Integer, List<String>>();

	public List<String> getRowFromStore(int row) {
		return rowStore.get(row);
	}
	
	public void putRowIntoStore(int row, List<String> selection) {
		rowStore.put(row, selection);
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}

	public int getSelectedRow() {
		return selectedRow;
	}

}
