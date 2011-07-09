package org.n52.sos.importer.config;

import javax.swing.DefaultComboBoxModel;

public class EditableComboBoxItems {
	
	private static EditableComboBoxItems instance = null;

	private DefaultComboBoxModel columnSeparators;
	
	private DefaultComboBoxModel commentIndicators;
	
	private DefaultComboBoxModel textQualifiers;
	
	private DefaultComboBoxModel dateAndTimePatterns;
	
	private DefaultComboBoxModel EPSGCodes;
	
	private DefaultComboBoxModel featureOfInterestNames;
	
	private DefaultComboBoxModel observedPropertyNames;
	
	private DefaultComboBoxModel unitOfMeasurementCodes;
	
	private DefaultComboBoxModel sensorNames;
	
	private DefaultComboBoxModel featureOfInterestURIs;
	
	private DefaultComboBoxModel observedPropertyURIs;
	
	private DefaultComboBoxModel unitOfMeasurementURIs;
	
	private DefaultComboBoxModel sensorURIs;

	private DefaultComboBoxModel sosURLs;
	
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
		setSosURLs(new DefaultComboBoxModel(
				Settings.getInstance().getSosURLs()));
		
		setFeatureOfInterestNames(new DefaultComboBoxModel(
				Settings.getInstance().getFeatureOfInterestNames()));
		setObservedPropertyNames(new DefaultComboBoxModel(
				Settings.getInstance().getObservedPropertyNames()));
		setUnitOfMeasurementCodes(new DefaultComboBoxModel(
				Settings.getInstance().getUnitOfMeasurementCodes()));
		setSensorNames(new DefaultComboBoxModel(
				Settings.getInstance().getSensorNames()));	
		setFeatureOfInterestURIs(new DefaultComboBoxModel(
				Settings.getInstance().getFeatureOfInterestURIs()));
		setObservedPropertyURIs(new DefaultComboBoxModel(
				Settings.getInstance().getObservedPropertyURIs()));
		setUnitOfMeasurementURIs(new DefaultComboBoxModel(
				Settings.getInstance().getUnitOfMeasurementURIs()));
		setSensorURIs(new DefaultComboBoxModel(
				Settings.getInstance().getSensorURIs()));
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

	public void setSosURLs(DefaultComboBoxModel sosURLs) {
		this.sosURLs = sosURLs;
	}

	public DefaultComboBoxModel getSosURLs() {
		return sosURLs;
	}
	
	public DefaultComboBoxModel getFeatureOfInterestNames() {
		return featureOfInterestNames;
	}

	public void setFeatureOfInterestNames(
			DefaultComboBoxModel featureOfInterestNames) {
		this.featureOfInterestNames = featureOfInterestNames;
	}

	public DefaultComboBoxModel getObservedPropertyNames() {
		return observedPropertyNames;
	}

	public void setObservedPropertyNames(DefaultComboBoxModel observedPropertyNames) {
		this.observedPropertyNames = observedPropertyNames;
	}

	public DefaultComboBoxModel getUnitOfMeasurementCodes() {
		return unitOfMeasurementCodes;
	}

	public void setUnitOfMeasurementCodes(
			DefaultComboBoxModel unitOfMeasurementCodes) {
		this.unitOfMeasurementCodes = unitOfMeasurementCodes;
	}

	public DefaultComboBoxModel getSensorNames() {
		return sensorNames;
	}

	public void setSensorNames(DefaultComboBoxModel sensorNames) {
		this.sensorNames = sensorNames;
	}

	public DefaultComboBoxModel getFeatureOfInterestURIs() {
		return featureOfInterestURIs;
	}

	public void setFeatureOfInterestURIs(DefaultComboBoxModel featureOfInterestURIs) {
		this.featureOfInterestURIs = featureOfInterestURIs;
	}

	public DefaultComboBoxModel getObservedPropertyURIs() {
		return observedPropertyURIs;
	}

	public void setObservedPropertyURIs(DefaultComboBoxModel observedPropertyURIs) {
		this.observedPropertyURIs = observedPropertyURIs;
	}

	public DefaultComboBoxModel getUnitOfMeasurementURIs() {
		return unitOfMeasurementURIs;
	}

	public void setUnitOfMeasurementURIs(DefaultComboBoxModel unitOfMeasurementURIs) {
		this.unitOfMeasurementURIs = unitOfMeasurementURIs;
	}

	public DefaultComboBoxModel getSensorURIs() {
		return sensorURIs;
	}

	public void setSensorURIs(DefaultComboBoxModel sensorURIs) {
		this.sensorURIs = sensorURIs;
	}
	
}
