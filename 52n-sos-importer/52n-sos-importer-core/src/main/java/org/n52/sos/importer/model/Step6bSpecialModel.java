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

import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.view.i18n.Lang;

public class Step6bSpecialModel implements StepModel {
	
	private final String featureOfInterestName;
	
	private final String observedPropertyName;
	
	private Sensor sensor;

	public Step6bSpecialModel(String featureOfInterestName, String observedPropertyName) {
		this.featureOfInterestName = featureOfInterestName;
		this.observedPropertyName = observedPropertyName;
		sensor = new Sensor();
	}

	public String getFeatureOfInterestName() {
		return featureOfInterestName;
	}

	public String getObservedPropertyName() {
		return observedPropertyName;
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
				+ ((featureOfInterestName == null) ? 0 : featureOfInterestName
						.hashCode());
		result = prime
				* result
				+ ((observedPropertyName == null) ? 0 : observedPropertyName
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
		if (featureOfInterestName == null) {
			if (other.featureOfInterestName != null)
				return false;
		} else if (!featureOfInterestName.equals(other.featureOfInterestName))
			return false;
		if (observedPropertyName == null) {
			if (other.observedPropertyName != null)
				return false;
		} else if (!observedPropertyName.equals(other.observedPropertyName))
			return false;
		return true;
	}

}
