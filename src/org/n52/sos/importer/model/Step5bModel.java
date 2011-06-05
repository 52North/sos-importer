package org.n52.sos.importer.model;

import java.awt.Color;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;

public class Step5bModel {

	private DateAndTimeController dateAndTimeController;
	
	private final Color markingColor;
	
	public Step5bModel() {
		markingColor = Color.green;
	}

	public Color getMarkingColor() {
		return markingColor;
	}

	public void setDateAndTimeController(DateAndTimeController dateAndTimeController) {
		this.dateAndTimeController = dateAndTimeController;
	}

	public DateAndTimeController getDateAndTimeController() {
		return dateAndTimeController;
	}
}
