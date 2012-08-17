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
package org.n52.sos.importer.feeder.model;

import java.util.Arrays;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public final class Position {
	
	public static final int LONG = 0;

	public static final int LAT = 1;

	public static final int ALT = 2;

	public static final String DEFAULT_UNIT_LAT = null;

	private final double[] values;
	
	private final String[] units;
	
	private final int epsgCode;

	/**
	 * @param values order: long, lat, alt
	 * @param units order: long, lat, alt
	 * @param epsgCode 
	 */
	public Position(double[] values, String[] units, int epsgCode) {
		this.values = values;
		this.units = units;
		this.epsgCode = epsgCode;
	}

	public int getEpsgCode() {
		return epsgCode;
	}
	
	public double getAltitude() {
		return values[Position.ALT];
	}
	
	public String getAltitudeUnit() {
		return units[Position.ALT];
	}
	
	public double getLongitude() {
		return values[Position.LONG];
	}
	
	public String getLongitudeUnit() {
		return units[Position.LONG];
	}
	
	public double getLatitude() {
		return values[Position.LAT];
	}
	
	public String getLatitudeUnit() {
		return units[Position.LAT];
	}

	@Override
	public String toString() {
		return String.format("Position [values=%s, units=%s, epsgCode=%s]",
				Arrays.toString(values), Arrays.toString(units), epsgCode);
	}


}
