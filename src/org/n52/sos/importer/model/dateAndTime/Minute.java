package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.dateAndTime.MissingMinutePanel;

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
	
	@Override
	public String toString() {
		return "Minute" + super.toString();
	}
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingMinutePanel((DateAndTime)c);
	}
}
