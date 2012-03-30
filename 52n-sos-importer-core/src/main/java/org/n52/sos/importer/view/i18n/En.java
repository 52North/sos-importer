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

import java.util.Locale;

import org.n52.sos.importer.view.utils.Constants;





/**
 * @author e.h.juerrens@52north.org
 * This class contains all String used by the GUI in English
 */
public class En extends Lang{
	
	private final static Locale locale = Locale.ENGLISH;
	
	/**
	 * @return Back
	 */
	public String backButtonLabel() {
		return "Back";
	}
	
	/**
	 * @return Error
	 */
	public String errorDialogTitle() {
		return "Error";
	}

	/**
	 * @return Do you really want to exit?\n
	 */
	public String exitDialogQuestion() { 
		return "Do you really want to exit?\n";
	}

	/**
	 * @return Exit
	 */
	public String exitDialogTitle() { return "Exit"; }

	/**
	 * @return Feature of Interest
	 */
	public String featureOfInterest() {
		return "Feature of Interest";
	}

	/**
	 * @return file
	 */
	public String file() {
		return "file";
	}

	/**
	 * @return Finish
	 */
	public String finishButtonLabel() {
		return "Finish";
	}

	/**
	 * @return Info
	 */
	public String infoDialogTitle() {
		return "Info";
	}

	/**
	 * @return measured value
	 */
	public String measuredValue() {
		return "measured value";
	}

	/**
	 * @return Next
	 */
	public String nextButtonLabel() {
		return "Next";
	}

	/**
	 * @return Decimal separator
	 */
	public String numValuePanelDecimalSeparator() {
		return "Decimal separator";
	}

	/**
	 * @return	Thousands separator
	 */
	public String numValuePanelThousandsSeparator() {
		return "Thousands separator";
	}

	/**
	 * @return Observed Property
	 */
	public String observedProperty() {
		return "Observed Property";
	}

	/**
	 * @return path
	 */
	public String path() {
		return "path";
	}

	/**
	 * @return Position
	 */
	public String position() {
		return "Position";
	}

	/**
	 * @return Sensor
	 */
	public String sensor() {
		return "Sensor";
	}

	/**
	 * @return Space
	 */
	public String spaceString() {
		return "Space";
	}

	public String step() { return "Step"; }
	
	/**
	 * @return Browse
	 */
	public String step1BrowseButton() {
		return "Select data file";
	}

	/**
	 * @return Step 1: Choose CSV file
	 */
	public String step1Description() {
		return this.step() + " 1: Choose CSV file";
	}

	/**
	 * @return CSV File
	 */
	public String step1File() {
		return "CSV File";
	}

	/**
	 * @return Column separator
	 */
	public String step2ColumnSeparator() {
		return "Column separator";
	}

	/**
	 * @return Comment indicator
	 */
	public String step2CommentIndicator() {
		return "Comment indicator";
	}

	/**
	 * @return Step 2: Import CSV file
	 */
	public String step2Description() {
		return this.step() + " 2: Import CSV file";
	}

	/**
	 * @return First Line with data
	 */
	public String step2FirstLineWithData() {
		return "First Line with data";
	}

	/**
	 * @return Parse Header
	 */
	public String step2ParseHeader() {
		return "Parse Header";
	}

	/**
	 * @return Text qualifier
	 */
	public String step2TextQualifier() {
		return "Text qualifier";
	}

	/**
	 * @return Step 3a: Choose Metadata for the selected column
	 */
	public String step3aDescription() {
		return this.step() + " 3a: Choose Metadata for the selected column";
	}

	/**
	 * @return Step 3b: Choose metadata for rows
	 */
	public String step3bDescription() {
		return this.step() + " 3b: Choose metadata for rows";
	}

	/**
	 * @return Date & Time
	 */
	public String step3ColTypeDateTime() {
		return "Date & Time";
	}

	/**
	 * @return Do not export
	 */
	public String step3ColTypeDoNotExport() {
		return "Do not export";
	}

	/**
	 * @return Measured Value
	 */
	public String step3ColTypeMeasuredValue() {
		return "Measured Value";
	}

	/**
	 * @return Undefined
	 */
	public String step3ColTypeUndefined() {
		return "Undefined";
	}

	/**
	 * @return Combination
	 */
	public String step3DateAndTimeCombination() {
		return "Combination";
	}

	/**
	 * @return UNIX time
	 */
	public String step3DateAndTimeUnixTime() {
		return "UNIX time";
	}
	
	/**
	 * @return Boolean
	 */
	public String step3MeasuredValBoolean() {
		return "Boolean";
	}

	/**
	 * @return Count
	 */
	public String step3MeasuredValCount() {
		return "Count";
	}

	/**
	 * @return Numeric Value
	 */
	public String step3MeasuredValNumericValue() {
		return "Numeric Value";
	}

	/**
	 * @return Text
	 */
	public String step3MeasuredValText() {
		return "Text";
	}

	/**
	 * @return {@linkplain org.n52.sos.importer.view.i18n.En.step3DateAndTimeCombination()}
	 * 	<br />:= Combination
	 */
	public String step3PositionCombination() {
		return this.step3DateAndTimeCombination();
	}

	/**
	 * @return Step 4a: Solve Date & Time ambiguities
	 */
	public String step4aDescription() {
		return this.step() + " 4a: Solve Date & Time ambiguities";
	}

	/**
	 * @return Date and Time are already set for this <code>En.measuredValue()</code>.
	 * @see {@link org.n52.sos.importer.view.i18n.En.measuredValue()}
	 */
	public String step4aInfoDateAndTime() {
		return "Date and Time are already set for this " + this.measuredValue() + ".";
	}

	/**
	 * @param element 
	 * @return This is not a <code>En.measuredValue()</code>.
	 * @see {@link org.n52.sos.importer.view.i18n.En.measuredValue()}
	 */
	public String step4aInfoMeasuredValue() {
		return "This is not a " + this.measuredValue() + ".";
	}

	/**
	 * @param stringReplacer
	 * @return Select all measured value <code>Constants.STRING_REPLACER</code>s where the marked Date & Time group corresponds to.
	 */
	public String step4aModelDescription() {
		return "Select all " + this.measuredValue() + " " + Constants.STRING_REPLACER + "s " +
				"where the marked Date & Time group corresponds to.";
	}

	/**
	 * @return Step 4b: Solve ambiguities
	 */
	public String step4bDescription() {
		return this.step() + " 4b: Solve ambiguities";
	}

	/**
	 * @return This is not a <code>En.measuredValue()</code>.
	 * @see {@link org.n52.sos.importer.view.i18n.En.measuredValue()}
	 * @see {@link org.n52.sos.importer.view.i18n.En.step4aInfoMeasuredValue()}
	 * 
	 */
	public String step4bInfoNotMeasuredValue() {
		return this.step4aInfoMeasuredValue();
	}

	@Override
	protected String step4bInfoResourceAlreadSetText() {
		return " already set for this ";
	}


	/**
	 * @return Select all measured value <code>Constants.STRING_REPLACER</code>s where the marked <code>Constants.STRING_REPLACER</code> <code>Constants.STRING_REPLACER</code> corresponds to.
	 */
	public String step4bModelDescription() {
		return "Select all measured value " + Constants.STRING_REPLACER + "s " +
				"where the marked " + Constants.STRING_REPLACER + " " + Constants.STRING_REPLACER + " corresponds to.";
	}

	/**
	 * @return Step 5a: Complete time data
	 */
	public String step5aDescription() {
		return this.step() + " 5a: Complete time data";
	}

	/**
	 * @return Complete missing information for the marked date and time.
	 */
	public String step5aModelDescription() {
		return "Complete missing information for the marked date and time.";
	}

	/**
	 * @return Step 5c: Complete position data
	 */
	public String step5cDescription() {
		return this.step() + " 5c: Complete position data";
	}

	/**
	 * @return Complete missing information for the marked position.
	 */
	public String step5cModelDescription() {
		return "Complete missing information for the marked position.";
	}

	/**
	 * @return Step 6a: Add missing dates and times
	 */
	public String step6aDescription() {
		return this.step() + " 6a: Add missing dates and times";
	}

	/**
	 * @return Step 6b: Add missing metadata
	 */
	public String step6bDescription() {
		return this.step() + " 6b: Add missing metadata";
	}

	/**
	 * Replacements: Resource &rarr; Orientation
	 * @return &lt;html&gt;What is the &lt;u&gt;<code>Constants.STRING_REPLACER</code>&lt;/u&gt; for the marked measured value <code>Constants.STRING_REPLACER</code>?&lt;/html&gt;
	 */
	public String step6bModelDescription() {
		return "<html>What is the <u>" + 
				Constants.STRING_REPLACER + 
				"</u> for the marked " + this.measuredValue() + " " + 
				Constants.STRING_REPLACER + 
				"?</html>";
	}

	/**
	 * @return Step 6b (Special): Add missing sensors
	 */
	public String step6bSpecialDescription() {
		return this.step() + " 6b (Special): Add missing " + this.sensor() + "s";
	}

	/**
	 * @return What is the sensor for
	 */
	public String step6bSpecialModelDescription() {
		return "What is the " + this.sensor().toLowerCase(En.locale) + " for";
	}

	/**
	 * @return Step 6c: Add missing positions
	 */
	public String step6cDescription() {
		return this.step() + " 6c: Add missing " + this.position().toLowerCase(En.locale) + "s";
	}

	/**
	 * @return What is the position of
	 */
	public String step6cModelDescription() {
		return "What is the " + this.position().toLowerCase(En.locale) + " of";
	}

	/**
	 * @return Step 7: Choose Sensor Observation Service
	 */
	public String step7Description() {
		return this.step() + " 7: Choose " + this.sos();
	}

	/**
	 * @param strURL
	 * @param responseCode
	 * @return Could not connect to Sensor Observation Service: <code>strURL</code> . HTTP Response Code: <code>responseCode</code>
	 */
	public String step7SOSconnectionFailed(String strURL,
			int responseCode) {
		return "Could not connect to " + this.sos() + ": "
        		+ strURL + 
        		". HTTP Response Code: " 
        		+ responseCode;
	}

	/**
	 * @param strURL
	 * @param message
	 * @return Connection to Sensor Observation Service <code>strURL</code> failed. Reason: <code>message</code>
	 */
	public String step7SOSConnectionFailedException(String strURL,
			String message) {
		return "Connection to " + this.sos() + " " + strURL + " failed. Reason: " + message;
	}

	/**
	 * @return Step 8: Register Sensors and Insert Observations into Sensor Observation Service
	 */
	public String step8Description() {
		return this.step() + " 8: Register " + this.sensor() + "s and Insert " + this.observation() + "s into " + this.sos();
	}
	
	public String observation() {
		return "Observation";
	}

	/**
	 * @param i
	 * @return Errors: <code>i</code>
	 */
	public String step8ErrorLable(int i) {
		return "Errors: " + i;
	}

	/**
	 * @param i
	 * @return Insert <code>i</code> Observations...
	 */
	public String step8InsertObservationLabel(int i) {
		return "Insert " + i + " Observations...";
	}

	/**
	 * @return Check log file
	 */
	public String step8LogFileLabel() {
		return "Check log file";
	}

	/**
	 * @param i
	 * @return Register <code>i</code> Sensors...
	 */
	public String step8RegisterSensorLabel(int i) {
		return "Register " + i + " " + this.sensor() + "(s)...";
	}

	/**
	 * @param i
	 * @return Successful: <code>i</code>
	 */
	public String step8SuccessLabel(int i) {
		return "Successful: " + i;
	}

	/**
	 * @return Unit of Measurement
	 */
	public String unitOfMeasurement() {
		return "Unit of Measurement";
	}

	/**
	 * @return Warning
	 */
	public String warningDialogTitle() {
		return "Warning";
	}

	@Override
	public Locale getLocale() {
		return En.locale;
	}

	/**
	 * @return Please select the language
	 */
	public String step1SelectLanguage() {
		return "Please select the language";
	}

	/**
	 * @return Day
	 */
	@Override
	public String day() {
		return "Day";
	}

	@Override
	public String hours() {
		return "Hours";
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
	public String seconds() {
		return "Seconds";
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
	public String year() {
		return "Year";
	}

	@Override
	public String epsgCode() {
		return "EPSG-Code";
	}

	@Override
	public String referenceSystem() {
		return "Reference System";
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
	public String heightWarningDialogDecimalNumber() {
		return "The " + this.altitude() + " has to be a decimal number.";
	}

	@Override
	public String altitude() {
		return "Altitude / Height";
	}

	@Override
	public String unit() {
		return "Unit";
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
	public String longitudeEasting() {
		return "Longitude / Easting";
	}

	@Override
	public String longitudeDialogDecimalValue() {
		return "The " + this.longitudeEasting() + " can only be a decimal number so far.";
	}

	@Override
	public String group() {
		return "Group";
	}

	@Override
	public String example() {
		return "Example";
	}

	@Override
	public String format() {
		return "Format";
	}

	@Override
	public String error() {
		return "Error";
	}

	@Override
	public String step3aParseTestAllOk() {
		return "All values parseable";
	}

	@Override
	public String step3aParseTest1Failed() {
		return "1 value not parseable";
	}

	@Override
	public String step3aParseTestNFailed(int n) {
		return n + " values not parseable";
	}

	@Override
	public String step3aMeasureValueColMissingDialogTitle() {
		return this.measuredValue() + " column missing";
	}

	@Override
	public String step3aMeasureValueColMissingDialogMessage() {
		return "You have to specify at least one " + this.measuredValue() + " column!";
	}

	@Override
	public String and() {
		return "and";
	}

	@Override
	public String sosURL() {
		return "SOS-URL (incl. endpoint, e.g. ../sos)";
	}

	@Override
	public String step3aSelectedColTypeUndefinedMsg() {
		return "The type for this column is \"" + 
				this.step3ColTypeUndefined() + 
				"\".\nPlease select one.nChose \"" +
				this.step3ColTypeDoNotExport() + 
				"\" for skipping it.";
	}

	@Override
	public String step3aSelectedColTypeUndefinedTitle() {
		return "Column Type is \"" + 
				this.step3ColTypeUndefined() + 
				"\"";
	}

}
