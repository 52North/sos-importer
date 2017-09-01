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
package org.n52.sos.importer.view.i18n;


import java.io.File;
import java.util.Locale;

import org.n52.sos.importer.Constants;

/**
 * This class contains all String used by the GUI in English
 *
 * @author e.h.juerrens@52north.org
 * @version $Id: $Id
 */
public class En extends Lang {

    private static final Locale LOCALE = Locale.ENGLISH;
    private static final String THE_CONFIGURATION_COULD_NOT_BE_SAVED_TO_FILE =
            "The configuration could not be saved to file\n\"";
    private static final String OPEN = "Open";
    private static final String VERSION = "Version";
    private static final String AQUOT_NL = "\"\n";
    private static final String NL_AQUOT = "\n\"";
    private static final String WHAT_IS_THE = "What is the ";
    private static final String CAN_ONLY_BE_A_DECIMAL_NUMBER_SO_FAR = " can only be a decimal number so far.";
    private static final String ERROR = "Error";
    private static final String THE = "The ";
    private static final String BINDING = "Binding";

    /** {@inheritDoc} */
    @Override
    public String altitude() {
        return "Altitude / Height";
    }

    /** {@inheritDoc} */
    @Override
    public String and() {
        return "and";
    }

    /** {@inheritDoc} */
    @Override
    public String backButtonLabel() {
        return "Back";
    }

    /** {@inheritDoc} */
    @Override
    public String binding() {
        return BINDING;
    }

    /** {@inheritDoc} */
    @Override
    public String code() {
        return "Code";
    }

    /** {@inheritDoc} */
    @Override
    public String column() {
        return "column";
    }

    /** {@inheritDoc} */
    @Override
    public String dataPreview() {
        return "Data Preview";
    }

    /** {@inheritDoc} */
    @Override
    public String date() {
        return "Date";
    }

    /** {@inheritDoc} */
    @Override
    public String day() {
        return "Day";
    }

    /** {@inheritDoc} */
    @Override
    public String editableComboBoxDeleteItemButton() {
        return "Delete the selected item from the list";
    }

    /** {@inheritDoc} */
    @Override
    public String editableComboBoxNewItemButton() {
        return "Add a new item to the list";
    }

    /** {@inheritDoc} */
    @Override
    public String epsgCode() {
        return "EPSG-Code";
    }

    /** {@inheritDoc} */
    @Override
    public String epsgCodeWarningDialogNaturalNumber() {
        return THE + Lang.l().epsgCode() + " has to be a natural number.";
    }

    /** {@inheritDoc} */
    @Override
    public String epsgCodeWarningDialogOutOfRange() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String error() {
        return ERROR;
    }

    /** {@inheritDoc} */
    @Override
    public String errorDialogTitle() {
        return ERROR;
    }

    /** {@inheritDoc} */
    @Override
    public String example() {
        return "Example";
    }

    /** {@inheritDoc} */
    @Override
    public String exitDialogQuestion() {
        return "Do you really want to exit?\n";
    }

    /** {@inheritDoc} */
    @Override
    public String exitDialogTitle() {
        return "Exit";
    }

    /** {@inheritDoc} */
    @Override
    public String featureOfInterest() {
        return "Feature of Interest";
    }

    /** {@inheritDoc} */
    @Override
    public String file() {
        return "file";
    }

    /** {@inheritDoc} */
    @Override
    public String finishButtonLabel() {
        return "Finish";
    }

    /** {@inheritDoc} */
    @Override
    public String format() {
        return "Format";
    }

    /* (non-Javadoc)
     * @see org.n52.sos.importer.view.i18n.Lang#generated()
     */
    /** {@inheritDoc} */
    @Override
    public String generated() {
        return "generated";
    }

    /** {@inheritDoc} */
    @Override
    public Locale getLocale() {
        return En.LOCALE;
    }

    /** {@inheritDoc} */
    @Override
    public String group() {
        return "Group";
    }

    /** {@inheritDoc} */
    @Override
    public String heightWarningDialogDecimalNumber() {
        return THE + altitude() + " has to be a decimal number.";
    }

    /** {@inheritDoc} */
    @Override
    public String hours() {
        return "Hours";
    }

    /** {@inheritDoc} */
    @Override
    public String infoDialogTitle() {
        return "Information";
    }

    /** {@inheritDoc} */
    @Override
    public String latitudeDialogDecimalValue() {
        return THE + latitudeNorthing() + CAN_ONLY_BE_A_DECIMAL_NUMBER_SO_FAR;
    }

    /** {@inheritDoc} */
    @Override
    public String latitudeNorthing() {
        return "Latitude / Northing";
    }

    /** {@inheritDoc} */
    @Override
    public String longitudeDialogDecimalValue() {
        return THE + longitudeEasting() + CAN_ONLY_BE_A_DECIMAL_NUMBER_SO_FAR;
    }

    /** {@inheritDoc} */
    @Override
    public String longitudeEasting() {
        return "Longitude / Easting";
    }

    /** {@inheritDoc} */
    @Override
    public String measuredValue() {
        return "measured value";
    }

    /** {@inheritDoc} */
    @Override
    public String metadata() {
        return "Metadata";
    }

    /** {@inheritDoc} */
    @Override
    public String minutes() {
        return "Minutes";
    }

    /** {@inheritDoc} */
    @Override
    public String month() {
        return "Month";
    }

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "Name";
    }

    /** {@inheritDoc} */
    @Override
    public String nextButtonLabel() {
        return "Next";
    }

    /** {@inheritDoc} */
    @Override
    public String numValuePanelThousandsSeparator() {
        return "Thousands separator";
    }

    /** {@inheritDoc} */
    @Override
    public String observation() {
        return "Observation";
    }

    /** {@inheritDoc} */
    @Override
    public String observedProperty() {
        return "Observed Property";
    }

    /** {@inheritDoc} */
    @Override
    public String offering() {
        return "Offering";
    }

    /** {@inheritDoc} */
    @Override
    public String path() {
        return "path";
    }

    /** {@inheritDoc} */
    @Override
    public String position() {
        return "Position";
    }

    /** {@inheritDoc} */
    @Override
    public String referenceSystem() {
        return "Reference System";
    }

    /** {@inheritDoc} */
    @Override
    public String row() {
        return "row";
    }

    /** {@inheritDoc} */
    @Override
    public String seconds() {
        return "Seconds";
    }

    /** {@inheritDoc} */
    @Override
    public String sensor() {
        return "Sensor";
    }

    /** {@inheritDoc} */
    @Override
    public String spaceString() {
        return "Space";
    }

    /** {@inheritDoc} */
    @Override
    public String specificationVersion() {
        return "Specification Version";
    }

    /** {@inheritDoc} */
    @Override
    public String step() {
        return "Step";
    }

    /** {@inheritDoc} */
    @Override
    public String step1BrowseButton() {
        return "Select";
    }

    /** {@inheritDoc} */
    @Override
    public String step1Description() {
        return step() + " 1: Choose CSV file";
    }

    /** {@inheritDoc} */
    @Override
    public String step1Directory() {
        return "Path";
    }

    /** {@inheritDoc} */
    @Override
    public String step1EncodingLabel() {
        return "Please select the input file encoding";
    }

    /** {@inheritDoc} */
    @Override
    public String step1FeedTypeCSV() {
        return "One-Time-Feed from a local CSV file";
    }

    /** {@inheritDoc} */
    @Override
    public String step1FeedTypeFTP() {
        return "One-Time-Feed / Repetitive Feed from a FTP-Server";
    }


    /** {@inheritDoc} */
    @Override
    public String step1File() {
        return "CSV File";
    }

    /** {@inheritDoc} */
    @Override
    public String step1FileSchema() {
        return "File-Schema";
    }

    /** {@inheritDoc} */
    @Override
    public String step1FtpServer() {
        return "FTP-Server";
    }

    /** {@inheritDoc} */
    @Override
    public String step1InstructionLabel() {
        return "Please select the CSV file";
    }

    /** {@inheritDoc} */
    @Override
    public String step1Introduction() {
        return "Introduction";
    }

    /** {@inheritDoc} */
    @Override
    public String step1Password() {
        return "Password";
    }

    /** {@inheritDoc} */
    @Override
    public String step1Regex() {
        return "<html>Regular<br/>expressions:</html>";
    }

    /** {@inheritDoc} */
    @Override
    public String step1RegexDescription() {
        return "<html>Note: Choose this option to describe dynamic folder and file "
                + "structures in the following lines. "
                + "Therefor be careful with special characters of regular expressions"
                + ", in front of all escape characters.</html>";
    }

    /** {@inheritDoc} */
    @Override
    public String step1SelectLanguage() {
        return "Change language";
    }

    /** {@inheritDoc} */
    @Override
    public String step1User() {
        return "User";
    }

    /** {@inheritDoc} */
    @Override
    public String step2ColumnSeparator() {
        return "Column separator";
    }

    /** {@inheritDoc} */
    @Override
    public String step2CommentIndicator() {
        return "Comment indicator";
    }

    /** {@inheritDoc} */
    @Override
    public String step2DataPreviewLabel() {
        return "CSV-Data-Preview";
    }

    /** {@inheritDoc} */
    @Override
    public String step2DecimalSeparator() {
        return "Decimal separator";
    }

    /** {@inheritDoc} */
    @Override
    public String step2Description() {
        return step() + " 2: Define CSV File Metadata";
    }

    /** {@inheritDoc} */
    @Override
    public String step2FirstLineWithData() {
        return "Ignore data until line";
    }

    /** {@inheritDoc} */
    @Override
    public String step2IsSampleBased() {
        return "Is data file sample based";
    }

    /** {@inheritDoc} */
    @Override
    public String step2ParseHeader() {
        return "Interpret Header";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDataOffsetLabel() {
        return "Offset data";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDataOffsetToolTip() {
        return "The offset in lines from sample beginning till the first lines with data.";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDateExtractionRegExLabel() {
        return "Regular Expression \"Date Extraction\"";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDateExtractionRegExTooltip() {
        return new StringBuffer("<html>The regular expression to extract the date<br/>")
            .append("information from the line containing the date<br/>")
            .append("information of the current sample. The expression MUST<br/>")
            .append("result in ONE group. This group will be parsed to a<br/>")
            .append("java.util.Date using \"sampleDatePattern\" attribute.</html>")
            .toString();
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDateOffsetLabel() {
        return "Offset date information";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDateOffsetToolTip() {
        return "The offset of the line containing the date of the sample from the start line.";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDatePatternLabel() {
        return "Parse Pattern \"Date Information\"";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDatePatternTooltip() {
        return "The pattern used to parse the date information of the current sample.";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedSampleSizeOffsetLabel() {
        return "Offset sample size";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedSampleSizeOffsetToolTip() {
        return new StringBuffer("<html>The offset in lines from sample beginning")
        .append("till<br>the line containing the sample size in lines ")
        .append("with data.</html>")
        .toString();
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedSampleSizeRegExLabel() {
        return "Regular Expression \"Sample Size\"";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedSampleSizeRegExTooltip() {
        return new StringBuffer("<html>The regular expression to extract the sample size.<br/>")
               .append("The regular expression MUST result in ONE group<br/>")
               .append("which contains an integer value.</html>")
               .toString();
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedStartRegExLabel() {
        return "Regular Expression \"Sample Start\"";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedStartRegExTooltip() {
        return "<html>Used to identify the start of a new sample.<br>MUST match the whole line.</html>";
    }

    /** {@inheritDoc} */
    @Override
    public String step2TextQualifier() {
        return "Text qualifier";
    }

    /** {@inheritDoc} */
    @Override
    public String step3Description() {
        return step() + " 3: Choose Metadata for the selected column";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasureValueColMissingDialogMessage() {
        return "You have to specify at least one " + measuredValue() + " column!";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasureValueColMissingDialogTitle() {
        return measuredValue() + " column missing";
    }

    /** {@inheritDoc} */
    @Override
    public String step3OmParameterCategory() {
        return "Category";
    }

    /** {@inheritDoc} */
    @Override
    public String step3OmParameterNameLabel() {
        return "Name";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ParseTest1Failed() {
        return "1 value could not be interpreted.";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ParseTestAllOk() {
        return "All values could be interpreted.";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ParseTestNFailed(final int n) {
        return n + " values could not be interpreted.";
    }

    /** {@inheritDoc} */
    @Override
    public String step3SelectedColTypeUndefinedMsg() {
        return "The type for this column is \"" +
                step3ColTypeUndefined() +
                "\".\nPlease select one.\nChoose \"" +
                step3ColTypeDoNotExport() +
                "\" for skipping it.";
    }

    /** {@inheritDoc} */
    @Override
    public String step3SelectedColTypeUndefinedTitle() {
        return "Column Type is \"" +
                step3ColTypeUndefined() +
                "\"";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ColTypeDateTime() {
        return "Date & Time";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ColTypeDoNotExport() {
        return "Do not export";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ColTypeMeasuredValue() {
        return "Measured Value";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ColTypeOmParameter() {
        return "om:Parameter";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ColTypeUndefined() {
        return "Undefined";
    }

    /** {@inheritDoc} */
    @Override
    public String step3DateAndTimeCombination() {
        return "Combination";
    }

    /** {@inheritDoc} */
    @Override
    public String step3DateAndTimeUnixTime() {
        return "UNIX time";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasuredValBoolean() {
        return "Boolean";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasuredValCount() {
        return "Count";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasuredValNumericValue() {
        return "Numeric Value";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasuredValText() {
        return "Text";
    }

    /** {@inheritDoc} */
    @Override
    public String step3PositionCombination() {
        return step3DateAndTimeCombination();
    }

    /** {@inheritDoc} */
    @Override
    public String step4aDescription() {
        return step() + " 4a: Solve Date & Time ambiguities";
    }

    /** {@inheritDoc} */
    @Override
    public String step4aInfoDateAndTime() {
        return "Date and Time are already set for this " + measuredValue() + ".";
    }

    /** {@inheritDoc} */
    @Override
    public String step4aInfoMeasuredValue() {
        return "This is not a " + measuredValue() + ".";
    }

    /** {@inheritDoc} */
    @Override
    public String step4aModelDescription() {
        return "Select all " + measuredValue() + " " + Constants.STRING_REPLACER + "s " +
                "where the marked Date & Time group corresponds to.";
    }

    /** {@inheritDoc} */
    @Override
    public String step4bDescription() {
        return step() + " 4b: Solve ambiguities";
    }

    /** {@inheritDoc} */
    @Override
    public String step4bInfoNotMeasuredValue() {
        return step4aInfoMeasuredValue();
    }

    /** {@inheritDoc} */
    @Override
    public String step4bInfoResourceAlreadySetText() {
        return " already set for this ";
    }

    /** {@inheritDoc} */
    @Override
    public String step4bModelDescription() {
        return "Please click the " +
            Constants.STRING_REPLACER +
            " (not the title) containing " +
            "the measured values for the marked " +
            Constants.STRING_REPLACER +
            " " +
            Constants.STRING_REPLACER +
            " that is marked. If several " +
            Constants.STRING_REPLACER +
            "s corespond to this " +
            Constants.STRING_REPLACER +
            " " +
            Constants.STRING_REPLACER +
            ", click all of them with pressed CTRL key.";
    }

    /** {@inheritDoc} */
    @Override
    public String step5aDescription() {
        return step() + " 5a: Complete time data";
    }

    /** {@inheritDoc} */
    @Override
    public String step5aModelDescription() {
        return "Please define the timezone for the marked date and time.";
    }

    /** {@inheritDoc} */
    @Override
    public String step5cDescription() {
        return step() + " 5c: Complete position data";
    }

    /** {@inheritDoc} */
    @Override
    public String step5cModelDescription() {
        return "Complete missing information for the marked position.";
    }

    /** {@inheritDoc} */
    @Override
    public String step6aDescription() {
        return step() + " 6a: Add missing dates and times";
    }

    /** {@inheritDoc} */
    @Override
    public String step6aModelDescription() {
        return "<html>What is the <u>Date & Time</u> for all measured values?</html>";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bDefineConcatString() {
        return "Please provide a String for linking the values from the " +
                "columns (OPTIONAL).";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bDescription() {
        return step() + " 6b: Add missing metadata";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bModelDescription() {
        return "<html>What is the <u>" +
                Constants.STRING_REPLACER +
                "</u> for the marked " + measuredValue() + " " +
                Constants.STRING_REPLACER +
                "?</html>";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bSelectColumnsLabel() {
        return "Please select the column(s) to generate the name.";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bSpecialDescription() {
        return step() + " 6b (Special): Add missing " + sensor() + "s";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bSpecialModelDescription() {
        return WHAT_IS_THE + sensor().toLowerCase(En.LOCALE) + " for";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bURIInstructions() {
        return "Please provide a URI or a prefix if using the name as part of the URI.";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bUseNameAfterPrefix() {
        return "Use Name after prefix?";
    }

    /** {@inheritDoc} */
    @Override
    public String step6cDescription() {
        return step() + " 6c: Add missing " + position().toLowerCase(En.LOCALE) + "s";
    }

    /** {@inheritDoc} */
    @Override
    public String step6cInfoToolName() {
        return "Set Position";
    }

    /** {@inheritDoc} */
    @Override
    public String step6cInfoToolTooltip() {
        return "Set the position by clicking on the map";
    }

    /** {@inheritDoc} */
    @Override
    public String step6cModelDescription() {
        return WHAT_IS_THE + position().toLowerCase(En.LOCALE) + " of";
    }

    /** {@inheritDoc} */
    @Override
    public String step6Generation() {
        return "Generate identifier automatically";
    }

    /** {@inheritDoc} */
    @Override
    public String step6ManualInput() {
        return "Set identifier manually";
    }

    /** {@inheritDoc} */
    @Override
    public String step6MissingUserInput() {
        return "Some User Input is missing. Please enter the required information.";
    }

    /** {@inheritDoc} */
    @Override
    public String step6NoUserInput() {
        return "No user input at all. Please fill in the required information.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigDirNotDirOrWriteable(final String folder) {
        return "The selected config file folder \n\"" +
                folder +
                "\"\nis not accessible for the application.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigFileButton() {
        return "Choose folder";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigFileDialogTitel() {
        return "Select Configuration file folder";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigFileLabel() {
        return "Folder";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigurationFile() {
        return "Configuration File";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigFileInstructions() {
        return "Please set the folder for saving the configuration file.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7Description() {
        return step() + " 7: Final Configuration";
    }

    /** {@inheritDoc} */
    @Override
    public String step7DirectImport() {
        return "Directly import data during next step";
    }

    /** {@inheritDoc} */
    @Override
    public String step7IgnoreColumnMismatchCheckBoxLabel() {
        return "Should lines with wrong number of columns be ignored?";
    }

    /** {@inheritDoc} */
    @Override
    public String step7IgnoreColumnMismatchBorderLabel() {
        return "Additional Settings";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategyBorderLabel() {
        return "Import Strategy";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategyLabel() {
        return "Strategy";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategySingleObservation() {
        return "Single Observation";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategySweArrayHunksizeLabel() {
        return "Hunk size";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategySweArrayObservation() {
        return "SweArrayObservation";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategySweArraySendBuffer() {
        return "Send Buffer";
    }

    /** {@inheritDoc} */
    @Override
    public String step7OfferingCheckBoxLabel() {
        return "Generate Offering from Sensor name?";
    }

    /** {@inheritDoc} */
    @Override
    public String step7OfferingInputTextfieldLabel() {
        return "Please specify the offering name:";
    }

    /** {@inheritDoc} */
    @Override
    public String step7OfferingNameNotGiven() {
        return "Please specify the offering name or select to generate it.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7OfferingNameNotValid(final String offeringName) {
        return String.format("The given offering '%s' is not valid. It should match XML-NCName specification.",
                offeringName);
    }

    /** {@inheritDoc} */
    @Override
    public String step7SaveConfig() {
        return "Save configuration to XML file";
    }

    /** {@inheritDoc} */
    @Override
    public String step7SosBindingInstructions() {
        return "Please specify the binding.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7SosBindingLabel() {
        return BINDING;
    }

    /** {@inheritDoc} */
    @Override
    public String step7SOSConncetionStart(final String strURL) {
        return "To start connection testing to URL" +
                NL_AQUOT + strURL + AQUOT_NL +
                "select YES. For changing values select NO.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7SOSconnectionFailed(final String strURL,
            final int responseCode) {
        return "Could not connect to " + sos() + ": "
                + strURL +
                ". HTTP Response Code: "
                + responseCode;
    }

    /** {@inheritDoc} */
    @Override
    public String step7SOSConnectionFailedException(final String strURL,
            final String message,
            final int readTimeoutSeconds,
            final int connectTimeoutSeconds) {
        return "Connection to " + sos() +
                NL_AQUOT + strURL + AQUOT_NL +
                "failed after " + connectTimeoutSeconds + " seconds connect and " +
                readTimeoutSeconds + " seconds read timeout.\n" +
                "Reason: " + message;
    }

    /** {@inheritDoc} */
    @Override
    public String step7SosUrlInstructions() {
        return "Please specify the SOS URL including the endpoint like this \"http://www.example.com/52nSOS/sos\" .";
    }

    /** {@inheritDoc} */
    @Override
    public String step7SosVersionInstructions() {
        return "Please specify the specification version that is implemented by the "
                + "SOS instance specified by the URL in the field above.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7SosVersionLabel() {
        return VERSION;
    }

    /** {@inheritDoc} */
    @Override
    public String step8ConfigFileButton() {
        return OPEN;
    }

    /** {@inheritDoc} */
    @Override
    public String step8ConfigurationFileInstructions() {
        return "For taking a look at the generated configuration file, please click button.";
    }

    /** {@inheritDoc} */
    @Override
    public String step8Description() {
        return step() + " 8: Final Step - Summary of the Results";
    }

    /** {@inheritDoc} */
    @Override
    public String step8DirectImportInstructions() {
        return "For importing the content of the data file one time, just click "
                + step8DirectImportStartButton();
    }

    /** {@inheritDoc} */
    @Override
    public String step8DirectImportLabel() {
        return "Register Sensors and Insert Observations into Sensor Observation Service";
    }

    /** {@inheritDoc} */
    @Override
    public String step8DirectImportStartButton() {
        return "Start Import";
    }

    /** {@inheritDoc} */
    @Override
    public String step8ErrorLable(final int i) {
        return "Errors: " + i;
    }

    @Override
    public String step8ErrorDesktopNotSupportedMesage(String pathToConfigFile) {
        return String.format("Could not open file. Please open it manually:%n%n'%s'%n%n",
                pathToConfigFile);
    }

    @Override
    public String step8ErrorDesktopNotSupportedTitle() {
        return "Opening File Failed ";
    }

    /** {@inheritDoc} */
    @Override
    public String step8FeederJarNotFound(final String expectedAbsolutePathToFeederJar) {
        return String.format("Could not find jar file!%nPlease place it there:%n'%s'",
                expectedAbsolutePathToFeederJar);
    }

    /** {@inheritDoc} */
    @Override
    public String step8FeederJarNotFoundSelectByUser(final String pathToDirectoryWithFeederJar) {
        return String.format("Could not find jar file here:%n'%s'%nPlease select YES for selecting the file manually.",
                pathToDirectoryWithFeederJar);
    }

    /** {@inheritDoc} */
    @Override
    public String step8InsertObservationLabel(final int i) {
        return "Insert " + i + " Observations...";
    }

    /** {@inheritDoc} */
    @Override
    public String step8LogFile() {
        return "Log File";
    }

    /** {@inheritDoc} */
    @Override
    public String step8LogFileButton() {
        return OPEN;
    }

    /** {@inheritDoc} */
    @Override
    public String step8LogFileInstructions() {
        return "To check for additional information, please take a look at the log file created during the process.";
    }

    /** {@inheritDoc} */
    @Override
    public String step8RegisterSensorLabel(final int i) {
        return "Register " + i + " " + sensor() + "(s)...";
    }

    /** {@inheritDoc} */
    @Override
    public String step8SaveModelFailed(final File f) {
        return THE_CONFIGURATION_COULD_NOT_BE_SAVED_TO_FILE +
                f.getAbsolutePath() +
                "\".";
    }

    /** {@inheritDoc} */
    @Override
    public String step8SaveModelFailed(final File f, final String exceptionText) {
        return THE_CONFIGURATION_COULD_NOT_BE_SAVED_TO_FILE +
                f.getAbsolutePath() +
                "\".\nAn Exception occurred:\n" +
                exceptionText +
                "Please check the log file for more details.";
    }

    /** {@inheritDoc} */
    @Override
    public String step8StartImportButton() {
        return "Start";
    }

    /** {@inheritDoc} */
    @Override
    public String step8SuccessLabel(final int i) {
        return "Successful: " + i;
    }

    /** {@inheritDoc} */
    @Override
    public String time() {
        return "Time";
    }

    /** {@inheritDoc} */
    @Override
    public String timeZone() {
        return "UTC offset";
    }

    /** {@inheritDoc} */
    @Override
    public String unit() {
        return "Unit";
    }

    /** {@inheritDoc} */
    @Override
    public String unitOfMeasurement() {
        return "Unit of Measurement";
    }

    /** {@inheritDoc} */
    @Override
    public String uri() {
        return "URI";
    }

    /** {@inheritDoc} */
    @Override
    public String uriSyntaxNotValidDialogMessage(final String uri) {
        return "The entered URI \"" + uri + "\" is syntactically not correct.";
    }

    /** {@inheritDoc} */
    @Override
    public String url() {
        return "URL";
    }

    /** {@inheritDoc} */
    @Override
    public String version() {
        return VERSION;
    }

    /** {@inheritDoc} */
    @Override
    public String waitForParseResultsLabel() {
        return "Testing evaluation pattern for column...";
    }

    /** {@inheritDoc} */
    @Override
    public String warningDialogTitle() {
        return "Warning";
    }

    /** {@inheritDoc} */
    @Override
    public String year() {
        return "Year";
    }

}
