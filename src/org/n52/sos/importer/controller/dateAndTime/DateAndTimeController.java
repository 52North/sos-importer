package org.n52.sos.importer.controller.dateAndTime;

import java.util.List;

import javax.swing.JPanel;


public class DateAndTimeController {
	
	private TimeController timeController = new TimeController();
	private DateController dateController = new DateController();
	private TimeZoneController timeZoneController = new TimeZoneController();
	
	public void getMissingComponents(List<JPanel> missingComponents) {
		timeController.getMissingComponents(missingComponents);
		dateController.getMissingComponents(missingComponents);
		timeZoneController.getMissingComponents(missingComponents);
	}	
	
	public TimeController getTimeController() {
		return timeController;
	}

	public DateController getDateController() {
		return dateController;
	}

	public TimeZoneController getTimeZoneController() {
		return timeZoneController;
	}
}
