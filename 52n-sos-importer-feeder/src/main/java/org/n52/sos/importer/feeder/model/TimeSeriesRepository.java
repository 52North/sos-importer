/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.feeder.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.n52.sos.importer.feeder.model.requests.InsertObservation;
import org.n52.sos.importer.feeder.model.requests.RegisterSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeSeriesRepository {

	private static final Logger LOG = LoggerFactory.getLogger(TimeSeriesRepository.class);

	private final Vector<TimeSeries> repo;

	public TimeSeriesRepository(final int numberOfTimeSeries) {
		repo = new Vector<TimeSeries>(numberOfTimeSeries);
		for (int i = 0; i < numberOfTimeSeries; i++) {
			repo.add(i, new TimeSeries());
		}
	}

	/**
	 * @param ios the {@link InsertObservation}s to add using the array index as timeseries id
	 */
	public void addObservations(final InsertObservation[] ios) {
		if (ios.length != repo.capacity()) {
			throw new IllegalArgumentException("Number of InsertObservations is not matching number of timeseries!");
		}
		if (ios != null) {
			for (int i = 0; i < ios.length; i++) {
				repo.get(i).addObservation(ios[i]);
			}
		}
	}

	public Collection<TimeSeries> getTimeSeries() {
		return repo;
	}

	/**
	 * @return an {@link RegisterSensor} using
	 * <ul><li>ObservedProperty</li>
	 * <li>MeasureValueType</li>
	 * <li>UnitOfMeasurement</li></ul>
	 * from each timeseries in this {@link TimeSeriesRepository}.
	 */
	public RegisterSensor getRegisterSensor() {
		final InsertObservation io = repo.get(0).getFirst();
		return new RegisterSensor(io,
				getObservedProperties(),
				getMeasuredValueTypes(),
				getUnitsOfMeasurement());
	}

	private Map<ObservedProperty, String> getUnitsOfMeasurement() {
		LOG.trace("getUnitsOfMeasurement(...)");
		final Map<ObservedProperty,String> unitsOfMeasurement = new HashMap<ObservedProperty, String>(repo.capacity());
		for (final TimeSeries ts : getTimeSeries()) {
			unitsOfMeasurement.put(ts.getObservedProperty(), ts.getUnitOfMeasurementCode());
		}
		return unitsOfMeasurement;
	}

	private Map<ObservedProperty, String> getMeasuredValueTypes() {
		LOG.trace("getMeasuredValueTypes(...)");
		final Map<ObservedProperty,String> measuredValueTypes = new HashMap<ObservedProperty, String>(repo.capacity());
		for (final TimeSeries ts : getTimeSeries()) {
			measuredValueTypes.put(ts.getObservedProperty(), ts.getMeasuredValueType());
		}
		return measuredValueTypes;
	}

	private Collection<ObservedProperty> getObservedProperties() {
		LOG.trace("getObservedProperties(...)");
		final Set<ObservedProperty> observedProperties = new HashSet<ObservedProperty>(repo.capacity());
		for (final TimeSeries ts : getTimeSeries()) {
				observedProperties.add(ts.getObservedProperty());
		}
		return observedProperties;
	}

	/**
	 * @return <code>true</code>, in the case of containing only empty time series
	 */
	public boolean isEmpty() {
		for (final TimeSeries timeSeries : getTimeSeries()) {
			if (!timeSeries.isEmpty()) {
				return false;
			}
		}
		return true;
	}

}
