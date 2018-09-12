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
package org.n52.sos.importer.feeder.importer;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.FeedingContext;
import org.n52.sos.importer.feeder.SosClient;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.InsertSensor;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.svalbard.encode.exception.EncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class InsertObservationTask implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(InsertObservationTask.class);

    private InsertObservation[] insertObservations;
    private SosClient sosClient;
    private List<URI> failedSensorInsertions;
    private Configuration configuration;
    private List<InsertObservation> failedObservations;
    private FeedingContext context;

    public InsertObservationTask(InsertObservation[] insertObservations, SosClient sosClient,
            List<URI> failedSensorInsertions, Configuration configuration, List<InsertObservation> failedObservations,
            FeedingContext context) {
        this.insertObservations = insertObservations.clone();
        this.sosClient = sosClient;
        this.failedSensorInsertions = failedSensorInsertions;
        this.configuration = configuration;
        this.failedObservations = failedObservations;
        this.context = context;
    }

    @Override
    public void run() {
        Timestamp newLastUsedTimestamp = null;
        insertObservations:
            for (InsertObservation io : insertObservations) {
                if (io != null) {
                    if (!sosClient.isSensorRegistered(io.getSensorURI()) &&
                            !failedSensorInsertions.contains(io.getSensorURI())) {
                        String assignedSensorId = null;
                        try {
                            InsertSensor insertSensor = new InsertSensor(io,
                                    getObservedProperties(io.getSensorURI(), insertObservations),
                                    getMeasuredValueTypes(io.getSensorURI(), insertObservations),
                                    getUnitsOfMeasurement(io.getSensorURI(), insertObservations),
                                    configuration.getReferenceValues(io.getSensorURI()));
                            assignedSensorId = sosClient.insertSensor(insertSensor).getKey().toString();
                        } catch (XmlException | IOException | EncodingException e) {
                            log(e);
                        }
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
                    } else {
                        LOG.debug("Sensor '{}' already registered.", io.getSensorURI());
                    }
                    String observationId = null;
                    try {
                        // sensor is registered -> insert the data
                        observationId = sosClient.insertObservation(io);
                    } catch (IOException e) {
                        log(e);
                    }
                    if (observationId == null || observationId.equalsIgnoreCase("")) {
                        LOG.error(String.format("Insert observation failed for sensor '%s'[%s]. Store: %s",
                                io.getSensorName(),
                                io.getSensorURI(),
                                io));
                        failedObservations.add(io);
                    } else if (observationId.equals(Configuration.SOS_OBSERVATION_ALREADY_CONTAINED)) {
                        LOG.debug(String.format("Observation was already contained in SOS: %s",
                                io));
                    } else if (configuration.isUseLastTimestamp()) {
                        newLastUsedTimestamp = io.getResultTime();
                    }
                }
            }
        if (context.shouldUpdateLastUsedTimestamp(newLastUsedTimestamp)) {
            context.setLastUsedTimestamp(newLastUsedTimestamp);
        }
    }

    private Collection<ObservedProperty> getObservedProperties(URI sensorURI, InsertObservation[] ios) {
        Set<ObservedProperty> observedProperties = new HashSet<>(ios.length);
        for (InsertObservation insertObservation : ios) {
            if (insertObservation.getSensorURI().equals(sensorURI)) {
                observedProperties.add(insertObservation.getObservedProperty());
            }
        }
        LOG.debug(String.format("Found '%d' Observed Properties for Sensor '%s': '%s'",
                observedProperties.size(), sensorURI, observedProperties));
        return observedProperties;
    }

    private Map<ObservedProperty, String> getMeasuredValueTypes(URI sensorURI, InsertObservation[] ios) {
        Map<ObservedProperty, String> measuredValueTypes = new HashMap<>(ios.length);
        for (InsertObservation insertObservation : ios) {
            if (insertObservation.getSensorURI().equals(sensorURI)) {
                measuredValueTypes.put(
                        insertObservation.getObservedProperty(),
                        insertObservation.getMeasuredValueType());
            }
        }
        LOG.debug(String.format("Found '%d' Measured value types for observed properties of sensor '%s': '%s'.",
                measuredValueTypes.size(), sensorURI, measuredValueTypes));
        return measuredValueTypes;
    }

    private Map<ObservedProperty, String> getUnitsOfMeasurement(URI sensorURI, InsertObservation[] ios) {
        Map<ObservedProperty, String> unitsOfMeasurement = new HashMap<>(ios.length);
        for (InsertObservation insertObservation : ios) {
            if (insertObservation.getSensorURI().equals(sensorURI)) {
                unitsOfMeasurement.put(
                        insertObservation.getObservedProperty(),
                        insertObservation.getUnitOfMeasurementCode());
            }
        }
        LOG.debug(String.format("Found '%d' units of measurement for observed properties of sensor '%s': '%s'.",
                unitsOfMeasurement.size(), sensorURI, unitsOfMeasurement));
        return unitsOfMeasurement;
    }

    private void log(Exception e) {
        LOG.error("Exception Thrown.", e.getLocalizedMessage());
        LOG.debug("Exception:", e);
    }
}