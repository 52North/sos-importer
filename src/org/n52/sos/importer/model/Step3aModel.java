package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;

public class Step3aModel {
	
	private final int markedColumn;
	
	private List<String> selection;
	
	public Step3aModel(int markedColumn) {
		this.markedColumn = markedColumn;
		selection = new ArrayList<String>();
		selection.add("Undefined");
	}

	public int getMarkedColumn() {
		return markedColumn;
	}

	/**
	 * saves the current selection of the radio button panel
	 */
	public void setSelection(List<String> selection) {
		this.selection = selection;
	}

	/**
	 * returns the saved selection of the radio button panel
	 */
	public List<String> getSelection() {
		return selection;
	}
}
