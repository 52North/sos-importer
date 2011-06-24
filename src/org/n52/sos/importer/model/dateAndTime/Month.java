package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.model.table.TableElement;

public class Month extends DateAndTimeComponent {
	
	public Month(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Month(int value) {
		super(value);
	}

	@Override
	public int getGregorianCalendarField() {
		return GregorianCalendar.MONTH;
	}
}
