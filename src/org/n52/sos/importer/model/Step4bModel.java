package org.n52.sos.importer.model;

import java.awt.Color;
import java.util.List;

import org.n52.sos.importer.bean.MeasuredValue;
import org.n52.sos.importer.model.dateAndTime.DateAndTimeModel;

public class Step4bModel {
	
	private List<MeasuredValue> selectedMeasuredValues; //to be replaced by table model
	
	private DateAndTimeModel dateAndTimeModel;
	
	private final Color markingColor;
	
	public Step4bModel(DateAndTimeModel dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
		markingColor = Color.yellow;
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
