package org.n52.sos.importer.model;

public class Step3aModel {
	
	private final int selectedColumn;
	
	public Step3aModel(int selectedColumn) {
		this.selectedColumn = selectedColumn;
	}

	public int getSelectedColumn() {
		return selectedColumn;
	}
}
