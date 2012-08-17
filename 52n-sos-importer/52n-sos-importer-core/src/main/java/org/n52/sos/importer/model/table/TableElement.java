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
package org.n52.sos.importer.model.table;

import java.util.HashSet;

/**
 * can either be a column, row or cell in the table
 * @author Raimund
 *
 */
public abstract class TableElement {

	/**
	 * Colours this table element in the table
	 */
	public abstract void mark();
	
	/**
	 * returns the corresponding value of another metadata type
	 * for a measured value or feature of interest cell
	 */
	public abstract String getValueFor(Cell c);
	
	/**
	 * returns the corresponding cell of another metadata type
	 * for a measured value or feature of interest cell
	 */
	public abstract Cell getCellFor(Cell c);
	
	/**
	 * retrieves all values in this column, row or cell
	 */
	public abstract HashSet<String> getValues();
}
