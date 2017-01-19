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
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.resources.UnitOfMeasurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Abstract Lang class.</p>
 *
 * @author e.h.juerrens@52north.org
 * @version $Id: $Id
 */
public abstract class Lang {

    private static HashMap<Locale, Lang> availableLocales = new HashMap<Locale, Lang>();

    // default language
    private static Locale currentLocale = Locale.ENGLISH;

    private static Lang instance;

    private static final Logger logger = LoggerFactory.getLogger(Lang.class);

    // Add default locale En
    static {
        Lang.availableLocales.put(Locale.ENGLISH, new En());
        En.setCurrentLocale(Locale.ENGLISH);
        Lang.availableLocales.put(Locale.GERMAN, new De());
    }

    /**
     * <p>Getter for the field <code>availableLocales</code>.</p>
     *
     * @return an array of {@link java.util.Locale} objects.
     */
    public static Locale[] getAvailableLocales() {
        final Set <Locale> locales = availableLocales.keySet();
        return locales.toArray(new Locale[locales.size()]);
    }

    /**
     * <p>Getter for the field <code>currentLocale</code>.</p>
     *
     * @return the currentLocale
     */
    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * <p>l.</p>
     *
     * @return An instance of the subclass of Lang implementing the defined
     * Locate.
     */
    public static Lang l() {
        if (instance == null) {
            instance = availableLocales.get(currentLocale);
        }
        return instance;
    }
    
    /**
     * <p>Setter for the field <code>currentLocale</code>.</p>
     *
     * @param newLocale the currentLocale to set
     */
    public static void setCurrentLocale(final Locale newLocale) {
        if (logger.isTraceEnabled()) {
            logger.trace("setCurrentLocale(" + newLocale + ")");
        }
        // when the locale is changed, reset is and change instance object
        if (!Lang.currentLocale.equals(newLocale)) {
            Lang.currentLocale = newLocale;
            Lang.instance = availableLocales.get(newLocale);
        }
    }
    
    /**
     * <p>altitude.</p>
     *
     * @return Altitude / Height
     */
    public abstract String altitude();
    
    /**
     * <p>and.</p>
     *
     * @return and
     */
    public abstract String and();
    
    /**
     * <p>backButtonLabel.</p>
     *
     * @return Back
     */
    public abstract String backButtonLabel();
    
    /**
     * <p>binding.</p>
     *
     * @return Binding
     */
    public abstract String binding();
    
    /**
     * <p>code.</p>
     *
     * @return Code
     */
    public abstract String code();
    
    /**
     * <p>column.</p>
     *
     * @return column
     */
    public abstract String column();
    
    /**
     * <p>dataPreview.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String dataPreview();
    
    /**
     * <p>date.</p>
     *
     * @return Date
     */
    public abstract String date();
    
    /**
     * <p>day.</p>
     *
     * @return Day
     */
    public abstract String day();
    
    /**
     * <p>editableComboBoxDeleteItemButton.</p>
     *
     * @return Delete the selected item from the list
     */
    public abstract String editableComboBoxDeleteItemButton();
    
    /**
     * <p>editableComboBoxNewItemButton.</p>
     *
     * @return Add a new item to the list
     */
    public abstract String editableComboBoxNewItemButton();
    
    /**
     * <p>epsgCode.</p>
     *
     * @return EPSG-Code
     */
    public abstract String epsgCode();
    
    /**
     * <p>epsgCodeWarningDialogNaturalNumber.</p>
     *
     * @return The EPSG-Code has to be a natural number.
     */
    public abstract String epsgCodeWarningDialogNaturalNumber();
    
    /**
     * <p>epsgCodeWarningDialogOutOfRange.</p>
     *
     * @return The EPSG-Code has to be in the range of 0 and 32767.
     */
    public abstract String epsgCodeWarningDialogOutOfRange();
    
    /**
     * <p>error.</p>
     *
     * @return Error
     */
    public abstract String error();
    
    /**
     * <p>errorDialogTitle.</p>
     *
     * @return Error
     */
    public abstract String errorDialogTitle();
    
    /**
     * <p>example.</p>
     *
     * @return Example
     */
    public abstract String example();
    
    /**
     * <p>exitDialogQuestion.</p>
     *
     * @return Do you really want to exit?\n
     */
    public abstract String exitDialogQuestion();
    
    /**
     * <p>exitDialogTitle.</p>
     *
     * @return Exit
     */
    public abstract String exitDialogTitle();
    
    /**
     * <p>featureOfInterest.</p>
     *
     * @return Feature of Interest
     */
    public abstract String featureOfInterest();
    
    /**
     * <p>file.</p>
     *
     * @return file
     */
    public abstract String file();
    
    /**
     * <p>finishButtonLabel.</p>
     *
     * @return Finish
     */
    public abstract String finishButtonLabel();
    
    /**
     * <p>format.</p>
     *
     * @return Format
     */
    public abstract String format();
    
    /**
     * <p>frameTitle.</p>
     *
     * @return SOS Importer {@link org.n52.sos.importer.Constants#VERSION}
     */
    public String frameTitle() {
        return "SOS Importer " + Constants.VERSION;
    }
    
    /**
     * <p>frameTitleExtension.</p>
     *
     * @param file a {@link java.lang.String} object.
     * @param path a {@link java.lang.String} object.
     * @return - file: "<code>file</code>" (path: "<code>path</code>")
     */
    public String frameTitleExtension(final String file, final String path) {
        return " - " + file() + ":\"" + file + "\" (" + path() + ": \"" + path + "\")";
    }
    
    /**
     * <p>generated.</p>
     *
     * @return generated
     */
    public abstract String generated();
    
    /**
     * <p>getLocale.</p>
     *
     * @return a {@link java.util.Locale} object.
     */
    public abstract Locale getLocale();
    
    /**
     * <p>group.</p>
     *
     * @return Group
     */
    public abstract String group();
    
    /**
     * <p>heightWarningDialogDecimalNumber.</p>
     *
     * @return The height has to be a decimal number.
     */
    public abstract String heightWarningDialogDecimalNumber();
    
    /**
     * <p>hours.</p>
     *
     * @return Hours
     */
    public abstract String hours();
    
    /**
     * <p>infoDialogTitle.</p>
     *
     * @return Information
     */
    public abstract String infoDialogTitle();
    
    /**
     * <p>latitudeDialogDecimalValue.</p>
     *
     * @return The latitude/northing can only be a decimal number so far.
     */
    public abstract String latitudeDialogDecimalValue();
    
    /**
     * <p>latitudeNorthing.</p>
     *
     * @return Latitude / Northing
     */
    public abstract String latitudeNorthing();
    
    /**
     * <p>longitudeDialogDecimalValue.</p>
     *
     * @return The longitude/easting can only be a decimal number so far.
     */
    public abstract String longitudeDialogDecimalValue();
    
    /**
     * <p>longitudeEasting.</p>
     *
     * @return Longitude / Easting
     */
    public abstract String longitudeEasting();
    
    /**
     * <p>measuredValue.</p>
     *
     * @return measured value
     */
    public abstract String measuredValue();
    
    /**
     * <p>metadata.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String metadata();
    
    /**
     * <p>minutes.</p>
     *
     * @return Minutes
     */
    public abstract String minutes();
    
    /**
     * <p>month.</p>
     *
     * @return Month
     */
    public abstract String month();
    
    /**
     * <p>name.</p>
     *
     * @return Name
     */
    public abstract String name();
    
    /**
     * <p>nextButtonLabel.</p>
     *
     * @return Next
     */
    public abstract String nextButtonLabel();
    
    /**
     * <p>numValuePanelThousandsSeparator.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String numValuePanelThousandsSeparator();
    
    /**
     * <p>observation.</p>
     *
     * @return Observation
     */
    public abstract String observation();
    
    /**
     * <p>observedProperty.</p>
     *
     * @return Observed Property
     */
    public abstract String observedProperty();
    
    /**
     * <p>offering.</p>
     *
     * @return Offering
     */
    public abstract String offering();
    
    /**
     * <p>path.</p>
     *
     * @return path
     */
    public abstract String path();
    
    /**
     * <p>position.</p>
     *
     * @return Position
     */
    public abstract String position();
    
    /**
     * <p>referenceSystem.</p>
     *
     * @return Reference System
     */
    public abstract String referenceSystem();
    
    /**
     * <p>row.</p>
     *
     * @return row
     */
    public abstract String row();
    
    /**
     * <p>seconds.</p>
     *
     * @return Seconds
     */
    public abstract String seconds();
    
    /**
     * <p>sensor.</p>
     *
     * @return Sensor
     */
    public abstract String sensor();
    
    /**
     * <p>sos.</p>
     *
     * @return Sensor Observation Service
     */
    public String sos() {
    
        return "Sensor Observation Service";
    }
    
    /**
     * <p>spaceString.</p>
     *
     * @return Space
     */
    public abstract String spaceString();
    
    /**
     * <p>specificationVersion.</p>
     *
     * @return Specification Version
     */
    public abstract String specificationVersion();
    
    /**
     * <p>step.</p>
     *
     * @return Step
     */
    public abstract String step();
    
    /**
     * <p>step1BrowseButton.</p>
     *
     * @return Select
     */
    public abstract String step1BrowseButton();
    
    /**
     * <p>step1Description.</p>
     *
     * @return Step 1: Choose CSV file
     */
    public abstract String step1Description();
    
    /**
     * <p>step1Directory.</p>
     *
     * @return Remote Directory
     */
    public abstract String step1Directory();
    
    /**
     * <p>step1EncodingLabel.</p>
     *
     * @return Please select the input file encoding
     */
    public abstract String step1EncodingLabel();
    
    /**
     * <p>step1FeedTypeCSV.</p>
     *
     * @return CSV Feed Type
     */
    public abstract String step1FeedTypeCSV();
    
    /**
     * <p>step1FeedTypeFTP.</p>
     *
     * @return FTP Feed type
     */
    public abstract String step1FeedTypeFTP();
    
    /**
     * <p>step1File.</p>
     *
     * @return CSV File
     */
    public abstract String step1File();
    
    /**
     * <p>step1FileSchema.</p>
     *
     * @return Remote File Schema
     */
    public abstract String step1FileSchema();
    
    /**
     * <p>step1FtpServer.</p>
     *
     * @return FTP Server
     */
    public abstract String step1FtpServer();
    
    /**
     * <p>step1InstructionLabel.</p>
     *
     * @return Please select the CSV file
     */
    public abstract String step1InstructionLabel();
    
    /**
     * <p>step1Introduction.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String step1Introduction();
    
    /**
     * <p>step1Password.</p>
     *
     * @return Password (FTP Server authentification)
     */
    public abstract String step1Password();
    
    /**
     * <p>step1Regex.</p>
     *
     * @return Regular Expression
     */
    public abstract String step1Regex();
    
    /**
     * <p>step1RegexDescription.</p>
     *
     * @return Regular Expression Description
     */
    public abstract String step1RegexDescription();
    
    /**
     * <p>step1SelectLanguage.</p>
     *
     * @return Change language
     */
    public abstract String step1SelectLanguage();
    
    /**
     * <p>step1User.</p>
     *
     * @return User (FTP Server authentification)
     */
    public abstract String step1User();
    
    /**
     * <p>step2ColumnSeparator.</p>
     *
     * @return Column separator
     */
    public abstract String step2ColumnSeparator();
    
    /**
     * <p>step2CommentIndicator.</p>
     *
     * @return Comment indicator
     */
    public abstract String step2CommentIndicator();
    
    /**
     * <p>step2DataPreviewLabel.</p>
     *
     * @return CSV-Data-Preview
     */
    public abstract String step2DataPreviewLabel();
    
    /**
     * <p>step2DecimalSeparator.</p>
     *
     * @return Decimal separator
     */
    public abstract String step2DecimalSeparator();
    
    /**
     * <p>step2Description.</p>
     *
     * @return Step 2: Import CSV file
     */
    public abstract String step2Description();
    
    /**
     * <p>step2FirstLineWithData.</p>
     *
     * @return Ignore data until line
     */
    public abstract String step2FirstLineWithData();
    
    /**
     * <p>step2IsSampleBased.</p>
     *
     * @return Is data file sample based
     */
    public abstract String step2IsSampleBased();
    
    /**
     * <p>step2ParseHeader.</p>
     *
     * @return Interpret Header
     */
    public abstract String step2ParseHeader();

    /**
     * <p>step2SampleBasedDataOffsetLabel.</p>
     *
     * @return Offset data
     */
    public abstract String step2SampleBasedDataOffsetLabel();

    /**
     * <p>step2SampleBasedDataOffsetToolTip.</p>
     *
     * @return The offset in lines from sample beginning till the first lines
     *          with data.
     */
    public abstract String step2SampleBasedDataOffsetToolTip();

    /**
     * <p>step2SampleBasedDateExtractionRegExLabel.</p>
     *
     * @return Regular Expression "Date Extraction"
     */
    public abstract String step2SampleBasedDateExtractionRegExLabel();

    /**
     * <p>step2SampleBasedDateExtractionRegExTooltip.</p>
     *
     * @return The regular expression to extract the date information from the
     *              line containing the date information of the current sample.
     *              The expression MUST result in ONE group. This group will be
     *              parsed to a {@link java.util.Date} using
     *              "sampleDatePattern" attribute.
     */
    public abstract String step2SampleBasedDateExtractionRegExTooltip();

    /**
     * <p>step2SampleBasedDateOffsetLabel.</p>
     *
     * @return Offset date information
     */
    public abstract String step2SampleBasedDateOffsetLabel();

    /**
     * <p>step2SampleBasedDateOffsetToolTip.</p>
     *
     * @return The offset of the line containing the date of the
     *                      sample from the start line.
     */
    public abstract String step2SampleBasedDateOffsetToolTip();

    /**
     * <p>step2SampleBasedDatePatternLabel.</p>
     *
     * @return Parse Pattern "Date Information"
     */
    public abstract String step2SampleBasedDatePatternLabel();

    /**
     * <p>step2SampleBasedDatePatternTooltip.</p>
     *
     * @return The pattern used to parse the date information of the
     *          current sample.
     */
    public abstract String step2SampleBasedDatePatternTooltip();

    /**
     * <p>step2SampleBasedSampleSizeOffsetLabel.</p>
     *
     * @return Offset sample size
     */
    public abstract String step2SampleBasedSampleSizeOffsetLabel();

    /**
     * <p>step2SampleBasedSampleSizeOffsetToolTip.</p>
     *
     * @return The offset in lines from sample beginning till the line
     *          containing the sample size in lines with data.
     */
    public abstract String step2SampleBasedSampleSizeOffsetToolTip();

    /**
     * <p>step2SampleBasedSampleSizeRegExLabel.</p>
     *
     * @return Regular Expression "Sample Size"
     */
    public abstract String step2SampleBasedSampleSizeRegExLabel();

    /**
     * <p>step2SampleBasedSampleSizeRegExTooltip.</p>
     *
     * @return The regular expression to extract the sample size. The regular
     *              expression MUST result in ONE group which contains an
     *              integer value.
     */
    public abstract String step2SampleBasedSampleSizeRegExTooltip();

    /**
     * <p>step2SampleBasedStartRegExLabel.</p>
     *
     * @return Regular Expression "Sample Start"
     */
    public abstract String step2SampleBasedStartRegExLabel();

    /**
     * <p>step2SampleBasedStartRegExTooltip.</p>
     *
     * @return Used to identify the start of a new sample.\n
     *                          MUST match the whole line.
     */
    public abstract String step2SampleBasedStartRegExTooltip();

    /**
     * <p>step2TextQualifier.</p>
     *
     * @return Text qualifier
     */
    public abstract String step2TextQualifier();

    /**
     * <p>step3aDescription.</p>
     *
     * @return Step 3a: Choose Metadata for the selected column
     */
    public abstract String step3aDescription();

    /**
     * <p>step3aMeasureValueColMissingDialogMessage.</p>
     *
     * @return You have to specify at least one measured value column.
     */
    public abstract String step3aMeasureValueColMissingDialogMessage();

    /**
     * <p>step3aMeasureValueColMissingDialogTitle.</p>
     *
     * @return Measured value column missing
     */
    public abstract String step3aMeasureValueColMissingDialogTitle();

    /**
     * <p>step3aParseTest1Failed.</p>
     *
     * @return 1 value not parseable.
     */
    public abstract String step3aParseTest1Failed();

    /**
     * <p>step3aParseTestAllOk.</p>
     *
     * @return All values parseable.
     */
    public abstract String step3aParseTestAllOk();

    /**
     * <p>step3aParseTestNFailed.</p>
     *
     * @param n a int.
     * @return <code>n</code> values not parseable.
     */
    public abstract String step3aParseTestNFailed(int n);

    /**
     * <p>step3aSelectedColTypeUndefinedMsg.</p>
     *
     * @return The type for this column is "undefined" please select one. Chose "Do Not Export" for skipping it.
     */
    public abstract String step3aSelectedColTypeUndefinedMsg();

    /**
     * <p>step3aSelectedColTypeUndefinedTitle.</p>
     *
     * @return Column Type is "undefined"
     */
    public abstract String step3aSelectedColTypeUndefinedTitle();

    /**
     * <p>step3bDescription.</p>
     *
     * @return Step 3b: Choose metadata for rows
     */
    public abstract String step3bDescription();

    /**
     * <p>step3ColTypeDateTime.</p>
     *
     * @return Date &amp; Time
     */
    public abstract String step3ColTypeDateTime();

    /**
     * <p>step3ColTypeDoNotExport.</p>
     *
     * @return Do not export
     */
    public abstract String step3ColTypeDoNotExport();

    /**
     * <p>step3ColTypeMeasuredValue.</p>
     *
     * @return Measured Value
     */
    public abstract String step3ColTypeMeasuredValue();

    /**
     * <p>step3ColTypeUndefined.</p>
     *
     * @return Undefined
     */
    public abstract String step3ColTypeUndefined();

    /**
     * <p>step3DateAndTimeCombination.</p>
     *
     * @return Combination
     */
    public abstract String step3DateAndTimeCombination();

    /**
     * <p>step3DateAndTimeUnixTime.</p>
     *
     * @return UNIX time
     */
    public abstract String step3DateAndTimeUnixTime();

    /**
     * <p>step3MeasuredValBoolean.</p>
     *
     * @return Boolean
     */
    public abstract String step3MeasuredValBoolean();

    /**
     * <p>step3MeasuredValCount.</p>
     *
     * @return Count
     */
    public abstract String step3MeasuredValCount();

    /**
     * <p>step3MeasuredValNumericValue.</p>
     *
     * @return Numeric Value
     */
    public abstract String step3MeasuredValNumericValue();

    /**
     * <p>step3MeasuredValText.</p>
     *
     * @return Text
     */
    public abstract String step3MeasuredValText();

    /**
     * <p>step3PositionCombination.</p>
     *
     * @return Combination
     *
     * @see Lang#step3DateAndTimeCombination()
     */
    public abstract String step3PositionCombination();

    /**
     * <p>step4aDescription.</p>
     *
     * @return Step 4a: Solve Date &amp; Time ambiguities
     */
    public abstract String step4aDescription();

    /**
     * <p>step4aInfoDateAndTime.</p>
     *
     * @return Date and Time are already set for this <code>En.measuredValue()</code>.
     * @see Lang#measuredValue()
     */
    public abstract String step4aInfoDateAndTime();

    /**
     * <p>step4aInfoMeasuredValue.</p>
     *
     * @return This is not a <code>En.measuredValue()</code>.
     * @see Lang#measuredValue()
     */
    public abstract String step4aInfoMeasuredValue();

    /**
     * <p>step4aModelDescription.</p>
     *
     * @return Select all measured value <code>Constants.STRING_REPLACER</code>s 
     *      where the marked Date &amp; Time group corresponds to.
     */
    public abstract String step4aModelDescription();

    /**
     * <p>step4bDescription.</p>
     *
     * @return Step 4b: Solve ambiguities
     */
    public abstract String step4bDescription();

    /**
     * <p>step4bInfoNotMeasuredValue.</p>
     *
     * @return This is not a <code>En.measuredValue()</code>.
     * @see Lang#measuredValue()
     * @see Lang#step4aInfoMeasuredValue()
     */
    public abstract String step4bInfoNotMeasuredValue();
    
    /**
     * <p>step4bInfoResourceAlreadySetText.</p>
     *
     * @return a java.lang.String object.
     */
    public abstract String step4bInfoResourceAlreadySetText();
    
    /**
     * <p>step4bInfoResoureAlreadySet.</p>
     *
     * @param resource resource.className already set for this <code>En.measuredValue()</code>.
     * @return a java.lang.String object.
     */
    public String step4bInfoResoureAlreadySet(final Resource resource) {
        String res = "RESOURCE NOT FOUND!";
        if (resource instanceof FeatureOfInterest) {
            res = featureOfInterest();
        } else if (resource instanceof ObservedProperty) {
            res = observedProperty();
        } else if (resource instanceof Sensor) {
            res = sensor();
        } else if (resource instanceof UnitOfMeasurement) {
            res = unitOfMeasurement();
        }
        return res + step4bInfoResourceAlreadySetText() + measuredValue();
    }
    
    /**
     *
     * List how to replace the
     * {@link org.n52.sos.importer.Constants#STRING_REPLACER}
     * in the correct order:
     * <ol>
     *  <li>The table element type of the measured values, maybe "column"</li>
     *  <li>The resource type, that is linked to the measured value table element</li>
     *  <li>Table element of element to be selected</li>
     *  <li>Table element of element to be selected</li>
     *  <li>The resource type, that is linked to the measured value table element</li>
     *  <li>Table element of element to be selected</li>
     * </ol>
     *
     * @return Please click the <code>Constants.STRING_REPLACER</code> (not the title)
     * containing the measured values for the marked
     * <code>Constants.STRING_REPLACER</code> <code>Constants.STRING_REPLACER</code> that is
     * marked. If several <code>Constants.STRING_REPLACER</code>s correspond to this
     * <code>Constants.STRING_REPLACER</code> <code>Constants.STRING_REPLACER</code>, click
     * all of them with pressed CTRL key.
     */
    public abstract String step4bModelDescription();
    
    /**
     * <p>step5aDescription.</p>
     *
     * @return Step 5a: Complete time data
     */
    public abstract String step5aDescription();
    
    /**
     * <p>step5aModelDescription.</p>
     *
     * @return Complete missing information for the marked date and time.
     */
    public abstract String step5aModelDescription();
    
    /**
     * <p>step5cDescription.</p>
     *
     * @return Step 5c: Complete position data
     */
    public abstract String step5cDescription();
    
    /**
     * <p>step5cModelDescription.</p>
     *
     * @return Complete missing information for the marked position.
     */
    public abstract String step5cModelDescription();
    
    /**
     * <p>step6aDescription.</p>
     *
     * @return Step 6a: Add missing dates and times
     */
    public abstract String step6aDescription();
    
    /**
     * <p>step6aModelDescription.</p>
     *
     * @return What is the Date &amp; Time for all measured values?
     */
    public abstract String step6aModelDescription();
    
    /**
     * <p>step6bDefineConcatString.</p>
     *
     * @return Please provide a String to used to link the values in the columns (Empty String is allowed).
     */
    public abstract String step6bDefineConcatString();
    
    /**
     * <p>step6bDescription.</p>
     *
     * @return Step 6b: Add missing metadata
     */
    public abstract String step6bDescription();

    /**
     * Replacements: Resource &rarr; Orientation
     *
     * @return &lt;html&gt;What is the &lt;u&gt;<code>Constants.STRING_REPLACER</code>&lt;/u&gt; 
     *      for the marked measured value <code>Constants.STRING_REPLACER</code>?&lt;/html&gt;
     */
    public abstract String step6bModelDescription();

    /**
     * <p>step6bSelectColumnsLabel.</p>
     *
     * @return Please select the columns to generate the name.<br>
     *          \nMultiple columns could be selected.
     */
    public abstract String step6bSelectColumnsLabel();

    /**
     * <p>step6bSpecialDescription.</p>
     *
     * @return Step 6b (Special): Add missing sensors
     */
    public abstract String step6bSpecialDescription();

    /**
     * <p>step6bSpecialModelDescription.</p>
     *
     * @return What is the sensor for
     */
    public abstract String step6bSpecialModelDescription();

    /**
     * <p>step6bURIInstructions.</p>
     *
     * @return Please provide a URI or a prefix if using the name as part of the URI.
     */
    public abstract String step6bURIInstructions();

    /**
     * <p>step6bUseNameAfterPrefix.</p>
     *
     * @return Use Name after prefix?
     */
    public abstract String step6bUseNameAfterPrefix();

    /**
     * <p>step6cDescription.</p>
     *
     * @return Step 6c: Add missing positions
     */
    public abstract String step6cDescription();

    /**
     * <p>step6cInfoToolName.</p>
     *
     * @return Set Position
     */
    public abstract String step6cInfoToolName();

    /**
     * <p>step6cInfoToolTooltip.</p>
     *
     * @return Set the position by clicking on the map
     */
    public abstract String step6cInfoToolTooltip();

    /**
     * <p>step6cModelDescription.</p>
     *
     * @return What is the position of
     */
    public abstract String step6cModelDescription();

    /**
     * <p>step6Generation.</p>
     *
     * @return Automatically Generate Identifier
     */
    public abstract String step6Generation();

    /**
     * <p>step6ManualInput.</p>
     *
     * @return Set Identifier Manually
     */
    public abstract String step6ManualInput();

    /**
     * <p>step6MissingUserInput.</p>
     *
     * @return Some User Input is missing. Please enter the required information.
     */
    public abstract String step6MissingUserInput();

    /**
     * <p>step6NoUserInput.</p>
     *
     * @return No user input at all. Please fill in the required information.
     */
    public abstract String step6NoUserInput();

    /**
     * <p>step7ConfigDirNotDirOrWriteable.</p>
     *
     * @return The selected config file folder <code>folder</code> is not
     *          accessible for the application.
     * @param folder a {@link java.lang.String} object.
     */
    public abstract String step7ConfigDirNotDirOrWriteable(String folder);

    /**
     * <p>step7ConfigFileButton.</p>
     *
     * @return Choose configuration file export folder
     */
    public abstract String step7ConfigFileButton();

    /**
     * <p>step7ConfigFileDialogTitel.</p>
     *
     * @return Folder
     */
    public abstract String step7ConfigFileDialogTitel();

    /**
     * <p>step7ConfigFileLabel.</p>
     *
     * @return Configuration file folder and name
     */
    public abstract String step7ConfigFileLabel();

    /**
     * <p>step7ConfigurationFile.</p>
     *
     * @return Configuration File
     */
    public abstract String step7ConfigurationFile();

    /**
     * <p>step7ConfigFileInstructions.</p>
     *
     * @return Please set the folder for saving the configuration file.
     */
    public abstract String step7ConfigFileInstructions();

    /**
     * <p>step7Description.</p>
     *
     * @return Step 7: Final Configuration
     */
    public abstract String step7Description();

    /**
     * <p>step7DirectImport.</p>
     *
     * @return Directly import data during next step
     */
    public abstract String step7DirectImport();

    /**
     * <p>step7ImportStrategyBorderLabel.</p>
     *
     * @return Import Strategy
     */
    public abstract String step7ImportStrategyBorderLabel();

    /**
     * <p>step7ImportStrategyLabel.</p>
     *
     * @return Strategy
     */
    public abstract String step7ImportStrategyLabel();

    /**
     * <p>step7ImportStrategySingleObservation.</p>
     *
     * @return Single Observation
     */
    public abstract String step7ImportStrategySingleObservation();

    /**
     * <p>step7ImportStrategySweArrayHunksizeLabel.</p>
     *
     * @return Hunk size
     */
    public abstract String step7ImportStrategySweArrayHunksizeLabel();

    /**
     * <p>step7ImportStrategySweArrayObservation.</p>
     *
     * @return SweArrayObservation
     */
    public abstract String step7ImportStrategySweArrayObservation();

    /**
     * <p>step7ImportStrategySweArraySendBuffer.</p>
     *
     * @return Send Buffer
     */
    public abstract String step7ImportStrategySweArraySendBuffer();

    /**
     * <p>step7OfferingCheckBoxLabel.</p>
     *
     * @return Generate Offering from Sensor name?
     */
    public abstract String step7OfferingCheckBoxLabel();

    /**
     * <p>step7OfferingInputTextfieldLabel.</p>
     *
     * @return Please specify the offering name:
     */
    public abstract String step7OfferingInputTextfieldLabel();

    /**
     * <p>step7OfferingNameNotGiven.</p>
     *
     * @return Please specify the offering name or select to generate it.
     */
    public abstract String step7OfferingNameNotGiven();

    /**
     * The given offering "offeringName" is not valid. It should match XML-NCName specification.
     *
     * @param offeringName a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public abstract String step7OfferingNameNotValid(String offeringName);

    /**
     * <p>step7SaveConfig.</p>
     *
     * @return Save configuration to XML file
     */
    public abstract String step7SaveConfig();

    /**
     * <p>step7SosBindingInstructions.</p>
     *
     * @return Please specify the binding.
     */
    public abstract String step7SosBindingInstructions();

    /**
     * <p>step7SosBindingLabel.</p>
     *
     * @return Binding
     */
    public abstract String step7SosBindingLabel();

    /**
     * <p>step7SOSConncetionStart.</p>
     *
     * @param strURL a {@link java.lang.String} object.
     * @return To start connection testing to URL "<code>strURL</code>" select YES.\n To change values select NO.
     */
    public abstract String step7SOSConncetionStart(String strURL);

    /**
     * <p>step7SOSconnectionFailed.</p>
     *
     * @param strURL a {@link java.lang.String} object.
     * @param responseCode a int.
     * @return Could not connect to Sensor Observation Service: <code>strURL</code>. 
     *      HTTP Response Code: <code>responseCode</code>
     */
    public abstract String step7SOSconnectionFailed(String strURL, int responseCode);

    /**
     * <p>step7SOSConnectionFailedException.</p>
     *
     * @param strURL a {@link java.lang.String} object.
     * @param message a {@link java.lang.String} object.
     * @param readTimeoutSeconds a int.
     * @param connectTimeoutSeconds a int.
     * @return Connection to Sensor Observation Service
     *          \n<code>strURL</code>\n
     *          failed after <code>connectTimeoutSeconds</code> seconds connect
     *          and <code>readTimeoutSeconds</code> seconds read timeout.\n
     *          Reason: <code>message</code>
     */
    public abstract String step7SOSConnectionFailedException(String strURL,
            String message,
            int readTimeoutSeconds,
            int connectTimeoutSeconds);

    /**
     * <p>step7SosUrlInstructions.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String step7SosUrlInstructions();

    /**
     * <p>step7SosVersionInstructions.</p>
     *
     * @return Please specify the specification version that is implemented by 
     *      the SOS instance specified by the URL in the field above.
     */
    public abstract String step7SosVersionInstructions();

    /**
     * <p>step7SosVersionLabel.</p>
     *
     * @return Specification
     */
    public abstract String step7SosVersionLabel();

    /**
     * <p>step8ConfigFileButton.</p>
     *
     * @return Open Configuration File
     */
    public abstract String step8ConfigFileButton();

    /**
     * <p>step8ConfigurationFileInstructions.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String step8ConfigurationFileInstructions();

    /**
     * <p>step8Description.</p>
     *
     * @return Step 8: Final Step - Summary of the Results
     */
    public abstract String step8Description();

    /**
     * <p>step8DirectImportInstructions.</p>
     *
     * @return For importing the content of the data file one time, just click + step8DirectImportStartButton()
     */
    public abstract String step8DirectImportInstructions();

    /**
     * <p>step8DirectImportLabel.</p>
     *
     * @return Register Sensors and Insert Observations into Sensor Observation Service
     */
    public abstract String step8DirectImportLabel();

    /**
     * <p>step8DirectImportStartButton.</p>
     *
     * @return Start Import
     */
    public abstract String step8DirectImportStartButton();

    /**
     * <p>step8ErrorLable.</p>
     *
     * @param i a int.
     * @return Errors: <code>i</code>
     */
    public abstract String step8ErrorLable(int i);

    /**
     * <p>step8FeederJarNotFound.</p>
     *
     * @return Could not find jar file!\nPlease place it there:\n\"expectedAbsolutePathToFeederJar\"
     * @param expectedAbsolutePathToFeederJar a {@link java.lang.String} object.
     */
    public abstract String step8FeederJarNotFound(String expectedAbsolutePathToFeederJar);

    /**
     * <p>step8FeederJarNotFoundSelectByUser.</p>
     *
     * @return Could not find jar file here:\n\"expectedAbsolutePathToFeederJar\". Click YES to select it.
     * @param pathToDirectoryWithFeederJar a {@link java.lang.String} object.
     */
    public abstract String step8FeederJarNotFoundSelectByUser(String pathToDirectoryWithFeederJar);

    /**
     * <p>step8InsertObservationLabel.</p>
     *
     * @param i a int.
     * @return Insert <code>i</code> Observations...
     */
    public abstract String step8InsertObservationLabel(int i);

    /**
     * <p>step8LogFile.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String step8LogFile();

    /**
     * <p>step8LogFileButton.</p>
     *
     * @return Check log file
     */
    public abstract String step8LogFileButton();

    /**
     * <p>step8LogFileInstructions.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String step8LogFileInstructions();

    /**
     * <p>step8RegisterSensorLabel.</p>
     *
     * @param i a int.
     * @return Register <code>i</code> Sensors...
     */
    public abstract String step8RegisterSensorLabel(int i);

    /**
     * <p>step8SaveModelFailed.</p>
     *
     * @param f The file which was used to save the XML model
     * @return The configuration could not be saved to file
     *                      <br>"<code>f</code>".
     */
    public abstract String step8SaveModelFailed(File f);

    /**
     * <p>step8SaveModelFailed.</p>
     *
     * @param f The file which was used to save the XML model
     * @return The configuration could not be saved to file
     *                      <br>"<code>f</code>".
     *                      <br>An Exception occurred:
     *                      <br><code>exceptionText</code>.
     *                      <br>Please check the log file for more details.
     * @param exceptionText a {@link java.lang.String} object.
     */
    public abstract String step8SaveModelFailed(File f, String exceptionText);

    /**
     * <p>step8StartImportButton.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public abstract String step8StartImportButton();

    /**
     * <p>step8SuccessLabel.</p>
     *
     * @param i a int.
     * @return Successful: <code>i</code>
     */
    public abstract String step8SuccessLabel(int i);

    /**
     * <p>time.</p>
     *
     * @return Time
     */
    public abstract String time();

    /**
     * <p>timeZone.</p>
     *
     * @return UTC offset
     */
    public abstract String timeZone();

    /**
     * <p>unit.</p>
     *
     * @return Unit
     */
    public abstract String unit();

    /**
     * <p>unitOfMeasurement.</p>
     *
     * @return Unit of Measurement
     */
    public abstract String unitOfMeasurement();

    /**
     * <p>uri.</p>
     *
     * @return URI
     */
    public abstract String uri();

    /**
     * <p>uriSyntaxNotValidDialogMessage.</p>
     *
     * @param uri The String with the wrong syntax entered by the user
     * @return The entered URI "<code>uri</code>" is syntactically not correct.
     */
    public abstract String uriSyntaxNotValidDialogMessage(String uri);

    /**
     * <p>url.</p>
     *
     * @return URL
     */
    public abstract String url();

    /**
     * <p>version.</p>
     *
     * @return Version
     */
    public abstract String version();

    /**
     * <p>waitForParseResultsLabel.</p>
     *
     * @return Testing evaluation pattern for column
     */
    public abstract String waitForParseResultsLabel();

    /**
     * <p>warningDialogTitle.</p>
     *
     * @return Warning
     */
    public abstract String warningDialogTitle();

    /**
     * <p>year.</p>
     *
     * @return Year
     */
    public abstract String year();

}
