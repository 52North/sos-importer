package org.n52.sos.importer.model;

import java.awt.Color;

import org.n52.sos.importer.model.dateAndTime.DateAndTimeModel;

public class Step5bModel {

	private DateAndTimeModel dateAndTimeModel;
	
	private final Color markingColor;
	
	public Step5bModel(DateAndTimeModel dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
		markingColor = Color.green;
	}

	public Color getMarkingColor() {
		return markingColor;
	}

	public void setDateAndTimeModel(DateAndTimeModel dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}

	public DateAndTimeModel getDateAndTimeModel() {
		return dateAndTimeModel;
	}

}
