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
import org.n52.sos.importer.model.Step6cModel;
import org.x52North.sensorweb.sos.importer.x02.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x02.AdditionalMetadataDocument.AdditionalMetadata.FOIPositions;
import org.x52North.sensorweb.sos.importer.x02.AltDocument.Alt;
import org.x52North.sensorweb.sos.importer.x02.LatDocument.Lat;
import org.x52North.sensorweb.sos.importer.x02.LongDocument.Long;
import org.x52North.sensorweb.sos.importer.x02.PositionDocument.Position;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Store the position for each feature of interest
 * (either stored in a column or manually selected) 
 * in case there are not any positions given in the CSV file. Add each to
 * <code>FOIPositions</code> element.
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 */
public class Step6cModelHandler implements ModelHandler<Step6cModel> {

	private static final Logger logger = Logger.getLogger(Step6cModelHandler.class);
	
	@Override
	public void handleModel(Step6cModel stepModel,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		/*
		 * 	LOCALE FIELDS
		 */
		String name;
		org.n52.sos.importer.model.resources.FeatureOfInterest foi;
		FOIPositions[] foiPositions;
		FOIPositions foiPos = null;
		AdditionalMetadata addiMeta;
		boolean addNewFoi = true, addNewMeta = false;
		/*
		 * check if FOI is already there
		 */
		foi = stepModel.getFeatureOfInterest();
		name = stepModel.getFeatureOfInterestName();
		if (name == null) {
			name = foi.getURIString();
		}
		// Check additional metadata element
		addiMeta = sosImportConf.getAdditionalMetadata();
		if (addiMeta == null) {
			addiMeta = sosImportConf.addNewAdditionalMetadata();
			addNewMeta = true;
			if (logger.isDebugEnabled()) {
				logger.debug("New AdditionalMetadata element created");
			}
		}
		if (!addNewMeta) {
			foiPositions = addiMeta.getFOIPositionsArray();
			if (foiPositions != null && foiPositions.length == 0) {
				// check for foi uri in FOIPosition.relatedFOI
				for (FOIPositions foiPosition : foiPositions) {
					if (foiPosition.getURI().equalsIgnoreCase(name)) {
						addNewFoi = false;
						foiPos = foiPosition;
						if (logger.isDebugEnabled()) {
							logger.debug("foi pos found: " + foiPos.xmlText());
						}
					}
				}
			}
		}
		// END: check for foi
		if (addNewFoi) {
			foiPos = addiMeta.addNewFOIPositions();
			foiPos.setURI(name);
			foiPos.setPosition(getXBPosition(stepModel.getPosition()));
			if (logger.isDebugEnabled()) {
				logger.debug("New foi pos added: " + foiPos.xmlText());
			}
		} 
		// foi is there, update position
		else {
			updateXBPosition(foiPos.getPosition(), stepModel.getPosition());
			if (logger.isDebugEnabled()) {
				logger.debug("foi pos updated: " + foiPos.xmlText());
			}
		}
	}
	
	private Position getXBPosition(
			org.n52.sos.importer.model.position.Position position) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\tgetXBPosition()");
		}
		Position pos = Position.Factory.newInstance();
		/*
		 * 	ALTITUDE
		 */
		Alt altitude = pos.addNewAlt();
		altitude.setUnit(position.getHeight().getUnit());
		altitude.setFloatValue(new Double(position.getHeight().getValue()).floatValue());
		/*
		 * 	EPSG_CODE
		 */
		pos.setEPSGCode(position.getEPSGCode().getValue());
		/*
		 * 	LATITUDE
		 */
		Lat latitude = pos.addNewLat();
		latitude.setUnit(position.getLatitude().getUnit());
		latitude.setFloatValue(new Double(position.getLatitude().getValue()).floatValue());
		/*
		 *	LONGITUDE
		 */
		Long longitude = pos.addNewLong();
		longitude.setUnit(position.getLongitude().getUnit());
		longitude.setFloatValue(new Double(position.getLongitude().getValue()).floatValue());
		if (logger.isDebugEnabled()) {
			logger.debug("XB pos created from model pos. Model Pos: " + 
					position.toString() + "; XB pos: " + pos.xmlText());
		}
		return pos;
	}

	

	private void updateXBPosition(Position posXB,
			org.n52.sos.importer.model.position.Position pos) {
		if (logger.isTraceEnabled()) {
			logger.trace("\t\t\tupdateXBPosition()");
		}
		if(posXB == null || pos == null) {
			logger.fatal("at least one parameter is null but MUST NOT: pos? " + 
					pos + "; posXB? " + posXB);
			return;
		}
		// Update each element in this position element
		/*
		 * EPSG code
		 */
		posXB.setEPSGCode(pos.getEPSGCode().getValue());
		/*
		 * 	ALTITUDE
		 */
		Alt alt = posXB.getAlt();
		alt.setFloatValue(new Double(pos.getHeight().getValue()).floatValue());
		alt.setUnit(pos.getHeight().getUnit());
		/*
		 * 	LATITUDE
		 */
		Lat lat = posXB.getLat();
		lat.setFloatValue(new Double(pos.getLatitude().getValue()).floatValue());
		lat.setUnit(pos.getLatitude().getUnit());
		/*
		 * 	LONGITUDE
		 */
		Long longi = posXB.getLong();
		longi.setFloatValue(new Double(pos.getLongitude().getValue()).floatValue());
		longi.setUnit(pos.getLongitude().getUnit());
		if (logger.isDebugEnabled()) {
			logger.debug("XB pos updated from model pos. Model Pos: " + 
					pos.toString() + "; XB pos: " + posXB.xmlText());
		}
	}


}
