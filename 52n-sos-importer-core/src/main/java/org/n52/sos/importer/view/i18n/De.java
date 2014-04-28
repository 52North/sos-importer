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
 * This class contains all String used by the GUI in German
 *
 * @author e.h.juerrens@52north.org
 */
public class De extends Lang {

	private final static Locale locale = Locale.GERMAN;

	@Override
	public String altitude() {
		return "Höhe";
	}

	@Override
	public String and() {
		return "und";
	}

	@Override
	public String backButtonLabel() {
		return "Zurück";
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
		return "Die " + epsgCode() + " muss eine natürliche Zahl sein.";
	}

	@Override
	public String epsgCodeWarningDialogOutOfRange() {
		return "Die " + epsgCode() + " sollte größer als 0 und kleiner als 32767 sein.";
	}

	@Override
	public String error() {
		return "Fehler";
	}

	@Override
	public String errorDialogTitle() {
		return "Fehler";
	}

	@Override
	public String example() {
		return "Beispiel";
	}

	@Override
	public String exitDialogQuestion() {
		return "Wollen Sie das Programm wirklich beenden?\n";
	}

	@Override
	public String exitDialogTitle() { return "Beenden"; }

	@Override
	public String featureOfInterest() {
		return "Geoobjekt";
	}

	@Override
	public String file() {
		return "Datei";
	}

	@Override
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
		return "Die " + altitude() + " kann zur Zeit nur als Dezimalzahl angegeben werden.";
	}

	@Override
	public String hours() {
		return "Stunden";
	}

	@Override
	public String infoDialogTitle() {
		return "Information";
	}

	@Override
	public String latitudeDialogDecimalValue() {
		return "Der " + latitudeNorthing() + " kann zur Zeit nur als Dezimalzahl angegeben werden.";
	}

	@Override
	public String latitudeNorthing() {
		return "Breitengrad / Hochwert";
	}

	@Override
	public String longitudeDialogDecimalValue() {
		return "Der " + longitudeEasting() + " kann zur Zeit nur als Dezimalzahl angegeben werden.";
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

	@Override
	public String nextButtonLabel() {
		return "Weiter";
	}

	@Override
	public String numValuePanelThousandsSeparator() {
		return "Tausender-Trennzeichen";
	}

	@Override
	public String observation() {
		return "Beobachtung";
	}

	@Override
	public String observedProperty() {
		return "Phänomen";
	}

	@Override
	public String offering() {
		return "Offering";
	}

	@Override
	public String path() {
		return "Pfad";
	}

	@Override
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

	@Override
	public String sensor() {
		return "Sensor";
	}

	@Override
	public String spaceString() {
		return "Leerzeichen";
	}

	@Override
	public String specificationVersion()
	{
		return "Spezifikations-Version";
	}

	@Override
	public String step() {
		return "Schritt";
	}

	@Override
	public String step1BrowseButton() {
		return "Auswählen";
	}

	@Override
	public String step1Description() {
		return "Schritt 1: Wählen Sie die CSV-Datei aus";
	}

	@Override
	public String step1Directory() {
		return "Pfad";
	}

	@Override
	public String step1EncodingLabel() {
		return "Bitte die Enkodierung für die Datendatei angeben";
	}

	@Override
	public String step1FeedTypeCSV() {
		return "Einmaliger Import aus einer CSV Datei";
	}

	@Override
	public String step1FeedTypeFTP() {
		return "Einmaliger / Wiederholter Import aus einer Datei eines FTP-Servers";
	}

	@Override
	public String step1File() {
		return "CSV-Datei";
	}

	@Override
	public String step1FileSchema() {
		return "Dateinamen-Schema";
	}

	@Override
	public String step1FtpServer() {
		return "FTP-Server";
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
	public String step1Password() {
		return "Passwort";
	}

	@Override
	public String step1Regex() {
		return "<html>Reguläre<br/>Ausdrücke:</html>";
	}

	@Override
	public String step1RegexDescription() {
		return "<html>Hinweis: Wählen Sie diese Option, um "
				+ "nachfolgend dynamische Ordner- oder/und Dateistrukturen zu beschreiben. "
				+ "Achten Sie dabei auf die Bedeutung spezieller Zeichen von regulären "
				+ "Ausdrücken, insbesonde Escape-Zeichen.</html>";
	}

	@Override
	public String step1SelectLanguage() {
		return "Sprachauswahl";
	}

	@Override
	public String step1User() {
		return "Benutzer";
	}

	@Override
	public String step2ColumnSeparator() {
		return "Spalten-Trenner";
	}

	@Override
	public String step2CommentIndicator() {
		return "Kommentar-Indikator";
	}

	@Override
	public String step2DataPreviewLabel() {
		return "Datendatei-Vorschau";
	}

	@Override
	public String step2DecimalSeparator() {
		return "Dezimal-Separator";
	}

	@Override
	public String step2Description() {
		return step() + " 2: Definieren Sie die Metadaten zum Einlesen der CSV-Datei";
	}

	@Override
	public String step2FirstLineWithData() {
		return "Ignoriere Daten bis Zeile";
	}

	@Override
	public String step2IsSampleBased() {
		return "Besteht die Datei aus Messreihen";
	}

	@Override
	public String step2ParseHeader() {
		return "Kopfzeile auswerten";
	}

	@Override
	public String step2SampleBasedDataOffsetLabel() {
		return "Zeilenabstand 'Messdaten'";
	}

	@Override
	public String step2SampleBasedDataOffsetToolTip() {
		return "Der Abstand zwischen dem Start einer Messreihe und dem Beginn der Daten in Zeilen.";
	}

	@Override
	public String step2SampleBasedDateExtractionRegExLabel() {
		return "Regulärer Ausdruck \"Datumsinformation\"";
	}

	@Override
	public String step2SampleBasedDateExtractionRegExTooltip() {
		return new StringBuffer("<html>Der Reguläre Ausdruck mit dessen Hilfe die<br />")
			.append("Datumsinformatione aus der Zeile extrahiert werden, die<br />")
			.append("die entspr. Informationen für die aktuelle Messreihe<br />")
			.append("enthält. Das Ergebnis des Asudrucks MUSS genau EINE<br />")
			.append("Gruppe enthalten. Diese Gruppe wird mit Hilfe des<br />")
			.append("Attributes \"sampleDatePattern\" in einen Zeitstempel<br />")
			.append("umgewandelt.</html>")
			.toString();
	}

	@Override
	public String step2SampleBasedDateOffsetLabel() {
		return "Zeilenabstand 'Datumsinformation'";
	}

	@Override
	public String step2SampleBasedDateOffsetToolTip() {
		return "Der Abstand zwischen dem Start einer Messreihe und der Zeile mit den Datumsinformationen";
	}

	@Override
	public String step2SampleBasedDatePatternLabel() {
		return "Analyse Muster \"Datumsinformationen\"";
	}

	@Override
	public String step2SampleBasedDatePatternTooltip() {
		return "Muster, das zur Interpretation der Datumsinformationen für die aktuelle Messreihe verwendet wird.";
	}

	@Override
	public String step2SampleBasedSampleSizeOffsetLabel() {
		return "Zeilenabstand 'Messreihengröße'";
	}

	@Override
	public String step2SampleBasedSampleSizeOffsetToolTip() {
		return "Der Abstand ziwschem dem Start einer Messreihe und der Zeile mit der Anzahl der Zeilen in der aktuellen Reihe.";
	}

	@Override
	public String step2SampleBasedStartRegExLabel() {
		return "Regulärer Ausdruck \"Beginn der Messreihe\"";
	}

	@Override
	public String step2SampleBasedStartRegExTooltip() {
		return "Wird benutzt um den Start einer Messreihe zu finden.\nMuss für die gesamte Zeile passen";
	}

	@Override
	public String step2TextQualifier() {
		return "Text-Qualifier";
	}

	@Override
	public String step3aDescription() {
		return step() + " 3a: Wählen Sie die Metainformationen für die aktuelle Spalte";
	}

	@Override
	public String step3aMeasureValueColMissingDialogMessage() {
		return "Es muss mindestens 1 " + measuredValue() + "-Spalte definiert werden!";
	}

	@Override
	public String step3aMeasureValueColMissingDialogTitle() {
		return measuredValue() + "-Spalte fehlt!";
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
	public String step3aParseTestNFailed(final int n) {
		return n + " Werte nicht einlesbar.";
	}

	@Override
	public String step3aSelectedColTypeUndefinedMsg() {
		return "Der Typ für die Spalte ist \"" +
				step3ColTypeUndefined() +
				"\".\n" +
				"Bitte wählen Sie einen anderen Typ.\n" +
				"Sollten Sie diese Spalte überspringen\n" +
				"(= nicht exportieren) wollen, dann " +
				"wählen Sie bitte als Typ \n\"" +
				step3ColTypeDoNotExport() +
				"\".";
	}

	@Override
	public String step3aSelectedColTypeUndefinedTitle() {
		return "Spalten-Typ ist \"" +
				step3ColTypeUndefined() +
				"\"";
	}

	@Override
	public String step3bDescription() {
		return step() + " 3b: Wählen Sie die Metainformationen für die aktuelle Zeile";
	}

	@Override
	public String step3ColTypeDateTime() {
		return "Datum & Zeit";
	}

	@Override
	public String step3ColTypeDoNotExport() {
		return "Nicht exportieren";
	}

	@Override
	public String step3ColTypeMeasuredValue() {
		return measuredValue();
	}

	@Override
	public String step3ColTypeUndefined() {
		return "Unbekannt";
	}

	@Override
	public String step3DateAndTimeCombination() {
		return "Kombination";
	}

	@Override
	public String step3DateAndTimeUnixTime() {
		return "UNIX-Zeit";
	}

	@Override
	public String step3MeasuredValBoolean() {
		return "Wahrheitswert";
	}

	@Override
	public String step3MeasuredValCount() {
		return "Zählwert";
	}

	@Override
	public String step3MeasuredValNumericValue() {
		return "Numerischer Wert";
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
		return step() + " 4a: Klären Sie Datum & Zeit Unklarheit";
	}

	@Override
	public String step4aInfoDateAndTime() {
		return "Datum und Zeit sind schon gesetzt für diesen " + measuredValue() + ".";
	}

	@Override
	public String step4aInfoMeasuredValue() {
		return "Dies ist kein " + measuredValue() + ".";
	}

	@Override
	public String step4aModelDescription() {
		return "Wähle Sie alle " + measuredValue() + " " + Constants.STRING_REPLACER + "n " +
				", die zur markierten Datum-Zeit-Gruppe gehören, aus.";
	}

	@Override
	public String step4bDescription() {
		return step() + " 4b: Kläre Sie Unklarheiten";
	}

	@Override
	public String step4bInfoNotMeasuredValue() {
		return step4aInfoMeasuredValue();
	}

	@Override
	public String step4bInfoResourceAlreadySetText() {
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

	@Override
	public String step5aDescription() {
		return step() + " 5a: Zeit-Informationen vervollständigen";
	}

	@Override
	public String step5aModelDescription() {
		return "Bitte geben Sie die Zeitzone für die markierte Spalte an.";
	}

	@Override
	public String step5cDescription() {
		return step() + " 5c: Positionsdaten vervollständigen";
	}

	@Override
	public String step5cModelDescription() {
		return "Vervollständigen Sie die fehlenden Positions-Informationen für die markierte Spalte.";
	}

	@Override
	public String step6aDescription() {
		return step() + " 6a: Fehlende Zeiten und Daten hinzufügen";
	}

	@Override
	public String step6aModelDescription() {
		return "<html>Bitte geben Sie <u>Datum und Uhrzeit</u> für alle Messwerte an!</html>";
	}

	@Override
	public String step6bDefineConcatString() {
		return "Bitte geben Sie eine Zeichenkette zur Verknüpfung der Spalten an (Optional).";
	}

	@Override
	public String step6bDescription() {
		return step() + " 6b: Fehlende Metainformationen hinzufügen";
	}

	@Override
	public String step6bModelDescription() {
		return "<html>Welche(r/s) <u>" +
				Constants.STRING_REPLACER +
				"</u> gehört zur markierten " + measuredValue() + "-" +
				Constants.STRING_REPLACER +
				"?</html>";
	}

	@Override
	public String step6bSelectColumnsLabel() {
		return "Bitte wählen Sie die Spalte(n) zum Generieren des Namens aus.";
	}

	@Override
	public String step6bSpecialDescription() {
		return step() + " 6b (Spezial): Fehlende " + sensor() + "en hinzufügen";
	}

	@Override
	public String step6bSpecialModelDescription() {
		return "Welches ist der " + sensor() + " für";
	}

	@Override
	public String step6bURIInstructions() {
		return "Bitte geben Sie einen URI oder einen Prefix für den URI, falls Sie den Namen im URI nutzen wollen, an.";
	}

	@Override
	public String step6bUseNameAfterPrefix() {
		return "Namen an prefix anhängen als URI?";
	}

	@Override
	public String step6cDescription() {
		return step() + " 6c: Fehlende " + position() + "s-Angaben hinzufügen";
	}

	@Override
	public String step6cInfoToolName() {
		return "Position festlegen";
	}

	@Override
	public String step6cInfoToolTooltip() {
		return "Legen Sie die Position durch Mausklick fest";
	}

	@Override
	public String step6cModelDescription() {
		return "Wie ist die " + position() + "s-Angabe von";
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
	public String step7ConfigDirNotDirOrWriteable(final String folder) {
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

	@Override
	public String step7Description() {
		return step() + " 7: Finale Konfiguration";
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
	public String step7OfferingNameNotValid(final String offeringName) {
		return String.format("Der Name '%s' für das Offering ist nicht erlaubt. Er muss der Spezifikation für XML-NCName entsprechen.", offeringName);
	}

	@Override
	public String step7SaveConfig() {
		return "Konfiguration in XML-Datei speichern";
	}

	@Override
	public String step7SosBindingInstructions()
	{
		return "Geben Sie das Binding (Kommunikationprotokoll) an.";
	}

	@Override
	public String step7SOSConncetionStart(final String strURL) {
		return "Um mit dem Verbindungstest zu dem " + sos() +
				"\n\"" + strURL + "\"\n" +
				"zu starten, wählen Sie JA.\n" +
				"Falls Sie Einstellungen ändern möchten, dann wählen Sie NEIN.";
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
		return "Verbindung zu " + sos() +
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
	public String step7SosVersionInstructions()
	{
		return "Bitte die Spezifikations-Version angeben, die durch die SOS-Instanz implementiert wird.";
	}

	@Override
	public String step8ConfigFileButton() {
		return "Öffnen";
	}

	@Override
	public String step8ConfigurationFileInstructions() {
		return "Klicken Sie auf den Button um die generierte Konfiguration anzuschauen";
	}

	@Override
	public String step8Description() {
		return step() + " 8: Finaler Schritt - Zusammenfassung der Ergebnisse";
	}

	@Override
	public String step8DirectImportInstructions() {
		return "Klicken Sie auf ".concat(step8DirectImportStartButton()).concat(" um einmalig die Daten in den SOS zu laden.");
	}

	@Override
	public String step8DirectImportLabel() {
		return "Registriere " + sensor() +
				"en und lade " + observation() +
				"en in den " + sos();
	}

	@Override
	public String step8DirectImportStartButton() {
		return "Starte Import-Vorgang";
	}

	@Override
	public String step8ErrorLable(final int i) {
		return "Fehler: " + i;
	}

	@Override
	public String step8FeederJarNotFound(final String expectedAbsolutePathToFeederJar) {
		return String.format("Konnte JAR-Datei nicht finden!\nBitte dort ablegen:\n'%s'",
				expectedAbsolutePathToFeederJar);
	}

	@Override
	public String step8FeederJarNotFoundSelectByUser(final String pathToDirectoryWithFeederJar)
	{
		return String.format("Konnte JAR-Datei nicht finden hier nicht finden:\n'%s'.\nKlicken sie auf JA um die Datei auszuwählen!",
				pathToDirectoryWithFeederJar);
	}

	@Override
	public String step8InsertObservationLabel(final int i) {
		return "Füge " + i + " " + observation() + "en hinzu...";
	}

	@Override
	public String step8LogFile() {
		return "Log-Datei";
	}

	@Override
	public String step8LogFileButton() {
		return "Öffnen";
	}

	@Override
	public String step8LogFileInstructions() {
		return "Klicken Sie auf den Button um zusätzliche Informationen zu bekommen, die während des Vorgangs gesammelt wurden.";
	}

	@Override
	public String step8RegisterSensorLabel(final int i) {
		return "Registriere " + i + " " + sensor() + "(en)...";
	}

	@Override
	public String step8SaveModelFailed(final File f) {
		return "Die Konfiguration konnte nicht in der Datei\n\"" +
				f.getAbsolutePath();
	}

	@Override
	public String step8SaveModelFailed(final File f, final String exceptionText) {
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

	@Override
	public String step8SuccessLabel(final int i) {
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

	@Override
	public String unitOfMeasurement() {
		return "Maßeinheit";
	}

	@Override
	public String uri() {
		return "URI";
	}

	@Override
	public String uriSyntaxNotValidDialogMessage(final String uri) {
		return "Der eingegebene URI \"" + uri + "\" ist syntaktisch nicht korrekt.";
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
		return "Teste Einlesen der Spalte...";
	}

	@Override
	public String warningDialogTitle() {
		return "Warnung";
	}

	@Override
	public String year() {
		return "Jahr";
	}

}
