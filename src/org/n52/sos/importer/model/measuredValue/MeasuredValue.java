package org.n52.sos.importer.model.measuredValue;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.n52.sos.importer.Parseable;
import org.n52.sos.importer.controller.DateAndTimeController;
import org.n52.sos.importer.controller.TableController;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.requests.InsertObservation;
import org.n52.sos.importer.model.requests.RegisterSensor;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Column;
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
		this.featureOfInterest = featureOfInterest;
		logger.info("Assign " + featureOfInterest + " to " + this);
	}

	public FeatureOfInterest getFeatureOfInterest() {
		return featureOfInterest;
	}

	public void setObservedProperty(ObservedProperty observedProperty) {
		this.observedProperty = observedProperty;
		logger.info("Assign " + observedProperty + " to " + this);
	}

	public ObservedProperty getObservedProperty() {
		return observedProperty;
	}

	public void setUnitOfMeasurement(UnitOfMeasurement unitOfMeasurement) {
		this.unitOfMeasurement = unitOfMeasurement;
		logger.info("Assign " + unitOfMeasurement + " to " + this);
	}

	public UnitOfMeasurement getUnitOfMeasurement() {
		return unitOfMeasurement;
	}

	public void setSensor(Sensor sensor) {
		logger.info("Assign " + sensor + " to " + this);
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
		this.dateAndTime = dateAndTime;
		logger.info("Assign " + dateAndTime + " to " + this);
	}

	public DateAndTime getDateAndTime() {
		return dateAndTime;
	}
	
	public void print() {
		Column column = (Column) getTableElement();
		DateAndTimeController dtc = new DateAndTimeController();
		
		for (int i = 0; i < TableController.getInstance().getRowCount(); i++) {
			RegisterSensor rs = new RegisterSensor();
			InsertObservation io = new InsertObservation();
			
			//the cell of the current Measured Value
			Cell c = new Cell(i, column.getNumber());
			String value = TableController.getInstance().getValueAt(c);
			try {
				String parsedValue = parse(value).toString();
				io.setValue(parsedValue);
			} catch (Exception e) {
				continue;
			}
			
			//when was the current Measured Value measured
			dtc.setDateAndTime(getDateAndTime());
			GregorianCalendar gc = dtc.forThis(c);	
			Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			String timeStamp = formatter.format(gc.getTime());
			io.setTimeStamp(timeStamp);
			
			FeatureOfInterest foi = getFeatureOfInterest().forThis(c);
			io.setFeatureOfInterestName(foi.getName());
			io.setFeatureOfInterestURI(foi.getURIString());
			
			//where was the current Measured Value measured
			Position p = foi.getPosition();
			io.setLatitudeValue(p.getLatitude().getValue() + "");
			io.setLongitudeValue(p.getLongitude().getValue() + "");
			io.setEpsgCode(p.getEPSGCode().getValue() + "");
			rs.setLatitudeValue(p.getLatitude().getValue() + "");
			rs.setLatitudeUnit(p.getLatitude().getUnit());
			rs.setLongitudeValue(p.getLongitude().getValue() + "");
			rs.setLongitudeUnit(p.getLongitude().getUnit());
			rs.setHeightValue(p.getHeight().getValue() + "");
			rs.setHeightUnit(p.getHeight().getUnit());
			rs.setEpsgCode(p.getEPSGCode().getValue() + "");
			
			ObservedProperty op = getObservedProperty().forThis(c);
			io.setObservedPropertyURI(op.getURIString());
			rs.setObservedPropertyName(op.getName());
			rs.setObservedPropertyURI(op.getURIString());
			
			UnitOfMeasurement uom = getUnitOfMeasurement().forThis(c);
			io.setUnitOfMeasurementCode(uom.getName());
			rs.setUnitOfMeasurementCode(uom.getName());
			
			Sensor sensor = getSensor().forThis(c);
			io.setSensorName(sensor.getName());
			io.setSensorURI(sensor.getURIString());
			rs.setSensorName(sensor.getName());
			rs.setSensorURI(sensor.getURIString());
			
			ModelStore.getInstance().addObservationToInsert(io);
			ModelStore.getInstance().addSensorToRegister(rs);
		}
	}
	
	@Override
	public String toString() {
		if (getTableElement() == null)
			return "";
		else
			return " " + getTableElement();
	}
	
	//public abstract String parse(String s);
}
