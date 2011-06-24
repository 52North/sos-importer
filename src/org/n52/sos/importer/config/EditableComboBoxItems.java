package org.n52.sos.importer.config;

import javax.swing.DefaultComboBoxModel;

public class EditableComboBoxItems {
	
	private static EditableComboBoxItems instance = null;

	private DefaultComboBoxModel columnSeparators;
	
	private DefaultComboBoxModel commentIndicators;
	
	private DefaultComboBoxModel textQualifiers;
	
	private DefaultComboBoxModel dateAndTimePatterns;
	
	private DefaultComboBoxModel EPSGCodes;
	
	private EditableComboBoxItems() {
		setColumnSeparators(new DefaultComboBoxModel(
				new Object[]{";", ":", ",", "\t"}));
		setCommentIndicators(new DefaultComboBoxModel(
				new Object[]{"#"}));
		setTextQualifiers(new DefaultComboBoxModel(
				new Object[]{"\"", "'"}));
		setDateAndTimePatterns(new DefaultComboBoxModel(
				new Object[] {
                "dd MMMMM yyyy",
                "dd.MM.yy",
                "MM/dd/yy",
                "yyyy.MM.dd G 'at' hh:mm:ss z",
                "EEE, MMM d, ''yy",
                "h:mm a",
                "H:mm:ss:SSS",
                "K:mm a,z",
                "yyyy.MMMMM.dd GGG hh:mm aaa"}));	
		setEPSGCodes(new DefaultComboBoxModel(
				new Object[]{5326, 1235}));
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
	
}
