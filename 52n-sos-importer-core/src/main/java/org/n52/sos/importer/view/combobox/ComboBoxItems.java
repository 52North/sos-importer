/**
 * Copyright (C) 2012
 * by 52North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
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

import org.apache.log4j.Logger;
import org.n52.sos.importer.Constants;

/**
 * loads and saves all combobox items in the properties file
 * @author Raimund
 *
 */
public class ComboBoxItems {

	private static final Logger logger = Logger.getLogger(ComboBoxItems.class);

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
	
	public static ComboBoxItems getInstance() {
		if (instance == null) instance = new ComboBoxItems();
		return instance;
	}
	
	public void load() {
		try {
			InputStream is;
			String filePath = EXTERNAL_FILE_PATH + FILE_NAME;
			File file = new File(filePath);
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
		} catch (FileNotFoundException e) {
			logger.error("SOS Importer Settings not found", e);
			System.exit(1);
		} catch (IOException e) {
			logger.error("SOS Importer Settings not readable.", e);
			System.exit(1);
		}

		this.decimalSeparators = parse(props.getProperty("decimalSeparators"));	
		this.latLonUnits = parse(props.getProperty("latLonUnits"));	
		this.heightUnits = parse(props.getProperty("heightUnits"));	
		this.dateAndTimeGroups = parse(props.getProperty("dateAndTimeGroups"));
		this.positionGroups = parse(props.getProperty("positionGroups"));
		
		this.columnSeparators = parse(props.getProperty("columnSeparators"));	
		this.commentIndicators = parse(props.getProperty("commentIndicators"));	
		this.textQualifiers = parse(props.getProperty("textQualifiers"));	
		this.dateAndTimePatterns = parse(props.getProperty("dateAndTimePatterns"));
		this.positionPatterns = parse(props.getProperty("positionPatterns"));
		this.epsgCodes = parse(props.getProperty("epsgCodes"));	
		this.referenceSystemNames = parse(props.getProperty("referenceSystemNames"));
		this.sosURLs = parse(props.getProperty("sosURLs"));	
		
		this.featureOfInterestNames = parse(props.getProperty("featureOfInterestNames"));	
		this.observedPropertyNames = parse(props.getProperty("observedPropertyNames"));	
		this.unitOfMeasurementCodes = parse(props.getProperty("unitOfMeasurementCodes"));	
		this.sensorNames = parse(props.getProperty("sensorNames"));	
		this.featureOfInterestURIs = parse(props.getProperty("featureOfInterestURIs"));	
		this.observedPropertyURIs = parse(props.getProperty("observedPropertyURIs"));	
		this.unitOfMeasurementURIs = parse(props.getProperty("unitOfMeasurementURIs"));	
		this.sensorURIs = parse(props.getProperty("sensorURIs"));	
	}
	
	public String[] parse(String property) {
		if (property == null) return new String[0];
		String[] values = property.split(Constants.SEPARATOR_STRING);
		return values;
	}
	
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
		
		File folder = new File(EXTERNAL_FILE_PATH);
		if (!folder.exists()) {
			
			boolean successful = folder.mkdir();	
			if (!successful) {
				logger.warn("SOS properties could not be saved.");
				logger.warn("No writing permissions at " + folder);
				return;
			} 
		}
		
		File file = new File(EXTERNAL_FILE_PATH + FILE_NAME);
		logger.info("Save settings at " + file.getAbsolutePath());	
		
		try { //save properties
			OutputStream os = new FileOutputStream(file);
			props.store(os, null);  
		} catch (IOException e) {
			logger.error("SOS properties could not be saved.", e);
		}
	}
	
	public String format(DefaultComboBoxModel dcbm) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dcbm.getSize(); i++) 
			sb.append(dcbm.getElementAt(i) + Constants.SEPARATOR_STRING);
		return sb.toString();
	}
	
	public String[] getFeatureOfInterestNames() {
		return featureOfInterestNames;
	}

	public void setFeatureOfInterestNames(String[] featureOfInterestNames) {
		this.featureOfInterestNames = featureOfInterestNames;
	}

	public String[] getObservedPropertyNames() {
		return observedPropertyNames;
	}

	public void setObservedPropertyNames(String[] observedPropertyNames) {
		this.observedPropertyNames = observedPropertyNames;
	}

	public String[] getUnitOfMeasurementCodes() {
		return unitOfMeasurementCodes;
	}

	public void setUnitOfMeasurementCodes(String[] unitOfMeasurementCodes) {
		this.unitOfMeasurementCodes = unitOfMeasurementCodes;
	}

	public String[] getSensorNames() {
		return sensorNames;
	}

	public void setSensorNames(String[] sensorNames) {
		this.sensorNames = sensorNames;
	}

	public String[] getFeatureOfInterestURIs() {
		return featureOfInterestURIs;
	}

	public void setFeatureOfInterestURIs(String[] featureOfInterestURIs) {
		this.featureOfInterestURIs = featureOfInterestURIs;
	}

	public String[] getObservedPropertyURIs() {
		return observedPropertyURIs;
	}

	public void setObservedPropertyURIs(String[] observedPropertyURIs) {
		this.observedPropertyURIs = observedPropertyURIs;
	}

	public String[] getUnitOfMeasurementURIs() {
		return unitOfMeasurementURIs;
	}

	public void setUnitOfMeasurementURIs(String[] unitOfMeasurementURIs) {
		this.unitOfMeasurementURIs = unitOfMeasurementURIs;
	}

	public String[] getSensorURIs() {
		return sensorURIs;
	}

	public void setSensorURIs(String[] sensorURIs) {
		this.sensorURIs = sensorURIs;
	}
	
	public String[] getDecimalSeparators() {
		return decimalSeparators;
	}

	public void setDecimalSeparators(String[] decimalSeparators) {
		this.decimalSeparators = decimalSeparators;
	}

	public String[] getLatLonUnits() {
		return latLonUnits;
	}

	public void setLatLonUnits(String[] latLonUnits) {
		this.latLonUnits = latLonUnits;
	}

	public String[] getHeightUnits() {
		return heightUnits;
	}

	public void setHeightUnits(String[] heightUnits) {
		this.heightUnits = heightUnits;
	}

	public String[] getColumnSeparators() {
		return columnSeparators;
	}

	public void setColumnSeparators(String[] columnSeparators) {
		this.columnSeparators = columnSeparators;
	}

	public String[] getCommentIndicators() {
		return commentIndicators;
	}

	public void setCommentIndicators(String[] commentIndicators) {
		this.commentIndicators = commentIndicators;
	}

	public String[] getTextQualifiers() {
		return textQualifiers;
	}

	public void setTextQualifiers(String[] textQualifiers) {
		this.textQualifiers = textQualifiers;
	}

	public String[] getDateAndTimePatterns() {
		return dateAndTimePatterns;
	}

	public void setDateAndTimePatterns(String[] dateAndTimePatterns) {
		this.dateAndTimePatterns = dateAndTimePatterns;
	}

	public String[] getEpsgCodes() {
		return epsgCodes;
	}

	public void setEpsgCodes(String[] epsgCodes) {
		this.epsgCodes = epsgCodes;
	}

	public String[] getSosURLs() {
		return sosURLs;
	}

	public void setSosURLs(String[] sosURLs) {
		this.sosURLs = sosURLs;
	}

	public String[] getDateAndTimeGroups() {
		return dateAndTimeGroups;
	}

	public String[] getPositionGroups() {
		return positionGroups;
	}

	public void setPositionPatterns(String[] positionPatterns) {
		this.positionPatterns = positionPatterns;
	}

	public String[] getPositionPatterns() {
		return positionPatterns;
	}

	public void setReferenceSystemNames(String[] referenceSystemNames) {
		this.referenceSystemNames = referenceSystemNames;
	}

	public String[] getReferenceSystemNames() {
		return referenceSystemNames;
	}
}
