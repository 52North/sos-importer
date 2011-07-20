package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.model.table.TableElement;

public class Hour extends DateAndTimeComponent {
	
	public Hour(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Hour(int value) {
		super(value);
	}

	@Override
	public int getGregorianCalendarField() {
		return GregorianCalendar.HOUR_OF_DAY;
	}
	
	@Override
	public String toString() {
		return "Hour" + super.toString();
	}
}
