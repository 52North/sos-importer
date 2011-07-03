package org.n52.sos.importer.model.measuredValue;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.requests.InsertObservation;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
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
	
	private Sensor sensor;
	
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

	public void setSensor(Sensor sensor) {
		logger.info("Assign Sensor Name to Measured Value");
		this.sensor = sensor;
	}

	public Sensor getSensor() {
		return sensor;
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
		Column column = (Column) getTableElement();
		DateAndTimeController dtc = new DateAndTimeController();
		
		for (int i = 0; i < TableController.getInstance().getRowCount(); i++) {
			//StringBuilder sb = new StringBuilder();
			InsertObservation io = new InsertObservation();
			Cell c = new Cell(i, column.getNumber());
			String value = TableController.getInstance().getValueAt(c);
			io.setValue(value);
			//sb.append(this + "[value=" + value + "] -> ");
			//TODO String parsedValue = parse(value);
			dtc.setDateAndTime(getDateAndTime());
			GregorianCalendar gc = dtc.forThis(c);	
			Format formatter = new SimpleDateFormat("yyyy-MM-ddThh:mm:ssZ");
			String timeStamp = formatter.format(gc.getTime());
			io.setTimeStamp(timeStamp);
			
			FeatureOfInterest foi = getFeatureOfInterest().forThis(c);
			io.setFeatureOfInterestName(foi.getName());
			io.setFeatureOfInterestURI(foi.getURIString());
			io.setLatitude(foi.getPosition().getLatitude().getValue());
			io.setLongitude(foi.getPosition().getLongitude().getValue());
			io.setEpsgCode(foi.getPosition().getEPSGCode() + "");
			
			ObservedProperty op = getObservedProperty().forThis(c);
			io.setObservedPropertyURI(op.getURIString());
			UnitOfMeasurement uom = getUnitOfMeasurement().forThis(c);
			io.setUnitOfMeasurementCode(uom.getName());
			Sensor sensor = getSensor().forThis(c);
			io.setSensorName(sensor.getName());
			io.setSensorURI(sensor.getURIString());
			//logger.info(sb.toString());
		}
	}
	
	//public abstract String parse(String s);
}
