package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.model.table.TableElement;

public class TimeZone extends DateAndTimeComponent {

	public TimeZone(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public TimeZone(int value) {
		super(value);
	}

	@Override
	public int getGregorianCalendarField() {
		return GregorianCalendar.ZONE_OFFSET;
	}	
	
	@Override
	public String toString() {
		return "Timezone" + super.toString();
	}
}
