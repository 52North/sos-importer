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

import org.n52.sos.importer.model.Step2Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x04.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x04.DataFileDocument.DataFile;
import org.x52North.sensorweb.sos.importer.x04.ParameterDocument.Parameter;
import org.x52North.sensorweb.sos.importer.x04.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * Get updates from Step2Model Provided information:
 * <ul>
 * <li>CsvMeta.Parameter.*</li>
 * <li>CsvMeta.FirstLineWithData</li>
 * <li>CsvMeta.UseHeader</li>
 * </ul>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class Step2ModelHandler implements ModelHandler<Step2Model> {

    private static final Logger logger = LoggerFactory.getLogger(Step2ModelHandler.class);

    /** {@inheritDoc} */
    @Override
    public void handleModel(final Step2Model stepModel,
            final SosImportConfiguration sosImportConf) {
        if (logger.isTraceEnabled()) {
            logger.trace("handleModel()");
        }
        CsvMetadata csvMetadata = sosImportConf.getCsvMetadata();
        Parameter parameter = null;
        //
        if (csvMetadata == null) {
            csvMetadata = sosImportConf.addNewCsvMetadata();
            parameter = csvMetadata.addNewParameter();
        } else {
            parameter = csvMetadata.getParameter();
        }
        if (parameter == null) {
            parameter = csvMetadata.addNewParameter();
        }
        csvMetadata.setFirstLineWithData(stepModel.getFirstLineWithData());
        csvMetadata.setUseHeader(stepModel.isUseHeader());
        csvMetadata.setDecimalSeparator(stepModel.getDecimalSeparator()+"");
        parameter.setCommentIndicator(stepModel.getCommentIndicator());
        parameter.setColumnSeparator(stepModel.getColumnSeparator());
        parameter.setTextIndicator(stepModel.getTextQualifier());
        if (stepModel.isSampleBased()) {
            // add other sampling parameters
            if (sosImportConf.getDataFile() == null) {
                sosImportConf.addNewDataFile();
            }
            final DataFile dataFile = sosImportConf.getDataFile();
            dataFile.setSampleStartRegEx(stepModel.getSampleBasedStartRegEx());
            dataFile.setSampleDateOffset(stepModel.getSampleBasedDateOffset());
            dataFile.setSampleDateExtractionRegEx(stepModel.getSampleBasedDateExtractionRegEx());
            dataFile.setSampleDatePattern(stepModel.getSampleBasedDatePattern());
            dataFile.setSampleDataOffset(stepModel.getSampleBasedDataOffset());
            dataFile.setSampleSizeOffset(stepModel.getSampleBasedSampleSizeOffset());
            dataFile.setSampleSizeRegEx(stepModel.getSampleBasedSampleSizeRegEx());
        }
    }

}
