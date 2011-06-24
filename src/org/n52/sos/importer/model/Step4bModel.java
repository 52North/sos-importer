package org.n52.sos.importer.model;

import java.util.List;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;

public class Step4bModel {
	
	private List<MeasuredValue> selectedMeasuredValues; //TODO to be replaced by table model
	
	private DateAndTime dateAndTimeModel;
	
	public Step4bModel(DateAndTime dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}

	public void setDateAndTimeModel(DateAndTime dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}

	public DateAndTime getDateAndTimeModel() {
		return dateAndTimeModel;
	}


}
