package org.n52.sos.importer.model;

import org.n52.sos.importer.combobox.EditableComboBoxItems;

public class Step2Model {

	private String csvFileContent;
	
	private String selectedColumnSeparator;
	
	private String selectedCommentIndicator;
	
	private String selectedTextQualifier;
	
	private int firstLineWithData;
	
	public Step2Model(String csvFileContent) {
		this.csvFileContent = csvFileContent;
		
		EditableComboBoxItems items = EditableComboBoxItems.getInstance();
		selectedColumnSeparator = (String) items.getColumnSeparators().getElementAt(0);
		selectedCommentIndicator = (String) items.getCommentIndicators().getElementAt(0);
		selectedTextQualifier = (String) items.getTextQualifiers().getElementAt(0);
		
		firstLineWithData = 1;
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

	/**
	 * @return the firstLineWithData
	 */
	public int getFirstLineWithData() {
		return firstLineWithData;
	}

	/**
	 * @param firstLineWithData the firstLineWithData to set
	 */
	public void setFirstLineWithData(int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
	}

	public String getSelectedTextQualifier() {
		return selectedTextQualifier;
	}

	public void setSelectedTextQualifier(String selectedTextQualifier) {
		this.selectedTextQualifier = selectedTextQualifier;
	}

	public String getCSVFileContent() {
		return csvFileContent;
	}

	public void setCSVFileContent(String cSVFileContent) {
		csvFileContent = cSVFileContent;
	}
}