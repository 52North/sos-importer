/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.model.table;

import java.util.HashSet;

import org.n52.sos.importer.controller.TableController;

/**
 * represents a single cell in the table
 * @author Raimund
 *
 */
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
		return "cell[" + row + "|" + column + "]";
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
		if (!(obj instanceof Cell))
			return false;
		Cell other = (Cell) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

}
