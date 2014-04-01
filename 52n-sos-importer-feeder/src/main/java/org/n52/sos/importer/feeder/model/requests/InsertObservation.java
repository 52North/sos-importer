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

import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Position;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;

/**
 * Holds all information for the InsertObservation request
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class InsertObservation {

	private final Object resultValue;
	private final Timestamp timeStamp;
	private final Sensor sensor;
	private final ObservedProperty observedProperty;
	private final FeatureOfInterest featureOfInterest;
	private final UnitOfMeasurement unitOfMeasurement;
	private final Offering offering;
	private final String measuredValueType;

	public InsertObservation(final Sensor sensor,
			final FeatureOfInterest foi,
			final Object value,
			final Timestamp timeStamp,
			final UnitOfMeasurement uom,
			final ObservedProperty obsProp,
			final Offering off,
			final String mvType) {
		featureOfInterest = foi;
		this.sensor = sensor;
		observedProperty = obsProp;
		this.timeStamp = timeStamp;
		unitOfMeasurement = uom;
		resultValue = value;
		offering = off;
		measuredValueType = mvType;
	}

	public String getSensorName() {
		return sensor.getName();
	}

	public String getSensorURI() {
		return sensor.getUri();
	}

	public String getFeatureOfInterestName() {
		return featureOfInterest.getName();
	}

	public String getFeatureOfInterestURI() {
		return featureOfInterest.getUri();
	}

	public String getObservedPropertyURI() {
		return observedProperty.getUri();
	}

	public String getUnitOfMeasurementCode() {
		return unitOfMeasurement.getCode();
	}

	public Object getResultValue() {
		return resultValue;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public String getEpsgCode() {
		return Integer.toString(featureOfInterest.getPosition().getEpsgCode());
	}

	public double getLatitudeValue() {
		return featureOfInterest.getPosition().getLatitude();
	}

	public double getLongitudeValue() {
		return featureOfInterest.getPosition().getLongitude();
	}

	public double getAltitudeValue()
	{
		return featureOfInterest.getPosition().getAltitude();
	}


	public boolean isSetAltitudeValue()
	{
		return featureOfInterest != null && featureOfInterest.getPosition().getAltitude() != Position.VALUE_NOT_SET;
	}

	public ObservedProperty getObservedProperty() {
		return observedProperty;
	}

	protected FeatureOfInterest getFeatureOfInterest() {
		return featureOfInterest;
	}

	protected UnitOfMeasurement getUnitOfMeasurment() {
		return unitOfMeasurement;
	}

	public Offering getOffering() {
		return offering;
	}

	public String getMeasuredValueType() {
		return measuredValueType;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("InsertObservation [resultValue=");
		builder.append(resultValue);
		builder.append(", timeStamp=");
		builder.append(timeStamp);
		builder.append(", sensor=");
		builder.append(sensor);
		builder.append(", observedProperty=");
		builder.append(observedProperty);
		builder.append(", featureOfInterest=");
		builder.append(featureOfInterest);
		builder.append(", unitOfMeasurement=");
		builder.append(unitOfMeasurement);
		builder.append(", offering=");
		builder.append(offering);
		builder.append(", measuredValueType=");
		builder.append(measuredValueType);
		builder.append("]");
		return builder.toString();
	}
}
