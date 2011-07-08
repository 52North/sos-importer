package org.n52.sos.importer.model;

import java.util.List;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;

public class Step4aModel {
	
	private List<MeasuredValue> selectedMeasuredValues; //TODO to be replaced by table model
	
	private DateAndTime dateAndTimeModel;
	
	public Step4aModel(DateAndTime dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}

	public void setDateAndTimeModel(DateAndTime dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}

	public DateAndTime getDateAndTimeModel() {
		return dateAndTimeModel;
	}


}
