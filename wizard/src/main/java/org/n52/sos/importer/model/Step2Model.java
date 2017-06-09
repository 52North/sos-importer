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
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
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

    private int dateOffset;

    private String sampleBasedDateExtractionRegEx = "";

    private String sampleBasedDatePattern = "";

    private int dataOffset;

    private int sampleSizeOffset;

    private String sampleBasedSampleSizeRegEx = "";

    /**
     * <p>Constructor for Step2Model.</p>
     *
     * @param csvFileContent a {@link java.lang.String} object.
     * @param csvFileRowCount a int.
     */
    public Step2Model(final String csvFileContent, final int csvFileRowCount) {
        this.csvFileContent = csvFileContent;

        final EditableComboBoxItems items = EditableComboBoxItems.getInstance();
        columnSeparator = items.getColumnSeparators().getElementAt(0);
        commentIndicator = items.getCommentIndicators().getElementAt(0);
        textQualifier = items.getTextQualifiers().getElementAt(0);
        firstLineWithData = 0;
        csvFileRowRount = csvFileRowCount;
    }

    /**
     * <p>Getter for the field <code>csvFileRowRount</code>.</p>
     *
     * @return a int.
     */
    public int getCsvFileRowRount() {
        return csvFileRowRount;
    }

    /**
     * <p>Setter for the field <code>csvFileRowRount</code>.</p>
     *
     * @param csvFileRowRount a int.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setCsvFileRowRount(final int csvFileRowRount) {
        this.csvFileRowRount = csvFileRowRount;
        return this;
    }

    /**
     * <p>Getter for the field <code>columnSeparator</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getColumnSeparator() {
        return columnSeparator;
    }

    /**
     * <p>Setter for the field <code>columnSeparator</code>.</p>
     *
     * @param selectedColumnSeparator a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setColumnSeparator(final String selectedColumnSeparator) {
        columnSeparator = selectedColumnSeparator;
        return this;
    }

    /**
     * <p>Getter for the field <code>commentIndicator</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCommentIndicator() {
        return commentIndicator;
    }

    /**
     * <p>Setter for the field <code>commentIndicator</code>.</p>
     *
     * @param selectedCommentIndicator a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setCommentIndicator(final String selectedCommentIndicator) {
        commentIndicator = selectedCommentIndicator;
        return this;
    }

    /**
     * <p>Getter for the field <code>firstLineWithData</code>.</p>
     *
     * @return a int.
     */
    public int getFirstLineWithData() {
        return firstLineWithData;
    }

    /**
     * <p>Setter for the field <code>firstLineWithData</code>.</p>
     *
     * @param firstLineWithData a int.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setFirstLineWithData(final int firstLineWithData) {
        this.firstLineWithData = firstLineWithData;
        return this;
    }

    /**
     * <p>Getter for the field <code>textQualifier</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTextQualifier() {
        return textQualifier;
    }

    /**
     * <p>Setter for the field <code>textQualifier</code>.</p>
     *
     * @param selectedTextQualifier a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setTextQualifier(final String selectedTextQualifier) {
        textQualifier = selectedTextQualifier;
        return this;
    }

    /**
     * <p>getCSVFileContent.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCSVFileContent() {
        return csvFileContent;
    }

    /**
     * <p>setCSVFileContent.</p>
     *
     * @param cSVFileContent a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setCSVFileContent(final String cSVFileContent) {
        csvFileContent = cSVFileContent;
        return this;
    }

    /**
     * <p>isUseHeader.</p>
     *
     * @return a boolean.
     */
    public boolean isUseHeader() {
        return useHeader;
    }

    /**
     * <p>Setter for the field <code>useHeader</code>.</p>
     *
     * @param useHeader a boolean.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setUseHeader(final boolean useHeader) {
        this.useHeader = useHeader;
        return this;
    }

    /**
     * <p>Getter for the field <code>decimalSeparator</code>.</p>
     *
     * @return a char.
     */
    public char getDecimalSeparator() {
        return decimalSeparator;
    }

    /**
     * <p>Setter for the field <code>decimalSeparator</code>.</p>
     *
     * @param decimalSeparator a char.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setDecimalSeparator(final char decimalSeparator) {
        this.decimalSeparator = decimalSeparator;
        return this;
    }

    /**
     * <p>isSampleBased.</p>
     *
     * @return a boolean.
     */
    public boolean isSampleBased() {
        return isSampleBased;
    }

    /**
     * <p>setSampleBased.</p>
     *
     * @param newIsSampleBased a boolean.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setSampleBased(final boolean newIsSampleBased) {
        this.isSampleBased = newIsSampleBased;
        return this;
    }

    /**
     * <p>Setter for the field <code>sampleBasedStartRegEx</code>.</p>
     *
     * @param sampleBasedStartRegEx a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setSampleBasedStartRegEx(final String sampleBasedStartRegEx) {
        this.sampleBasedStartRegEx = sampleBasedStartRegEx;
        return this;
    }

    /**
     * <p>Getter for the field <code>sampleBasedStartRegEx</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSampleBasedStartRegEx() {
        return sampleBasedStartRegEx;
    }

    /**
     * <p>setSampleBasedDateOffset.</p>
     *
     * @param newDateOffset a int.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setSampleBasedDateOffset(final int newDateOffset) {
        this.dateOffset = newDateOffset;
        return this;
    }

    /**
     * <p>getSampleBasedDateOffset.</p>
     *
     * @return a int.
     */
    public int getSampleBasedDateOffset() {
        return dateOffset;
    }

    /**
     * <p>Setter for the field <code>sampleBasedDateExtractionRegEx</code>.</p>
     *
     * @param sampleBasedDateExtractionRegEx a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setSampleBasedDateExtractionRegEx(final String sampleBasedDateExtractionRegEx) {
        this.sampleBasedDateExtractionRegEx  = sampleBasedDateExtractionRegEx;
        return this;
    }

    /**
     * <p>Getter for the field <code>sampleBasedDateExtractionRegEx</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSampleBasedDateExtractionRegEx() {
        return sampleBasedDateExtractionRegEx;
    }

    /**
     * <p>Setter for the field <code>sampleBasedDatePattern</code>.</p>
     *
     * @param sampleBasedDateDatePattern a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setSampleBasedDatePattern(final String sampleBasedDateDatePattern) {
        sampleBasedDatePattern  = sampleBasedDateDatePattern;
        return this;
    }

    /**
     * <p>Getter for the field <code>sampleBasedDatePattern</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSampleBasedDatePattern() {
        return sampleBasedDatePattern;
    }

    /**
     * <p>setSampleBasedDataOffset.</p>
     *
     * @param newDataOffset a int.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setSampleBasedDataOffset(final int newDataOffset) {
        this.dataOffset = newDataOffset;
        return this;
    }

    /**
     * <p>getSampleBasedDataOffset.</p>
     *
     * @return a int.
     */
    public int getSampleBasedDataOffset() {
        return dataOffset;
    }

    /**
     * <p>setSampleBasedSampleSizeOffset.</p>
     *
     * @param newSampleSizeOffset a int.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setSampleBasedSampleSizeOffset(final int newSampleSizeOffset) {
        this.sampleSizeOffset = newSampleSizeOffset;
        return this;
    }

    /**
     * <p>getSampleBasedSampleSizeOffset.</p>
     *
     * @return a int.
     */
    public int getSampleBasedSampleSizeOffset() {
        return sampleSizeOffset;
    }

    /**
     * <p>Setter for the field <code>sampleBasedSampleSizeRegEx</code>.</p>
     *
     * @param sampleBasedSampleSizeRegEx a {@link java.lang.String} object.
     * @return a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Model setSampleBasedSampleSizeRegEx(final String sampleBasedSampleSizeRegEx) {
        this.sampleBasedSampleSizeRegEx  = sampleBasedSampleSizeRegEx;
        return this;
    }

    /**
     * <p>Getter for the field <code>sampleBasedSampleSizeRegEx</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSampleBasedSampleSizeRegEx() {
        return sampleBasedSampleSizeRegEx;
    }


}
