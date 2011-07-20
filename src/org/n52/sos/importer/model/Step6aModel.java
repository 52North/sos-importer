package org.n52.sos.importer.model;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public class Step6aModel {
	
	private final String description = "<html>What is the <u>Date & Time</u> for " + 
	"all measured values?</html>";
	
	private DateAndTime dateAndTime;

	public Step6aModel(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}
	
	public void setDateAndTime(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public DateAndTime getDateAndTime() {
		return dateAndTime;
	}

	public String getDescription() {
		return description;
	}
}
