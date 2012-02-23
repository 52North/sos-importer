package org.n52.sos.importer.model.dateAndTime;

import java.util.GregorianCalendar;

import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.dateAndTime.MissingYearPanel;

public class Year extends DateAndTimeComponent {

	public Year(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Year(int value) {
		super(value);
	}

	@Override
	public int getGregorianCalendarField() {
		return GregorianCalendar.YEAR;
	}
	
	@Override 
	public String toString() {
		return "Year" + super.toString();
	}
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingYearPanel((DateAndTime)c);
	}
}
