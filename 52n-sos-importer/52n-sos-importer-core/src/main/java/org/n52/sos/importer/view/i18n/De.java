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
	public String code() {
		return "Code";
	}

	@Override
	public String column() {
		return "Spalte";
	}

	@Override
	public String dataPreview() {
		return "Daten Vorschau";
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
	public String editableComboBoxDeleteItemButton() {
		return "Entfernt das aktuelle Element auf der List";
	}

	@Override
	public String editableComboBoxNewItemButton() {
		return "Fügt ein neues Element zur Liste hinzu";
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
		return "Geoobjekt";
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
	public String generated() {
		return "generiert";
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

	@Override
	public String measuredValue() {
		return "Messwert";
	}

	@Override
	public String metadata() {
		return "Metadaten";
	}

	@Override
	public String minutes() {
		return "Minuten";
	}

	@Override
	public String month() {
		return "Monat";
	}
	
	@Override
	public String name() {
		return "Name";
	}

	public String nextButtonLabel() {
		return "Weiter";
	}

	public String numValuePanelThousandsSeparator() {
		return "Tausender-Trennzeichen";
	}

	public String observation() {
		return "Beobachtung";
	}

	public String observedProperty() {
		return "Phänomen";
	}

	@Override
	public String offering() {
		return "Offering";
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

	public String spaceString() {
		return "Leerzeichen";
	}

	public String step() {
		return "Schritt";
	}

	public String step1BrowseButton() {
		return "Auswählen";
	}

	public String step1Description() {
		return "Schritt 1: Wählen Sie die CSV-Datei aus";
	}

	public String step1File() {
		return "CSV-Datei";
	}

	@Override
	public String step1InstructionLabel() {
		return "Bitte wählen Sie die CSV-Datei aus";
	}

	@Override
	public String step1Introduction() {
		return "Erklärung";
	}

	@Override
	public String step1SelectLanguage() {
		return "Sprachauswahl";
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

	public String step2DecimalSeparator() {
		return "Dezimal-Separator";
	}

	public String step2Description() {
		return this.step() + " 2: Definieren Sie die Metadaten zum Einlesen der CSV-Datei";
	}

	public String step2FirstLineWithData() {
		return "Ignoriere Daten bis Zeile";
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
		return "Wahrheitswert";
	}

	public String step3MeasuredValCount() {
		return "Zählwert";
	}

	public String step3MeasuredValNumericValue() {
		return "Numerischer Wert";
	}

	public String step3MeasuredValText() {
		return "Text (als CategoyObservation)";
	}

	public String step3PositionCombination() {
		return this.step3DateAndTimeCombination();
	}

	public String step4aDescription() {
		return this.step() + " 4a: Klären Sie Datum & Zeit Unklarheit";
	}

	public String step4aInfoDateAndTime() {
		return "Datum und Zeit sind schon gesetzt für diesen " + this.measuredValue() + ".";
	}

	public String step4aInfoMeasuredValue() {
		return "Dies ist kein " + this.measuredValue() + ".";
	}

	public String step4aModelDescription() {
		return "Wähle Sie alle " + this.measuredValue() + " " + Constants.STRING_REPLACER + "n " +
				", die zur markierten Datum-Zeit-Gruppe gehören, aus.";
	}

	public String step4bDescription() {
		return this.step() + " 4b: Kläre Sie Unklarheiten";
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
		return "Bitte geben Sie die Zeitzone für die markierte Spalte an.";
	}

	public String step5cDescription() {
		return this.step() + " 5c: Positionsdaten vervollständigen";
	}

	public String step5cModelDescription() {
		return "Vervollständigen Sie die fehlenden Positions-Informationen für die markierte Spalte.";
	}

	public String step6aDescription() {
		return this.step() + " 6a: Fehlende Zeiten und Daten hinzufügen";
	}

	@Override
	public String step6aModelDescription() {
		return "<html>Bitte geben Sie <u>Datum und Uhrzeit</u> für alle Messwerte an!</html>";
	}

	@Override
	public String step6bDefineConcatString() {
		return "Bitte geben Sie eine Zeichenkette zur Verknüpfung der Spalten an (Optional).";
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

	@Override
	public String step6bSelectColumnsLabel() {
		return "Bitte wählen Sie die Spalte(n) zum Generieren des Namens aus.";
	}

	public String step6bSpecialDescription() {
		return this.step() + " 6b (Spezial): Fehlende " + this.sensor() + "en hinzufügen";
	}

	public String step6bSpecialModelDescription() {
		return "Welches ist der " + this.sensor() + " für";
	}

	@Override
	public String step6bURIInstructions() {
		return "Bitte geben Sie einen URI oder einen Prefix für den URI, falls Sie den Namen im URI nutzen wollen, an.";
	}

	@Override
	public String step6bUseNameAfterPrefix() {
		return "Namen an prefix anhängen als URI?";
	}

	public String step6cDescription() {
		return this.step() + " 6c: Fehlende " + this.position() + "s-Angaben hinzufügen";
	}

	@Override
	public String step6cInfoToolName() {
		return "Position festlegen";
	}

	@Override
	public String step6cInfoToolTooltip() {
		return "Legen Sie die Position durch Mausklick fest";
	}

	public String step6cModelDescription() {
		return "Wie ist die " + this.position() + "s-Angabe von";
	}

	@Override
	public String step6Generation() {
		return "Erzeuge Bezeichner automatisch";
	}

	@Override
	public String step6ManualInput() {
		return "Setze Bezeichner manuell";
	}
	
	@Override
	public String step6MissingUserInput() {
		return "Ihre Angaben sind nicht vollständig. Bitte überprüfen Sie sie.";
	}

	@Override
	public String step6NoUserInput() {
		return "Keine Nutzereingaben gefunden. Bitte geben Sie die notwendigen Informationen an.";
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
		return "Verzeichnis";
	}

	@Override
	public String step7ConfigurationFile() {
		return "Konfigurations-Datei";
	}

	public String step7Description() {
		return this.step() + " 7: Finale Konfiguration";
	}

	@Override
	public String step7DirectImport() {
		return "Im nächsten Schritt die Daten in den SOS importieren";
	}

	@Override
	public String step7OfferingCheckBoxLabel() {
		return "Offering-Bezeichner aus Sensor-Namen generieren?";
	}

	@Override
	public String step7OfferingInputTextfieldLabel() {
		return "Bitte geben Sie den Offering-Namen an:";
	}

	@Override
	public String step7OfferingNameNotGiven() {
		return "Bitte geben Sie den Offering-Namen an, oder wählen die Generierung aus";
	}

	@Override
	public String step7OfferingNameNotValid(String offeringName) {
		return String.format("Der Name \"%s\" für das Offering ist nicht erlaubt. Er muss der Spezifikation für XML-NCName entsprechen.", offeringName);
	}

	@Override
	public String step7SaveConfig() {
		return "Konfiguration in XML-Datei speichern";
	}

	@Override
	public String step7SOSConncetionStart(String strURL) {
		return "Um mit dem Verbindungstest zu dem " + this.sos() + 
				"\n\"" + strURL + "\"\n" +
				"zu starten, wählen Sie JA.\n" +
				"Falls Sie Einstellungen ändern möchten, dann wählen Sie NEIN.";
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
		return "Verbindung zu " + this.sos() + 
				"\n\"" + strURL + "\"\n" +
				"fehlgeschlagen nach " + connectTimeoutSeconds + " Sekunden Verindungs- und " + 
				readTimeoutSeconds + " Sekunden Lese-Timeout.\n" +
				"Grund: " + message;
	}

	@Override
	public String step7SosUrlInstructions() {
		return "Geben Sie die SOS-Web-Adresse inkl. Endpoint wie z.B. \"http://www.example.com/52nSOS/sos\" an";
	}

	@Override
	public String step8ConfigFileButton() {
		return "Öffnen";
	}

	@Override
	public String step8ConfigurationFileInstructions() {
		return "Klicken Sie auf den Button um die generierte Konfiguration anzuschauen";
	}

	public String step8Description() {
		return this.step() + " 8: Finaler Schritt - Zusammenfassung der Ergebnisse";
	}

	@Override
	public String step8DirectImportInstructions() {
		return "Klicken Sie auf ".concat(step8DirectImportStartButton()).concat(" um einmalig die Daten in den SOS zu laden.");
	}

	@Override
	public String step8DirectImportLabel() {
		return "Registriere " + this.sensor() + 
				"en und lade " + this.observation() + 
				"en in den " + this.sos();
	}

	@Override
	public String step8DirectImportStartButton() {
		return "Starte Import-Vorgang";
	}

	public String step8ErrorLable(int i) {
		return "Fehler: " + i;
	}

	@Override
	public String step8FeederJarNotFound(String expectedAbsolutePathToFeederJar) {
		return String.format("Konnte JAR-Datei nicht finden!\nBitte dort ablegen:\n\"%s\"",
				expectedAbsolutePathToFeederJar);
	}

	public String step8InsertObservationLabel(int i) {
		return "Füge " + i + " " + this.observation() + "en hinzu...";
	}

	@Override
	public String step8LogFile() {
		return "Log-Datei";
	}

	public String step8LogFileButton() {
		return "Öffnen";
	}

	@Override
	public String step8LogFileInstructions() {
		return "Klicken Sie auf den Button um zusätzliche Informationen zu bekommen, die während des Vorgangs gesammelt wurden.";
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

	@Override
	public String step8StartImportButton() {
		return "Starte Importvorgang";
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
		return "Maßeinheit";
	}

	@Override
	public String uri() {
		return "URI";
	}

	@Override
	public String uriSyntaxNotValidDialogMessage(String uri) {
		return "Der eingegebene URI \"" + uri + "\" ist syntaktisch nicht korrekt.";
	}

	@Override
	public String url() {
		return "URL";
	}

	@Override
	public String waitForParseResultsLabel() {
		return "Teste Einlesen der Spalte...";
	}

	public String warningDialogTitle() {
		return "Warnung";
	}

	@Override
	public String year() {
		return "Jahr";
	}

}
