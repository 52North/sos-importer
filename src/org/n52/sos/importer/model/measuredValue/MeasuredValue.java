package org.n52.sos.importer.model.measuredValue;

import org.apache.log4j.Logger;
import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.SensorName;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.model.table.TableElement;

public abstract class MeasuredValue  {
	
	private static final Logger logger = Logger.getLogger(MeasuredValue.class);
	
	private TableElement tableElement;
	
	private DateAndTime dateAndTime;
	
	private ObservedProperty observedProperty;
	
	private UnitOfMeasurement unitOfMeasurement;
	
	private FeatureOfInterest featureOfInterest;
	
	private SensorName sensorName;
	
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

	public void setDateAndTime(DateAndTime dateAndTime) {
		logger.info("Assign Date & Time to Measured Value");
		this.dateAndTime = dateAndTime;
	}

	public DateAndTime getDateAndTime() {
		return dateAndTime;
	}
	
	public void print() {
		Column cm = (Column) getTableElement();
		DateAndTimeController dtc = new DateAndTimeController();
		
		for (int i = 0; i < TableController.getInstance().getRowCount(); i++) {
			StringBuilder sb = new StringBuilder();
			Cell c = new Cell(i, cm.getNumber());
			String value = TableController.getInstance().getValueAt(c);
			sb.append(this + "[value=" + value + "] -> ");
			//String parsedValue = parse(value);
			dtc.setDateAndTime(getDateAndTime());
			sb.append(dtc.getParsed(c));
			sb.append(getObservedProperty().print(c));
			sb.append(getUnitOfMeasurement().print(c));
			sb.append(getSensorName().print(c));
			sb.append(getFeatureOfInterest().print(c));
			logger.info(sb.toString());
		}
	}
	
	//public abstract String parse(String s);
}
