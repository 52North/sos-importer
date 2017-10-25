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
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.n52.oxf.om.x20.OmParameter;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;
import org.n52.sos.importer.feeder.util.LineHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleBasedObservationCollector extends CollectorSkeleton {

    private static final Logger LOG = LoggerFactory.getLogger(SampleBasedObservationCollector.class);

    // stores the Timestamp of the last insertObservations
    // (required for handling sample based files)
    private Timestamp sampleLastTimestamp;

    // The date information of the current sample
    private Timestamp sampleDate;

    private Pattern sampleIdPattern;

    private Pattern sampleSizePattern;

    private boolean isInSample;

    private int sampleSize;

    private int sampleSizeOffset;

    private int sampleDateOffset;

    private int sampleOffsetDifference;

    private int sampleDataOffset;

    private int sampleSizeDivisor;

    public SampleBasedObservationCollector() {
        super();
        sampleIdPattern = Pattern.compile(configuration.getSampleStartRegEx());
        sampleSizePattern = Pattern.compile(configuration.getSampleSizeRegEx());
        sampleSizeOffset = configuration.getSampleSizeOffset();
        sampleDateOffset = configuration.getSampleDateOffset();
        sampleSizeDivisor = configuration.getSampleSizeDivisor();
        sampleOffsetDifference = Math.abs(sampleDateOffset - sampleSizeOffset);
        sampleDataOffset = configuration.getSampleDataOffset();
    }

    @Override
    public void collectObservations(DataFile dataFile, CountDownLatch latch) throws IOException, ParseException {
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
            if (configuration.isUseLastTimestamp()) {
                // pointing back on first line with data because we are skipping by date and not line
                lastLine = dataFile.getFirstLineWithData();
            }
            if (configuration.getFirstLineWithData() == 0) {
                skipLines(lastLine + 1);
            } else {
                skipLines(lastLine);
            }
            String[] line;
            int sampleStartLine = 0;
            while ((line = parser.readNext()) != null && !stopped) {
                // if it is a sample based file, I need to get the following information
                // * date information (depends on last timestamp because of
                if (!isInSample && isSampleStart(line)) {
                    sampleStartLine = processSampleStart();
                    continue;
                }

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
            LOG.debug("SampleFile: isInSample: {}; lineCounter: {}; "
                    + "sampleStartLine: {}; sampleSize: {}; sampleDataOffset: {}",
                    isInSample, lineCounter,
                    sampleStartLine, sampleSize, sampleDataOffset);

            if (isInSample && isSampleEndReached(sampleStartLine)) {
                isInSample = false;
                LOG.debug("Current sample left");
            }
            context.setLastReadLine(lineCounter);
        } finally {
            latch.countDown();
        }
    }

    @Override
    protected InsertObservation getInsertObservationForMeasuredValue(int measureValueColumn, String[] line)
            throws ParseException {
        // TIMESTAMP
        final Timestamp timeStamp = dataFile.getTimeStamp(measureValueColumn, line);
        if (sampleLastTimestamp != null && timeStamp.isBefore(sampleLastTimestamp)) {
            sampleDate.applyDayDelta(1);
        }
        sampleLastTimestamp = new Timestamp().enrich(timeStamp);
        timeStamp.enrich(sampleDate);
        if (configuration.isUseLastTimestamp() && !verifyTimeStamp(timeStamp)) {
            return null;
        }
        // TODO implement using different templates in later version depending on the class of value
        LOG.debug("Timestamp: {}", timeStamp);
        // SENSOR
        final Sensor sensor = dataFile.getSensorForColumn(measureValueColumn, line);
        LOG.debug("Sensor: {}", sensor);
        // FEATURE OF INTEREST incl. Position
        final FeatureOfInterest foi = dataFile.getFoiForColumn(measureValueColumn, line);
        LOG.debug("Feature of Interest: {}", foi);
        // VALUE
        final Object value = dataFile.getValue(measureValueColumn, line);
        if (value.equals(Configuration.SOS_OBSERVATION_TYPE_NO_DATA_VALUE)) {
            return null;
        }
        // TODO implement handling for value == null => skip observation and log it, or logging is done in getValue(..)
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
        final Optional<List<OmParameter<?>>> omParameter = dataFile.getOmParameters(measureValueColumn, line);
        return new InsertObservation(sensor,
                foi,
                value,
                timeStamp,
                uom,
                observedProperty,
                offer,
                omParameter,
                dataFile.getType(measureValueColumn));
    }

    public boolean isSampleEndReached(int sampleStartLine) {
        return sampleStartLine + sampleSize + sampleDataOffset == lineCounter;
    }

    private boolean isSampleStart(String[] line) {
        return sampleIdPattern.matcher(configuration.restoreLine(line)).matches();
    }

    private int processSampleStart() throws IOException, ParseException {
        int sampleStartLine;
        sampleStartLine = lineCounter;
        sampleLastTimestamp = null;
        getSampleMetaData();
        isInSample = true;
        skipLines(sampleDataOffset - (lineCounter - sampleStartLine));
        return sampleStartLine;
    }

    private void getSampleMetaData() throws IOException, ParseException {
        LOG.trace("getSampleMetadata(...)");
        LOG.trace("dataOffset: {}; sizeOffset: {}; OffsetDifference: {}",
                sampleDataOffset, sampleSizeOffset, sampleOffsetDifference);
        if (sampleDateOffset < sampleSizeOffset) {
            skipLines(sampleDateOffset);
            sampleDate = parseSampleDate(parser.readNext());
            lineCounter++;
            skipLines(sampleOffsetDifference);
            sampleSize = parseSampleSize(parser.readNext());
            lineCounter++;
        } else {
            skipLines(sampleSizeOffset);
            sampleSize = parseSampleSize(parser.readNext());
            lineCounter++;
            skipLines(sampleOffsetDifference);
            sampleDate = parseSampleDate(parser.readNext());
            lineCounter++;
        }
        LOG.info("Parsed Metadata: Date: '{}'; Size: {}",
                sampleDate, sampleSize);
    }

    private int parseSampleSize(String[] line) throws ParseException {
        String lineToParse = configuration.restoreLine(line);
        Matcher matcher = sampleSizePattern.matcher(lineToParse);
        if (matcher.matches() && matcher.groupCount() == 1) {
            String sampleSizeTmp = matcher.group(1);
            return Integer.parseInt(sampleSizeTmp) / sampleSizeDivisor;
            // TODO handle NumberformatException
        }
        throw new ParseException(
                String.format(
                        "Could not extract sampleSize from '%s' using "
                                + "regular expression '%s' (Offset is always 42).",
                                lineToParse,
                                sampleSizePattern.pattern()), 42);
    }

    private Timestamp parseSampleDate(String[] line) throws ParseException {
        String dateInfoPattern = configuration.getSampleDatePattern();
        String regExToExtractDateInfo = configuration.getSampleDateExtractionRegEx();
        String timestampInformation = configuration.restoreLine(line);
        LOG.trace("parseSampleDate: dateInfoPattern: '{}'; extractRegEx: '{}'; line: '{}'",
                dateInfoPattern, regExToExtractDateInfo, timestampInformation);
        return new Timestamp().enrich(timestampInformation, regExToExtractDateInfo, dateInfoPattern);
    }

}
