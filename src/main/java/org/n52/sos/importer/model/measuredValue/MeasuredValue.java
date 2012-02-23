package org.n52.sos.importer.model.measuredValue;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.n52.sos.importer.interfaces.Parseable;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bSpecialModel;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.TableElement;

public abstract class MeasuredValue implements Parseable {
	
	private static final Logger logger = Logger.getLogger(MeasuredValue.class);
	
	private TableElement tableElement;
	
	private DateAndTime dateAndTime;
	
	private ObservedProperty observedProperty;
	
	private UnitOfMeasurement unitOfMeasurement;
	
	private FeatureOfInterest featureOfInterest;
	
	private Sensor sensor;
	
	public void setFeatureOfInterest(FeatureOfInterest featureOfInterest) {
		if (featureOfInterest == null)
			logger.info("Unassign Feature Of Interest from " + this);
		else
			logger.info("Assign " + featureOfInterest + " to " + this);
		this.featureOfInterest = featureOfInterest;
	}

	public FeatureOfInterest getFeatureOfInterest() {
		return featureOfInterest;
	}

	public void setObservedProperty(ObservedProperty observedProperty) {
		if (observedProperty != null)
			logger.info("Assign " + observedProperty + " to " + this);
		else 
			logger.info("Unassign Observed Property from " + this);
		this.observedProperty = observedProperty;
	}

	public ObservedProperty getObservedProperty() {
		return observedProperty;
	}

	public void setUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) {
		if (unitOfMeasurement != null)
			logger.info("Assign " + unitOfMeasurement + " to " + this);
		else 
			logger.info("Unassign Unit of Measurement from " + this);
		this.unitOfMeasurement = unitOfMeasurement;
	}

	public UnitOfMeasurement getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setSensor(Sensor sensor) {
		if (sensor != null)
			logger.info("Assign " + sensor + " to " + this);
		else 
			logger.info("Unassign Sensor from " + this);
		this.sensor = sensor;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setTableElement(TableElement tableElement) {
		logger.info("In " + tableElement + " are " + this + "s");
		this.tableElement = tableElement;
	}

	public TableElement getTableElement() {
		return tableElement;
	}

	public void setDateAndTime(DateAndTime dateAndTime) {
		if (dateAndTime != null)
			logger.info("Assign " + dateAndTime + " to " + this);
		else 
			logger.info("Unassign " + this.dateAndTime + " from " + this);
		this.dateAndTime = dateAndTime;
	}

	public DateAndTime getDateAndTime() {
		return dateAndTime;
	}
	
	/**
	 * returns a sensor for the given feature of interest name and observed property
	 * name; this method is called when a sensor has been assigned to them in
	 * step 6b (special)
	 */
	public Sensor getSensorFor(String featureOfInterestName, String observedPropertyName) {
		Iterator<Step6bSpecialModel> iterator = 
			ModelStore.getInstance().getStep6bSpecialModels().iterator();
		
		Step6bSpecialModel step6bSpecialModel;
		while (iterator.hasNext()) {
			step6bSpecialModel = iterator.next();
			if (step6bSpecialModel.getFeatureOfInterestName().equals(featureOfInterestName) &&
				step6bSpecialModel.getObservedPropertyName().equals(observedPropertyName))
				return step6bSpecialModel.getSensor();
		}
		
		//should never get here
		return null;
	}
	
	@Override
	public String toString() {
		if (getTableElement() == null)
			return "";
		else
			return " " + getTableElement();
	}
}
