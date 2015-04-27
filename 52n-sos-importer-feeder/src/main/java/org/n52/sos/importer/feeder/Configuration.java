/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
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
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.n52.sos.importer.feeder.csv.WrappedCSVReader;
import org.n52.sos.importer.feeder.model.Offering;
import org.n52.sos.importer.feeder.model.Position;
import org.n52.sos.importer.feeder.model.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x04.AdditionalMetadataDocument.AdditionalMetadata.FOIPosition;
import org.x52North.sensorweb.sos.importer.x04.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x04.FeatureOfInterestType;
import org.x52North.sensorweb.sos.importer.x04.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x04.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x04.ObservedPropertyType;
import org.x52North.sensorweb.sos.importer.x04.RelatedFOIDocument.RelatedFOI;
import org.x52North.sensorweb.sos.importer.x04.RelatedSensorDocument.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x04.SensorType;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x04.TypeDocument.Type;
import org.x52North.sensorweb.sos.importer.x04.TypeDocument.Type.Enum;
import org.x52North.sensorweb.sos.importer.x04.UnitOfMeasurementType;

/**
 * This class holds the configuration XML file and provides easy access to all
 * parameters. In addition, it validates the configuration during
 * initialisation.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public final class Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    private static final String POSITION_PARSEPATTERN_LATITUDE = "LAT";
    private static final String POSITION_PARSEPATTERN_LONGITUDE = "LON";
    private static final String POSITION_PARSEPATTERN_ALTITUDE = "ALT";
    private static final String POSITION_PARSEPATTERN_EPSG = "EPSG";
    // TODO read from configuration file
    public static final String SOS_200_EPSG_CODE_PREFIX = "http://www.opengis.net/def/crs/EPSG/0/";
    public static final String SOS_100_EPSG_CODE_PREFIX = "urn:ogc:def:crs:EPSG::";
    public static final String REGISTER_SENSOR_SML_SYSTEM_TEMPLATE = "./SML_1.0.1_System_template.xml";
    private static final String NS_SWE_1_0_1 = "http://www.opengis.net/swe/1.0.1";
    private static final String NS_SOS_1_0_0 = "http://www.opengis.net/sos/1.0";
    public static final QName QN_SOS_1_0_OFFERING = new QName(NS_SOS_1_0_0, "offering");
    public static final QName QN_SOS_1_0_ID = new QName(NS_SOS_1_0_0, "id");
    public static final QName QN_SOS_1_0_NAME = new QName(NS_SOS_1_0_0, "name");
    public static final QName QN_SWE_1_0_1_SIMPLE_DATA_RECORD = new QName(NS_SWE_1_0_1,"SimpleDataRecord");
    public static final QName QN_SWE_1_0_1_DATA_RECORD = new QName (NS_SWE_1_0_1,"DataRecord");
    public static final QName QN_SWE_1_0_1_ENVELOPE = new QName (NS_SWE_1_0_1,"Envelope");
    public static final String SML_ATTRIBUTE_VERSION = "version";
    public static final String SML_VERSION = "1.0.1";
    public static final char UNICODE_REPLACER = '_';
    public static final Pattern UNICODE_ONLY_REPLACER_LEFT_PATTERN = Pattern.compile(UNICODE_REPLACER + "+");
    private static final String COLUMN_SEPARATOR_SPACE = "Space";
    private static final String COLUMN_SEPARATOR_TAB = "Tab";
    public static final String SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_START = "Sensor with ID";
    public static final String SOS_SENSOR_ALREADY_REGISTERED_MESSAGE_END = "is already registered at this SOS";
    public static final String SOS_EXCEPTION_CODE_NO_APPLICABLE_CODE = "NoApplicableCode";
    public static final String SOS_EXCEPTION_OBSERVATION_DUPLICATE_CONSTRAINT = "observation_time_stamp_key";
    public static final String SOS_OBSERVATION_ALREADY_CONTAINED = "observation already contained in sos";
    public static final String SOS_OBSERVATION_TYPE_TEXT = "TEXT";
    public static final String SOS_OBSERVATION_TYPE_COUNT = "COUNT";
    public static final String SOS_OBSERVATION_TYPE_BOOLEAN = "BOOLEAN";
    public static final String OGC_DISCOVERY_ID_TERM_DEFINITION = "urn:ogc:def:identifier:OGC:1.0:uniqueID";
    public static final String OGC_DISCOVERY_LONG_NAME_DEFINITION = "urn:ogc:def:identifier:OGC:1.0:longName";
    public static final String OGC_DISCOVERY_SHORT_NAME_DEFINITION = "urn:ogc:def:identifier:OGC:1.0:shortName";
    public static final String OGC_DISCOVERY_INTENDED_APPLICATION_DEFINITION = "urn:ogc:def:classifier:OGC:1.0:application";
    public static final String OGC_DISCOVERY_OBSERVED_BBOX_DEFINITION = "urn:ogc:def:property:OGC:1.0:observedBBOX";

    public static final String SOS_EXCEPTION_OBSERVATION_ALREADY_CONTAINED = "This observation is already contained in SOS database!";

    public static final String SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_START = "The offering with the identifier";
    public static final String SOS_200_OFFERING_ALREADY_REGISTERED_MESSAGE_END = "still exists in this service and it is not allowed to insert more than one procedure to an offering!";

    public static final String SOS_200_DUPLICATE_OBSERVATION_CONSTRAINT = "observation_featureofinterestid_observablepropertyid_proced_key";

    public static HashMap<String, Boolean> EPSG_EASTING_FIRST_MAP = null;
    static {
        EPSG_EASTING_FIRST_MAP = new HashMap<String, Boolean>();
        EPSG_EASTING_FIRST_MAP.put("default", false);
        EPSG_EASTING_FIRST_MAP.put("4326",false);
        EPSG_EASTING_FIRST_MAP.put("4979",false);
        EPSG_EASTING_FIRST_MAP.put("21037",true);

    }

    public enum ImportStrategy {
        /**
         * Each value will be inserted as single observation into the SOS.
         */
        SingleObservation,
        /**
         * The file will be read at once. For each identified timeseries
         * an OM_SWEArrayObservation will be created and inserted into the
         * SOS using the "SplitDataArrayIntoObservations" of the 52North
         * implementation.
         */
        SweArrayObservationWithSplitExtension;
    }

    private SosImportConfiguration importConf;
    private final File configFile;

    private Pattern localeFilePattern = null;

    public Configuration(final String pathToFile) throws XmlException, IOException {
        LOG.trace("Configuration({})",pathToFile);
        configFile = new File(pathToFile);
        final SosImportConfigurationDocument sosImportDoc =
                SosImportConfigurationDocument.Factory.parse(configFile);
        // Create an XmlOptions instance and set the error listener.
        final XmlOptions validateOptions = new XmlOptions();
        final ArrayList<XmlError> errorList = new ArrayList<XmlError>();
        validateOptions.setErrorListener(errorList);

        // Validate the XML.
        final boolean isValid = sosImportDoc.validate(validateOptions);

        // If the XML isn't valid, loop through the listener's contents,
        // printing contained messages.
        if (!isValid) {
            for (int i = 0; i < errorList.size(); i++) {
                final XmlError error = errorList.get(i);

                LOG.error("Message: {}; Location: {}",
                        error.getMessage(),
                        error.getCursorLocation().xmlText());
            }
            final String msg = "Configuration is not valid and could not be parsed.";
            throw new XmlException(msg, null, errorList);
        } else {
            importConf = sosImportDoc.getSosImportConfiguration();
            setLocaleFilePattern();
        }
    }

    private void setLocaleFilePattern() {
        if (isRegularExpressionForLocalFileAvailable()) {
            localeFilePattern  = Pattern.compile(importConf.getDataFile().getLocalFile().getRegularExpresssionForAllowedFileNames());
        }
    }

    /**
     * For testing only!
     */
    protected Configuration(final SosImportConfiguration importConf) {
        if (importConf == null) {
            throw new IllegalArgumentException("SosImportConfiguration MUST NOT be null or INVALID");
        }
        this.importConf = importConf;
        configFile = null;
        setLocaleFilePattern();
    }

    private boolean isRegularExpressionForLocalFileAvailable()
    {
        return importConf.getDataFile().isSetLocalFile() &&
                importConf.getDataFile().getLocalFile().isSetRegularExpresssionForAllowedFileNames() &&
                importConf.getDataFile().getLocalFile().getRegularExpresssionForAllowedFileNames() != null &&
                !importConf.getDataFile().getLocalFile().getRegularExpresssionForAllowedFileNames().isEmpty();
    }

    /**
     * Returns a File instance pointing to the data file defined in XML import
     * configuration.
     * @return a <b><code>new File</code></b> instance pointing to
     *             <code>DataFile.LocalFile.Path</code> or<br />
     *             <b><code>null</code></b>, if element is not defined in config
     */
    public File getDataFile() {
        LOG.trace("getDataFile()");
        if (importConf.getDataFile() != null &&
                importConf.getDataFile().isSetLocalFile() &&
                !importConf.getDataFile().getLocalFile().getPath().equalsIgnoreCase("") ) {
            // Path for LocalFile set to something, so return a new File using is
            return new File(importConf.getDataFile().getLocalFile().getPath());
        }
        LOG.error("DataFile.LocalFile.Path not set!");
        return null;
    }

    public File getConfigFile() {
        return configFile;
    }

    /**
     * Returns a truth value according to the presence of the remote file
     * element in the xml document.
     *
     * @return true if it is a remote file
     */
    public boolean isRemoteFile() {
        return importConf.getDataFile().getRemoteFile() != null;
    }

    /**
     * Returns the host name of the ftp server.
     *
     * @return ftp host
     */
    public String getFtpHost() {
        final String[] splitString = importConf.getDataFile().getRemoteFile().getURL().split("/");
        return splitString[0];
    }

    /**
     * Returns a string, that indicates the path of subdirectories, where the
     * ftp file is located.
     *
     * @return subdirectory structure
     */
    public String getFtpSubdirectory() {
        final String[] splitString = importConf.getDataFile().getRemoteFile().getURL().split("/");
        String result = "";
        // certain file
        if (!isFtpUrlRegex()) {
            for (int i = 1; i < splitString.length-1; i++) {
                result += splitString[i];
            }
        } else
        // regular expression
        {
            // TODO
        }

        return result;
    }

    /**
     * Returns the name of the ftp file.
     *
     * @return ftp file name
     */
    public String getFtpFile() {
        final String[] splitString = importConf.getDataFile().getRemoteFile().getURL().split("/");
        String result;
        // certain file
        if (!isFtpUrlRegex()) {
            result = splitString[splitString.length-1];
        } else
        // regular expression
        {
            // TODO
            result = null;
        }
        return result;
    }

    public boolean isFtpUrlRegex() {
        LOG.trace("isSosUrlRegex()");
        return importConf.getDataFile().getReferenceIsARegularExpression();
    }

    /**
     *
     * @return
     * @throws MalformedURLException
     */
    public URL getSosUrl() throws MalformedURLException {
        LOG.trace("getSosUrl()");
        if (!importConf.getSosMetadata().getURL().equalsIgnoreCase("") ){
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

    /**
     * The number of the first line with data. Line counting starts at 0.
     * @return
     */
    public int getFirstLineWithData() {
        return importConf.getCsvMetadata().getFirstLineWithData();
    }

    public char getCsvSeparator() {
        final String sep = importConf.getCsvMetadata().getParameter().getColumnSeparator();
        if (sep.equals(Configuration.COLUMN_SEPARATOR_SPACE)) {
            return ' ';
        } else if (sep.equals(Configuration.COLUMN_SEPARATOR_TAB)) {
            return '\t';
        } else    {
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
     * @return An <code>int[]</code> if any measured value column is found. <code>null</code>
     *         if no column is found.
     */
    public int[] getMeasureValueColumnIds() {
        LOG.trace("getMeasureValueColumnIds()");
        final Column[] cols = importConf.getCsvMetadata().getColumnAssignments().getColumnArray();
        final ArrayList<Integer> ids = new ArrayList<Integer>();
        for (final Column column : cols) {
            if (column.getType().equals(Type.MEASURED_VALUE)){
            	LOG.debug("Found measured value column: {}", column.getNumber());
                ids.add(column.getNumber());
            }
        }
        ids.trimToSize();
        if (ids.size() > 0) {
            final int[] result = new int[ids.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = ids.get(i);
            }
            return result;
        }
        return null;
    }

    public int[] getIgnoredColumnIds() {
    	LOG.trace("getIgnoredColumnIds()");
        final Column[] cols = importConf.getCsvMetadata().getColumnAssignments().getColumnArray();
        final ArrayList<Integer> ids = new ArrayList<Integer>();
        for (final Column column : cols) {
            if (column.getType().equals(Type.DO_NOT_EXPORT)){
            	LOG.debug("Found ignored column: {}", column.getNumber());
                ids.add(column.getNumber());
            }
        }
        ids.trimToSize();
        if (ids.size() > 0) {
            final int[] result = new int[ids.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = ids.get(i);
            }
            return result;
        }
        return null;
    }

    /**
     * Returns the column id for the given measured value column if available.
     * If not -1.
     * @param mvColumnId
     * @return The column id of the sensor related to this measure value column
     *             or -1 if no sensor column is available for this column
     */
    public int getColumnIdForSensor(final int mvColumnId) {
        LOG.trace(String.format("getColumnIdForSensor(%d)",
                mvColumnId));
        // check for RelatedSensor element and if its a number -> return number
        final Column c = getColumnById(mvColumnId);
        if (c.getRelatedSensorArray() != null && c.getRelatedSensorArray().length > 0) {
            final RelatedSensor rS = c.getRelatedSensorArray(0);
            if (rS.isSetNumber()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Found RelatedSensor column for measured value column %d: %d",
                            mvColumnId,
                            rS.getNumber()));
                }
                return rS.getNumber();
            } else if (rS.isSetIdRef()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Found RelatedSensor %s is not a column but a Resource.", rS.getIdRef() ));
                }
            } else {
                LOG.error(String.format("RelatedSensor element not set properly: %s", rS.xmlText()));
            }
        }
        // if element is not set
        //    get column id from ColumnAssignments
        final Column[] cols = importConf.getCsvMetadata().getColumnAssignments().getColumnArray();
        for (final Column column : cols) {
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

    /**
     *
     * @param mvColumnId
     * @return
     */
    public Column getColumnById(final int mvColumnId) {
        LOG.trace(String.format("getColumnById(%d)",mvColumnId));
        final Column[] cols = importConf.getCsvMetadata().getColumnAssignments().getColumnArray();
        for (final Column column : cols) {
            if (column.getNumber() == mvColumnId) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Column found for id %d",
                            mvColumnId));
                }
                return column;
            }
        }
        LOG.error(String.format("CsvMetadat.ColumnAssignments not set properly. Could not find Column for id %d.",
                mvColumnId));
        return null;
    }

    /**
     * Returns the SensorType linked to the column, identified by the given id,
     * by its RelatedSensor.IdRef element. If no sensor could be found
     * <code>null</code> is returned.
     * @param mvColumnId
     * @return
     */
    public SensorType getRelatedSensor(final int mvColumnId) {
        LOG.trace(String.format("getRelatedSensor(%d)",
                    mvColumnId));
        final Column c = getColumnById(mvColumnId);
        if (c.getRelatedSensorArray() != null &&
                c.getRelatedSensorArray().length > 0 &&
                c.getRelatedSensorArray(0) != null &&
                c.getRelatedSensorArray(0).isSetIdRef()) {
            final String sensorXmlId = c.getRelatedSensorArray(0).getIdRef();
            if (importConf.getAdditionalMetadata() != null &&
                    importConf.getAdditionalMetadata().getSensorArray() != null &&
                    importConf.getAdditionalMetadata().getSensorArray().length > 0) {
                for (final SensorType s : importConf.getAdditionalMetadata().getSensorArray()) {
                    if (s.getResource() != null && s.getResource().getID() != null && s.getResource().getID().equals(sensorXmlId)) {
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

    /**
     *
     * @param mvColumnId
     * @return
     */
    public int getColumnIdForFoi(final int mvColumnId) {
        LOG.trace(String.format("getColumnIdForFoi(%d)",
                mvColumnId));
        // check for RelatedFOI element and if its a number -> return number
        final Column c = getColumnById(mvColumnId);
        if (c.getRelatedFOIArray() != null && c.getRelatedFOIArray().length > 0) {
            final RelatedFOI rF = c.getRelatedFOIArray(0);
            if (rF.isSetNumber()) {
                LOG.debug(String.format("Found RelatedFOI column for measured value column %d: %d",
                        mvColumnId,
                        rF.getNumber()));
                return rF.getNumber();
            } else if (rF.isSetIdRef()) {
                LOG.debug(String.format("Found RelatedFOI %s is not a column but a Resource.", rF.getIdRef() ));
            } else {
                LOG.error(String.format("RelatedFOI element not set properly: %s", rF.xmlText()));
            }
        }
        // if element is not set
        //    get column id from ColumnAssignments
        final Column[] cols = importConf.getCsvMetadata().getColumnAssignments().getColumnArray();
        for (final Column column : cols) {
            if (column.getType().equals(Type.FOI)) {
                LOG.debug(String.format("Found related feature of interest column for measured value column %d: %d",
                        mvColumnId,
                        column.getNumber()));
                return column.getNumber();
            }
        }
        return -1;
    }

    /**
     *
     * @param foiUri
     * @return
     */
    public Position getFoiPosition(final String foiUri) {
        LOG.trace(String.format("getFoiPosition(%s)",
                foiUri));
        // get all elements from foi positions and compare the uri
        if (importConf.getAdditionalMetadata() != null &&
                importConf.getAdditionalMetadata().getFOIPositionArray() != null &&
                importConf.getAdditionalMetadata().getFOIPositionArray().length > 0) {
            final FOIPosition[] foiPos = importConf.getAdditionalMetadata().getFOIPositionArray();
            for (final FOIPosition pos : foiPos) {
                if (pos.getURI() != null &&
                        pos.getURI().getStringValue() != null &&
                        pos.getURI().getStringValue().equals(foiUri)){
                    // if element is found -> fill position
                    final org.x52North.sensorweb.sos.importer.x04.PositionDocument.Position p = pos.getPosition();
                    if (p.isSetAlt() &&
                            p.isSetEPSGCode() &&
                            p.isSetLat() &&
                            p.isSetLong()) {
                        return getModelPositionXBPosition(p);
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param p {@link org.x52North.sensorweb.sos.importer.x04.PositionDocument.Position}
     * @return {@link org.n52.sos.importer.feeder.model.Position}
     */
    public Position getModelPositionXBPosition(
            final org.x52North.sensorweb.sos.importer.x04.PositionDocument.Position p) {
        LOG.trace("getPosition()");
        final String[] units = new String[3];
        final double[] values = new double[3];

        if (p.isSetAlt()) {
            units[Position.ALT] = p.getAlt().getUnit();
            values[Position.ALT] = p.getAlt().getFloatValue();
        } else {
            units[Position.ALT] = Position.UNIT_NOT_SET;
            values[Position.ALT] = Position.VALUE_NOT_SET;
        }
        units[Position.LAT] = p.getLat().getUnit();
        values[Position.LAT] = p.getLat().getFloatValue();

        units[Position.LONG] = p.getLong().getUnit();
        values[Position.LONG] = p.getLong().getFloatValue();

        final int epsgCode = p.getEPSGCode();
        return new Position(values, units, epsgCode);
    }

    /**
     *
     * @param mvColumnId
     * @return
     */
    public FeatureOfInterestType getRelatedFoi(final int mvColumnId) {
        LOG.trace(String.format("getRelatedFoi(%d)",
                mvColumnId));
        final Column c = getColumnById(mvColumnId);
        if (c.getRelatedFOIArray() != null &&
                c.getRelatedFOIArray(0) != null &&
                c.getRelatedFOIArray(0).isSetIdRef()) {
            final String foiXmlId = c.getRelatedFOIArray(0).getIdRef();
            if (importConf.getAdditionalMetadata() != null &&
                    importConf.getAdditionalMetadata().getFeatureOfInterestArray() != null &&
                    importConf.getAdditionalMetadata().getFeatureOfInterestArray().length > 0) {
                for (final FeatureOfInterestType foi : importConf.getAdditionalMetadata().getFeatureOfInterestArray()) {
                    if (foi.getResource() != null && foi.getResource().getID() != null && foi.getResource().getID().equals(foiXmlId)) {
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

    public Position getPosition(final String group, final String[] values) throws ParseException {
        LOG.trace(String.format("getPosition(group:%s,%s)",
                group,
                Arrays.toString(values)));
        final Column[] cols = getAllColumnsForGroup(group, Type.POSITION);
        // combine the values from the different columns
        final String[] units = new String[3];
        final double[] posValues = new double[3];
        int epsgCode = -1;
        for (final Column c : cols) {
            //            boolean isCombination = false; // now every position is of type combination
            for (final Metadata m : c.getMetadataArray()) {
                // check for type combination
                //                if (m.getKey().equals(Key.TYPE) && m.getValue().equals(Configuration.POSITION_TYPE_COMBINATION)) {
                //                    isCombination = true;
                //                }
                // get parse pattern and parse available values
                /*else*/ if (m.getKey().equals(Key.PARSE_PATTERN)) {
                    String pattern = m.getValue();
                    pattern = pattern.replaceAll(Configuration.POSITION_PARSEPATTERN_LATITUDE, "{0}");
                    pattern = pattern.replaceAll(Configuration.POSITION_PARSEPATTERN_LONGITUDE, "{1}");
                    pattern = pattern.replaceAll(Configuration.POSITION_PARSEPATTERN_ALTITUDE, "{2}");
                    pattern = pattern.replaceAll(Configuration.POSITION_PARSEPATTERN_EPSG, "{3}");

                    final MessageFormat mf = new MessageFormat(pattern);
                    Object[] tokens = null;
                    try {
                        tokens = mf.parse(values[c.getNumber()]);
                    } catch (final ParseException e) {
                        throw new NumberFormatException();
                    }

                    if (tokens == null) {
                        throw new NumberFormatException();
                    }

                    Object[] latitude, longitude, height;

                    if (tokens.length > 0 && tokens[0] != null) {
                        latitude = parseLat((String) tokens[0]);
                        posValues[Position.LAT] = (Double) latitude[0];
                        units[Position.LAT] = (String) latitude[1];
                    }
                    if (tokens.length > 1 && tokens[1] != null) {
                        longitude = parseLon((String)tokens[1]);
                        posValues[Position.LONG] = (Double) longitude[0];
                        units[Position.LONG] = (String) longitude[1];
                    }
                    if (tokens.length > 2 && tokens[2] != null) {
                        height = parseAlt((String)tokens[2]);
                        posValues[Position.ALT] = (Double) height[0];
                        units[Position.ALT] = (String) height[1];
                    }
                    if (tokens.length > 3 && tokens[3] != null) {
                        epsgCode = Integer.valueOf((String)tokens[3]);
                    }
                }
                // get additional information
                // LATITUDE
                else if (m.getKey().equals(Key.POSITION_LATITUDE)) {
                    final Object[] latitude = parseLat(m.getValue());
                    posValues[Position.LAT] = (Double) latitude[0];
                    units[Position.LAT] = (String) latitude[1];
                }
                // LONGITUDE
                else if (m.getKey().equals(Key.POSITION_LONGITUDE)) {
                    final Object[] longitude = parseLon(m.getValue());
                    posValues[Position.LONG] = (Double) longitude[0];
                    units[Position.LONG] = (String) longitude[1];
                }
                // ALTITUDE
                else if (m.getKey().equals(Key.POSITION_ALTITUDE)) {
                    final Object[] altitude = parseAlt(m.getValue());
                    posValues[Position.ALT] = (Double) altitude[0];
                    units[Position.ALT] = (String) altitude[1];
                }
                // EPSG
                else if (m.getKey().equals(Key.POSITION_EPSG_CODE)) {
                    epsgCode = Integer.valueOf(m.getValue());
                }
            }

        }
        return new Position(posValues, units, epsgCode);
    }

    private Object[] parseAlt(final String alt) throws ParseException {
        LOG.trace(String.format("parseAlt(%s)",
                alt));
        double value = Position.VALUE_NOT_SET;
        String unit = Position.UNIT_NOT_SET;

        String number;
        if (alt.contains("km")) {
            unit = "km";
            number = alt.replace("km", "");
        }
        else if (alt.contains("mi")) {
            unit = "mi";
            number = alt.replace("mi", "");
        }
        else if (alt.contains("m")) {
            unit = "m";
            number = alt.replace("m", "");
        }
        else if (alt.contains("ft")) {
            unit = "ft";
            number = alt.replace("ft", "");
        }
        else {
            number = alt;
        }
        value = parseToDouble(number);

        return new Object[] {value, unit};
    }

    private Object[] parseLon(final String lon) throws ParseException {
        LOG.trace(String.format("parseLon(%s)",
                lon));
        double value;
        String unit = "";

        String number;
        //TODO handle inputs like degrees/minutes/seconds, n.Br.
        if (lon.contains("°")) {
            unit = "°";
            final String[] part = lon.split("°");
            number = part[0];
        } else if (lon.contains("m")) {
            unit = "m";
            number = lon.replace("m", "");
        } else {
            number = lon;
        }
        value = parseToDouble(number);

        if (unit.equals("")) {
            if (value <= 180.0 && value >= -180.0) {
                unit = "deg";
            }
            else {
                unit = "m";
            }
        }

        final Object[] result = {value, unit};
        return result;
    }

    private Object[] parseLat(final String lat) throws ParseException {
        LOG.trace(String.format("parseLat(%s)",
                lat));
        double value;
        String unit = "";

        String number;
        //TODO handle inputs like degrees/minutes/seconds, n.Br.
        if (lat.contains("°")) {
            unit = "°";
            final String[] part = lat.split("°");
            number = part[0];
        } else if (lat.contains("m")) {
            unit = "m";
            number = lat.replace("m", "");
        } else {
            number = lat;
        }
        value = parseToDouble(number);

        if (unit.equals("")) {
            if (value <= 90.0 && value >= -90.0) {
                unit = "deg";
            }
            else {
                unit = "m";
            }
        }

        final Object[] result = {value, unit};
        return result;
    }

    public double parseToDouble(final String number) throws ParseException{
        LOG.trace(String.format("parseToDouble(%s)",
                number));
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        final char dSep = getDecimalSeparator();
        symbols.setDecimalSeparator(dSep);
        symbols.setGroupingSeparator(getThousandsSeparator(dSep));

        Number n;
        final DecimalFormat formatter = new DecimalFormat();
        formatter.setDecimalFormatSymbols(symbols);
        n = formatter.parse(number);

        return n.doubleValue();
    }

    private char getThousandsSeparator(final char dSep) {
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
     * @param group a <code>{@link java.lang.String String}</code> as group identifier
     * @return a <code>Column[]</code> having all the group id
     *             <code>group</code> <b>or</or><br />
     */
    public Column[] getAllColumnsForGroup(final String group, final Enum t) {
        LOG.trace("getAllColumnsForGroup()");
        if (group == null) { return null; }
        final Column[] allCols = importConf.getCsvMetadata().getColumnAssignments().getColumnArray();
        final ArrayList<Column> tmpResultSet = new ArrayList<Column>(allCols.length);
        for (final Column col : allCols) {
            if (col.getType() != null &&
                    col.getType().equals(t) ) {
                // we have a position or dateTime
                // check the Metadata kvps
                if (col.getMetadataArray() != null && col.getMetadataArray().length > 0) {
                    findGroup:
                        for (final Metadata meta : col.getMetadataArray()) {
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
     * @return a <code>{@link java.lang.String String}</code>
     */
    public String getFirstDateTimeGroup() {
        LOG.trace("getFirstDateTimeGroup()");
        final Column[] cols = importConf.getCsvMetadata().getColumnAssignments().getColumnArray();
        for (final Column col : cols) {
            if (col.getType().equals(Type.DATE_TIME)){
                // it's DATE_TIME -> get group id from metadata[]
                if (col.getMetadataArray() != null && col.getMetadataArray().length > 0) {
                    for (final Metadata m : col.getMetadataArray()) {
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
     * @param idRef
     * @return <code>UnitOfMeasurementType</code> instance with
     *                 <code>id == idRef</code>,<br />or <code>null</code>
     */
    public UnitOfMeasurementType getUomById(final String idRef) {
        LOG.trace(String.format("getUomById('%s')",
                idRef));
        final UnitOfMeasurementType[] uoms = importConf.getAdditionalMetadata().getUnitOfMeasurementArray();
        for (final UnitOfMeasurementType uom : uoms) {
            if (uom.getResource().getID().equals(idRef)) {
                return uom;
            }
        }
        return null;
    }

    /**
     * Checks all columns in CsvMetadata.ColumnAssignments.Column[] and returns
     * the id of the first column with Type "UOM"
     * @param mVColumnId
     * @return the id of the first uom column or -1 if not found
     */
    public int getColumnIdForUom(final int mVColumnId) {
        LOG.trace(String.format("getColumnIdForUom(%s)",
                mVColumnId));
        final Column[] cols = importConf.getCsvMetadata().
                getColumnAssignments().getColumnArray();
        for (final Column col : cols) {
            if (col.getType().equals(Type.UOM)) {
                return col.getNumber();
            }
        }
        return -1;
    }

    /**
     * Returns the op with the given id or <code>null</code>
     * @param idRef
     * @return
     */
    public ObservedPropertyType getObsPropById(final String idRef) {
        LOG.trace(String.format("getObsPropById('%s')",
                idRef));
        final ObservedPropertyType[] ops =
                importConf.getAdditionalMetadata().getObservedPropertyArray();
        for (final ObservedPropertyType op : ops) {
            if (op.getResource().getID().equals(idRef)) {
                return op;
            }
        }
        return null;
    }

    /**
     * Checks all columns in CsvMetadata.ColumnAssignments.Column[] and returns
     * the id of the first column with Type "OBSERVED_PROPERTY"
     * @param mVColumnId
     * @return the id of the first op column or -1 if not found
     */
    public int getColumnIdForOpsProp(final int mVColumnId) {
        LOG.trace(String.format("getColumnIdForOpsProp(%s)",
                mVColumnId));
        final Column[] cols = importConf.getCsvMetadata().
                getColumnAssignments().getColumnArray();
        for (final Column col : cols) {
            if (col.getType().equals(Type.OBSERVED_PROPERTY)) {
                return col.getNumber();
            }
        }
        return -1;
    }

    public Offering getOffering(final Sensor s) {
        LOG.trace("getOffering()");
        if( importConf.getSosMetadata().getOffering().isSetGenerate() &&
                importConf.getSosMetadata().getOffering().getGenerate()) {
            return new Offering(s.getName(), s.getUri());
        } else {
            final String o = importConf.getSosMetadata().getOffering().getStringValue();
            return new Offering(o,o);
        }
    }

    public String getFileName() {
        return configFile.getName();
    }

    @Override
    public String toString() {
        return String.format("Configuration [file=%s]", configFile);
    }

    public String getType(final int mVColumnId) {
        for (final Column col : importConf.getCsvMetadata().getColumnAssignments().getColumnArray()) {
            if (col.getNumber() == mVColumnId) {
                for (final Metadata m : col.getMetadataArray()) {
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
        return (getMeasureValueColumnIds().length == 1);
    }

    public String getSosVersion() {
        LOG.trace("getSosVersion()");
        return importConf.getSosMetadata().getVersion();
    }

    public String getSosBinding() {
        LOG.trace("getSosBinding()");
        if (importConf.getSosMetadata().isSetBinding())
        {
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

    /**
     * @return <code>true</code>, if all required attributes are available for
     * 			importing sample based files,<br />
     * 			else <code>false</code>.
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


    /**
     * @return The configured value <b>> 0</b>, if it is set<br />
     *             else <b>-1</b>.
     */
    public int getLastModifiedDelta() {
        if (importConf.getDataFile().isSetLastModifiedDelta()) {
            return importConf.getDataFile().getLastModifiedDelta();
        }
        return -1;
    }

    /**
     * @return number of the line that contains the header information for the
     *             first time, or<br />
     *             <b>-1</b> if the optional attribute "headerLine" is not
     *             set in DataFile element
     */
    public int getHeaderLine() {
        if (importConf.getDataFile().isSetHeaderLine()) {
            return importConf.getDataFile().getHeaderLine().intValue();
        }
        return -1;
    }

    /**
     * @return Name of the data file encoding, or <br />
     *             if not set, "UTF-8"
     */
    public String getDataFileEncoding() {
        if (importConf.getDataFile().isSetLocalFile() &&
                importConf.getDataFile().getLocalFile().isSetEncoding() &&
                !importConf.getDataFile().getLocalFile().getEncoding().isEmpty()) {
            return importConf.getDataFile().getLocalFile().getEncoding();
        }
        LOG.debug("Using default encoding 'UTF-8'");
        return "UTF-8";
    }

    /**
     * @return The {@link ImportStrategy} that could be configured in
     *         <code>SosImportConfiguration/AdditionalMetadata/Metadata/Key=IMPORT_STRATEGY/Value=The_import_strategy_to_use</code>
     *         <br />Default if nothing matching is found: {@link ImportStrategy#SingleObservation}
     */
    public ImportStrategy getImportStrategy() {
        if (importConf.isSetAdditionalMetadata() && importConf.getAdditionalMetadata().getMetadataArray().length > 0) {
            for (int i = 0; i < importConf.getAdditionalMetadata().getMetadataArray().length; i++) {
                final Metadata metadata = importConf.getAdditionalMetadata().getMetadataArray(i);
                if (metadata.getKey().equals(Key.IMPORT_STRATEGY)) {
                    if (metadata.getValue().equalsIgnoreCase(ImportStrategy.SweArrayObservationWithSplitExtension.name())) {
                        return ImportStrategy.SweArrayObservationWithSplitExtension;
                    } else {
                        return ImportStrategy.SingleObservation;
                    }
                }
            }
        }
        return ImportStrategy.SingleObservation;
    }

    public int getHunkSize() {
        if (importConf.isSetAdditionalMetadata() && importConf.getAdditionalMetadata().getMetadataArray().length > 0) {
            for (int i = 0; i < importConf.getAdditionalMetadata().getMetadataArray().length; i++) {
                final Metadata metadata = importConf.getAdditionalMetadata().getMetadataArray(i);
                if (metadata.getKey().equals(Key.HUNK_SIZE)) {
                    try {
                        return Integer.parseInt(metadata.getValue());
                    } catch (final NumberFormatException nfe) {
                        LOG.error(
                                String.format("Value of metadata element with key '%s' could not be parsed to int: '%s'. Ignoring it.",
                                        Key.HUNK_SIZE.toString()),
                                nfe);
                    }
                }
            }
        }
        return -1;
    }


    public boolean isIgnoreLineRegExSet() {
    	return importConf.getDataFile().getIgnoreLineRegExArray() != null &&
    			importConf.getDataFile().getIgnoreLineRegExArray().length > 0;
	}

	public Pattern[] getIgnoreLineRegExPatterns() {
		if (!isIgnoreLineRegExSet()) {
			return new Pattern[0];
		}
		final String[] ignoreLineRegExArray = importConf.getDataFile().getIgnoreLineRegExArray();
		final LinkedList<Pattern> patterns = new LinkedList<>();
		for (final String regEx : ignoreLineRegExArray) {
			if (regEx != null && !regEx.isEmpty()) {
				patterns.add(Pattern.compile(regEx));
			}
		}
		return patterns.toArray(new Pattern[patterns.size()]);
	}

	public boolean isInsertSweArrayObservationTimeoutBufferSet() {
		return importConf.getSosMetadata().isSetInsertSweArrayObservationTimeoutBuffer();
	}

	public int getInsertSweArrayObservationTimeoutBuffer() {
		if (isInsertSweArrayObservationTimeoutBufferSet()) {
			return importConf.getSosMetadata().getInsertSweArrayObservationTimeoutBuffer();
		}
		throw new IllegalArgumentException("Attribute 'insertSweArrayObservationTimeoutBuffer' of <SosMetadata> not set.");
	}

	public int getSampleSizeDivisor() {
		if (isSamplingFile() && importConf.getDataFile().isSetSampleSizeDivisor()) {
			return importConf.getDataFile().getSampleSizeDivisor();
		}
		return 1;
	}

	public boolean isCsvParserDefined() {
		return importConf.getCsvMetadata().isSetCsvParserClass();
	}

	public String getCsvParser() {
		return isCsvParserDefined()?importConf.getCsvMetadata().getCsvParserClass():WrappedCSVReader.class.getName();
	}

}
