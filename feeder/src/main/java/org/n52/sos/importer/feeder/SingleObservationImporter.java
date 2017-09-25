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
package org.n52.sos.importer.feeder;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.RegisterSensor;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleObservationImporter extends ImporterSkeleton implements Importer {

    private static final Logger LOG = LoggerFactory.getLogger(SingleObservationImporter.class);

    @Override
    public void addObservationForImporting(InsertObservation... insertObservations)
            throws OXFException, XmlException, IOException {
        if (insertObservations == null) {
            return;
        }
        Timestamp newLastUsedTimestamp = null;
        insertObservations:
            for (InsertObservation io : insertObservations) {
                if (io != null) {
                    if (!sosClient.isSensorRegistered(io.getSensorURI()) &&
                            !failedSensorInsertions.contains(io.getSensorURI())) {
                        RegisterSensor rs = new RegisterSensor(io,
                                getObservedProperties(io.getSensorURI(), insertObservations),
                                getMeasuredValueTypes(io.getSensorURI(), insertObservations),
                                getUnitsOfMeasurement(io.getSensorURI(), insertObservations));
                        String assignedSensorId = sosClient.registerSensor(rs);
                        if (assignedSensorId == null || assignedSensorId.equalsIgnoreCase("")) {
                            LOG.error(String.format(
                                    "Sensor '%s'[%s] could not be registered at SOS."
                                            + "Skipping insert observation for this and store it.",
                                            io.getSensorName(),
                                            io.getSensorURI()));
                            failedObservations.add(io);
                            failedSensorInsertions.add(io.getSensorURI());
                            continue insertObservations;
                        }
                    }
                    // sensor is registered -> insert the data
                    String observationId = sosClient.insertObservation(io);
                    if (observationId == null || observationId.equalsIgnoreCase("")) {
                        LOG.error(String.format("Insert observation failed for sensor '%s'[%s]. Store: %s",
                                io.getSensorName(),
                                io.getSensorURI(),
                                io));
                        failedObservations.add(io);
                    } else if (observationId.equals(Configuration.SOS_OBSERVATION_ALREADY_CONTAINED)) {
                        LOG.debug(String.format("Observation was already contained in SOS: %s",
                                io));
                    } else {
                        newLastUsedTimestamp = io.getTimeStamp();
                    }
                }
            }
        if (context.shouldUpdateLastUsedTimestamp(newLastUsedTimestamp)) {
            context.setLastUsedTimestamp(newLastUsedTimestamp);
        }
    }

    private Collection<ObservedProperty> getObservedProperties(String sensorURI, InsertObservation[] ios) {
        Set<ObservedProperty> observedProperties = new HashSet<>(ios.length);
        for (InsertObservation insertObservation : ios) {
            if (insertObservation.getSensorURI().equalsIgnoreCase(sensorURI)) {
                observedProperties.add(insertObservation.getObservedProperty());
            }
        }
        LOG.debug(String.format("Found '%d' Observed Properties for Sensor '%s': '%s'",
                observedProperties.size(), sensorURI, observedProperties));
        return observedProperties;
    }

    private Map<ObservedProperty, String> getMeasuredValueTypes(String sensorURI, InsertObservation[] ios) {
        Map<ObservedProperty, String> measuredValueTypes = new HashMap<>(ios.length);
        for (InsertObservation insertObservation : ios) {
            if (insertObservation.getSensorURI().equalsIgnoreCase(sensorURI)) {
                measuredValueTypes.put(
                        insertObservation.getObservedProperty(),
                        insertObservation.getMeasuredValueType());
            }
        }
        LOG.debug(String.format("Found '%d' Measured value types for observed properties of sensor '%s': '%s'.",
                measuredValueTypes.size(), sensorURI, measuredValueTypes));
        return measuredValueTypes;
    }

    private Map<ObservedProperty, String> getUnitsOfMeasurement(String sensorURI, InsertObservation[] ios) {
        Map<ObservedProperty, String> unitsOfMeasurement = new HashMap<>(ios.length);
        for (InsertObservation insertObservation : ios) {
            if (insertObservation.getSensorURI().equalsIgnoreCase(sensorURI)) {
                unitsOfMeasurement.put(
                        insertObservation.getObservedProperty(),
                        insertObservation.getUnitOfMeasurementCode());
            }
        }
        LOG.debug(String.format("Found '%d' units of measurement for observed properties of sensor '%s': '%s'.",
                unitsOfMeasurement.size(), sensorURI, unitsOfMeasurement));
        return unitsOfMeasurement;
    }

}
