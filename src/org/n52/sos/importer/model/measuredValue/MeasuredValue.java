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
			RegisterSensor rs = new RegisterSensor();
			InsertObservation io = new InsertObservation();
			
			//the cell of the current Measured Value
			Cell c = new Cell(i, column.getNumber());
			String value = TableController.getInstance().getValueAt(c);
			String parsedValue = parse(value).toString();
			io.setValue(parsedValue);
			
			//when was the current Measured Value measured
			dtc.setDateAndTime(getDateAndTime());
			GregorianCalendar gc = dtc.forThis(c);	
			Format formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
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
	
	//public abstract String parse(String s);
}
