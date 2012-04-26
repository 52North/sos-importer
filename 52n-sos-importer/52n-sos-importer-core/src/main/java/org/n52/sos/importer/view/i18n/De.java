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

import org.n52.sos.importer.view.utils.Constants;

/**
 * @author e.h.juerrens@52north.org
 * This class contains all String used by the GUI in German
 */
public class De extends Lang{
	
	private final static Locale locale = Locale.GERMAN;
	
	@Override
	public String altitude() {
		return "Höhe";
	}
	
	@Override
	public String and() {
		return "und";
	}

	public String backButtonLabel() {
		return "Zurück";
	}

	@Override
	public String column() {
		return "Spalte";
	}

	@Override
	public String date() {
		return "Datum";
	}

	@Override
	public String day() {
		return "Tag";
	}

	@Override
	public String epsgCode() {
		return "EPSG-Kennzahl";
	}

	@Override
	public String epsgCodeWarningDialogNaturalNumber() {
		return "Die " + this.epsgCode() + " muss eine natürliche Zahl sein.";
	}

	@Override
	public String epsgCodeWarningDialogOutOfRange() {
		return "Die " + this.epsgCode() + " sollte größer als 0 und kleiner als 32767 sein.";
	}

	@Override
	public String error() {
		return "Fehler";
	}

	public String errorDialogTitle() {
		return "Fehler";
	}

	@Override
	public String example() {
		return "Beispiel";
	}

	public String exitDialogQuestion() { 
		return "Wollen Sie das Programm wirklich beenden?\n";
	}

	public String exitDialogTitle() { return "Beenden"; }

	public String featureOfInterest() {
		return "Feature of Interest";
	}

	public String file() {
		return "Datei";
	}

	public String finishButtonLabel() {
		return "Beenden";
	}
	
	@Override
	public String format() {
		return "Format";
	}

	@Override
	public Locale getLocale() {
		return De.locale;
	}

	@Override
	public String group() {
		return "Gruppe";
	}

	@Override
	public String heightWarningDialogDecimalNumber() {
		return "Die " + this.altitude() + " kann zur Zeit nur als Dezimalzahl angegeben werden.";
	}

	@Override
	public String hours() {
		return "Stunden";
	}

	public String infoDialogTitle() {
		return "Information";
	}

	@Override
	public String latitudeDialogDecimalValue() {
		return "Der " + this.latitudeNorthing() + " kann zur Zeit nur als Dezimalzahl angegeben werden.";
	}

	@Override
	public String latitudeNorthing() {
		return "Breitengrad / Hochwert";
	}

	@Override
	public String longitudeDialogDecimalValue() {
		return "Der " + this.longitudeEasting() + " kann zur Zeit nur als Dezimalzahl angegeben werden.";
	}

	@Override
	public String longitudeEasting() {
		return "Längengrad / Rechtswert";
	}

	public String measuredValue() {
		return "Messwert";
	}

	@Override
	public String minutes() {
		return "Minuten";
	}

	@Override
	public String month() {
		return "Monat";
	}

	public String nextButtonLabel() {
		return "Weiter";
	}

	public String numValuePanelDecimalSeparator() {
		return "Dezimal-Separator";
	}

	public String numValuePanelThousandsSeparator() {
		return "Tausender-Trennzeichen";
	}

	public String observation() {
		return "Beobachtung";
	}

	public String observedProperty() {
		return "Observed Property";
	}
	
	public String path() {
		return "Pfad";
	}

	public String position() {
		return "Position";
	}

	@Override
	public String referenceSystem() {
		return "Referenzsystem";
	}

	@Override
	public String row() {
		return "Zeile";
	}

	@Override
	public String seconds() {
		return "Sekunden";
	}

	public String sensor() {
		return "Sensor";
	}

	@Override
	public String sosURL() {
		return "SOS-Web-Adresse (inkl. Endpoint, z.B. ../sos)";
	}

	public String spaceString() {
		return "Leerzeichen";
	}

	public String step() {
		return "Schritt";
	}

	public String step1BrowseButton() {
		return "Datendatei auswählen";
	}

	public String step1Description() {
		return "Schritt 1: Wählen Sie die CSV-Datei aus";
	}
	
	public String step1File() {
		return "CSV-Datei";
	}

	@Override
	public String step1SelectLanguage() {
		return "Bitte wählen Sie die Sprache aus";
	}

	public String step2ColumnSeparator() {
		return "Spalten-Trenner";
	}

	public String step2CommentIndicator() {
		return "Kommentar-Indikator";
	}

	@Override
	public String step2DataPreviewLabel() {
		return "Datendatei-Vorschau";
	}

	public String step2Description() {
		return this.step() + " 2: CSV-Datei importieren";
	}

	public String step2FirstLineWithData() {
		return "Erste Zeile mit Daten";
	}

	public String step2ParseHeader() {
		return "Kopfzeile auswerten";
	}

	public String step2TextQualifier() {
		return "Text-Qualifier";
	}

	public String step3aDescription() {
		return this.step() + " 3a: Wählen Sie die Metainformationen für die aktuelle Spalte";
	}

	@Override
	public String step3aMeasureValueColMissingDialogMessage() {
		return "Es muss mindestens 1 " + this.measuredValue() + "-Spalte definiert werden!";
	}

	@Override
	public String step3aMeasureValueColMissingDialogTitle() {
		return this.measuredValue() + "-Spalte fehlt!";
	}

	@Override
	public String step3aParseTest1Failed() {
		return "1 Wert nicht einlesbar.";
	}

	@Override
	public String step3aParseTestAllOk() {
		return "Alle Werte einlesbar.";
	}

	@Override
	public String step3aParseTestNFailed(int n) {
		return n + " Werte nicht einlesbar.";
	}

	@Override
	public String step3aSelectedColTypeUndefinedMsg() {
		return "Der Typ für die Spalte ist \"" + 
				this.step3ColTypeUndefined() + 
				"\".\n" +
				"Bitte wählen Sie einen anderen Typ.\n" +
				"Sollten Sie diese Spalte überspringen\n" +
				"(= nicht exportieren) wollen, dann " +
				"wählen Sie bitte als Typ \n\"" +
				this.step3ColTypeDoNotExport() + 
				"\".";
	}

	@Override
	public String step3aSelectedColTypeUndefinedTitle() {
		return "Spalten-Typ ist \"" + 
				this.step3ColTypeUndefined() + 
				"\"";
	}

	public String step3bDescription() {
		return this.step() + " 3b: Wählen Sie die Metainformationen für die aktuelle Zeile";
	}

	public String step3ColTypeDateTime() {
		return "Datum & Zeit";
	}

	public String step3ColTypeDoNotExport() {
		return "Nicht exportieren";
	}

	public String step3ColTypeMeasuredValue() {
		return this.measuredValue();
	}

	public String step3ColTypeUndefined() {
		return "Unbekannt";
	}

	public String step3DateAndTimeCombination() {
		return "Kombination";
	}

	public String step3DateAndTimeUnixTime() {
		return "UNIX-Zeit";
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
		return "Text";
	}

	public String step3PositionCombination() {
		return this.step3DateAndTimeCombination();
	}

	public String step4aDescription() {
		return this.step() + " 4a: Kläre Datum & Zeit Unklarheit";
	}

	public String step4aInfoDateAndTime() {
		return "Datum und Zeit sind schon gesetzt für diesen " + this.measuredValue() + ".";
	}

	public String step4aInfoMeasuredValue() {
		return "Dies ist kein " + this.measuredValue() + ".";
	}

	public String step4aModelDescription() {
		return "Wähle alle " + this.measuredValue() + " " + Constants.STRING_REPLACER + "n " +
				", die zur markierten Datum-Zeit-Gruppe gehören.";
	}

	public String step4bDescription() {
		return this.step() + " 4b: Kläre Unklarheiten";
	}

	public String step4bInfoNotMeasuredValue() {
		return this.step4aInfoMeasuredValue();
	}

	@Override
	protected String step4bInfoResourceAlreadySetText() {
		return " schon gesetzt für ";
	}

	@Override
	public String step4bModelDescription() {
		return "Bitte klicken Sie in die " + 
				Constants.STRING_REPLACER + 
			" (nicht auf die Titel), die die Messwerte enthält, die zu der " +
			"hervorgehobenen " + 
			Constants.STRING_REPLACER +
			"-" +
			Constants.STRING_REPLACER + 
			" gehört. Wenn mehrere Messwert-" +
			Constants.STRING_REPLACER + 
			"n dazugehören, wählen Sie sie mit gedrückter Strg-Taste aus.";
	}

	public String step5aDescription() {
		return this.step() + " 5a: Zeit-Informationen vervollständigen";
	}

	public String step5aModelDescription() {
		return "Vervollständige die fehlenden Zeit-Datum-Informationen für die markierten Elemente.";
	}

	public String step5cDescription() {
		return this.step() + " 5c: Positionsdaten vervollständigen";
	}

	public String step5cModelDescription() {
		return "Vervollständige die fehlenden Positions-Informationen für die markierten Elemente.";
	}

	public String step6aDescription() {
		return this.step() + " 6a: Fehlende Zeiten und Daten hinzufügen";
	}

	@Override
	public String step6aModelDescription() {
		return "<html>Bitte geben Sie <u>Datum und Uhrzeit</u> für alle Messwerte an!</html>";
	}

	public String step6bDescription() {
		return this.step() + " 6b: Fehlende Metainformationen hinzufügen";
	}

	public String step6bModelDescription() {
		return "<html>Welche(r/s) <u>" + 
				Constants.STRING_REPLACER + 
				"</u> gehört zur markierten " + this.measuredValue() + "-" +
				Constants.STRING_REPLACER + 
				"?</html>";
	}

	public String step6bSpecialDescription() {
		return this.step() + " 6b (Spezial): Fehlende " + this.sensor() + "en hinzufügen";
	}

	public String step6bSpecialModelDescription() {
		return "Welches ist der " + this.sensor() + " für";
	}

	public String step6cDescription() {
		return this.step() + " 6c: Fehlende " + this.position() + "s-Angaben hinzufügen";
	}

	public String step6cModelDescription() {
		return "Wie ist die " + this.position() + "s-Angabe von";
	}

	@Override
	public String step7ConfigDirNotDirOrWriteable(String folder) {
		return "Auf das Verzeichnis \n\"" + 
				folder + "\"\n kann nicht zugegriffen werden";
	}

	@Override
	public String step7ConfigFileButton() {
		return "Verzeichnis wählen";
	}

	@Override
	public String step7ConfigFileDialogTitel() {
		return "Bitte das Verzeichnis für die Konfigurations-Datei wählen";
	}

	@Override
	public String step7ConfigFileLabel() {
		return "Konfigurations-Dateiname und -verzeichnis";
	}

	public String step7Description() {
		return this.step() + " 7: Geben Sie den " + this.sos() + " an";
	}

	@Override
	public String step7DirectImport() {
		return "Im nächsten Schritt die Daten in den SOS importieren";
	}

	@Override
	public String step7SaveConfig() {
		return "Konfiguration in XML-Datei speichern";
	}

	public String step7SOSconnectionFailed(String strURL,
			int responseCode) {
		return "Could not connect to " + this.sos() + ": "
        		+ strURL + 
        		". HTTP Response Code: " 
        		+ responseCode;
	}

	public String step7SOSConnectionFailedException(String strURL,
			String message) {
		return "Connection to " + this.sos() + " " + strURL + " failed. Reason: " + message;
	}

	@Override
	public String step8ConfigFileButton() {
		return "Konfigurationsdatei öffnen";
	}

	public String step8Description() {
		return this.step() + " 8: Finaler Schritt - Zusammenfassung der Ergebnisse";
	}

	@Override
	public String step8DirectImportLabel() {
		return "Registriere " + this.sensor() + 
				"en und lade " + this.observation() + 
				"en in den " + this.sos();
	}

	public String step8ErrorLable(int i) {
		return "Fehler: " + i;
	}

	public String step8InsertObservationLabel(int i) {
		return "Füge " + i + " " + this.observation() + "en hinzu...";
	}

	public String step8LogFileButton() {
		return "Prüfen Sie die Logdatei";
	}

	public String step8RegisterSensorLabel(int i) {
		return "Registriere " + i + " " + this.sensor() + "(en)...";
	}
	
	@Override
	public String step8SaveModelFailed(File f, String exceptionText) {
		return "Die Konfiguration konnte nicht in der Datei\n\"" +
				f.getAbsolutePath() +
				"\"\ngespeichert werden.\nEin Fehler ist aufgetreten:\n" +
				exceptionText +
				"Für weitere Informationen bitte die Logdatei konsultieren.";
	}

	public String step8SuccessLabel(int i) {
		return "Erfolgreich: " + i;
	}

	@Override
	public String time() {
		return "Zeit";
	}

	@Override
	public String timeZone() {
		return "Zeitzone (UTC-Abstand)";
	}

	@Override
	public String unit() {
		return "Einheit";
	}

	public String unitOfMeasurement() {
		return "Unit of Measurement";
	}

	public String warningDialogTitle() {
		return "Warnung";
	}

	@Override
	public String year() {
		return "Jahr";
	}

}
