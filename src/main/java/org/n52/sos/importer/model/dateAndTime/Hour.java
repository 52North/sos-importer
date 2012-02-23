package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.dateAndTime.MissingHourPanel;

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
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingHourPanel((DateAndTime)c);
	}
}
