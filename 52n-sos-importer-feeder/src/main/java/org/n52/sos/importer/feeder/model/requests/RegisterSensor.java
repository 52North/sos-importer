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
