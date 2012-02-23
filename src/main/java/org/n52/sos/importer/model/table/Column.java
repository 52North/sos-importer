package org.n52.sos.importer.model.table;

import java.util.HashSet;

import org.n52.sos.importer.controller.TableController;

/**
 * represents a column in the table
 * @author Raimund
 *
 */
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
	
	public void mark() {
		TableController.getInstance().mark(this);
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
			if (value != null && value != "")
				values.add(value);
		}
		return values;
	}
	
	@Override
	public String toString() {
		return "column "+number;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
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
		Column other = (Column) obj;
		if (number != other.number)
			return false;
		return true;
	}
}
