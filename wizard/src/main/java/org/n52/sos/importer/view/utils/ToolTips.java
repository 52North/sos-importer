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
package org.n52.sos.importer.view.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ToolTipManager;

import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * loads tooltips from the properties file
 * @author Raimund
 *
 */
public class ToolTips {
	
	private static final Logger logger = LoggerFactory.getLogger(ToolTips.class);
	
	private static final String BUNDLE_NAME = "org.n52.sos.importer.tooltips.tooltips"; //$NON-NLS-1$
	
	private static ResourceBundle res;

	/*
	 * For the following keys tooltips are available
	 */
	public static final String CSV_File = "CSVFile";
	public static final String COLUMN_SEPARATOR = "ColumnSeparator";
	public static final String COMMENT_INDICATOR = "CommentIndicator";
	public static final String TEXT_QUALIFIER = "TextQualifier";
	public static final String MEASURED_VALUE = "MeasuredValue";
	public static final String DATE_AND_TIME = "DateAndTime";
	public static final String POSITION = "Position";
	public static final String FEATURE_OF_INTEREST = "FeatureOfInterest";
	public static final String OBSERVED_PROPERTY = "ObservedProperty";
	public static final String UNIT_OF_MEASUREMENT = "UnitOfMeasurement";
	public static final String SENSOR = "Sensor";
	public static final String TEXT = "Text";
	public static final String BOOLEAN = "Boolean";
	public static final String COUNT = "Count";
	public static final String NUMERIC_VALUE = "NumericValue";
	public static final String DECIMAL_SEPARATOR = "DecimalSeparator";
	public static final String THOUSANDS_SEPARATOR = "ThousandsSeparator";
	public static final String TIME_ZONE = "UTCOffset";
	public static final String EPSG = "EPSG";
	public static final String REFERENCE_SYSTEMS = "ReferenceSystem";
	public static final String DATE_AND_TIME_PATTERNS = "DateAndTimePatterns";
	public static final String DATE_AND_TIME_GROUPS = "DateAndTimeGroups";
	public static final String POSITION_PATTERNS = "PositionPatterns";
	public static final String POSITION_GROUPS = "PositionGroups";
	public static final String SOS = "SOS";
	public static final String NAME = "Name";
	public static final String URI = "URI";
	public static final String STEP6_AUTOMATIC_GENERATION_COLUMN_TOOLTIPS = "Step6AutogenerationColumn";
	public static final String OFFERING = "Offering";

	private ToolTips() {}

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
						+ "\".",e);
			return '!' + key + '!';
		}
	}
	
	public static void loadSettings() {
		ToolTipManager.sharedInstance().setInitialDelay(500);
		ToolTipManager.sharedInstance().setDismissDelay(50000);
		ToolTipManager.sharedInstance().setReshowDelay(500);
		res = ResourceBundle.getBundle(BUNDLE_NAME, Lang.getCurrentLocale());
	}
}
