/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
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
package org.n52.sos.importer.feeder.util;

import static org.n52.sos.importer.feeder.Configuration.*;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.requests.RegisterSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensorDescriptionBuilder {
	
	private static final Logger LOG = LoggerFactory.getLogger(SensorDescriptionBuilder.class);

	public String createSML(final RegisterSensor rs) throws XmlException, IOException {
		LOG.trace("createSML()");
		final org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder builder = new org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder();
		
		final StringBuilder intendedApplication = new StringBuilder();
		
		// add keywords
		builder.addKeyword(rs.getFeatureOfInterestName());
		builder.addKeyword(rs.getSensorName());
		
		// add all identifier
		builder.setIdentifierUniqeId(rs.getSensorURI());
		builder.setIdentifierLongName(rs.getSensorName());
		builder.setIdentifierShortName(rs.getSensorName());
		
		// set capabilities - status
		builder.setCapabilityCollectingStatus("status", true);
		builder.addFeatureOfInterest(rs.getFeatureOfInterestName(), rs.getFeatureOfInterestURI());
		builder.setCapabilityBbox(rs.getLongitudeUnit(),
				rs.getLongitudeValue(), rs.getLatitudeUnit(),
				rs.getLatitudeValue(), rs.getLongitudeUnit(),
				rs.getLongitudeValue(), rs.getLatitudeUnit(),
				rs.getLatitudeValue());
		
		builder.addCapability("offerings",rs.getOfferingName(),"urn:ogc:def:identifier:OGC:offeringID",rs.getOfferingUri());
		// set position data
		builder.setPosition("sensorPosition",
				org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder.EPSG_CODE_PREFIX + rs.getEpsgCode(),
				"SYSTEM_LOCATION",
				rs.getLongitudeUnit(), rs.getLongitudeValue(),
				rs.getLatitudeUnit(), rs.getLatitudeValue(),
				rs.getAltitudeUnit(), rs.getAltitudeValue());
		
		for (final ObservedProperty observedProperty : rs.getObservedProperties()) {
			// add inputs
			builder.addInput(observedProperty.getName(), observedProperty.getUri());
			// add outputs
			if (rs.getMeasuredValueType(observedProperty).equals(SOS_OBSERVATION_TYPE_TEXT)) {
				builder.addOutputText(observedProperty.getName(),
						observedProperty.getUri(), 
						rs.getOfferingUri(),
						rs.getOfferingName());
			} 
			else if (rs.getMeasuredValueType(observedProperty).equals(SOS_OBSERVATION_TYPE_BOOLEAN)) {
				builder.addOutputBoolean(observedProperty.getName(),
						observedProperty.getUri(),
						rs.getOfferingUri(),
						rs.getOfferingName());
			} 
			else if (rs.getMeasuredValueType(observedProperty).equals(SOS_OBSERVATION_TYPE_COUNT)) {
				builder.addOutputCount(observedProperty.getName(),
						observedProperty.getUri(),
						rs.getOfferingUri(),
						rs.getOfferingName());
			}
			else {
				builder.addOutputMeasurement(observedProperty.getName(),
						observedProperty.getUri(),
						rs.getOfferingUri(),
						rs.getOfferingName(),
						rs.getUnitOfMeasurementCode(observedProperty));
			}
			// add keyword
			builder.addKeyword(observedProperty.getName());
			intendedApplication.append(observedProperty.getName());
			intendedApplication.append(", ");
		}
		
		// add all classifier
		builder.setClassifierIntendedApplication(intendedApplication.substring(0, intendedApplication.length()-2));
		
		return builder.buildSensorDescription();
	}
	
}
