/**
 * Copyright (C) 2011-2016 52°North Initiative for Geospatial Open Source
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

import org.n52.sos.importer.model.Step6cModel;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x04.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x04.AdditionalMetadataDocument.AdditionalMetadata.FOIPosition;
import org.x52North.sensorweb.sos.importer.x04.AltDocument.Alt;
import org.x52North.sensorweb.sos.importer.x04.FeatureOfInterestType;
import org.x52North.sensorweb.sos.importer.x04.GeneratedSpatialResourceType;
import org.x52North.sensorweb.sos.importer.x04.LatDocument.Lat;
import org.x52North.sensorweb.sos.importer.x04.LongDocument.Long;
import org.x52North.sensorweb.sos.importer.x04.PositionDocument.Position;
import org.x52North.sensorweb.sos.importer.x04.ResourceType;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x04.SpatialResourceType;

/**
 * Store the position for each feature of interest
 * (either stored in a column or manually selected)
 * in case there are not any positions given in the CSV file. Add each to
 * <code>FOIPositions</code> element.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
/*
 * How to identify in the current foi is not from file but from manual input or generated
 *
 * foi.getTableElement() != null
 *      -> FOI is in a colum -> add position to foi positions
 *
 * foi.getTableElement() == null && foi.isGenerated() == false
 *      -> FOI is from manual input -> add position to FeatureOfInterest element
 *
 * foi.getTableElement() == null && foi.isGenerated() == true
 *      -> FOI is generated -> add position to FeatureOfInterest element
 */
public class Step6cModelHandler implements ModelHandler<Step6cModel> {

    private static final String XB_POS = "; XB pos: ";
    private static final String FEATURE_OF_INTEREST_ELEMENT_NOT_DEFINED_CORRECT =
            "FeatureOfInterest element not defined correct: ";
    private static final Logger logger = LoggerFactory.getLogger(Step6cModelHandler.class);

    /** {@inheritDoc} */
    @Override
    public void handleModel(final Step6cModel stepModel,
            final SosImportConfiguration sosImportConf) {
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
            if (stepModel.getFeatureOfInterestName() != null &&
                    !stepModel.getFeatureOfInterestName().equalsIgnoreCase("")) {
                foi.setName(stepModel.getFeatureOfInterestName());
            }
            addToFoiPositionsElement(foi, pos, sosImportConf);
        } else {
            // Feature of Interest is created from user input
            addToFeatureOfInterestElement(foi, pos, sosImportConf);
        }
    }

    private void addToFeatureOfInterestElement(
            final FeatureOfInterest foi,
            final org.n52.sos.importer.model.position.Position pos,
            final SosImportConfiguration sosImportConf) {
        if (logger.isTraceEnabled()) {
            logger.trace("addToFeatureOfInterestElement()");
        }
        // Get Foi by foi.getXMLId()
        final String xmlId = foi.getXMLId();
        FeatureOfInterestType foiXB;
        foiXB = getFoiByXmlId(xmlId, sosImportConf);
        Position posXB;
        // is foi generated or manual input
        if (foi.isGenerated()) {
            GeneratedSpatialResourceType foiGSRT = null;
            if (foiXB.getResource() != null &&
                    foiXB.getResource() instanceof GeneratedSpatialResourceType) {
                foiGSRT = (GeneratedSpatialResourceType) foiXB.getResource();
            } else {
                logger.error(FEATURE_OF_INTEREST_ELEMENT_NOT_DEFINED_CORRECT +
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
                logger.error(FEATURE_OF_INTEREST_ELEMENT_NOT_DEFINED_CORRECT +
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
     * @return the FeatureOfInterest element identified by the given
     *          <code>xmlId</code> or<br>
     *          <code>null</code> if the element is not found.
     */
    private FeatureOfInterestType getFoiByXmlId(final String xmlId,
            final SosImportConfiguration sosImportConf) {
        if (logger.isTraceEnabled()) {
            logger.trace("getFoiByXmlId(" + xmlId + ",...)");
        }
        // get all feature of interests from additional metadata element
        final AdditionalMetadata addiMeta = sosImportConf.getAdditionalMetadata();
        if (addiMeta != null) {
            final FeatureOfInterestType[] fois = addiMeta.getFeatureOfInterestArray();
            if (fois != null && fois.length > 0) {
                // iterate of fois
                for (final FeatureOfInterestType foi : fois) {
                    // compare ids
                    if (!foi.isNil() && foi.getResource() != null) {
                        final ResourceType foiRes = foi.getResource();
                        if (foiRes.getID() != null &&
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
     * @param pos the {@link org.n52.sos.importer.model.position.Position}
     */
    private void addToFoiPositionsElement(final FeatureOfInterest foi,
            final org.n52.sos.importer.model.position.Position pos,
            final SosImportConfiguration sosImportConf) {
        if (logger.isTraceEnabled()) {
            logger.trace("addToFoiPositionsElement()");
        }
        FOIPosition[] foiPositions;
        FOIPosition foiPos = null;
        AdditionalMetadata addiMeta;
        boolean addNewFoi = true;
        boolean addNewMeta = false;
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
                for (final FOIPosition foiPosition : foiPositions) {
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
            // foi is there, update position
        } else {
            updateXBPosition(foiPos.getPosition(), pos);
            if (logger.isDebugEnabled()) {
                logger.debug("foi pos updated: " + foiPos.xmlText());
            }
        }
    }

    private Position getXBPosition(
            final org.n52.sos.importer.model.position.Position position) {
        if (logger.isTraceEnabled()) {
            logger.trace("\t\t\tgetXBPosition()");
        }
        final Position pos = Position.Factory.newInstance();
        /*
         *  ALTITUDE
         */
        final Alt altitude = pos.addNewAlt();
        altitude.setUnit(position.getHeight().getUnit());
        altitude.setFloatValue(new Double(position.getHeight().getValue()).floatValue());
        /*
         *  EPSG_CODE
         */
        pos.setEPSGCode(position.getEPSGCode().getValue());
        /*
         *  LATITUDE
         */
        final Lat latitude = pos.addNewLat();
        latitude.setUnit(position.getLatitude().getUnit());
        latitude.setFloatValue(new Double(position.getLatitude().getValue()).floatValue());
        /*
         *  LONGITUDE
         */
        final Long longitude = pos.addNewLong();
        longitude.setUnit(position.getLongitude().getUnit());
        longitude.setFloatValue(new Double(position.getLongitude().getValue()).floatValue());
        if (logger.isDebugEnabled()) {
            logger.debug("XB pos created from model pos. Model Pos: " +
                    position.toString() + XB_POS + pos.xmlText());
        }
        return pos;
    }

    private void updateXBPosition(final Position posXB,
            final org.n52.sos.importer.model.position.Position pos) {
        if (logger.isTraceEnabled()) {
            logger.trace("\t\t\tupdateXBPosition()");
        }
        if (posXB == null || pos == null) {
            logger.error("at least one parameter is null but MUST NOT: pos? {}; posXB? {}", pos, posXB);
            return;
        }
        // Update each element in this position element
        /*
         * EPSG code
         */
        posXB.setEPSGCode(pos.getEPSGCode().getValue());
        /*
         *  ALTITUDE
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
         *  LATITUDE
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
         *  LONGITUDE
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
                    pos.toString() + XB_POS + posXB.xmlText());
        }
    }
}
