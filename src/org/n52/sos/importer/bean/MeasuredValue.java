package org.n52.sos.importer.bean;

import org.apache.log4j.Logger;
import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.dateAndTime.DateAndTimeModel;
import org.n52.sos.importer.model.table.TableElement;

public class MeasuredValue  {
	
	private static final Logger logger = Logger.getLogger(MeasuredValue.class);
	
	private TableElement tableElement;
	
	private String type;
	
	private DateAndTimeModel dateAndTimeModel;
	
	private ObservedProperty observedProperty;
	
	private UnitOfMeasurement unitOfMeasurement;
	
	private FeatureOfInterest featureOfInterest;
	
	private SensorName sensorName;
	
	public MeasuredValue(String type) {
		this.type = type;
	}
	
	public void setFeatureOfInterest(FeatureOfInterest featureOfInterest) {
		logger.info("Assign Feature Of Interest to Measured Value");
		this.featureOfInterest = featureOfInterest;
	}

	public FeatureOfInterest getFeatureOfInterest() {
		return featureOfInterest;
	}

	public void setObservedProperty(ObservedProperty observedProperty) {
		logger.info("Assign Observed Property to Measured Value");
		this.observedProperty = observedProperty;
	}

	public ObservedProperty getObservedProperty() {
		return observedProperty;
	}

	public void setUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) {
		logger.info("Assign Unit of Measurement to Measured Value");
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public UnitOfMeasurement getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setSensorName(SensorName sensorName) {
		logger.info("Assign Sensor Name to Measured Value");
		this.sensorName = sensorName;
	}

	public SensorName getSensorName() {
		return sensorName;
	}

	public void setTableElement(TableElement tableElement) {
		logger.info("Assign Column to Measured Value");
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}

	public void setDateAndTimeModel(DateAndTimeModel dateAndTimeModel) {
		logger.info("Assign Date & Time to Measured Value");
		this.dateAndTimeModel = dateAndTimeModel;
	}

	public DateAndTimeModel getDateAndTimeModel() {
		return dateAndTimeModel;
	}
}
