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

import org.n52.sos.importer.model.table.Cell;
import org.n52.sos.importer.model.table.Row;
import org.n52.sos.importer.model.table.TableElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.ColumnAssignmentsDocument.ColumnAssignments;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x05.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x05.RelatedSensorDocument.RelatedSensor;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Offering methods used by different ModelHandler classes.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Helper {

    private static final Logger logger = LoggerFactory.getLogger(Helper.class);

    /**
     * Checks, if a Metadata element with the given <b>key</b> exists,<br>
     *      if <b>yes</b>, update this one, <br>
     *      <b>else</b> add a new metadata element.
     *
     * @param key a Enum object.
     * @param value a {@link java.lang.String} object.
     * @param col a Column object.
     * @return a boolean.
     */
    protected static boolean addOrUpdateColumnMetadata(final Enum key,
            final String value,
            final Column col) {
        if (logger.isTraceEnabled()) {
            logger.trace("\t\taddOrUpdateColumnMetadata()");
        }
        final Metadata[] metaElems = col.getMetadataArray();
        Metadata meta = null;
        String addedOrUpdated = "Updated";
        // check if there is already a element with the given key
        for (final Metadata metadata : metaElems) {
            if (metadata.getKey().equals(key)) {
                meta = metadata;
                break;
            }
        }
        if (meta == null) {
            meta = col.addNewMetadata();
            meta.setKey(key);
            addedOrUpdated = "Added";
        }
        meta.setValue(value);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("%s column metadata. Key: '%s'; Value: '%s' in column '%d'.",
                    addedOrUpdated, key, value, col.getNumber()));
        }
        return meta.getValue().equalsIgnoreCase(value);
    }

    /**
     * <p>getColumnIdFromTableElement.</p>
     *
     * @param tabElem a {@link org.n52.sos.importer.model.table.TableElement} object.
     * @return the id of the column of this TableElement or -1
     */
    protected static int getColumnIdFromTableElement(final TableElement tabElem) {
        if (logger.isTraceEnabled()) {
            logger.trace("getColumnIdFromTableElement()");
        }
        if (tabElem == null) {
            return -1;
        }
        if (tabElem instanceof Cell) {
            final Cell c = (Cell) tabElem;
            return c.getColumn();
        } else if (tabElem instanceof org.n52.sos.importer.model.table.Column) {
            final org.n52.sos.importer.model.table.Column c = (org.n52.sos.importer.model.table.Column) tabElem;
            return c.getNumber();
            // TODO What is the reason for having it in rows?
        } else if (tabElem instanceof Row) {
            logger.error("Element is stored in rows. NOT YET IMPLEMENTED");
            return -1;
        }
        return -1;
    }

    /**
     * <p>getColumnById.</p>
     *
     * @param columnId a int.
     * @param sosImportConf a {@link SosImportConfiguration} object.
     * @return the Column from the configuration having id columnId
     */
    protected static Column getColumnById(final int columnId,
            final SosImportConfiguration sosImportConf) {
        if (logger.isTraceEnabled()) {
            logger.trace("getColumnById()");
        }
        final CsvMetadata csvMeta = sosImportConf.getCsvMetadata();
        if (csvMeta != null) {
            final ColumnAssignments colAssignMnts = csvMeta.getColumnAssignments();
            if (colAssignMnts != null) {
                final Column[] cols = colAssignMnts.getColumnArray();
                if (cols != null && cols.length > 0) {
                    // now we have the columns, iterate and check the id
                    // return the one with the required one
                    for (final Column col : cols) {
                        if (col.getNumber() == columnId) {
                            return col;
                        }
                    }
                }
            }
        }
        return null;
    }

    protected static boolean isIdOfRelatedSensorMatching(final RelatedSensor relatedSensor, final String sensorXmlId) {
        if (relatedSensor == null || sensorXmlId == null || sensorXmlId.isEmpty()) {
            return false;
        }
        return relatedSensor.isSetIdRef() && relatedSensor.getIdRef().equalsIgnoreCase(sensorXmlId);
    }
}
