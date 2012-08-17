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
package org.n52.sos.importer.model.xml;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
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

	private static final Logger logger = Logger.getLogger(Step6bSpecialModelHandler.class);
	
	@Override
	public void handleModel(Step6bSpecialModel stepModel,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		/*
		 * add sensor to model
		 */
		Sensor sensor = stepModel.getSensor();
		FeatureOfInterest foi = stepModel.getFeatureOfInterest();
		ObservedProperty obsProp = stepModel.getObservedProperty();
		SensorType sensorXB = null;
		SensorType[] sensorsXB;
		AdditionalMetadata addiMeta = sosImportConf.getAdditionalMetadata();
		ModelStore ms = ModelStore.getInstance();
		if (addiMeta == null) {
			addiMeta = sosImportConf.addNewAdditionalMetadata();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new AdditionalMetadata element");
			}
		} else {
			 sensorsXB = addiMeta.getSensorArray();
			 
			 findSensor: 
			 for (SensorType aSensor : sensorsXB) {
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
				String concat = sensor.getConcatString();
				if (concat != null && !concat.equalsIgnoreCase("")) {
					sensorGRT.setConcatString(concat);
				}
				java.net.URI uri = sensor.getURI();
				String uriPrefix = sensor.getUriPrefix();
				boolean useNameAfterPrefixAsUri = sensor.isUseNameAfterPrefixAsURI();
				org.x52North.sensorweb.sos.importer.x02.URIDocument.URI uriXB = sensorGRT.addNewURI();
				uriXB.setUseAsPrefix(useNameAfterPrefixAsUri);
				if (uri != null) {
					uriXB.setStringValue(uri.toString());
				} else if (uriPrefix != null && 
						!uriPrefix.equalsIgnoreCase("") &&
						useNameAfterPrefixAsUri) {
					uriXB.setStringValue(uriPrefix);
				}
				Column[] relCols = (Column[]) sensor.getRelatedCols();
				for (int i = 0; i < relCols.length; i++) {
					Number num = sensorGRT.addNewNumber();
					Column c = relCols[i];
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
		ArrayList<MeasuredValue> mVs = (ArrayList<MeasuredValue>) ms.getMeasuredValues();
		ArrayList<MeasuredValue> relatedMVs = new ArrayList<MeasuredValue>(mVs.size());
		for (Iterator<MeasuredValue> iterator = mVs.iterator(); iterator.hasNext();) {
			MeasuredValue measuredValue = iterator.next();
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
		for (MeasuredValue mV : relatedMVs) {
			TableElement tabE = mV.getTableElement();
			int mvColId = Helper.getColumnIdFromTableElement(tabE);
			org.x52North.sensorweb.sos.importer.x02.ColumnDocument.Column 
						mvColumn = Helper.getColumnById(mvColId, sosImportConf);
			RelatedSensor[] relSensors = mvColumn.getRelatedSensorArray();
			if (!Helper.isSensorInArray(relSensors,sensor.getXMLId())) {
				RelatedSensor relSensor = mvColumn.addNewRelatedSensor();
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
