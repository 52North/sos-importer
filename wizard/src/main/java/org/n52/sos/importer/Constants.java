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
package org.n52.sos.importer;

import java.awt.Color;
import java.awt.Font;
import java.util.regex.Pattern;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.xml.namespace.QName;

import org.n52.sos.importer.view.i18n.Lang;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * TODO move to shared module all constants that need to be shared!
 */
public class Constants {

	public static enum ImportStrategy {
        /**
         * Each value will be inserted as single observation into the SOS.
         */
        SingleObservation,
        /**
         * The file will be read at once. For each identified timeseries
         * an OM_SWEArrayObservation will be created and inserted into the
         * SOS using the "SplitDataArrayIntoObservations" of the 52North
         * implementation.
         */
        SweArrayObservationWithSplitExtension;
    }

	public static final String BOOLEAN = "BOOLEAN";
	public static final String COMBINATION = "COMBINATION";
	public static final String COUNT = "COUNT";
	public static final String DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
	public static final Border DEBUG_BORDER = new LineBorder(Color.RED,1,true);
	private static final int DEFAULT__COLOR_BACKGROUND_COMPONENT_BLUE = 238;
	private static final int DEFAULT__COLOR_BACKGROUND_COMPONENT_GREEN = 238;
	private static final int DEFAULT__COLOR_BACKGROUND_COMPONENT_RED = 238;
	public static final Color DEFAULT_COLOR_BACKGROUND = Color.getHSBColor(
			Color.RGBtoHSB(DEFAULT__COLOR_BACKGROUND_COMPONENT_RED,
					DEFAULT__COLOR_BACKGROUND_COMPONENT_GREEN,
					DEFAULT__COLOR_BACKGROUND_COMPONENT_BLUE, null)[0],
			Color.RGBtoHSB(DEFAULT__COLOR_BACKGROUND_COMPONENT_RED,
					DEFAULT__COLOR_BACKGROUND_COMPONENT_GREEN,
					DEFAULT__COLOR_BACKGROUND_COMPONENT_BLUE, null)[1],
			Color.RGBtoHSB(DEFAULT__COLOR_BACKGROUND_COMPONENT_RED,
					DEFAULT__COLOR_BACKGROUND_COMPONENT_GREEN,
					DEFAULT__COLOR_BACKGROUND_COMPONENT_BLUE, null)[2]);
	public static final Font DEFAULT_LABEL_FONT = new Font("SansSerif", Font.PLAIN, 12);
	public static final Font DEFAULT_LABEL_FONT_BOLD =  new Font("SansSerif", Font.BOLD, 12);
	public static final Font DEFAULT_STEP_TITLE_FONT = new Font("SansSerif", Font.BOLD, 14);
	public static final int NO_INPUT_INT = Integer.MIN_VALUE;
	public static final String NUMERIC = "NUMERIC";
	/**
	 * Is used to distinguish between line number and content
	 */
	public static final String RAW_DATA_SEPARATOR = ":";
	public static final CharSequence SOS_RESPONSE_EXCEPTION_SENSOR_ALREADY_REGISTERED_START = "Sensor with ID: '";
	public static final CharSequence SOS_RESPONSE_EXCEPTION_SENSOR_ALREADY_REGISTERED_END = "' is already registered at ths SOS!";
	public static final CharSequence SOS_RESPONSE_EXCEPTION_CODE_NO_APPLICABLE_CODE = "exceptionCode=\"NoApplicableCode\"";
	public static final String XML_SCHEMA_PREFIX = "xsi";
	public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
	public static final QName XML_SCHEMALOCATION_QNAME = new QName(XML_SCHEMA_NAMESPACE,"schemaLocation",XML_SCHEMA_PREFIX);
	public static final String XML_SOS_IMPORTER_SCHEMA_LOCATION = "https://raw.githubusercontent.com/52North/sos-importer/master/bindings/src/main/resources/import-configuration.xsd";
	public static final String UNICODE_OFFERING_PREFIX = "_offering-";
	public final static String SEPARATOR_STRING = "SEP";
	public static final String SPACE_STRING = Lang.l().spaceString();
	public static final String STRING_REPLACER = "-@@@-";
	public static final String TEXT = "TEXT";
	public static final String UNIX_TIME = "UNIX_TIME";
	public static final String VERSION = "0.4";
	public static final String WELCOME_RES_CONTENT_TYPE = "text/html";
	public static final String XML_CONFIG_DEFAULT_FILE_NAME_SUFFIX = ".52n-sos-import-config.xml";
	public static final Font DEFAULT_INSTRUCTIONS_FONT_LARGE_BOLD = new Font("SansSerif", Font.BOLD, 12);
	public static final Color DEFAULT_HIGHLIGHT_COLOR = Color.LIGHT_GRAY;
	public static final int URL_CONNECT_TIMEOUT_SECONDS = 5;
	public static final int URL_CONNECT_READ_TIMEOUT_SECONDS = 5;
	public static final String XML_BINDINGS_NAMESPACE = "http://52north.org/sensorweb/sos/importer/0.4/";
	public static final QName QNAME_GENERATED_RESOURCE = new QName(XML_BINDINGS_NAMESPACE, "GeneratedResource");
	public static final QName QNAME_GENERATED_SPATIAL_RESOURCE = new QName(XML_BINDINGS_NAMESPACE, "GeneratedSpatialResource");
	public static final QName QNAME_MANUAL_RESOURCE = new QName(XML_BINDINGS_NAMESPACE,"ManualResource");
	public static final QName QNAME_MANUAL_SPATIAL_RESOURCE = new QName(XML_BINDINGS_NAMESPACE,"SpatialResource");
	public static final String[] DECIMAL_SEPARATORS = {".",","};
	public static final String DEFAULT_LATITUDE_UNIT = "deg";
	public static final String DEFAULT_LONGITUDE_UNIT = "deg";
	public static final String DEFAULT_ALTITUDE_UNIT = "m";
	// TODO read from file!
	public static final char UNICODE_REPLACER = '_';
	public static final Pattern UNICODE_ONLY_REPLACER_LEFT_PATTERN = Pattern.compile(UNICODE_REPLACER + "+");
	public static final String UNICODE_FOI_PREFIX = "_foi-";
	public static final String SOS_GET_GET_CAPABILITIES_REQUEST = "?service=SOS&REQUEST=GetCapabilities&AcceptVersions=1.0.0";
	public static final int DEFAULT_EPSG_CODE = 4326;
	public static final double DEFAULT_HEIGHT_FOI_POSITION = 0.0;
	public static final String DEFAULT_HEIGHT_UNIT_FOI_POSITION = "m";
	public static final String DEFAULT_UNIT_FOI_POSITION = "deg";

	public static final String DEFAULT_FEEDER_JAR_NAME_START = "52n-sos-importer-feeder-";

	public final static int DIALOG_WIDTH = 800;

	public final static int DIALOG_HEIGHT = 600;

	public static final String WMS_DEFAULT_BACKGROUND_LAYER_NAME = "OSMBackground";


	/*
	 * CHANGEABLE VALUES
	 */
	public static boolean GUI_DEBUG = false;
	public static char DECIMAL_SEPARATOR = '.';
	public static char THOUSANDS_SEPARATOR = ',';

	/**
	 * TODO implement loading of language parameter from configuration file
	 * @return {@link org.n52.sos.importer.view.i18n.Lang.l().getClass().getSimpleName()}
	 */
	public static String language() {
		return Lang.l().getClass().getSimpleName();
	}

}
