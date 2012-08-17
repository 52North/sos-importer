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
package org.n52.sos.importer.model;

/**
 * interface for objects which can convert Strings with the help 
 * of a certain pattern into this object
 * (e.g. "2011-08-04" + pattern "yyyy-MM-dd" --> DateAndTime object)
 * @author Raimund, e.h.juerrens@52north.org
 *
 */
public interface Parseable {

	/**
	 * converts a String with the help of a certain pattern 
	 * into a user-defined object
	 * @param s
	 * @return
	 */
	public Object parse(String s);
	
	/**
	 * set the pattern to be used by parse(String s);
	 * @param parsePattern
	 */
	public void setPattern(String parsePattern); 
}
