package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public class Step5aModel {

	private final String description = 
		"Complete missing information for the marked date and time.";
	
	private DateAndTime dateAndTime;
	
	private List<Component> missingDateAndTimeComponents;
	
	public Step5aModel(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
		missingDateAndTimeComponents = new ArrayList<Component>();
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

	public void setMissingDateAndTimeComponents(List<Component> missingDateAndTimeComponents) {
		this.missingDateAndTimeComponents = missingDateAndTimeComponents;
	}

	public List<Component> getMissingDateAndTimeComponents() {
		return missingDateAndTimeComponents;
	}

}
