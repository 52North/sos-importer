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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.OXFException;
import org.n52.oxf.adapter.OperationResult;
import org.n52.oxf.om.x20.OmParameter;
import org.n52.oxf.ows.ExceptionReport;
import org.n52.oxf.ows.OWSException;
import org.n52.oxf.ows.OwsExceptionCode;
import org.n52.oxf.ows.ServiceDescriptor;
import org.n52.oxf.ows.capabilities.OperationsMetadata;
import org.n52.oxf.request.MimetypeAwareRequestParameters;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder;
import org.n52.oxf.sos.adapter.ISOSRequestBuilder.Binding;
import org.n52.oxf.sos.adapter.SOSAdapter;
import org.n52.oxf.sos.adapter.wrapper.SOSWrapper;
import org.n52.oxf.sos.adapter.wrapper.SosWrapperFactory;
import org.n52.oxf.sos.adapter.wrapper.builder.ObservationTemplateBuilder;
import org.n52.oxf.sos.capabilities.ObservationOffering;
import org.n52.oxf.sos.capabilities.SOSContents;
import org.n52.oxf.sos.observation.BooleanObservationParameters;
import org.n52.oxf.sos.observation.CountObservationParameters;
import org.n52.oxf.sos.observation.MeasurementObservationParameters;
import org.n52.oxf.sos.observation.ObservationParameters;
import org.n52.oxf.sos.observation.TextObservationParameters;
import org.n52.oxf.sos.request.v100.RegisterSensorParameters;
import org.n52.oxf.sos.request.v200.InsertSensorParameters;
import org.n52.sos.importer.feeder.Configuration.ImportStrategy;
import org.n52.sos.importer.feeder.csv.CsvParser;
import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.InsertObservation;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.RegisterSensor;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.TimeSeries;
import org.n52.sos.importer.feeder.model.TimeSeriesRepository;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;
import org.n52.sos.importer.feeder.util.DescriptionBuilder;
import org.n52.sos.importer.feeder.util.InvalidColumnCountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.opengis.sos.x10.InsertObservationResponseDocument;
import net.opengis.sos.x10.InsertObservationResponseDocument.InsertObservationResponse;
import net.opengis.sos.x10.RegisterSensorResponseDocument;
import net.opengis.swes.x20.InsertSensorResponseDocument;

/**
 * Handles connection to SOS and provides an easy to use interface.<br>
 * Now this class supports only OGC SOS <b>1.0.0</b>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public final class Feeder {

    private static final String N_M_FORMAT = "%s %s";

    private static final String PROBLEM_WITH_OXF_EXCEPTION_THROWN = "Problem with OXF. Exception thrown: %s";

    private static final String EXCEPTION_THROWN = "Exception thrown: %s%n%s";

    private static final String EXCEPTION_CODE_STRING = "ExceptionCode: '%s' because of '%s'%n";

    private static final String SOS_2_0_INSERT_OBSERVATION_RESPONSE =
            "SOS 2.0 InsertObservation doesn't return the assigned id";

    private static final String INSERT_OBSERVATION_FAILED = "Insert observation failed for sensor '%s'[%s]. Store: %s";

    private static final String SENSOR_REGISTERED_WITH_ID = "Sensor registered at SOS  '%s' with assigned id '%s'";

    private static final String OBSERVATION_INSERTED_SUCCESSFULLY =
            "Observation inserted successfully.";

    private static final String OBSERVATION_INSERTED_SUCCESSFULLY_WITH_ID =
            "Observation inserted successfully. Returned id: %s";

    private static final String SOS_VERSION_100 = "1.0.0";

    private static final String SOS_200_VERSION = "2.0.0";

    private static final Logger LOG = LoggerFactory.getLogger(Feeder.class);

    private static final String SML_101_FORMAT_URI = "http://www.opengis.net/sensorML/1.0.1";

    private static final String OM_200_SAMPLING_FEATURE =
            "http://www.opengis.net/def/samplingFeatureType/OGC-OM/2.0/SF_SamplingPoint";

    private final URL sosUrl;
    private final String sosVersion;
    private final SOSWrapper sosWrapper;
    private final ServiceDescriptor serviceDescriptor;
    private final List<String> registeredSensors;
    private final List<InsertObservation> failedInsertObservations;
    private int lastLine;
    private final Binding sosBinding;
    private Map<String, String> offerings;
    private final DescriptionBuilder sensorDescBuilder;

    private String[] headerLine;

    private boolean isSampleBasedDataFile;

    // Identified on localhost on development system
    // Default value: 5000
    // Max possible value: 12500
    private int hunkSize = 5000;

    private Configuration configuration;

    // stores the Timestamp of the last insertObservations
    // (required for handling sample based files)
    private Timestamp sampleLastTimestamp;

    // stores the Timestamp of the last insertObservations
    private Timestamp lastUsedTimestamp;

    private boolean isUseLastTimestamp;

    private Timestamp newLastUsedTimestamp;

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

    private int lineCounter;

    private final int[] ignoredColumns;

    private Pattern[] ignorePatterns;

    // adding 25s
    private int sweArrayObservationTimeOutBuffer = 25000;

    private int sampleSizeDivisor;

    private String skipReason = "";

    /**
     * <p>Constructor for SensorObservationService.</p>
     *
     * @param config a {@link org.n52.sos.importer.feeder.Configuration} object.
     * @throws org.n52.oxf.ows.ExceptionReport if any.
     * @throws org.n52.oxf.OXFException if any.
     * @throws java.net.MalformedURLException if any.
     */
    public Feeder(final Configuration config)
            throws ExceptionReport, OXFException, MalformedURLException {
        LOG.trace(String.format("SensorObservationService(%s)", config.toString()));
        configuration = config;
        sosUrl = config.getSosUrl();
        sosVersion = config.getSosVersion();
        sosBinding = getBinding(config.getSosBinding());
        isSampleBasedDataFile = config.isSamplingFile();
        ignoredColumns = config.getIgnoredColumnIds();
        if (isSampleBasedDataFile) {
            sampleIdPattern = Pattern.compile(config.getSampleStartRegEx());
            sampleSizePattern = Pattern.compile(config.getSampleSizeRegEx());
            sampleSizeOffset = config.getSampleSizeOffset();
            sampleDateOffset = config.getSampleDateOffset();
            sampleSizeDivisor = config.getSampleSizeDivisor();
            sampleOffsetDifference = Math.abs(sampleDateOffset - sampleSizeOffset);
            sampleDataOffset = config.getSampleDataOffset();
        }
        if (sosBinding == null) {
            sosWrapper = SosWrapperFactory.newInstance(sosUrl.toString(), sosVersion);
        } else {
            sosWrapper = SosWrapperFactory.newInstance(sosUrl.toString(), sosVersion, sosBinding);
        }
        serviceDescriptor = sosWrapper.getServiceDescriptor();
        if (sosVersion.equals(SOS_200_VERSION)) {
            sensorDescBuilder = new DescriptionBuilder(false);
        } else {
            sensorDescBuilder = new DescriptionBuilder();
        }
        failedInsertObservations = new LinkedList<>();
        registeredSensors = new LinkedList<>();
        offerings = new HashMap<>();
        if (config.getHunkSize() > 0) {
            hunkSize = config.getHunkSize();
        }
        if (config.isIgnoreLineRegExSet()) {
            ignorePatterns = config.getIgnoreLineRegExPatterns();
        }
        if (config.isInsertSweArrayObservationTimeoutBufferSet()) {
            sweArrayObservationTimeOutBuffer = config.getInsertSweArrayObservationTimeoutBuffer();
        }
        if (config.getImportStrategy().equals(ImportStrategy.SweArrayObservationWithSplitExtension)) {
            LOG.info("Using {}ms timeout buffer during insert observation requests. "
                    + "Change "
                    + "<SosImportConfiguration><SosMetadata><insertSweArrayObservationTimeoutBuffer>"
                    + " if required.",
                    sweArrayObservationTimeOutBuffer);
        }
        isUseLastTimestamp = config.isUseLastTimestamp();
    }

    private Binding getBinding(final String binding) throws OXFException {
        if (binding == null  || binding.isEmpty()) {
            return null;
        }
        if (binding.equals(Binding.POX.name())) {
            return Binding.POX;
        }
        if (binding.equals(Binding.SOAP.name())) {
            return Binding.SOAP;
        }
        throw new OXFException(String.format("Binding not supported by this implementation: %s. Use '%s' or '%s'.",
                binding,
                Binding.POX.name(),
                Binding.SOAP.name()));
    }

    /**
     * <p>isAvailable.</p>
     *
     * @return a boolean.
     */
    public boolean isSosAvailable() {
        return sosWrapper.getServiceDescriptor() != null;
    }

    /**
     * Checks for <b>RegisterSensor</b> and <b>InsertObservation</b> operations.
     *
     * @return <code>true</code> if RegisterSensor and InsertObservation
     *         operations are listed in the capabilities of this SOS, <br>
     *         else <code>false</code>.
     */
    public boolean isSosTransactional() {
        if (!isServiceDescriptorAvailable()) {
            return false;
        }
        final OperationsMetadata opMeta = serviceDescriptor.getOperationsMetadata();
        LOG.debug(String.format("OperationsMetadata found: %s", opMeta));
        // check for (Insert|Register)Sensor and InsertObservationOperation
        if ((opMeta.getOperationByName(SOSAdapter.REGISTER_SENSOR) != null ||
                opMeta.getOperationByName(SOSAdapter.INSERT_SENSOR) != null)
                &&
                opMeta.getOperationByName(SOSAdapter.INSERT_OBSERVATION) != null) {
            LOG.debug(String.format("Found all required operations: (%s|%s), %s",
                    SOSAdapter.REGISTER_SENSOR,
                    SOSAdapter.INSERT_SENSOR,
                    SOSAdapter.INSERT_OBSERVATION));
            return true;
        }
        return false;
    }

    /**
     * Checks for <b>isUseLastTimestamp</b> and <b>newLastUsedTimestamp</b>
     * operations.
     * @return <code>true</code> if isUseLastTimestamp is true and
     *         newLastUsedTimestamp is after lastUsedTimestamp
     *         else <code>false</code>.
     */
    private boolean shouldUpdateLastUsedTimestamp() {
        return isUseLastTimestamp
                && (lastUsedTimestamp == null && newLastUsedTimestamp != null
                ||
                newLastUsedTimestamp != null  && newLastUsedTimestamp.isAfter(lastUsedTimestamp));
    }

    /**
     * <p>importData.</p>
     *
     * @param dataFile a {@link org.n52.sos.importer.feeder.DataFile} object.
     * @throws java.io.IOException if any.
     * @throws org.n52.oxf.OXFException if any.
     * @throws org.apache.xmlbeans.XmlException if any.
     * @throws java.lang.IllegalArgumentException if any.
     * @throws java.text.ParseException if any.
     */
    public void importData(final DataFile dataFile)
            throws IOException, OXFException, XmlException, IllegalArgumentException, ParseException {
        LOG.trace("importData()");
        // 0 Get line
        final CsvParser cr = dataFile.getCSVReader();
        String[] values;
        lineCounter = dataFile.getFirstLineWithData();
        if (dataFile.getHeaderLine() > -1 && headerLine == null) {
            headerLine = readHeaderLine(dataFile);
        }
        final int failedObservationsBefore = failedInsertObservations.size();
        int numOfObsTriedToInsert = 0;
        // 1 Get all measured value columns =: mvCols
        final int[] mVCols = dataFile.getMeasuredValueColumnIds();
        if (mVCols == null || mVCols.length == 0) {
            LOG.error("No measured value columns found in configuration");
            return;
        }
        if (isUseLastTimestamp) {
            lastLine = dataFile.getFirstLineWithData();
            // TODO pointing back on first line with data secured?
        }
        if (configuration.getFirstLineWithData() == 0) {
            skipLines(cr, lastLine + 1);
        } else {
            skipLines(cr, lastLine);
        }
        switch (configuration.getImportStrategy()) {
            case SweArrayObservationWithSplitExtension:
                LOG.debug("Using hunkSize '{}'", hunkSize);
                long startReadingFile = System.currentTimeMillis();
                TimeSeriesRepository timeSeriesRepository = new TimeSeriesRepository();
                int currentHunk = 0;
                int sampleStartLine = lineCounter;
                while ((values = cr.readNext()) != null) {
                    // if it is a sample based file, I need to get the following information
                    // * date information (depends on last timestamp because of
                    if (isSampleBasedDataFile && !isInSample && isSampleStart(values)) {
                        sampleStartLine = processSampleStart(cr);
                        continue;
                    }
                    if (!isLineIgnorable(values) &&
                            containsData(values) &&
                            isSizeValid(dataFile, values) &&
                            !isHeaderLine(values)) {
                        logLine(values);
                        final InsertObservation[] ios = getInsertObservations(values, mVCols, dataFile);
                        timeSeriesRepository.addObservations(ios);
                        numOfObsTriedToInsert += ios.length;
                        LOG.debug(Application.heapSizeInformation());
                        if (currentHunk == hunkSize) {
                            currentHunk = 0;
                            insertTimeSeries(timeSeriesRepository);
                            timeSeriesRepository = new TimeSeriesRepository();
                        } else {
                            currentHunk++;
                        }
                    } else {
                        logSkippedLine(values);
                    }
                    incrementLineCounter();
                    if (isSampleBasedDataFile) {
                        LOG.debug("SampleFile: {}; isInSample: {}; lineCounter: {}; "
                                + "sampleStartLine: {}; sampleSize: {}; sampleDataOffset: {}",
                                isSampleBasedDataFile, isInSample, lineCounter,
                                sampleStartLine, sampleSize, sampleDataOffset);

                        if (isInSample && isSampleEndReached(sampleStartLine)) {
                            isInSample = false;
                            LOG.debug("Current sample left");
                        }
                    }
                }
                if (!timeSeriesRepository.isEmpty()) {
                    insertTimeSeries(timeSeriesRepository);
                }
                if (shouldUpdateLastUsedTimestamp()) {
                    lastUsedTimestamp = newLastUsedTimestamp;
                }
                lastLine = lineCounter;
                logTiming(startReadingFile);
                break;
            case SingleObservation:
                startReadingFile = System.currentTimeMillis();
                // for each line
                while ((values = cr.readNext()) != null) {
                    if (!isLineIgnorable(values) &&
                            isSizeValid(dataFile, values) &&
                            containsData(values) &&
                            !isHeaderLine(values)) {
                        trimValues(values);
                        logLine(values);
                        final InsertObservation[] ios = getInsertObservations(values, mVCols, dataFile);
                        numOfObsTriedToInsert += ios.length;
                        insertObservationsForOneLine(ios, values, dataFile);
                        LOG.debug(Application.heapSizeInformation());
                    } else {
                        logSkippedLine(values);
                    }
                    incrementLineCounter();
                }
                if (shouldUpdateLastUsedTimestamp()) {
                    lastUsedTimestamp = newLastUsedTimestamp;
                }
                lastLine = lineCounter;
                logTiming(startReadingFile);
                break;
            default:
                LOG.error("Not supported strategy given '{}'.",
                        configuration.getImportStrategy());
                break;
        }
        final int newFailedObservationsCount = failedInsertObservations.size() - failedObservationsBefore;
        final int newObservationsCount = numOfObsTriedToInsert - newFailedObservationsCount;
        LOG.info("New observations in SOS: {}. Failed observations: {}.",
                newObservationsCount,
                newFailedObservationsCount);
        // TODO the failed insert observations should be handled here!
    }

    private void trimValues(String[] values) {
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] != null && !values[i].isEmpty()) {
                    values[i] = values[i].trim();
                }
            }
        }
    }

    private void logLine(String[] values) {
        LOG.debug(String.format("Handling CSV line #%d: %s", lineCounter + 1,
                Arrays.toString(values)));
    }

    private int processSampleStart(final CsvParser cr) throws IOException,
            ParseException {
        int sampleStartLine;
        sampleStartLine = lineCounter;
        sampleLastTimestamp = null;
        getSampleMetaData(cr);
        isInSample = true;
        skipLines(cr, sampleDataOffset - (lineCounter - sampleStartLine));
        return sampleStartLine;
    }

    private void logTiming(long startReadingFile) {
        LOG.debug("Timing:\nStart File: {}\nFinished importing: {}",
                new Date(startReadingFile).toString(),
                new Date(System.currentTimeMillis()).toString());
    }

    private void incrementLineCounter() {
        lineCounter++;
        if (lineCounter % 10000 == 0) {
            LOG.info("Processed line {}.", lineCounter);
        }
    }

    private void logSkippedLine(String[] values) {
        LOG.debug(String.format("\t\tSkip CSV line #%d; %s; Raw data: '%s'",
                lineCounter + 1,
                !skipReason.isEmpty() ? String.format("Reason: %s", skipReason) : "",
                Arrays.toString(values)));
        skipReason = "";
    }

    private boolean isLineIgnorable(final String[] values) {
        if (ignorePatterns != null && ignorePatterns.length > 0) {
            final String line = restoreLine(values);
            for (final Pattern pattern : ignorePatterns) {
                if (pattern.matcher(line).matches()) {
                    skipReason = "Matched ignore pattern.";
                    return true;
                }
            }
        }
        return false;
    }

    private void getSampleMetaData(final CsvParser cr) throws IOException, ParseException {
        LOG.trace("getSampleMetadata(...)");
        LOG.trace("dataOffset: {}; sizeOffset: {}; OffsetDifference: {}",
                sampleDataOffset, sampleSizeOffset, sampleOffsetDifference);
        if (sampleDateOffset < sampleSizeOffset) {
            skipLines(cr, sampleDateOffset);
            sampleDate = parseSampleDate(cr.readNext());
            lineCounter++;
            skipLines(cr, sampleOffsetDifference);
            sampleSize = parseSampleSize(cr.readNext());
            lineCounter++;
        } else {
            skipLines(cr, sampleSizeOffset);
            sampleSize = parseSampleSize(cr.readNext());
            lineCounter++;
            skipLines(cr, sampleOffsetDifference);
            sampleDate = parseSampleDate(cr.readNext());
            lineCounter++;
        }
        LOG.info("Parsed Metadata: Date: '{}'; Size: {}",
                sampleDate, sampleSize);
    }

    private int parseSampleSize(final String[] values) throws ParseException {
        final String lineToParse = restoreLine(values);
        final Matcher matcher = sampleSizePattern.matcher(lineToParse);
        if (matcher.matches() && matcher.groupCount() == 1) {
            final String sampleSizeTmp = matcher.group(1);
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

    private String restoreLine(final String[] values) {
        if (values == null || values.length == 0) {
            return "";
        }
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i]);
            if (i != values.length - 1) {
                sb.append(configuration.getCsvSeparator());
            }
        }
        return sb.toString();
    }

    private Timestamp parseSampleDate(final String[] values) throws ParseException {
        final String dateInfoPattern = configuration.getSampleDatePattern();
        final String regExToExtractDateInfo = configuration.getSampleDateExtractionRegEx();
        final String timestampInformation = restoreLine(values);
        LOG.trace("parseSampleDate: dateInfoPattern: '{}'; extractRegEx: '{}'; line: '{}'",
                dateInfoPattern, regExToExtractDateInfo, timestampInformation);
        return new Timestamp().enrich(timestampInformation, regExToExtractDateInfo, dateInfoPattern);
    }

    /**
     * <p>isSampleEndReached.</p>
     *
     * @param sampleStartLine a int.
     * @return a boolean.
     */
    public boolean isSampleEndReached(final int sampleStartLine) {
        return sampleStartLine + sampleSize + sampleDataOffset == lineCounter;
    }

    private boolean isSampleStart(final String[] values) {
        return sampleIdPattern.matcher(restoreLine(values)).matches();
    }

    private void skipLines(final CsvParser cr, final int skipCount)
            throws IOException {
        // get the number of lines to skip (coming from already read lines)
        String[] values;
        int skipLimit = cr.getSkipLimit();
        int linesToSkip = skipCount;
        while (linesToSkip > skipLimit) {
            values = cr.readNext();
            LOG.trace(String.format("\t\tSkip CSV line #%d: %s", lineCounter + 1, restoreLine(values)));
            linesToSkip--;
            lineCounter++;
        }
    }

    private String[] readHeaderLine(final DataFile dataFile)
            throws UnsupportedEncodingException, FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(
                                dataFile.getCanonicalPath()),
                                dataFile.getEncoding()))) {
            int counter = 1;
            for (String line; (line = br.readLine()) != null;) {
                if (counter++ == dataFile.getHeaderLine()) {
                    return line.split(Character.toString(dataFile.getSeparatorChar()));
                }
            }
        }
        return null;
    }

    private boolean isHeaderLine(final String[] values) {
        boolean isHeaderLine = Arrays.equals(headerLine, values);
        if (!isHeaderLine) {
            skipReason = "Headerline found.";
        }
        return isHeaderLine;
    }

    private boolean isSizeValid(final DataFile dataFile,
            final String[] values) {
        if (values.length != dataFile.getExpectedColumnCount()) {
            if (configuration.isIgnoreColumnMismatch()) {
                return false;
            } else {
                final String errorMsg = String.format(
                        "Number of Expected columns '%s' does not match number of "
                                + "found columns '%s' -> Cancel import! Please update your "
                                + "configuration to match the number of columns.",
                                dataFile.getExpectedColumnCount(),
                                values.length);
                LOG.error(errorMsg);
                throw new InvalidColumnCountException(errorMsg);
            }
        }
        return true;
    }

    private boolean containsData(final String[] values) {
        skipReason = "Line is empty.";
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                final String value = values[i];
                if (!isColumnIgnored(i) && (value == null || value.isEmpty())) {
                    skipReason = String.format("Value of column '%s' is null or empty but shouldn't." , i);
                    return false;
                }
            }
            skipReason = "";
            return true;
        }
        return false;
    }

    private boolean isColumnIgnored(final int i) {
        if (ignoredColumns != null && ignoredColumns.length > 0) {
            for (final int ignoredColumn : ignoredColumns) {
                if (i == ignoredColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    private InsertObservation[] getInsertObservations(final String[] values,
            final int[] mVColumns,
            final DataFile df) {
        if (mVColumns == null || mVColumns.length == 0) {
            LOG.error("Method called with bad arguments: values: {}, mVColumns: {}",
                    Arrays.toString(values),
                    Arrays.toString(mVColumns));
            return null;
        }
        final ArrayList<InsertObservation> result = new ArrayList<>(mVColumns.length);
        for (final int mVColumn : mVColumns) {
            LOG.debug("Parsing measured value column {}", mVColumn);
            try {
                final InsertObservation io = getInsertObservationForColumnIdFromValues(mVColumn, values, df);
                if (io != null) {
                    result.add(io);
                }
            } catch (final ParseException pe) {
                logExceptionThrownDuringParsing(pe);
            } catch (final NumberFormatException nfe) {
                logExceptionThrownDuringParsing(nfe);
            }
        }
        result.trimToSize();
        return result.toArray(new InsertObservation[result.size()]);
    }

    private InsertObservation getInsertObservationForColumnIdFromValues(final int mVColumnId,
            final String[] values,
            final DataFile dataFile) throws ParseException {
        // TIMESTAMP
        final Timestamp timeStamp = dataFile.getTimeStamp(mVColumnId, values);
        if (isSampleBasedDataFile) {
            if (sampleLastTimestamp != null && timeStamp.isBefore(sampleLastTimestamp)) {
                sampleDate.applyDayDelta(1);
            }
            sampleLastTimestamp = new Timestamp().enrich(timeStamp);
            timeStamp.enrich(sampleDate);
        }
        if (isUseLastTimestamp) {
            if (lastUsedTimestamp == null || timeStamp.isAfter(lastUsedTimestamp)) {
                // update newLastUsedTimestamp, if timeStamp is After:
                if (newLastUsedTimestamp == null || timeStamp.isAfter(newLastUsedTimestamp)) {
                    newLastUsedTimestamp = timeStamp;
                }
                // store lastUsedTimestamp in configuration/station?
            } else {
                // abort Insertion
                LOG.debug("skip InsertObservation with timestamp '{}' because not after LastUsedTimestamp '{}'",
                         timeStamp, lastUsedTimestamp);
                return null;
            }
        }
        // TODO implement using different templates in later version depending on the class of value
        LOG.debug("Timestamp: {}", timeStamp);
        // SENSOR
        final Sensor sensor = dataFile.getSensorForColumn(mVColumnId, values);
        LOG.debug("Sensor: {}", sensor);
        // FEATURE OF INTEREST incl. Position
        final FeatureOfInterest foi = dataFile.getFoiForColumn(mVColumnId, values);
        LOG.debug("Feature of Interest: {}", foi);
        // VALUE
        final Object value = dataFile.getValue(mVColumnId, values);
        if (value.equals(Configuration.SOS_OBSERVATION_TYPE_NO_DATA_VALUE)) {
            return null;
        }
        // TODO implement handling for value == null => skip observation and log it, or logging is done in getValue(..)
        LOG.debug("Value: {}", value.toString());
        // UOM CODE
        final UnitOfMeasurement uom = dataFile.getUnitOfMeasurement(mVColumnId, values);
        LOG.debug("UomCode: '{}'", uom);
        // OBSERVED_PROPERTY
        final ObservedProperty observedProperty = dataFile.getObservedProperty(mVColumnId, values);
        LOG.debug("ObservedProperty: {}", observedProperty);
        // OFFERING
        final Offering offer;
        if (offerings.containsKey(sensor.getUri())) {
            String offering = offerings.get(sensor.getUri());
            offer = new Offering(offering, offering);
        } else {
            offer = dataFile.getOffering(sensor);
        }
        LOG.debug("Offering: {}", offer);
        // OM:PARAMETER
        final Optional<List<OmParameter<?>>> omParameter = dataFile.getOmParameters(mVColumnId, values);
        return new InsertObservation(sensor,
                foi,
                value,
                timeStamp,
                uom,
                observedProperty,
                offer,
                omParameter,
                dataFile.getType(mVColumnId));
    }

    private void logExceptionThrownDuringParsing(final Exception exception) {
        LOG.error("Could not retrieve all information required for insert "
                + "observation because of parsing error: {}: {}. "
                + "Skipped this one.",
                exception.getClass().getName(),
                exception.getMessage());
        LOG.debug("Exception stack trace:", exception);
    }

    private void insertTimeSeries(final TimeSeriesRepository timeSeriesRepository)
            throws OXFException, XmlException, IOException {
        LOG.trace("insertTimeSeries()");
        insertObservationForATimeSeries:
        for (final TimeSeries timeSeries : timeSeriesRepository.getTimeSeries()) {
            // check if sensor is registered
            if (!isSensorRegistered(timeSeries.getSensorURI())) {
                final String assignedSensorId = registerSensor(
                        timeSeriesRepository.getRegisterSensor(timeSeries.getSensorURI()));
                if (assignedSensorId == null || assignedSensorId.equalsIgnoreCase("")) {
                    LOG.error(String.format(
                            "Sensor '%s'[%s] could not be registered at SOS '%s'. "
                            + "Skipping insert observation for this timeseries '%s'.",
                            timeSeries.getSensorName(),
                            timeSeries.getSensorURI(),
                            sosUrl.toExternalForm(),
                            timeSeries));
                    failedInsertObservations.addAll(timeSeries.getInsertObservations());
                    continue insertObservationForATimeSeries;
                } else {
                    LOG.debug(String.format(SENSOR_REGISTERED_WITH_ID,
                            sosUrl.toExternalForm(),
                            assignedSensorId));
                    registeredSensors.add(assignedSensorId);
                }
            }
            // insert observation
            String offering = null;
            if (offerings.containsKey(timeSeries.getSensorURI())) {
                offering = offerings.get(timeSeries.getSensorURI());
            }
            final String observationId = insertSweArrayObservation(timeSeries.getSweArrayObservation(sosVersion, offering));
            if (observationId == null || observationId.equalsIgnoreCase("")) {
                LOG.error(String.format(INSERT_OBSERVATION_FAILED,
                        timeSeries.getSensorName(),
                        timeSeries.getSensorURI(),
                        timeSeries));
                failedInsertObservations.addAll(timeSeries.getInsertObservations());
            } else if (observationId.equals(Configuration.SOS_OBSERVATION_ALREADY_CONTAINED)) {
                LOG.debug(String.format("TimeSeries '%s' was already contained in SOS.",
                        timeSeries));
            }
        }
    }

    private void insertObservationsForOneLine(
            final InsertObservation[] ios,
            final String[] values,
            final DataFile dataFile)
            throws OXFException, XmlException, IOException {
        insertObservationForALine:
        for (final InsertObservation io : ios) {
            if (io != null) {
                if (!isSensorRegistered(io.getSensorURI())) {
                    final RegisterSensor rs = new RegisterSensor(io,
                            getObservedProperties(io.getSensorURI(), ios),
                            getMeasuredValueTypes(io.getSensorURI(), ios),
                            getUnitsOfMeasurement(io.getSensorURI(), ios));
                    final String assignedSensorId = registerSensor(rs);
                    if (assignedSensorId == null || assignedSensorId.equalsIgnoreCase("")) {
                        LOG.error(String.format(
                                "Sensor '%s'[%s] could not be registered at SOS '%s'."
                                + "Skipping insert observation for this and store it.",
                                io.getSensorName(),
                                io.getSensorURI(),
                                sosUrl.toExternalForm()));
                        failedInsertObservations.add(io);
                        continue insertObservationForALine;
                    } else {
                        LOG.debug(String.format(SENSOR_REGISTERED_WITH_ID,
                                sosUrl.toExternalForm(),
                                assignedSensorId));
                        registeredSensors.add(assignedSensorId);
                    }
                }
                // sensor is registered -> insert the data
                final String observationId = insertObservation(io);
                if (observationId == null || observationId.equalsIgnoreCase("")) {
                    LOG.error(String.format(INSERT_OBSERVATION_FAILED,
                            io.getSensorName(),
                            io.getSensorURI(),
                            io));
                    failedInsertObservations.add(io);
                } else if (observationId.equals(Configuration.SOS_OBSERVATION_ALREADY_CONTAINED)) {
                    LOG.debug(String.format("Observation was already contained in SOS: %s",
                            io));
                }
            }
        }
    }

    private Map<ObservedProperty, String> getUnitsOfMeasurement(final String sensorURI,
            final InsertObservation[] ios) {
        final Map<ObservedProperty, String> unitsOfMeasurement = new HashMap<>(ios.length);
        for (final InsertObservation insertObservation : ios) {
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

    private Map<ObservedProperty, String> getMeasuredValueTypes(final String sensorURI, final InsertObservation[] ios) {
        final Map<ObservedProperty, String> measuredValueTypes = new HashMap<>(ios.length);
        for (final InsertObservation insertObservation : ios) {
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

    private Collection<ObservedProperty> getObservedProperties(final String sensorURI, final InsertObservation[] ios) {
        final Set<ObservedProperty> observedProperties = new HashSet<>(ios.length);
        for (final InsertObservation insertObservation : ios) {
            if (insertObservation.getSensorURI().equalsIgnoreCase(sensorURI)) {
                observedProperties.add(insertObservation.getObservedProperty());
            }
        }
        LOG.debug(String.format("Found '%d' Observed Properties for Sensor '%s': '%s'",
                observedProperties.size(),
                sensorURI,
                observedProperties));
        return observedProperties;
    }

    private String insertSweArrayObservation(
            final org.n52.oxf.sos.request.InsertObservationParameters sweArrayObservation) {
        try {
            try {
                final int connectionTimeout = sosWrapper.getConnectionTimeout();
                final int readTimeout = sosWrapper.getReadTimeout();
                sosWrapper.setConnectionTimeOut(connectionTimeout + sweArrayObservationTimeOutBuffer);
                sosWrapper.setReadTimeout(readTimeout + sweArrayObservationTimeOutBuffer);
                setMimetype(sweArrayObservation);
                OperationResult opResult = sosWrapper.doInsertObservation(sweArrayObservation);
                sosWrapper.setConnectionTimeOut(connectionTimeout);
                sosWrapper.setReadTimeout(readTimeout);
                if (sosVersion.equals(SOS_VERSION_100)) {
                    try {
                        final InsertObservationResponse response =
                                InsertObservationResponseDocument.Factory.parse(
                                        opResult.getIncomingResultAsAutoCloseStream()).getInsertObservationResponse();
                        LOG.debug(String.format(OBSERVATION_INSERTED_SUCCESSFULLY_WITH_ID,
                                response.getAssignedObservationId()));
                        return response.getAssignedObservationId();
                    } catch (final XmlException | IOException e) {
                        log(e);
                    }
                } else if (sosVersion.equals(SOS_200_VERSION)) {
                    try {
                        net.opengis.sos.x20.InsertObservationResponseDocument.Factory.parse(
                                opResult.getIncomingResultAsAutoCloseStream()).getInsertObservationResponse();
                        LOG.debug(OBSERVATION_INSERTED_SUCCESSFULLY);
                        return SOS_2_0_INSERT_OBSERVATION_RESPONSE;
                    } catch (final XmlException | IOException e) {
                        log(e);
                    }
                }
            } catch (final ExceptionReport e) {
                final Iterator<OWSException> iter = e.getExceptionsIterator();
                StringBuffer buf = new StringBuffer();
                while (iter.hasNext()) {
                    final OWSException owsEx = iter.next();
                    // check for observation already contained exception
                    // TODO update to latest 52nSOS 4.0x message
                    if (isObservationAlreadyContained(owsEx)) {
                        return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
                    }
                    buf = buf.append(String.format(EXCEPTION_CODE_STRING,
                            owsEx.getExceptionCode(),
                            Arrays.toString(owsEx.getExceptionTexts())));
                }
                // TODO improve logging here:
                // add logOwsEceptionReport static util method to OxF or
                // some OER report logger which has unit tests
                LOG.error(String.format(EXCEPTION_THROWN, e.getMessage(), buf.toString()));
                LOG.debug(e.getMessage(), e);
            }

        } catch (final OXFException e) {
            LOG.error(String.format(PROBLEM_WITH_OXF_EXCEPTION_THROWN, e.getMessage()), e);
        }
        return null;
    }

    private void log(final Exception e) {
        LOG.error("Exception thrown: {}", e.getMessage());
        LOG.debug("Stacktrace:", e);
    }

    private boolean isObservationAlreadyContained(final OWSException owsEx) {
        return owsEx.getExceptionCode().equals(Configuration.SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE) &&
                owsEx.getExceptionTexts().length > 0 &&
                (owsEx.getExceptionTexts()[0].contains(Configuration.SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT)
                        ||
                        isExceptionTextContained(owsEx,
                                Configuration.SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED)
                        ||
                        isExceptionTextContained(owsEx,
                                Configuration.SOS_200_DUPLICATE_OBSERVATION_CONSTRAINT)
                        ||
                        isExceptionTextContained(owsEx,
                                Configuration.SOS_UNIQUE_CONSTRAINT_VIOLATION));
    }

    private boolean isExceptionTextContained(final OWSException owsEx, final String exceptionText) {
        return owsEx.getExceptionTexts()[0].contains(exceptionText);
    }

    private String insertObservation(final InsertObservation io) throws IOException {
        try {
            org.n52.oxf.sos.request.InsertObservationParameters parameters = createParameterAssemblyFromIO(io);
            setMimetype(parameters);
            try {
                OperationResult opResult = sosWrapper.doInsertObservation(parameters);
                if (sosVersion.equals(SOS_VERSION_100)) {
                    try {
                        final InsertObservationResponse response =
                                InsertObservationResponseDocument.Factory.parse(
                                        opResult.getIncomingResultAsAutoCloseStream()).getInsertObservationResponse();
                        LOG.debug(String.format(OBSERVATION_INSERTED_SUCCESSFULLY_WITH_ID,
                                response.getAssignedObservationId()));
                        return response.getAssignedObservationId();
                    } catch (final XmlException | IOException e) {
                        log(e);
                    }
                } else if (sosVersion.equals(SOS_200_VERSION)) {
                    try {
                        net.opengis.sos.x20.InsertObservationResponseDocument.Factory.parse(
                                opResult.getIncomingResultAsAutoCloseStream())
                                    .getInsertObservationResponse();
                        LOG.debug(OBSERVATION_INSERTED_SUCCESSFULLY);
                        return SOS_2_0_INSERT_OBSERVATION_RESPONSE;
                    } catch (final XmlException e) {
                        log(e);
                    }
                }
            } catch (final ExceptionReport e) {
                final Iterator<OWSException> iter = e.getExceptionsIterator();
                StringBuffer buf = new StringBuffer();
                while (iter.hasNext()) {
                    final OWSException owsEx = iter.next();
                    // check for observation already contained exception
                    // TODO update to latest 52nSOS 4.0x message
                    if (isObservationAlreadyContained(owsEx)) {
                        return Configuration.SOS_OBSERVATION_ALREADY_CONTAINED;
                    }
                    buf = buf.append(String.format(EXCEPTION_CODE_STRING,
                            owsEx.getExceptionCode(),
                            Arrays.toString(owsEx.getExceptionTexts())));
                }
                // TODO improve logging here:
                // add logOwsEceptionReport static util method to OxF or
                // some OER report logger which has unit tests
                LOG.error(String.format(EXCEPTION_THROWN, e.getMessage(), buf.toString()));
                LOG.debug(e.getMessage(), e);
            }

        } catch (final OXFException e) {
            LOG.error(String.format(PROBLEM_WITH_OXF_EXCEPTION_THROWN, e.getMessage()), e);
        }
        return null;
    }

    private org.n52.oxf.sos.request.InsertObservationParameters createParameterAssemblyFromIO(
            final InsertObservation io) throws OXFException {
        ObservationParameters obsParameter;

        switch (io.getMeasuredValueType()) {
            case Configuration.SOS_OBSERVATION_TYPE_TEXT:
                // set text
                obsParameter = new TextObservationParameters();
                ((TextObservationParameters) obsParameter).addObservationValue(io.getResultValue().toString());
                break;
            case Configuration.SOS_OBSERVATION_TYPE_COUNT:
                // set count
                obsParameter = new CountObservationParameters();
                ((CountObservationParameters) obsParameter).addObservationValue((Integer) io.getResultValue());
                break;
            case Configuration.SOS_OBSERVATION_TYPE_BOOLEAN:
                // set boolean
                obsParameter = new BooleanObservationParameters();
                ((BooleanObservationParameters) obsParameter).addObservationValue((Boolean) io.getResultValue());
                break;
            default:
                // set default value type
                obsParameter = new MeasurementObservationParameters();
                ((MeasurementObservationParameters) obsParameter).addUom(io.getUnitOfMeasurementCode());
                ((MeasurementObservationParameters) obsParameter).addObservationValue(io.getResultValue().toString());
                break;
        }
        obsParameter.addObservedProperty(io.getObservedPropertyURI());
        obsParameter.addNewFoiId(io.getFeatureOfInterestURI());
        obsParameter.addNewFoiName(io.getFeatureOfInterestName());
        obsParameter.addFoiDescription(io.getFeatureOfInterestURI());
        if (io.hasFeatureParentFeature()) {
            obsParameter.addFoiSampleFeature(io.getParentFeatureIdentifier());
        }
        // position
        boolean eastingFirst;
        if (Configuration.getEpsgEastingFirstMap().get(io.getEpsgCode()) == null) {
            eastingFirst = Configuration.getEpsgEastingFirstMap().get("default");
        } else {
            eastingFirst = Configuration.getEpsgEastingFirstMap().get(io.getEpsgCode());
        }
        String pos = eastingFirst ?
                String.format(N_M_FORMAT,
                io.getLongitudeValue(),
                io.getLatitudeValue()) :
                    String.format(N_M_FORMAT,
                            io.getLatitudeValue(),
                            io.getLongitudeValue());
        if (io.isSetAltitudeValue()) {
            pos = String.format(N_M_FORMAT, pos, io.getAltitudeValue());
        }
        obsParameter.addFoiPosition(pos);
        obsParameter.addObservedProperty(io.getObservedPropertyURI());
        obsParameter.addProcedure(io.getSensorURI());

        if (sosVersion.equalsIgnoreCase(SOS_200_VERSION)) {
            obsParameter.addSrsPosition(Configuration.SOS_200_EPSG_CODE_PREFIX + io.getEpsgCode());
            obsParameter.addPhenomenonTime(io.getTimeStamp().toString());
            obsParameter.addResultTime(io.getTimeStamp().toString());
            if (io.isSetOmParameters()) {
                return new org.n52.oxf.sos.request.v200.InsertObservationParameters(
                        obsParameter,
                        Collections.singletonList(io.getOffering().getUri()),
                        io.getOmParameters());
            } else {
                return new org.n52.oxf.sos.request.v200.InsertObservationParameters(
                    obsParameter,
                    Collections.singletonList(io.getOffering().getUri()));
            }
        } else {
            obsParameter.addSrsPosition(Configuration.SOS_100_EPSG_CODE_PREFIX + io.getEpsgCode());
            obsParameter.addSamplingTime(io.getTimeStamp().toString());
            return new org.n52.oxf.sos.request.v100.InsertObservationParameters(obsParameter);
        }
    }

    private String registerSensor(final RegisterSensor rs) throws OXFException, XmlException, IOException {
        try {
            if (sosVersion.equals(SOS_VERSION_100)) {
                final RegisterSensorParameters regSensorParameter = createRegisterSensorParametersFromRS(rs);
                setMimetype(regSensorParameter);
                final OperationResult opResult = sosWrapper.doRegisterSensor(regSensorParameter);
                final RegisterSensorResponseDocument response =
                        RegisterSensorResponseDocument.Factory.parse(opResult.getIncomingResultAsAutoCloseStream());
                LOG.debug("RegisterSensorResponse parsed");
                return response.getRegisterSensorResponse().getAssignedSensorId();
            } else if (sosVersion.equals(SOS_200_VERSION)) {
                final InsertSensorParameters insSensorParams = createInsertSensorParametersFromRS(rs);
                if (sosBinding != null) {
                    insSensorParams.addParameterValue(ISOSRequestBuilder.BINDING, sosBinding.name());
                }
                setMimetype(insSensorParams);
                final OperationResult opResult = sosWrapper.doInsertSensor(insSensorParams);
                final InsertSensorResponseDocument response =
                        InsertSensorResponseDocument.Factory.parse(opResult.getIncomingResultAsAutoCloseStream());
                LOG.debug("InsertSensorResponse parsed");
                offerings.put(
                        response.getInsertSensorResponse().getAssignedProcedure(),
                        response.getInsertSensorResponse().getAssignedOffering());
                return response.getInsertSensorResponse().getAssignedProcedure();
            }
        } catch (final ExceptionReport e) {
            /*
             *  Handle already registered sensor case here (happens when the
             *  sensor is registered but not listed in the capabilities):
             */
            final Iterator<OWSException> iter = e.getExceptionsIterator();
            while (iter.hasNext()) {
                final OWSException owsEx = iter.next();
                if (owsEx.getExceptionCode().equals(OwsExceptionCode.NoApplicableCode.name()) &&
                        owsEx.getExceptionTexts() != null &&
                        owsEx.getExceptionTexts().length > 0) {
                    for (final String string : owsEx.getExceptionTexts()) {
                        if (string.contains(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START) &&
                                string.contains(Configuration.SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END)) {
                            return rs.getSensorURI();
                        }
                    }
                    // handle offering already contained case here
                } else if (owsEx.getExceptionCode().equals(OwsExceptionCode.InvalidParameterValue.name()) &&
                        owsEx.getLocator().equals("offeringIdentifier") &&
                        owsEx.getExceptionTexts() != null &&
                        owsEx.getExceptionTexts().length > 0) {
                    for (final String string : owsEx.getExceptionTexts()) {
                        if (string.contains(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START) &&
                                string.contains(Configuration.SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END)) {
                            offerings.put(rs.getSensorURI(), rs.getOfferingUri());
                            return rs.getSensorURI();
                        }
                    }
                }

            }
            log(e);
        } catch (final OXFException | XmlException | IOException e) {
            log(e);
        }
        return null;
    }

    private void setMimetype(final MimetypeAwareRequestParameters parameters) {
        String mimeType = "text/xml";
        if (sosBinding != null) {
            parameters.addParameterValue(ISOSRequestBuilder.BINDING, sosBinding.name());
            if (sosBinding.equals(Binding.SOAP)) {
                mimeType = "application/soap+xml";
            }
        }

        parameters.setCharset(Charset.forName("UTF-8"));
        parameters.setType(mimeType);
    }

    private InsertSensorParameters createInsertSensorParametersFromRS(final RegisterSensor rs)
            throws XmlException, IOException {
        return new InsertSensorParameters(sensorDescBuilder.createSML(rs),
                SML_101_FORMAT_URI,
                getObservedPropertyURIs(rs.getObservedProperties()),
                Collections.singleton(OM_200_SAMPLING_FEATURE),
                getObservationTypeURIs(rs));
    }

    private Collection<String> getObservationTypeURIs(final RegisterSensor rs) {
        if (rs == null || rs.getObservedProperties() == null || rs.getObservedProperties().size() < 1) {
            return Collections.emptyList();
        }
        final Set<String> tmp = Sets.newHashSetWithExpectedSize(rs.getObservedProperties().size());
        for (final ObservedProperty obsProp : rs.getObservedProperties()) {
            final String measuredValueType = rs.getMeasuredValueType(obsProp);
            if (measuredValueType != null) {
                tmp.add(getURIForObservationType(measuredValueType));
            }
        }
        return tmp;
    }

    private String getURIForObservationType(final String measuredValueType) {
        if (measuredValueType.equals("NUMERIC")) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement";
        }
        if (measuredValueType.equals("COUNT")) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_CountObservation";
        }
        if (measuredValueType.equals("BOOLEAN")) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TruthObservation";
        }
        if (measuredValueType.equals("TEXT")) {
            return "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_TextObservation";
        }
        final String errorMsg = String.format("Observation type '%s' not supported!", measuredValueType);
        LOG.error(errorMsg);
        throw new IllegalArgumentException(errorMsg);
    }

    private Collection<String> getObservedPropertyURIs(final Collection<ObservedProperty> observedProperties) {
        if (observedProperties == null || observedProperties.size() < 1) {
            return Collections.emptyList();
        }
        final Collection<String> result = Lists.newArrayListWithCapacity(observedProperties.size());
        for (final ObservedProperty observedProperty : observedProperties) {
            result.add(observedProperty.getUri());
        }
        return result;
    }

    private RegisterSensorParameters createRegisterSensorParametersFromRS(
            final RegisterSensor registerSensor) throws OXFException, XmlException, IOException {
        /*
         * create SensorML
         *
         * create template --> within the 52N 1.0.0 SOS implementation this
         * template is somehow ignored
         * --> take first observed property to get values for template
         */
        ObservationTemplateBuilder observationTemplate;
        final ObservedProperty firstObservedProperty = registerSensor.getObservedProperties().iterator().next();
        if (isObservationTypeMatching(registerSensor,
                firstObservedProperty,
                org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_TEXT)) {
            observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeText();
        } else if (isObservationTypeMatching(registerSensor,
                firstObservedProperty,
                org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_COUNT)) {
            observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeCount();
        } else if (isObservationTypeMatching(registerSensor,
                firstObservedProperty,
                org.n52.sos.importer.feeder.Configuration.SOS_OBSERVATION_TYPE_BOOLEAN)) {
            observationTemplate = ObservationTemplateBuilder.createObservationTemplateBuilderForTypeTruth();
        } else {
            observationTemplate =
                    ObservationTemplateBuilder.createObservationTemplateBuilderForTypeMeasurement(
                            registerSensor.getUnitOfMeasurementCode(firstObservedProperty));
        }
        observationTemplate.setDefaultValue(registerSensor.getDefaultValue());

        return new RegisterSensorParameters(
                sensorDescBuilder.createSML(registerSensor),
                observationTemplate.generateObservationTemplate());
    }

    private boolean isObservationTypeMatching(final RegisterSensor registerSensor,
            final ObservedProperty firstObservedProperty,
            final String observationType) {
        return registerSensor.getMeasuredValueType(firstObservedProperty).equals(observationType);
    }

    private boolean isSensorRegistered(final String sensorURI) {
        if (!isServiceDescriptorAvailable()) {
            return false;
        }

        // 1 check if offering is available
        final SOSContents sosContent = (SOSContents) serviceDescriptor.getContents();
        final String[] offeringIds = sosContent.getDataIdentificationIDArray();
        if (offeringIds != null) {
            for (final String offeringId : offeringIds) {
                final ObservationOffering offering = sosContent.getDataIdentification(offeringId);
                final String[] sensorIds = offering.getProcedures();
                for (final String sensorId : sensorIds) {
                    if (sensorId.equals(sensorURI)) {
                        offerings.put(sensorId, offering.getIdentifier());
                        return true;
                    }
                }
            }
        }
        // 2 check the list of newly registered sensors because the capabilities update might take to long to wait for
        if (registeredSensors != null && registeredSensors.size() > 0) {
            for (final String sensorId : registeredSensors) {
                if (sensorId.equals(sensorURI)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isServiceDescriptorAvailable() {
        if (serviceDescriptor == null) {
            LOG.error(String.format("Service descriptor not available for SOS '%s'", sosUrl));
            return false;
        }
        return true;
    }

    /**
     * <p>Getter for the field <code>lastLine</code>.</p>
     *
     * @return a int.
     */
    public int getLastLine() {
        return lastLine;
    }

    /**
     * <p>Setter for the field <code>lastLine</code>.</p>
     *
     * @param lastLine a int.
     */
    public void setLastLine(final int lastLine) {
        LOG.debug("Lastline updated: old: {}; new: {}", this.lastLine, lastLine);
        this.lastLine = lastLine;
    }

    public Timestamp getLastUsedTimestamp() {
        return lastUsedTimestamp;
    }

    public void setLastUsedTimeStamp(final Timestamp timeStamp) {
        LOG.debug("LastUsedTimestamp updated: old: {}; new: {}", lastUsedTimestamp, timeStamp);
        lastUsedTimestamp = timeStamp;
    }
}
