package org.n52.sos.importer.model.table;

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

	public void mark() {
		TableController.getInstance().mark(this);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
