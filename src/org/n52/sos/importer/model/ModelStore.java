package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.requests.InsertObservation;
import org.n52.sos.importer.model.requests.RegisterSensor;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.Column;

public class ModelStore {
	
	private static ModelStore instance = null;
	
	private List<MeasuredValue> measuredValues;
	
	private List<DateAndTime> dateAndTimes;
	
	private List<FeatureOfInterest> featureOfInterests;
	
	private List<ObservedProperty> observedProperties;

	private List<UnitOfMeasurement> unitOfMeasurements;
	
	private List<Sensor> sensors;
	
	private List<Position> positions;
	
	private HashSet<InsertObservation> observationsToInsert;
	
	private HashSet<RegisterSensor> sensorsToRegister;
	
	private ModelStore() {
		measuredValues = new ArrayList<MeasuredValue>();
		dateAndTimes = new ArrayList<DateAndTime>();
		featureOfInterests = new ArrayList<FeatureOfInterest>();
		observationsToInsert = new HashSet<InsertObservation>();
		sensorsToRegister = new HashSet<RegisterSensor>();
	}
	
	public static ModelStore getInstance() {
		if (instance == null)
			instance = new ModelStore();
		return instance;
	}
	
	public void add(MeasuredValue mv) {
		measuredValues.add(mv);
	}
	
	public List<MeasuredValue> getMeasuredValues() {
		return measuredValues;
	}
	
	public MeasuredValue getMeasuredValueAtColumn(int column) {
		for (MeasuredValue mv: measuredValues) {
			if (((Column)mv.getTableElement()).getNumber() == column)
				return mv;
		}
		return null;
	}
	
	public void add(DateAndTime dateAndTime) {
		dateAndTimes.add(dateAndTime);
	}
	
	public List<DateAndTime> getDateAndTimes() {
		return dateAndTimes;
	}

	public void add(FeatureOfInterest featureOfInterest) {
		featureOfInterests.add(featureOfInterest);
	}

	public List<FeatureOfInterest> getFeatureOfInterests() {
		return featureOfInterests;
	}
	
	public void add(ObservedProperty observedProperty) {
		observedProperties.add(observedProperty);
	}
	
	public List<ObservedProperty> getObservedProperties() {
		return observedProperties;
	}
		
	public void add(UnitOfMeasurement unitOfMeasurement) {
		unitOfMeasurements.add(unitOfMeasurement);
	}

	public List<UnitOfMeasurement> getUnitOfMeasurements() {
		return unitOfMeasurements;
	}
	
	public void add(Sensor sensor) {
		sensors.add(sensor);
	}

	public List<Sensor> getSensors() {
		return sensors;
	}
	
	public void add(Position position) {
		positions.add(position);
	}

	public List<Position> getPositions() {
		return positions;
	}
	
	public void addObservationToInsert(InsertObservation io) {
		observationsToInsert.add(io);
	}
	
	public void addSensorToRegister(RegisterSensor rs) {
		sensorsToRegister.add(rs);
	}
	
	public HashSet<RegisterSensor> getSensorsToRegister() {
		return sensorsToRegister;
	}

	public HashSet<InsertObservation> getObservationsToInsert() {
		return observationsToInsert;
	}
}
