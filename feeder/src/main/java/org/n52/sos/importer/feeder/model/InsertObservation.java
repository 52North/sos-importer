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
package org.n52.sos.importer.feeder.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.n52.shetland.ogc.om.NamedValue;

/**
 * Holds all information for the InsertObservation request
 *
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
    private List<NamedValue<?>> omParameters;

    public InsertObservation(final Sensor sensor,
            final FeatureOfInterest foi,
            final Object value,
            final Timestamp timeStamp,
            final UnitOfMeasurement uom,
            final ObservedProperty obsProp,
            final Offering off,
            Optional<List<NamedValue<?>>> omParameters,
            final String mvType) {
        featureOfInterest = foi;
        this.sensor = sensor;
        observedProperty = obsProp;
        this.timeStamp = timeStamp;
        unitOfMeasurement = uom;
        resultValue = value;
        offering = off;
        measuredValueType = mvType;
        if (omParameters.isPresent()) {
            this.omParameters = omParameters.get();
        } else {
            this.omParameters = Collections.emptyList();
        }
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

    public double getAltitudeValue() {
        return featureOfInterest.getPosition().getAltitude();
    }


    public boolean isSetAltitudeValue() {
        return featureOfInterest != null && featureOfInterest.getPosition().getAltitude() != Position.VALUE_NOT_SET;
    }

    public boolean isFeaturePositionValid() {
        return getFeatureOfInterest().isPositionValid();
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
        builder.append(", omParameter=");
        builder.append(Arrays.toString(getOmParameters()));
        builder.append("]");
        return builder.toString();
    }

    public boolean isSetOmParameters() {
        return omParameters != null && !omParameters.isEmpty();
    }

    public NamedValue<?>[] getOmParameters() {
        return isSetOmParameters() ? Collections.unmodifiableList(omParameters)
                .toArray(new NamedValue<?>[omParameters.size()])
                : new NamedValue<?>[0];
    }

    public boolean hasFeatureParentFeature() {
        return featureOfInterest.hasParentFeature();
    }

    public String getParentFeatureIdentifier() {
        return featureOfInterest.getParentFeature();
    }
}
