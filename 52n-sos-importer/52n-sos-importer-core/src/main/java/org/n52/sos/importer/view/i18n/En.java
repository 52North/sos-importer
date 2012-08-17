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
package org.n52.sos.importer.view.i18n;


import java.io.File;
import java.util.Locale;

import org.n52.sos.importer.Constants;

/**
 * @author e.h.juerrens@52north.org
 * This class contains all String used by the GUI in English
 */
public class En extends Lang{
	
	private final static Locale locale = Locale.ENGLISH;
	
	@Override
	public String altitude() {
		return "Altitude / Height";
	}
	
	@Override
	public String and() {
		return "and";
	}

	public String backButtonLabel() {
		return "Back";
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

	public String errorDialogTitle() {
		return "Error";
	}

	@Override
	public String example() {
		return "Example";
	}

	public String exitDialogQuestion() { 
		return "Do you really want to exit?\n";
	}

	public String exitDialogTitle() { return "Exit"; }
	
	public String featureOfInterest() {
		return "Feature of Interest";
	}

	public String file() {
		return "file";
	}

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
		return "The " + this.altitude() + " has to be a decimal number.";
	}

	@Override
	public String hours() {
		return "Hours";
	}

	public String infoDialogTitle() {
		return "Information";
	}

	@Override
	public String latitudeDialogDecimalValue() {
		return "The " + this.latitudeNorthing() + " can only be a decimal number so far.";
	}

	@Override
	public String latitudeNorthing() {
		return "Latitude / Northing";
	}
	
	@Override
	public String longitudeDialogDecimalValue() {
		return "The " + this.longitudeEasting() + " can only be a decimal number so far.";
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

	public String sensor() {
		return "Sensor";
	}

	@Override
	public String spaceString() {
		return "Space";
	}


	public String step() { return "Step"; }

	public String step1BrowseButton() {
		return "Select";
	}

	public String step1Description() {
		return this.step() + " 1: Choose CSV file";
	}

	public String step1File() {
		return "CSV File";
	}

	@Override
	public String step1InstructionLabel() {
		return "Please select the CSV file";
	}

	@Override
	public String step1Introduction() {
		return "Introduction";
	}

	public String step1SelectLanguage() {
		return "Change language";
	}

	public String step2ColumnSeparator() {
		return "Column separator";
	}

	public String step2CommentIndicator() {
		return "Comment indicator";
	}

	@Override
	public String step2DataPreviewLabel() {
		return "CSV-Data-Preview";
	}

	public String step2DecimalSeparator() {
		return "Decimal separator";
	}

	public String step2Description() {
		return this.step() + " 2: Define CSV File Metadata";
	}

	public String step2FirstLineWithData() {
		return "Ignore data until line";
	}

	public String step2ParseHeader() {
		return "Interpret Header";
	}

	public String step2TextQualifier() {
		return "Text qualifier";
	}

	public String step3aDescription() {
		return this.step() + " 3a: Choose Metadata for the selected column";
	}
	
	@Override
	public String step3aMeasureValueColMissingDialogMessage() {
		return "You have to specify at least one " + this.measuredValue() + " column!";
	}

	@Override
	public String step3aMeasureValueColMissingDialogTitle() {
		return this.measuredValue() + " column missing";
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
	public String step3aParseTestNFailed(int n) {
		return n + " values could not be interpreted.";
	}

	@Override
	public String step3aSelectedColTypeUndefinedMsg() {
		return "The type for this column is \"" + 
				this.step3ColTypeUndefined() + 
				"\".\nPlease select one.\nChoose \"" +
				this.step3ColTypeDoNotExport() + 
				"\" for skipping it.";
	}

	@Override
	public String step3aSelectedColTypeUndefinedTitle() {
		return "Column Type is \"" + 
				this.step3ColTypeUndefined() + 
				"\"";
	}

	public String step3bDescription() {
		return this.step() + " 3b: Choose metadata for rows";
	}

	public String step3ColTypeDateTime() {
		return "Date & Time";
	}

	public String step3ColTypeDoNotExport() {
		return "Do not export";
	}

	public String step3ColTypeMeasuredValue() {
		return "Measured Value";
	}

	public String step3ColTypeUndefined() {
		return "Undefined";
	}

	public String step3DateAndTimeCombination() {
		return "Combination";
	}

	public String step3DateAndTimeUnixTime() {
		return "UNIX time";
	}

	public String step3MeasuredValBoolean() {
		return "Boolean";
	}

	public String step3MeasuredValCount() {
		return "Count";
	}

	public String step3MeasuredValNumericValue() {
		return "Numeric Value";
	}

	public String step3MeasuredValText() {
		return "Text (via CategoryObservation)";
	}

	public String step3PositionCombination() {
		return this.step3DateAndTimeCombination();
	}

	public String step4aDescription() {
		return this.step() + " 4a: Solve Date & Time ambiguities";
	}

	public String step4aInfoDateAndTime() {
		return "Date and Time are already set for this " + this.measuredValue() + ".";
	}

	public String step4aInfoMeasuredValue() {
		return "This is not a " + this.measuredValue() + ".";
	}

	public String step4aModelDescription() {
		return "Select all " + this.measuredValue() + " " + Constants.STRING_REPLACER + "s " +
				"where the marked Date & Time group corresponds to.";
	}

	public String step4bDescription() {
		return this.step() + " 4b: Solve ambiguities";
	}

	public String step4bInfoNotMeasuredValue() {
		return this.step4aInfoMeasuredValue();
	}

	@Override
	protected String step4bInfoResourceAlreadySetText() {
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

	public String step5aDescription() {
		return this.step() + " 5a: Complete time data";
	}

	public String step5aModelDescription() {
		return "Please define the timezone for the marked date and time.";
	}

	public String step5cDescription() {
		return this.step() + " 5c: Complete position data";
	}

	public String step5cModelDescription() {
		return "Complete missing information for the marked position.";
	}

	public String step6aDescription() {
		return this.step() + " 6a: Add missing dates and times";
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

	public String step6bDescription() {
		return this.step() + " 6b: Add missing metadata";
	}

	public String step6bModelDescription() {
		return "<html>What is the <u>" + 
				Constants.STRING_REPLACER + 
				"</u> for the marked " + this.measuredValue() + " " + 
				Constants.STRING_REPLACER + 
				"?</html>";
	}

	@Override
	public String step6bSelectColumnsLabel() {
		return "Please select the column(s) to generate the name.";
	}

	public String step6bSpecialDescription() {
		return this.step() + " 6b (Special): Add missing " + this.sensor() + "s";
	}

	public String step6bSpecialModelDescription() {
		return "What is the " + this.sensor().toLowerCase(En.locale) + " for";
	}

	@Override
	public String step6bURIInstructions() {
		return "Please provide a URI or a prefix if using the name as part of the URI.";
	}

	@Override
	public String step6bUseNameAfterPrefix() {
		return "Use Name after prefix?";
	}

	public String step6cDescription() {
		return this.step() + " 6c: Add missing " + this.position().toLowerCase(En.locale) + "s";
	}

	@Override
	public String step6cInfoToolName() {
		return "Set Position";
	}

	@Override
	public String step6cInfoToolTooltip() {
		 return "Set the position by clicking on the map";
	}

	public String step6cModelDescription() {
		return "What is the " + this.position().toLowerCase(En.locale) + " of";
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
	public String step7ConfigDirNotDirOrWriteable(String folder) {
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

	public String step7Description() {
		return this.step() + " 7: Final Configuration";
	}

	@Override
	public String step7DirectImport() {
		return "Directly import data during next step";
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
	public String step7OfferingNameNotValid(String offeringName) {
		return String.format("The given offering \"%s\" is not valid. It should match XML-NCName specification.", offeringName);
	}

	@Override
	public String step7SaveConfig() {
		return "Save configuration to XML file";
	}

	@Override
	public String step7SOSConncetionStart(String strURL) {
		return "To start connection testing to URL" +
				"\n\"" + strURL + "\"\n" +
				"select YES. For changing values select NO.";
	}

	public String step7SOSconnectionFailed(String strURL,
			int responseCode) {
		return "Could not connect to " + this.sos() + ": "
        		+ strURL + 
        		". HTTP Response Code: " 
        		+ responseCode;
	}

	public String step7SOSConnectionFailedException(String strURL,
			String message,
			int readTimeoutSeconds,
			int connectTimeoutSeconds) {
		return "Connection to " + this.sos() + 
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
	public String step8ConfigFileButton() {
		return "Open";
	}

	@Override
	public String step8ConfigurationFileInstructions() {
		return "For taking a look at the generated configuration file, please click button.";
	}

	public String step8Description() {
		return this.step() + " 8: Final Step - Summary of the Results";
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

	public String step8ErrorLable(int i) {
		return "Errors: " + i;
	}

	@Override
	public String step8FeederJarNotFound(String expectedAbsolutePathToFeederJar) {
		return String.format("Could not find jar file!\nPlease place it there:\n\"%s\"",
				expectedAbsolutePathToFeederJar);
	}

	public String step8InsertObservationLabel(int i) {
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

	public String step8RegisterSensorLabel(int i) {
		return "Register " + i + " " + this.sensor() + "(s)...";
	}

	@Override
	public String step8SaveModelFailed(File f, String exceptionText) {
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

	public String step8SuccessLabel(int i) {
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

	public String unitOfMeasurement() {
		return "Unit of Measurement";
	}

	@Override
	public String uri() {
		return "URI";
	}

	@Override
	public String uriSyntaxNotValidDialogMessage(String uri) {
		return "The entered URI \"" + uri + "\" is syntactically not correct.";
	}

	@Override
	public String url() {
		return "URL";
	}

	@Override
	public String waitForParseResultsLabel() {
		return "Testing evaluation pattern for column...";
	}

	public String warningDialogTitle() {
		return "Warning";
	}

	@Override
	public String year() {
		return "Year";
	}

}
