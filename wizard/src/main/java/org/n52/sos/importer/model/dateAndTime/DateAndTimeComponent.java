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
package org.n52.sos.importer.model.dateAndTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Component;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DateAndTimeComponent extends Component {

	private static final Logger logger = LoggerFactory.getLogger(DateAndTimeComponent.class);
	
	private TableElement tableElement;
	
	private String pattern;
	
	private int value = Constants.NO_INPUT_INT;

	public DateAndTimeComponent(final TableElement tableElement, final String pattern) {
		this.tableElement = tableElement;
		this.pattern = pattern;
	}
	
	public DateAndTimeComponent(final int value) {
		this.value = value;
	}
	
	public void setValue(final int value) {
		logger.info("Assign Value to " + this.getClass().getName());
		this.value = value;
	}

	/**
	 * @return the value of this DateAndTimeComponent, or Integer.MIN_VALUE
	 */
	public int getValue() {
		return value;
	}

	public void setTableElement(final TableElement tableElement) {
		logger.info("Assign Column to " + this.getClass().getName());
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}

	/**
	 * Colours the particular date&time component
	 */
	public void mark() {
		if (tableElement != null) {
			tableElement.mark();
		}
	}
	
	/**
	 * Returns either the manually set value or 
	 * the value of this component in the table
	 * @throws ParseException 
	 */
	public int getParsedValue(final Cell measuredValuePosition) throws ParseException {
		if (tableElement == null) {
			return getValue();
		} else {
			return parse(tableElement.getValueFor(measuredValuePosition));
		}
	}
	
	/**
	 * Converts a String along a given pattern into the value of this component
	 * @param s
	 * @return
	 * @throws ParseException 
	 */
	public int parse(final String s) throws ParseException {
		Date date = null;
		final SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		
        try {
        	date = formatter.parse(s);
		} catch (final ParseException e) {
			logger.error("Given String could not be parsed: " + s, e);
			throw e;
		}
		
		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		
		final int value = gc.get(getGregorianCalendarField());
		return value;
	}
	
	/**
	 * Returns the corresponding Gregorian calendar field 
	 * for this component
	 * @return
	 */
	public abstract int getGregorianCalendarField();
	
	@Override
	public String toString() {
		if (getTableElement() == null) {
			return " '" + getValue() + "'";
		} else {
			return " " + getTableElement();
		}
	}
	
}
