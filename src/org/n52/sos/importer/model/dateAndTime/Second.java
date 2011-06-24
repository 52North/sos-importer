package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.model.table.TableElement;

public class Second extends DateAndTimeComponent {

	public Second(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Second(int value) {
		super(value);
	}

	@Override
	public int getGregorianCalendarField() {
		return GregorianCalendar.SECOND;
	}
}
