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
import org.x52North.sensorweb.sos.importer.x04.ColumnAssignmentsDocument.ColumnAssignments;
import org.x52North.sensorweb.sos.importer.x04.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x04.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x04.RelatedFOIDocument.RelatedFOI;
import org.x52North.sensorweb.sos.importer.x04.RelatedObservedPropertyDocument.RelatedObservedProperty;
import org.x52North.sensorweb.sos.importer.x04.RelatedSensorDocument.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x04.RelatedUnitOfMeasurementDocument.RelatedUnitOfMeasurement;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * During this step, the relations between resource columns and the measured
 * value column are saved.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * TODO check functionality
 * @version $Id: $Id
 */
public class Step4bModelHandler implements ModelHandler<Step4bModel> {

    private static final String ERROR_INSTRUCTIONS = "step 4; should not happen. Please check the log file!";
    private static final Logger logger = LoggerFactory.getLogger(Step4bModelHandler.class);

    /** {@inheritDoc} */
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
        relCol = new ArrayList<Column>(availableCols.length);
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
                final RelatedFOI[] relFois = column.getRelatedFOIArray();
                addNew = !isFoiColIdInArray(relFois, colId);
                if (addNew) {
                    column.addNewRelatedFOI().setNumber(colId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Added new related foi by column");
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("Related foi was already there");
                }
                result = result && isFoiColIdInArray(relFois, colId);
                /*
                 *  SENSOR
                 */
            } else if (res instanceof org.n52.sos.importer.model.resources.Sensor) {
                final RelatedSensor[] relSensors = column.getRelatedSensorArray();
                addNew = !isSensorInArray(relSensors, colId);
                if (addNew) {
                    column.addNewRelatedSensor().setNumber(colId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Added new related sensor by column");
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("Related sensor was already there");
                }
                result = result && isSensorInArray(relSensors, colId);
                /*
                 *  UNIT_OF_MEASUREMENT
                 */
            } else if (res instanceof
                    org.n52.sos.importer.model.resources.UnitOfMeasurement) {
                final RelatedUnitOfMeasurement[] relUOMs = column.getRelatedUnitOfMeasurementArray();
                addNew = !isUOMInArray(relUOMs, colId);
                if (addNew) {
                    column.addNewRelatedUnitOfMeasurement().setNumber(colId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Added new related UOM by column");
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("Related UOM was already there");
                }
                result = result && isUOMInArray(relUOMs, colId);
                /*
                 *  OBSERVED_PROPERTY
                 */
            } else if (res instanceof
                    org.n52.sos.importer.model.resources.ObservedProperty) {
                final RelatedObservedProperty[] relObsProps = column.getRelatedObservedPropertyArray();
                addNew = !isObsPropInArray(relObsProps, colId);
                if (addNew) {
                    column.addNewRelatedObservedProperty().setNumber(colId);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Added new related observed property by column");
                    }
                } else if (logger.isDebugEnabled()) {
                    logger.debug("Related observed property was already there");
                }
                result = result && isObsPropInArray(relObsProps, colId);
            }
        }
        return result;
    }

    /**
     * Check if the column is already referenced.
     */
    private boolean isFoiColIdInArray(final RelatedFOI[] relFois, final int colId) {
        if (logger.isTraceEnabled()) {
            logger.trace("isFoiInArray()");
        }
        for (final RelatedFOI relatedFOI : relFois) {
            if (relatedFOI.isSetNumber() &&
                    relatedFOI.getNumber() == colId) {
                return true;
            }
        }
        return false;
    }

    private boolean isIntInArray(final int[] array, final int i) {
        if (logger.isTraceEnabled()) {
            logger.trace("\t\t\t\tisIntInArray()");
        }
        for (final int intFromArray : array) {
            if (intFromArray == i) {
                return true;
            }
        }
        return false;
    }

    private boolean isObsPropInArray(final RelatedObservedProperty[] relObsProps,
            final int colId) {
        if (logger.isTraceEnabled()) {
            logger.trace("isObsPropInArray()");
        }
        for (final RelatedObservedProperty relatedObsProp : relObsProps) {
            if (relatedObsProp.isSetNumber() &&
                    relatedObsProp.getNumber() == colId) {
                return true;
            }
        }
        return false;
    }

    private boolean isSensorInArray(final RelatedSensor[] relSensors, final int colId) {
        if (logger.isTraceEnabled()) {
            logger.trace("isSensorInArray()");
        }
        for (final RelatedSensor relatedSensor : relSensors) {
            if (relatedSensor.isSetNumber() &&
                    relatedSensor.getNumber() == colId) {
                return true;
            }
        }
        return false;
    }

    private boolean isUOMInArray(final RelatedUnitOfMeasurement[] relUOMs, final int colId) {
        if (logger.isTraceEnabled()) {
            logger.trace("isUOMInArray()");
        }
        for (final RelatedUnitOfMeasurement relatedUOM : relUOMs) {
            if (relatedUOM.isSetNumber() &&
                    relatedUOM.getNumber() == colId) {
                return true;
            }
        }
        return false;
    }
}
