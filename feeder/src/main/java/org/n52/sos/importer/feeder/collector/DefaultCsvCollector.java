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
import org.n52.sos.importer.feeder.model.PhenomenonTime;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;
import org.n52.sos.importer.feeder.util.LineHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Collector} implementation which fits the most basic CSV files.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 *
 */
public class DefaultCsvCollector extends CollectorSkeleton {

    static final Logger LOG = LoggerFactory.getLogger(DefaultCsvCollector.class);

    @Override
    public void collectObservations(DataFile dataFile, CountDownLatch latch)
            throws IOException {
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
            if (dataFile.getHeaderLine() > -1) {
                headerLine = readHeaderLine(dataFile);
            }
            if (!dataFile.areMeasureValuesAvailable()) {
                LOG.error("No measured value columns found in configuration");
                return;
            }
            int lastLine = context.getLastReadLine();
            if (configuration.isUseLastTimestamp() || lastLine < configuration.getFirstLineWithData()) {
                // pointing back on first line with data because we are skipping by date and not line
                lastLine = dataFile.getFirstLineWithData();
            }
            skipLines(lastLine);
            String[] line;
            while ((line = parser.readNext()) != null && !stopped) {
                if (!configuration.isLineIgnorable(line) &&
                        configuration.containsData(line) &&
                        configuration.isParsedColumnCountCorrect(line.length) &&
                        !isHeaderLine(headerLine, line)) {
                    LineHelper.trimValues(line);
                    LOG.debug(String.format("Handling CSV line #%d: %s", lineCounter + 1, Arrays.toString(line)));
                    final InsertObservation[] ios = getInsertObservationsForOneLine(line);
                    if (ios != null && ios.length > 0) {
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
            context.setLastReadLine(lineCounter);
        } finally {
            latch.countDown();
        }
    }

    @Override
    protected InsertObservation getInsertObservationForMeasuredValue(int measureValueColumn, String[] line)
            throws ParseException, URISyntaxException {
        LOG.trace("getInsertObservationForMeasuredValue(..)");
        // TIMESTAMPS
        Timestamp resultTime = dataFile.getResultTime(measureValueColumn, line);
        if (configuration.isUseLastTimestamp() && !verifyTimeStamp(resultTime)) {
            return null;
        }
        LOG.debug("Result Time    : {}", resultTime);
        PhenomenonTime phenomenonTime = null;
        if (configuration.arePhenomenonTimesAvailable(measureValueColumn)) {
            phenomenonTime = dataFile.getPhenomenonTime(measureValueColumn, line);
        } else {
            phenomenonTime = new PhenomenonTime(resultTime);
        }
        LOG.debug("Phenomenon Time: {}", phenomenonTime);
        // SENSOR
        Sensor sensor = dataFile.getSensor(measureValueColumn, line);
        LOG.debug("Sensor: {}", sensor);
        // FEATURE OF INTEREST incl. Position
        FeatureOfInterest foi = dataFile.getFoiForColumn(measureValueColumn, line);
        LOG.debug("Feature of Interest: {}", foi);
        // VALUE
        Object value = dataFile.getValue(measureValueColumn, line);
        if (value == null || value.equals(Configuration.SOS_OBSERVATION_TYPE_NO_DATA_VALUE)) {
            return null;
        }
        LOG.debug("Value: {}", value.toString());
        // UOM CODE
        UnitOfMeasurement uom = dataFile.getUnitOfMeasurement(measureValueColumn, line);
        LOG.debug("UomCode: '{}'", uom);
        // OBSERVED_PROPERTY
        ObservedProperty observedProperty = dataFile.getObservedProperty(measureValueColumn, line);
        LOG.debug("ObservedProperty: {}", observedProperty);
        // OFFERING
        Offering offer = dataFile.getOffering(sensor);
        LOG.debug("Offering: {}", offer);
        // OM:PARAMETER
        Optional<List<NamedValue<?>>> omParameter = dataFile.getOmParameters(measureValueColumn, line);
        return new InsertObservation(sensor,
                foi,
                value,
                resultTime,
                phenomenonTime,
                uom,
                observedProperty,
                offer,
                omParameter,
                dataFile.getType(measureValueColumn));
    }

}
