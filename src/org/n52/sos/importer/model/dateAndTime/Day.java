package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.model.table.TableElement;

public class Day extends DateAndTimeComponent {

	public Day(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Day(int value) {
		super(value);
	}

	@Override
	public int getGregorianCalendarField() {
		return GregorianCalendar.DAY_OF_MONTH;
	}

	@Override
	public String toString() {
		return "Day" + super.toString();
	}
}
