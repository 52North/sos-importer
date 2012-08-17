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
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.table.TableElement;
import org.x52North.sensorweb.sos.importer.x02.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x02.AdditionalMetadataDocument.AdditionalMetadata.FOIPosition;
import org.x52North.sensorweb.sos.importer.x02.AltDocument.Alt;
import org.x52North.sensorweb.sos.importer.x02.FeatureOfInterestType;
import org.x52North.sensorweb.sos.importer.x02.GeneratedSpatialResourceType;
import org.x52North.sensorweb.sos.importer.x02.LatDocument.Lat;
import org.x52North.sensorweb.sos.importer.x02.LongDocument.Long;
import org.x52North.sensorweb.sos.importer.x02.PositionDocument.Position;
import org.x52North.sensorweb.sos.importer.x02.ResourceType;
import org.x52North.sensorweb.sos.importer.x02.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x02.SpatialResourceType;

/**
 * Store the position for each feature of interest
 * (either stored in a column or manually selected) 
 * in case there are not any positions given in the CSV file. Add each to
 * <code>FOIPositions</code> element.
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
/*
 * How to identify in the current foi is not from file but from manual input or generated
 * 
 * foi.getTableElement() != null                               
 * 		-> FOI is in a colum -> add position to foi positions
 * 
 * foi.getTableElement() == null && foi.isGenerated() == false
 * 		-> FOI is from manual input -> add position to FeatureOfInterest element
 * 
 * foi.getTableElement() == null && foi.isGenerated() == true
 * 		-> FOI is generated -> add position to FeatureOfInterest element
 */
public class Step6cModelHandler implements ModelHandler<Step6cModel> {

	private static final Logger logger = Logger.getLogger(Step6cModelHandler.class);
	
	@Override
	public void handleModel(Step6cModel stepModel,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("handleModel()");
		}
		FeatureOfInterest foi;
		TableElement tabE = null;
		org.n52.sos.importer.model.position.Position pos;
		/*
		 * Identify type of foi: from file, or not
		 */
		pos = stepModel.getPosition();
		foi = stepModel.getFeatureOfInterest();
		tabE = foi.getTableElement();
		if (tabE != null) {
			// Feature of Interest is from file
			// set name for foi, if from file to get the relation between the foi and its position
			if (stepModel.getFeatureOfInterestName() != null && !stepModel.getFeatureOfInterestName().equalsIgnoreCase("")) {
				foi.setName(stepModel.getFeatureOfInterestName());
			}
			addToFoiPositionsElement(foi,pos,sosImportConf);
		} else {
			// Feature of Interest is created from user input 
			addToFeatureOfInterestElement(foi,pos,sosImportConf);
		}
	}

	private void addToFeatureOfInterestElement(
			FeatureOfInterest foi, 
			org.n52.sos.importer.model.position.Position pos, 
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("addToFeatureOfInterestElement()");
		}
		// Get Foi by foi.getXMLId()
		String xmlId = foi.getXMLId();
		FeatureOfInterestType foiXB;
		foiXB = getFoiByXmlId(xmlId,sosImportConf);
		Position posXB;
		// is foi generated or manual input
		if (foi.isGenerated()) {
			GeneratedSpatialResourceType foiGSRT = null;
			if (foiXB.getResource() != null && 
					foiXB.getResource() instanceof GeneratedSpatialResourceType) {
				foiGSRT = (GeneratedSpatialResourceType) foiXB.getResource();
			} else {
				logger.error("FeatureOfInterest element not defined correct: " +
						foiXB.xmlText());
				// TODO how to handle this case?
				return;
			}
			posXB = foiGSRT.getPosition();
			// Check, if position is already there
			if (posXB == null) {
				// if not, add a new position
				foiGSRT.setPosition(getXBPosition(pos));
			} else {
				// if yes, update
				updateXBPosition(posXB, pos);
			}
		} else {
			SpatialResourceType foiSRT = null;
			if (foiXB.getResource() != null && 
					foiXB.getResource() instanceof SpatialResourceType) {
				foiSRT = (SpatialResourceType) foiXB.getResource();
			} else {
				logger.error("FeatureOfInterest element not defined correct: " +
						foiXB.xmlText());
				// TODO how to handle this case?
				return;
			}
			posXB = foiSRT.getPosition();
			// Check, if position is already there
			if (posXB == null) {
				// if not, add a new position
				foiSRT.setPosition(getXBPosition(pos));
			} else {
				// if yes, update
				updateXBPosition(posXB, pos);
			}
		}
	}

	/**
	 * Returns the FeatureOfInterest element identified by the given 
	 * <code>xmlId</code> or <code>null</code> if the element is not found.
	 * @param xmlId
	 * @param sosImportConf
	 * @return the FeatureOfInterest element identified by the given 
	 * 			<code>xmlId</code> or<br>
	 * 			<code>null</code> if the element is not found.
	 */
	private FeatureOfInterestType getFoiByXmlId(String xmlId,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("getFoiByXmlId(" + xmlId + ",...)");
		}
		// get all feature of interests from additional metadata element
		AdditionalMetadata addiMeta = sosImportConf.getAdditionalMetadata();
		if (addiMeta != null) {
			FeatureOfInterestType[] fois = addiMeta.getFeatureOfInterestArray();
			if (fois != null && fois.length > 0) {
				// iterate of fois
				for (FeatureOfInterestType foi : fois) {
					// compare ids
					if (!foi.isNil() && foi.getResource() != null) {
						ResourceType foiRes = foi.getResource();
						if (foiRes.getID()!= null &&
								!foiRes.getID().equalsIgnoreCase("") &&
								foiRes.getID().equalsIgnoreCase(xmlId)) {
							if (logger.isDebugEnabled()) {
								logger.debug("foi found");
							}
							// return if found	
							return foi;
						} else if (logger.isDebugEnabled()) {
							logger.debug("foiRes has wrong id");
						}
					} else if (logger.isDebugEnabled()) {
						logger.debug("foi has no resource defined");
					}
				}
			} else if (logger.isDebugEnabled()) {
				logger.debug("no fois found in AdditionalMetadata element");
			}
		} else if (logger.isDebugEnabled()) {
			logger.debug("no AdditionalMetadata element found");
		}
		return null;
	}

	/**
	 * Adds a new FOIPosition element to AdditionalMetadata in 
	 * <code>sosImportConf</code> or updates an existing one for the given 
	 * <code>foi</code> with values from the <code>pos</code>.
	 * @param foi
	 * @param pos the {@link org.n52.sos.importer.model.position.Position}
	 * @param sosImportConf 
	 */
	private void addToFoiPositionsElement(FeatureOfInterest foi,
			org.n52.sos.importer.model.position.Position pos,
			SosImportConfiguration sosImportConf) {
		if (logger.isTraceEnabled()) {
			logger.trace("addToFoiPositionsElement()");
		}
		FOIPosition[] foiPositions;
		FOIPosition foiPos = null;
		AdditionalMetadata addiMeta;
		boolean addNewFoi = true,
				addNewMeta = false;
		String name;
		//
		// Check, if position for foi is already contained in additional metadata
		name = foi.getName();
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
			foiPositions = addiMeta.getFOIPositionArray();
			if (foiPositions != null && foiPositions.length == 0) {
				// check for foi uri in FOIPosition.relatedFOI
				for (FOIPosition foiPosition : foiPositions) {
					if (foiPosition.getURI().getStringValue().equalsIgnoreCase(name)) {
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
			// new foi -> new position
			foiPos = addiMeta.addNewFOIPosition();
			foiPos.addNewURI().setStringValue(name);
			foiPos.setPosition(getXBPosition(pos));
			if (logger.isDebugEnabled()) {
				logger.debug("New foi pos added: " + foiPos.xmlText());
			}
		} 
		// foi is there, update position
		else {
			updateXBPosition(foiPos.getPosition(), pos);
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
		if (alt == null) {
			alt = posXB.addNewAlt();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new Alt element to Position element");
			}
		}
		alt.setFloatValue(new Double(pos.getHeight().getValue()).floatValue());
		alt.setUnit(pos.getHeight().getUnit());
		/*
		 * 	LATITUDE
		 */
		Lat lat = posXB.getLat();
		if (lat == null) {
			lat = posXB.addNewLat();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new Lat element to Position element");
			}
		}
		lat.setFloatValue(new Double(pos.getLatitude().getValue()).floatValue());
		lat.setUnit(pos.getLatitude().getUnit());
		/*
		 * 	LONGITUDE
		 */
		Long loong = posXB.getLong();
		if (loong == null) {
			loong = posXB.addNewLong();
			if (logger.isDebugEnabled()) {
				logger.debug("Added new Long element to Position element");
			}
		}
		loong.setFloatValue(new Double(pos.getLongitude().getValue()).floatValue());
		loong.setUnit(pos.getLongitude().getUnit());
		if (logger.isDebugEnabled()) {
			logger.debug("XB pos updated from model pos. Model Pos: " + 
					pos.toString() + "; XB pos: " + posXB.xmlText());
		}
	}
}
