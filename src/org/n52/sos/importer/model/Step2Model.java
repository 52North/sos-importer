package org.n52.sos.importer.model;

import java.util.List;

public class Step2Model {

	private String selectedColumnSeparator;
	
	private String selectedCommentIndicator;
	
	private String selectedTextQualifier;
	
	private List<String> columnSeparators;
	
	private List<String> commentIndicators;
	
	private List<String> textQualifiers;
	
	private String CSVFileContent;

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

	public List<String> getColumnSeparators() {
		return columnSeparators;
	}

	public void setColumnSeparators(List<String> columnSeparators) {
		this.columnSeparators = columnSeparators;
	}

	public List<String> getCommentIndicators() {
		return commentIndicators;
	}

	public void setCommentIndicators(List<String> commentIndicators) {
		this.commentIndicators = commentIndicators;
	}

	public List<String> getTextQualifiers() {
		return textQualifiers;
	}

	public void setTextQualifiers(List<String> textQualifiers) {
		this.textQualifiers = textQualifiers;
	}

	public String getCSVFileContent() {
		return CSVFileContent;
	}

	public void setCSVFileContent(String cSVFileContent) {
		CSVFileContent = cSVFileContent;
	}
}