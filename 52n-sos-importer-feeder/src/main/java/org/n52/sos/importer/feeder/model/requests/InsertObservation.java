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

import org.n52.sos.importer.feeder.model.FeatureOfInterest;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Sensor;
import org.n52.sos.importer.feeder.model.UnitOfMeasurement;

/**
 * Holds all information for the InsertObservation request
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class InsertObservation {
	
	private Object value;
	private String timeStamp;
	private Sensor s;
	private ObservedProperty o;
	private FeatureOfInterest f;
	private UnitOfMeasurement u;
	private Offering off;
	private String mvType;
	
	public InsertObservation(Sensor sensor,
			FeatureOfInterest foi,
			Object value,
			String timeStamp,
			UnitOfMeasurement uom,
			ObservedProperty obsProp,
			Offering off,
			String mvType) {
		this.f = foi; 
		this.s = sensor;
		this.o = obsProp;
		this.timeStamp = timeStamp;
		this.u = uom;
		this.value = value;
		this.off = off;
		this.mvType = mvType;
	}
	
	public String getSensorName() {
		return s.getName();
	}

	public String getSensorURI() {
		return s.getUri();
	}

	public String getFeatureOfInterestName() {
		return f.getName();
	}

	public String getFeatureOfInterestURI() {
		return f.getUri();
	}

	public String getObservedPropertyURI() {
		return o.getUri();
	}

	public String getUnitOfMeasurementCode() {
		return u.getCode();
	}

	public Object getValue() {
		return value;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public String getEpsgCode() {
		return f.getPosition().getEpsgCode()+"";
	}

	public double getLatitudeValue() {
		return f.getPosition().getLatitude();
	}

	public double getLongitudeValue() {
		return f.getPosition().getLongitude();
	}

	protected ObservedProperty getObservedProperty() {
		return o;
	}

	protected FeatureOfInterest getFeatureOfInterest() {
		return f;
	}

	protected UnitOfMeasurement getUnitOfMeasurment() {
		return u;
	}
	
	protected Offering getOffering() {
		return off;
	}
	
	public String getMvType() {
		return mvType;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InsertObservation [value=");
		builder.append(value);
		builder.append(", timeStamp=");
		builder.append(timeStamp);
		builder.append(", s=");
		builder.append(s);
		builder.append(", o=");
		builder.append(o);
		builder.append(", f=");
		builder.append(f);
		builder.append(", u=");
		builder.append(u);
		builder.append(", off=");
		builder.append(off);
		builder.append(", mvType=");
		builder.append(mvType);
		builder.append("]");
		return builder.toString();
	}
	
}
