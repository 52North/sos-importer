/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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

import java.net.URI;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Data holding class for all observations of a time series.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class TimeSeries {

    public static final String OBSERVED_PROPERTY_NOT_SET_CONST = "OBSERVED_PROPERTY_NOT_SET";

    public static final URI SENSOR_ID_NOT_SET = URI.create("SENSOR_ID_NOT_SET");

    public static final ObservedProperty OBSERVED_PROPERTY_NOT_SET =
            new ObservedProperty(OBSERVED_PROPERTY_NOT_SET_CONST, "property-not-set");

    public static final String UOM_CODE_NOT_SET = "UOM_CODE_NOT_SET";

    public static final String MV_TYPE_NOT_SET = "MV_TYPE_NOT_SET";

    public static final String SENSOR_NAME_NOT_SET = "SENSOR_NAME_NOT_SET";

    private final LinkedList<InsertObservation> timeseries = new LinkedList<>();

    public boolean addObservation(final InsertObservation insertObservation) {
        return timeseries.add(insertObservation);
    }

    public URI getSensorURI() {
        if (timeseries.isEmpty()) {
            return SENSOR_ID_NOT_SET;
        }
        final URI sensorURI = timeseries.getFirst().getSensorURI();
        if (sensorURI == null || sensorURI.toString().isEmpty()) {
            return SENSOR_ID_NOT_SET;
        }
        return sensorURI;
    }

    public String getSensorName() {
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

    public String getResultTime() {
        Timestamp resultTime = null;
        for (final InsertObservation io : timeseries) {
            if (resultTime == null || resultTime.isBefore(io.getResultTime())) {
                resultTime = io.getResultTime();
            }
        }
        if (resultTime == null || resultTime.toString().isEmpty()) {
            return "Could not get result time date of timeseries";
        }
        return resultTime.toString();
    }

    public Timestamp getYoungestResultTime() {
        if (isEmpty()) {
            return new Timestamp().ofUnixTimeMillis(Long.MIN_VALUE);
        }
        Timestamp youngestTimestamp = getFirst().getResultTime();
        for (InsertObservation insertObservation : timeseries) {
            if (insertObservation.getResultTime().isAfter(youngestTimestamp)) {
                youngestTimestamp = insertObservation.getResultTime();
            }
        }
        return youngestTimestamp;
    }

    public List<? extends InsertObservation> getInsertObservations() {
        return Collections.unmodifiableList(timeseries);
    }

    public boolean isEmpty() {
        return timeseries.isEmpty();
    }

    public int size() {
        return timeseries.size();
    }

    @Override
    public String toString() {
        return String.format("TimeSeries [sensor=%s, observedProperty=%s, feature=%s]",
                getSensorURI(),
                getObservedProperty(),
                timeseries.getFirst().getFeatureOfInterestURI());
    }

}
