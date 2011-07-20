package org.n52.sos.importer.model.table;

import java.awt.Color;
import java.util.HashSet;

import org.n52.sos.importer.controller.TableController;

public class Column extends TableElement {
	
	private int number = -1;

	public Column(int number) {
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

	@Override
	public String getValueFor(Cell c) {
		return TableController.getInstance().getValueAt(c.getRow(), this.getNumber());
	}
	
	@Override
	public Cell getCellFor(Cell c) {
		return new Cell(c.getRow(), this.getNumber());
	}
	
	@Override
	public HashSet<String> getValues() {
		HashSet<String> values = new HashSet<String>();
		for (int i = 0; i < TableController.getInstance().getRowCount(); i++) {
			String value = TableController.getInstance().getValueAt(i, this.getNumber());
			values.add(value);
		}
		return values;
	}
	
	@Override
	public String toString() {
		return "column "+number;
	}
}
