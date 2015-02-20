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
package org.n52.sos.importer.feeder.model.requests;

import java.util.Collection;
import java.util.Map;

import org.n52.sos.importer.feeder.model.ObservedProperty;

/**
 * Holds all information for the RegisterSensor request
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class RegisterSensor {

	private final InsertObservation io;
	
	private final Map<ObservedProperty, String> measuredValueTypes;

	private final Collection<ObservedProperty> observedProperties;

	private final Map<ObservedProperty, String> unitOfMeasurements;
	
	public RegisterSensor(
			final InsertObservation io, 
			final Collection<ObservedProperty> observedProperties,
			final Map<ObservedProperty, String> measuredValueTypes,
			final Map<ObservedProperty, String> unitOfMeasurements) {
		this.io = io;
		this.observedProperties = observedProperties;
		this.measuredValueTypes = measuredValueTypes;
		this.unitOfMeasurements = unitOfMeasurements;
	}

	public String getAltitudeUnit() {
		return io.getFeatureOfInterest().getPosition().getAltitudeUnit();
	}

	public double getAltitudeValue() {
		return io.getFeatureOfInterest().getPosition().getAltitude();
	}

	public String getDefaultValue() {
		if (io.getResultValue() instanceof Boolean) {
			return "false";
		}
		if (io.getResultValue() instanceof Integer) {
			return "0";
		}
		if (io.getResultValue() instanceof Double) {
			return "0.0";
		}
		if (io.getResultValue() instanceof String) {
			return " ";
		}
		return "notDefined";
	}

	public String getEpsgCode() {
		return io.getEpsgCode();
	}

	public String getFeatureOfInterestName() {
		return io.getFeatureOfInterestName();
	}

	public String getFeatureOfInterestURI() {
		return io.getFeatureOfInterestURI();
	}

	public String getLatitudeUnit() {
		return io.getFeatureOfInterest().getPosition().getLatitudeUnit();
	}

	public double getLatitudeValue() {
		return io.getLatitudeValue();
	}

	public String getLongitudeUnit() {
		return io.getFeatureOfInterest().getPosition().getLongitudeUnit();
	}

	public double getLongitudeValue() {
		return io.getLongitudeValue();
	}

	public String getMeasuredValueType(final ObservedProperty observedProperty)
	{
		return measuredValueTypes.get(observedProperty);
	}

	public Collection<ObservedProperty> getObservedProperties()
	{
		return observedProperties;
	}

	public String getOfferingName() {
		return io.getOffering().getName();
	}

	public String getOfferingUri() {
		return io.getOffering().getUri();
	}

	public String getSensorName() {
		return io.getSensorName();
	}
	
	public String getSensorURI() {
		return io.getSensorURI();
	}

	public String getUnitOfMeasurementCode(final ObservedProperty observedProperty) {
		return unitOfMeasurements.get(observedProperty);
	}
}
