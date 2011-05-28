package org.n52.sos.importer.bean;

public class MeasuredValue extends TableConnection {
	
	private String type;
	
	public MeasuredValue(String type) {
		this.type = type;
	}
	
	public void setFeatureOfInterest(FeatureOfInterest featureOfInterest) {
		this.featureOfInterest = featureOfInterest;
	}

	public FeatureOfInterest getFeatureOfInterest() {
		return featureOfInterest;
	}

	private DateAndTime dateAndTime;
	
	private ObservedProperty observedProperty;
	
	private UnitOfMeasurement unitOfMeasurement;
	
	private FeatureOfInterest featureOfInterest;
	
	private SensorName sensorName;
}
