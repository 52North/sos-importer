package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.model.table.TableElement;

public class Minute extends DateAndTimeComponent {

	public Minute(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Minute(int value) {
		super(value);
	}

	@Override
	public int getGregorianCalendarField() {
		return GregorianCalendar.MINUTE;
	}
}
