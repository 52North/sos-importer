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
package org.n52.sos.importer.test;

import java.io.File;
import java.io.IOException;

import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.ColumnDocument.Column;
import org.x52North.sensorweb.sos.importer.x05.CsvMetadataDocument.CsvMetadata;
import org.x52North.sensorweb.sos.importer.x05.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x05.OfferingDocument.Offering;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x05.SosMetadataDocument.SosMetadata;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 0.5.0
 */
public class ConfigurationTest {

    private static final String AQUOT = "\"";
    private static final Logger logger = LoggerFactory.getLogger(ConfigurationTest.class);
    private static final File TEST_CONFIG = new File("src/test/xml/configuration-test.xml");

    //CHECKSTYLE:OFF
    public static void main(final String[] args) throws XmlException, IOException {
        //CHECKSTYLE_ON
        if (logger.isDebugEnabled()) {
            logger.debug("Starting configuration test.");
            logger.debug("try loading file \"" +
                    TEST_CONFIG.getAbsolutePath() + AQUOT);
        }
        final SosImportConfigurationDocument importerConfiguration =
                SosImportConfigurationDocument.Factory.parse(TEST_CONFIG);
        final SosImportConfiguration importConf = importerConfiguration.getSosImportConfiguration();
        if (importConf.getDataFile().isSetLocalFile()) {
            final String path = importConf.getDataFile().getLocalFile().getPath();
            if (logger.isDebugEnabled()) {
                logger.debug("Path for datafile: " + path);
            }
        }
        final CsvMetadata csvMeta = importConf.getCsvMetadata();
        if (logger.isDebugEnabled()) {
            logger.debug("CSV metadata found:");
            //
            final int firstLineWithData = csvMeta.getFirstLineWithData();
            final String commentIndicator = csvMeta.getParameter().getCommentIndicator();
            final String elemSep = csvMeta.getParameter().getColumnSeparator();
            final String txtIndi = csvMeta.getParameter().getTextIndicator();
            final Column[] columns = csvMeta.getColumnAssignments().getColumnArray();
            Metadata[] colMetadata;
            //
            logger.debug("firstLineWithData: " + firstLineWithData);
            logger.debug("commentIndicator: "  + commentIndicator);
            logger.debug("elementSeparator: "  + elemSep);
            logger.debug("textIndicator: "     + txtIndi);
            logger.debug("Columns (" + columns.length + "): ");
            for (final Column column : columns) {
                logger.debug("Column[" + column.getNumber() + "]: " +
                        "; type: " +
                        column.getType().toString() +
                        "; metadata?: " +
                        column.sizeOfMetadataArray());
                if (column.sizeOfMetadataArray() > 0) {
                    colMetadata = column.getMetadataArray();
                    for (final Metadata m : colMetadata) {
                        logger.debug("\tKey: " +
                                m.getKey().toString() +
                                "; Value: " +
                                m.getValue());
                    }
                }
            }
        }
        final SosMetadata sosMeta = importConf.getSosMetadata();
        if (logger.isDebugEnabled()) {
            logger.debug("SOS metadata found");
            //
            final String sosUrl = sosMeta.getURL();
            boolean offeringAutogenerate = false;
            final Offering sosOff = sosMeta.getOffering();
            final String offeringInfo = sosOff.getStringValue();
            //
            if (sosOff.isSetGenerate()) {
                offeringAutogenerate = sosOff.getGenerate();
            }
            //
            logger.debug("sosURL: " + sosUrl);
            logger.debug("autogenerate Offering?: " + offeringAutogenerate);
            logger.debug("offeringInfo: \"" + offeringInfo + AQUOT);
        }
    }

}
