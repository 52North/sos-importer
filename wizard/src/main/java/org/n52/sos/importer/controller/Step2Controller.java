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

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.CsvData;
import org.n52.sos.importer.model.Step2Model;
import org.n52.sos.importer.view.Step2Panel;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

/**
 * offers settings for parsing the CSV file
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step2Controller extends StepController {

    private static final Logger LOG = LoggerFactory.getLogger(Step2Controller.class);

    private Step2Model model;

    private Step2Panel panel;

    /**
     * <p>Constructor for Step2Controller.</p>
     *
     * @param step2Model a {@link org.n52.sos.importer.model.Step2Model} object.
     */
    public Step2Controller(Step2Model step2Model) {
        model = step2Model;
    }

    @Override
    public String getDescription() {
        return Lang.l().step2Description();
    }

    @Override
    public boolean isFinished() {
        String columnSeparator = panel.getColumnSeparator();
        if (columnSeparator == null || columnSeparator.equals("")) {
            return false;
        }
        String commentIndicator = panel.getCommentIndicator();
        if (commentIndicator == null || commentIndicator.equals("")) {
            return false;
        }
        String textQualifier = panel.getTextQualifier();
        if (textQualifier == null || textQualifier.equals("")) {
            return false;
        }
        int firstLineWithData = panel.getFirstLineWithData();
        if (firstLineWithData < 0 ||
                firstLineWithData > model.getCsvFileRowRount() - 1) {
            return false;
        }

        return !(model.isSampleBased() &&
                (model.getSampleBasedStartRegEx() == null ||
                model.getSampleBasedStartRegEx().isEmpty() ||
                model.getSampleBasedDateOffset() < 1 ||
                model.getSampleBasedDateExtractionRegEx() == null ||
                model.getSampleBasedDateExtractionRegEx().isEmpty() ||
                !model.getSampleBasedDateExtractionRegEx().contains("(") ||
                model.getSampleBasedDateExtractionRegEx().indexOf(")") < 1 ||
                model.getSampleBasedDatePattern() == null ||
                model.getSampleBasedDatePattern().isEmpty() ||
                model.getSampleBasedDataOffset() < 1 ||
                model.getSampleBasedSampleSizeOffset() < 1 ||
                model.getSampleBasedSampleSizeRegEx() == null ||
                model.getSampleBasedSampleSizeRegEx().isEmpty() ||
                !model.getSampleBasedSampleSizeRegEx().contains("(") ||
                model.getSampleBasedSampleSizeRegEx().indexOf(")") < 1
                ));
    }

    @Override
    public StepController getNextStepController() {
        CsvData content = parseCSVFile();
        TableController.getInstance().setContent(content);
        TableController.getInstance().setFirstLineWithData(
                model.getFirstLineWithData());
        return new Step3Controller(0,
                model.getFirstLineWithData(),
                model.isUseHeader());
    }

    @Override
    public void loadSettings() {
        LOG.trace("loadSettings()");
        panel = new Step2Panel(model.getCsvFileRowRount());

        String columnSeparator = model.getColumnSeparator();
        panel.setColumnSeparator(columnSeparator);

        String commentIndicator = model.getCommentIndicator();
        panel.setCommentIndicator(commentIndicator);

        String textQualifier = model.getTextQualifier();
        panel.setTextQualifier(textQualifier);

        int firstLineWithData = model.getFirstLineWithData();
        panel.setFirstLineWithData(firstLineWithData);

        String csvFileContent = model.getCSVFileContent();
        panel.setCSVFileContent(csvFileContent);

        boolean useHeader = model.isUseHeader();
        panel.setUseHeader(useHeader);
        panel.setCSVFileHighlight(firstLineWithData);

        char decimalSeparator = model.getDecimalSeparator();
        panel.setDecimalSeparator(decimalSeparator + "");

        if (model.isSampleBased()) {
            panel.setSampleBased(true);
            panel.setSampleBasedStartRegEx(model.getSampleBasedStartRegEx());
            panel.setSampleBasedDateOffset(model.getSampleBasedDateOffset());
            panel.setSampleBasedDateExtractionRegEx(model.getSampleBasedDateExtractionRegEx());
            panel.setSampleBasedDatePattern(model.getSampleBasedDatePattern());
            panel.setSampleBasedDataOffset(model.getSampleBasedDataOffset());
            panel.setSampleBasedSampleSizeOffset(model.getSampleBasedSampleSizeOffset());
            panel.setSampleBasedSampleSizeRegEx(model.getSampleBasedSampleSizeRegEx());
        }
    }

    @Override
    public void saveSettings() {
        LOG.trace("saveSettings()");
        String columnSeparator = panel.getColumnSeparator();
        model.setColumnSeparator(columnSeparator);

        String commentIndicator = panel.getCommentIndicator();
        model.setCommentIndicator(commentIndicator);

        String textQualifier = panel.getTextQualifier();
        model.setTextQualifier(textQualifier);

        int firstLineWithData = panel.getFirstLineWithData();
        if (firstLineWithData < 0 || firstLineWithData > model.getCsvFileRowRount() - 1) {
            LOG.info("FirstLineWithData is to large. Set to 0");
            model.setFirstLineWithData(0);
        } else {
            model.setFirstLineWithData(firstLineWithData);
        }

        boolean useHeader = panel.getUseHeader();
        model.setUseHeader(useHeader);

        String csvFileContent = panel.getCSVFileContent();
        model.setCSVFileContent(csvFileContent);

        String decimalSeparator = panel.getDecimalSeparator();
        model.setDecimalSeparator(decimalSeparator.charAt(0));
        // Update global decimal separator
        Constants.setDecimalSeparator(decimalSeparator.charAt(0));
        if (Constants.getDecimalSeparator() == '.') {
            Constants.setThousandsSeparator(',');
        } else {
            Constants.setThousandsSeparator('.');
        }

        if (panel.isSampleBased()) {
            model.setSampleBased(true);
            model.setSampleBasedStartRegEx(panel.getSampleBasedStartRegEx());
            model.setSampleBasedDateOffset(panel.getSampleBasedDateOffset());
            model.setSampleBasedDateExtractionRegEx(panel.getSampleBasedDateExtractionRegEx());
            model.setSampleBasedDatePattern(panel.getSampleBasedDatePattern());
            model.setSampleBasedDataOffset(panel.getSampleBasedDataOffset());
            model.setSampleBasedSampleSizeOffset(panel.getSampleBasedSampleSizeOffset());
            model.setSampleBasedSampleSizeRegEx(panel.getSampleBasedSampleSizeRegEx());
        }

        panel = null;
    }

    /**
     * <p>convertSpaceSeparatedText.</p>
     *
     * @param text a {@link java.lang.String} object.
     * @param separator a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String convertSpaceSeparatedText(String text, String separator) {
        StringBuilder replacedText = new StringBuilder();
        StringReader sr = new StringReader(text);
        BufferedReader br = new BufferedReader(sr);
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                line = line.trim();
                line = replaceWhiteSpace(line, separator);
                replacedText.append(line).append("\n");
            }
        } catch (IOException e) {
            LOG.info("Error while parsing space-separated file", e);
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
    public String replaceWhiteSpace(String text, String separator) {
        StringBuilder replacedText = new StringBuilder();
        boolean lastCharacterWasAWhiteSpace = false;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (Character.isWhitespace(ch)) {
                if (!lastCharacterWasAWhiteSpace) {
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

    @Override
    public Step2Panel getStepPanel() {
        return panel;
    }

    @Override
    public boolean isNecessary() {
        return true;
    }

    @Override
    public StepController getNext() {
        return null;
    }

    @Override
    public boolean isStillValid() {
        //TODO: check whether the CSV file has changed
        return false;
    }

    @Override
    public Step2Model getModel() {
        return model;
    }

    private CsvData parseCSVFile() {
        CsvData content = new CsvData();
        String csvFileContent = model.getCSVFileContent();
        String separator = model.getColumnSeparator();
        String quoteChar = model.getCommentIndicator();
        String escape = model.getTextQualifier();
        int firstLineWithData = model.getFirstLineWithData();
        boolean useHeader = model.isUseHeader();

        String comma = "', ";
        LOG.info("Parse CSV file: " +
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
        StringReader sr = new StringReader(csvFileContent);
        try (CSVReader reader = new CSVReader(sr, separator.charAt(0), quoteChar.charAt(0), escape.charAt(0))) {
            content.setLines(reader.readAll());
        } catch (IOException e) {
            LOG.error("Error while parsing CSV file.", e);
        }
        return content;
    }
}
