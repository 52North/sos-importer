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
	public static final String XML_SOS_IMPORTER_SCHEMA_LOCATION = "http://52north.org/sensorweb/sos/importer/0.2/ http://svn.52north.org/svn/swe/incubation/SOS-importer/trunk/52n-sos-importer/52n-sos-importer-bindings/src/main/xsd/datafile_configuration.xsd";
	public static final String UNICODE_OFFERING_PREFIX = "_offering-";
	public final static String SEPARATOR_STRING = "SEP";
	public static final String SPACE_STRING = Lang.l().spaceString();
	public static final String STRING_REPLACER = "-@@@-";
	public static final String TEXT = "TEXT";
	public static final String UNIX_TIME = "UNIX_TIME";
	public static final String VERSION = "0.2 RC1";
	public static final String WELCOME_RES_CONTENT_TYPE = "text/html";
	public static final String XML_CONFIG_DEFAULT_FILE_NAME_SUFFIX = ".52n-sos-import-config.xml";
	public static final Font DEFAULT_INSTRUCTIONS_FONT_LARGE_BOLD = new Font("SansSerif", Font.BOLD, 12);
	public static final Color DEFAULT_HIGHLIGHT_COLOR = Color.RED;
	public static final int URL_CONNECT_TIMEOUT_SECONDS = 5;
	public static final int URL_CONNECT_READ_TIMEOUT_SECONDS = 5;
	public static final String XML_BINDINGS_NAMESPACE = "http://52north.org/sensorweb/sos/importer/0.2/";
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
	/*
	 * CHANGEABLE VALUES
	 */
	public static boolean GUI_DEBUG = false;
	public static char DECIMAL_SEPARATOR = '.';
	public static char THOUSANDS_SEPARATOR = ',';
	
	/**
	 * TODO implement loading of language parameter from configuration file
	 * @return {@linkplain org.n52.sos.importer.view.i18n.Lang.l().getClass().getSimpleName()}
	 */
	public static String language() {
		return Lang.l().getClass().getSimpleName();
	}

}
