package org.n52.sos.importer.model.dateAndTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;

public abstract class DateAndTimeComponent extends Component {

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

	/**
	 * colors the particular date&time component
	 */
	public void mark() {
		if (tableElement != null)
			tableElement.mark();
	}
	
	/**
	 * returns either the manually set value or 
	 * the value of this component in the table
	 */
	public int getParsedValue(Cell measuredValuePosition) {
		if (tableElement == null)
			return getValue();
		else 
			return parse(tableElement.getValueFor(measuredValuePosition));
	}
	
	/**
	 * converts a String along a given pattern into the value of this component
	 * @param s
	 * @return
	 */
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
	
	/**
	 * returns the corresponding Gregorian calendar field 
	 * for this component
	 * @return
	 */
	public abstract int getGregorianCalendarField();
	
	@Override
	public String toString() {
		if (getTableElement() == null)
			return " '" + getValue() + "'";
		else 
			return " " + getTableElement();
	}
	
}
