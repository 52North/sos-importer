/**
 * Copyright (C) 2011-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
package org.n52.sos.importer.feeder.util;

import static org.n52.sos.importer.feeder.Configuration.*;

import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.n52.oxf.sos.adapter.wrapper.builder.SensorDescriptionBuilder;
import org.n52.sos.importer.feeder.model.ObservedProperty;
import org.n52.sos.importer.feeder.model.Timestamp;
import org.n52.sos.importer.feeder.model.requests.RegisterSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DescriptionBuilder {
	
	private final boolean shouldAddOfferingMetadataToOutputs;

	public DescriptionBuilder(final boolean shouldAddOfferingMetadataToOutputs) {
		this.shouldAddOfferingMetadataToOutputs = shouldAddOfferingMetadataToOutputs;
	}
	
	public DescriptionBuilder() {
		this(true);
	}
	
	private static final Logger LOG = LoggerFactory.getLogger(DescriptionBuilder.class);

	public String createSML(final RegisterSensor rs) throws XmlException, IOException {
		LOG.trace("createSML()");
		final SensorDescriptionBuilder builder = new SensorDescriptionBuilder();
		
		builder.setAddOfferingMetadataToOutputs(shouldAddOfferingMetadataToOutputs);
		
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
				rs.getLatitudeValue(),
				rs.getEpsgCode().equalsIgnoreCase(Integer.toString(4979))?
						Integer.toString(4326):
							rs.getEpsgCode());
		
		builder.addCapability("offerings",rs.getOfferingName(),"urn:ogc:def:identifier:OGC:1.0:offeringID",rs.getOfferingUri());
		
		// set position data
		builder.setPosition("sensorPosition",
				rs.getEpsgCode(),
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
		
		// add validTime starting from now
		builder.setValidTime(new Timestamp().set(System.currentTimeMillis()).toString(),"unknown");
		
		return builder.buildSensorDescription();
	}
	
}
