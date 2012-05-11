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

import org.apache.log4j.Logger;
import org.n52.sos.importer.model.Step6bSpecialModel;
import org.x52North.sensorweb.sos.importer.x02.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x02.SensorDocument.Sensor;
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
		String foiName = stepModel.getFeatureOfInterestName(), 
				foiURI = foiName,
				obsPropName = stepModel.getObservedPropertyName(),
				obsPropURI = obsPropName,
				sensorName, 
				sensorURI;
		org.n52.sos.importer.model.resources.Sensor sensor = stepModel.getSensor();
		/*
		 * TODO get FOI and obsProp URI. Requires update of GUI.
		 */
		/*
		 * add sensor to model
		 */
		sensorName = sensor.getName();
		sensorURI = sensor.getURIString();
		Sensor sensorXB = null;
		Sensor[] sensorsXB;
		AdditionalMetadata addiMeta = sosImportConf.getAdditionalMetadata();
		if (addiMeta == null) {
			addiMeta = sosImportConf.addNewAdditionalMetadata();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new AdditionalMetadata element");
			}
		} else {
			 sensorsXB = addiMeta.getSensorArray();
			 
			 findSensor: 
			 for (Sensor aSensor : sensorsXB) {
				if (aSensor.getURI().equalsIgnoreCase(sensorURI)) {
					sensorXB = aSensor;
					if (logger.isDebugEnabled()) {
						logger.debug("Found Sensor element");
					}
					break findSensor;
				}
			}
		}
		// sensor found or add new one?
		if (sensorXB == null) {
			sensorXB = addiMeta.addNewSensor();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new Sensor element");
			}
		}
		sensorXB.setName(sensorName);
		sensorXB.setURI(sensorURI);
		/*
		 * add relations
		 */
		sensorXB.setRelatedFOI(foiURI);
		sensorXB.setRelatedObservedProperty(obsPropURI);
		// TODO identify related measured value column and update relation
	}
}
