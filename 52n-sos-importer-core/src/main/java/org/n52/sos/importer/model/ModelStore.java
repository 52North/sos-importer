/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */
package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.dateAndTime.DateAndTime;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.position.Position;
import org.n52.sos.importer.model.requests.InsertObservation;
import org.n52.sos.importer.model.requests.RegisterSensor;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.n52.sos.importer.model.table.TableElement;

/**
 * stores all identified and manually set metadata instances;
 * provides methods to add, retrieve and remove them
 * @author Raimund
 */
public class ModelStore {
	
	private static final Logger logger = Logger.getLogger(ModelStore.class);
	
	private static ModelStore instance = null;
	
	private List<MeasuredValue> measuredValues;
	
	private List<DateAndTime> dateAndTimes;
	
	private List<FeatureOfInterest> featureOfInterests;
	
	private List<ObservedProperty> observedProperties;

	private List<UnitOfMeasurement> unitOfMeasurements;
	
	private List<Sensor> sensors;
	
	private List<Position> positions;
	
	private HashSet<Step6bSpecialModel> step6bSpecialModels;
	
	private HashSet<InsertObservation> observationsToInsert;
	
	private HashSet<RegisterSensor> sensorsToRegister;
	
	private ModelStore() {
		measuredValues = new ArrayList<MeasuredValue>();
		dateAndTimes = new ArrayList<DateAndTime>();
		featureOfInterests = new ArrayList<FeatureOfInterest>();
		observedProperties = new ArrayList<ObservedProperty>();
		unitOfMeasurements = new ArrayList<UnitOfMeasurement>();
		sensors = new ArrayList<Sensor>();
		positions = new ArrayList<Position>();
		step6bSpecialModels = new HashSet<Step6bSpecialModel>();
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
		((ArrayList<MeasuredValue>) measuredValues).trimToSize();
		return measuredValues;
	}
	
	public MeasuredValue getMeasuredValueAt(TableElement tableElement) {
		for (MeasuredValue mv: measuredValues) {
			if (mv.getTableElement().equals(tableElement))
				return mv;
		}
		return null;
	}
	
	public void remove(MeasuredValue mv) {
		measuredValues.remove(mv);
	}
	
	public void add(DateAndTime dateAndTime) {
		dateAndTimes.add(dateAndTime);
	}
	
	public List<DateAndTime> getDateAndTimes() {
		((ArrayList<DateAndTime>) dateAndTimes).trimToSize();
		return dateAndTimes;
	}
	
	public void setDateAndTimes(List<DateAndTime> dateAndTimes) {
		this.dateAndTimes = dateAndTimes;
	}
	
	public void remove(DateAndTime dateAndTime) {
		dateAndTimes.remove(dateAndTime);
	}

	public void add(Resource resource) {
		if (resource instanceof FeatureOfInterest)
			add((FeatureOfInterest) resource);
		else if (resource instanceof ObservedProperty)
			add((ObservedProperty) resource);
		else if (resource instanceof UnitOfMeasurement)
			add((UnitOfMeasurement) resource);
		else if (resource instanceof Sensor)
			add((Sensor) resource);
	}
	
	public void remove(Resource resource) {
		if (resource instanceof FeatureOfInterest)
			remove((FeatureOfInterest) resource);
		else if (resource instanceof ObservedProperty)
			remove((ObservedProperty) resource);
		else if (resource instanceof UnitOfMeasurement)
			remove((UnitOfMeasurement) resource);
		else if (resource instanceof Sensor)
			remove((Sensor) resource);
	}
	
	public void add(FeatureOfInterest featureOfInterest) {
		featureOfInterests.add(featureOfInterest);
	}
	
	public void remove(FeatureOfInterest featureOfInterest) {
		featureOfInterests.remove(featureOfInterest);
	}

	public List<FeatureOfInterest> getFeatureOfInterests() {
		((ArrayList<FeatureOfInterest>) featureOfInterests).trimToSize();
		Object[] a = featureOfInterests.toArray(); 
		Arrays.sort(a);
		featureOfInterests = new ArrayList<FeatureOfInterest>(a.length);
		for (int i = 0; i < a.length; i++) {
			featureOfInterests.add((FeatureOfInterest) a[i]);
		}
		return featureOfInterests;
	}
	
	public void add(ObservedProperty observedProperty) {
		observedProperties.add(observedProperty);
	}
	
	public void remove(ObservedProperty observedProperty) {
		observedProperties.remove(observedProperty);
	}
	
	public List<ObservedProperty> getObservedProperties() {
		((ArrayList<ObservedProperty>) observedProperties).trimToSize();
		return observedProperties;
	}
		
	public void add(UnitOfMeasurement unitOfMeasurement) {
		unitOfMeasurements.add(unitOfMeasurement);
	}
	
	public void remove(UnitOfMeasurement unitOfMeasurement) {
		unitOfMeasurements.remove(unitOfMeasurement);
	}

	public List<UnitOfMeasurement> getUnitOfMeasurements() {
		((ArrayList<UnitOfMeasurement>) unitOfMeasurements).trimToSize();
		return unitOfMeasurements;
	}
	
	public void add(Sensor sensor) {
		sensors.add(sensor);
	}
	
	public void remove(Sensor sensor) {
		sensors.remove(sensor);
	}

	public List<Sensor> getSensors() {
		((ArrayList<Sensor>) sensors).trimToSize();
		return sensors;
	}
	
	public void add(Position position) {
		positions.add(position);
	}

	public List<Position> getPositions() {
		((ArrayList<Position>) positions).trimToSize();
		return positions;
	}
	
	public void setPositions(List<Position> positions) {
		this.positions = positions;
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
	
	public void clearSensorsToRegister() {
		sensorsToRegister.clear();
	}

	public void clearObservationsToInsert() {
		observationsToInsert.clear();
	}
	
	public void remove(Position position) {
		positions.remove(position);
	}
	
	public List<FeatureOfInterest> getFeatureOfInterestsInTable() {
		ArrayList<FeatureOfInterest> foisInTable = new ArrayList<FeatureOfInterest>();
		for (FeatureOfInterest foi: featureOfInterests) {
			if (foi.getTableElement() != null) 
				foisInTable.add(foi);
		}
		foisInTable.trimToSize();
		return foisInTable;
	}
	
	public List<Sensor> getSensorsInTable() {
		ArrayList<Sensor> sensorsInTable = new ArrayList<Sensor>();
		for (Sensor s: sensors) {
			if (s.getTableElement() != null) 
				sensorsInTable.add(s);
		}
		sensorsInTable.trimToSize();
		return sensorsInTable;
	}

	public List<ObservedProperty> getObservedPropertiesInTable() {
		ArrayList<ObservedProperty> opsInTable = new ArrayList<ObservedProperty>();
		for (ObservedProperty op: observedProperties) {
			if (op.getTableElement() != null) 
				opsInTable.add(op);
		}
		opsInTable.trimToSize();
		return opsInTable;
	}

	public void add(Step6bSpecialModel step6bSpecialModel) {
		logger.info("Assign " + step6bSpecialModel.getSensor() + " to Feature of Interest \"" +
				step6bSpecialModel.getFeatureOfInterest().getName() + "\" and Observed Property \"" +
				step6bSpecialModel.getObservedProperty().getName() + "\"");
		step6bSpecialModels.add(step6bSpecialModel);
	} 
	
	public void remove(Step6bSpecialModel step6bSpecialModel) {
		if (step6bSpecialModel.getSensor().getName() != null ||
				step6bSpecialModel.getSensor().getURI() != null)
			logger.info("Unassign " + step6bSpecialModel.getSensor() + " from Feature of Interest \"" +
				step6bSpecialModel.getFeatureOfInterest().getName() + " and Observed Property \"" +
				step6bSpecialModel.getObservedProperty().getName()+ "\"");
		step6bSpecialModels.remove(step6bSpecialModel);
	}

	public HashSet<Step6bSpecialModel> getStep6bSpecialModels() {
		return step6bSpecialModels;
	}
	
}
