package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;

public class Step3aModel {
	
	private final int selectedColumn;
	
	private List<String> selection;
	
	public Step3aModel(int selectedColumn) {
		this.selectedColumn = selectedColumn;
		selection = new ArrayList<String>();
		selection.add("Undefined");
	}

	public int getSelectedColumn() {
		return selectedColumn;
	}

	public void setSelection(List<String> selection) {
		this.selection = selection;
	}

	public List<String> getSelection() {
		return selection;
	}
}
