package org.n52.sos.importer.model;

import org.n52.sos.importer.bean.Resource;

public class Step6aModel {
	
	private Resource resource;
	
	private int[] selectedColumns;

	public Step6aModel(Resource resource) {
		this.resource = resource;
		selectedColumns = new int[0];
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Resource getResource() {
		return resource;
	}

	public void setSelectedColumns(int[] selectedColumns) {
		this.selectedColumns = selectedColumns;
	}

	public int[] getSelectedColumns() {
		return selectedColumns;
	}
}
