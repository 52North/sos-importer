package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.List;

import org.n52.sos.importer.interfaces.Component;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public class Step6aModel {
	
	private final String description = "<html>What is the <u>Date & Time</u> for " + 
	"all measured values?</html>";
	
	private DateAndTime dateAndTime;

	private List<Component> missingDateAndTimeComponents;
	
	public Step6aModel(DateAndTime dateAndTime) {
		this.dateAndTime = dateAndTime;
		missingDateAndTimeComponents = new ArrayList<Component>();
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

	public void setMissingDateAndTimeComponents(
			List<Component> missingDateAndTimeComponents) {
		this.missingDateAndTimeComponents = missingDateAndTimeComponents;
	}

	public List<Component> getMissingDateAndTimeComponents() {
		return missingDateAndTimeComponents;
	}
}
