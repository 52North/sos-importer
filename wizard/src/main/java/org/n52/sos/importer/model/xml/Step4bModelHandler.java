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

import java.util.ArrayList;

import org.n52.sos.importer.model.Step4bModel;
import org.n52.sos.importer.model.resources.Resource;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.ColumnAssignmentsDocument.ColumnAssignments;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x05.RelatedFOIDocument.RelatedFOI;
import org.x52North.sensorweb.sos.importer.x05.RelatedObservedPropertyDocument.RelatedObservedProperty;
import org.x52North.sensorweb.sos.importer.x05.RelatedSensorDocument.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x05.RelatedUnitOfMeasurementDocument.RelatedUnitOfMeasurement;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * During this step, the relations between resource columns and the measured
 * value column are saved.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * TODO check functionality
 */
public class Step4bModelHandler implements ModelHandler<Step4bModel> {

    private static final String ERROR_INSTRUCTIONS = "step 4; should not happen. Please check the log file!";
    private static final Logger logger = LoggerFactory.getLogger(Step4bModelHandler.class);

    @Override
    public void handleModel(final Step4bModel s4M,
            final SosImportConfiguration sosImportConf) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleModel()");
        }
        /*
         *  LOCALE FIELDS
         */
        int[] relatedColumnsIds;
        CsvMetadata csvMeta;
        ColumnAssignments colAssignmts;
        Column[] availableCols;
        Column[] relatedCols;
        ArrayList<Column> relCol;
        Resource res;
        // get related columns
        relatedColumnsIds = s4M.getSelectedColumns();
        csvMeta = sosImportConf.getCsvMetadata();
        if (csvMeta == null) {
            logger.error("CsvMetadata element not set in step 4; should not " +
                    "happen. Please check the log file!");
            return;
        }
        colAssignmts = csvMeta.getColumnAssignments();
        if (colAssignmts == null) {
            logger.error("CsvMetadata.ColumnAssignments element not set in " + ERROR_INSTRUCTIONS);
            return;
        }
        availableCols = colAssignmts.getColumnArray();
        if (availableCols == null) {
            logger.error("CsvMetadata.ColumnAssignments.Column elements not set in " +
                    ERROR_INSTRUCTIONS);
            return;
        }
        relCol = new ArrayList<>(availableCols.length);
        for (final Column column : availableCols) {
            // check for correct column id
            if (isIntInArray(relatedColumnsIds, column.getNumber())) {
                // add column to result set
                relCol.add(column);
            }
        }
        relCol.trimToSize();
        relatedCols = relCol.toArray(new Column[relCol.size()]);

        // identify type of resource that is linked to the given row and or columns
        res = s4M.getResource();

        // add relation to the related column
        addRelatedResourceColumn(res, relatedCols);

    }

    /**
     * For each <code>Column</code> in the array <code>relatedCols</code> add
     * a related resource
     * @param res the resource to add
     * @param relatedCols the column where to add the resource
     */
    private boolean addRelatedResourceColumn(final Resource res, final Column[] relatedCols) {
        if (logger.isTraceEnabled()) {
            logger.trace("\t\t\taddRelatedResourceColumn()");
        }
        /*
         *  LOCALE FIELDS
         */
        final TableElement tabE = res.getTableElement();
        int colId;
        boolean result = true;
        // get resource column
        if (tabE instanceof org.n52.sos.importer.model.table.Column) {
            final org.n52.sos.importer.model.table.Column col = (org.n52.sos.importer.model.table.Column) tabE;
            colId = col.getNumber();
        } else {
            logger.error("Type org.n52.sos.importer.model.table.Column expected. Type is: {}", tabE.getClass());
            return false;
        }
        /*
         * add colId of related resource to Columns in relatedCols
         */
        for (final Column column : relatedCols) {
            boolean addNew;
            /*
             *  FEATURE_OF_INTEREST
             */
            if (res instanceof org.n52.sos.importer.model.resources.FeatureOfInterest) {
                final RelatedFOI relFoi = column.getRelatedFOI();
                addNew = !isRelatedFoiLinked(relFoi, colId);
                if (addNew) {
                    column.addNewRelatedFOI().setNumber(colId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Added new related foi by column");
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("Related foi was already there");
                }
                result = result && isRelatedFoiLinked(relFoi, colId);
                /*
                 *  SENSOR
                 */
            } else if (res instanceof org.n52.sos.importer.model.resources.Sensor) {
                final RelatedSensor relSensor = column.getRelatedSensor();
                addNew = !isRelatedSensorLinked(relSensor, colId);
                if (addNew) {
                    column.addNewRelatedSensor().setNumber(colId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Added new related sensor by column");
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("Related sensor was already there");
                }
                result = result && isRelatedSensorLinked(relSensor, colId);
                /*
                 *  UNIT_OF_MEASUREMENT
                 */
            } else if (res instanceof
                    org.n52.sos.importer.model.resources.UnitOfMeasurement) {
                final RelatedUnitOfMeasurement relUOM = column.getRelatedUnitOfMeasurement();
                addNew = !isRelatedUOMLinked(relUOM, colId);
                if (addNew) {
                    column.addNewRelatedUnitOfMeasurement().setNumber(colId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Added new related UOM by column");
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("Related UOM was already there");
                }
                result = result && isRelatedUOMLinked(relUOM, colId);
                /*
                 *  OBSERVED_PROPERTY
                 */
            } else if (res instanceof
                    org.n52.sos.importer.model.resources.ObservedProperty) {
                final RelatedObservedProperty relObsProp = column.getRelatedObservedProperty();
                addNew = !isRelatedObservedPropertyLinked(relObsProp, colId);
                if (addNew) {
                    column.addNewRelatedObservedProperty().setNumber(colId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Added new related observed property by column");
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("Related observed property was already there");
                }
                result = result && isRelatedObservedPropertyLinked(relObsProp, colId);
            }
        }
        return result;
    }

    /**
     * Check if the column is already referenced.
     */
    private boolean isRelatedFoiLinked(final RelatedFOI relatedFOI, final int colId) {
        return relatedFOI.isSetNumber() && relatedFOI.getNumber() == colId;
    }

    private boolean isIntInArray(final int[] array, final int i) {
        for (final int intFromArray : array) {
            if (intFromArray == i) {
                return true;
            }
        }
        return false;
    }

    private boolean isRelatedObservedPropertyLinked(final RelatedObservedProperty relatedObsProp,
            final int colId) {
        return relatedObsProp.isSetNumber() && relatedObsProp.getNumber() == colId;
    }

    private boolean isRelatedSensorLinked(final RelatedSensor relatedSensor, final int colId) {
        return relatedSensor.isSetNumber() && relatedSensor.getNumber() == colId;
    }

    private boolean isRelatedUOMLinked(final RelatedUnitOfMeasurement relatedUOM, final int colId) {
        return relatedUOM.isSetNumber() && relatedUOM.getNumber() == colId;
    }
}
