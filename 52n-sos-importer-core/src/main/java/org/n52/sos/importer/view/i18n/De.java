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
 * This class contains all String used by the GUI in German
 */
public class De extends Lang{
	
	private final static Locale locale = Locale.GERMAN;
	
	/**
	 * @return Back
	 */
	public String backButtonLabel() {
		return "Zurück";
	}
	
	/**
	 * @return Error
	 */
	public String errorDialogTitle() {
		return "Fehler";
	}

	/**
	 * @return Do you really want to exit?\n
	 */
	public String exitDialogQuestion() { 
		return "Wollen Sie das Programm wirklich beenden?\n";
	}

	/**
	 * @return Exit
	 */
	public String exitDialogTitle() { return "Beenden"; }

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
		return "Datei";
	}

	/**
	 * @return Finish
	 */
	public String finishButtonLabel() {
		return "Beenden";
	}

	/**
	 * @return Info
	 */
	public String infoDialogTitle() {
		return "Information";
	}

	/**
	 * @return measured value
	 */
	public String measuredValue() {
		return "Messwert";
	}

	/**
	 * @return Next
	 */
	public String nextButtonLabel() {
		return "Weiter";
	}

	/**
	 * @return Decimal separator
	 */
	public String numValuePanelDecimalSeparator() {
		return "Dezimal-Separator";
	}

	/**
	 * @return	Thousands separator
	 */
	public String numValuePanelThousandsSeparator() {
		return "Tausender-Trennzeichen";
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
		return "Pfad";
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
		return "Leerzeichen";
	}
	
	public String step() {
		return "Schritt";
	}

	/**
	 * @return Browse
	 */
	public String step1BrowseButton() {
		return "Wähle";
	}

	/**
	 * @return Step 1: Choose CSV file
	 */
	public String step1Description() {
		return "Schritt 1: Wählen Sie die CSV-Datei aus";
	}

	/**
	 * @return CSV File
	 */
	public String step1File() {
		return "CSV-Datei";
	}

	/**
	 * @return Column separator
	 */
	public String step2ColumnSeparator() {
		return "Spalten-Trenner";
	}

	/**
	 * @return Comment indicator
	 */
	public String step2CommentIndicator() {
		return "Kommentar-Indikator";
	}

	/**
	 * @return Step 2: Import CSV file
	 */
	public String step2Description() {
		return this.step() + " 2: CSV-Datei importieren";
	}

	/**
	 * @return First Line with data
	 */
	public String step2FirstLineWithData() {
		return "Erste Zeile mit Daten";
	}

	/**
	 * @return Parse Header
	 */
	public String step2ParseHeader() {
		return "Kopfzeile auswerten";
	}

	/**
	 * @return Text qualifier
	 */
	public String step2TextQualifier() {
		return "Text-Qualifier";
	}

	/**
	 * @return Step 3a: Choose Metadata for the selected column
	 */
	public String step3aDescription() {
		return this.step() + " 3a: Wählen Sie die Metainformationen für die aktuelle Spalte";
	}

	/**
	 * @return Step 3b: Choose metadata for rows
	 */
	public String step3bDescription() {
		return this.step() + " 3b: Wählen Sie die Metainformationen für die aktuelle Zeile";
	}

	/**
	 * @return Date & Time
	 */
	public String step3ColTypeDateTime() {
		return "Datum & Zeit";
	}

	/**
	 * @return Do not export
	 */
	public String step3ColTypeDoNotExport() {
		return "Nicht exportieren";
	}

	/**
	 * @return Measured Value
	 */
	public String step3ColTypeMeasuredValue() {
		return this.measuredValue();
	}

	/**
	 * @return Undefined
	 */
	public String step3ColTypeUndefined() {
		return "Unbekannt";
	}

	/**
	 * @return Combination
	 */
	public String step3DateAndTimeCombination() {
		return "Kombination";
	}

	/**
	 * @return UNIX time
	 */
	public String step3DateAndTimeUnixTime() {
		return "UNIX-Zeit";
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
		return this.step() + " 4a: Kläre Datum & Zeit Unklarheit";
	}

	/**
	 * @return Date and Time are already set for this <code>En.measuredValue()</code>.
	 * @see {@link org.n52.sos.importer.view.i18n.En.measuredValue()}
	 */
	public String step4aInfoDateAndTime() {
		return "Datum und Zeit sind schon gesetzt für diesen " + this.measuredValue() + ".";
	}

	/**
	 * @param element 
	 * @return This is not a <code>En.measuredValue()</code>.
	 * @see {@link org.n52.sos.importer.view.i18n.En.measuredValue()}
	 */
	public String step4aInfoMeasuredValue() {
		return "Dies ist kein " + this.measuredValue() + ".";
	}

	/**
	 * @param stringReplacer
	 * @return Select all measured value <code>Constants.STRING_REPLACER</code>s where the marked Date & Time group corresponds to.
	 */
	public String step4aModelDescription() {
		return "Wähle alle " + this.measuredValue() + " " + Constants.STRING_REPLACER + "n " +
				", die zur markierten Datum-Zeit-Gruppe gehören.";
	}

	/**
	 * @return Step 4b: Solve ambiguities
	 */
	public String step4bDescription() {
		return this.step() + " 4b: Käre Unklarheiten";
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
	
	/**
	 * @return Select all measured value <code>Constants.STRING_REPLACER</code>s where the marked <code>Constants.STRING_REPLACER</code> <code>Constants.STRING_REPLACER</code> corresponds to.
	 */
	public String step4bModelDescription() {
		return "Wähle alle " + this.measuredValue() + " " + Constants.STRING_REPLACER + "n " +
				", die zur markierten " + Constants.STRING_REPLACER + " " + Constants.STRING_REPLACER + " gehören.";
	}

	/**
	 * @return Step 5a: Complete time data
	 */
	public String step5aDescription() {
		return this.step() + " 5a: Zeit-Informationen vervollständigen";
	}

	/**
	 * @return Complete missing information for the marked date and time.
	 */
	public String step5aModelDescription() {
		return "Vervollständige die fehlenden Zeit-Datum-Informationen für die markierten Elemente.";
	}

	/**
	 * @return Step 5c: Complete position data
	 */
	public String step5cDescription() {
		return this.step() + " 5c: Positionsdaten vervollständigen";
	}

	/**
	 * @return Complete missing information for the marked position.
	 */
	public String step5cModelDescription() {
		return "Vervollständige die fehlenden Positions-Informationen für die markierten Elemente.";
	}

	/**
	 * @return Step 6a: Add missing dates and times
	 */
	public String step6aDescription() {
		return this.step() + " 6a: Fehlende Zeiten und Daten hinzufügen";
	}

	/**
	 * @return Step 6b: Add missing metadata
	 */
	public String step6bDescription() {
		return this.step() + " 6b: Fehlende Metainformationen hinzufügen";
	}

	/**
	 * Replacements: Resource &rarr; Orientation
	 * @return &lt;html&gt;What is the &lt;u&gt;<code>Constants.STRING_REPLACER</code>&lt;/u&gt; for the marked measured value <code>Constants.STRING_REPLACER</code>?&lt;/html&gt;
	 */
	public String step6bModelDescription() {
		return "<html>Welche(r/s) <u>" + 
				Constants.STRING_REPLACER + 
				"</u> gehört zur markierten " + this.measuredValue() + " " +
				Constants.STRING_REPLACER + 
				"?</html>";
	}

	/**
	 * @return Step 6b (Special): Add missing sensors
	 */
	public String step6bSpecialDescription() {
		return this.step() + " 6b (Spezial): Fehlende " + this.sensor() + "en hinzufügen";
	}

	/**
	 * @return What is the sensor for
	 */
	public String step6bSpecialModelDescription() {
		return "Welches ist der " + this.sensor() + " für";
	}

	/**
	 * @return Step 6c: Add missing positions
	 */
	public String step6cDescription() {
		return this.step() + " 6c: Fehlende " + this.position() + "s-Angaben hinzufügen";
	}

	/**
	 * @return What is the position of
	 */
	public String step6cModelDescription() {
		return "Wie ist die " + this.position() + "s-Angabe von";
	}

	/**
	 * @return Step 7: Choose Sensor Observation Service
	 */
	public String step7Description() {
		return this.step() + " 7: Geben Sie den " + this.sos() + " an";
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
		return this.step() + " 8: Registriere " + this.sensor() + "en und lade " + this.observation() + "en in den " + this.sos();
	}

	public String observation() {
		return "Beobachtung";
	}

	/**
	 * @param i
	 * @return Errors: <code>i</code>
	 */
	public String step8ErrorLable(int i) {
		return "Fehler: " + i;
	}

	/**
	 * @param i
	 * @return Insert <code>i</code> Observations...
	 */
	public String step8InsertObservationLabel(int i) {
		return "Füge " + i + " " + this.observation() + "en hinzu...";
	}

	/**
	 * @return Check log file
	 */
	public String step8LogFileLabel() {
		return "Prüfen Sie die Logdatei";
	}

	/**
	 * @param i
	 * @return Register <code>i</code> Sensors...
	 */
	public String step8RegisterSensorLabel(int i) {
		return "Registriere " + i + this.sensor() + "en...";
	}

	/**
	 * @param i
	 * @return Successful: <code>i</code>
	 */
	public String step8SuccessLabel(int i) {
		return "Erfolgreich: " + i;
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
		return "Warnung";
	}

	@Override
	public Locale getLocale() {
		return De.locale;
	}

	@Override
	protected String step4bInfoResourceAlreadSetText() {
		return " schon gesetzt für ";
	}

	@Override
	public String step1SelectLanguage() {
		return "Bitte wählen Sie die Sprache aus";
	}

}
