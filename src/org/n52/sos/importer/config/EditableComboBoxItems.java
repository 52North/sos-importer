package org.n52.sos.importer.config;

import javax.swing.DefaultComboBoxModel;

public class EditableComboBoxItems {
	
	private static EditableComboBoxItems instance = null;

	private DefaultComboBoxModel columnSeparators;
	
	private DefaultComboBoxModel commentIndicators;
	
	private DefaultComboBoxModel textQualifiers;
	
	private DefaultComboBoxModel dateAndTimePatterns;
	
	private DefaultComboBoxModel EPSGCodes;
	
	private DefaultComboBoxModel sensorObservationServices;
	
	private EditableComboBoxItems() {
		setColumnSeparators(new DefaultComboBoxModel(
				Settings.getInstance().getColumnSeparators()));
		setCommentIndicators(new DefaultComboBoxModel(
				Settings.getInstance().getCommentIndicators()));
		setTextQualifiers(new DefaultComboBoxModel(
				Settings.getInstance().getTextQualifiers()));
		setDateAndTimePatterns(new DefaultComboBoxModel(
				Settings.getInstance().getDateAndTimePatterns()));	
		setEPSGCodes(new DefaultComboBoxModel(
				Settings.getInstance().getEpsgCodes()));
		setSensorObservationServices(new DefaultComboBoxModel(
				Settings.getInstance().getSosURLs()));
	}

	public static EditableComboBoxItems getInstance() {
		if (instance == null)
			instance = new EditableComboBoxItems();
		return instance;
	}
	
	public DefaultComboBoxModel getColumnSeparators() {
		return columnSeparators;
	}

	public void setColumnSeparators(DefaultComboBoxModel columnSeparators) {
		this.columnSeparators = columnSeparators;
	}

	public DefaultComboBoxModel getCommentIndicators() {
		return commentIndicators;
	}

	public void setCommentIndicators(DefaultComboBoxModel commentIndicators) {
		this.commentIndicators = commentIndicators;
	}

	public DefaultComboBoxModel getTextQualifiers() {
		return textQualifiers;
	}

	public void setTextQualifiers(DefaultComboBoxModel textQualifiers) {
		this.textQualifiers = textQualifiers;
	}

	public DefaultComboBoxModel getDateAndTimePatterns() {
		return dateAndTimePatterns;
	}

	public void setDateAndTimePatterns(DefaultComboBoxModel dateAndTimePatterns) {
		this.dateAndTimePatterns = dateAndTimePatterns;
	}

	public DefaultComboBoxModel getEPSGCodes() {
		return EPSGCodes;
	}

	public void setEPSGCodes(DefaultComboBoxModel ePSGCodes) {
		EPSGCodes = ePSGCodes;
	}

	public void setSensorObservationServices(DefaultComboBoxModel sensorObservationServices) {
		this.sensorObservationServices = sensorObservationServices;
	}

	public DefaultComboBoxModel getSosURLs() {
		return sensorObservationServices;
	}
	
}
