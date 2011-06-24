package org.n52.sos.importer.model.dateAndTime;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;

public abstract class DateAndTimeComponent {

	private static final Logger logger = Logger.getLogger(DateAndTimeComponent.class);
	
	private TableElement tableElement;
	
	private String pattern;
	
	private int value = -1;

	public DateAndTimeComponent(TableElement tableElement, String pattern) {
		this.tableElement = tableElement;
		this.pattern = pattern;
	}
	
	public DateAndTimeComponent(int value) {
		this.value = value;
	}
	
	public void setValue(int value) {
		logger.info("Assign Value to " + this.getClass().getName());
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setTableElement(TableElement tableElement) {
		logger.info("Assign Column to " + this.getClass().getName());
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
	
	public void mark(Color color) {
		if (tableElement != null)
			tableElement.mark(color);
	}
	
	public int getParsedValue(Cell measuredValuePosition) {
		if (tableElement == null)
			return getValue();
		else 
			return parse(tableElement.getValueFor(measuredValuePosition));
	}
	
	public int parse(String s) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		
        try {
        	date = formatter.parse(s);
		} catch (ParseException e) {
			return -1;
			//TODO throw new NumberFormatException();
		}
		
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		
		int value = gc.get(getGregorianCalendarField());
		return value;
	}
	
	public abstract int getGregorianCalendarField();
	
}
