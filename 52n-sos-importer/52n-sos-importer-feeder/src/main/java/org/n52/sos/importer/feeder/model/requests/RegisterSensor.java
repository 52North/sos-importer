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
	
	public String getMvType() {
		return io.getMvType();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegisterSensor [io=");
		builder.append(io);
		builder.append(", getSensorName()=");
		builder.append(getSensorName());
		builder.append(", getSensorURI()=");
		builder.append(getSensorURI());
		builder.append(", getFeatureOfInterestName()=");
		builder.append(getFeatureOfInterestName());
		builder.append(", getObservedPropertyName()=");
		builder.append(getObservedPropertyName());
		builder.append(", getObservedPropertyURI()=");
		builder.append(getObservedPropertyURI());
		builder.append(", getOfferingName()=");
		builder.append(getOfferingName());
		builder.append(", getUnitOfMeasurementCode()=");
		builder.append(getUnitOfMeasurementCode());
		builder.append(", getEpsgCode()=");
		builder.append(getEpsgCode());
		builder.append(", getLatitudeValue()=");
		builder.append(getLatitudeValue());
		builder.append(", getLatitudeUnit()=");
		builder.append(getLatitudeUnit());
		builder.append(", getLongitudeValue()=");
		builder.append(getLongitudeValue());
		builder.append(", getLongitudeUnit()=");
		builder.append(getLongitudeUnit());
		builder.append(", getAltitudeValue()=");
		builder.append(getAltitudeValue());
		builder.append(", getAltitudeUnit()=");
		builder.append(getAltitudeUnit());
		builder.append(", getFeatureOfInterestURI()=");
		builder.append(getFeatureOfInterestURI());
		builder.append(", getDefaultValue()=");
		builder.append(getDefaultValue());
		builder.append(", getOfferingUri()=");
		builder.append(getOfferingUri());
		builder.append(", getMvType()=");
		builder.append(getMvType());
		builder.append("]");
		return builder.toString();
	}

}
