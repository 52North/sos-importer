/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * stores all identified and manually set metadata instances;
 * provides methods to add, retrieve and remove them
 * @author Raimund
 */
public class ModelStore {
	
	private static final Logger logger = LoggerFactory.getLogger(ModelStore.class);
	
	private static ModelStore instance = null;
	
	private final List<MeasuredValue> measuredValues;
	
	private List<DateAndTime> dateAndTimes;
	
	private List<FeatureOfInterest> featureOfInterests;
	
	private final List<ObservedProperty> observedProperties;

	private final List<UnitOfMeasurement> unitOfMeasurements;
	
	private final List<Sensor> sensors;
	
	private List<Position> positions;
	
	private final HashSet<Step6bSpecialModel> step6bSpecialModels;
	
	private final HashSet<InsertObservation> observationsToInsert;
	
	private final HashSet<RegisterSensor> sensorsToRegister;
	
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
		if (instance == null) {
			instance = new ModelStore();
		}
		return instance;
	}
	
	public void add(final MeasuredValue mv) {
		measuredValues.add(mv);
	}
	
	public List<MeasuredValue> getMeasuredValues() {
		((ArrayList<MeasuredValue>) measuredValues).trimToSize();
		return measuredValues;
	}
	
	public MeasuredValue getMeasuredValueAt(final TableElement tableElement) {
		for (final MeasuredValue mv: measuredValues) {
			if (mv.getTableElement().equals(tableElement)) {
				return mv;
			}
		}
		return null;
	}
	
	public void remove(final MeasuredValue mv) {
		measuredValues.remove(mv);
	}
	
	public void add(final DateAndTime dateAndTime) {
		dateAndTimes.add(dateAndTime);
	}
	
	public List<DateAndTime> getDateAndTimes() {
		((ArrayList<DateAndTime>) dateAndTimes).trimToSize();
		return dateAndTimes;
	}
	
	public void setDateAndTimes(final List<DateAndTime> dateAndTimes) {
		this.dateAndTimes = dateAndTimes;
	}
	
	public void remove(final DateAndTime dateAndTime) {
		dateAndTimes.remove(dateAndTime);
	}

	public void add(final Resource resource) {
		if (resource instanceof FeatureOfInterest) {
			add((FeatureOfInterest) resource);
		} else if (resource instanceof ObservedProperty) {
			add((ObservedProperty) resource);
		} else if (resource instanceof UnitOfMeasurement) {
			add((UnitOfMeasurement) resource);
		} else if (resource instanceof Sensor) {
			add((Sensor) resource);
		}
	}
	
	public void remove(final Resource resource) {
		if (resource instanceof FeatureOfInterest) {
			remove((FeatureOfInterest) resource);
		} else if (resource instanceof ObservedProperty) {
			remove((ObservedProperty) resource);
		} else if (resource instanceof UnitOfMeasurement) {
			remove((UnitOfMeasurement) resource);
		} else if (resource instanceof Sensor) {
			remove((Sensor) resource);
		}
	}
	
	public void add(final FeatureOfInterest featureOfInterest) {
		featureOfInterests.add(featureOfInterest);
	}
	
	public void remove(final FeatureOfInterest featureOfInterest) {
		featureOfInterests.remove(featureOfInterest);
	}

	public List<FeatureOfInterest> getFeatureOfInterests() {
		((ArrayList<FeatureOfInterest>) featureOfInterests).trimToSize();
		final Object[] a = featureOfInterests.toArray(); 
		Arrays.sort(a);
		featureOfInterests = new ArrayList<FeatureOfInterest>(a.length);
		for (final Object element : a) {
			featureOfInterests.add((FeatureOfInterest) element);
		}
		return featureOfInterests;
	}
	
	public void add(final ObservedProperty observedProperty) {
		observedProperties.add(observedProperty);
	}
	
	public void remove(final ObservedProperty observedProperty) {
		observedProperties.remove(observedProperty);
	}
	
	public List<ObservedProperty> getObservedProperties() {
		((ArrayList<ObservedProperty>) observedProperties).trimToSize();
		return observedProperties;
	}
		
	public void add(final UnitOfMeasurement unitOfMeasurement) {
		unitOfMeasurements.add(unitOfMeasurement);
	}
	
	public void remove(final UnitOfMeasurement unitOfMeasurement) {
		unitOfMeasurements.remove(unitOfMeasurement);
	}

	public List<UnitOfMeasurement> getUnitOfMeasurements() {
		((ArrayList<UnitOfMeasurement>) unitOfMeasurements).trimToSize();
		return unitOfMeasurements;
	}
	
	public void add(final Sensor sensor) {
		sensors.add(sensor);
	}
	
	public void remove(final Sensor sensor) {
		sensors.remove(sensor);
	}

	public List<Sensor> getSensors() {
		((ArrayList<Sensor>) sensors).trimToSize();
		return sensors;
	}
	
	public void add(final Position position) {
		positions.add(position);
	}

	public List<Position> getPositions() {
		((ArrayList<Position>) positions).trimToSize();
		return positions;
	}
	
	public void setPositions(final List<Position> positions) {
		this.positions = positions;
	}
	
	public void addObservationToInsert(final InsertObservation io) {
		observationsToInsert.add(io);
	}
	
	public void addSensorToRegister(final RegisterSensor rs) {
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
	
	public void remove(final Position position) {
		positions.remove(position);
	}
	
	public List<FeatureOfInterest> getFeatureOfInterestsInTable() {
		final ArrayList<FeatureOfInterest> foisInTable = new ArrayList<FeatureOfInterest>();
		for (final FeatureOfInterest foi: featureOfInterests) {
			if (foi.getTableElement() != null) {
				foisInTable.add(foi);
			}
		}
		foisInTable.trimToSize();
		return foisInTable;
	}
	
	public List<Sensor> getSensorsInTable() {
		final ArrayList<Sensor> sensorsInTable = new ArrayList<Sensor>();
		for (final Sensor s: sensors) {
			if (s.getTableElement() != null) {
				sensorsInTable.add(s);
			}
		}
		sensorsInTable.trimToSize();
		return sensorsInTable;
	}

	public List<ObservedProperty> getObservedPropertiesInTable() {
		final ArrayList<ObservedProperty> opsInTable = new ArrayList<ObservedProperty>();
		for (final ObservedProperty op: observedProperties) {
			if (op.getTableElement() != null) {
				opsInTable.add(op);
			}
		}
		opsInTable.trimToSize();
		return opsInTable;
	}

	public void add(final Step6bSpecialModel step6bSpecialModel) {
		logger.info("Assign " + step6bSpecialModel.getSensor() + " to Feature of Interest \"" +
				step6bSpecialModel.getFeatureOfInterest().getName() + "\" and Observed Property \"" +
				step6bSpecialModel.getObservedProperty().getName() + "\"");
		step6bSpecialModels.add(step6bSpecialModel);
	} 
	
	public void remove(final Step6bSpecialModel step6bSpecialModel) {
		if (step6bSpecialModel.getSensor().getName() != null ||
				step6bSpecialModel.getSensor().getURI() != null) {
			logger.info("Unassign " + step6bSpecialModel.getSensor() + " from Feature of Interest \"" +
				step6bSpecialModel.getFeatureOfInterest().getName() + " and Observed Property \"" +
				step6bSpecialModel.getObservedProperty().getName()+ "\"");
		}
		step6bSpecialModels.remove(step6bSpecialModel);
	}

	public HashSet<Step6bSpecialModel> getStep6bSpecialModels() {
		return step6bSpecialModels;
	}
	
}
