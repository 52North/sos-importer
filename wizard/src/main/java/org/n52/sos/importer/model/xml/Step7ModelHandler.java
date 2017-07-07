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

import org.n52.sos.importer.Constants.ImportStrategy;
import org.n52.sos.importer.model.Step7Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x05.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x05.OfferingDocument.Offering;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;
import org.x52North.sensorweb.sos.importer.x05.SosMetadataDocument.SosMetadata;

/**
 * Saves the SOS URL and the offering settings.
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @version $Id: $Id
 */
public class Step7ModelHandler implements ModelHandler<Step7Model> {

    private static final Logger LOG = LoggerFactory.getLogger(Step7ModelHandler.class);

    /** {@inheritDoc} */
    @Override
    public void handleModel(final Step7Model stepModel,
            final SosImportConfiguration sosImportConf) {
        LOG.trace("handleModel()");
        SosMetadata sosMeta = sosImportConf.getSosMetadata();
        //
        if (sosMeta == null) {
            sosMeta = sosImportConf.addNewSosMetadata();
            LOG.debug("Added new SosMetadata element.");
        }
        sosMeta.setURL(stepModel.getSosURL());
        Offering off = sosMeta.getOffering();
        if (off == null) {
            off = sosMeta.addNewOffering();
            LOG.debug("Added new Offering element");
        }
        off.setGenerate(stepModel.isGenerateOfferingFromSensorName());
        if (!off.getGenerate()) {
            off.setStringValue(stepModel.getOffering());
        }
        if (stepModel.getBinding() != null && !stepModel.getBinding().isEmpty()) {
            sosMeta.setBinding(stepModel.getBinding());
        }
        if (stepModel.getVersion() != null && !stepModel.getVersion().isEmpty()) {
            sosMeta.setVersion(stepModel.getVersion());
        }
        addImportStrategy(stepModel.getImportStrategy(), sosImportConf);
        if (stepModel.getImportStrategy().equals(ImportStrategy.SweArrayObservationWithSplitExtension)) {
            sosMeta.setInsertSweArrayObservationTimeoutBuffer(stepModel.getSendBuffer());
            addHunkSize(stepModel.getHunkSize(), sosImportConf);
        }
        addIgnoreColumnMismatch(stepModel.isIgnoreColumnMismatch(), sosImportConf);
    }

    private void addIgnoreColumnMismatch(boolean ignoreColumnMismatch, SosImportConfiguration sosImportConf) {
        if (sosImportConf.getCsvMetadata() == null) {
            sosImportConf.addNewCsvMetadata();
        }
        if (!sosImportConf.getCsvMetadata().isSetCsvParserClass()) {
            sosImportConf.getCsvMetadata().addNewCsvParserClass();
        }
        sosImportConf.getCsvMetadata().getCsvParserClass().setIgnoreColumnCountMismatch(ignoreColumnMismatch);
    }

    private void addHunkSize(final int hunkSize,
            final SosImportConfiguration sosImportConf) {
        addAdditionalMetadata(sosImportConf, Key.HUNK_SIZE, Integer.toString(hunkSize));
    }

    private void addImportStrategy(final ImportStrategy importStrategy,
            final SosImportConfiguration sosImportConf) {
        final Enum key = Key.IMPORT_STRATEGY;
        String value = "SingleObservation";
        if (importStrategy == ImportStrategy.SweArrayObservationWithSplitExtension) {
            value = "SweArrayObservationWithSplitExtension";
        }
        addAdditionalMetadata(sosImportConf, key, value);
    }

    private void addAdditionalMetadata(final SosImportConfiguration sosImportConf,
            final Enum key,
            final String value) {
        if (!sosImportConf.isSetAdditionalMetadata()) {
            sosImportConf.addNewAdditionalMetadata();
        }
        Metadata importStrategyMetadata = null;
        if (sosImportConf.getAdditionalMetadata().getMetadataArray() == null ||
                sosImportConf.getAdditionalMetadata().getMetadataArray().length == 0) {
            importStrategyMetadata = sosImportConf.getAdditionalMetadata().addNewMetadata();
        } else {
            boolean notFound = true;
            for (final Metadata metadata : sosImportConf.getAdditionalMetadata().getMetadataArray()) {
                if (metadata.getKey().equals(key)) {
                    importStrategyMetadata = metadata;
                    notFound = false;
                    break;
                }
            }
            if (notFound) {
                importStrategyMetadata = sosImportConf.getAdditionalMetadata().addNewMetadata();
            }
        }
        importStrategyMetadata.setKey(key);
        importStrategyMetadata.setValue(value);
    }

}
