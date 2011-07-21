package org.n52.sos.importer.model;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;

public class Step4aModel {
	
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


}
