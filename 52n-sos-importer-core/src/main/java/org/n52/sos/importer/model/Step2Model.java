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
package org.n52.sos.importer.model;

import org.n52.sos.importer.view.combobox.EditableComboBoxItems;

public class Step2Model implements StepModel {

	private String csvFileContent;

	private String columnSeparator;

	private String commentIndicator;

	private String textQualifier;

	private char decimalSeparator;

	private int firstLineWithData;

	private int csvFileRowRount;

	private boolean useHeader;

	private boolean isSampleBased;

	private String sampleBasedStartRegEx = "";

	public Step2Model(final String csvFileContent, final int csvFileRowCount) {
		this.csvFileContent = csvFileContent;

		final EditableComboBoxItems items = EditableComboBoxItems.getInstance();
		columnSeparator = (String) items.getColumnSeparators().getElementAt(0);
		commentIndicator = (String) items.getCommentIndicators().getElementAt(0);
		textQualifier = (String) items.getTextQualifiers().getElementAt(0);
		firstLineWithData = 0;
		csvFileRowRount = csvFileRowCount;
	}

	public int getCsvFileRowRount() {
		return csvFileRowRount;
	}

	public Step2Model setCsvFileRowRount(final int csvFileRowRount) {
		this.csvFileRowRount = csvFileRowRount;
		return this;
	}

	public String getColumnSeparator() {
		return columnSeparator;
	}

	public Step2Model setColumnSeparator(final String selectedColumnSeparator) {
		columnSeparator = selectedColumnSeparator;
		return this;
	}

	public String getCommentIndicator() {
		return commentIndicator;
	}

	public Step2Model setCommentIndicator(final String selectedCommentIndicator) {
		commentIndicator = selectedCommentIndicator;
		return this;
	}

	public int getFirstLineWithData() {
		return firstLineWithData;
	}

	public Step2Model setFirstLineWithData(final int firstLineWithData) {
		this.firstLineWithData = firstLineWithData;
		return this;
	}

	public String getTextQualifier() {
		return textQualifier;
	}

	public Step2Model setTextQualifier(final String selectedTextQualifier) {
		textQualifier = selectedTextQualifier;
		return this;
	}

	public String getCSVFileContent() {
		return csvFileContent;
	}

	public Step2Model setCSVFileContent(final String cSVFileContent) {
		csvFileContent = cSVFileContent;
		return this;
	}

	public boolean isUseHeader() {
		return useHeader;
	}

	public Step2Model setUseHeader(final boolean useHeader) {
		this.useHeader = useHeader;
		return this;
	}

	public char getDecimalSeparator() {
		return decimalSeparator;
	}

	public Step2Model setDecimalSeparator(final char decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
		return this;
	}

	public boolean isSampleBased() {
		return isSampleBased;
	}

	public Step2Model setSampleBased(final boolean isSampleBased) {
		this.isSampleBased = isSampleBased;
		return this;
	}

	public Step2Model setSampleBasedStartRegEx(final String sampleBasedStartRegEx) {
		this.sampleBasedStartRegEx = sampleBasedStartRegEx;
		return this;
	}

	public String getSampleBasedStartRegEx() {
		return sampleBasedStartRegEx;
	}
}