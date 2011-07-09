package org.n52.sos.importer.model;

import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.Resource;

public class Step6bModel {
	
	private MeasuredValue measuredValue;
	
	private Resource resource;

	public Step6bModel(MeasuredValue measuredValue, Resource resource) {
		setMeasuredValue(measuredValue);
		setResource(resource);
	}
	
	public void setMeasuredValue(MeasuredValue measuredValue) {
		this.measuredValue = measuredValue;
	}

	public MeasuredValue getMeasuredValue() {
		return measuredValue;
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Resource getResource() {
		return resource;
	}
}
