package org.n52.sos.importer.model;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public class Step4aModel {
	
	private final String description = 
		"Select all measured value ORIENTATIONs " +
		"where the marked Date & Time group corresponds to.";
	
	private int[] selectedRowsOrColumns;
	
	private DateAndTime dateAndTimeModel;
	
	public Step4aModel(DateAndTime dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
		this.selectedRowsOrColumns = new int[0];
	}

	public void setDateAndTimeModel(DateAndTime dateAndTimeModel) {
		this.dateAndTimeModel = dateAndTimeModel;
	}

	public DateAndTime getDateAndTimeModel() {
		return dateAndTimeModel;
	}

	public void setSelectedRowsOrColumns(int[] selectedRowsOrColumns) {
		this.selectedRowsOrColumns = selectedRowsOrColumns;
	}

	public int[] getSelectedRowsOrColumns() {
		return selectedRowsOrColumns;
	}

	public String getDescription() {
		return description;
	}
}
