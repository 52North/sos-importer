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

import java.util.LinkedList;

import org.apache.commons.lang.NotImplementedException;
import org.n52.oxf.sos.request.InsertObservationParameters;
import org.n52.sos.importer.feeder.model.requests.InsertObservation;

public class TimeSeries {

	public static final String SENSOR_ID_NOT_SET = "SENSOR_ID_NOT_SET";

	public static final ObservedProperty OBSERVED_PROPERTY_NOT_SET = new ObservedProperty("OBSERVED_PROPERTY_NOT_SET", "OBSERVED_PROPERTY_NOT_SET");

	public static final String UOM_CODE_NOT_SET = "UOM_CODE_NOT_SET";

	public static final String MV_TYPE_NOT_SET = "MV_TYPE_NOT_SET";

	public static final String SENSOR_NAME_NOT_SET = "SENSOR_NAME_NOT_SET";

	private final LinkedList<InsertObservation> timeseries = new LinkedList<>();

	public boolean addObservation(final InsertObservation insertObservation) {
		return timeseries.add(insertObservation);
	}

	public String getSensorURI() {
		if (timeseries.isEmpty()) {
			return SENSOR_ID_NOT_SET;
		}
		final String sensorURI = timeseries.getFirst().getSensorURI();
		if (sensorURI == null || sensorURI.isEmpty()) {
			return SENSOR_ID_NOT_SET;
		}
		return sensorURI;
	}

	public Object getSensorName() {
		if (timeseries.isEmpty()) {
			return SENSOR_NAME_NOT_SET;
		}
		final String sensorName = timeseries.getFirst().getSensorName();
		if (sensorName == null || sensorName.isEmpty()) {
			return SENSOR_NAME_NOT_SET;
		}
		return sensorName;
	}

	public InsertObservation getFirst() {
		if (timeseries.isEmpty()) {
			return null;
		}
		return timeseries.getFirst();
	}

	public ObservedProperty getObservedProperty() {
		if (timeseries.isEmpty()) {
			return OBSERVED_PROPERTY_NOT_SET;
		}
		final ObservedProperty obsProp = timeseries.getFirst().getObservedProperty();
		if (obsProp == null) {
			return OBSERVED_PROPERTY_NOT_SET;
		}
		return obsProp;
	}

	public String getUnitOfMeasurementCode() {
		if (timeseries.isEmpty()) {
			return UOM_CODE_NOT_SET;
		}
		final String uomCode = timeseries.getFirst().getUnitOfMeasurementCode();
		if (uomCode == null || uomCode.isEmpty()) {
			return UOM_CODE_NOT_SET;
		}
		return uomCode;
	}

	public String getMeasuredValueType() {
		if (timeseries.isEmpty()) {
			return MV_TYPE_NOT_SET;
		}
		final String mVType = timeseries.getFirst().getMeasuredValueType();
		if (mVType == null || mVType.isEmpty()) {
			return MV_TYPE_NOT_SET;
		}
		return mVType;
	}

	public InsertObservationParameters getSweArrayObservation() {
		// TODO Auto-generated method "getSweArrayObservation" stub generated on 28.03.2014 around 12:49:28 by eike
		// TODO continue implementation here!
		// add extension
		// add offering from InsertObservation
    	// add OM_SWEArrayObservation
			// OM_Observation
    		// observation type
    		// phentime
    		// temporal bbox for result time
    		// procedure
    		// obsProp
    		// feature
    		// result
    			// count
    			// type
    				// phentime
    				// obsProp
    			// encoding
    				// token
    				// block seperator
    			// values <-- linebreak every 100 lines?
		throw new NotImplementedException();
	}

	@Override
	public String toString() {
		return String.format("TimeSeries [sensor=%s, observedProperty=%s, feature=%s]",
				getSensorURI(),
				getObservedProperty(),
				timeseries.getFirst().getFeatureOfInterestURI());
	}

}
