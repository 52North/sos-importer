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

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.n52.sos.importer.Constants;
import org.n52.sos.importer.model.Step3Model;
import org.n52.sos.importer.view.i18n.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.ColumnAssignmentsDocument.ColumnAssignments;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x05.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x05.TypeDocument.Type;

/**
 * Get updates from Step3Model Provided information for each column:
 * <ul>
 * <li>column index</li>
 * <li>column type</li>
 * <li>the type depending metadata</li>
 * </ul>
 * Allowed column types are:
 * <ul>
 * <li>DO_NOT_EXPORT</li>
 * <li>MEASURED_VALUE</li>
 * <li>DATE_TIME</li>
 * <li>POSITION</li>
 * <li>FOI with optional parent feature identifier</li>
 * <li>OBSERVED_PROPERTY</li>
 * <li>UOM</li>
 * <li>SENSOR</li>
 * </ul>
 * The metadata consists of key value pairs. The allowed keys are:
 * <ul>
 * <li>TYPE</li>
 * <li>GROUP</li>
 * <li>PARSE_PATTERN</li>
 * <li>DECIMAL_SEPARATOR</li>
 * <li>THOUSANDS_SEPARATOR</li>
 * <li>SOS_FOI</li>
 * <li>SOS_OBSERVED_PROPERTY</li>
 * <li>SOS_SENSOR</li>
 * <li>OTHER</li>
 * </ul>
 * For the latest configuration set-up and schema check:
 * 52n-sos-importer-bindings/src/main/xsd/
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 */
public class Step3ModelHandler implements ModelHandler<Step3Model> {

    private static final Logger logger = LoggerFactory.getLogger(Step3ModelHandler.class);

    @Override
    public void handleModel(Step3Model stepModel, SosImportConfiguration sosImportConf) {
        logger.trace("handleModel()");
        final HashMap<Integer, List<String>> colAssignments = stepModel.getAllSelections();
        CsvMetadata csvMeta = sosImportConf.getCsvMetadata();
        //
        if (csvMeta == null) {
            csvMeta = sosImportConf.addNewCsvMetadata();
            logger.debug("Added new CsvMetadata element");
        }
        //
        ColumnAssignments colAssignmentsXB = csvMeta.getColumnAssignments();
        if (colAssignmentsXB == null) {
            colAssignmentsXB = csvMeta.addNewColumnAssignments();
            logger.debug("Added new ColumnAssignments element");
        }
        final Column[] cols = colAssignmentsXB.getColumnArray();
        //
        for (final Entry<Integer, List<String>> colAssignment : colAssignments.entrySet()) {
            /*
             * key = columnIndex List<String> contains: list.get(0) = type
             * list.get(n) = endcoded meta data Type:
             * * Date & Time
             * * Measured Value
             * * Position
             * * Feature of Interest
             * * Observed Property
             * * Unit of Measurement
             * * Sensor
             * * Do not export
             * Encoded Metadata:
             *  Date & Time:
             *  * Combination, Pattern <- parse pattern SEP Group
             *  * UNIX TIME
             * Measured Value:
             *  * Numeric, .:, (: is separator between decimal
             *    Count, 0 and thousands separator)
             *  * Boolean, 0
             *  * Text, 0
             * Position: Combination, Pattern <- parse pattern SEP Group
             */
            // value should have one or two elements
            final List<String> value = colAssignment.getValue();
            Column col = getColumnForKey(colAssignment.getKey(), cols);
            String encodedMetadata = null;
            if (col == null) {
                col = colAssignmentsXB.addNewColumn();
                col.setNumber(colAssignment.getKey());
            }
            /*
             * SIMPLE TYPES (incl. UnixTime, Do-Not-Export require no metadata)
             */
            if (value.size() < 2) {
                setSimpleColumnType(col, value.get(0));
                /*
                 * COMPLEX TYPES
                 */
            } else if (value.size() == 3) {
                String type = value.get(0);
                encodedMetadata = value.get(2);
                removeMetadataArray(col);
                //
                /*
                 * DATE & TIME
                 */
                if (type.equalsIgnoreCase(Lang.l().step3ColTypeDateTime())) {
                    setComplexColumnTypeDateAndTime(col, value.get(1),
                            encodedMetadata);
                    /*
                     * MEASURED VALUE
                     */
                } else if (type.equalsIgnoreCase(Lang.l().step3ColTypeMeasuredValue())) {
                    setComplexColumnTypeMeasuredValue(col, value.get(1),
                            encodedMetadata);
                    /*
                     * POSITION
                     */
                } else if (type.equalsIgnoreCase(Lang.l().position())) {
                    setComplexColumnTypePosition(col, value.get(1),
                            encodedMetadata);
                    /*
                     * OM:PARAMETER
                     */
                } else if (type.equalsIgnoreCase(Lang.l().step3ColTypeOmParameter())) {
                    setComplexColumnTypeOmParameter(col, value.get(1),
                            encodedMetadata);
                    /*
                     * FEATURE OF INTEREST with optional parent feature identifier
                     */
                } else if (type.equalsIgnoreCase(Lang.l().featureOfInterest())) {
                    setComplexTypeFeatureOfInterest(col, value);
                }
            } else {
                logger.error("Implementation error: value should have one to three elements: "
                        + value);
            }
        }
        logger.debug("handling of Step3Model finished");
    }

    private void setComplexTypeFeatureOfInterest(Column col, List<String> value) {
        logger.trace("\t\tsetComplexTypeFeatureOfInterest()");
        col.setType(Type.FOI);
        if (value != null && !value.isEmpty() && value.size() == 3 && value.get(1).equalsIgnoreCase("1")) {
            Helper.addOrUpdateColumnMetadata(Key.PARENT_FEATURE_IDENTIFIER, value.get(2), col);
        }
    }

    private void removeMetadataArray(Column col) {
        // delete old metadata
        Metadata[] metadata = col.getMetadataArray();
        if (metadata != null && metadata.length > 0) {
            int count = metadata.length;
            while (count-- > 0) {
                col.removeMetadata(0);
            }
        }
        metadata = null;
    }

    /**
     * Date & Time:<br>
     * Combination, Pattern <- parse pattern SEP Group<br>
     * UNIX TIME
     *
     */
    private void setComplexColumnTypeDateAndTime(Column col, String type, String encodedMetadata) {
        logger.trace("\t\tsetComplexTypeDateAndTime()");
        //
        col.setType(Type.DATE_TIME);
        //
        String[] splittedMetadata = encodedMetadata
                .split(Constants.SEPARATOR_STRING);
        String parsePattern = splittedMetadata[0];
        String group = splittedMetadata[1];
        //
        if (isValueSet(group)) {
            Helper.addOrUpdateColumnMetadata(Key.GROUP, group, col);
        }
        //
        if (isValueSet(parsePattern)) {
            Helper.addOrUpdateColumnMetadata(Key.PARSE_PATTERN, parsePattern, col);
        }
        //
        // TODO add type correct
        if (type.equalsIgnoreCase(Constants.COMBINATION)) {
            Helper.addOrUpdateColumnMetadata(Key.TYPE, Constants.COMBINATION, col);
        } else if (type.equalsIgnoreCase(Lang.l().step3DateAndTimeUnixTime())) {
            Helper.addOrUpdateColumnMetadata(Key.TYPE, Constants.UNIX_TIME, col);
        }
        //
        splittedMetadata = null;
        parsePattern = null;
        group = null;
    }

    private boolean isValueSet(String valueString) {
        return valueString != null && !valueString.isEmpty() && !valueString.equalsIgnoreCase("null");
    }

    /**
     * Measured Value:<br>
     * Numeric, .SEP, (decimal and thousands separator)<br>
     * Count, 0<br>
     * Boolean, 0<br>
     * Text, 0<br>
     *
     * @param type
     *            Numeric, Count, Boolean, or Text
     */
    private void setComplexColumnTypeMeasuredValue(Column col, String type, String encodedMetadata) {
        logger.trace("\t\tsetComplexColumnTypeMeasuredValue()");
        col.setType(Type.MEASURED_VALUE);

        String value = null;
        //
        if (type.equalsIgnoreCase(Lang.l().step3MeasuredValNumericValue())) {
            value = Constants.NUMERIC;
        } else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValBoolean())) {
            value = Constants.BOOLEAN;
        } else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValCount())) {
            value = Constants.COUNT;
        } else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValText())) {
            value = Constants.TEXT;
        }
        Helper.addOrUpdateColumnMetadata(Key.TYPE, value, col);
    }

    private void setComplexColumnTypeOmParameter(Column col, String type, String encodedMetadata) {
        if (col == null || type == null || type.isEmpty() || encodedMetadata == null || encodedMetadata.isEmpty()) {
            logger.error("Bad input given for setComplexColumnTypeOmParameter: {}, {}, {}",
                    col, type, encodedMetadata);
            return;
        }
        col.setType(Type.OM_PARAMETER);
        String value = null;
        if (type.equalsIgnoreCase(Lang.l().step3MeasuredValNumericValue())) {
            value = Constants.NUMERIC;
        } else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValBoolean())) {
            value = Constants.BOOLEAN;
        } else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValCount())) {
            value = Constants.COUNT;
        } else if (type.equalsIgnoreCase(Lang.l().step3MeasuredValText())) {
            value = Constants.TEXT;
        } else if (type.equalsIgnoreCase(Lang.l().step3OmParameterCategory())) {
            value = Constants.CATEGORY;
        } else {
            logger.error("Not Supported type for om:parameter found: '{}'", type);
            return;
        }
        Helper.addOrUpdateColumnMetadata(Key.TYPE, value, col);
        Helper.addOrUpdateColumnMetadata(Key.NAME, encodedMetadata, col);
    }

    /**
     * Position: Combination, Pattern <- parse pattern SEP Group
     *
     * @param type
     *            Combination
     * @param encodedMetadata
     *            e.g.: LAT LON ALT EPSG<code>SEP</code>A
     */
    private void setComplexColumnTypePosition(Column col, String type, String encodedMetadata) {
        logger.trace("\t\tsetComplexColumnTypePosition()");
        //
        col.setType(Type.POSITION);
        Metadata meta = col.addNewMetadata();
        meta.setKey(Key.TYPE);
        //
        if (type.equalsIgnoreCase(Lang.l().step3PositionCombination())) {
            final String[] splittedEncodedMetadat = encodedMetadata
                    .split(Constants.SEPARATOR_STRING);
            final String parsePattern = splittedEncodedMetadat[0];
            final String group = splittedEncodedMetadat[1];
            //
            meta.setValue(Constants.COMBINATION);
            //
            meta = col.addNewMetadata();
            meta.setKey(Key.PARSE_PATTERN);
            meta.setValue(parsePattern);
            //
            meta = col.addNewMetadata();
            meta.setKey(Key.GROUP);
            meta.setValue(group);
        }
        meta = null;
    }

    private void setSimpleColumnType(Column col, String type) {
        logger.trace("\t\tsetSimpleColumnType()");
        if (type.equalsIgnoreCase(Lang.l().step3ColTypeDoNotExport())) {
            col.setType(Type.DO_NOT_EXPORT);
        } else if (type.equalsIgnoreCase(Lang.l().sensor())) {
            col.setType(Type.SENSOR);
        } else if (type.equalsIgnoreCase(Lang.l().unitOfMeasurement())) {
            col.setType(Type.UOM);
        } else if (type.equalsIgnoreCase(Lang.l().observedProperty())) {
            col.setType(Type.OBSERVED_PROPERTY);
        } else if (type.equalsIgnoreCase(Lang.l().featureOfInterest())) {
            col.setType(Type.FOI);
        } else {
            logger.error("Type not known to schema : " + type);
        }
    }
}
