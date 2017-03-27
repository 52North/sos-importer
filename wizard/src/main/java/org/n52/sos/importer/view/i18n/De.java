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
 * This class contains all String used by the GUI in German
 *
 * @author e.h.juerrens@52north.org
 * @version $Id: $Id
 */
public class De extends Lang {

    private static final String BEENDEN = "Beenden";
    private static final String DIE_KONFIGURATION_KONNTE_NICHT_IN_DER_DATEI =
            "Die Konfiguration konnte nicht in der Datei\n\"";
    private static final String REGISTRIERE = "Registriere ";
    private static final String ÖFFNEN = "Öffnen";
    private static final String VERSION = "Version";
    private static final String AQUOT_NL = "\"\n";
    private static final String NL_AQUOT = "\n\"";
    private static final String PFAD = "Pfad";
    private static final String DER = "Der ";
    private static final String KANN_ZUR_ZEIT_NUR_ALS_DEZIMALZAHL_ANGEGEBEN_WERDEN =
            " kann zur Zeit nur als Dezimalzahl angegeben werden.";
    private static final String FEHLER = "Fehler";
    private static final String DIE = "Die ";
    private static final String BINDING = "Binding";
    private static final Locale LOCALE = Locale.GERMAN;

    /** {@inheritDoc} */
    @Override
    public String altitude() {
        return "Höhe";
    }

    /** {@inheritDoc} */
    @Override
    public String and() {
        return "und";
    }

    /** {@inheritDoc} */
    @Override
    public String backButtonLabel() {
        return "Zurück";
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
        return "Spalte";
    }

    /** {@inheritDoc} */
    @Override
    public String dataPreview() {
        return "Daten Vorschau";
    }

    /** {@inheritDoc} */
    @Override
    public String date() {
        return "Datum";
    }

    /** {@inheritDoc} */
    @Override
    public String day() {
        return "Tag";
    }

    /** {@inheritDoc} */
    @Override
    public String editableComboBoxDeleteItemButton() {
        return "Entfernt das aktuelle Element auf der List";
    }

    /** {@inheritDoc} */
    @Override
    public String editableComboBoxNewItemButton() {
        return "Fügt ein neues Element zur Liste hinzu";
    }

    /** {@inheritDoc} */
    @Override
    public String epsgCode() {
        return "EPSG-Kennzahl";
    }

    /** {@inheritDoc} */
    @Override
    public String epsgCodeWarningDialogNaturalNumber() {
        return DIE + epsgCode() + " muss eine natürliche Zahl sein.";
    }

    /** {@inheritDoc} */
    @Override
    public String epsgCodeWarningDialogOutOfRange() {
        return DIE + epsgCode() + " sollte größer als 0 und kleiner als 32767 sein.";
    }

    /** {@inheritDoc} */
    @Override
    public String error() {
        return FEHLER;
    }

    /** {@inheritDoc} */
    @Override
    public String errorDialogTitle() {
        return FEHLER;
    }

    /** {@inheritDoc} */
    @Override
    public String example() {
        return "Beispiel";
    }

    /** {@inheritDoc} */
    @Override
    public String exitDialogQuestion() {
        return "Wollen Sie das Programm wirklich beenden?\n";
    }

    /** {@inheritDoc} */
    @Override
    public String exitDialogTitle() {
        return BEENDEN;
    }

    /** {@inheritDoc} */
    @Override
    public String featureOfInterest() {
        return "Geoobjekt";
    }

    /** {@inheritDoc} */
    @Override
    public String file() {
        return "Datei";
    }

    /** {@inheritDoc} */
    @Override
    public String finishButtonLabel() {
        return BEENDEN;
    }

    /** {@inheritDoc} */
    @Override
    public String format() {
        return "Format";
    }

    /** {@inheritDoc} */
    @Override
    public String generated() {
        return "generiert";
    }

    /** {@inheritDoc} */
    @Override
    public Locale getLocale() {
        return De.LOCALE;
    }

    /** {@inheritDoc} */
    @Override
    public String group() {
        return "Gruppe";
    }

    /** {@inheritDoc} */
    @Override
    public String heightWarningDialogDecimalNumber() {
        return DIE + altitude() + KANN_ZUR_ZEIT_NUR_ALS_DEZIMALZAHL_ANGEGEBEN_WERDEN;
    }

    /** {@inheritDoc} */
    @Override
    public String hours() {
        return "Stunden";
    }

    /** {@inheritDoc} */
    @Override
    public String infoDialogTitle() {
        return "Information";
    }

    /** {@inheritDoc} */
    @Override
    public String latitudeDialogDecimalValue() {
        return DER + latitudeNorthing() + KANN_ZUR_ZEIT_NUR_ALS_DEZIMALZAHL_ANGEGEBEN_WERDEN;
    }

    /** {@inheritDoc} */
    @Override
    public String latitudeNorthing() {
        return "Breitengrad / Hochwert";
    }

    /** {@inheritDoc} */
    @Override
    public String longitudeDialogDecimalValue() {
        return DER + longitudeEasting() + KANN_ZUR_ZEIT_NUR_ALS_DEZIMALZAHL_ANGEGEBEN_WERDEN;
    }

    /** {@inheritDoc} */
    @Override
    public String longitudeEasting() {
        return "Längengrad / Rechtswert";
    }

    /** {@inheritDoc} */
    @Override
    public String measuredValue() {
        return "Messwert";
    }

    /** {@inheritDoc} */
    @Override
    public String metadata() {
        return "Metadaten";
    }

    /** {@inheritDoc} */
    @Override
    public String minutes() {
        return "Minuten";
    }

    /** {@inheritDoc} */
    @Override
    public String month() {
        return "Monat";
    }

    /** {@inheritDoc} */
    @Override
    public String name() {
        return "Name";
    }

    /** {@inheritDoc} */
    @Override
    public String nextButtonLabel() {
        return "Weiter";
    }

    /** {@inheritDoc} */
    @Override
    public String numValuePanelThousandsSeparator() {
        return "Tausender-Trennzeichen";
    }

    /** {@inheritDoc} */
    @Override
    public String observation() {
        return "Beobachtung";
    }

    /** {@inheritDoc} */
    @Override
    public String observedProperty() {
        return "Phänomen";
    }

    /** {@inheritDoc} */
    @Override
    public String offering() {
        return "Offering";
    }

    /** {@inheritDoc} */
    @Override
    public String path() {
        return PFAD;
    }

    /** {@inheritDoc} */
    @Override
    public String position() {
        return "Position";
    }

    /** {@inheritDoc} */
    @Override
    public String referenceSystem() {
        return "Referenzsystem";
    }

    /** {@inheritDoc} */
    @Override
    public String row() {
        return "Zeile";
    }

    /** {@inheritDoc} */
    @Override
    public String seconds() {
        return "Sekunden";
    }

    /** {@inheritDoc} */
    @Override
    public String sensor() {
        return "Sensor";
    }

    /** {@inheritDoc} */
    @Override
    public String spaceString() {
        return "Leerzeichen";
    }

    /** {@inheritDoc} */
    @Override
    public String specificationVersion() {
        return "Spezifikations-Version";
    }

    /** {@inheritDoc} */
    @Override
    public String step() {
        return "Schritt";
    }

    /** {@inheritDoc} */
    @Override
    public String step1BrowseButton() {
        return "Auswählen";
    }

    /** {@inheritDoc} */
    @Override
    public String step1Description() {
        return "Schritt 1: Wählen Sie die CSV-Datei aus";
    }

    /** {@inheritDoc} */
    @Override
    public String step1Directory() {
        return PFAD;
    }

    /** {@inheritDoc} */
    @Override
    public String step1EncodingLabel() {
        return "Bitte das Enkoding angeben";
    }

    /** {@inheritDoc} */
    @Override
    public String step1FeedTypeCSV() {
        return "Einmaliger Import aus einer CSV Datei";
    }

    /** {@inheritDoc} */
    @Override
    public String step1FeedTypeFTP() {
        return "Einmaliger / Wiederholter Import aus einer Datei eines FTP-Servers";
    }

    /** {@inheritDoc} */
    @Override
    public String step1File() {
        return "CSV-Datei";
    }

    /** {@inheritDoc} */
    @Override
    public String step1FileSchema() {
        return "Dateinamen-Schema";
    }

    /** {@inheritDoc} */
    @Override
    public String step1FtpServer() {
        return "FTP-Server";
    }

    /** {@inheritDoc} */
    @Override
    public String step1InstructionLabel() {
        return "Bitte wählen Sie die CSV-Datei aus";
    }

    /** {@inheritDoc} */
    @Override
    public String step1Introduction() {
        return "Erklärung";
    }

    /** {@inheritDoc} */
    @Override
    public String step1Password() {
        return "Passwort";
    }

    /** {@inheritDoc} */
    @Override
    public String step1Regex() {
        return "<html>Reguläre<br/>Ausdrücke:</html>";
    }

    /** {@inheritDoc} */
    @Override
    public String step1RegexDescription() {
        return "<html>Hinweis: Wählen Sie diese Option, um "
                + "nachfolgend dynamische Ordner- oder/und Dateistrukturen zu beschreiben. "
                + "Achten Sie dabei auf die Bedeutung spezieller Zeichen von regulären "
                + "Ausdrücken, insbesonde Escape-Zeichen.</html>";
    }

    /** {@inheritDoc} */
    @Override
    public String step1SelectLanguage() {
        return "Sprachauswahl";
    }

    /** {@inheritDoc} */
    @Override
    public String step1User() {
        return "Benutzer";
    }

    /** {@inheritDoc} */
    @Override
    public String step2ColumnSeparator() {
        return "Spalten-Trenner";
    }

    /** {@inheritDoc} */
    @Override
    public String step2CommentIndicator() {
        return "Kommentar-Indikator";
    }

    /** {@inheritDoc} */
    @Override
    public String step2DataPreviewLabel() {
        return "Datendatei-Vorschau";
    }

    /** {@inheritDoc} */
    @Override
    public String step2DecimalSeparator() {
        return "Dezimal-Separator";
    }

    /** {@inheritDoc} */
    @Override
    public String step2Description() {
        return step() + " 2: Definieren Sie die Metadaten zum Einlesen der CSV-Datei";
    }

    /** {@inheritDoc} */
    @Override
    public String step2FirstLineWithData() {
        return "Ignoriere Daten bis Zeile";
    }

    /** {@inheritDoc} */
    @Override
    public String step2IsSampleBased() {
        return "Besteht die Datei aus Messreihen";
    }

    /** {@inheritDoc} */
    @Override
    public String step2ParseHeader() {
        return "Kopfzeile auswerten";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDataOffsetLabel() {
        return "Zeilenabstand 'Messdaten'";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDataOffsetToolTip() {
        return "Der Abstand zwischen dem Start einer Messreihe und dem Beginn der Daten in Zeilen.";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDateExtractionRegExLabel() {
        return "Regulärer Ausdruck \"Datumsinformation\"";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDateExtractionRegExTooltip() {
        return new StringBuffer("<html>Der Reguläre Ausdruck mit dessen Hilfe die<br>")
            .append("Datumsinformatione aus der Zeile extrahiert werden, die<br>")
            .append("die entspr. Informationen für die aktuelle Messreihe<br>")
            .append("enthält. Das Ergebnis des Asudrucks MUSS genau EINE<br>")
            .append("Gruppe enthalten. Diese Gruppe wird mit Hilfe des<br>")
            .append("Attributes \"sampleDatePattern\" in einen Zeitstempel<br>")
            .append("umgewandelt.</html>")
            .toString();
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDateOffsetLabel() {
        return "Zeilenabstand 'Datumsinformation'";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDateOffsetToolTip() {
        return "Der Abstand zwischen dem Start einer Messreihe und der Zeile mit den Datumsinformationen";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDatePatternLabel() {
        return "Analyse Muster \"Datumsinformationen\"";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedDatePatternTooltip() {
        return "Muster, das zur Interpretation der Datumsinformationen für die aktuelle Messreihe verwendet wird.";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedSampleSizeOffsetLabel() {
        return "Zeilenabstand 'Messreihengröße'";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedSampleSizeOffsetToolTip() {
        return "Der Abstand zwischem dem Start einer Messreihe und "
                + "der Zeile mit der Anzahl der Zeilen in der aktuellen Reihe.";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedSampleSizeRegExLabel() {
        return "Regulärer Ausdruck \"Messreihengröße\"";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedSampleSizeRegExTooltip() {
        return new StringBuffer("<html>Wird benutzt um die Größe der aktuellen Messreihe aus der Zeile")
            .append(" zu extrahieren.<br>Das Ergebnis der Zeile muss EINE Gruppe sein,<br>")
            .append("die in eine Zahl umgewandelt werden kann.</html>")
            .toString();
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedStartRegExLabel() {
        return "Regulärer Ausdruck \"Beginn der Messreihe\"";
    }

    /** {@inheritDoc} */
    @Override
    public String step2SampleBasedStartRegExTooltip() {
        return "Wird benutzt um den Start einer Messreihe zu finden.\nMuss für die gesamte Zeile passen";
    }

    /** {@inheritDoc} */
    @Override
    public String step2TextQualifier() {
        return "Text-Qualifier";
    }

    /** {@inheritDoc} */
    @Override
    public String step3Description() {
        return step() + " 3: Wählen Sie die Metainformationen für die aktuelle Spalte";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasureValueColMissingDialogMessage() {
        return "Es muss mindestens 1 " + measuredValue() + "-Spalte definiert werden!";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasureValueColMissingDialogTitle() {
        return measuredValue() + "-Spalte fehlt!";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ParseTest1Failed() {
        return "1 Wert nicht einlesbar.";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ParseTestAllOk() {
        return "Alle Werte einlesbar.";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ParseTestNFailed(final int n) {
        return n + " Werte nicht einlesbar.";
    }

    /** {@inheritDoc} */
    @Override
    public String step3SelectedColTypeUndefinedMsg() {
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

    /** {@inheritDoc} */
    @Override
    public String step3SelectedColTypeUndefinedTitle() {
        return "Spalten-Typ ist \"" +
                step3ColTypeUndefined() +
                "\"";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ColTypeDateTime() {
        return "Datum & Zeit";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ColTypeDoNotExport() {
        return "Nicht exportieren";
    }

    /** {@inheritDoc} */
    @Override
    public String step3ColTypeMeasuredValue() {
        return measuredValue();
    }

    /** {@inheritDoc} */
    @Override
    public String step3ColTypeUndefined() {
        return "Unbekannt";
    }

    /** {@inheritDoc} */
    @Override
    public String step3DateAndTimeCombination() {
        return "Kombination";
    }

    /** {@inheritDoc} */
    @Override
    public String step3DateAndTimeUnixTime() {
        return "UNIX-Zeit";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasuredValBoolean() {
        return "Wahrheitswert";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasuredValCount() {
        return "Zählwert";
    }

    /** {@inheritDoc} */
    @Override
    public String step3MeasuredValNumericValue() {
        return "Numerischer Wert";
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
        return step() + " 4a: Klären Sie Datum & Zeit Unklarheit";
    }

    /** {@inheritDoc} */
    @Override
    public String step4aInfoDateAndTime() {
        return "Datum und Zeit sind schon gesetzt für diesen " + measuredValue() + ".";
    }

    /** {@inheritDoc} */
    @Override
    public String step4aInfoMeasuredValue() {
        return "Dies ist kein " + measuredValue() + ".";
    }

    /** {@inheritDoc} */
    @Override
    public String step4aModelDescription() {
        return "Wähle Sie alle " + measuredValue() + " " + Constants.STRING_REPLACER + "n " +
                ", die zur markierten Datum-Zeit-Gruppe gehören, aus.";
    }

    /** {@inheritDoc} */
    @Override
    public String step4bDescription() {
        return step() + " 4b: Kläre Sie Unklarheiten";
    }

    /** {@inheritDoc} */
    @Override
    public String step4bInfoNotMeasuredValue() {
        return step4aInfoMeasuredValue();
    }

    /** {@inheritDoc} */
    @Override
    public String step4bInfoResourceAlreadySetText() {
        return " schon gesetzt für ";
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public String step5aDescription() {
        return step() + " 5a: Zeit-Informationen vervollständigen";
    }

    /** {@inheritDoc} */
    @Override
    public String step5aModelDescription() {
        return "Bitte geben Sie die Zeitzone für die markierte Spalte an.";
    }

    /** {@inheritDoc} */
    @Override
    public String step5cDescription() {
        return step() + " 5c: Positionsdaten vervollständigen";
    }

    /** {@inheritDoc} */
    @Override
    public String step5cModelDescription() {
        return "Vervollständigen Sie die fehlenden Positions-Informationen für die markierte Spalte.";
    }

    /** {@inheritDoc} */
    @Override
    public String step6aDescription() {
        return step() + " 6a: Fehlende Zeiten und Daten hinzufügen";
    }

    /** {@inheritDoc} */
    @Override
    public String step6aModelDescription() {
        return "<html>Bitte geben Sie <u>Datum und Uhrzeit</u> für alle Messwerte an!</html>";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bDefineConcatString() {
        return "Bitte geben Sie eine Zeichenkette zur Verknüpfung der Spalten an (Optional).";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bDescription() {
        return step() + " 6b: Fehlende Metainformationen hinzufügen";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bModelDescription() {
        return "<html>Welche(r/s) <u>" +
                Constants.STRING_REPLACER +
                "</u> gehört zur markierten " + measuredValue() + "-" +
                Constants.STRING_REPLACER +
                "?</html>";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bSelectColumnsLabel() {
        return "Bitte wählen Sie die Spalte(n) zum Generieren des Namens aus.";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bSpecialDescription() {
        return step() + " 6b (Spezial): Fehlende " + sensor() + "en hinzufügen";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bSpecialModelDescription() {
        return "Welches ist der " + sensor() + " für";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bURIInstructions() {
        return "Bitte geben Sie einen URI oder einen Prefix für den URI, falls Sie den Namen im URI nutzen wollen, an.";
    }

    /** {@inheritDoc} */
    @Override
    public String step6bUseNameAfterPrefix() {
        return "Namen an prefix anhängen als URI?";
    }

    /** {@inheritDoc} */
    @Override
    public String step6cDescription() {
        return step() + " 6c: Fehlende " + position() + "s-Angaben hinzufügen";
    }

    /** {@inheritDoc} */
    @Override
    public String step6cInfoToolName() {
        return "Position festlegen";
    }

    /** {@inheritDoc} */
    @Override
    public String step6cInfoToolTooltip() {
        return "Legen Sie die Position durch Mausklick fest";
    }

    /** {@inheritDoc} */
    @Override
    public String step6cModelDescription() {
        return "Wie ist die " + position() + "s-Angabe von";
    }

    /** {@inheritDoc} */
    @Override
    public String step6Generation() {
        return "Erzeuge Bezeichner automatisch";
    }

    /** {@inheritDoc} */
    @Override
    public String step6ManualInput() {
        return "Setze Bezeichner manuell";
    }

    /** {@inheritDoc} */
    @Override
    public String step6MissingUserInput() {
        return "Ihre Angaben sind nicht vollständig. Bitte überprüfen Sie sie.";
    }

    /** {@inheritDoc} */
    @Override
    public String step6NoUserInput() {
        return "Keine Nutzereingaben gefunden. Bitte geben Sie die notwendigen Informationen an.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigDirNotDirOrWriteable(final String folder) {
        return "Auf das Verzeichnis \n\"" +
                folder + "\"\n kann nicht zugegriffen werden";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigFileButton() {
        return "Verzeichnis wählen";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigFileDialogTitel() {
        return "Bitte das Verzeichnis für die Konfigurations-Datei wählen";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigFileLabel() {
        return "Verzeichnis";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigurationFile() {
        return "Konfigurations-Datei";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ConfigFileInstructions() {
        return "Bitte das Verzeichnis zum Speichern der Konfigurationsdatei angeben.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7Description() {
        return step() + " 7: Finale Konfiguration";
    }

    /** {@inheritDoc} */
    @Override
    public String step7DirectImport() {
        return "Im nächsten Schritt die Daten in den SOS importieren";
    }

    /** {@inheritDoc} */
    @Override
    public String step7IgnoreColumnMismatchCheckBoxLabel() {
        return "Sollen Zeilen mit falscher Spaltenanzahl ignoriert werden?";
    }

    /** {@inheritDoc} */
    @Override
    public String step7IgnoreColumnMismatchBorderLabel() {
        return "Weitere Einstellungen";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategyBorderLabel() {
        return "Import-Strategie";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategyLabel() {
        return "Strategie";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategySingleObservation() {
        return "Einzelne Beobachtungen";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategySweArrayHunksizeLabel() {
        return "Brockengröße";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategySweArrayObservation() {
        return "SweArrayObservation";
    }

    /** {@inheritDoc} */
    @Override
    public String step7ImportStrategySweArraySendBuffer() {
        return "Sendepuffer";
    }

    /** {@inheritDoc} */
    @Override
    public String step7OfferingCheckBoxLabel() {
        return "Offering-Bezeichner aus Sensor-Namen generieren?";
    }

    /** {@inheritDoc} */
    @Override
    public String step7OfferingInputTextfieldLabel() {
        return "Bitte geben Sie den Offering-Namen an:";
    }

    /** {@inheritDoc} */
    @Override
    public String step7OfferingNameNotGiven() {
        return "Bitte geben Sie den Offering-Namen an, oder wählen die Generierung aus";
    }

    /** {@inheritDoc} */
    @Override
    public String step7OfferingNameNotValid(final String offeringName) {
        return String.format("Der Name '%s' für das Offering ist nicht erlaubt. "
                + "Er muss der Spezifikation für XML-NCName entsprechen.",
                offeringName);
    }

    /** {@inheritDoc} */
    @Override
    public String step7SaveConfig() {
        return "Konfiguration in XML-Datei speichern";
    }

    /** {@inheritDoc} */
    @Override
    public String step7SosBindingInstructions() {
        return "Geben Sie das Binding (Kommunikationprotokoll) an.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7SosBindingLabel() {
        return BINDING;
    }

    /** {@inheritDoc} */
    @Override
    public String step7SOSConncetionStart(final String strURL) {
        return "Um mit dem Verbindungstest zu dem " + sos() +
                NL_AQUOT + strURL + AQUOT_NL +
                "zu starten, wählen Sie JA.\n" +
                "Falls Sie Einstellungen ändern möchten, dann wählen Sie NEIN.";
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
        return "Verbindung zu " + sos() +
                NL_AQUOT + strURL + AQUOT_NL +
                "fehlgeschlagen nach " + connectTimeoutSeconds + " Sekunden Verindungs- und " +
                readTimeoutSeconds + " Sekunden Lese-Timeout.\n" +
                "Grund: " + message;
    }

    /** {@inheritDoc} */
    @Override
    public String step7SosUrlInstructions() {
        return "Geben Sie die SOS-Web-Adresse inkl. Endpoint wie z.B. \"http://www.example.com/52nSOS/sos\" an";
    }

    /** {@inheritDoc} */
    @Override
    public String step7SosVersionInstructions() {
        return "Bitte die Spezifikations-Version angeben, die durch die SOS-Instanz implementiert wird.";
    }

    /** {@inheritDoc} */
    @Override
    public String step7SosVersionLabel() {
        return VERSION;
    }

    /** {@inheritDoc} */
    @Override
    public String step8ConfigFileButton() {
        return ÖFFNEN;
    }

    /** {@inheritDoc} */
    @Override
    public String step8ConfigurationFileInstructions() {
        return "Klicken Sie auf den Button um die generierte Konfiguration anzuschauen";
    }

    /** {@inheritDoc} */
    @Override
    public String step8Description() {
        return step() + " 8: Finaler Schritt - Zusammenfassung der Ergebnisse";
    }

    /** {@inheritDoc} */
    @Override
    public String step8DirectImportInstructions() {
        return "Klicken Sie auf "
                + step8DirectImportStartButton()
                + " um einmalig die Daten in den SOS zu laden.";
    }

    /** {@inheritDoc} */
    @Override
    public String step8DirectImportLabel() {
        return REGISTRIERE + sensor() +
                "en und lade " + observation() +
                "en in den " + sos();
    }

    /** {@inheritDoc} */
    @Override
    public String step8DirectImportStartButton() {
        return "Starte Import-Vorgang";
    }

    /** {@inheritDoc} */
    @Override
    public String step8ErrorLable(final int i) {
        return "Fehler: " + i;
    }

    /** {@inheritDoc} */
    @Override
    public String step8FeederJarNotFound(final String expectedAbsolutePathToFeederJar) {
        return String.format("Konnte JAR-Datei nicht finden!%nBitte dort ablegen:%n'%s'",
                expectedAbsolutePathToFeederJar);
    }

    /** {@inheritDoc} */
    @Override
    public String step8FeederJarNotFoundSelectByUser(final String pathToDirectoryWithFeederJar) {
        return String.format("Konnte JAR-Datei nicht finden hier nicht finden:%n"
                + "'%s'.%nKlicken sie auf JA um die Datei auszuwählen!",
                pathToDirectoryWithFeederJar);
    }

    /** {@inheritDoc} */
    @Override
    public String step8InsertObservationLabel(final int i) {
        return "Füge " + i + " " + observation() + "en hinzu...";
    }

    /** {@inheritDoc} */
    @Override
    public String step8LogFile() {
        return "Log-Datei";
    }

    /** {@inheritDoc} */
    @Override
    public String step8LogFileButton() {
        return ÖFFNEN;
    }

    /** {@inheritDoc} */
    @Override
    public String step8LogFileInstructions() {
        return "Klicken Sie auf den Button um zusätzliche Informationen zu bekommen, "
                + "die während des Vorgangs gesammelt wurden.";
    }

    /** {@inheritDoc} */
    @Override
    public String step8RegisterSensorLabel(final int i) {
        return REGISTRIERE + i + " " + sensor() + "(en)...";
    }

    /** {@inheritDoc} */
    @Override
    public String step8SaveModelFailed(final File f) {
        return DIE_KONFIGURATION_KONNTE_NICHT_IN_DER_DATEI +
                f.getAbsolutePath();
    }

    /** {@inheritDoc} */
    @Override
    public String step8SaveModelFailed(final File f, final String exceptionText) {
        return DIE_KONFIGURATION_KONNTE_NICHT_IN_DER_DATEI +
                f.getAbsolutePath() +
                "\"\ngespeichert werden.\nEin Fehler ist aufgetreten:\n" +
                exceptionText +
                "Für weitere Informationen bitte die Logdatei konsultieren.";
    }

    /** {@inheritDoc} */
    @Override
    public String step8StartImportButton() {
        return "Starte Importvorgang";
    }

    /** {@inheritDoc} */
    @Override
    public String step8SuccessLabel(final int i) {
        return "Erfolgreich: " + i;
    }

    /** {@inheritDoc} */
    @Override
    public String time() {
        return "Zeit";
    }

    /** {@inheritDoc} */
    @Override
    public String timeZone() {
        return "Zeitzone (UTC-Abstand)";
    }

    /** {@inheritDoc} */
    @Override
    public String unit() {
        return "Einheit";
    }

    /** {@inheritDoc} */
    @Override
    public String unitOfMeasurement() {
        return "Maßeinheit";
    }

    /** {@inheritDoc} */
    @Override
    public String uri() {
        return "URI";
    }

    /** {@inheritDoc} */
    @Override
    public String uriSyntaxNotValidDialogMessage(final String uri) {
        return "Der eingegebene URI \"" + uri + "\" ist syntaktisch nicht korrekt.";
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
        return "Teste Einlesen der Spalte...";
    }

    /** {@inheritDoc} */
    @Override
    public String warningDialogTitle() {
        return "Warnung";
    }

    /** {@inheritDoc} */
    @Override
    public String year() {
        return "Jahr";
    }

}
