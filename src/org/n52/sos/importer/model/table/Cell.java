package org.n52.sos.importer.model.table;

import java.awt.Color;
import java.util.HashSet;

import org.n52.sos.importer.controller.TableController;

public class Cell extends TableElement {
	
	private int column;
	
	private int row;
	
	public Cell() {
		column = -1;
		row = -1;
	}
	
	public Cell(int row, int column) {
		setRow(row);
		setColumn(column);
	}
	
	public void setColumn(int column) {
		this.column = column;
	}

	public int getColumn() {
		return column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getRow() {
		return row;
	}

	public void mark(Color color) {
		TableController.getInstance().colorCell(color, this);
	}
	
	@Override
	public String getValueFor(Cell c) {
		return TableController.getInstance().getValueAt(this);
	}

	@Override
	public Cell getCellFor(Cell c) {
		return this;
	}

	@Override
	public HashSet<String> getValues() {
		HashSet<String> values = new HashSet<String>();
		String value = TableController.getInstance().getValueAt(this);
		values.add(value);
		return values;
	}
	
	@Override
	public String toString() {
		return "cell "+row+"|"+column;
	}
}
