package org.n52.sos.importer.model.dateAndTime;

import org.n52.sos.importer.interfaces.Combination;
import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.interfaces.MissingComponentPanel;
import org.n52.sos.importer.view.dateAndTime.MissingTimePanel;

public class Time extends Component {

	private Hour hour;
	
	private Minute minute;
	
	private Second second;

	public void setHour(Hour hour) {
		this.hour = hour;
	}

	public Hour getHour() {
		return hour;
	}

	public void setMinute(Minute minute) {
		this.minute = minute;
	}

	public Minute getMinute() {
		return minute;
	}

	public void setSecond(Second second) {
		this.second = second;
	}

	public Second getSecond() {
		return second;
	}
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingTimePanel((DateAndTime)c);
	}
}
