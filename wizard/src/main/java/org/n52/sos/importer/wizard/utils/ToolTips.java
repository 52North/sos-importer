/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.sos.importer.wizard.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ToolTipManager;

import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * loads tooltips from the properties file
 *
 * @author Raimund
 */
public final class ToolTips {


    /*
     * For the following keys tooltips are available
     */
    /** Constant <code>CSV_FILE="CSVFile"</code> */
    public static final String CSV_FILE = "CSVFile";
    /** Constant <code>COLUMN_SEPARATOR="ColumnSeparator"</code> */
    public static final String COLUMN_SEPARATOR = "ColumnSeparator";
    /** Constant <code>COMMENT_INDICATOR="CommentIndicator"</code> */
    public static final String COMMENT_INDICATOR = "CommentIndicator";
    /** Constant <code>TEXT_QUALIFIER="TextQualifier"</code> */
    public static final String TEXT_QUALIFIER = "TextQualifier";
    /** Constant <code>MEASURED_VALUE="MeasuredValue"</code> */
    public static final String MEASURED_VALUE = "MeasuredValue";
    /** Constant <code>DATE_AND_TIME="DateAndTime"</code> */
    public static final String DATE_AND_TIME = "DateAndTime";
    /** Constant <code>POSITION="Position"</code> */
    public static final String POSITION = "Position";
    /** Constant <code>FEATURE_OF_INTEREST="FeatureOfInterest"</code> */
    public static final String FEATURE_OF_INTEREST = "FeatureOfInterest";
    /** Constant <code>OBSERVED_PROPERTY="ObservedProperty"</code> */
    public static final String OBSERVED_PROPERTY = "ObservedProperty";
    /** Constant <code>UNIT_OF_MEASUREMENT="UnitOfMeasurement"</code> */
    public static final String UNIT_OF_MEASUREMENT = "UnitOfMeasurement";
    /** Constant <code>SENSOR="Sensor"</code> */
    public static final String SENSOR = "Sensor";
    /** Constant <code>TEXT="Text"</code> */
    public static final String TEXT = "Text";
    /** Constant <code>BOOLEAN="Boolean"</code> */
    public static final String BOOLEAN = "Boolean";
    /** Constant <code>COUNT="Count"</code> */
    public static final String COUNT = "Count";
    /** Constant <code>NUMERIC_VALUE="NumericValue"</code> */
    public static final String NUMERIC_VALUE = "NumericValue";
    /** Constant <code>DECIMAL_SEPARATOR="DecimalSeparator"</code> */
    public static final String DECIMAL_SEPARATOR = "DecimalSeparator";
    /** Constant <code>THOUSANDS_SEPARATOR="ThousandsSeparator"</code> */
    public static final String THOUSANDS_SEPARATOR = "ThousandsSeparator";
    /** Constant <code>TIME_ZONE="UTCOffset"</code> */
    public static final String TIME_ZONE = "TimeZone";
    /** Constant <code>EPSG="EPSG"</code> */
    public static final String EPSG = "EPSG";
    /** Constant <code>REFERENCE_SYSTEMS="ReferenceSystem"</code> */
    public static final String REFERENCE_SYSTEMS = "ReferenceSystem";
    /** Constant <code>DATE_AND_TIME_PATTERNS="DateAndTimePatterns"</code> */
    public static final String DATE_AND_TIME_PATTERNS = "DateAndTimePatterns";
    /** Constant <code>DATE_AND_TIME_GROUPS="DateAndTimeGroups"</code> */
    public static final String DATE_AND_TIME_GROUPS = "DateAndTimeGroups";
    /** Constant <code>OM_PARAMETER="omParameter"</code> */
    public static final String OM_PARAMETER = "omParameter";
    /** Constant <code>OM_PARAMETER_CATEGORY="omParameterCategory"</code> */
    public static final String OM_PARAMETER_CATEGORY = "omParameterCategory";
    /** Constant <code>POSITION_PATTERNS="PositionPatterns"</code> */
    public static final String POSITION_PATTERNS = "PositionPatterns";
    /** Constant <code>POSITION_GROUPS="PositionGroups"</code> */
    public static final String POSITION_GROUPS = "PositionGroups";
    /** Constant <code>SOS="SOS"</code> */
    public static final String SOS = "SOS";
    /** Constant <code>NAME="Name"</code> */
    public static final String NAME = "Name";
    /** Constant <code>URI="URI"</code> */
    public static final String URI = "URI";
    /** Constant <code>STEP6_AUTOMATIC_GENERATION_COLUMN_TOOLTIPS="Step6AutogenerationColumn"</code> */
    public static final String STEP6_AUTOMATIC_GENERATION_COLUMN_TOOLTIPS = "Step6AutogenerationColumn";
    /** Constant <code>OFFERING="Offering"</code> */
    public static final String OFFERING = "Offering";
    /** Constant <code>IGNORE_COLUMN_MISMATCH="IGNORE_COLUMN_MISMATCH"</code> */
    public static final String IGNORE_COLUMN_MISMATCH = "IGNORE_COLUMN_MISMATCH";

    private static final Logger logger = LoggerFactory.getLogger(ToolTips.class);
    private static final String BUNDLE_NAME = "org.n52.sos.importer.tooltips.tooltips";
    private static ResourceBundle res;

    private ToolTips() {}

    /**
     * <p>get.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String get(final String key) {
        try {
            if (res == null) {
                res = ResourceBundle.getBundle(BUNDLE_NAME, Lang.getCurrentLocale());
            }
            return res.getString(key);
        } catch (final MissingResourceException e) {
            logger.error("Value for key: \"" +
                        key +
                        "\" not found in resource bundle \""
                        + res
                        + "\".", e);
            return '!' + key + '!';
        }
    }

    /**
     * <p>loadSettings.</p>
     */
    public static void loadSettings() {
        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setDismissDelay(50000);
        ToolTipManager.sharedInstance().setReshowDelay(500);
        res = ResourceBundle.getBundle(BUNDLE_NAME, Lang.getCurrentLocale());
    }
}
