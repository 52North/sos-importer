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
import org.n52.sos.importer.view.position.MissingLongitudePanel;

public class Longitude extends PositionComponent {

	public Longitude(TableElement tableElement, String pattern) {
		super(tableElement, pattern);
	}
	
	public Longitude(double value, String unit) {
		super(value, unit);
	}
	
	@Override
	public String toString() {
		return "Longitude" + super.toString();
	}
	
	@Override
	public MissingComponentPanel getMissingComponentPanel(Combination c) {
		return new MissingLongitudePanel((Position)c);
	}
	
	/**
	 * tries to convert a given String into a valid Longitude object
	 */
	// TODO units and Strings -> Constants
	public static Longitude parse(String s) {
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
			if (value <= 180.0 && value >= -180.0)
				unit = "°";
			else 
				unit = "m";
		
		return new Longitude(value, unit);
	}
	
	@Override
	public Longitude forThis(Cell featureOfInterestPosition) {
		if (getTableElement() == null)
			return new Longitude(getValue(), getParsedUnit());
		else {
			String longitudeString = getTableElement().getValueFor(featureOfInterestPosition);
			return Longitude.parse(longitudeString);
		}
	}
}
