package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.dateAndTime.MissingSecondPanel;

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
	
	@Override
	public String toString() {
		return "Second" + super.toString();
	}
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingSecondPanel((DateAndTime)c);
	}
}
