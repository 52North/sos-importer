package org.n52.sos.importer.model;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public class Step5aModel {

	private final String description = 
		"Complete missing information for the marked date and time.";
	
	private DateAndTime dateAndTime;
	
	public Step5aModel(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public void setDateAndTimeModel(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
	}

	public DateAndTime getDateAndTime() {
		return dateAndTime;
	}

	public String getDescription() {
		return description;
	}

}
