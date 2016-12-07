/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
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

	private int dateOffset = 0;

	private String sampleBasedDateExtractionRegEx = "";

	private String sampleBasedDatePattern = "";

	private int dataOffset = 0;

	private int sampleSizeOffset = 0;

	private String sampleBasedSampleSizeRegEx = "";

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

	public Step2Model setSampleBasedDateOffset(final int dateOffset) {
		this.dateOffset = dateOffset;
		return this;
	}

	public int getSampleBasedDateOffset() {
		return dateOffset;
	}

	public Step2Model setSampleBasedDateExtractionRegEx(final String sampleBasedDateExtractionRegEx) {
		this.sampleBasedDateExtractionRegEx  = sampleBasedDateExtractionRegEx;
		return this;
	}

	public String getSampleBasedDateExtractionRegEx() {
		return sampleBasedDateExtractionRegEx;
	}

	public Step2Model setSampleBasedDatePattern(final String sampleBasedDateDatePattern) {
		sampleBasedDatePattern  = sampleBasedDateDatePattern;
		return this;
	}

	public String getSampleBasedDatePattern() {
		return sampleBasedDatePattern;
	}

	public Step2Model setSampleBasedDataOffset(final int dataOffset) {
		this.dataOffset = dataOffset;
		return this;
	}

	public int getSampleBasedDataOffset() {
		return dataOffset;
	}

	public Step2Model setSampleBasedSampleSizeOffset(final int sampleSizeOffset) {
		this.sampleSizeOffset = sampleSizeOffset;
		return this;
	}

	public int getSampleBasedSampleSizeOffset() {
		return sampleSizeOffset;
	}

	public Step2Model setSampleBasedSampleSizeRegEx(final String sampleBasedSampleSizeRegEx) {
		this.sampleBasedSampleSizeRegEx  = sampleBasedSampleSizeRegEx;
		return this;
	}

	public String getSampleBasedSampleSizeRegEx() {
		return sampleBasedSampleSizeRegEx;
	}


}