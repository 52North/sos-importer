package org.n52.sos.importer.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;

import org.apache.log4j.Logger;

public class Settings {

	private static final Logger logger = Logger.getLogger(Settings.class);

	private static Settings instance = null;
	
	private static final String SEPARATOR = "SEP";
	
	private static final String FILE_PATH = "/org/n52/sos/importer/config/SOSImporter.properties";
	
	private final Properties props = new Properties();
	
	private String[] decimalSeparators;
	
	private String[] thousandsSeparators;
	
	private String[] latLonUnits;
	
	private String[] heightUnits;
	
	private String[] columnSeparators;
	
	private String[] commentIndicators;
	
	private String[] textQualifiers;
	
	private String[] dateAndTimePatterns;
	
	private String[] epsgCodes;
	
	private String[] sosURLs;

	private Settings() {
		load();
	}
	
	public static Settings getInstance() {
		if (instance == null) instance = new Settings();
		return instance;
	}
	
	public void load() {
        InputStream is;
		try {
			is = this.getClass().getResourceAsStream(FILE_PATH);
			props.load(is);     
		} catch (FileNotFoundException e) {
			logger.error("SOS Importer Settings not found", e);
			System.exit(1);
		} catch (IOException e) {
			logger.error("SOS Importer Settings not readable.", e);
			System.exit(1);
		}

		this.decimalSeparators = parse(props.getProperty("decimalSeparators"));	
		this.thousandsSeparators = parse(props.getProperty("thousandsSeparators"));	
		this.latLonUnits = parse(props.getProperty("latLonUnits"));	
		this.heightUnits = parse(props.getProperty("heightUnits"));	
		this.thousandsSeparators = parse(props.getProperty("thousandsSeparators"));	
		
		this.columnSeparators = parse(props.getProperty("columnSeparators"));	
		this.commentIndicators = parse(props.getProperty("commentIndicators"));	
		this.textQualifiers = parse(props.getProperty("textQualifiers"));	
		this.dateAndTimePatterns = parse(props.getProperty("dateAndTimePatterns"));	
		this.epsgCodes = parse(props.getProperty("epsgCodes"));	
		this.sosURLs = parse(props.getProperty("sosURLs"));	
	}
	
	public String[] parse(String property) {
		String[] values = property.split(SEPARATOR);
		for (int i = 0; i < values.length; i++) 
			values[i] = values[i].trim();
		return values;
	}
	
	public void save() {
		props.setProperty("columnSeparators", format(EditableComboBoxItems.getInstance().getColumnSeparators()));
		props.setProperty("commentIndicators", format(EditableComboBoxItems.getInstance().getCommentIndicators()));
		props.setProperty("textQualifiers", format(EditableComboBoxItems.getInstance().getTextQualifiers()));
		props.setProperty("dateAndTimePatterns", format(EditableComboBoxItems.getInstance().getDateAndTimePatterns()));
		props.setProperty("epsgCodes", format(EditableComboBoxItems.getInstance().getEPSGCodes()));
		props.setProperty("sosURLs", format(EditableComboBoxItems.getInstance().getSosURLs()));
		
		URL url = this.getClass().getResource(FILE_PATH);
		File file;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			file = new File(url.getPath());
		}
		System.out.println(file.getAbsolutePath());
		try { //save properties
			OutputStream os = new FileOutputStream(file);
			props.store(os, null);  
		} catch (IOException e) {
			logger.error("SOS properties could not be saved.", e);
		}
	}
	
	public String format(DefaultComboBoxModel dcbm) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < dcbm.getSize(); i++) {
			sb.append(dcbm.getElementAt(i) + " " + SEPARATOR);
		}
		return sb.toString();
	}
	
	public String[] getDecimalSeparators() {
		return decimalSeparators;
	}

	public void setDecimalSeparators(String[] decimalSeparators) {
		this.decimalSeparators = decimalSeparators;
	}

	public String[] getThousandsSeparators() {
		return thousandsSeparators;
	}

	public void setThousandsSeparators(String[] thousandsSeparators) {
		this.thousandsSeparators = thousandsSeparators;
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
}
