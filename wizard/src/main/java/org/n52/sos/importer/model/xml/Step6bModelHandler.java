/*
 * Copyright (C) 2011-2018 52°North Initiative for Geospatial Open Source
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

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step6bModel;
import org.n52.sos.importer.model.measuredValue.MeasuredValue;
import org.n52.sos.importer.model.position.Position.Id;
import org.n52.sos.importer.model.resources.FeatureOfInterest;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.wizard.utils.EPSGHelper;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.AdditionalMetadataDocument.AdditionalMetadata;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.CoordinateDocument.Coordinate;
import org.x52North.sensorweb.sos.importer.x05.FeatureOfInterestType;
import org.x52North.sensorweb.sos.importer.x05.GeneratedResourceType;
import org.x52North.sensorweb.sos.importer.x05.GeneratedSpatialResourceType;
import org.x52North.sensorweb.sos.importer.x05.ManualResourceType;
import org.x52North.sensorweb.sos.importer.x05.ObservedPropertyType;
import org.x52North.sensorweb.sos.importer.x05.PositionDocument.Position;
import org.x52North.sensorweb.sos.importer.x05.RelatedFOIDocument.RelatedFOI;
import org.x52North.sensorweb.sos.importer.x05.RelatedObservedPropertyDocument.RelatedObservedProperty;
import org.x52North.sensorweb.sos.importer.x05.RelatedSensorDocument.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x05.RelatedUnitOfMeasurementDocument.RelatedUnitOfMeasurement;
import org.x52North.sensorweb.sos.importer.x05.SensorType;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x05.SpatialResourceType;
import org.x52North.sensorweb.sos.importer.x05.URIDocument.URI;
import org.x52North.sensorweb.sos.importer.x05.UnitOfMeasurementType;

/**
 * Called in the case of having missing foi, observed property, unit of
 * measurement, or sensor for any measured value column.<br>
 * This handler deals with two cases: <ul>
 *  <li>Manual Resources</li>
 *  <li>Generated Resources</li></ul>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step6bModelHandler implements ModelHandler<Step6bModel> {

    private static final Logger logger = LoggerFactory.getLogger(Step6bModelHandler.class);

    @Override
    public void handleModel(final Step6bModel stepModel,
            final SosImportConfiguration sosImportConf) {
        logger.trace("handleModel()");
        /*
         *  LOCAL FIELDS
         */
        MeasuredValue mV;
        Resource res;
        int mVColumnID = -1;
        AdditionalMetadata addiMeta;
        Column mVColumn;
        /*
         * Get column reference from measured value object
         * Get Resource
         * Check Type
         * AddOrUpdate Element in additional metadata and in column meta data
         */
        mV = stepModel.getMeasuredValue();
        res = stepModel.getResource();
        logger.debug("Measured value: '{}'; Resource: '{}'", mV, res);
        if (mV != null && mV.getTableElement() != null) {
            mVColumnID = Helper.getColumnIdFromTableElement(mV.getTableElement());
            logger.debug("Column ID of measured value: {}", mVColumnID);
        }
        addiMeta = sosImportConf.getAdditionalMetadata();
        if (addiMeta == null) {
            addiMeta = sosImportConf.addNewAdditionalMetadata();
            logger.debug("added new AddtionalMetadata element");
        }
        mVColumn = Helper.getColumnById(mVColumnID, sosImportConf);
        // add resource to model
        if (addRelatedResource(res, mVColumn, addiMeta)) {
            logger.info("Related resource updated/added: '{}[{}]'", res, res.getXMLId());
        }
    }

    /**
     * Check and add/update additional metadata element and ColumnMetadata
     * measure value column having id <code>mVColumnID</code>.
     * @param res the related <code>Resource</code>
     */
    private boolean addRelatedResource(final Resource res, final Column mVColumn, final AdditionalMetadata addiMeta) {
        logger.trace("\t\taddRelatedResource()");
        /*
         *  ADD FEATURE_OF_INTEREST
         */
        if (res instanceof org.n52.sos.importer.model.resources.FeatureOfInterest) {
            final org.n52.sos.importer.model.resources.FeatureOfInterest foi =
                    (org.n52.sos.importer.model.resources.FeatureOfInterest) res;
            return addRelatedFOI(foi, mVColumn, addiMeta);
            /*
             *  ADD OBSERVED_PROPERTY
             */
        } else if (res instanceof org.n52.sos.importer.model.resources.ObservedProperty) {
            final org.n52.sos.importer.model.resources.ObservedProperty obsProp =
                    (org.n52.sos.importer.model.resources.ObservedProperty) res;
            return addRelatedObservedProperty(obsProp, mVColumn, addiMeta);
            /*
             *  ADD SENSOR
             */
        } else if (res instanceof org.n52.sos.importer.model.resources.Sensor) {
            final org.n52.sos.importer.model.resources.Sensor sensor =
                    (org.n52.sos.importer.model.resources.Sensor) res;
            return addRelatedSensor(sensor, mVColumn, addiMeta);
            /*
             *  ADD UNIT_OF_MEASUREMENT
             */
        } else if (res instanceof org.n52.sos.importer.model.resources.UnitOfMeasurement) {
            final org.n52.sos.importer.model.resources.UnitOfMeasurement uOM =
                    (org.n52.sos.importer.model.resources.UnitOfMeasurement) res;
            return addRelatedUOM(uOM, mVColumn, addiMeta);
        }
        return false;
    }

    private boolean addRelatedFOI(final FeatureOfInterest foi,
            final Column mVColumn,
            final AdditionalMetadata addiMeta) {
        logger.trace("\t\taddRelatedFOI()");

        FeatureOfInterestType foiXB = null;
        final FeatureOfInterestType[] foisXB = addiMeta.getFeatureOfInterestArray();
        org.n52.sos.importer.model.position.Position pos;
        Position posXB = null;

        if (foisXB != null && foisXB.length > 0) {

            findFOI :
            for (final FeatureOfInterestType aFOI : foisXB) {
                if (aFOI.getResource().getID().equalsIgnoreCase(foi.getXMLId())) {
                    foiXB = aFOI;
                    break findFOI;
                }
            }

        }
        if (foiXB == null) {
            foiXB = addiMeta.addNewFeatureOfInterest();
        }
        if (foi.isGenerated()) {
            /*
             * GENERATED
             */
            GeneratedSpatialResourceType foiGRT = null;
            if (foiXB.getResource() instanceof GeneratedResourceType) {
                foiGRT = (GeneratedSpatialResourceType) foiXB.getResource();
            }
            if (foiGRT == null) {
                foiGRT = (GeneratedSpatialResourceType) foiXB.addNewResource().
                        substitute(Constants.QNAME_GENERATED_SPATIAL_RESOURCE,
                                GeneratedSpatialResourceType.type);
            }
            foiGRT.setID(foi.getXMLId());
            URI uri = foiGRT.getURI();
            if (uri == null) {
                uri = foiGRT.addNewURI();
            }
            uri.setUseAsPrefix(foi.isUseNameAfterPrefixAsURI());
            if (foi.isUseNameAfterPrefixAsURI()) {
                uri.setStringValue(foi.getUriPrefix());
            }
            foiGRT.setConcatString(foi.getConcatString());
            final org.n52.sos.importer.model.table.Column[] relCols =
                    (org.n52.sos.importer.model.table.Column[]) foi.getRelatedCols();
            final int[] numbers = new int[relCols.length];
            for (int i = 0; i < relCols.length; i++) {
                final org.n52.sos.importer.model.table.Column c = relCols[i];
                numbers[i] = c.getNumber();
            }
            foiGRT.setNumberArray(numbers);
            // add position to FOI
            pos = foi.getPosition();
            posXB = foiGRT.getPosition();
            if (posXB == null) {
                posXB = foiGRT.addNewPosition();
            }
            fillXBPosition(posXB, pos);
        } else {
            /*
             * MANUAL
             */
            SpatialResourceType foiSRT = null;
            if (foiXB.getResource() instanceof SpatialResourceType) {
                foiSRT = (SpatialResourceType) foiXB.getResource();
            }
            if (foiSRT == null) {
                foiSRT = (SpatialResourceType) foiXB.addNewResource().
                        substitute(Constants.QNAME_MANUAL_SPATIAL_RESOURCE,
                                SpatialResourceType.type);
            }
            foiSRT.setName(foi.getName());
            foiSRT.setID(foi.getXMLId());
            URI uri = foiSRT.getURI();
            if (uri == null) {
                uri = foiSRT.addNewURI();
            }
            uri.setStringValue(foi.getURIString());
            // add position to FOI
            pos = foi.getPosition();
            posXB = foiSRT.getPosition();
            if (posXB == null) {
                posXB = foiSRT.addNewPosition();
            }
            fillXBPosition(posXB, pos);
        }
        /*
         * the FOI is in the model.
         * Next is to link measured value column to this entity by its XML id
         */
        RelatedFOI relatedFOI = mVColumn.getRelatedFOI();
        boolean addNew = !isIdOfRelatedFOIMatching(relatedFOI, foi.getXMLId());
        if (addNew) {
            mVColumn.addNewRelatedFOI().setIdRef(foi.getXMLId());
            logger.debug("Added new related FOI element to column #{} with id '{}'",
                    mVColumn.getNumber(), foi.getXMLId());
        }
        relatedFOI = mVColumn.getRelatedFOI();
        return isIdOfRelatedFOIMatching(relatedFOI, foi.getXMLId());
    }

    private void fillXBPosition(final Position posXB,
            final org.n52.sos.importer.model.position.Position pos) {
        logger.trace("\t\t\t\taddOrUpdatePosition()");
        if (pos == null || posXB == null) {
            logger.debug("One position is null: skip filling: pos? {}; posXB? {}", pos, posXB);
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("BEFORE: pos: {}; posXB: {}", pos, posXB);
        }
        if (pos.getGroup() != null && !pos.getGroup().equalsIgnoreCase("")) {
            /*
             * The position is contained in the file, so just add the link to
             * the group and finish
             */
            final String groupId = pos.getGroup();
            // check for old artifacts like alt/lat/long/epsg
            if (posXB.isSetEPSGCode()) {
                posXB.unsetEPSGCode();
            }
            posXB.setGroup(groupId);
        } else {
            // the position is defined manually, so add the elements
            // clean group before if set
            if (posXB.isSetGroup()) {
                posXB.unsetGroup();
            }
            /*
             *  EPSG_CODE
             */
            int epsgCode = pos.getEPSGCode().getValue();
            posXB.setEPSGCode(epsgCode);
            /*
             *  Coordinate values
             */
            CoordinateSystem cs = EPSGHelper.getCoordinateSystem(epsgCode);
            for (int i = 0; i < cs.getDimension(); i++) {
                CoordinateSystemAxis axis = cs.getAxis(i);
                Coordinate coordinate = getCoordinateByAxisAbbreviation(axis.getAbbreviation(), posXB);
                if (coordinate == null) {
                    coordinate = posXB.addNewCoordinate();
                }
                coordinate.setDoubleValue(pos.getCoordinate(Id.values()[i]).getValue());
                coordinate.setAxisAbbreviation(axis.getAbbreviation());
                coordinate.setUnit(axis.getUnit().toString());
            }
        }
        logger.debug("AFTER: posXB: {}", posXB);
    }

    private Coordinate getCoordinateByAxisAbbreviation(String abbreviation, Position posXB) {
        for (Coordinate coordinate : posXB.getCoordinateArray()) {
            if (coordinate != null && abbreviation.equals(coordinate.getAxisAbbreviation())) {
                return coordinate;
            }
        }
        return null;
    }

    private boolean addRelatedObservedProperty(
            final org.n52.sos.importer.model.resources.ObservedProperty obsProp,
            final Column mVColumn,
            final AdditionalMetadata addiMeta) {
        logger.trace("\t\taddRelatedObservedProperty()");

        ObservedPropertyType obsPropXB = null;
        final ObservedPropertyType[] obsPropsXB = addiMeta.getObservedPropertyArray();
        RelatedObservedProperty relatedObsProp;
        boolean addNew;

        if (obsPropsXB != null && obsPropsXB.length > 0) {
            findObservedProperty :
            for (final ObservedPropertyType obsPropy : obsPropsXB) {
                if (obsPropy.getResource().getID().equalsIgnoreCase(obsProp.getXMLId())) {
                    obsPropXB = obsPropy;
                    break findObservedProperty;
                }
            }
        }
        if (obsPropXB == null) {
            obsPropXB = addiMeta.addNewObservedProperty();
        }
        if (obsProp.isGenerated()) {
            /*
             * GENERATED
             */
            GeneratedResourceType obsPropGRT = null;
            if (obsPropXB.getResource() instanceof GeneratedResourceType) {
                obsPropGRT = (GeneratedResourceType) obsPropXB.getResource();
            }
            if (obsPropGRT == null) {
                obsPropGRT = (GeneratedResourceType) obsPropXB.addNewResource().
                        substitute(Constants.QNAME_GENERATED_RESOURCE,
                                GeneratedResourceType.type);
            }
            obsPropGRT.setID(obsProp.getXMLId());
            URI uri = obsPropGRT.getURI();
            if (uri == null) {
                uri = obsPropGRT.addNewURI();
            }
            uri.setUseAsPrefix(obsProp.isUseNameAfterPrefixAsURI());
            if (obsProp.isUseNameAfterPrefixAsURI()) {
                uri.setStringValue(obsProp.getUriPrefix());
            }
            obsPropGRT.setConcatString(obsProp.getConcatString());
            final org.n52.sos.importer.model.table.Column[] relCols =
                    (org.n52.sos.importer.model.table.Column[])
                    obsProp.getRelatedCols();
            final int[] numbers = new int[relCols.length];
            for (int i = 0; i < relCols.length; i++) {
                final org.n52.sos.importer.model.table.Column c = relCols[i];
                numbers[i] = c.getNumber();
            }
            obsPropGRT.setNumberArray(numbers);
        } else {
            /*
             * MANUAL
             */
            ManualResourceType obsPropMRT = null;
            if (obsPropXB.getResource() instanceof ManualResourceType) {
                obsPropMRT = (ManualResourceType) obsPropXB.getResource();
            }
            if (obsPropMRT == null) {
                obsPropMRT = (ManualResourceType) obsPropXB.addNewResource().
                        substitute(Constants.QNAME_MANUAL_RESOURCE,
                                ManualResourceType.type);
            }
            obsPropMRT.setName(obsProp.getName());
            obsPropMRT.setID(obsProp.getXMLId());
            URI uri = obsPropMRT.getURI();
            if (uri == null) {
                uri = obsPropMRT.addNewURI();
            }
            uri.setStringValue(obsProp.getURIString());
        }
        /*
         * the ObservedProperty is in the model.
         * Next is to link measure value column to this entity by its URI
         */
        relatedObsProp = mVColumn.getRelatedObservedProperty();
        addNew = !isIdOfObservedPropertyMatching(relatedObsProp, obsProp.getXMLId());
        if (addNew) {
            mVColumn.addNewRelatedObservedProperty().setIdRef(obsProp.getXMLId());
        }
        relatedObsProp = mVColumn.getRelatedObservedProperty();
        return isIdOfObservedPropertyMatching(relatedObsProp, obsProp.getXMLId());
    }

    private boolean addRelatedSensor(
            final org.n52.sos.importer.model.resources.Sensor sensor,
            final Column mVColumn,
            final AdditionalMetadata addiMeta) {
        logger.trace("\t\t\taddRelatedSensor()");

        SensorType sensorXB = null;
        final SensorType[] sensorsXB = addiMeta.getSensorArray();
        RelatedSensor relatedSensor;
        boolean addNew;

        if (sensorsXB != null && sensorsXB.length > 0) {

            findSensor :
            for (final SensorType aSensor : sensorsXB) {
                if (aSensor.getResource().getID().equalsIgnoreCase(sensor.getXMLId())) {
                    sensorXB = aSensor;
                    break findSensor;
                }
            }

        }
        if (sensorXB == null) {
            sensorXB = addiMeta.addNewSensor();
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
            URI uri = sensorGRT.getURI();
            if (uri == null) {
                uri = sensorGRT.addNewURI();
            }
            uri.setUseAsPrefix(sensor.isUseNameAfterPrefixAsURI());
            if (sensor.isUseNameAfterPrefixAsURI()) {
                uri.setStringValue(sensor.getUriPrefix());
            }
            sensorGRT.setConcatString(sensor.getConcatString());
            final org.n52.sos.importer.model.table.Column[] relCols =
                    (org.n52.sos.importer.model.table.Column[])
                    sensor.getRelatedCols();
            final int[] numbers = new int[relCols.length];
            for (int i = 0; i < relCols.length; i++) {
                final org.n52.sos.importer.model.table.Column c = relCols[i];
                numbers[i] = c.getNumber();
            }
            sensorGRT.setNumberArray(numbers);
            sensorXB.setResource(sensorGRT);
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
            sensorRT.setName(sensor.getName());
            URI uri = sensorRT.getURI();
            if (uri == null) {
                uri = sensorRT.addNewURI();
            }
            uri.setStringValue(sensor.getURIString());
            sensorRT.setID(sensor.getXMLId());
        }
        /*
         * the Sensor is in the model.
         * Next is to link measure value column to this entity by its URI
         */
        relatedSensor = mVColumn.getRelatedSensor();
        addNew = !Helper.isIdOfRelatedSensorMatching(relatedSensor, sensor.getXMLId());
        if (addNew) {
            mVColumn.addNewRelatedSensor().setIdRef(sensor.getXMLId());
        }
        relatedSensor = mVColumn.getRelatedSensor();
        return Helper.isIdOfRelatedSensorMatching(relatedSensor, sensor.getXMLId());
    }

    private boolean addRelatedUOM(
            final org.n52.sos.importer.model.resources.UnitOfMeasurement uOM,
            final Column mVColumn,
            final AdditionalMetadata addiMeta) {
        logger.trace("\t\t\taddRelatedUOM()");

        UnitOfMeasurementType uOMXB = null;
        final UnitOfMeasurementType[] uOMsXB = addiMeta.getUnitOfMeasurementArray();
        RelatedUnitOfMeasurement relatedUOM;
        boolean addNew;

        if (uOMsXB != null && uOMsXB.length > 0) {

            findUOM :
            for (final UnitOfMeasurementType uom : uOMsXB) {
                if (uom.getResource().getID().equalsIgnoreCase(uOM.getXMLId())) {
                    uOMXB = uom;
                    break findUOM;
                }
            }

        }
        if (uOMXB == null) {
            uOMXB = addiMeta.addNewUnitOfMeasurement();
        }
        if (uOM.isGenerated()) {
            /*
             * GENERATED
             */
            GeneratedResourceType uOMGRT = null;
            if (uOMXB.getResource() instanceof GeneratedResourceType) {
                uOMGRT = (GeneratedResourceType) uOMXB.getResource();
            }
            if (uOMGRT == null) {
                uOMGRT = (GeneratedResourceType) uOMXB.addNewResource().
                        substitute(Constants.QNAME_GENERATED_RESOURCE,
                                GeneratedResourceType.type);
            }
            uOMGRT.setID(uOM.getXMLId());
            URI uri = uOMGRT.getURI();
            if (uri == null) {
                uri = uOMGRT.addNewURI();
            }
            uri.setUseAsPrefix(uOM.isUseNameAfterPrefixAsURI());
            if (uOM.isUseNameAfterPrefixAsURI()) {
                uri.setStringValue(uOM.getUriPrefix());
            }
            uOMGRT.setConcatString(uOM.getConcatString());
            final org.n52.sos.importer.model.table.Column[] relCols =
                    (org.n52.sos.importer.model.table.Column[])
                                                        uOM.getRelatedCols();
            final int[] numbers = new int[relCols.length];
            for (int i = 0; i < relCols.length; i++) {
                final org.n52.sos.importer.model.table.Column c = relCols[i];
                numbers[i] = c.getNumber();
            }
            uOMGRT.setNumberArray(numbers);
        } else {
            /*
             * MANUAL
             */
            ManualResourceType uOMMRT = null;
            if (uOMXB.getResource() instanceof ManualResourceType) {
                uOMMRT = (ManualResourceType) uOMXB.getResource();
            }
            if (uOMMRT == null) {
                uOMMRT = (ManualResourceType) uOMXB.addNewResource().
                        substitute(Constants.QNAME_MANUAL_RESOURCE,
                                ManualResourceType.type);
            }
            uOMMRT.setName(uOM.getName());
            uOMMRT.setID(uOM.getXMLId());
            URI uri = uOMMRT.getURI();
            if (uri == null) {
                uri = uOMMRT.addNewURI();
            }
            uri.setStringValue(uOM.getURIString());
        }
        /*
         * the UOM is in the model.
         * Next is to link measure value column to this entity by its URI
         */
        relatedUOM = mVColumn.getRelatedUnitOfMeasurement();
        addNew = !isIdOfRelatedUnitOfMeasurementMatching(relatedUOM, uOM.getXMLId());
        if (addNew) {
            mVColumn.addNewRelatedUnitOfMeasurement().setIdRef(uOM.getXMLId());
        }
        relatedUOM = mVColumn.getRelatedUnitOfMeasurement();
        return isIdOfRelatedUnitOfMeasurementMatching(relatedUOM, uOM.getURIString());
    }

    private boolean isIdOfRelatedFOIMatching(RelatedFOI relatedFOI, String foiXmlId) {
        if (relatedFOI == null || foiXmlId == null || foiXmlId.isEmpty()) {
            return false;
        }
        return relatedFOI.isSetIdRef() && relatedFOI.getIdRef().equalsIgnoreCase(foiXmlId);
    }

    private boolean isIdOfObservedPropertyMatching(RelatedObservedProperty relatedObsProp, String obsPropXmlId) {
        if (relatedObsProp == null || obsPropXmlId == null || obsPropXmlId.isEmpty()) {
            return false;
        }
        return relatedObsProp.isSetIdRef() && relatedObsProp.getIdRef().equalsIgnoreCase(obsPropXmlId);
    }

    private boolean isIdOfRelatedUnitOfMeasurementMatching(RelatedUnitOfMeasurement relatedUOM, String uomUriXmlId) {
        if (relatedUOM == null || uomUriXmlId == null || uomUriXmlId .isEmpty()) {
            return false;
        }
        return relatedUOM.isSetIdRef() && relatedUOM.getIdRef().equalsIgnoreCase(uomUriXmlId);
    }
}
