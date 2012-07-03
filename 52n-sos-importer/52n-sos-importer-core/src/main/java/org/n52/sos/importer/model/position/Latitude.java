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
import org.n52.sos.importer.view.position.MissingLatitudePanel;

public class Latitude extends PositionComponent {

	public Latitude(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Latitude(double value, String unit) {
		super(value, unit);
	}
	
	@Override
	public String toString() {
		return "Latitude" + super.toString();
	}
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingLatitudePanel((Position)c);
	}
	
	/**
	 * Tries to convert a given String into a valid Latitude object
	 */
	// TODO units and Strings -> Constants
	public static Latitude parse(String s) {
		double value = 0.0;
		String unit = "";
		
		String number;
		//TODO handle inputs like degrees/minutes/seconds, n.Br.
		if (s.contains("°")) {
			unit = "°";
			String[] part = s.split("°");
			number = part[0];
		} else if (s.contains("m")) {
			unit = "m";
			number = s.replace("m", "");
		} else
			number = s;
		
		NumericValue nv = new NumericValue();
		
		value = nv.parse(number);			

		if (unit.equals(""))
			if (value <= 90.0 && value >= -90.0)
				unit = "°";
			else 
				unit = "m";
		
		return new Latitude(value, unit);
	}
	
	@Override
	public Latitude forThis(Cell featureOfInterestPosition) {
		if (getTableElement() == null)
			return new Latitude(getValue(), getParsedUnit());
		else {
			String latitudeString = getTableElement().getValueFor(featureOfInterestPosition);
			return Latitude.parse(latitudeString);
		}
	}
}
