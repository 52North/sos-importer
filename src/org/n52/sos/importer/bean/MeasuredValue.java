package org.n52.sos.importer.bean;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;

public class MeasuredValue extends TableConnection {
	
	private String type;
	
	private DateAndTimeController dateAndTime;
	
	private ObservedProperty observedProperty;
	
	private UnitOfMeasurement unitOfMeasurement;
	
	private FeatureOfInterest featureOfInterest;
	
	private SensorName sensorName;
	
	public MeasuredValue(String type) {
		this.type = type;
	}
	
	public void setFeatureOfInterest(FeatureOfInterest featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
	}

	public FeatureOfInterest getFeatureOfInterest() {
		return featureOfInterest;
	}

	public void setObservedProperty(ObservedProperty observedProperty) {
		this.observedProperty = observedProperty;
	}

	public ObservedProperty getObservedProperty() {
		return observedProperty;
	}

	public void setUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public UnitOfMeasurement getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setSensorName(SensorName sensorName) {
		this.sensorName = sensorName;
	}

	public SensorName getSensorName() {
		return sensorName;
	}

	public Resource getMissingResource() {
		if (featureOfInterest == null) return new FeatureOfInterest();
		if (observedProperty == null) return new ObservedProperty();
		if (unitOfMeasurement == null) return new UnitOfMeasurement();
		if (sensorName == null) return new SensorName();
		return null;
	}
	

}
