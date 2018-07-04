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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.xml.namespace.QName;

import org.n52.sos.importer.view.i18n.Lang;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * <p>Constants class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * TODO move to shared module all constants that need to be shared!
 */
public class Constants {

    public static final String SANS_SERIF = "SansSerif";
    public static final String DEG = "°";

    public enum ImportStrategy {
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
        SweArrayObservationWithSplitExtension,
        /**
         * The collected observations will be inserted using the SOS
         * Result Handling extension.
         */
        ResultHandling;
    }

    /** Constant <code>BOOLEAN="BOOLEAN"</code> */
    public static final String BOOLEAN = "BOOLEAN";
    /** Constant <code>CATEGORY="CATEGORY"</code> */
    public static final String CATEGORY = "CATEGORY";
    /** Constant <code>COMBINATION="COMBINATION"</code> */
    public static final String COMBINATION = "COMBINATION";
    /** Constant <code>COUNT="COUNT"</code> */
    public static final String COUNT = "COUNT";
    /** Constant <code>DATE_FORMAT_STRING="yyyy-MM-dd'T'HH:mm:ssZ"</code> */
    public static final String DATE_FORMAT_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
    /** Constant <code>DEBUG_BORDER</code> */
    public static final Border DEBUG_BORDER = new LineBorder(Color.RED, 1, true);
    /** Constant <code>DEFAULT_LABEL_FONT</code> */
    public static final Font DEFAULT_LABEL_FONT = new Font(SANS_SERIF, Font.PLAIN, 12);
    /** Constant <code>DEFAULT_LABEL_FONT_BOLD</code> */
    public static final Font DEFAULT_LABEL_FONT_BOLD =  new Font(SANS_SERIF, Font.BOLD, 12);
    /** Constant <code>DEFAULT_STEP_TITLE_FONT</code> */
    public static final Font DEFAULT_STEP_TITLE_FONT = new Font(SANS_SERIF, Font.BOLD, 14);
    /** Constant <code>NO_INPUT_INT=Integer.MIN_VALUE</code> */
    public static final int NO_INPUT_INT = Integer.MIN_VALUE;
    /** Constant <code>NUMERIC="NUMERIC"</code> */
    public static final String NUMERIC = "NUMERIC";
    /** Constant <code>DEFAULT_COLOR_BACKGROUND</code> */
    public static final int DEFAULT__COLOR_BACKGROUND_COMPONENT_BLUE = 238;
    public static final int DEFAULT__COLOR_BACKGROUND_COMPONENT_GREEN = 238;
    public static final int DEFAULT__COLOR_BACKGROUND_COMPONENT_RED = 238;
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
    /**
     * Is used to distinguish between line number and content
     */
    public static final String RAW_DATA_SEPARATOR = ":";
    /** Constant <code>SOS_RESPONSE_EXCEPTION_SENSOR_ALREADY_REGISTERED_START</code> */
    public static final CharSequence SOS_RESPONSE_EXCEPTION_SENSOR_ALREADY_REGISTERED_START = "Sensor with ID: '";
    /** Constant <code>SOS_RESPONSE_EXCEPTION_SENSOR_ALREADY_REGISTERED_END</code> */
    public static final CharSequence SOS_RESPONSE_EXCEPTION_SENSOR_ALREADY_REGISTERED_END =
            "' is already registered at ths SOS!";
    /** Constant <code>SOS_RESPONSE_EXCEPTION_CODE_NO_APPLICABLE_CODE</code> */
    public static final CharSequence SOS_RESPONSE_EXCEPTION_CODE_NO_APPLICABLE_CODE =
            "exceptionCode=\"NoApplicableCode\"";
    /** Constant <code>XML_SCHEMA_PREFIX="xsi"</code> */
    public static final String XML_SCHEMA_PREFIX = "xsi";
    /** Constant <code>XML_SCHEMA_NAMESPACE="http://www.w3.org/2001/XMLSchema-instan"{trunked}</code> */
    public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";
    /** Constant <code>XML_SCHEMALOCATION_QNAME</code> */
    public static final QName XML_SCHEMALOCATION_QNAME =
            new QName(XML_SCHEMA_NAMESPACE, "schemaLocation", XML_SCHEMA_PREFIX);
    /** Constant <code>XML_SOS_IMPORTER_SCHEMA_LOCATION="https://raw.githubusercontent.com/52Nor"{trunked}</code> */
    public static final String XML_SOS_IMPORTER_SCHEMA_LOCATION =
            "https://raw.githubusercontent.com/52North/sos-importer/"
            + "master/bindings/src/main/resources/import-configuration.xsd";
    /** Constant <code>UNICODE_OFFERING_PREFIX="_offering-"</code> */
    public static final String UNICODE_OFFERING_PREFIX = "_offering-";
    /** Constant <code>SEPARATOR_STRING="SEP"</code> */
    public static final String SEPARATOR_STRING = "SEP";
    /** Constant <code>SPACE_STRING="Lang.l().spaceString()"</code> */
    public static final String SPACE_STRING = Lang.l().spaceString();
    /** Constant <code>STRING_REPLACER="-@@@-"</code> */
    public static final String STRING_REPLACER = "-@@@-";
    /** Constant <code>TEXT="TEXT"</code> */
    public static final String TEXT = "TEXT";
    /** Constant <code>UNIX_TIME="UNIX_TIME"</code> */
    public static final String UNIX_TIME = "UNIX_TIME";
    /** Constant <code>VERSION="0.5.0-SNAPSHOT"</code> */
    public static final String VERSION = "0.5.0-SNAPSHOT";
    /** Constant <code>WELCOME_RES_CONTENT_TYPE="text/html"</code> */
    public static final String WELCOME_RES_CONTENT_TYPE = "text/html";
    /** Constant <code>XML_CONFIG_DEFAULT_FILE_NAME_SUFFIX=".52n-sos-import-config.xml"</code> */
    public static final String XML_CONFIG_DEFAULT_FILE_NAME_SUFFIX = ".52n-sos-import-config.xml";
    /** Constant <code>DEFAULT_INSTRUCTIONS_FONT_LARGE_BOLD</code> */
    public static final Font DEFAULT_INSTRUCTIONS_FONT_LARGE_BOLD = new Font(SANS_SERIF, Font.BOLD, 12);
    /** Constant <code>DEFAULT_HIGHLIGHT_COLOR</code> */
    public static final Color DEFAULT_HIGHLIGHT_COLOR = Color.LIGHT_GRAY;
    /** Constant <code>URL_CONNECT_TIMEOUT_SECONDS=5</code> */
    public static final int URL_CONNECT_TIMEOUT_SECONDS = 5;
    /** Constant <code>URL_CONNECT_READ_TIMEOUT_SECONDS=5</code> */
    public static final int URL_CONNECT_READ_TIMEOUT_SECONDS = 5;
    /** Constant <code>XML_BINDINGS_NAMESPACE</code> */
    public static final String XML_BINDINGS_NAMESPACE =
            SosImportConfiguration.Factory.newInstance().addNewAdditionalMetadata().getDomNode().getNamespaceURI();
    // "http://52north.org/sensorweb/sos/importer/0.5/";
    /** Constant <code>QNAME_GENERATED_RESOURCE</code> */
    public static final QName QNAME_GENERATED_RESOURCE = new QName(XML_BINDINGS_NAMESPACE, "GeneratedResource");
    /** Constant <code>QNAME_GENERATED_SPATIAL_RESOURCE</code> */
    public static final QName QNAME_GENERATED_SPATIAL_RESOURCE =
            new QName(XML_BINDINGS_NAMESPACE, "GeneratedSpatialResource");
    /** Constant <code>QNAME_MANUAL_RESOURCE</code> */
    public static final QName QNAME_MANUAL_RESOURCE = new QName(XML_BINDINGS_NAMESPACE, "ManualResource");
    /** Constant <code>QNAME_MANUAL_SPATIAL_RESOURCE</code> */
    public static final QName QNAME_MANUAL_SPATIAL_RESOURCE = new QName(XML_BINDINGS_NAMESPACE, "SpatialResource");
    /** Constant <code>DECIMAL_SEPARATORS="{.,,}"</code> */
    public static final List<String> DECIMAL_SEPARATORS = Collections.unmodifiableList(Arrays.asList(".", ","));
    /** Constant <code>DEFAULT_LATITUDE_UNIT="°"</code> */
    public static final String DEFAULT_LATITUDE_UNIT = DEG;
    /** Constant <code>DEFAULT_LONGITUDE_UNIT="°"</code> */
    public static final String DEFAULT_LONGITUDE_UNIT = DEG;
    /** Constant <code>DEFAULT_ALTITUDE_UNIT="m"</code> */
    public static final String DEFAULT_ALTITUDE_UNIT = "m";
    // TODO read from file!
    /** Constant <code>UNICODE_REPLACER='_'</code> */
    public static final char UNICODE_REPLACER = '_';
    /** Constant <code>UNICODE_ONLY_REPLACER_LEFT_PATTERN</code> */
    public static final Pattern UNICODE_ONLY_REPLACER_LEFT_PATTERN = Pattern.compile(UNICODE_REPLACER + "+");
    /** Constant <code>UNICODE_FOI_PREFIX="_foi-"</code> */
    public static final String UNICODE_FOI_PREFIX = "_foi-";
    /** Constant <code>SOS_GET_GET_CAPABILITIES_REQUEST=
     * "?service=SOS&amp;REQUEST=GetCapabilities&amp;AcceptVersions=1.0.0"</code> */
    public static final String SOS_GET_GET_CAPABILITIES_REQUEST =
            "?service=SOS&REQUEST=GetCapabilities&AcceptVersions=1.0.0";
    /** Constant <code>DEFAULT_EPSG_CODE=4326</code> */
    public static final int DEFAULT_EPSG_CODE = 4326;
    /** Constant <code>DEFAULT_HEIGHT_FOI_POSITION=0.0</code> */
    public static final double DEFAULT_HEIGHT_FOI_POSITION = 0.0;
    /** Constant <code>DEFAULT_HEIGHT_UNIT_FOI_POSITION="m"</code> */
    public static final String DEFAULT_HEIGHT_UNIT_FOI_POSITION = "m";
    /** Constant <code>DEFAULT_UNIT_FOI_POSITION="°"</code> */
    public static final String DEFAULT_UNIT_FOI_POSITION = DEG;

    /** Constant <code>DEFAULT_FEEDER_JAR_NAME_START="52n-sos-importer-feeder-"</code> */
    public static final String DEFAULT_FEEDER_JAR_NAME_START = "52n-sos-importer-feeder-";

    /** Constant <code>DIALOG_WIDTH=800</code> */
    public static final int DIALOG_WIDTH = 800;

    /** Constant <code>DIALOG_HEIGHT=600</code> */
    public static final int DIALOG_HEIGHT = 600;

    /** Constant <code>WMS_DEFAULT_BACKGROUND_LAYER_NAME="OSMBackground"</code> */
    public static final String WMS_DEFAULT_BACKGROUND_LAYER_NAME = "OSMBackground";

    public static final String UTF_8 = "UTF-8";
    public static final String DEFAULT_FILE_ENCODING = UTF_8;

    /*
     * CHANGEABLE VALUES
     */
    /** Constant <code>GUI_DEBUG=false</code> */
    private static boolean GUI_DEBUG;
    /** Constant <code>DECIMAL_SEPARATOR='.'</code> */
    private static char DECIMAL_SEPARATOR = '.';
    /** Constant <code>THOUSANDS_SEPARATOR=','</code> */
    private static char THOUSANDS_SEPARATOR = ',';

    public static boolean isGuiDebug() {
        return GUI_DEBUG;
    }

    public static void setGuiDebug(boolean guiDebug) {
        GUI_DEBUG = guiDebug;
    }

    public static char getDecimalSeparator() {
        return DECIMAL_SEPARATOR;
    }

    public static void setDecimalSeparator(char decimalSeparator) {
        DECIMAL_SEPARATOR = decimalSeparator;
    }

    public static char getThousandsSeparator() {
        return THOUSANDS_SEPARATOR;
    }

    public static void setThousandsSeparator(char thousandsSeparator) {
        THOUSANDS_SEPARATOR = thousandsSeparator;
    }

    /**
     * TODO implement loading of language parameter from configuration file
     *
     * @return The simple name string of the currently loaded {@link Lang} implementation.
     */
    public static String language() {
        return Lang.l().getClass().getSimpleName();
    }
}
