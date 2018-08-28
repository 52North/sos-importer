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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import org.n52.sos.importer.feeder.Collector;
import org.n52.sos.importer.feeder.Configuration;
import org.n52.sos.importer.feeder.DataFile;
import org.n52.sos.importer.feeder.FeedingContext;
import org.n52.sos.importer.feeder.collector.csv.CsvParser;
import org.n52.sos.importer.feeder.collector.csv.WrappedCSVParser;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CollectorSkeleton implements Collector {

    private static final Logger LOG = LoggerFactory.getLogger(CollectorSkeleton.class);

    protected Configuration configuration;

    protected DataFile dataFile;

    protected FeedingContext context;

    protected Timestamp newLastUsedTimestamp;

    protected int lineCounter;

    protected CsvParser parser;

    protected boolean stopped;

    public CollectorSkeleton() {
        super();
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setFeedingContext(FeedingContext context) {
        this.context = context;
    }

    @Override
    public void stopCollecting() {
        stopped = true;
    }

    protected void logExceptionThrownDuringParsing(Exception exception) {
        LOG.error("Could not retrieve all information required for insert observation because of parsing error:"
                + " {}: {}. Skipped this one.",
                exception.getClass().getName(),
                exception.getMessage());
        LOG.debug("Exception stack trace:", exception);
    }

    protected boolean isHeaderLine(String[] storedHeaderline, String[] lineToCheck) {
        boolean isHeaderLine = Arrays.equals(storedHeaderline, lineToCheck);
        if (isHeaderLine) {
            LOG.debug("Headerline found: '{}'", Arrays.toString(lineToCheck));
        }
        return isHeaderLine;
    }

    protected void skipLines(int skipCount) throws IOException {
        // get the number of lines to skip (coming from already read lines)
        String[] values;
        int skipLimit = parser.getSkipLimit();
        int linesToSkip = skipCount;
        while (linesToSkip > skipLimit) {
            values = parser.readNext();
            LOG.trace(String.format("\t\tSkip CSV line #%d: %s", lineCounter + 1, configuration.restoreLine(values)));
            linesToSkip--;
            lineCounter++;
        }
    }

    /**
     * Returns a CSVReader instance for the given {@link DataFile} using the configuration
     * including the defined values for: first line with data, separator, escape, and text qualifier.
     *
     *@param dataFile the <code>DataFile</code> to read
     *
     * @return a <code>CSVReader</code> instance
     *
     * @throws java.io.IOException if any.
     */
    protected CsvParser getCSVReader(DataFile dataFile) throws IOException {
        LOG.trace("getCSVReader()");
        parser = null;
        if (configuration.isCsvParserDefined()) {
            String csvParser = configuration.getCsvParser();
            try {
                Class<?> clazz = Class.forName(csvParser);
                Constructor<?> constructor = clazz.getConstructor((Class<?>[]) null);
                Object instance = constructor.newInstance();
                if (CsvParser.class.isAssignableFrom(instance.getClass())) {
                    parser = (CsvParser) instance;
                }
            } catch (ClassNotFoundException |
                    NoSuchMethodException |
                    SecurityException |
                    InstantiationException |
                    IllegalAccessException |
                    IllegalArgumentException |
                    InvocationTargetException e) {
                String errorMsg = String.format("Could not load defined CsvParser implementation class '%s'. "
                        + "Cancel import",
                        csvParser);
                LOG.error(errorMsg);
                LOG.debug("Exception thrown: {}", e.getMessage(), e);
                throw new IllegalArgumentException(errorMsg, e);
            }
        }
        if (parser == null) {
            parser = new WrappedCSVParser();
        }
        Reader fr = new InputStreamReader(
                new FileInputStream(dataFile.getCanonicalPath()), dataFile.getEncoding());
        BufferedReader br = new BufferedReader(fr);
        parser.init(br, configuration);
        return parser;
    }

    protected String[] readHeaderLine(DataFile dataFile)
            throws UnsupportedEncodingException, FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(
                                dataFile.getCanonicalPath()),
                                dataFile.getEncoding()))) {
            int counter = 1;
            for (String line; (line = br.readLine()) != null;) {
                if (counter == dataFile.getHeaderLine()) {
                    return line.split(Character.toString(configuration.getCsvSeparator()));
                } else {
                    counter++;
                }
            }
        }
        return null;
    }

    protected InsertObservation[] getInsertObservationsForOneLine(String[] line) {
        final ArrayList<InsertObservation> result = new ArrayList<>(line.length);
        int[] measuredValueColumnIds = dataFile.getMeasuredValueColumnIds();
        for (int measureValueColumn : measuredValueColumnIds) {
            LOG.debug("Parsing values for measured value column #{}/{}",
                    measureValueColumn,
                    measuredValueColumnIds.length);
            try {
                final InsertObservation io = getInsertObservationForMeasuredValue(measureValueColumn, line);
                if (io != null) {
                    result.add(io);
                }
            } catch (ParseException | NumberFormatException | URISyntaxException e) {
                logExceptionThrownDuringParsing(e);
            }
        }
        result.trimToSize();
        return result.toArray(new InsertObservation[result.size()]);
    }

    protected abstract InsertObservation getInsertObservationForMeasuredValue(int measureValueColumn, String[] line)
            throws ParseException, URISyntaxException;

    protected boolean verifyTimeStamp(Timestamp timeStamp) {
        if (context.getLastUsedTimestamp() == null || timeStamp.isAfter(context.getLastUsedTimestamp())) {
            // update newLastUsedTimestamp, if timeStamp is new or After:
            if (newLastUsedTimestamp == null || timeStamp.isAfter(newLastUsedTimestamp)) {
                newLastUsedTimestamp = timeStamp;
            } else {
                // abort Insertion
                LOG.debug("skip InsertObservation with timestamp '{}' because not after LastUsedTimestamp '{}'",
                        timeStamp, context.getLastUsedTimestamp());
                return false;
            }
        }
        return true;
    }

}