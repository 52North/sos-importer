package org.n52.sos.importer.model.table;

import java.awt.Color;

import org.n52.sos.importer.controller.TableController;

public class ColumnModel extends TableElement {
	
	private int number = -1;

	public ColumnModel(int number) {
		this.number = number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
	
	public void mark(Color color) {
		TableController.getInstance().colorColumn(color, number);
	}
}
