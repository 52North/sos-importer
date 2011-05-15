package org.n52.sos.importer.bean;

public class MeasurementRow {
	
	private int rowNumber;
	
	private int measurementType;
	
	private DateAndTimeColumn dateAndTimeColumn;
	
	private ObservedPropertyColumn observedPropertyColumn;
	private ObservedPropertyRow observedPropertyRow;
	
	private UnitOfMeasurementColumn unitOfMeasurementColumn;
	private UnitOfMeasurementRow unitOfMeasurementRow;
	
	private FeatureOfInterestColumn featureOfInterestColumn;
	private FeatureOfInterestRow featureOfInterestRow;
	
	private SensorNameColumn sensorNameColumn;
	private SensorNameRow sensorNameRow;
}
