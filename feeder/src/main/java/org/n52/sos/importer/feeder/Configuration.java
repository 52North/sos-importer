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

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.n52.janmayen.NcName;
import org.n52.sos.importer.feeder.collector.DefaultCsvCollector;
import org.n52.sos.importer.feeder.collector.SampleBasedObservationCollector;
import org.n52.sos.importer.feeder.collector.csv.WrappedCSVParser;
import org.n52.sos.importer.feeder.importer.SingleObservationImporter;
import org.n52.sos.importer.feeder.model.Coordinate;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Position;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.util.EPSGHelper;
import org.n52.sos.importer.feeder.util.InvalidColumnCountException;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x05.AdditionalMetadataDocument.AdditionalMetadata.FOIPosition;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.FeatureOfInterestType;
import org.x52North.sensorweb.sos.importer.x05.GeneratedSpatialResourceType;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x05.ManualResourceType;
import org.x52North.sensorweb.sos.importer.x05.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x05.ObservedPropertyType;
import org.x52North.sensorweb.sos.importer.x05.RelatedFOIDocument.RelatedFOI;
import org.x52North.sensorweb.sos.importer.x05.RelatedObservedPropertyDocument.RelatedObservedProperty;
import org.x52North.sensorweb.sos.importer.x05.RelatedReferenceValueDocument.RelatedReferenceValue;
import org.x52North.sensorweb.sos.importer.x05.RelatedSensorDocument.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x05.ResourceType;
import org.x52North.sensorweb.sos.importer.x05.SensorType;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x05.SpatialResourceType;
import org.x52North.sensorweb.sos.importer.x05.TypeDocument.Type;
import org.x52North.sensorweb.sos.importer.x05.TypeDocument.Type.Enum;
import org.x52North.sensorweb.sos.importer.x05.UnitOfMeasurementType;

/**
 * This class holds the configuration XML file and provides easy access to all
 * parameters. In addition, it validates the configuration during
 * initialization.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 */
public class Configuration {

    /**
     * Constant
     * <code>SOS_200_EPSG_CODE_PREFIX="http://www.opengis.net/def/crs/EPSG/0/"</code>
     */
    // TODO read from configuration file
    public static final String SOS_200_EPSG_CODE_PREFIX = "http://www.opengis.net/def/crs/EPSG/0/";

    /** Constant <code>SOS_100_EPSG_CODE_PREFIX="urn:ogc:def:crs:EPSG::"</code> */
    public static final String  SOS_100_EPSG_CODE_PREFIX = "urn:ogc:def:crs:EPSG::";

    /**
     * Constant
     * <code>REGISTER_SENSOR_SML_SYSTEM_TEMPLATE="./SML_1.0.1_System_template.xml"</code>
     */
    public static final String REGISTER_SENSOR_SML_SYSTEM_TEMPLATE = "./SML_1.0.1_System_template.xml";

    public static final String NS_SWE_1_0_1 = "http://www.opengis.net/swe/1.0.1";

    public static final String NS_SOS_1_0_0 = "http://www.opengis.net/sos/1.0";

    /** Constant <code>QN_SOS_1_0_OFFERING</code> */
    public static final QName QN_SOS_1_0_OFFERING = new QName(NS_SOS_1_0_0, "offering");

    /** Constant <code>QN_SOS_1_0_ID</code> */
    public static final QName QN_SOS_1_0_ID = new QName(NS_SOS_1_0_0, "id");

    /** Constant <code>QN_SOS_1_0_NAME</code> */
    public static final QName QN_SOS_1_0_NAME = new QName(NS_SOS_1_0_0, "name");

    /** Constant <code>QN_SWE_1_0_1_SIMPLE_DATA_RECORD</code> */
    public static final QName QN_SWE_1_0_1_SIMPLE_DATA_RECORD = new QName(NS_SWE_1_0_1, "SimpleDataRecord");

    /** Constant <code>QN_SWE_1_0_1_DATA_RECORD</code> */
    public static final QName QN_SWE_1_0_1_DATA_RECORD = new QName(NS_SWE_1_0_1, "DataRecord");

    /** Constant <code>QN_SWE_1_0_1_ENVELOPE</code> */
    public static final QName QN_SWE_1_0_1_ENVELOPE = new QName(NS_SWE_1_0_1, "Envelope");

    /** Constant <code>SML_ATTRIBUTE_VERSION="version"</code> */
    public static final String SML_ATTRIBUTE_VERSION = "version";

    /** Constant <code>SML_VERSION="1.0.1"</code> */
    public static final String SML_VERSION = "1.0.1";

    /** Constant <code>UNICODE_REPLACER='_'</code> */
    public static final char UNICODE_REPLACER = '_';

    /** Constant <code>UNICODE_ONLY_REPLACER_LEFT_PATTERN</code> */
    public static final Pattern UNICODE_ONLY_REPLACER_LEFT_PATTERN = Pattern.compile(UNICODE_REPLACER + "+");

    /**
     * Constant
     * <code>SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START="Sensor with ID"</code>
     */
    public static final String SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START = "Sensor with ID";

    /**
     * Constant
     * <code>SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END="is already registered at this SOS"</code>
     */
    public static final String SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END = "is already registered at this SOS";

    /**
     * Constant
     * <code>SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE="NoApplicableCode"</code>
     */
    public static final String SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE = "NoApplicableCode";

    /**
     * Constant
     * <code>SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT="observation_time_stamp_key"</code>
     */
    public static final String SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT = "observation_time_stamp_key";

    /**
     * Constant
     * <code>SOS_OBSERVATION_ALREADY_CONTAINED="observation already contained in sos"</code>
     */
    public static final String SOS_OBSERVATION_ALREADY_CONTAINED = "observation already contained in sos";

    /** Constant <code>SOS_OBSERVATION_TYPE_NO_DATA_VALUE="NO_DATA_VALUE"</code> */
    public static final String SOS_OBSERVATION_TYPE_NO_DATA_VALUE = "NO_DATA_VALUE";

    /** Constant <code>SOS_OBSERVATION_TYPE_TEXT="TEXT"</code> */
    public static final String SOS_OBSERVATION_TYPE_TEXT = "TEXT";

    /** Constant <code>SOS_OBSERVATION_TYPE_COUNT="COUNT"</code> */
    public static final String SOS_OBSERVATION_TYPE_COUNT = "COUNT";

    /** Constant <code>SOS_OBSERVATION_TYPE_BOOLEAN="BOOLEAN"</code> */
    public static final String SOS_OBSERVATION_TYPE_BOOLEAN = "BOOLEAN";

    /** Constant <code>SOS_OBSERVATION_TYPE_NUMERIC="NUMERIC"</code> */
    public static final String SOS_OBSERVATION_TYPE_NUMERIC = "NUMERIC";

    /**
     * Constant
     * <code>OGC_DISCOVERY_ID_TERM_DEFINITION="urn:ogc:def:identifier:OGC:1.0:uniqueID"</code>
     */
    public static final String OGC_DISCOVERY_ID_TERM_DEFINITION = "urn:ogc:def:identifier:OGC:1.0:uniqueID";

    /**
     * Constant
     * <code>OGC_DISCOVERY_LONG_NAME_DEFINITION="urn:ogc:def:identifier:OGC:1.0:longName"</code>
     */
    public static final String OGC_DISCOVERY_LONG_NAME_DEFINITION = "urn:ogc:def:identifier:OGC:1.0:longName";

    /**
     * Constant
     * <code>OGC_DISCOVERY_SHORT_NAME_DEFINITION="urn:ogc:def:identifier:OGC:1.0:shortNam"{trunked}</code>
     */
    public static final String  OGC_DISCOVERY_SHORT_NAME_DEFINITION = "urn:ogc:def:identifier:OGC:1.0:shortName";

    /**
     * Constant <code>OGC_DISCOVERY_INTENDED_APPLICATION_DEFINITION =
     * "urn:ogc:def:classifier:OGC:1.0:application"</code>
     */
    public static final String OGC_DISCOVERY_INTENDED_APPLICATION_DEFINITION =
            "urn:ogc:def:classifier:OGC:1.0:application";

    /**
     * Constant <code>OGC_DISCOVERY_OBSERVED_BBOX_DEFINITION =
     * "urn:ogc:def:property:OGC:1.0:observedBBox"</code>
     */
    public static final String OGC_DISCOVERY_OBSERVED_BBOX_DEFINITION = "urn:ogc:def:property:OGC:1.0:observedBBOX";

    /**
     * Constant <code>SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED =
     * "This observation is already contained in SOS database!"</code>
     */
    public static final String SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED =
            "This observation is already contained in SOS database!";

    /**
     * Constant
     * <code>SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START="The offering with the identifier"</code>
     */
    public static final String SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START = "The offering with the identifier";

    /**
     * Constant <code>SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END =
     * "still exists in this service and it is not allowed to insert more than one procedure to an offering!"</code>
     */
    public static final String SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END =
            "still exists in this service and it is not allowed to insert more than one procedure to an offering!";

    /**
     * Constant <code>SOS_200_DUPLICATE_OBSERVATION_CONSTRAINT =
     * "observation_featureofinterestid_observablepropertyid_proced_key"</code>
     */
    public static final String SOS_200_DUPLICATE_OBSERVATION_CONSTRAINT =
            "observation_featureofinterestid_observablepropertyid_proced_key";

    /**
     * Constant
     * <code>SOS_UNIQUE_CONSTRAINT_VIOLATION="duplicate key value violates unique con"{trunked}</code>
     */
    public static final String SOS_UNIQUE_CONSTRAINT_VIOLATION =
            "duplicate key value violates unique constraint";

    public static final String DEFAULT_CHARSET = "UTF-8";

    public static final String TIMESTAMP_FILE_POSTFIX = ".timestamp";

    public static final String COUNTER_FILE_POSTFIX = ".counter";

    public static final String TIME_TYPE_RESULT = "RESULT";

    public static final String TIME_TYPE_PHENOMENON_INSTANT = "OBSERVATION_INSTANT";

    public static final String TIME_TYPE_PHENOMENON_START = "OBSERVATION_START";

    public static final String TIME_TYPE_PHENOMENON_END = "OBSERVATION_END";

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    private static final String OFFERING_SUFFIX = "-offering";

    private static final String POSITION_PARSEPATTERN_COORD_0 = "COORD_0";

    private static final String POSITION_PARSEPATTERN_COORD_1 = "COORD_1";

    private static final String POSITION_PARSEPATTERN_COORD_2 = "COORD_2";

    private static final String POSITION_PARSEPATTERN_EPSG = "EPSG";

    private static final String COLUMN_SEPARATOR_SPACE = "Space";

    private static final String COLUMN_SEPARATOR_TAB = "Tab";

    private SosImportConfiguration importConf;

    private File configFile;

    private Pattern localeFilePattern;

    private Pattern[] ignorePatterns;

    private boolean oneTimeFeeding;

    public Configuration(String pathToFile) throws XmlException, IOException, IllegalArgumentException {
        LOG.trace("Configuration({})", pathToFile);
        configFile = new File(pathToFile);
        SosImportConfigurationDocument sosImportDoc = SosImportConfigurationDocument.Factory.parse(configFile);
        // Create an XmlOptions instance and set the error listener.
        XmlOptions validateOptions = new XmlOptions();
        ArrayList<XmlError> errorList = new ArrayList<>();
        validateOptions.setErrorListener(errorList);

        // Validate the XML.
        boolean isValid = sosImportDoc.validate(validateOptions);

        // If the XML isn't valid, loop through the listener's contents,
        // printing contained messages.
        if (!isValid) {
            for (int i = 0; i < errorList.size(); i++) {
                XmlError error = errorList.get(i);

                LOG.error("Message: {}; Location: {}",
                        error.getMessage(),
                        error.getCursorLocation().xmlText());
            }
            String msg = "Configuration is not valid and could not be parsed.";
            throw new XmlException(msg, null, errorList);
        } else {
            importConf = sosImportDoc.getSosImportConfiguration();
            setLocaleFilePattern();
        }

        ignorePatterns = getIgnoreLineRegExPatterns();
        validateFeatures();
    }

    private void validateFeatures() {
        if (!importConf.isSetAdditionalMetadata() ||
                importConf.getAdditionalMetadata().sizeOfFeatureOfInterestArray() == 0 &&
                importConf.getAdditionalMetadata().sizeOfFOIPositionArray() == 0) {
            // nothing to check
            return;
        }
        // first: check foi positions
        FOIPosition[] positions = importConf.getAdditionalMetadata().getFOIPositionArray();
        List<org.x52North.sensorweb.sos.importer.x05.PositionDocument.Position> xbPositions = new LinkedList<>();
        for (FOIPosition foiPosition : positions) {
            if (foiPosition.getPosition() != null) {
                xbPositions.add(foiPosition.getPosition());
            }
        }
        // second: check features
        for (FeatureOfInterestType foiType : importConf.getAdditionalMetadata().getFeatureOfInterestArray()) {
            ResourceType res = foiType.getResource();
            if (res instanceof GeneratedSpatialResourceType) {
                if (((GeneratedSpatialResourceType) res).getPosition() != null) {
                    xbPositions.add(((GeneratedSpatialResourceType) res).getPosition());
                }
            } else if (res instanceof SpatialResourceType) {
                if (((SpatialResourceType) res).getPosition() != null) {
                    xbPositions.add(((SpatialResourceType) res).getPosition());
                }
            }
        }
        for (org.x52North.sensorweb.sos.importer.x05.PositionDocument.Position position : xbPositions) {
            int epsgCode = position.getEPSGCode();
            if (!EPSGHelper.isValidEPSGCode(epsgCode)) {
                logInvalidFeaturesFound();
                throw new IllegalArgumentException("Given EPSG code " + epsgCode + " is invalid!");
            }
            if (!EPSGHelper.areValidXBCoordinates(epsgCode, position.getCoordinateArray())) {
                logInvalidFeaturesFound();
                throw new IllegalArgumentException("Given coordinates " +
                        Arrays.toString(position.getCoordinateArray()) +
                        " are invalid for given EPSG code " + epsgCode + "!");
            }
        }
        LOG.info("Feature positions validated");
    }

    private void logInvalidFeaturesFound() {
        LOG.error("Invalid feature positions found in configuration file!");
    }

    private void setLocaleFilePattern() {
        if (isRegularExpressionForLocalFileAvailable()) {
            String pattern = importConf.getDataFile().getLocalFile().getRegularExpressionForAllowedFileNames();
            localeFilePattern = Pattern.compile(pattern);
        }
    }

    private boolean isRegularExpressionForLocalFileAvailable() {
        return importConf.getDataFile().isSetLocalFile() &&
                importConf.getDataFile().getLocalFile().isSetRegularExpressionForAllowedFileNames() &&
                importConf.getDataFile().getLocalFile().getRegularExpressionForAllowedFileNames() != null &&
                !importConf.getDataFile().getLocalFile().getRegularExpressionForAllowedFileNames().isEmpty();
    }

    /**
     * Returns a File instance pointing to the data file defined in XML import
     * configuration.
     *
     * @return a <b><code>new File</code></b> instance pointing to
     *         <code>DataFile.LocalFile.Path</code> or<br>
     *         <b><code>null</code></b>, if element is not defined in config
     */
    public File getDataFile() {
        LOG.trace("getDataFile()");
        if (importConf.getDataFile() != null &&
                importConf.getDataFile().isSetLocalFile() &&
                !importConf.getDataFile().getLocalFile().getPath().equalsIgnoreCase("")) {
            // Path for LocalFile set to something, so return a new File using is
            return new File(importConf.getDataFile().getLocalFile().getPath());
        }
        LOG.error("DataFile.LocalFile.Path not set!");
        return null;
    }

    public File getConfigFile() {
        return configFile;
    }

    public boolean isRemoteFile() {
        return importConf.getDataFile().getRemoteFile() != null;
    }

    public String getRemoteFileURL() {
        return importConf.getDataFile().getRemoteFile().getURL();
    }

    public boolean isRemoteFileURLRegex() {
        LOG.trace("isRemoteFileURLRegex()");
        return importConf.getDataFile().getReferenceIsARegularExpression();
    }

    public URL getSosUrl() throws MalformedURLException {
        LOG.trace("getSosUrl()");
        if (!importConf.getSosMetadata().getURL().equalsIgnoreCase("")) {
            return new URL(importConf.getSosMetadata().getURL());
        }
        LOG.error("SosMetadata.URL not set!");
        return null;
    }

    public String getUser() {
        return importConf.getDataFile().getRemoteFile().getCredentials().getUserName();
    }

    public String getPassword() {
        return importConf.getDataFile().getRemoteFile().getCredentials().getPassword();
    }

    public int getFirstLineWithData() {
        return importConf.getCsvMetadata().getFirstLineWithData();
    }

    public char getCsvSeparator() {
        String sep = importConf.getCsvMetadata().getParameter().getColumnSeparator();
        if (sep.equals(Configuration.COLUMN_SEPARATOR_SPACE)) {
            return ' ';
        } else if (sep.equals(Configuration.COLUMN_SEPARATOR_TAB)) {
            return '\t';
        } else {
            return sep.charAt(0);
        }
    }

    public char getCsvQuoteChar() {
        return importConf.getCsvMetadata().getParameter().getTextIndicator().charAt(0);
    }

    public char getCsvEscape() {
        return importConf.getCsvMetadata().getParameter().getCommentIndicator().charAt(0);
    }

    /**
     * Returns the ids of measured value columns.
     *
     * @return An <code>int[]</code> if any measured value column is found.
     *         <code>null</code> if no column is found.
     */
    public int[] getMeasureValueColumnIds() {
        LOG.trace("getMeasureValueColumnIds()");
        Column[] cols = getColumns();
        LinkedList<Integer> ids = new LinkedList<>();
        for (Column column : cols) {
            if (column.getType().equals(Type.MEASURED_VALUE)) {
                LOG.debug("Found measured value column: {}", column.getNumber());
                ids.add(column.getNumber());
            }
        }
        if (ids.size() > 0) {
            int[] result = new int[ids.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = ids.get(i);
            }
            return result;
        }
        return null;
    }

    public int[] getIgnoredColumnIds() {
        LOG.trace("getIgnoredColumnIds()");
        Column[] cols = getColumns();
        ArrayList<Integer> ids = new ArrayList<>();
        for (Column column : cols) {
            if (column.getType().equals(Type.DO_NOT_EXPORT)) {
                LOG.debug("Found ignored column: {}", column.getNumber());
                ids.add(column.getNumber());
            }
        }
        ids.trimToSize();
        if (ids.size() > 0) {
            int[] result = new int[ids.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = ids.get(i);
            }
            return result;
        }
        return new int[0];
    }

    /**
     * Returns the column id for the given measured value column if available. If
     * not -1.
     *
     * @param mvColumnId
     *            a int.
     * @return The column id of the sensor related to this measure value column or
     *         -1 if no sensor column is available for this column
     */
    public int getColumnIdForSensor(int mvColumnId) {
        LOG.trace(String.format("getColumnIdForSensor(%d)",
                mvColumnId));
        // check for RelatedSensor element and if its a number -> return number
        Column c = getColumnById(mvColumnId);
        if (c.isSetRelatedSensor()) {
            RelatedSensor rS = c.getRelatedSensor();
            if (rS.isSetNumber()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Found RelatedSensor column for measured value column %d: %d",
                            mvColumnId,
                            rS.getNumber()));
                }
                return rS.getNumber();
            } else if (rS.isSetIdRef()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Found RelatedSensor %s is not a column but a Resource.", rS.getIdRef()));
                }
            } else {
                LOG.error(String.format("RelatedSensor element not set properly: %s", rS.xmlText()));
            }
        }
        // if element is not set
        // get column id from ColumnAssignments
        Column[] cols = getColumns();
        for (Column column : cols) {
            if (column.getType().equals(Type.SENSOR)) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Found related sensor column for measured value column %d: %d",
                            mvColumnId,
                            column.getNumber()));
                }
                return column.getNumber();
            }
        }
        return -1;
    }

    public Column getColumnById(int columnId) {
        LOG.trace(String.format("getColumnById(%d)", columnId));
        Column[] cols = getColumns();
        for (Column column : cols) {
            if (column.getNumber() == columnId) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Column found for id %d",
                            columnId));
                }
                return column;
            }
        }
        LOG.error(String.format("CsvMetadat.ColumnAssignments not set properly. Could not find Column for id %d.",
                columnId));
        return null;
    }

    /**
     * Returns the SensorType linked to the column, identified by the given id, by
     * its RelatedSensor.IdRef element. If no sensor could be found
     * <code>null</code> is returned.
     *
     * @param mvColumnId
     *            a int.
     * @return a SensorType object.
     */
    public SensorType getRelatedSensor(int mvColumnId) {
        LOG.trace(String.format("getRelatedSensor(%d)",
                mvColumnId));
        Column c = getColumnById(mvColumnId);
        if (c.isSetRelatedSensor() &&
                c.getRelatedSensor().isSetIdRef()) {
            String sensorXmlId = c.getRelatedSensor().getIdRef();
            if (importConf.getAdditionalMetadata() != null &&
                    importConf.getAdditionalMetadata().getSensorArray() != null &&
                    importConf.getAdditionalMetadata().getSensorArray().length > 0) {
                for (SensorType s : importConf.getAdditionalMetadata().getSensorArray()) {
                    if (isSensorIdMatching(sensorXmlId, s)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug(String.format("Sensor found for id '%s': %s",
                                    sensorXmlId,
                                    s.xmlText()));
                        }
                        return s;
                    }
                }
                LOG.debug(String.format("No Sensor found for column '%s'.",
                        sensorXmlId));
                return null;
            } else {
                LOG.error("Element AdditionalMetadata.Sensor not found.");
            }
        }
        LOG.debug(String.format("RelatedSensor element not found for given measured value column id %s",
                mvColumnId));
        return null;
    }

    private boolean isSensorIdMatching(String sensorXmlId, SensorType s) {
        return s.getResource() != null &&
                s.getResource().getID() != null &&
                s.getResource().getID().equals(sensorXmlId);
    }

    public int getColumnIdForFoi(int mvColumnId) {
        LOG.trace(String.format("getColumnIdForFoi(%d)",
                mvColumnId));
        // check for RelatedFOI element and if its a number -> return number
        Column c = getColumnById(mvColumnId);
        if (c.isSetRelatedFOI()) {
            RelatedFOI rF = c.getRelatedFOI();
            if (rF.isSetNumber()) {
                LOG.debug(String.format("Found RelatedFOI column for measured value column %d: %d",
                        mvColumnId,
                        rF.getNumber()));
                return rF.getNumber();
            } else if (rF.isSetIdRef()) {
                LOG.debug(String.format("Found RelatedFOI %s is not a column but a Resource.", rF.getIdRef()));
            } else {
                LOG.error(String.format("RelatedFOI element not set properly: %s", rF.xmlText()));
            }
        }
        // if element is not set
        // get column id from ColumnAssignments
        Column[] cols = getColumns();
        for (Column column : cols) {
            if (column.getType().equals(Type.FOI)) {
                LOG.debug(String.format("Found related feature of interest column for measured value column %d: %d",
                        mvColumnId,
                        column.getNumber()));
                return column.getNumber();
            }
        }
        return -1;
    }

    public Position getFoiPosition(String foiUri) {
        LOG.trace(String.format("getFoiPosition(%s)",
                foiUri));
        // get all elements from foi positions and compare the uri
        if (importConf.getAdditionalMetadata() != null &&
                importConf.getAdditionalMetadata().getFOIPositionArray() != null &&
                importConf.getAdditionalMetadata().getFOIPositionArray().length > 0) {
            FOIPosition[] foiPos = importConf.getAdditionalMetadata().getFOIPositionArray();
            for (FOIPosition pos : foiPos) {
                if (pos.getURI() != null &&
                        pos.getURI().getStringValue() != null &&
                        pos.getURI().getStringValue().equals(foiUri)) {
                    // if element is found -> fill position
                    org.x52North.sensorweb.sos.importer.x05.PositionDocument.Position p = pos.getPosition();
                    if (p.isSetEPSGCode()) {
                        if  (!EPSGHelper.isValidEPSGCode(p.getEPSGCode())) {
                            throw new IllegalArgumentException("given EPSG code is invalid! EPSG code:" +
                                    p.getEPSGCode());
                        } else if (!EPSGHelper.areValidXBCoordinates(p.getEPSGCode(), p.getCoordinateArray())) {
                            throw new IllegalArgumentException("Specified coordinates are invalid for EPSG code " +
                                    p.getEPSGCode());
                        }
                        return getModelPositionXBPosition(p);
                    }
                }
            }
        }
        return null;
    }

    public Position getModelPositionXBPosition(
            org.x52North.sensorweb.sos.importer.x05.PositionDocument.Position p) {
        LOG.trace("getPosition()");
        if (!EPSGHelper.isValidEPSGCode(p.getEPSGCode())) {
            return null;
        }
        Coordinate[] coordinates = new Coordinate[p.sizeOfCoordinateArray()];
        for (int i = 0; i < p.sizeOfCoordinateArray(); i++) {
            org.x52North.sensorweb.sos.importer.x05.CoordinateDocument.Coordinate xbCoordinate = p
                    .getCoordinateArray(i);
            coordinates[i] = new Coordinate(xbCoordinate.getAxisAbbreviation(), xbCoordinate.getUnit(),
                    xbCoordinate.getDoubleValue());
        }
        if (!EPSGHelper.areValid(p.getEPSGCode(), coordinates)) {
            return null;
        }
        return new Position(p.getEPSGCode(), coordinates);
    }

    public FeatureOfInterestType getRelatedFoi(int mvColumnId) {
        LOG.trace(String.format("getRelatedFoi(%d)",
                mvColumnId));
        Column c = getColumnById(mvColumnId);
        if (c.isSetRelatedFOI() &&
                c.getRelatedFOI().isSetIdRef()) {
            String foiXmlId = c.getRelatedFOI().getIdRef();
            if (importConf.getAdditionalMetadata() != null &&
                    importConf.getAdditionalMetadata().sizeOfFeatureOfInterestArray() > 0) {
                for (FeatureOfInterestType foi : importConf.getAdditionalMetadata().getFeatureOfInterestArray()) {
                    if (isFoiIdMatching(foiXmlId, foi)) {
                        LOG.debug(String.format("Feature of Interest found for id '%s': %s",
                                foiXmlId,
                                foi.xmlText()));
                        return foi;
                    }
                }
                LOG.debug(String.format("No Feature of Interest found for column '%s'.",
                        foiXmlId));
                return null;
            } else {
                LOG.error("Element AdditionalMetadata.FeatureOfInterest not found.");
            }
        }
        LOG.debug(String.format("RelatedFOI element not found for given measured value column id %s",
                mvColumnId));
        return null;
    }

    private boolean isFoiIdMatching(String foiXmlId, FeatureOfInterestType foi) {
        return foi.getResource() != null &&
                foi.getResource().getID() != null &&
                foi.getResource().getID().equals(foiXmlId);
    }

    public Position getPosition(String[] values) throws ParseException {
        String group = "";
        // get first group in Document for position column
        for (Column column : getColumnsByType(Type.POSITION)) {
            Optional<String> groupValue = getMetadataValue(column, Key.GROUP);
            if (groupValue.isPresent() && !groupValue.get().isEmpty()) {
                group = groupValue.get();
                break;
            }
        }
        return getPosition(group, values);
    }

    public Position getPosition(String group, String[] values) throws ParseException {
        LOG.trace(String.format("getPosition(group:%s,..)", group));
        Column[] cols = getAllColumnsForGroup(group, Type.POSITION);
        // combine the values from the different columns
        Coordinate[] coordinates = new Coordinate[3];
        int epsgCode = -1;
        for (Column column : cols) {
            for (Metadata m : column.getMetadataArray()) {
                if (m.getKey().equals(Key.PARSE_PATTERN)) {
                    String pattern = m.getValue();
                    pattern = pattern.replaceAll(Configuration.POSITION_PARSEPATTERN_COORD_0, "{0,number}");
                    pattern = pattern.replaceAll(Configuration.POSITION_PARSEPATTERN_COORD_1, "{1,number}");
                    pattern = pattern.replaceAll(Configuration.POSITION_PARSEPATTERN_COORD_2, "{2,number}");
                    pattern = pattern.replaceAll(Configuration.POSITION_PARSEPATTERN_EPSG, "{3,number}");

                    MessageFormat mf = new MessageFormat(pattern);
                    Object[] tokens = null;
                    try {
                        tokens = mf.parse(values[column.getNumber()]);
                    } catch (ParseException e) {
                        throw new NumberFormatException();
                    }

                    if (tokens == null) {
                        throw new NumberFormatException();
                    }

                    if (tokens.length > 0 && tokens[0] != null) {
                        coordinates[0] = new Coordinate("", "", parseToDouble(tokens[0] + ""));
                    }
                    if (tokens.length > 1 && tokens[1] != null) {
                        coordinates[1] = new Coordinate("", "", parseToDouble(tokens[1] + ""));
                    }
                    if (tokens.length > 2 && tokens[2] != null) {
                        coordinates[2] = new Coordinate("", "", parseToDouble(tokens[2] + ""));
                    }
                    if (tokens.length > 3 && tokens[3] != null) {
                        epsgCode = Integer.parseInt(tokens[3] + "");
                    }
                    // get additional information
                    // e.g. additional coordinate values
                } else if (m.getKey().equals(Key.POSITION_COORD_0)) {
                    coordinates[0] = new Coordinate("", "", parseToDouble(m.getValue()));
                } else if (m.getKey().equals(Key.POSITION_COORD_1)) {
                    coordinates[1] = new Coordinate("", "", parseToDouble(m.getValue()));
                } else if (m.getKey().equals(Key.POSITION_COORD_2)) {
                    coordinates[2] = new Coordinate("", "", parseToDouble(m.getValue()));
                } else if (m.getKey().equals(Key.POSITION_EPSG_CODE)) {
                    // EPSG
                    epsgCode = Integer.parseInt(m.getValue());
                }
            }
        }
        if (!EPSGHelper.isValidEPSGCode(epsgCode)) {
            return null;
        }
        CoordinateSystem cs = EPSGHelper.getCoordinateSystem(epsgCode);
        Coordinate[] finalCoordinates = new Coordinate[cs.getDimension()];
        for (int i = 0; i < cs.getDimension(); i++) {
            if (coordinates[i] == null) {
                continue;
            }
            CoordinateSystemAxis axis = cs.getAxis(i);
            coordinates[i].setAxisAbbreviation(axis.getAbbreviation());
            coordinates[i].setUnit(axis.getUnit().toString());
            finalCoordinates[i] = coordinates[i];
        }
        if (!EPSGHelper.areValid(epsgCode, finalCoordinates)) {
            return null;
        }
        return new Position(epsgCode, finalCoordinates);
    }

    public double parseToDouble(String number) throws ParseException {
        LOG.trace(String.format("parseToDouble(%s)", number));
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        char dSep = getDecimalSeparator();
        symbols.setDecimalSeparator(dSep);
        symbols.setGroupingSeparator(getThousandsSeparator(dSep));

        Number n;
        DecimalFormat formatter = new DecimalFormat();
        formatter.setDecimalFormatSymbols(symbols);
        n = formatter.parse(number);

        return n.doubleValue();
    }

    private char getThousandsSeparator(char dSep) {
        if (dSep == '.') {
            return ',';
        } else if (dSep == ',') {
            return '.';
        } else {
            return 0;
        }
    }

    private char getDecimalSeparator() {
        return importConf.getCsvMetadata().getDecimalSeparator().charAt(0);
    }

    /**
     * Returns all columns of the corresponding <code>group</code>
     *
     * @param group
     *            a <code>{@link java.lang.String String}</code> as group identifier
     * @param type
     *            a TypeDocument.Type.Enum object.
     * @return a <code>Column[]</code> having all the group id <code>group</code>
     *         <b>or</b><br>
     *         an empty <code>Column[]</code>
     */
    public Column[] getAllColumnsForGroup(String group, Enum type) {
        LOG.trace("getAllColumnsForGroup()");
        if (group == null) {
            return null;
        }
        Column[] allCols = getColumns();
        ArrayList<Column> tmpResultSet = new ArrayList<>(allCols.length);
        for (Column col : allCols) {
            if (col.getType() != null &&
                    col.getType().equals(type)) {
                // we have a position or dateTime
                // check the Metadata kvps
                if (col.getMetadataArray() != null && col.getMetadataArray().length > 0) {
                    findGroup: for (Metadata meta : col.getMetadataArray()) {
                        if (meta.getKey().equals(Key.GROUP) &&
                                meta.getValue().equals(group)) {
                            tmpResultSet.add(col);
                            break findGroup;
                        }
                    }
                }
            }
        }
        tmpResultSet.trimToSize();
        Column[] result = new Column[tmpResultSet.size()];
        result = tmpResultSet.toArray(result);
        return result;
    }

    /**
     * Returns the group id of the first date time group found in
     * <code>CsvMetadata.ColumnAssignments.Column[]</code>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFirstDateTimeGroup() {
        LOG.trace("getFirstDateTimeGroup()");
        Column[] cols = getColumns();
        for (Column col : cols) {
            if (col.getType().equals(Type.DATE_TIME)) {
                // it's DATE_TIME -> get group id from metadata[]
                if (col.getMetadataArray() != null && col.getMetadataArray().length > 0) {
                    for (Metadata m : col.getMetadataArray()) {
                        if (m.getKey().equals(Key.GROUP)) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(String.format("First date time group found: %s",
                                        m.getValue()));
                            }
                            return m.getValue();
                        }
                    }
                }
            }
        }
        LOG.error("No date time group found in configuration.");
        return null;
    }

    /**
     * Returns the uom with the given id or <code>null</code>
     *
     * @param idRef
     *            a {@link java.lang.String} object.
     * @return <code>UnitOfMeasurementType</code> instance with
     *         <code>id == idRef</code>,<br>
     *         or <code>null</code>
     */
    public UnitOfMeasurementType getUomById(String idRef) {
        LOG.trace(String.format("getUomById('%s')",
                idRef));
        UnitOfMeasurementType[] uoms = importConf.getAdditionalMetadata().getUnitOfMeasurementArray();
        for (UnitOfMeasurementType uom : uoms) {
            if (uom.getResource().getID().equals(idRef)) {
                return uom;
            }
        }
        return null;
    }

    /**
     * Checks all columns in CsvMetadata.ColumnAssignments.Column[] and returns the
     * id of the first column with Type "UOM"
     *
     * @param mVColumnId
     *            a int.
     * @return the id of the first uom column or -1 if not found
     */
    public int getColumnIdForUom(int mVColumnId) {
        LOG.trace(String.format("getColumnIdForUom(%s)",
                mVColumnId));
        Column[] cols = getColumns();
        for (Column col : cols) {
            if (col.getType().equals(Type.UOM)) {
                return col.getNumber();
            }
        }
        return -1;
    }

    /**
     * Returns the op with the given id or <code>null</code>
     *
     * @param idRef
     *            a {@link java.lang.String} object.
     * @return a ObservedPropertyType object.
     */
    public ObservedPropertyType getObsPropById(String idRef) {
        LOG.trace(String.format("getObsPropById('%s')",
                idRef));
        ObservedPropertyType[] ops = importConf.getAdditionalMetadata().getObservedPropertyArray();
        for (ObservedPropertyType op : ops) {
            if (op.getResource().getID().equals(idRef)) {
                return op;
            }
        }
        return null;
    }

    /**
     * Checks all columns in CsvMetadata.ColumnAssignments.Column[] and returns the
     * id of the first column with Type "OBSERVED_PROPERTY"
     *
     * @param mVColumnId
     *            a int.
     * @return the id of the first op column or -1 if not found
     */
    public int getColumnIdForOpsProp(int mVColumnId) {
        LOG.trace(String.format("getColumnIdForOpsProp(%s)",
                mVColumnId));
        Column[] cols = getColumns();
        for (Column col : cols) {
            if (col.getType().equals(Type.OBSERVED_PROPERTY)) {
                return col.getNumber();
            }
        }
        return -1;
    }

    public Offering getOffering(Sensor s) {
        LOG.trace("getOffering()");
        if (importConf.getSosMetadata().getOffering().isSetGenerate() &&
                importConf.getSosMetadata().getOffering().getGenerate()) {
            return new Offering(s.getName() + OFFERING_SUFFIX, s.getUri() + OFFERING_SUFFIX);
        } else {
            String o = importConf.getSosMetadata().getOffering().getStringValue();
            return new Offering(o, o);
        }
    }

    public String getFileName() {
        return configFile.getName();
    }

    @Override
    public String toString() {
        return String.format("Configuration [file=%s]", configFile);
    }

    public String getType(int mVColumnId) {
        for (Column col : getColumns()) {
            if (col.getNumber() == mVColumnId) {
                for (Metadata m : col.getMetadataArray()) {
                    if (m.getKey().equals(Key.TYPE)) {
                        return m.getValue();
                    }
                }
            }
        }
        return null;
    }

    public SensorType getSensorFromAdditionalMetadata() {
        LOG.trace("getSensorFromAdditionalMetadata()");
        if (importConf.getAdditionalMetadata() != null &&
                importConf.getAdditionalMetadata().getSensorArray() != null &&
                importConf.getAdditionalMetadata().getSensorArray().length == 1) {
            return importConf.getAdditionalMetadata().getSensorArray(0);
        }
        return null;
    }

    public boolean isOneMvColumn() {
        return getMeasureValueColumnIds().length == 1;
    }

    public String getSosVersion() {
        LOG.trace("getSosVersion()");
        return importConf.getSosMetadata().getVersion();
    }

    public String getSosBinding() {
        LOG.trace("getSosBinding()");
        if (importConf.getSosMetadata().isSetBinding()) {
            return importConf.getSosMetadata().getBinding();
        }
        LOG.info("Optional SosMetadata.Binding not set!");
        return null;
    }

    public int getExpectedColumnCount() {
        return importConf.getCsvMetadata().getColumnAssignments().sizeOfColumnArray();
    }

    public Pattern getLocaleFilePattern() {
        return localeFilePattern;
    }

    public String getRegExDateInfoInFileName() {
        return importConf.getDataFile().getRegExDateInfoInFileName();
    }

    public String getDateInfoPattern() {
        return importConf.getDataFile().getDateInfoPattern();
    }

    public boolean isDateInfoExtractionFromFileNameSetupValid() {
        return importConf.getDataFile().isSetRegExDateInfoInFileName() &&
                importConf.getDataFile().isSetRegExDateInfoInFileName() &&
                !getRegExDateInfoInFileName().isEmpty() &&
                importConf.getDataFile().isSetDateInfoPattern() &&
                getRegExDateInfoInFileName().indexOf("(") >= 0 &&
                getRegExDateInfoInFileName().indexOf(")") > 1 &&
                !getDateInfoPattern().isEmpty();
    }

    public boolean isUseDateInfoFromFileModificationSet() {
        return importConf.getDataFile().isSetUseDateFromLastModifiedDate() &&
                importConf.getDataFile().getUseDateFromLastModifiedDate();
    }

    public boolean isUseLastTimestamp() {
        return importConf.getCsvMetadata().isSetUseLastTimestamp();
    }

    /**
     * <p>
     * isSamplingFile.
     * </p>
     *
     * @return <code>true</code>, if all required attributes are available for
     *         importing sample based files,<br>
     *         else <code>false</code>.
     */
    public boolean isSamplingFile() {
        return importConf.getDataFile().isSetSampleStartRegEx() &&
                !importConf.getDataFile().getSampleStartRegEx().isEmpty() &&
                importConf.getDataFile().isSetSampleDateOffset() &&
                importConf.getDataFile().isSetSampleDateExtractionRegEx() &&
                !importConf.getDataFile().getSampleDateExtractionRegEx().isEmpty() &&
                importConf.getDataFile().getSampleDateExtractionRegEx().indexOf("(") >= 0 &&
                importConf.getDataFile().getSampleDateExtractionRegEx().indexOf(")") > 1 &&
                importConf.getDataFile().isSetSampleDatePattern() &&
                !importConf.getDataFile().getSampleDatePattern().isEmpty() &&
                importConf.getDataFile().isSetSampleDataOffset() &&
                importConf.getDataFile().isSetSampleSizeOffset() &&
                importConf.getDataFile().isSetSampleSizeRegEx() &&
                !importConf.getDataFile().getSampleSizeRegEx().isEmpty() &&
                importConf.getDataFile().getSampleSizeRegEx().indexOf("(") >= 0 &&
                importConf.getDataFile().getSampleSizeRegEx().indexOf(")") > 1;
    }

    public String getSampleStartRegEx() {
        if (importConf.getDataFile().isSetSampleStartRegEx() &&
                !importConf.getDataFile().getSampleStartRegEx().isEmpty()) {
            return importConf.getDataFile().getSampleStartRegEx();
        }
        throw new IllegalArgumentException("Attribute 'sampleIdRegEx' of <DataFile> not set.");
    }

    public String getSampleSizeRegEx() {
        if (importConf.getDataFile().isSetSampleSizeRegEx() &&
                !importConf.getDataFile().getSampleSizeRegEx().isEmpty() &&
                importConf.getDataFile().getSampleSizeRegEx().indexOf("(") >= 0 &&
                importConf.getDataFile().getSampleSizeRegEx().indexOf(")") > 1) {
            return importConf.getDataFile().getSampleSizeRegEx();
        }
        throw new IllegalArgumentException("Attribute 'sampleSizeRegEx' of <DataFile> not set.");
    }

    public int getSampleSizeOffset() {
        if (importConf.getDataFile().isSetSampleSizeOffset()) {
            return importConf.getDataFile().getSampleSizeOffset();
        }
        throw new IllegalArgumentException("Attribute 'sampleSizeOffset' of <DataFile> not set.");
    }

    public int getSampleDateOffset() {
        if (importConf.getDataFile().isSetSampleDateOffset()) {
            return importConf.getDataFile().getSampleDateOffset();
        }
        throw new IllegalArgumentException("Attribute 'sampleDateOffset' of <DataFile> not set.");
    }

    public int getSampleDataOffset() {
        if (importConf.getDataFile().isSetSampleDataOffset()) {
            return importConf.getDataFile().getSampleDataOffset();
        }
        throw new IllegalArgumentException("Attribute 'sampleDataOffset' of <DataFile> not set.");
    }

    public String getSampleDatePattern() {
        if (importConf.getDataFile().isSetSampleDatePattern() &&
                !importConf.getDataFile().getSampleDatePattern().isEmpty()) {
            return importConf.getDataFile().getSampleDatePattern();
        }
        throw new IllegalArgumentException("Attribute 'sampleDateInfoPattern' of <DataFile> not set.");
    }

    public String getSampleDateExtractionRegEx() {
        if (importConf.getDataFile().isSetSampleDateExtractionRegEx() &&
                !importConf.getDataFile().getSampleDateExtractionRegEx().isEmpty() &&
                importConf.getDataFile().getSampleDateExtractionRegEx().indexOf("(") >= 0 &&
                importConf.getDataFile().getSampleDateExtractionRegEx().indexOf(")") > 1) {
            return importConf.getDataFile().getSampleDateExtractionRegEx();
        }
        throw new IllegalArgumentException("Attribute 'sampleDateExtractionRegEx' of <DataFile> not set.");
    }

    public int getLastModifiedDelta() {
        if (importConf.getDataFile().isSetLastModifiedDelta()) {
            return importConf.getDataFile().getLastModifiedDelta();
        }
        return -1;
    }

    /**
     * <p>
     * getHeaderLine.
     * </p>
     *
     * @return number of the line that contains the header information for the first
     *         time, or<br>
     *         <b>-1</b> if the optional attribute "headerLine" is not set in
     *         DataFile element
     */
    public int getHeaderLine() {
        if (importConf.getDataFile().isSetHeaderLine()) {
            return importConf.getDataFile().getHeaderLine().intValue();
        }
        return -1;
    }

    /**
     * <p>
     * getDataFileEncoding.
     * </p>
     *
     * @return Name of the data file encoding, or <br>
     *         if not set, "UTF-8"
     */
    public String getDataFileEncoding() {
        if (importConf.getDataFile().isSetLocalFile() &&
                importConf.getDataFile().getLocalFile().isSetEncoding() &&
                !importConf.getDataFile().getLocalFile().getEncoding().isEmpty()) {
            String encoding = importConf.getDataFile().getLocalFile().getEncoding();
            try {
                if (Charset.isSupported(encoding)) {
                    return encoding;
                }
            } catch (IllegalCharsetNameException e) {
                String msg = String.format("The specified data file encoding name '%s' is invalid!", encoding);
                LOG.error(msg);
                throw new IllegalArgumentException(e);
            }
        }
        LOG.info("Data file encoding not set, hence using default encoding '{}'.", DEFAULT_CHARSET);
        return DEFAULT_CHARSET;
    }

    public int getHunkSize() {
        if (importConf.isSetAdditionalMetadata() && importConf.getAdditionalMetadata().getMetadataArray().length > 0) {
            for (int i = 0; i < importConf.getAdditionalMetadata().getMetadataArray().length; i++) {
                Metadata metadata = importConf.getAdditionalMetadata().getMetadataArray(i);
                if (metadata.getKey().equals(Key.HUNK_SIZE)) {
                    try {
                        return Integer.parseInt(metadata.getValue());
                    } catch (NumberFormatException nfe) {
                        LOG.error(
                                String.format(
                                        "Value of metadata element with key "
                                                + "'%s' could not be parsed to int: "
                                                + "'%s'. Ignoring it.",
                                        Key.HUNK_SIZE.toString(),
                                        metadata.getValue()),
                                nfe);
                    }
                }
            }
        }
        return -1;
    }

    public boolean isParsedColumnCountCorrect(int count) {
        if (count != getExpectedColumnCount()) {
            if (isIgnoreColumnMismatch()) {
                return false;
            } else {
                String errorMsg = String.format(
                        "Number of Expected columns '%s' does not match number of "
                                + "found columns '%s' -> Cancel import! Please update your "
                                + "configuration to match the number of columns.",
                        getExpectedColumnCount(),
                        count);
                LOG.error(errorMsg);
                throw new InvalidColumnCountException(errorMsg);
            }
        }
        return true;
    }

    public boolean containsData(String[] values) {
        if (values != null && values.length > 0) {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                if (!isColumnIgnored(i) && (value == null || value.isEmpty())) {
                    LOG.debug("Value of column '{}' is null or empty but shouldn't.", i);
                    return false;
                }
            }
            return true;
        }
        LOG.debug("Line is empty");
        return false;
    }

    private boolean isColumnIgnored(int i) {
        int[] ignoredColumns = getIgnoredColumnIds();
        if (ignoredColumns != null && ignoredColumns.length > 0) {
            for (int ignoredColumn : ignoredColumns) {
                if (i == ignoredColumn) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isIgnoreLineRegExSet() {
        return importConf.getDataFile().getIgnoreLineRegExArray() != null &&
                importConf.getDataFile().getIgnoreLineRegExArray().length > 0;
    }

    public boolean isLineIgnorable(String[] values) {
        if (ignorePatterns != null && ignorePatterns.length > 0) {
            String line = restoreLine(values);
            for (Pattern pattern : ignorePatterns) {
                if (pattern.matcher(line).matches()) {
                    LOG.info("Line '{}' matches ingore patter '{}'", line, pattern.toString());
                    return true;
                }
            }
        }
        return false;
    }

    private Pattern[] getIgnoreLineRegExPatterns() {
        if (!isIgnoreLineRegExSet()) {
            return new Pattern[0];
        }
        String[] ignoreLineRegExArray = importConf.getDataFile().getIgnoreLineRegExArray();
        LinkedList<Pattern> patterns = new LinkedList<>();
        for (String regEx : ignoreLineRegExArray) {
            if (regEx != null && !regEx.isEmpty()) {
                patterns.add(Pattern.compile(regEx));
            }
        }
        return patterns.toArray(new Pattern[patterns.size()]);
    }

    public boolean isTimeoutBufferSet() {
        return getAdditionalMetadata(Key.TIMEOUT_BUFFER) != null;
    }

    private Metadata getAdditionalMetadata(Key.Enum key) {
        if (!importConf.isSetAdditionalMetadata()) {
            return null;
        }
        for (Metadata metadata : importConf.getAdditionalMetadata().getMetadataArray()) {
            if (metadata.getKey().equals(key) && !metadata.getValue().isEmpty()) {
                return metadata;
            }
        }
        return null;
    }

    public int getTimeoutBuffer() {
        if (isTimeoutBufferSet()) {
            return Integer.parseInt(getAdditionalMetadata(Key.TIMEOUT_BUFFER).getValue());
        }
        throw new IllegalArgumentException(
                "Attribute 'insertSweArrayObservationTimeoutBuffer' of <SosMetadata> not set.");
    }

    public int getSampleSizeDivisor() {
        if (isSamplingFile() && importConf.getDataFile().isSetSampleSizeDivisor()) {
            return importConf.getDataFile().getSampleSizeDivisor();
        }
        return 1;
    }

    public boolean isIgnoreColumnMismatch() {
        return importConf.getCsvMetadata().getObservationCollector() != null &&
                importConf.getCsvMetadata().getObservationCollector().isSetIgnoreColumnCountMismatch() &&
                importConf.getCsvMetadata().getObservationCollector().getIgnoreColumnCountMismatch();
    }

    public boolean isOmParameterAvailableFor(int mVColumnId) {
        // Case A: relatedOmParameter set
        if (importConf.getCsvMetadata().getColumnAssignments().sizeOfColumnArray() > 0 &&
                importConf.getCsvMetadata().getColumnAssignments().getColumnArray(mVColumnId)
                        .getRelatedOmParameterArray().length > 0) {
            return true;
        } else {
            // Case B: column with type omParameter
            for (Column column : getColumns()) {
                if (column.getType().equals(Type.OM_PARAMETER)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Column> getColumnsForOmParameter(int mVColumnId) {
        if (isOmParameterAvailableFor(mVColumnId)) {
            List<Column> cols = new LinkedList<>();
            // get by id
            if (importConf.getCsvMetadata().getColumnAssignments().getColumnArray(mVColumnId)
                    .getRelatedOmParameterArray().length > 0) {
                for (BigInteger relatedOmParameter : importConf.getCsvMetadata().getColumnAssignments()
                        .getColumnArray(mVColumnId).getRelatedOmParameterArray()) {
                    cols.add(getColumnById(relatedOmParameter.intValue()));
                }
            } else {
                // collect all
                for (Column col : getColumns()) {
                    if (col.getType().equals(Type.OM_PARAMETER)) {
                        cols.add(col);
                    }
                }
            }
            return cols;
        }
        return Collections.emptyList();
    }

    public boolean isNoDataValueDefinedAndMatching(Column column, String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        if (column == null || column.getMetadataArray() == null || column.sizeOfMetadataArray() == 0) {
            return false;
        }
        for (Metadata md : column.getMetadataArray()) {
            if (md.getKey().equals(Key.NO_DATA_VALUE)) {
                if (value.equals(md.getValue())) {
                    LOG.trace("value '{}' is matching NO_DATA_VALUE '{}'.", value, md.getValue());
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean areRemoteFileCredentialsSet() {
        return isRemoteFile() && importConf.getDataFile().getRemoteFile().isSetCredentials();
    }

    public boolean isParentFeatureSetForFeature(int featureColumnIndex) {
        Column column = getColumnById(featureColumnIndex);
        if (column == null) {
            return false;
        }
        if (column.getType().equals(Type.FOI) &&
                isColumnMetadataSet(column, Key.PARENT_FEATURE_IDENTIFIER)) {
            return true;
        }
        return false;
    }

    private boolean isColumnMetadataSet(Column column, Key.Enum key) {
        return getMetadataValue(column, key) != null &&
                getMetadataValue(column, key).isPresent() &&
                !getMetadataValue(column, key).get().isEmpty();
    }

    public String getParentFeature(int featureColumnIndex) {
        Column column = getColumnById(featureColumnIndex);
        if (column == null) {
            return "";
        }
        Optional<String> value = getMetadataValue(column, Key.PARENT_FEATURE_IDENTIFIER);
        return value.isPresent() ? value.get() : "";
    }

    public Optional<String> getMetadataValue(Column column, Key.Enum key) {
        for (Metadata metadata : column.getMetadataArray()) {
            if (metadata.getKey().equals(key)) {
                return Optional.ofNullable(metadata.getValue());
            }
        }
        return Optional.empty();
    }

    public Optional<String> getMetadataValue(int columnIndex, Key.Enum key) {
        return getMetadataValue(getColumnById(columnIndex), key);
    }

    public String restoreLine(String[] values) {
        if (values == null || values.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            sb.append(values[i]);
            if (i != values.length - 1) {
                sb.append(getCsvSeparator());
            }
        }
        return sb.toString();
    }

    public String getImporterClassName() {
        if (!importConf.getSosMetadata().getImporter().isEmpty()) {
            return importConf.getSosMetadata().getImporter();
        } else {
            return SingleObservationImporter.class.getName();
        }
    }

    public String getCollectorClassName() {
        if (!importConf.getCsvMetadata().getObservationCollector().getStringValue().isEmpty()) {
            return importConf.getCsvMetadata().getObservationCollector().getStringValue();
        } else if (isSamplingFile()) {
            return SampleBasedObservationCollector.class.getName();
        } else {
            LOG.error("Collector implementation not defined! Using default: {}", DefaultCsvCollector.class.getName());
            return DefaultCsvCollector.class.getName();
        }
    }

    public String getParentFeatureFromAdditionalMetadata() {
        if (importConf.isSetAdditionalMetadata() && importConf.getAdditionalMetadata().sizeOfMetadataArray() > 0) {
            for (Metadata metadata : importConf.getAdditionalMetadata().getMetadataArray()) {
                if (metadata.getKey().equals(Key.PARENT_FEATURE_IDENTIFIER)) {
                    return metadata.getValue();
                }
            }
        }
        return null;
    }

    public String getAbsolutePath() {
        return configFile.getAbsolutePath();
    }

    public Map<ObservedProperty, List<SimpleEntry<String, String>>> getReferenceValues(URI sensorURI) {
        if (!hasReferenceValues()) {
            return Collections.emptyMap();
        }
        Map<ObservedProperty, List<SimpleEntry<String, String>>> result = new HashMap<>();
        for (Column column : getColumns()) {
            // if sensor matches and reference value is available
            if (!column.isSetRelatedSensor() ||
                    !column.getRelatedSensor().isSetIdRef() ||
                    !column.isSetRelatedObservedProperty() ||
                    !column.getRelatedObservedProperty().isSetIdRef()) {
                continue;
            }
            ResourceType resource = getRelatedSensor(column.getNumber()).getResource();
            if (resource == null ||
                    !(resource instanceof ManualResourceType) ||
                    !NcName.makeValid(((ManualResourceType) resource).getURI().getStringValue())
                            .equals(NcName.makeValid(sensorURI.toString()))) {
                continue;
            }
            // This column contains the correct sensor
            ObservedProperty observedProperty = getObservedProperty(column.getRelatedObservedProperty());
            if (observedProperty == null) {
                continue;
            }
            if (column.sizeOfRelatedReferenceValueArray() < 1) {
                continue;
            }
            // Next: check for reference values
            for (RelatedReferenceValue refValue : column.getRelatedReferenceValueArray()) {
                List<SimpleEntry<String, String>> list;
                if (result.containsKey(observedProperty)) {
                    list = result.get(observedProperty);
                } else {
                    list = Collections.synchronizedList(new LinkedList<>());
                    result.put(observedProperty, list);
                }
                list.add(new SimpleEntry<>(refValue.getLabel(), refValue.getValue()));
            }
        }
        return result;
    }

    private ObservedProperty getObservedProperty(RelatedObservedProperty relatedObservedProperty) {
        ManualResourceType observedPropertyResource = getRelatedResourceById(relatedObservedProperty.getIdRef());
        if (observedPropertyResource == null) {
            return null;
        }
        return new ObservedProperty(observedPropertyResource.getName(),
                observedPropertyResource.getURI().getStringValue());
    }

    private ManualResourceType getRelatedResourceById(String idRef) {
        if (importConf.isSetAdditionalMetadata()) {
            AdditionalMetadata additionalMetadata = importConf.getAdditionalMetadata();
            // check features
            for (FeatureOfInterestType feature : additionalMetadata.getFeatureOfInterestArray()) {
                if (feature.getResource().getID().equalsIgnoreCase(idRef)) {
                    return feature.getResource() instanceof ManualResourceType
                            ? (ManualResourceType) feature.getResource()
                            : null;
                }
            }
            // check sensors
            for (SensorType sensor : additionalMetadata.getSensorArray()) {
                if (sensor.getResource().getID().equalsIgnoreCase(idRef)) {
                    return sensor.getResource() instanceof ManualResourceType
                            ? (ManualResourceType) sensor.getResource()
                            : null;
                }
            }
            // observed property
            for (ObservedPropertyType property : additionalMetadata.getObservedPropertyArray()) {
                if (property.getResource().getID().equalsIgnoreCase(idRef)) {
                    return property.getResource() instanceof ManualResourceType
                            ? (ManualResourceType) property.getResource()
                            : null;
                }
            }
            // unitofmeasurement
            for (UnitOfMeasurementType uom : additionalMetadata.getUnitOfMeasurementArray()) {
                if (uom.getResource().getID().equalsIgnoreCase(idRef)) {
                    return uom.getResource() instanceof ManualResourceType ? (ManualResourceType) uom.getResource()
                            : null;
                }
            }
        }
        return null;
    }

    public boolean hasReferenceValues() {
        Column[] cols = getColumns();
        if (cols.length > 0) {
            for (Column column : cols) {
                if (column.sizeOfRelatedReferenceValueArray() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private Column[] getColumns() {
        if (importConf.getCsvMetadata().getColumnAssignments().sizeOfColumnArray() > 0) {
            return importConf.getCsvMetadata().getColumnAssignments().getColumnArray();
        } else {
            return new Column[0];
        }
    }

    public boolean isCsvParserDefined() {
        return importConf.getCsvMetadata().isSetCsvParser();
    }

    public String getCsvParser() {
        if (isCsvParserDefined()) {
            return importConf.getCsvMetadata().getCsvParser();
        } else {
            String parser = WrappedCSVParser.class.getName();
            LOG.info("No <CsvMetadata><CsvParser> found. Using default '{}'.", parser);
            return parser;
        }
    }

    public boolean arePhenomenonTimesAvailable(int measureValueColumnIndex) {
        List<Column> dateTimeColumns;
        if (getMeasureValueColumnIds().length == 1) {
            dateTimeColumns = getColumnsByType(Type.DATE_TIME);
        } else {
            dateTimeColumns = getDateTimeColumnsForMeasureValue(measureValueColumnIndex);
        }
        boolean phenomenonTimeInstantFound = false;
        boolean phenomenonTimeStartFound = false;
        boolean phenomenonTimeEndFound = false;
        for (Column column : dateTimeColumns) {
            for (Metadata metadata : column.getMetadataArray()) {
                if (metadata.getKey().equals(Key.TIME_TYPE)) {
                    switch (metadata.getValue()) {
                        case TIME_TYPE_PHENOMENON_INSTANT:
                            phenomenonTimeInstantFound = true;
                            break;
                        case TIME_TYPE_PHENOMENON_START:
                            phenomenonTimeStartFound = true;
                            break;
                        case TIME_TYPE_PHENOMENON_END:
                            phenomenonTimeEndFound = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        if (phenomenonTimeInstantFound && !phenomenonTimeStartFound && !phenomenonTimeEndFound) {
            return true;
        }
        if (!phenomenonTimeInstantFound && phenomenonTimeStartFound && phenomenonTimeEndFound) {
            return true;
        }
        return false;
    }

    public List<Column> getDateTimeColumnsForMeasureValue(int measureValueColumn) {
        Column mvColumn = getColumnById(measureValueColumn);
        List<Column> dateTimeColumns = new LinkedList<>();
        if (mvColumn.sizeOfRelatedDateTimeGroupArray() == 0) {
            if (getMeasureValueColumnIds().length > 1) {
                return Collections.emptyList();
            }
            return getColumnsByType(Type.DATE_TIME);
        }
        for (String dateTimeGroupIdentifier : mvColumn.getRelatedDateTimeGroupArray()) {
            Column[] allColumnsForGroup = getAllColumnsForGroup(dateTimeGroupIdentifier, Type.DATE_TIME);
            if (allColumnsForGroup.length == 0) {
                return Collections.emptyList();
            } else {
                dateTimeColumns.add(allColumnsForGroup[0]);
            }
        }
        return dateTimeColumns;
    }

    public List<Column> getColumnsByType(Enum columnType) {
        List<Column> result = new LinkedList<>();
        for (Column column : importConf.getCsvMetadata().getColumnAssignments().getColumnArray()) {
            if (column.getType().toString().equalsIgnoreCase(columnType.toString())) {
                result.add(column);
            }
        }
        return result;
    }

    public boolean isOneTimeFeeding() {
        return oneTimeFeeding;
    }

    public Configuration enableOneTimeFeeding() {
        oneTimeFeeding = true;
        return this;
    }

}
