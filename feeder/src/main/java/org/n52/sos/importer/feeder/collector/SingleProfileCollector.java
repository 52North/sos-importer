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
package org.n52.sos.importer.feeder.collector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import org.n52.shetland.ogc.om.NamedValue;
import org.n52.sos.importer.feeder.Application;
import org.n52.sos.importer.feeder.Collector;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Position;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;
import org.n52.sos.importer.feeder.util.LineHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Collector} for profile based CSV files. A profile is located at one
 * location and contain several series at different depth levels, for example.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class SingleProfileCollector extends CollectorSkeleton {

    private static final Logger LOG = LoggerFactory.getLogger(SingleProfileCollector.class);

    private Timestamp timestamp;

    private Sensor sensor;

    private FeatureOfInterest foi;

    @Override
    public void collectObservations(DataFile dataFile, CountDownLatch latch)
            throws IOException, ParseException, URISyntaxException {
        if (configuration == null) {
            LOG.error("Configuration not set!");
            return;
        }
        if (context == null) {
            LOG.error("Context not set!");
            return;
        }
        try {
            parser = getCSVReader(dataFile);
            String[] headerLine = new String[0];
            this.dataFile = dataFile;
            lineCounter = dataFile.getFirstLineWithData();
            if (!dataFile.areMeasureValuesAvailable()) {
                LOG.error("No measured value columns found in configuration");
                return;
            }
            String[] line;
            processSampleStart();
            skipLines(1);
            while ((line = parser.readNext()) != null && !stopped) {
                if (!configuration.isLineIgnorable(line) &&
                        configuration.containsData(line) &&
                        configuration.isParsedColumnCountCorrect(line.length) &&
                        !isHeaderLine(headerLine, line)) {
                    LineHelper.trimValues(line);
                    LOG.debug(String.format("Handling CSV line #%d: %s", lineCounter + 1, Arrays.toString(line)));
                    final InsertObservation[] ios = getInsertObservationsForOneLine(line);
                    if (ios != null && ios.length > 1) {
                        context.addObservationForImporting(ios);
                    }
                    LOG.debug(Application.heapSizeInformation());
                } else {
                    LOG.debug("\t\tSkip CSV line #{}; Raw data: '{}'", lineCounter + 1, Arrays.toString(line));
                }
                lineCounter++;
                if (lineCounter % 10000 == 0) {
                    LOG.info("Handled line {}.", lineCounter);
                }
            }
        } finally {
            latch.countDown();
        }
    }

    private void processSampleStart() throws IOException, URISyntaxException {
        String[] line;
        // long, lat, alt
        double[] values = new double[3];
        while ((line = parser.readNext()) != null) {
            if (line[0].equalsIgnoreCase(" Device")) {
                LOG.debug("Creating sensor from '{}'", Arrays.toString(line));
                sensor = new Sensor(line[1], line[1]);
            } else if (line[0].equalsIgnoreCase(" Cast time (UTC)")) {
                LOG.debug("Creating timestamp from '{}'", Arrays.toString(line));
                timestamp = new Timestamp(line[1].replaceAll(" ", "T").concat("Z"));
                timestamp.setTimezone(0);
            } else if (line[0].equalsIgnoreCase(" Start latitude")) {
                values[1] = Double.parseDouble(line[1]);
            } else if (line[0].equalsIgnoreCase(" Start longitude")) {
                values[0] = Double.parseDouble(line[1]);
            } else if (line[0].equalsIgnoreCase(" Start altitude")) {
                values[2] = Double.parseDouble(line[1]);
            } else if (line[0].equalsIgnoreCase(" ")) {
                break;
            }
        }
        String deg = "deg";
        Position position = new Position(values , new String[] {deg, deg, "m"}, 4326);
        String identifier = generateFeatureIdentifier(values);
        foi = new FeatureOfInterest(identifier, identifier, position);
        foi.setParentFeature(configuration.getParentFeatureFromAdditionalMetadata());
    }

    private String generateFeatureIdentifier(double[] values) {
        return new StringBuilder("profile-observation-at-")
                .append(values[1])
                .append("-")
                .append(values[0])
                .toString()
                .replaceAll("\\.|,", "_");
    }

    @Override
    protected InsertObservation getInsertObservationForMeasuredValue(int measureValueColumn, String[] line)
            throws ParseException, URISyntaxException {
        // TIMESTAMP
        if (configuration.isUseLastTimestamp() && !verifyTimeStamp(timestamp)) {
            return null;
        }
        LOG.debug("Timestamp: {}", timestamp);
        LOG.debug("Sensor: {}", sensor);
        LOG.debug("Feature of Interest: {}", foi);
        // VALUE
        final Object value = dataFile.getValue(measureValueColumn, line);
        if (value.equals(Configuration.SOS_OBSERVATION_TYPE_NO_DATA_VALUE)) {
            return null;
        }
        LOG.debug("Value: {}", value.toString());
        // UOM CODE
        final UnitOfMeasurement uom = dataFile.getUnitOfMeasurement(measureValueColumn, line);
        LOG.debug("UomCode: '{}'", uom);
        // OBSERVED_PROPERTY
        final ObservedProperty observedProperty = dataFile.getObservedProperty(measureValueColumn, line);
        LOG.debug("ObservedProperty: {}", observedProperty);
        // OFFERING
        final Offering offer = dataFile.getOffering(sensor);
        LOG.debug("Offering: {}", offer);
        // OM:PARAMETER
        final Optional<List<NamedValue<?>>> omParameter = dataFile.getOmParameters(measureValueColumn, line);
        return new InsertObservation(sensor,
                foi,
                value,
                timestamp,
                uom,
                observedProperty,
                offer,
                omParameter,
                dataFile.getType(measureValueColumn));
    }

}
