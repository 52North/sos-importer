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
package org.n52.sos.importer.model.position;

import org.n52.sos.importer.model.Combination;
import org.n52.sos.importer.model.measuredValue.NumericValue;
import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.TableElement;
import org.n52.sos.importer.view.MissingComponentPanel;
import org.n52.sos.importer.view.position.MissingHeightPanel;

public class Height extends PositionComponent {

	public Height(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Height(double value, String unit) {
		super(value, unit);
	}

	@Override
	public String toString() {
		return "Height" + super.toString();
	}
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingHeightPanel((Position)c);
	}
	
	/**
	 * tries to convert a given String into a valid Height object
	 */
	// TODO units and Strings -> Constants
	public static Height parse(String s) {
		double value = 0;
		String unit = "m";
		
		String number;
		if (s.contains("km")) {
			unit = "km";
			number = s.replace("km", "");
		} else if (s.contains("mi")) {
			unit = "mi";
			number = s.replace("mi", "");
		} else if (s.contains("m")) {
			unit = "m";
			number = s.replace("m", "");
		} else if (s.contains("ft")) {
			unit = "ft";
			number = s.replace("ft", "");
		} else
			number = s;
		
		NumericValue nv = new NumericValue();
		
		value = nv.parse(number);			
		
		return new Height(value, unit);
	}

	@Override
	public Height forThis(Cell featureOfInterestPosition) {
		if (getTableElement() == null)
			return new Height(getValue(), getParsedUnit());
		else {
			String heightString = getTableElement().getValueFor(featureOfInterestPosition);
			return Height.parse(heightString);
		}
	}
}
