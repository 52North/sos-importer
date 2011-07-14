package org.n52.sos.importer.model;

import org.n52.sos.importer.model.resources.Resource;

public class Step4bModel {
	
	private final String description = 
		"Select all measured value ORIENTATIONs " +
		"where the marked RESOURCE ORIENTATION corresponds to.";
	
	private Resource resource;
	
	private int[] selectedRowsOrColumns;

	public Step4bModel(Resource resource) {
		this.resource = resource;
		this.selectedRowsOrColumns = new int[0];
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Resource getResource() {
		return resource;
	}

	public String getDescription() {
		return description;
	}

	public void setSelectedRowsOrColumns(int[] selectedRowsOrColumns) {
		this.selectedRowsOrColumns = selectedRowsOrColumns;
	}

	public int[] getSelectedRowsOrColumns() {
		return selectedRowsOrColumns;
	}
}
