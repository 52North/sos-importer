package org.n52.sos.importer.combobox;

import javax.swing.DefaultComboBoxModel;

/**
 * contains all models of comboboxes which can be edited by the user 
 * @author Raimund
 *
 */
public class EditableComboBoxItems {
	
	private static EditableComboBoxItems instance = null;

	private DefaultComboBoxModel columnSeparators;
	
	private DefaultComboBoxModel commentIndicators;
	
	private DefaultComboBoxModel textQualifiers;
	
	private DefaultComboBoxModel dateAndTimePatterns;
	
	private DefaultComboBoxModel positionPatterns;
	
	private DefaultComboBoxModel EPSGCodes;
	
	private DefaultComboBoxModel referenceSystemNames;
	
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
				ComboBoxItems.getInstance().getColumnSeparators()));
		setCommentIndicators(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getCommentIndicators()));
		setTextQualifiers(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getTextQualifiers()));
		setDateAndTimePatterns(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getDateAndTimePatterns()));	
		setPositionPatterns(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getPositionPatterns()));
		setEPSGCodes(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getEpsgCodes()));
		setReferenceSystemNames(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getReferenceSystemNames()));
		setSosURLs(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getSosURLs()));
		
		setFeatureOfInterestNames(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getFeatureOfInterestNames()));
		setObservedPropertyNames(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getObservedPropertyNames()));
		setUnitOfMeasurementCodes(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getUnitOfMeasurementCodes()));
		setSensorNames(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getSensorNames()));	
		setFeatureOfInterestURIs(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getFeatureOfInterestURIs()));
		setObservedPropertyURIs(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getObservedPropertyURIs()));
		setUnitOfMeasurementURIs(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getUnitOfMeasurementURIs()));
		setSensorURIs(new DefaultComboBoxModel(
				ComboBoxItems.getInstance().getSensorURIs()));
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

	public void setPositionPatterns(DefaultComboBoxModel positionPatterns) {
		this.positionPatterns = positionPatterns;
	}

	public DefaultComboBoxModel getPositionPatterns() {
		return positionPatterns;
	}

	public void setReferenceSystemNames(DefaultComboBoxModel referenceSystemNames) {
		this.referenceSystemNames = referenceSystemNames;
	}

	public DefaultComboBoxModel getReferenceSystemNames() {
		return referenceSystemNames;
	}
}
