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

import java.util.Collection;
import java.util.Map;

/**
 * Holds all information for the RegisterSensor request
 *
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class RegisterSensor {

    private final InsertObservation io;

    private final Map<ObservedProperty, String> measuredValueTypes;

    private final Collection<ObservedProperty> observedProperties;

    private final Map<ObservedProperty, String> unitOfMeasurements;

    /**
     * <p>Constructor for RegisterSensor.</p>
     *
     * @param io a {@link org.n52.sos.importer.feeder.model.InsertObservation} object.
     * @param observedProperties a {@link java.util.Collection} object.
     * @param measuredValueTypes a {@link java.util.Map} object.
     * @param unitOfMeasurements a {@link java.util.Map} object.
     */
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

    /**
     * <p>getAltitudeUnit.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAltitudeUnit() {
        return io.getFeatureOfInterest().getPosition().getAltitudeUnit();
    }

    /**
     * <p>getAltitudeValue.</p>
     *
     * @return a double.
     */
    public double getAltitudeValue() {
        return io.getFeatureOfInterest().getPosition().getAltitude();
    }

    /**
     * <p>getDefaultValue.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDefaultValue(ObservedProperty observedProperty) {
        if (io.getObservedPropertiesResultValue().get(observedProperty) instanceof Boolean) {
            return "false";
        }
        if (io.getObservedPropertiesResultValue().get(observedProperty) instanceof Integer) {
            return "0";
        }
        if (io.getObservedPropertiesResultValue().get(observedProperty) instanceof Double) {
            return "0.0";
        }
        if (io.getObservedPropertiesResultValue().get(observedProperty) instanceof String) {
            return " ";
        }
        return "notDefined";
    }

    /**
     * <p>getEpsgCode.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getEpsgCode() {
        return io.getEpsgCode();
    }

    /**
     * <p>getFeatureOfInterestName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFeatureOfInterestName() {
        return io.getFeatureOfInterestName();
    }

    /**
     * <p>getFeatureOfInterestURI.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFeatureOfInterestURI() {
        return io.getFeatureOfInterestURI();
    }

    /**
     * <p>getLatitudeUnit.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLatitudeUnit() {
        return io.getFeatureOfInterest().getPosition().getLatitudeUnit();
    }

    /**
     * <p>getLatitudeValue.</p>
     *
     * @return a double.
     */
    public double getLatitudeValue() {
        return io.getLatitudeValue();
    }

    /**
     * <p>getLongitudeUnit.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLongitudeUnit() {
        return io.getFeatureOfInterest().getPosition().getLongitudeUnit();
    }

    /**
     * <p>getLongitudeValue.</p>
     *
     * @return a double.
     */
    public double getLongitudeValue() {
        return io.getLongitudeValue();
    }

    /**
     * <p>getMeasuredValueType.</p>
     *
     * @param observedProperty a {@link org.n52.sos.importer.feeder.model.ObservedProperty} object.
     * @return a {@link java.lang.String} object.
     */
    public String getMeasuredValueType(final ObservedProperty observedProperty) {
        return measuredValueTypes.get(observedProperty);
    }

    /**
     * <p>Getter for the field <code>observedProperties</code>.</p>
     *
     * @return a {@link java.util.Collection} object.
     */
    public Collection<ObservedProperty> getObservedProperties() {
        return observedProperties;
    }

    /**
     * <p>getOfferingName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOfferingName() {
        return io.getOffering().getName();
    }

    /**
     * <p>getOfferingUri.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOfferingUri() {
        return io.getOffering().getUri();
    }

    /**
     * <p>getSensorName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSensorName() {
        return io.getSensorName();
    }

    /**
     * <p>getSensorURI.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSensorURI() {
        return io.getSensorURI();
    }

    /**
     * <p>getUnitOfMeasurementCode.</p>
     *
     * @param observedProperty a {@link org.n52.sos.importer.feeder.model.ObservedProperty} object.
     * @return a {@link java.lang.String} object.
     */
    public String getUnitOfMeasurementCode(final ObservedProperty observedProperty) {
        return unitOfMeasurements.get(observedProperty);
    }

}
