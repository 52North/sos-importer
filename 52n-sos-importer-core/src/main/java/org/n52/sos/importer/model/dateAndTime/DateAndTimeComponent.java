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
