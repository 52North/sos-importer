/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PositionComponent extends Component {

	private static final Logger logger = LoggerFactory.getLogger(PositionComponent.class);
	
	private TableElement tableElement;
	
	private String pattern;
	
	private double value = -1;
	
	private String unit = null;

	public PositionComponent(final TableElement tableElement, final String pattern) {
		this.tableElement = tableElement;
		this.pattern = pattern;
	}
	
	public PositionComponent(final double value, final String unit) {
		this.value = value;
		this.unit = unit;
	}

	public void setValue(final double value) {
		logger.info("Assign Value to " + this.getClass().getName());
		this.value = value;
	}

	public double getValue() {
		return value;
	}
	
	public void setUnit(final String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void setTableElement(final TableElement tableElement) {
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
		if (tableElement != null) {
			tableElement.mark();
		}
	}

	public String getParsedUnit() {
		if (unit == null || unit.equals("")) {
			return "n/a";
		} else if (unit.equals("m") || unit.equals("meters")) {
			return "m";
		} else if (unit.equals("ft") || unit.equals("feet")) {
			return "ft";
		} else if (unit.equals("degree") || unit.equals("°")) {
			return "degree";
		}
		return "n/a";
	}
	
	@Override 
	public String toString() {
		if (getTableElement() == null) {
			return " " + getValue() + getUnit();
		} else {
			return " " + getTableElement();
		}
	}

	public void setPattern(final String pattern) {
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
