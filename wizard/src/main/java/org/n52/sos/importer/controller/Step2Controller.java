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
package org.n52.sos.importer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JPanel;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.CsvData;
import org.n52.sos.importer.model.Step2Model;
import org.n52.sos.importer.model.StepModel;
import org.n52.sos.importer.view.Step2Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

/**
 * offers settings for parsing the CSV file
 *
 * @version $Id: $Id
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step2Controller extends StepController {

    private static final Logger logger = LoggerFactory.getLogger(Step2Controller.class);

    private final Step2Model step2Model;

    private Step2Panel step2Panel;

    /**
     * <p>Constructor for Step2Controller.</p>
     *
     * @param step2Model a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Controller(final Step2Model step2Model) {
        this.step2Model = step2Model;
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return Lang.l().step2Description();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isFinished() {
        final String columnSeparator = step2Panel.getColumnSeparator();
        if (columnSeparator == null || columnSeparator.equals("")) {
            return false;
        }
        final String commentIndicator = step2Panel.getCommentIndicator();
        if (commentIndicator == null || commentIndicator.equals("")) {
            return false;
        }
        final String textQualifier = step2Panel.getTextQualifier();
        if (textQualifier == null || textQualifier.equals("")) {
            return false;
        }
        final int firstLineWithData = step2Panel.getFirstLineWithData();
        if (firstLineWithData < 0 ||
                firstLineWithData > (step2Model.getCsvFileRowRount() - 1)) {
            return false;
        }

        if (step2Model.isSampleBased() &&
                (step2Model.getSampleBasedStartRegEx() == null ||
                step2Model.getSampleBasedStartRegEx().isEmpty() ||
                step2Model.getSampleBasedDateOffset() < 1 ||
                step2Model.getSampleBasedDateExtractionRegEx() == null ||
                step2Model.getSampleBasedDateExtractionRegEx().isEmpty() ||
                step2Model.getSampleBasedDateExtractionRegEx().indexOf("(") < 0 ||
                step2Model.getSampleBasedDateExtractionRegEx().indexOf(")") < 1 ||
                step2Model.getSampleBasedDatePattern() == null ||
                step2Model.getSampleBasedDatePattern().isEmpty() ||
                step2Model.getSampleBasedDataOffset() < 1 ||
                step2Model.getSampleBasedSampleSizeOffset() < 1 ||
                step2Model.getSampleBasedSampleSizeRegEx() == null ||
                step2Model.getSampleBasedSampleSizeRegEx().isEmpty() ||
                step2Model.getSampleBasedSampleSizeRegEx().indexOf("(") < 0 ||
                step2Model.getSampleBasedSampleSizeRegEx().indexOf(")") < 1
                )) {
            return false;
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public StepController getNextStepController() {
        final CsvData content = parseCSVFile();
        TableController.getInstance().setContent(content);
        TableController.getInstance().setFirstLineWithData(
                step2Model.getFirstLineWithData());
        return new Step3Controller(0,
                step2Model.getFirstLineWithData(),
                step2Model.isUseHeader());
    }

    /** {@inheritDoc} */
    @Override
    public void loadSettings() {
        if (logger.isTraceEnabled()) {
            logger.trace("loadSettings()");
        }
        step2Panel = new Step2Panel(step2Model.getCsvFileRowRount());

        final String columnSeparator = step2Model.getColumnSeparator();
        step2Panel.setColumnSeparator(columnSeparator);

        final String commentIndicator = step2Model.getCommentIndicator();
        step2Panel.setCommentIndicator(commentIndicator);

        final String textQualifier = step2Model.getTextQualifier();
        step2Panel.setTextQualifier(textQualifier);

        final int firstLineWithData = step2Model.getFirstLineWithData();
        step2Panel.setFirstLineWithData(firstLineWithData);

        final String csvFileContent = step2Model.getCSVFileContent();
        step2Panel.setCSVFileContent(csvFileContent);

        final boolean useHeader = step2Model.isUseHeader();
        step2Panel.setUseHeader(useHeader);
        step2Panel.setCSVFileHighlight(firstLineWithData);

        final char decimalSeparator = step2Model.getDecimalSeparator();
        step2Panel.setDecimalSeparator(decimalSeparator + "");

        if (step2Model.isSampleBased()) {
            step2Panel.setSampleBased(true);
            step2Panel.setSampleBasedStartRegEx(step2Model.getSampleBasedStartRegEx());
            step2Panel.setSampleBasedDateOffset(step2Model.getSampleBasedDateOffset());
            step2Panel.setSampleBasedDateExtractionRegEx(step2Model.getSampleBasedDateExtractionRegEx());
            step2Panel.setSampleBasedDatePattern(step2Model.getSampleBasedDatePattern());
            step2Panel.setSampleBasedDataOffset(step2Model.getSampleBasedDataOffset());
            step2Panel.setSampleBasedSampleSizeOffset(step2Model.getSampleBasedSampleSizeOffset());
            step2Panel.setSampleBasedSampleSizeRegEx(step2Model.getSampleBasedSampleSizeRegEx());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void saveSettings() {
        if (logger.isTraceEnabled()) {
            logger.trace("saveSettings()");
        }
        final String columnSeparator = step2Panel.getColumnSeparator();
        step2Model.setColumnSeparator(columnSeparator);

        final String commentIndicator = step2Panel.getCommentIndicator();
        step2Model.setCommentIndicator(commentIndicator);

        final String textQualifier = step2Panel.getTextQualifier();
        step2Model.setTextQualifier(textQualifier);

        final int firstLineWithData = step2Panel.getFirstLineWithData();
        if (firstLineWithData < 0 || firstLineWithData > (step2Model.getCsvFileRowRount() - 1)) {
            logger.info("FirstLineWithData is to large. Set to 0");
            step2Model.setFirstLineWithData(0);
        } else {
            step2Model.setFirstLineWithData(firstLineWithData);
        }

        final boolean useHeader = step2Panel.getUseHeader();
        step2Model.setUseHeader(useHeader);

        final String csvFileContent = step2Panel.getCSVFileContent();
        step2Model.setCSVFileContent(csvFileContent);

        final String decimalSeparator = step2Panel.getDecimalSeparator();
        step2Model.setDecimalSeparator(decimalSeparator.charAt(0));
        // Update global decimal separator
        Constants.setDecimalSeparator(decimalSeparator.charAt(0));
        if (Constants.getDecimalSeparator() == '.') {
            Constants.setThousandsSeparator(',');
        } else {
            Constants.setThousandsSeparator('.');
        }

        if (step2Panel.isSampleBased()) {
            step2Model.setSampleBased(true);
            step2Model.setSampleBasedStartRegEx(step2Panel.getSampleBasedStartRegEx());
            step2Model.setSampleBasedDateOffset(step2Panel.getSampleBasedDateOffset());
            step2Model.setSampleBasedDateExtractionRegEx(step2Panel.getSampleBasedDateExtractionRegEx());
            step2Model.setSampleBasedDatePattern(step2Panel.getSampleBasedDatePattern());
            step2Model.setSampleBasedDataOffset(step2Panel.getSampleBasedDataOffset());
            step2Model.setSampleBasedSampleSizeOffset(step2Panel.getSampleBasedSampleSizeOffset());
            step2Model.setSampleBasedSampleSizeRegEx(step2Panel.getSampleBasedSampleSizeRegEx());
        }

        step2Panel = null;
    }

    /**
     * <p>convertSpaceSeparatedText.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @param separator a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String convertSpaceSeparatedText(final String text, final String separator) {
        final StringBuilder replacedText = new StringBuilder();
        final StringReader sr = new StringReader(text);
        final BufferedReader br = new BufferedReader(sr);
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                line = replaceWhiteSpace(line, separator);
                replacedText.append(line + "\n");
            }
        } catch (final IOException e) {
            logger.info("Error while parsing space-separated file", e);
        }
        return replacedText.toString();
    }

    /**
     * replaces any whitespace in the text by the given separator
     *
     * @param text a {@link java.lang.String} object.
     * @param separator a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String replaceWhiteSpace(final String text, final String separator) {
        final StringBuilder replacedText = new StringBuilder();
        boolean lastCharacterWasAWhiteSpace = false;
        for (int i = 0; i < text.length(); i++) {
            final char ch = text.charAt(i);
            if (Character.isWhitespace(ch)) {
                if (lastCharacterWasAWhiteSpace) {
                    continue;
                } else {
                    replacedText.append(separator);
                    lastCharacterWasAWhiteSpace = true;
                }
            } else {
                replacedText.append(ch);
                lastCharacterWasAWhiteSpace = false;
            }
        }
        return replacedText.toString();
    }

    /** {@inheritDoc} */
    @Override
    public JPanel getStepPanel() {
        return step2Panel;
    }

    /* (non-Javadoc)
     * @see org.n52.sos.importer.controller.StepController#isNecessary()
     * this step is always required
     */
    /** {@inheritDoc} */
    @Override
    public boolean isNecessary() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public StepController getNext() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isStillValid() {
        //TODO: check whether the CSV file has changed
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public StepModel getModel() {
        return step2Model;
    }

    private CsvData parseCSVFile() {
        final CsvData content = new CsvData();
        String csvFileContent = step2Model.getCSVFileContent();
        String separator = step2Model.getColumnSeparator();
        final String quoteChar = step2Model.getCommentIndicator();
        final String escape = step2Model.getTextQualifier();
        final int firstLineWithData = step2Model.getFirstLineWithData();
        final boolean useHeader = step2Model.isUseHeader();

        final String comma = "', ";
        logger.info("Parse CSV file: " +
                "column separator: '"    + separator         + comma +
                "comment indicator: '"   + quoteChar         + comma +
                "text qualifier: '"      + escape            + comma +
                "first line with data: " + firstLineWithData + "; "  +
                "use header? " + useHeader);

        if (separator.equals("Tab")) {
            separator = "\t";
        } else if (separator.equals(Constants.SPACE_STRING)) {
            separator = ";";
            csvFileContent = convertSpaceSeparatedText(csvFileContent, separator);
        }
        final StringReader sr = new StringReader(csvFileContent);
        try (CSVReader reader = new CSVReader(sr, separator.charAt(0), quoteChar.charAt(0), escape.charAt(0))) {
            content.setLines(reader.readAll());
        } catch (final IOException e) {
            logger.error("Error while parsing CSV file.", e);
        }
        return content;
    }
}
