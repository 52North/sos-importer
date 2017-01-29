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
package org.n52.sos.importer.view.combobox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;

import org.n52.sos.importer.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * loads and saves all combobox items in the properties file
 *
 * @author Raimund
 * @version $Id: $Id
 */
public final class ComboBoxItems {

    private static final String STACKTRACE = "Stacktrace";
    private static final String SOS_PROPERTIES_COULD_NOT_BE_SAVED = "SOS properties could not be saved.";
    private static final String SENSOR_URIS = "sensorURIs";
    private static final String UNIT_OF_MEASUREMENT_URIS = "unitOfMeasurementURIs";
    private static final String OBSERVED_PROPERTY_URIS = "observedPropertyURIs";
    private static final String FEATURE_OF_INTEREST_URIS = "featureOfInterestURIs";
    private static final String SENSOR_NAMES = "sensorNames";
    private static final String UNIT_OF_MEASUREMENT_CODES = "unitOfMeasurementCodes";
    private static final String OBSERVED_PROPERTY_NAMES = "observedPropertyNames";
    private static final String FEATURE_OF_INTEREST_NAMES = "featureOfInterestNames";
    private static final String SOS_UR_LS = "sosURLs";
    private static final String REFERENCE_SYSTEM_NAMES = "referenceSystemNames";
    private static final String EPSG_CODES = "epsgCodes";
    private static final String POSITION_PATTERNS = "positionPatterns";
    private static final String DATE_AND_TIME_PATTERNS = "dateAndTimePatterns";
    private static final String TEXT_QUALIFIERS = "textQualifiers";
    private static final String COMMENT_INDICATORS = "commentIndicators";
    private static final String COLUMN_SEPARATORS = "columnSeparators";
    private static final String LOAD_DEFAULT_SETTINGS_FROM_JAR_FILE = "Load default settings from jar file";

    private static final Logger logger = LoggerFactory.getLogger(ComboBoxItems.class);

    private static ComboBoxItems instance;

    private static final String EXTERNAL_FILE_PATH =
            System.getProperty("user.home") + File.separator + ".SOSImporter" + File.separator;

    private static final String INTERNAL_FILE_PATH = "/org/n52/sos/importer/combobox/";

    private static final String FILE_NAME = "52n-sensorweb-sos-importer.properties";

    private final Properties props = new Properties();

    private String[] decimalSeparators;
    private String[] latLonUnits;
    private String[] heightUnits;
    private String[] dateAndTimeGroups;
    private String[] positionGroups;
    private String[] columnSeparators;
    private String[] commentIndicators;
    private String[] textQualifiers;
    private String[] dateAndTimePatterns;
    private String[] positionPatterns;
    private String[] epsgCodes;
    private String[] referenceSystemNames;
    private String[] sosURLs;
    private String[] featureOfInterestNames;
    private String[] observedPropertyNames;
    private String[] unitOfMeasurementCodes;
    private String[] sensorNames;
    private String[] featureOfInterestURIs;
    private String[] observedPropertyURIs;
    private String[] unitOfMeasurementURIs;
    private String[] sensorURIs;

    private ComboBoxItems() {
        InputStream is = null;
        try {
            String filePath = EXTERNAL_FILE_PATH + FILE_NAME;
            final File file = new File(filePath);
            if (!file.exists()) {
                logger.info(LOAD_DEFAULT_SETTINGS_FROM_JAR_FILE);
                filePath = INTERNAL_FILE_PATH + FILE_NAME;
                is = getClass().getResourceAsStream(filePath);
            } else if (!file.canRead()) {
                logger.warn("Could not load settings.");
                logger.warn("No reading permissions for " + file);
                logger.info(LOAD_DEFAULT_SETTINGS_FROM_JAR_FILE);
                filePath = INTERNAL_FILE_PATH + FILE_NAME;
                is = getClass().getResourceAsStream(filePath);
            } else {
                logger.info("Load settings from " + file);
                is = new FileInputStream(file);
            }

            props.load(is);
        } catch (final FileNotFoundException e) {
            logExceptionAndThrowRuntimeException(e, "SOS Importer Settings not found");
        } catch (final IOException e) {
            logExceptionAndThrowRuntimeException(e, "SOS Importer Settings not readable.");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log(e);
                }
            }
        }

        decimalSeparators = parse(props.getProperty("decimalSeparators"));
        latLonUnits = parse(props.getProperty("latLonUnits"));
        heightUnits = parse(props.getProperty("heightUnits"));
        dateAndTimeGroups = parse(props.getProperty("dateAndTimeGroups"));
        positionGroups = parse(props.getProperty("positionGroups"));

        columnSeparators = parse(props.getProperty(COLUMN_SEPARATORS));
        commentIndicators = parse(props.getProperty(COMMENT_INDICATORS));
        textQualifiers = parse(props.getProperty(TEXT_QUALIFIERS));
        dateAndTimePatterns = parse(props.getProperty(DATE_AND_TIME_PATTERNS));
        positionPatterns = parse(props.getProperty(POSITION_PATTERNS));
        epsgCodes = parse(props.getProperty(EPSG_CODES));
        referenceSystemNames = parse(props.getProperty(REFERENCE_SYSTEM_NAMES));
        sosURLs = parse(props.getProperty(SOS_UR_LS));

        featureOfInterestNames = parse(props.getProperty(FEATURE_OF_INTEREST_NAMES));
        observedPropertyNames = parse(props.getProperty(OBSERVED_PROPERTY_NAMES));
        unitOfMeasurementCodes = parse(props.getProperty(UNIT_OF_MEASUREMENT_CODES));
        sensorNames = parse(props.getProperty(SENSOR_NAMES));
        featureOfInterestURIs = parse(props.getProperty(FEATURE_OF_INTEREST_URIS));
        observedPropertyURIs = parse(props.getProperty(OBSERVED_PROPERTY_URIS));
        unitOfMeasurementURIs = parse(props.getProperty(UNIT_OF_MEASUREMENT_URIS));
        sensorURIs = parse(props.getProperty(SENSOR_URIS));
    }

    private void log(IOException e) {
        logger.error("Exception thrown!", e.getMessage());
        logger.debug(STACKTRACE, e);
    }

    private void logExceptionAndThrowRuntimeException(final IOException e, String message) {
        logger.error(message, e);
        logger.debug(STACKTRACE, e);
        throw new RuntimeException(message, e);
    }

    /**
     * <p>Getter for the field <code>instance</code>.</p>
     *
     * @return a {@link org.n52.sos.importer.view.combobox.ComboBoxItems} object.
     */
    public static ComboBoxItems getInstance() {
        if (instance == null) {
            instance = new ComboBoxItems();
        }
        return instance;
    }

    /**
     * <p>parse.</p>
     *
     * @param property a {@link java.lang.String} object.
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] parse(final String property) {
        if (property == null) {
            return new String[0];
        }
        final String[] values = property.split(Constants.SEPARATOR_STRING);
        return values;
    }

    /**
     * <p>save.</p>
     */
    public void save() {
        props.setProperty(COLUMN_SEPARATORS, format(EditableComboBoxItems.getInstance().getColumnSeparators()));
        props.setProperty(COMMENT_INDICATORS, format(EditableComboBoxItems.getInstance().getCommentIndicators()));
        props.setProperty(TEXT_QUALIFIERS, format(EditableComboBoxItems.getInstance().getTextQualifiers()));
        props.setProperty(DATE_AND_TIME_PATTERNS, format(EditableComboBoxItems.getInstance().getDateAndTimePatterns()));
        props.setProperty(POSITION_PATTERNS, format(EditableComboBoxItems.getInstance().getPositionPatterns()));
        props.setProperty(EPSG_CODES, format(EditableComboBoxItems.getInstance().getEPSGCodes()));
        props.setProperty(REFERENCE_SYSTEM_NAMES,
                format(EditableComboBoxItems.getInstance().getReferenceSystemNames()));
        props.setProperty(SOS_UR_LS, format(EditableComboBoxItems.getInstance().getSosURLs()));

        props.setProperty(FEATURE_OF_INTEREST_NAMES,
                format(EditableComboBoxItems.getInstance().getFeatureOfInterestNames()));
        props.setProperty(OBSERVED_PROPERTY_NAMES,
                format(EditableComboBoxItems.getInstance().getObservedPropertyNames()));
        props.setProperty(UNIT_OF_MEASUREMENT_CODES,
                format(EditableComboBoxItems.getInstance().getUnitOfMeasurementCodes()));
        props.setProperty(SENSOR_NAMES, format(EditableComboBoxItems.getInstance().getSensorNames()));
        props.setProperty(FEATURE_OF_INTEREST_URIS,
                format(EditableComboBoxItems.getInstance().getFeatureOfInterestURIs()));
        props.setProperty(OBSERVED_PROPERTY_URIS,
                format(EditableComboBoxItems.getInstance().getObservedPropertyURIs()));
        props.setProperty(UNIT_OF_MEASUREMENT_URIS,
                format(EditableComboBoxItems.getInstance().getUnitOfMeasurementURIs()));
        props.setProperty(SENSOR_URIS, format(EditableComboBoxItems.getInstance().getSensorURIs()));

        final File folder = new File(EXTERNAL_FILE_PATH);
        if (!folder.exists()) {

            final boolean successful = folder.mkdir();
            if (!successful) {
                logger.warn(SOS_PROPERTIES_COULD_NOT_BE_SAVED);
                logger.warn("No writing permissions at " + folder);
                return;
            }
        }

        final File file = new File(EXTERNAL_FILE_PATH + FILE_NAME);
        logger.info("Save settings at " + file.getAbsolutePath());

        //save properties
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            props.store(os, null);
        } catch (final IOException e) {
            logger.error(SOS_PROPERTIES_COULD_NOT_BE_SAVED, e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    log(e);
                }
            }
        }
    }

    /**
     * <p>format.</p>
     *
     * @param dcbm a {@link javax.swing.DefaultComboBoxModel} object.
     * @return a {@link java.lang.String} object.
     */
    public String format(final DefaultComboBoxModel<String> dcbm) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dcbm.getSize(); i++) {
            sb.append(dcbm.getElementAt(i) + Constants.SEPARATOR_STRING);
        }
        return sb.toString();
    }

    /**
     * <p>Getter for the field <code>featureOfInterestNames</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getFeatureOfInterestNames() {
        return featureOfInterestNames.clone();
    }

    /**
     * <p>Setter for the field <code>featureOfInterestNames</code>.</p>
     *
     * @param featureOfInterestNames an array of {@link java.lang.String} objects.
     */
    public void setFeatureOfInterestNames(final String[] featureOfInterestNames) {
        this.featureOfInterestNames = featureOfInterestNames.clone();
    }

    /**
     * <p>Getter for the field <code>observedPropertyNames</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getObservedPropertyNames() {
        return observedPropertyNames.clone();
    }

    /**
     * <p>Setter for the field <code>observedPropertyNames</code>.</p>
     *
     * @param observedPropertyNames an array of {@link java.lang.String} objects.
     */
    public void setObservedPropertyNames(final String[] observedPropertyNames) {
        this.observedPropertyNames = observedPropertyNames.clone();
    }

    /**
     * <p>Getter for the field <code>unitOfMeasurementCodes</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getUnitOfMeasurementCodes() {
        return unitOfMeasurementCodes.clone();
    }

    /**
     * <p>Setter for the field <code>unitOfMeasurementCodes</code>.</p>
     *
     * @param unitOfMeasurementCodes an array of {@link java.lang.String} objects.
     */
    public void setUnitOfMeasurementCodes(final String[] unitOfMeasurementCodes) {
        this.unitOfMeasurementCodes = unitOfMeasurementCodes.clone();
    }

    /**
     * <p>Getter for the field <code>sensorNames</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getSensorNames() {
        return sensorNames.clone();
    }

    /**
     * <p>Setter for the field <code>sensorNames</code>.</p>
     *
     * @param sensorNames an array of {@link java.lang.String} objects.
     */
    public void setSensorNames(final String[] sensorNames) {
        this.sensorNames = sensorNames.clone();
    }

    /**
     * <p>Getter for the field <code>featureOfInterestURIs</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getFeatureOfInterestURIs() {
        return featureOfInterestURIs.clone();
    }

    /**
     * <p>Setter for the field <code>featureOfInterestURIs</code>.</p>
     *
     * @param featureOfInterestURIs an array of {@link java.lang.String} objects.
     */
    public void setFeatureOfInterestURIs(final String[] featureOfInterestURIs) {
        this.featureOfInterestURIs = featureOfInterestURIs.clone();
    }

    /**
     * <p>Getter for the field <code>observedPropertyURIs</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getObservedPropertyURIs() {
        return observedPropertyURIs.clone();
    }

    /**
     * <p>Setter for the field <code>observedPropertyURIs</code>.</p>
     *
     * @param observedPropertyURIs an array of {@link java.lang.String} objects.
     */
    public void setObservedPropertyURIs(final String[] observedPropertyURIs) {
        this.observedPropertyURIs = observedPropertyURIs.clone();
    }

    /**
     * <p>Getter for the field <code>unitOfMeasurementURIs</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getUnitOfMeasurementURIs() {
        return unitOfMeasurementURIs.clone();
    }

    /**
     * <p>Setter for the field <code>unitOfMeasurementURIs</code>.</p>
     *
     * @param unitOfMeasurementURIs an array of {@link java.lang.String} objects.
     */
    public void setUnitOfMeasurementURIs(final String[] unitOfMeasurementURIs) {
        this.unitOfMeasurementURIs = unitOfMeasurementURIs.clone();
    }

    /**
     * <p>Getter for the field <code>sensorURIs</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getSensorURIs() {
        return sensorURIs.clone();
    }

    /**
     * <p>Setter for the field <code>sensorURIs</code>.</p>
     *
     * @param sensorURIs an array of {@link java.lang.String} objects.
     */
    public void setSensorURIs(final String[] sensorURIs) {
        this.sensorURIs = sensorURIs.clone();
    }

    /**
     * <p>Getter for the field <code>decimalSeparators</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getDecimalSeparators() {
        return decimalSeparators.clone();
    }

    /**
     * <p>Setter for the field <code>decimalSeparators</code>.</p>
     *
     * @param decimalSeparators an array of {@link java.lang.String} objects.
     */
    public void setDecimalSeparators(final String[] decimalSeparators) {
        this.decimalSeparators = decimalSeparators.clone();
    }

    /**
     * <p>Getter for the field <code>latLonUnits</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getLatLonUnits() {
        return latLonUnits.clone();
    }

    /**
     * <p>Setter for the field <code>latLonUnits</code>.</p>
     *
     * @param latLonUnits an array of {@link java.lang.String} objects.
     */
    public void setLatLonUnits(final String[] latLonUnits) {
        this.latLonUnits = latLonUnits.clone();
    }

    /**
     * <p>Getter for the field <code>heightUnits</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getHeightUnits() {
        return heightUnits.clone();
    }

    /**
     * <p>Setter for the field <code>heightUnits</code>.</p>
     *
     * @param heightUnits an array of {@link java.lang.String} objects.
     */
    public void setHeightUnits(final String[] heightUnits) {
        this.heightUnits = heightUnits.clone();
    }

    /**
     * <p>Getter for the field <code>columnSeparators</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getColumnSeparators() {
        return columnSeparators.clone();
    }

    /**
     * <p>Setter for the field <code>columnSeparators</code>.</p>
     *
     * @param columnSeparators an array of {@link java.lang.String} objects.
     */
    public void setColumnSeparators(final String[] columnSeparators) {
        this.columnSeparators = columnSeparators.clone();
    }

    /**
     * <p>Getter for the field <code>commentIndicators</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getCommentIndicators() {
        return commentIndicators.clone();
    }

    /**
     * <p>Setter for the field <code>commentIndicators</code>.</p>
     *
     * @param commentIndicators an array of {@link java.lang.String} objects.
     */
    public void setCommentIndicators(final String[] commentIndicators) {
        this.commentIndicators = commentIndicators.clone();
    }

    /**
     * <p>Getter for the field <code>textQualifiers</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getTextQualifiers() {
        return textQualifiers.clone();
    }

    /**
     * <p>Setter for the field <code>textQualifiers</code>.</p>
     *
     * @param textQualifiers an array of {@link java.lang.String} objects.
     */
    public void setTextQualifiers(final String[] textQualifiers) {
        this.textQualifiers = textQualifiers.clone();
    }

    /**
     * <p>Getter for the field <code>dateAndTimePatterns</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getDateAndTimePatterns() {
        return dateAndTimePatterns.clone();
    }

    /**
     * <p>Setter for the field <code>dateAndTimePatterns</code>.</p>
     *
     * @param dateAndTimePatterns an array of {@link java.lang.String} objects.
     */
    public void setDateAndTimePatterns(final String[] dateAndTimePatterns) {
        this.dateAndTimePatterns = dateAndTimePatterns.clone();
    }

    /**
     * <p>Getter for the field <code>epsgCodes</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getEpsgCodes() {
        return epsgCodes.clone();
    }

    /**
     * <p>Setter for the field <code>epsgCodes</code>.</p>
     *
     * @param epsgCodes an array of {@link java.lang.String} objects.
     */
    public void setEpsgCodes(final String[] epsgCodes) {
        this.epsgCodes = epsgCodes.clone();
    }

    /**
     * <p>Getter for the field <code>sosURLs</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getSosURLs() {
        return sosURLs.clone();
    }

    /**
     * <p>Setter for the field <code>sosURLs</code>.</p>
     *
     * @param sosURLs an array of {@link java.lang.String} objects.
     */
    public void setSosURLs(final String[] sosURLs) {
        this.sosURLs = sosURLs.clone();
    }

    /**
     * <p>Getter for the field <code>dateAndTimeGroups</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getDateAndTimeGroups() {
        return dateAndTimeGroups.clone();
    }

    /**
     * <p>Getter for the field <code>positionGroups</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getPositionGroups() {
        return positionGroups.clone();
    }

    /**
     * <p>Setter for the field <code>positionPatterns</code>.</p>
     *
     * @param positionPatterns an array of {@link java.lang.String} objects.
     */
    public void setPositionPatterns(final String[] positionPatterns) {
        this.positionPatterns = positionPatterns.clone();
    }

    /**
     * <p>Getter for the field <code>positionPatterns</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getPositionPatterns() {
        return positionPatterns.clone();
    }

    /**
     * <p>Setter for the field <code>referenceSystemNames</code>.</p>
     *
     * @param referenceSystemNames an array of {@link java.lang.String} objects.
     */
    public void setReferenceSystemNames(final String[] referenceSystemNames) {
        this.referenceSystemNames = referenceSystemNames.clone();
    }

    /**
     * <p>Getter for the field <code>referenceSystemNames</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getReferenceSystemNames() {
        return referenceSystemNames.clone();
    }
}
