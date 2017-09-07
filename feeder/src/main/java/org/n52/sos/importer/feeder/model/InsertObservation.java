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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.n52.oxf.om.x20.OmParameter;

/**
 * Holds all information for the InsertObservation request
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class InsertObservation {

    private final Set<ObservedProperty> observedProperties;
    private final Map<ObservedProperty, Object> observedPropertiesResultValue;
    private final Timestamp timeStamp;
    private final Sensor sensor;
    private final Map<ObservedProperty, UnitOfMeasurement> observedPropertiesWithUom;
    private final FeatureOfInterest featureOfInterest;
    private final Offering offering;
    private final Map<ObservedProperty, String> observedPropertiesMeasuredValueType;
    private final List<OmParameter<?>> omParameters;

    /**
     * <p>Constructor for InsertObservation.</p>
     *
     * @param sensor a {@link org.n52.sos.importer.feeder.model.Sensor} object.
     * @param foi a {@link org.n52.sos.importer.feeder.model.FeatureOfInterest} object.
     * @param value a {@link java.lang.Object} object.
     * @param timeStamp a {@link org.n52.sos.importer.feeder.model.Timestamp} object.
     * @param uom a {@link org.n52.sos.importer.feeder.model.UnitOfMeasurement} object.
     * @param obsProp a {@link org.n52.sos.importer.feeder.model.ObservedProperty} object.
     * @param off a {@link org.n52.sos.importer.feeder.model.Offering} object.
     * @param omParameters a optional list of {@link OmParameter}.
     * @param mvType a {@link java.lang.String} object.
     */
    public InsertObservation(final Sensor sensor,
            final FeatureOfInterest foi,
            final Object value,
            final Timestamp timeStamp,
            final UnitOfMeasurement uom,
            final ObservedProperty obsProp,
            final Offering off,
            Optional<List<OmParameter<?>>> omParameters,
            final String mvType) {
        featureOfInterest = foi;
        this.sensor = sensor;
        this.observedProperties = new HashSet<>();
        this.observedProperties.add(obsProp);
        observedPropertiesWithUom = new HashMap<>();
        observedPropertiesWithUom.put(obsProp, uom);
        this.timeStamp = timeStamp;
        observedPropertiesResultValue = new HashMap<>();
        observedPropertiesResultValue.put(obsProp, value);
        offering = off;
        observedPropertiesMeasuredValueType = new HashMap<>();
        observedPropertiesMeasuredValueType.put(obsProp, mvType);
        if (omParameters.isPresent()) {
            this.omParameters = omParameters.get();
        } else {
            this.omParameters = Collections.emptyList();
        }
    }

    public InsertObservation(final Sensor sensor,
            final FeatureOfInterest foi,
            final Map<ObservedProperty, Object> obsValue,
            final Timestamp timeStamp,
            final Map<ObservedProperty, UnitOfMeasurement> obsUom,
            final Set<ObservedProperty> obsProps,
            final Offering off,
            Optional<List<OmParameter<?>>> omParameters,
            final Map<ObservedProperty, String> obsMvType) {
        this.featureOfInterest = foi;
        this.sensor = sensor;
        this.observedProperties = obsProps;
        this.observedPropertiesWithUom = obsUom;
        this.timeStamp = timeStamp;
        this.observedPropertiesResultValue = obsValue;
        this.offering = off;
        this.observedPropertiesMeasuredValueType = obsMvType;
        if (omParameters.isPresent()) {
            this.omParameters = omParameters.get();
        } else {
            this.omParameters = Collections.emptyList();
        }
    }

    /**
     * <p>getSensorName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSensorName() {
        return sensor.getName();
    }

    /**
     * <p>getSensorURI.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSensorURI() {
        return sensor.getUri();
    }

    /**
     * <p>getFeatureOfInterestName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFeatureOfInterestName() {
        return featureOfInterest.getName();
    }

    /**
     * <p>getFeatureOfInterestURI.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFeatureOfInterestURI() {
        return featureOfInterest.getUri();
    }

    /**
     * <p>getObservedPropertyURI.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUniqueObservedPropertyURI() {
        if (observedProperties.size() == 1) {
            return observedProperties.iterator().next().getUri();
        } else if (observedProperties.size() > 1) {
            StringBuilder sb = new StringBuilder("urn:phenomenon:composite:");
            // return a composite generated composite name
            for (ObservedProperty op : observedProperties) {
                sb.append(op.getName()).append("-");
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * <p>getObservedPropertyURI.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public List<String> getObservedPropertyURI() {
        List<String> results = new ArrayList<>();
        for (ObservedProperty op : observedProperties) {
            results.add(op.getUri());
        }
        return results;
    }


    public Map<ObservedProperty, String> getObservedPropertiesUomCode() {
        Map<ObservedProperty, String> results = new LinkedHashMap<>();
        for (Entry<ObservedProperty, UnitOfMeasurement> entry : observedPropertiesWithUom.entrySet()) {
            results.put(entry.getKey(), entry.getValue().getCode());
        }
        return results;
    }

    /**
     * <p>getUnitOfMeasurementCode.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUniqueUnitOfMeasurementCode() {
        if (observedPropertiesWithUom.size() == 1) {
            return observedPropertiesWithUom.values().iterator().next().getUri();
        } else if (observedPropertiesWithUom.size() > 1) {
            // shound not happen, return the first code
            return observedPropertiesWithUom.values().iterator().next().getUri();
        }
        return null;
    }

    public Map<ObservedProperty, Object> getObservedPropertiesResultValue() {
        return observedPropertiesResultValue;
    }

    /**
     * <p>Getter for the field <code>timeStamp</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.feeder.model.Timestamp} object.
     */
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    /**
     * <p>getEpsgCode.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getEpsgCode() {
        return Integer.toString(featureOfInterest.getPosition().getEpsgCode());
    }

    /**
     * <p>getLatitudeValue.</p>
     *
     * @return a double.
     */
    public double getLatitudeValue() {
        return featureOfInterest.getPosition().getLatitude();
    }

    /**
     * <p>getLongitudeValue.</p>
     *
     * @return a double.
     */
    public double getLongitudeValue() {
        return featureOfInterest.getPosition().getLongitude();
    }

    /**
     * <p>getAltitudeValue.</p>
     *
     * @return a double.
     */
    public double getAltitudeValue() {
        return featureOfInterest.getPosition().getAltitude();
    }


    /**
     * <p>isSetAltitudeValue.</p>
     *
     * @return a boolean.
     */
    public boolean isSetAltitudeValue() {
        return featureOfInterest != null && featureOfInterest.getPosition().getAltitude() != Position.VALUE_NOT_SET;
    }

    /**
     * <p>Getter for the field <code>observedProperty</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.feeder.model.ObservedProperty} object.
     */
    public Set<ObservedProperty> getObservedProperties() {
        return observedPropertiesWithUom.keySet();
    }

    /**
     * <p>Getter for the field <code>featureOfInterest</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.feeder.model.FeatureOfInterest} object.
     */
    protected FeatureOfInterest getFeatureOfInterest() {
        return featureOfInterest;
    }

    /**
     * <p>getUnitOfMeasurment.</p>
     *
     * @return a {@link org.n52.sos.importer.feeder.model.UnitOfMeasurement} object.

    protected UnitOfMeasurement getUnitOfMeasurment() {
        return unitOfMeasurement;
    }*/

    /**
     * <p>Getter for the field <code>offering</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.feeder.model.Offering} object.
     */
    public Offering getOffering() {
        return offering;
    }

    public Map<ObservedProperty, String> getObservedPropertiesMeasuredValueType() {
        return observedPropertiesMeasuredValueType;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("InsertObservation [resultValue=[");
        for (Object resultValue : observedPropertiesResultValue.values())  {
            builder.append(resultValue).append(',');
        }
        builder.append("], timeStamp=");
        builder.append(timeStamp);
        builder.append(", sensor=");
        builder.append(sensor);
        builder.append(", observedProperty=[");
        for (ObservedProperty observedProperty : observedPropertiesWithUom.keySet())  {
            builder.append(observedProperty).append(',');
        }
        builder.append("], featureOfInterest=");
        builder.append(featureOfInterest);
        builder.append(", unitOfMeasurement=[");
        for (UnitOfMeasurement unitOfMeasurement : observedPropertiesWithUom.values())  {
            builder.append(unitOfMeasurement).append(',');
        }
        builder.append("], offering=");
        builder.append(offering);
        builder.append(", measuredValueType=[");
        for (String measuredValueType : observedPropertiesMeasuredValueType.values())  {
            builder.append(measuredValueType).append(',');
        }
        builder.append("], omParameter=");
        builder.append(Arrays.toString(getOmParameters()));
        builder.append("]");
        return builder.toString();
    }

    public boolean isSetOmParameters() {
        return omParameters != null && !omParameters.isEmpty();
    }

    public OmParameter<?>[] getOmParameters() {
        return isSetOmParameters() ? Collections.unmodifiableList(omParameters)
                .toArray(new OmParameter<?>[omParameters.size()])
                : new OmParameter<?>[0];
    }

    public boolean hasFeatureParentFeature() {
        return featureOfInterest.hasParentFeature();
    }

    public String getParentFeatureIdentifier() {
        return featureOfInterest.getParentFeature();
    }
}
