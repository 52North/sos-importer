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

import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.view.i18n.Lang;

public class Step6bSpecialModel implements StepModel {
	
	private final FeatureOfInterest foi;
	
	private final ObservedProperty obsProp;
	
	private Sensor sensor;

	public Step6bSpecialModel(FeatureOfInterest foi, ObservedProperty obsProp) {
		this.foi = foi;
		this.obsProp = obsProp;
		sensor = new Sensor();
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Sensor getSensor() {
		return sensor;
	}
	
	public String getDescription() {
		return Lang.l().step6bSpecialModelDescription();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((foi == null) ? 0 : foi
						.hashCode());
		result = prime
				* result
				+ ((obsProp == null) ? 0 : obsProp
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Step6bSpecialModel))
			return false;
		Step6bSpecialModel other = (Step6bSpecialModel) obj;
		if (foi == null) {
			if (other.foi != null)
				return false;
		} else if (!foi.equals(other.foi))
			return false;
		if (obsProp == null) {
			if (other.obsProp != null)
				return false;
		} else if (!obsProp.equals(other.obsProp))
			return false;
		return true;
	}

	/**
	 * @return the foi
	 */
	public FeatureOfInterest getFeatureOfInterest() {
		return foi;
	}

	/**
	 * @return the obsProp
	 */
	public ObservedProperty getObservedProperty() {
		return obsProp;
	}

}
