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
 * represents a column in the table
 * @author Raimund
 *
 */
public class Column extends TableElement {
	
	private int number = -1;
	private int firstLineWithData = -1;

	public Column(int number, int firstLineWithData) {
		this.number = number;
		this.firstLineWithData = firstLineWithData;
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
		for (int i = this.firstLineWithData; i < TableController.getInstance().getRowCount(); i++) {
			String value = TableController.getInstance().getValueAt(i, this.getNumber());
			if (value != null && !value.equalsIgnoreCase("")) {
				values.add(value);
			}
		}
		return values;
	}
	
	@Override
	public String toString() {
		return "column[#:" + number + "; fLWD:" + firstLineWithData + "]";
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
		if (!(obj instanceof Column))
			return false;
		Column other = (Column) obj;
		if (number != other.number)
			return false;
		return true;
	}

	/**
	 * @return firstLineWithData, -1 if not set
	 */
	public int getFirstLineWithData() {
		return firstLineWithData;
	}

	/**
	 * @param firstLineWithData the firstLineWithData to set
	 */
	public void setFirstLineWithData(int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
	}
}
