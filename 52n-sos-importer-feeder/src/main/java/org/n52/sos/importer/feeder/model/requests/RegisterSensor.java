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
package org.n52.sos.importer.feeder.model.requests;


/**
 * Holds all information for the RegisterSensor request
 * @author Raimund
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class RegisterSensor {

	private InsertObservation io;
	
	public RegisterSensor(InsertObservation io) {
		this.io = io;
	}

	public String getSensorName() {
		return io.getSensorName();
	}

	public String getSensorURI() {
		return io.getSensorURI();
	}

	public String getFeatureOfInterestName() {
		return io.getFeatureOfInterestName();
	}

	public String getObservedPropertyName() {
		return io.getObservedProperty().getName();
	}

	public String getObservedPropertyURI() {
		return io.getObservedPropertyURI();
	}

	public String getOfferingName() {
		return io.getOffering().getName();
	}

	public String getUnitOfMeasurementCode() {
		return io.getUnitOfMeasurementCode();
	}

	public String getEpsgCode() {
		return io.getEpsgCode();
	}

	public double getLatitudeValue() {
		return io.getLatitudeValue();
	}

	public String getLatitudeUnit() {
		return io.getFeatureOfInterest().getPosition().getLatitudeUnit();
	}

	public double getLongitudeValue() {
		return io.getLongitudeValue();
	}

	public String getLongitudeUnit() {
		return io.getFeatureOfInterest().getPosition().getLongitudeUnit();
	}

	public double getAltitudeValue() {
		return io.getFeatureOfInterest().getPosition().getAltitude();
	}

	public String getAltitudeUnit() {
		return io.getFeatureOfInterest().getPosition().getAltitudeUnit();
	}

	public String getFeatureOfInterestURI() {
		return io.getFeatureOfInterestURI();
	}

	public String getDefaultValue() {
		if (io.getValue() instanceof Boolean) {
			return "false";
		}
		if (io.getValue() instanceof Integer) {
			return "0";
		}
		if (io.getValue() instanceof Double) {
			return "0.0";
		}
		if (io.getValue() instanceof String) {
			return " ";
		}
		return "notDefined";
	}

	public String getOfferingUri() {
		return io.getOffering().getUri();
	}

	@Override
	public String toString() {
		return String
				.format("RegisterSensor [getSensorName()=%s, getSensorURI()=%s, getFeatureOfInterestName()=%s, getObservedPropertyName()=%s, getObservedPropertyURI()=%s, getOfferingName()=%s, getUnitOfMeasurementCode()=%s, getEpsgCode()=%s, getLatitudeValue()=%s, getLatitudeUnit()=%s, getLongitudeValue()=%s, getLongitudeUnit()=%s, getAltitudeValue()=%s, getAltitudeUnit()=%s, getFeatureOfInterestURI()=%s, getDefaultValue()=%s, getOfferingUri()=%s]",
						getSensorName(), getSensorURI(),
						getFeatureOfInterestName(), getObservedPropertyName(),
						getObservedPropertyURI(), getOfferingName(),
						getUnitOfMeasurementCode(), getEpsgCode(),
						getLatitudeValue(), getLatitudeUnit(),
						getLongitudeValue(), getLongitudeUnit(),
						getAltitudeValue(), getAltitudeUnit(),
						getFeatureOfInterestURI(), getDefaultValue(),
						getOfferingUri());
	}

}
