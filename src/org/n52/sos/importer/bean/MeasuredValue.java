package org.n52.sos.importer.bean;

import org.n52.sos.importer.controller.dateAndTime.DateAndTimeController;
import org.n52.sos.importer.model.table.TableElement;

public class MeasuredValue  {
	
	private TableElement tableElement;
	
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

	public void setTableElement(TableElement tableElement) {
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}
}
