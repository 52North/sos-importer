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
 *
 * @author Raimund
 * @version $Id: $Id
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

	/**
	 * <p>Getter for the field <code>instance</code>.</p>
	 *
	 * @return a {@link org.n52.sos.importer.model.ModelStore} object.
	 */
	public static ModelStore getInstance() {
		if (instance == null) {
			instance = new ModelStore();
		}
		return instance;
	}

	/**
	 * <p>add.</p>
	 *
	 * @param mv a {@link org.n52.sos.importer.model.measuredValue.MeasuredValue} object.
	 */
	public void add(final MeasuredValue mv) {
		measuredValues.add(mv);
	}

	/**
	 * <p>Getter for the field <code>measuredValues</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<MeasuredValue> getMeasuredValues() {
		((ArrayList<MeasuredValue>) measuredValues).trimToSize();
		return measuredValues;
	}

	/**
	 * <p>getMeasuredValueAt.</p>
	 *
	 * @param tableElement a {@link org.n52.sos.importer.model.table.TableElement} object.
	 * @return a {@link org.n52.sos.importer.model.measuredValue.MeasuredValue} object.
	 */
	public MeasuredValue getMeasuredValueAt(final TableElement tableElement) {
		for (final MeasuredValue mv: measuredValues) {
			if (mv.getTableElement().equals(tableElement)) {
				return mv;
			}
		}
		return null;
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param mv a {@link org.n52.sos.importer.model.measuredValue.MeasuredValue} object.
	 */
	public void remove(final MeasuredValue mv) {
		measuredValues.remove(mv);
	}

	/**
	 * <p>add.</p>
	 *
	 * @param dateAndTime a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
	 */
	public void add(final DateAndTime dateAndTime) {
		dateAndTimes.add(dateAndTime);
	}

	/**
	 * <p>Getter for the field <code>dateAndTimes</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<DateAndTime> getDateAndTimes() {
		((ArrayList<DateAndTime>) dateAndTimes).trimToSize();
		return dateAndTimes;
	}

	/**
	 * <p>Setter for the field <code>dateAndTimes</code>.</p>
	 *
	 * @param dateAndTimes a {@link java.util.List} object.
	 */
	public void setDateAndTimes(final List<DateAndTime> dateAndTimes) {
		this.dateAndTimes = dateAndTimes;
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param dateAndTime a {@link org.n52.sos.importer.model.dateAndTime.DateAndTime} object.
	 */
	public void remove(final DateAndTime dateAndTime) {
		dateAndTimes.remove(dateAndTime);
	}

	/**
	 * <p>add.</p>
	 *
	 * @param resource a {@link org.n52.sos.importer.model.resources.Resource} object.
	 */
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

	/**
	 * <p>remove.</p>
	 *
	 * @param resource a {@link org.n52.sos.importer.model.resources.Resource} object.
	 */
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

	/**
	 * <p>add.</p>
	 *
	 * @param featureOfInterest a {@link org.n52.sos.importer.model.resources.FeatureOfInterest} object.
	 */
	public void add(final FeatureOfInterest featureOfInterest) {
		featureOfInterests.add(featureOfInterest);
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param featureOfInterest a {@link org.n52.sos.importer.model.resources.FeatureOfInterest} object.
	 */
	public void remove(final FeatureOfInterest featureOfInterest) {
		featureOfInterests.remove(featureOfInterest);
	}

	/**
	 * <p>Getter for the field <code>featureOfInterests</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
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

	/**
	 * <p>add.</p>
	 *
	 * @param observedProperty a {@link org.n52.sos.importer.model.resources.ObservedProperty} object.
	 */
	public void add(final ObservedProperty observedProperty) {
		observedProperties.add(observedProperty);
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param observedProperty a {@link org.n52.sos.importer.model.resources.ObservedProperty} object.
	 */
	public void remove(final ObservedProperty observedProperty) {
		observedProperties.remove(observedProperty);
	}

	/**
	 * <p>Getter for the field <code>observedProperties</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ObservedProperty> getObservedProperties() {
		((ArrayList<ObservedProperty>) observedProperties).trimToSize();
		return observedProperties;
	}

	/**
	 * <p>add.</p>
	 *
	 * @param unitOfMeasurement a {@link org.n52.sos.importer.model.resources.UnitOfMeasurement} object.
	 */
	public void add(final UnitOfMeasurement unitOfMeasurement) {
		unitOfMeasurements.add(unitOfMeasurement);
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param unitOfMeasurement a {@link org.n52.sos.importer.model.resources.UnitOfMeasurement} object.
	 */
	public void remove(final UnitOfMeasurement unitOfMeasurement) {
		unitOfMeasurements.remove(unitOfMeasurement);
	}

	/**
	 * <p>Getter for the field <code>unitOfMeasurements</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<UnitOfMeasurement> getUnitOfMeasurements() {
		((ArrayList<UnitOfMeasurement>) unitOfMeasurements).trimToSize();
		return unitOfMeasurements;
	}

	/**
	 * <p>add.</p>
	 *
	 * @param sensor a {@link org.n52.sos.importer.model.resources.Sensor} object.
	 */
	public void add(final Sensor sensor) {
		sensors.add(sensor);
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param sensor a {@link org.n52.sos.importer.model.resources.Sensor} object.
	 */
	public void remove(final Sensor sensor) {
		sensors.remove(sensor);
	}

	/**
	 * <p>Getter for the field <code>sensors</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<Sensor> getSensors() {
		((ArrayList<Sensor>) sensors).trimToSize();
		return sensors;
	}

	/**
	 * <p>add.</p>
	 *
	 * @param position a {@link org.n52.sos.importer.model.position.Position} object.
	 */
	public void add(final Position position) {
		positions.add(position);
	}

	/**
	 * <p>Getter for the field <code>positions</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<Position> getPositions() {
		((ArrayList<Position>) positions).trimToSize();
		return positions;
	}

	/**
	 * <p>Setter for the field <code>positions</code>.</p>
	 *
	 * @param positions a {@link java.util.List} object.
	 */
	public void setPositions(final List<Position> positions) {
		this.positions = positions;
	}

	/**
	 * <p>addObservationToInsert.</p>
	 *
	 * @param io a {@link org.n52.sos.importer.model.requests.InsertObservation} object.
	 */
	public void addObservationToInsert(final InsertObservation io) {
		observationsToInsert.add(io);
	}

	/**
	 * <p>addSensorToRegister.</p>
	 *
	 * @param rs a {@link org.n52.sos.importer.model.requests.RegisterSensor} object.
	 */
	public void addSensorToRegister(final RegisterSensor rs) {
		sensorsToRegister.add(rs);
	}

	/**
	 * <p>Getter for the field <code>sensorsToRegister</code>.</p>
	 *
	 * @return a {@link java.util.HashSet} object.
	 */
	public HashSet<RegisterSensor> getSensorsToRegister() {
		return sensorsToRegister;
	}

	/**
	 * <p>Getter for the field <code>observationsToInsert</code>.</p>
	 *
	 * @return a {@link java.util.HashSet} object.
	 */
	public HashSet<InsertObservation> getObservationsToInsert() {
		return observationsToInsert;
	}

	/**
	 * <p>clearSensorsToRegister.</p>
	 */
	public void clearSensorsToRegister() {
		sensorsToRegister.clear();
	}

	/**
	 * <p>clearObservationsToInsert.</p>
	 */
	public void clearObservationsToInsert() {
		observationsToInsert.clear();
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param position a {@link org.n52.sos.importer.model.position.Position} object.
	 */
	public void remove(final Position position) {
		positions.remove(position);
	}

	/**
	 * <p>getFeatureOfInterestsInTable.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
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

	/**
	 * <p>getSensorsInTable.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
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

	/**
	 * <p>getObservedPropertiesInTable.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
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

	/**
	 * <p>add.</p>
	 *
	 * @param step6bSpecialModel a {@link org.n52.sos.importer.model.Step6bSpecialModel} object.
	 */
	public void add(final Step6bSpecialModel step6bSpecialModel) {
		logger.info("Assign " + step6bSpecialModel.getSensor() + " to Feature of Interest \"" +
				step6bSpecialModel.getFeatureOfInterest().getName() + "\" and Observed Property \"" +
				step6bSpecialModel.getObservedProperty().getName() + "\"");
		step6bSpecialModels.add(step6bSpecialModel);
	}

	/**
	 * <p>remove.</p>
	 *
	 * @param step6bSpecialModel a {@link org.n52.sos.importer.model.Step6bSpecialModel} object.
	 */
	public void remove(final Step6bSpecialModel step6bSpecialModel) {
		if (step6bSpecialModel.getSensor().getName() != null ||
				step6bSpecialModel.getSensor().getURI() != null) {
			logger.info("Unassign " + step6bSpecialModel.getSensor() + " from Feature of Interest \"" +
				step6bSpecialModel.getFeatureOfInterest().getName() + " and Observed Property \"" +
				step6bSpecialModel.getObservedProperty().getName()+ "\"");
		}
		step6bSpecialModels.remove(step6bSpecialModel);
	}

	/**
	 * <p>Getter for the field <code>step6bSpecialModels</code>.</p>
	 *
	 * @return a {@link java.util.HashSet} object.
	 */
	public HashSet<Step6bSpecialModel> getStep6bSpecialModels() {
		return step6bSpecialModels;
	}

}
