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
public class ComboBoxItems {

	private static final Logger logger = LoggerFactory.getLogger(ComboBoxItems.class);

	private static ComboBoxItems instance = null;

	private static final String EXTERNAL_FILE_PATH = System.getProperty("user.home") + File.separator + ".SOSImporter" + File.separator;

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
		load();
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
	 * <p>load.</p>
	 */
	public void load() {
		try {
			InputStream is;
			String filePath = EXTERNAL_FILE_PATH + FILE_NAME;
			final File file = new File(filePath);
			if (!file.exists()) {
				logger.info("Load default settings from jar file");
				filePath = INTERNAL_FILE_PATH + FILE_NAME;
				is = getClass().getResourceAsStream(filePath);
			} else if (!file.canRead()) {
				logger.warn("Could not load settings.");
				logger.warn("No reading permissions for " + file);
				logger.info("Load default settings from jar file");
				filePath = INTERNAL_FILE_PATH + FILE_NAME;
				is = getClass().getResourceAsStream(filePath);
			} else {
				logger.info("Load settings from " + file);
				is = new FileInputStream(file);
			}

			props.load(is);
		} catch (final FileNotFoundException e) {
			logger.error("SOS Importer Settings not found", e);
			System.exit(1);
		} catch (final IOException e) {
			logger.error("SOS Importer Settings not readable.", e);
			System.exit(1);
		}

		decimalSeparators = parse(props.getProperty("decimalSeparators"));
		latLonUnits = parse(props.getProperty("latLonUnits"));
		heightUnits = parse(props.getProperty("heightUnits"));
		dateAndTimeGroups = parse(props.getProperty("dateAndTimeGroups"));
		positionGroups = parse(props.getProperty("positionGroups"));

		columnSeparators = parse(props.getProperty("columnSeparators"));
		commentIndicators = parse(props.getProperty("commentIndicators"));
		textQualifiers = parse(props.getProperty("textQualifiers"));
		dateAndTimePatterns = parse(props.getProperty("dateAndTimePatterns"));
		positionPatterns = parse(props.getProperty("positionPatterns"));
		epsgCodes = parse(props.getProperty("epsgCodes"));
		referenceSystemNames = parse(props.getProperty("referenceSystemNames"));
		sosURLs = parse(props.getProperty("sosURLs"));

		featureOfInterestNames = parse(props.getProperty("featureOfInterestNames"));
		observedPropertyNames = parse(props.getProperty("observedPropertyNames"));
		unitOfMeasurementCodes = parse(props.getProperty("unitOfMeasurementCodes"));
		sensorNames = parse(props.getProperty("sensorNames"));
		featureOfInterestURIs = parse(props.getProperty("featureOfInterestURIs"));
		observedPropertyURIs = parse(props.getProperty("observedPropertyURIs"));
		unitOfMeasurementURIs = parse(props.getProperty("unitOfMeasurementURIs"));
		sensorURIs = parse(props.getProperty("sensorURIs"));
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
		props.setProperty("columnSeparators", format(EditableComboBoxItems.getInstance().getColumnSeparators()));
		props.setProperty("commentIndicators", format(EditableComboBoxItems.getInstance().getCommentIndicators()));
		props.setProperty("textQualifiers", format(EditableComboBoxItems.getInstance().getTextQualifiers()));
		props.setProperty("dateAndTimePatterns", format(EditableComboBoxItems.getInstance().getDateAndTimePatterns()));
		props.setProperty("positionPatterns", format(EditableComboBoxItems.getInstance().getPositionPatterns()));
		props.setProperty("epsgCodes", format(EditableComboBoxItems.getInstance().getEPSGCodes()));
		props.setProperty("referenceSystemNames", format(EditableComboBoxItems.getInstance().getReferenceSystemNames()));
		props.setProperty("sosURLs", format(EditableComboBoxItems.getInstance().getSosURLs()));

		props.setProperty("featureOfInterestNames", format(EditableComboBoxItems.getInstance().getFeatureOfInterestNames()));
		props.setProperty("observedPropertyNames", format(EditableComboBoxItems.getInstance().getObservedPropertyNames()));
		props.setProperty("unitOfMeasurementCodes", format(EditableComboBoxItems.getInstance().getUnitOfMeasurementCodes()));
		props.setProperty("sensorNames", format(EditableComboBoxItems.getInstance().getSensorNames()));
		props.setProperty("featureOfInterestURIs", format(EditableComboBoxItems.getInstance().getFeatureOfInterestURIs()));
		props.setProperty("observedPropertyURIs", format(EditableComboBoxItems.getInstance().getObservedPropertyURIs()));
		props.setProperty("unitOfMeasurementURIs", format(EditableComboBoxItems.getInstance().getUnitOfMeasurementURIs()));
		props.setProperty("sensorURIs", format(EditableComboBoxItems.getInstance().getSensorURIs()));

		final File folder = new File(EXTERNAL_FILE_PATH);
		if (!folder.exists()) {

			final boolean successful = folder.mkdir();
			if (!successful) {
				logger.warn("SOS properties could not be saved.");
				logger.warn("No writing permissions at " + folder);
				return;
			}
		}

		final File file = new File(EXTERNAL_FILE_PATH + FILE_NAME);
		logger.info("Save settings at " + file.getAbsolutePath());

		try { //save properties
			final OutputStream os = new FileOutputStream(file);
			props.store(os, null);
		} catch (final IOException e) {
			logger.error("SOS properties could not be saved.", e);
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
		return featureOfInterestNames;
	}

	/**
	 * <p>Setter for the field <code>featureOfInterestNames</code>.</p>
	 *
	 * @param featureOfInterestNames an array of {@link java.lang.String} objects.
	 */
	public void setFeatureOfInterestNames(final String[] featureOfInterestNames) {
		this.featureOfInterestNames = featureOfInterestNames;
	}

	/**
	 * <p>Getter for the field <code>observedPropertyNames</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getObservedPropertyNames() {
		return observedPropertyNames;
	}

	/**
	 * <p>Setter for the field <code>observedPropertyNames</code>.</p>
	 *
	 * @param observedPropertyNames an array of {@link java.lang.String} objects.
	 */
	public void setObservedPropertyNames(final String[] observedPropertyNames) {
		this.observedPropertyNames = observedPropertyNames;
	}

	/**
	 * <p>Getter for the field <code>unitOfMeasurementCodes</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getUnitOfMeasurementCodes() {
		return unitOfMeasurementCodes;
	}

	/**
	 * <p>Setter for the field <code>unitOfMeasurementCodes</code>.</p>
	 *
	 * @param unitOfMeasurementCodes an array of {@link java.lang.String} objects.
	 */
	public void setUnitOfMeasurementCodes(final String[] unitOfMeasurementCodes) {
		this.unitOfMeasurementCodes = unitOfMeasurementCodes;
	}

	/**
	 * <p>Getter for the field <code>sensorNames</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getSensorNames() {
		return sensorNames;
	}

	/**
	 * <p>Setter for the field <code>sensorNames</code>.</p>
	 *
	 * @param sensorNames an array of {@link java.lang.String} objects.
	 */
	public void setSensorNames(final String[] sensorNames) {
		this.sensorNames = sensorNames;
	}

	/**
	 * <p>Getter for the field <code>featureOfInterestURIs</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getFeatureOfInterestURIs() {
		return featureOfInterestURIs;
	}

	/**
	 * <p>Setter for the field <code>featureOfInterestURIs</code>.</p>
	 *
	 * @param featureOfInterestURIs an array of {@link java.lang.String} objects.
	 */
	public void setFeatureOfInterestURIs(final String[] featureOfInterestURIs) {
		this.featureOfInterestURIs = featureOfInterestURIs;
	}

	/**
	 * <p>Getter for the field <code>observedPropertyURIs</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getObservedPropertyURIs() {
		return observedPropertyURIs;
	}

	/**
	 * <p>Setter for the field <code>observedPropertyURIs</code>.</p>
	 *
	 * @param observedPropertyURIs an array of {@link java.lang.String} objects.
	 */
	public void setObservedPropertyURIs(final String[] observedPropertyURIs) {
		this.observedPropertyURIs = observedPropertyURIs;
	}

	/**
	 * <p>Getter for the field <code>unitOfMeasurementURIs</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getUnitOfMeasurementURIs() {
		return unitOfMeasurementURIs;
	}

	/**
	 * <p>Setter for the field <code>unitOfMeasurementURIs</code>.</p>
	 *
	 * @param unitOfMeasurementURIs an array of {@link java.lang.String} objects.
	 */
	public void setUnitOfMeasurementURIs(final String[] unitOfMeasurementURIs) {
		this.unitOfMeasurementURIs = unitOfMeasurementURIs;
	}

	/**
	 * <p>Getter for the field <code>sensorURIs</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getSensorURIs() {
		return sensorURIs;
	}

	/**
	 * <p>Setter for the field <code>sensorURIs</code>.</p>
	 *
	 * @param sensorURIs an array of {@link java.lang.String} objects.
	 */
	public void setSensorURIs(final String[] sensorURIs) {
		this.sensorURIs = sensorURIs;
	}

	/**
	 * <p>Getter for the field <code>decimalSeparators</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getDecimalSeparators() {
		return decimalSeparators;
	}

	/**
	 * <p>Setter for the field <code>decimalSeparators</code>.</p>
	 *
	 * @param decimalSeparators an array of {@link java.lang.String} objects.
	 */
	public void setDecimalSeparators(final String[] decimalSeparators) {
		this.decimalSeparators = decimalSeparators;
	}

	/**
	 * <p>Getter for the field <code>latLonUnits</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getLatLonUnits() {
		return latLonUnits;
	}

	/**
	 * <p>Setter for the field <code>latLonUnits</code>.</p>
	 *
	 * @param latLonUnits an array of {@link java.lang.String} objects.
	 */
	public void setLatLonUnits(final String[] latLonUnits) {
		this.latLonUnits = latLonUnits;
	}

	/**
	 * <p>Getter for the field <code>heightUnits</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getHeightUnits() {
		return heightUnits;
	}

	/**
	 * <p>Setter for the field <code>heightUnits</code>.</p>
	 *
	 * @param heightUnits an array of {@link java.lang.String} objects.
	 */
	public void setHeightUnits(final String[] heightUnits) {
		this.heightUnits = heightUnits;
	}

	/**
	 * <p>Getter for the field <code>columnSeparators</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getColumnSeparators() {
		return columnSeparators;
	}

	/**
	 * <p>Setter for the field <code>columnSeparators</code>.</p>
	 *
	 * @param columnSeparators an array of {@link java.lang.String} objects.
	 */
	public void setColumnSeparators(final String[] columnSeparators) {
		this.columnSeparators = columnSeparators;
	}

	/**
	 * <p>Getter for the field <code>commentIndicators</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getCommentIndicators() {
		return commentIndicators;
	}

	/**
	 * <p>Setter for the field <code>commentIndicators</code>.</p>
	 *
	 * @param commentIndicators an array of {@link java.lang.String} objects.
	 */
	public void setCommentIndicators(final String[] commentIndicators) {
		this.commentIndicators = commentIndicators;
	}

	/**
	 * <p>Getter for the field <code>textQualifiers</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getTextQualifiers() {
		return textQualifiers;
	}

	/**
	 * <p>Setter for the field <code>textQualifiers</code>.</p>
	 *
	 * @param textQualifiers an array of {@link java.lang.String} objects.
	 */
	public void setTextQualifiers(final String[] textQualifiers) {
		this.textQualifiers = textQualifiers;
	}

	/**
	 * <p>Getter for the field <code>dateAndTimePatterns</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getDateAndTimePatterns() {
		return dateAndTimePatterns;
	}

	/**
	 * <p>Setter for the field <code>dateAndTimePatterns</code>.</p>
	 *
	 * @param dateAndTimePatterns an array of {@link java.lang.String} objects.
	 */
	public void setDateAndTimePatterns(final String[] dateAndTimePatterns) {
		this.dateAndTimePatterns = dateAndTimePatterns;
	}

	/**
	 * <p>Getter for the field <code>epsgCodes</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getEpsgCodes() {
		return epsgCodes;
	}

	/**
	 * <p>Setter for the field <code>epsgCodes</code>.</p>
	 *
	 * @param epsgCodes an array of {@link java.lang.String} objects.
	 */
	public void setEpsgCodes(final String[] epsgCodes) {
		this.epsgCodes = epsgCodes;
	}

	/**
	 * <p>Getter for the field <code>sosURLs</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getSosURLs() {
		return sosURLs;
	}

	/**
	 * <p>Setter for the field <code>sosURLs</code>.</p>
	 *
	 * @param sosURLs an array of {@link java.lang.String} objects.
	 */
	public void setSosURLs(final String[] sosURLs) {
		this.sosURLs = sosURLs;
	}

	/**
	 * <p>Getter for the field <code>dateAndTimeGroups</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getDateAndTimeGroups() {
		return dateAndTimeGroups;
	}

	/**
	 * <p>Getter for the field <code>positionGroups</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getPositionGroups() {
		return positionGroups;
	}

	/**
	 * <p>Setter for the field <code>positionPatterns</code>.</p>
	 *
	 * @param positionPatterns an array of {@link java.lang.String} objects.
	 */
	public void setPositionPatterns(final String[] positionPatterns) {
		this.positionPatterns = positionPatterns;
	}

	/**
	 * <p>Getter for the field <code>positionPatterns</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getPositionPatterns() {
		return positionPatterns;
	}

	/**
	 * <p>Setter for the field <code>referenceSystemNames</code>.</p>
	 *
	 * @param referenceSystemNames an array of {@link java.lang.String} objects.
	 */
	public void setReferenceSystemNames(final String[] referenceSystemNames) {
		this.referenceSystemNames = referenceSystemNames;
	}

	/**
	 * <p>Getter for the field <code>referenceSystemNames</code>.</p>
	 *
	 * @return an array of {@link java.lang.String} objects.
	 */
	public String[] getReferenceSystemNames() {
		return referenceSystemNames;
	}
}
