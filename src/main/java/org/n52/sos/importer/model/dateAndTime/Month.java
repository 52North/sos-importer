package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.dateAndTime.MissingMonthPanel;

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
	
	@Override
	public String toString() {
		return "Month" + super.toString();
	}
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingMonthPanel((DateAndTime)c);
	}
}
