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

import java.math.BigInteger;

import org.n52.sos.importer.model.Step4dModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.ColumnAssignmentsDocument.ColumnAssignments;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

public class Step4dModelHandler implements ModelHandler<Step4dModel> {

    private static final Logger logger = LoggerFactory.getLogger(Step4dModelHandler.class);

    @Override
    public void handleModel(Step4dModel stepModel, SosImportConfiguration sosImportConf) {
        if (stepModel == null || sosImportConf == null) {
            String error = String.format("Received null values: stepModel: '%s'); sosImportConf: '%s'",
                    stepModel,
                    sosImportConf);
            logger.error(error);
            throw new IllegalArgumentException(error);
        }
        CsvMetadata csvMeta = sosImportConf.getCsvMetadata();
        if (csvMeta == null) {
            csvMeta = sosImportConf.addNewCsvMetadata();
            logger.debug("Added new CsvMetadata element");
        }
        ColumnAssignments columnAssignments = csvMeta.getColumnAssignments();
        if (columnAssignments == null) {
            columnAssignments = csvMeta.addNewColumnAssignments();
            logger.debug("Added new ColumnAssignments element");
        }
        final Column[] columns = columnAssignments.getColumnArray();
        for (int measuredValueColumnIndex : stepModel.getSelectedColumns()) {
            Column column = getColumnForKey(measuredValueColumnIndex, columns);
            if (column == null) {
                column = columnAssignments.addNewColumn();
                column.setNumber(measuredValueColumnIndex);
                logger.debug("Added new Column element");
            }
            column.addNewRelatedOmParameter()
                .setBigIntegerValue(new BigInteger(
                    ((org.n52.sos.importer.model.table.Column) stepModel.getOmParameter().getTableElement())
                    .getNumber() + ""));

        }
        logger.debug("handling of Step4dModel finished");
    }

}
