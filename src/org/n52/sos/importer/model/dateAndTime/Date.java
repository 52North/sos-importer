package org.n52.sos.importer.model.dateAndTime;

import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.view.dateAndTime.MissingDatePanel;

/**
 * aggregates year, month and day
 * @author Raimund
 */
public class Date extends Component {

	private Year year;
	
	private Month month;
	
	private Day day;

	public void setYear(Year year) {
		this.year = year;
	}

	public Year getYear() {		
		return year;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public Month getMonth() {
		return month;
	}

	public void setDay(Day day) {
		this.day = day;
	}

	public Day getDay() {
		return day;
	}

	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingDatePanel((DateAndTime)c);
	}
}
