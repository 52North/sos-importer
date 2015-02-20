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
package org.n52.sos.importer.model.xml;

import java.util.ArrayList;

import org.apache.xmlbeans.XmlOptions;
import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.ModelStore;
import org.n52.sos.importer.model.Step6bSpecialModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.ObservedProperty;
import org.n52.sos.importer.model.resources.Sensor;
import org.n52.sos.importer.model.table.Column;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x02.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x02.GeneratedResourceType;
import org.x52North.sensorweb.sos.importer.x02.ManualResourceType;
import org.x52North.sensorweb.sos.importer.x02.NumberDocument.Number;
import org.x52North.sensorweb.sos.importer.x02.RelatedSensorDocument.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x02.SensorType;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * In the case of having no sensor. This sensor must be related to:
 * <ol><li>a measured value column</li>
 * <li>a feature of interest</li>
 * <li>an observed property</li></ol>
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step6bSpecialModelHandler implements ModelHandler<Step6bSpecialModel> {

	private static final Logger logger = LoggerFactory.getLogger(Step6bSpecialModelHandler.class);
	
	@Override
	public void handleModel(final Step6bSpecialModel stepModel,
			final SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		/*
		 * add sensor to model
		 */
		final Sensor sensor = stepModel.getSensor();
		final FeatureOfInterest foi = stepModel.getFeatureOfInterest();
		final ObservedProperty obsProp = stepModel.getObservedProperty();
		SensorType sensorXB = null;
		SensorType[] sensorsXB;
		AdditionalMetadata addiMeta = sosImportConf.getAdditionalMetadata();
		final ModelStore ms = ModelStore.getInstance();
		if (addiMeta == null) {
			addiMeta = sosImportConf.addNewAdditionalMetadata();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new AdditionalMetadata element");
			}
		} else {
			 sensorsXB = addiMeta.getSensorArray();
			 
			 findSensor: 
			 for (final SensorType aSensor : sensorsXB) {
				if (aSensor.getResource().getID().equalsIgnoreCase(sensor.getXMLId())) {
					sensorXB = aSensor;
					if (logger.isDebugEnabled()) {
						logger.debug("Found Sensor element");
					}
					break findSensor;
				}
			}
		}
		// if only 1 mv column is present, we have to replace the old sensor with the new one
		if (ms.getMeasuredValues() != null &&
				ms.getMeasuredValues().size() == 1 &&
				addiMeta.getSensorArray() != null) {
			for (int i = 0; i < addiMeta.getSensorArray().length; i++) {
				addiMeta.removeSensor(0);
			}
			sensorXB = null;
		}
		// sensor found or add new one?
		if (sensorXB == null) {
			sensorXB = addiMeta.addNewSensor();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new Sensor element");
			}
			if (sensor.isGenerated()) {
				/*
				 * GENERATED
				 */
				GeneratedResourceType sensorGRT = null;
				if (sensorXB.getResource() instanceof GeneratedResourceType) {
					sensorGRT = (GeneratedResourceType) sensorXB.getResource();
				}
				if (sensorGRT == null) {
					sensorGRT = (GeneratedResourceType) sensorXB.addNewResource().
							substitute(Constants.QNAME_GENERATED_RESOURCE,
									GeneratedResourceType.type);
				}
				sensorGRT.setID(sensor.getXMLId());
				/*
				 * Add generation parameter
				 */
				final String concat = sensor.getConcatString();
				if (concat != null && !concat.equalsIgnoreCase("")) {
					sensorGRT.setConcatString(concat);
				}
				final java.net.URI uri = sensor.getURI();
				final String uriPrefix = sensor.getUriPrefix();
				final boolean useNameAfterPrefixAsUri = sensor.isUseNameAfterPrefixAsURI();
				final org.x52North.sensorweb.sos.importer.x02.URIDocument.URI uriXB = sensorGRT.addNewURI();
				uriXB.setUseAsPrefix(useNameAfterPrefixAsUri);
				if (uri != null) {
					uriXB.setStringValue(uri.toString());
				} else if (uriPrefix != null && 
						!uriPrefix.equalsIgnoreCase("") &&
						useNameAfterPrefixAsUri) {
					uriXB.setStringValue(uriPrefix);
				}
				final Column[] relCols = (Column[]) sensor.getRelatedCols();
				for (final Column c : relCols) {
					final Number num = sensorGRT.addNewNumber();
					num.setIntValue(c.getNumber());
					if (logger.isDebugEnabled()) {
						logger.debug("Added new number element: " + num.xmlText(new XmlOptions().setSaveOuter()));
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Added new generated sensor");
				}
			} else {
				/*
				 * MANUAL
				 */
				ManualResourceType sensorRT = null;
				if (sensorXB.getResource() instanceof ManualResourceType) {
					sensorRT = (ManualResourceType) sensorXB.getResource();
				}
				if (sensorRT == null) {
					sensorRT = (ManualResourceType) sensorXB.addNewResource().
							substitute(Constants.QNAME_MANUAL_RESOURCE,
									ManualResourceType.type);
				}
				sensorRT.setID(sensor.getXMLId());
				sensorRT.setName(sensor.getName());
				sensorRT.addNewURI().setStringValue(sensor.getURI()+"");
				if (logger.isDebugEnabled()) {
					logger.debug("Added new manual sensor");
				}
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Sensor is already contained in AdditionalMetadata element");
			}
		}
		/*
		 * identify related measured value columns and update relation
		 */
		final ArrayList<MeasuredValue> mVs = (ArrayList<MeasuredValue>) ms.getMeasuredValues();
		final ArrayList<MeasuredValue> relatedMVs = new ArrayList<MeasuredValue>(mVs.size());
		for (final MeasuredValue measuredValue : mVs) {
			if (foi.isAssignedTo(measuredValue) && obsProp.isAssigned(measuredValue)) {
				relatedMVs.add(measuredValue);
				if (logger.isDebugEnabled()) {
					logger.debug("Found related MeasureValue" +
							"[" + measuredValue.hashCode() + "]" +
							" for foi" +
							"[" + foi.getXMLId() + "]" +
							" and obsProp" +
							"[" + obsProp.getXMLId() + "]");
				}
			}
		}
		relatedMVs.trimToSize();
		if (relatedMVs.size() < 1) {
			logger.error("No related MeasuredValues found for foi" +
					"[" + foi.getXMLId() + "]" +
					" and obsProp" +
					"[" + obsProp.getXMLId() + "]");
		}
		/*
		 * Add relation to all found measured value column using the xmlId
		 */
		for (final MeasuredValue mV : relatedMVs) {
			final TableElement tabE = mV.getTableElement();
			final int mvColId = Helper.getColumnIdFromTableElement(tabE);
			final org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column 
						mvColumn = Helper.getColumnById(mvColId, sosImportConf);
			final RelatedSensor[] relSensors = mvColumn.getRelatedSensorArray();
			if (!Helper.isSensorInArray(relSensors,sensor.getXMLId())) {
				final RelatedSensor relSensor = mvColumn.addNewRelatedSensor();
				relSensor.setIdRef(sensor.getXMLId());
				if (logger.isDebugEnabled()) {
					logger.debug("Added new related sensor" +
							"[" + sensor.getXMLId() + "]" +
							" to MeasuredValue Column #" + mvColId);
				}
			} else if (logger.isDebugEnabled()) {
				logger.debug("Sensor [" + sensor.getXMLId() + "] is already " +
						"related to MeasuredValue column #" + mvColId);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("handling of Step6bSpecialModel finished.");
		}
	}
}
