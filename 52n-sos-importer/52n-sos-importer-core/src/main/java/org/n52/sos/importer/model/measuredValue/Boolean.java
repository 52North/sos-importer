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
package org.n52.sos.importer.model.measuredValue;


public class Boolean extends MeasuredValue {

	@Override
	public String toString() {
		return "Boolean" + super.toString();
	}
	
	@Override
	public Object parse(String s) {
		s = s.trim();
		if (s.equals("0") || s.equalsIgnoreCase("false")) {
			return false;
		} else
		if (s.equals("1") || s.equalsIgnoreCase("true")) {
			return true;
		}
		else throw new NumberFormatException(
				"value of String should be: 0,1,true,false. " +
				"Regarding the last two case is ignored. " +
				"Given value is: " + s);
	}

	@Override
	public void setPattern(String parsePattern) {
		// do nothing, it's Java base type wrapper
	}
}
