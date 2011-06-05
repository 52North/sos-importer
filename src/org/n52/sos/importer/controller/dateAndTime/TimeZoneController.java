package org.n52.sos.importer.controller.dateAndTime;

import java.util.List;

import javax.swing.JPanel;

import org.n52.sos.importer.model.dateAndTime.TimeZoneModel;
import org.n52.sos.importer.view.dateAndTime.MissingTimeZonePanel;

public class TimeZoneController extends DateAndTimeComponentController {

	private TimeZoneModel timeZoneModel;
	
	public TimeZoneController() {
		timeZoneModel = new TimeZoneModel(-13);
	}
	
	public TimeZoneModel getTimeZoneModel() {
		return timeZoneModel;
	}

	public void setTimeZoneModel(TimeZoneModel timeZoneModel) {
		this.timeZoneModel = timeZoneModel;
	}

	@Override
	public void getMissingComponents(List<JPanel> missingComponents) {
		if (timeZoneModel.getUTCOffset() == -13)
			missingComponents.add(new MissingTimeZonePanel());
	}

}
