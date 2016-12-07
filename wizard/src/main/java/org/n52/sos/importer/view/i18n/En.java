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
 */
public class En extends Lang {

	private final static Locale locale = Locale.ENGLISH;

	@Override
	public String altitude() {
		return "Altitude / Height";
	}

	@Override
	public String and() {
		return "and";
	}

	@Override
	public String backButtonLabel() {
		return "Back";
	}

	@Override
	public String binding()
	{
		return "Binding";
	}

	@Override
	public String code() {
		return "Code";
	}

	@Override
	public String column() {
		return "column";
	}

	@Override
	public String dataPreview() {
		return "Data Preview";
	}

	@Override
	public String date() {
		return "Date";
	}

	@Override
	public String day() {
		return "Day";
	}

	@Override
	public String editableComboBoxDeleteItemButton() {
		return "Delete the selected item from the list";
	}

	@Override
	public String editableComboBoxNewItemButton() {
		return "Add a new item to the list";
	}

	@Override
	public String epsgCode() {
		return "EPSG-Code";
	}

	@Override
	public String epsgCodeWarningDialogNaturalNumber() {
		return "The " + Lang.l().epsgCode() + " has to be a natural number.";
	}

	@Override
	public String epsgCodeWarningDialogOutOfRange() {
		return null;
	}

	@Override
	public String error() {
		return "Error";
	}

	@Override
	public String errorDialogTitle() {
		return "Error";
	}

	@Override
	public String example() {
		return "Example";
	}

	@Override
	public String exitDialogQuestion() {
		return "Do you really want to exit?\n";
	}

	@Override
	public String exitDialogTitle() { return "Exit"; }

	@Override
	public String featureOfInterest() {
		return "Feature of Interest";
	}

	@Override
	public String file() {
		return "file";
	}

	@Override
	public String finishButtonLabel() {
		return "Finish";
	}

	@Override
	public String format() {
		return "Format";
	}

	/* (non-Javadoc)
	 * @see org.n52.sos.importer.view.i18n.Lang#generated()
	 */
	@Override
	public String generated() {
		return "generated";
	}

	@Override
	public Locale getLocale() {
		return En.locale;
	}

	@Override
	public String group() {
		return "Group";
	}

	@Override
	public String heightWarningDialogDecimalNumber() {
		return "The " + altitude() + " has to be a decimal number.";
	}

	@Override
	public String hours() {
		return "Hours";
	}

	@Override
	public String infoDialogTitle() {
		return "Information";
	}

	@Override
	public String latitudeDialogDecimalValue() {
		return "The " + latitudeNorthing() + " can only be a decimal number so far.";
	}

	@Override
	public String latitudeNorthing() {
		return "Latitude / Northing";
	}

	@Override
	public String longitudeDialogDecimalValue() {
		return "The " + longitudeEasting() + " can only be a decimal number so far.";
	}

	@Override
	public String longitudeEasting() {
		return "Longitude / Easting";
	}

	@Override
	public String measuredValue() {
		return "measured value";
	}

	@Override
	public String metadata() {
		return "Metadata";
	}

	@Override
	public String minutes() {
		return "Minutes";
	}

	@Override
	public String month() {
		return "Month";
	}

	@Override
	public String name() {
		return "Name";
	}

	@Override
	public String nextButtonLabel() {
		return "Next";
	}

	@Override
	public String numValuePanelThousandsSeparator() {
		return "Thousands separator";
	}

	@Override
	public String observation() {
		return "Observation";
	}

	@Override
	public String observedProperty() {
		return "Observed Property";
	}

	@Override
	public String offering() {
		return "Offering";
	}

	@Override
	public String path() {
		return "path";
	}

	@Override
	public String position() {
		return "Position";
	}

	@Override
	public String referenceSystem() {
		return "Reference System";
	}

	@Override
	public String row() {
		return "row";
	}

	@Override
	public String seconds() {
		return "Seconds";
	}

	@Override
	public String sensor() {
		return "Sensor";
	}

	@Override
	public String spaceString() {
		return "Space";
	}

	@Override
	public String specificationVersion()
	{
		return "Specification Version";
	}

	@Override
	public String step() { return "Step"; }

	@Override
	public String step1BrowseButton() {
		return "Select";
	}

	@Override
	public String step1Description() {
		return step() + " 1: Choose CSV file";
	}

	@Override
	public String step1Directory() {
		return "Path";
	}

	@Override
	public String step1EncodingLabel() {
		return "Please select the input file encoding";
	}

	@Override
	public String step1FeedTypeCSV() {
		return "One-Time-Feed from a local CSV file";
	}

	@Override
	public String step1FeedTypeFTP() {
		return "One-Time-Feed / Repetitive Feed from a FTP-Server";
	}


	@Override
	public String step1File() {
		return "CSV File";
	}

	@Override
	public String step1FileSchema() {
		return "File-Schema";
	}

	@Override
	public String step1FtpServer() {
		return "FTP-Server";
	}

	@Override
	public String step1InstructionLabel() {
		return "Please select the CSV file";
	}

	@Override
	public String step1Introduction() {
		return "Introduction";
	}

	@Override
	public String step1Password() {
		return "Password";
	}

	@Override
	public String step1Regex() {
		return "<html>Regular<br/>expressions:</html>";
	}

	@Override
	public String step1RegexDescription() {
		return "<html>Note: Choose this option to describe dynamic folder and file "
				+ "structures in the following lines. "
				+ "Therefor be careful with special characters of regular expressions"
				+ ", in front of all escape characters.</html>";
	}

	@Override
	public String step1SelectLanguage() {
		return "Change language";
	}

	@Override
	public String step1User() {
		return "User";
	}

	@Override
	public String step2ColumnSeparator() {
		return "Column separator";
	}

	@Override
	public String step2CommentIndicator() {
		return "Comment indicator";
	}

	@Override
	public String step2DataPreviewLabel() {
		return "CSV-Data-Preview";
	}

	@Override
	public String step2DecimalSeparator() {
		return "Decimal separator";
	}

	@Override
	public String step2Description() {
		return step() + " 2: Define CSV File Metadata";
	}

	@Override
	public String step2FirstLineWithData() {
		return "Ignore data until line";
	}

	@Override
	public String step2IsSampleBased() {
		return "Is data file sample based";
	}

	@Override
	public String step2ParseHeader() {
		return "Interpret Header";
	}

	@Override
	public String step2SampleBasedDataOffsetLabel() {
		return "Offset data";
	}

	@Override
	public String step2SampleBasedDataOffsetToolTip() {
		return "The offset in lines from sample beginning till the first lines with data.";
	}

	@Override
	public String step2SampleBasedDateExtractionRegExLabel() {
		return "Regular Expression \"Date Extraction\"";
	}

	@Override
	public String step2SampleBasedDateExtractionRegExTooltip() {
		return new StringBuffer("<html>The regular expression to extract the date<br/>")
			.append("information from the line containing the date<br/>")
			.append("information of the current sample. The expression MUST<br/>")
			.append("result in ONE group. This group will be parsed to a<br/>")
			.append("java.util.Date using \"sampleDatePattern\" attribute.</html>")
			.toString();
	}

	@Override
	public String step2SampleBasedDateOffsetLabel() {
		return "Offset date information";
	}

	@Override
	public String step2SampleBasedDateOffsetToolTip() {
		return "The offset of the line containing the date of the sample from the start line.";
	}

	@Override
	public String step2SampleBasedDatePatternLabel() {
		return "Parse Pattern \"Date Information\"";
	}

	@Override
	public String step2SampleBasedDatePatternTooltip() {
		return "The pattern used to parse the date information of the current sample.";
	}

	@Override
	public String step2SampleBasedSampleSizeOffsetLabel() {
		return "Offset sample size";
	}

	@Override
	public String step2SampleBasedSampleSizeOffsetToolTip() {
		return new StringBuffer("<html>The offset in lines from sample beginning")
		.append("till<br />the line containing the sample size in lines ")
		.append("with data.</html>")
		.toString();
	}

	@Override
	public String step2SampleBasedSampleSizeRegExLabel() {
		return "Regular Expression \"Sample Size\"";
	}

	@Override
	public String step2SampleBasedSampleSizeRegExTooltip() {
		return new StringBuffer("<html>The regular expression to extract the sample size.<br/>")
			   .append("The regular expression MUST result in ONE group<br/>")
			   .append("which contains an integer value.</html>")
			   .toString();
	}

	@Override
	public String step2SampleBasedStartRegExLabel() {
		return "Regular Expression \"Sample Start\"";
	}

	@Override
	public String step2SampleBasedStartRegExTooltip() {
		return "<html>Used to identify the start of a new sample.<br />MUST match the whole line.</html>";
	}

	@Override
	public String step2TextQualifier() {
		return "Text qualifier";
	}

	@Override
	public String step3aDescription() {
		return step() + " 3a: Choose Metadata for the selected column";
	}

	@Override
	public String step3aMeasureValueColMissingDialogMessage() {
		return "You have to specify at least one " + measuredValue() + " column!";
	}

	@Override
	public String step3aMeasureValueColMissingDialogTitle() {
		return measuredValue() + " column missing";
	}

	@Override
	public String step3aParseTest1Failed() {
		return "1 value could not be interpreted.";
	}

	@Override
	public String step3aParseTestAllOk() {
		return "All values could be interpreted.";
	}

	@Override
	public String step3aParseTestNFailed(final int n) {
		return n + " values could not be interpreted.";
	}

	@Override
	public String step3aSelectedColTypeUndefinedMsg() {
		return "The type for this column is \"" +
				step3ColTypeUndefined() +
				"\".\nPlease select one.\nChoose \"" +
				step3ColTypeDoNotExport() +
				"\" for skipping it.";
	}

	@Override
	public String step3aSelectedColTypeUndefinedTitle() {
		return "Column Type is \"" +
				step3ColTypeUndefined() +
				"\"";
	}

	@Override
	public String step3bDescription() {
		return step() + " 3b: Choose metadata for rows";
	}

	@Override
	public String step3ColTypeDateTime() {
		return "Date & Time";
	}

	@Override
	public String step3ColTypeDoNotExport() {
		return "Do not export";
	}

	@Override
	public String step3ColTypeMeasuredValue() {
		return "Measured Value";
	}

	@Override
	public String step3ColTypeUndefined() {
		return "Undefined";
	}

	@Override
	public String step3DateAndTimeCombination() {
		return "Combination";
	}

	@Override
	public String step3DateAndTimeUnixTime() {
		return "UNIX time";
	}

	@Override
	public String step3MeasuredValBoolean() {
		return "Boolean";
	}

	@Override
	public String step3MeasuredValCount() {
		return "Count";
	}

	@Override
	public String step3MeasuredValNumericValue() {
		return "Numeric Value";
	}

	@Override
	public String step3MeasuredValText() {
		return "Text";
	}

	@Override
	public String step3PositionCombination() {
		return step3DateAndTimeCombination();
	}

	@Override
	public String step4aDescription() {
		return step() + " 4a: Solve Date & Time ambiguities";
	}

	@Override
	public String step4aInfoDateAndTime() {
		return "Date and Time are already set for this " + measuredValue() + ".";
	}

	@Override
	public String step4aInfoMeasuredValue() {
		return "This is not a " + measuredValue() + ".";
	}

	@Override
	public String step4aModelDescription() {
		return "Select all " + measuredValue() + " " + Constants.STRING_REPLACER + "s " +
				"where the marked Date & Time group corresponds to.";
	}

	@Override
	public String step4bDescription() {
		return step() + " 4b: Solve ambiguities";
	}

	@Override
	public String step4bInfoNotMeasuredValue() {
		return step4aInfoMeasuredValue();
	}

	@Override
	public String step4bInfoResourceAlreadySetText() {
		return " already set for this ";
	}

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

	@Override
	public String step5aDescription() {
		return step() + " 5a: Complete time data";
	}

	@Override
	public String step5aModelDescription() {
		return "Please define the timezone for the marked date and time.";
	}

	@Override
	public String step5cDescription() {
		return step() + " 5c: Complete position data";
	}

	@Override
	public String step5cModelDescription() {
		return "Complete missing information for the marked position.";
	}

	@Override
	public String step6aDescription() {
		return step() + " 6a: Add missing dates and times";
	}

	@Override
	public String step6aModelDescription() {
		return "<html>What is the <u>Date & Time</u> for all measured values?</html>";
	}

	@Override
	public String step6bDefineConcatString() {
		return "Please provide a String for linking the values from the " +
				"columns (OPTIONAL).";
	}

	@Override
	public String step6bDescription() {
		return step() + " 6b: Add missing metadata";
	}

	@Override
	public String step6bModelDescription() {
		return "<html>What is the <u>" +
				Constants.STRING_REPLACER +
				"</u> for the marked " + measuredValue() + " " +
				Constants.STRING_REPLACER +
				"?</html>";
	}

	@Override
	public String step6bSelectColumnsLabel() {
		return "Please select the column(s) to generate the name.";
	}

	@Override
	public String step6bSpecialDescription() {
		return step() + " 6b (Special): Add missing " + sensor() + "s";
	}

	@Override
	public String step6bSpecialModelDescription() {
		return "What is the " + sensor().toLowerCase(En.locale) + " for";
	}

	@Override
	public String step6bURIInstructions() {
		return "Please provide a URI or a prefix if using the name as part of the URI.";
	}

	@Override
	public String step6bUseNameAfterPrefix() {
		return "Use Name after prefix?";
	}

	@Override
	public String step6cDescription() {
		return step() + " 6c: Add missing " + position().toLowerCase(En.locale) + "s";
	}

	@Override
	public String step6cInfoToolName() {
		return "Set Position";
	}

	@Override
	public String step6cInfoToolTooltip() {
		 return "Set the position by clicking on the map";
	}

	@Override
	public String step6cModelDescription() {
		return "What is the " + position().toLowerCase(En.locale) + " of";
	}

	@Override
	public String step6Generation() {
		return "Generate identifier automatically";
	}

	@Override
	public String step6ManualInput() {
		return "Set identifier manually";
	}

	@Override
	public String step6MissingUserInput() {
		return "Some User Input is missing. Please enter the required information.";
	}

	@Override
	public String step6NoUserInput() {
		return "No user input at all. Please fill in the required information.";
	}

	@Override
	public String step7ConfigDirNotDirOrWriteable(final String folder) {
		return "The selected config file folder \n\"" +
				folder +
				"\"\nis not accessible for the application.";
	}

	@Override
	public String step7ConfigFileButton() {
		return "Choose folder";
	}

	@Override
	public String step7ConfigFileDialogTitel() {
		return "Select Configuration file folder";
	}

	@Override
	public String step7ConfigFileLabel() {
		return "Folder";
	}

	@Override
	public String step7ConfigurationFile() {
		return "Configuration File";
	}
	
	@Override
	public String step7ConfigFileInstructions() {
		return "Please set the folder for saving the configuration file.";
	}

	@Override
	public String step7Description() {
		return step() + " 7: Final Configuration";
	}

	@Override
	public String step7DirectImport() {
		return "Directly import data during next step";
	}

	@Override
	public String step7ImportStrategyBorderLabel() {
		return "Import Strategy";
	}

	@Override
	public String step7ImportStrategyLabel() {
		return "Strategy";
	}

	@Override
	public String step7ImportStrategySingleObservation() {
		return "Single Observation";
	}

	@Override
	public String step7ImportStrategySweArrayHunksizeLabel() {
		return "Hunk size";
	}

	@Override
	public String step7ImportStrategySweArrayObservation() {
		return "SweArrayObservation";
	}

	@Override
	public String step7ImportStrategySweArraySendBuffer() {
		return "Send Buffer";
	}

	@Override
	public String step7OfferingCheckBoxLabel() {
		return "Generate Offering from Sensor name?";
	}

	@Override
	public String step7OfferingInputTextfieldLabel() {
		return "Please specify the offering name:";
	}

	@Override
	public String step7OfferingNameNotGiven() {
		return "Please specify the offering name or select to generate it.";
	}

	@Override
	public String step7OfferingNameNotValid(final String offeringName) {
		return String.format("The given offering '%s' is not valid. It should match XML-NCName specification.", offeringName);
	}

	@Override
	public String step7SaveConfig() {
		return "Save configuration to XML file";
	}

	@Override
	public String step7SosBindingInstructions()
	{
		return "Please specify the binding.";
	}

	@Override
	public String step7SosBindingLabel() {
		return "Binding";
	}

	@Override
	public String step7SOSConncetionStart(final String strURL) {
		return "To start connection testing to URL" +
				"\n\"" + strURL + "\"\n" +
				"select YES. For changing values select NO.";
	}

	@Override
	public String step7SOSconnectionFailed(final String strURL,
			final int responseCode) {
		return "Could not connect to " + sos() + ": "
        		+ strURL +
        		". HTTP Response Code: "
        		+ responseCode;
	}

	@Override
	public String step7SOSConnectionFailedException(final String strURL,
			final String message,
			final int readTimeoutSeconds,
			final int connectTimeoutSeconds) {
		return "Connection to " + sos() +
				"\n\"" + strURL + "\"\n" +
				"failed after " + connectTimeoutSeconds + " seconds connect and " +
				readTimeoutSeconds + " seconds read timeout.\n" +
				"Reason: " + message;
	}

	@Override
	public String step7SosUrlInstructions() {
		return "Please specify the SOS URL including the endpoint like this \"http://www.example.com/52nSOS/sos\" .";
	}

	@Override
	public String step7SosVersionInstructions()
	{
		return "Please specify the specification version that is implemented by the SOS instance specified by the URL in the field above.";
	}

	@Override
	public String step7SosVersionLabel() {
		return "Version";
	}

	@Override
	public String step8ConfigFileButton() {
		return "Open";
	}

	@Override
	public String step8ConfigurationFileInstructions() {
		return "For taking a look at the generated configuration file, please click button.";
	}

	@Override
	public String step8Description() {
		return step() + " 8: Final Step - Summary of the Results";
	}

	@Override
	public String step8DirectImportInstructions() {
		return "For importing the content of the data file one time, just click ".concat(step8DirectImportStartButton());
	}

	@Override
	public String step8DirectImportLabel() {
		return "Register Sensors and Insert Observations into Sensor Observation Service";
	}

	@Override
	public String step8DirectImportStartButton() {
		return "Start Import";
	}

	@Override
	public String step8ErrorLable(final int i) {
		return "Errors: " + i;
	}

	@Override
	public String step8FeederJarNotFound(final String expectedAbsolutePathToFeederJar) {
		return String.format("Could not find jar file!\nPlease place it there:\n'%s'",
				expectedAbsolutePathToFeederJar);
	}

	@Override
	public String step8FeederJarNotFoundSelectByUser(final String pathToDirectoryWithFeederJar)
	{
		return String.format("Could not find jar file here:\n'%s'\nPlease select YES for selecting the file manually.",
				pathToDirectoryWithFeederJar);
	}

	@Override
	public String step8InsertObservationLabel(final int i) {
		return "Insert " + i + " Observations...";
	}

	@Override
	public String step8LogFile() {
		return "Log File";
	}

	@Override
	public String step8LogFileButton() {
		return "Open";
	}

	@Override
	public String step8LogFileInstructions() {
		return "To check for additional information, please take a look at the log file created during the process.";
	}

	@Override
	public String step8RegisterSensorLabel(final int i) {
		return "Register " + i + " " + sensor() + "(s)...";
	}

	@Override
	public String step8SaveModelFailed(final File f) {
		return "The configuration could not be saved to file\n\"" +
				f.getAbsolutePath();
	}

	@Override
	public String step8SaveModelFailed(final File f, final String exceptionText) {
		return "The configuration could not be saved to file\n\"" +
				f.getAbsolutePath() +
				"\".\nAn Exception occurred:\n" +
				exceptionText +
				"Please check the log file for more details.";
	}

	@Override
	public String step8StartImportButton() {
		return "Start";
	}

	@Override
	public String step8SuccessLabel(final int i) {
		return "Successful: " + i;
	}

	@Override
	public String time() {
		return "Time";
	}

	@Override
	public String timeZone() {
		return "UTC offset";
	}

	@Override
	public String unit() {
		return "Unit";
	}

	@Override
	public String unitOfMeasurement() {
		return "Unit of Measurement";
	}

	@Override
	public String uri() {
		return "URI";
	}

	@Override
	public String uriSyntaxNotValidDialogMessage(final String uri) {
		return "The entered URI \"" + uri + "\" is syntactically not correct.";
	}

	@Override
	public String url() {
		return "URL";
	}

	@Override
	public String version()
	{
		return "Version";
	}

	@Override
	public String waitForParseResultsLabel() {
		return "Testing evaluation pattern for column...";
	}

	@Override
	public String warningDialogTitle() {
		return "Warning";
	}

	@Override
	public String year() {
		return "Year";
	}

}
