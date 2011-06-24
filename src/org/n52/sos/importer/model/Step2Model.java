package org.n52.sos.importer.model;

import org.n52.sos.importer.config.EditableComboBoxItems;

public class Step2Model {

	private String selectedColumnSeparator;
	
	private String selectedCommentIndicator;
	
	private String selectedTextQualifier;
	
	private String CSVFileContent;
	
	public Step2Model(String CSVFileContent) {
		this.CSVFileContent = CSVFileContent;
		
		EditableComboBoxItems items = EditableComboBoxItems.getInstance();
		selectedColumnSeparator = (String) items.getColumnSeparators().getElementAt(0);
		selectedCommentIndicator = (String) items.getCommentIndicators().getElementAt(0);
		selectedTextQualifier = (String) items.getTextQualifiers().getElementAt(0);
	}

	public String getSelectedColumnSeparator() {
		return selectedColumnSeparator;
	}

	public void setSelectedColumnSeparator(String selectedColumnSeparator) {
		this.selectedColumnSeparator = selectedColumnSeparator;
	}

	public String getSelectedCommentIndicator() {
		return selectedCommentIndicator;
	}

	public void setSelectedCommentIndicator(String selectedCommentIndicator) {
		this.selectedCommentIndicator = selectedCommentIndicator;
	}

	public String getSelectedTextQualifier() {
		return selectedTextQualifier;
	}

	public void setSelectedTextQualifier(String selectedTextQualifier) {
		this.selectedTextQualifier = selectedTextQualifier;
	}

	public String getCSVFileContent() {
		return CSVFileContent;
	}

	public void setCSVFileContent(String cSVFileContent) {
		CSVFileContent = cSVFileContent;
	}
}