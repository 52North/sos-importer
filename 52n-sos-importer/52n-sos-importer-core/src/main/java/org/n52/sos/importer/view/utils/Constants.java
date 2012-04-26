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
package org.n52.sos.importer.view.utils;

import java.awt.Color;
import java.awt.Font;

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.n52.sos.importer.view.i18n.Lang;

/**
 * @author e.h.juerrens@52north.org
 *
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
	public static final boolean GUI_DEBUG = false;
	public static final int NO_INPUT_INT = Integer.MIN_VALUE;
	public static final String NUMERIC = "NUMERIC";
	/**
	 * Is used to distinguish between line number and content
	 */
	public static final String RAW_DATA_SEPARATOR = ":";
	public final static String SEPARATOR_STRING = "SEP";
	public static final String SPACE_STRING = Lang.l().spaceString();
	public static final String STRING_REPLACER = "-@@@-";
	public static final String TEXT = "TEXT";
	public static final String UNIX_TIME = "UNIX_TIME";
	public static final String VERSION = "0.2 RC1";
	public static final String WELCOME_RES_CONTENT_TYPE = "text/html";
	public static final String XML_CONFIG_DEFAULT_FILE_NAME = "sos-importer-csv-file-configuration.xml";
	public static final Font DEFAULT_INSTRUCTIONS_FONT_LARGE_BOLD = new Font("SansSerif", Font.BOLD, 12);;
	
	/**
	 * TODO implement loading of language parameter from config file
	 * @return {@linkplain org.n52.sos.importer.view.i18n.Lang.l().getClass().getSimpleName()}
	 */
	public static String language() {
		return Lang.l().getClass().getSimpleName();
	}

}
