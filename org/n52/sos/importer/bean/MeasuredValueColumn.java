package org.n52.sos.importer.bean;

public class MeasuredValueColumn extends TableConnection {
	
	private String type;
	
	public MeasuredValueColumn(String type) {
		this.type = type;
	}
	
	private DateAndTime dateAndTimeColumn;
	
	private ObservedProperty observedPropertyColumn;
	
	private UnitOfMeasurement unitOfMeasurementColumn;
	
	private FeatureOfInterest featureOfInterestColumn;
	
	private SensorName sensorNameColumn;
}
