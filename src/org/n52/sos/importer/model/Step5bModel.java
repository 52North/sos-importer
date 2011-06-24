package org.n52.sos.importer.model;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public class Step5bModel {

	private DateAndTime dateAndTimeModel;
	
	public Step5bModel(DateAndTime dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}

	public void setDateAndTimeModel(DateAndTime dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}

	public DateAndTime getDateAndTimeModel() {
		return dateAndTimeModel;
	}

}
