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

import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.n52.sos.importer.view.i18n.Lang;

/**
 * @author e.h.juerrens@52north.org
 *
 */
public class Constants {
	
	public final static String SEPARATOR_STRING = "SEP";
	public static final String SPACE_STRING = Lang.l().spaceString();
	public static final String WELCOME_RES_CONTENT_TYPE = "text/html";
	/**
	 * Is used to distinguish between line number and content
	 */
	public static final String RAW_DATA_SEPARATOR = ":";
	public static final String UNIX_TIME = "UNIX_TIME";
	public static final String COMBINATION = "COMBINATION";
	public static final String NUMERIC = "NUMERIC";
	public static final String BOOLEAN = "BOOLEAN";
	public static final String COUNT = "COUNT";
	public static final String TEXT = "TEXT";
	public static final String STRING_REPLACER = "-@@@-";
	public static final boolean GUI_DEBUG = false;
	public static final Border DEBUG_BORDER = new LineBorder(Color.RED,1,true);
	public static final int NO_INPUT_INT = Integer.MIN_VALUE;
	/**
	 * TODO implement loading of language parameter from config file
	 * @return en
	 */
	public static String language() {
		return "en";
	}

}
