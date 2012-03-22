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
package org.n52.sos.importer.model.position;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;

public abstract class PositionComponent extends Component {

	private static final Logger logger = Logger.getLogger(PositionComponent.class);
	
	private TableElement tableElement;
	
	private String pattern;
	
	private double value = -1;
	
	private String unit = null;

	public PositionComponent(TableElement tableElement, String pattern) {
		this.tableElement = tableElement;
		this.pattern = pattern;
	}
	
	public PositionComponent(double value, String unit) {
		this.value = value;
		this.unit = unit;
	}

	public void setValue(double value) {
		logger.info("Assign Value to " + this.getClass().getName());
		this.value = value;
	}

	public double getValue() {
		return value;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setTableElement(TableElement tableElement) {
		logger.info("Assign Column to " + this.getClass().getName());
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
	
	/**
	 * colors this particular component
	 */
	public void mark() {
		if (tableElement != null)
			tableElement.mark();
	}

	public String getParsedUnit() {
		if (unit == null || unit.equals(""))
			return "n/a";
		else if (unit.equals("m") || unit.equals("meters")) 
			return "m";
		else if (unit.equals("ft") || unit.equals("feet"))
			return "ft";
		else if (unit.equals("degree") || unit.equals("°"))
			return "degree";
		return "n/a";
	}
	
	@Override 
	public String toString() {
		if (getTableElement() == null)
			return " " + getValue() + getUnit();
		else 
			return " " + getTableElement();
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}
	
	/**
	 * returns the corresponding position component for a feature of interest cell
	 */
	public abstract PositionComponent forThis(Cell featureOfInterestPosition);
}
