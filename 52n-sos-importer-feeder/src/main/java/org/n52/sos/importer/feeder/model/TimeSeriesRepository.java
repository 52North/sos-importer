/**
 * Copyright (C) 2014
 * by 52 North Initiative for Geospatial Open Source Software GmbH
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

}
