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

import java.util.NoSuchElementException;

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.n52.sos.importer.Constants.ImportStrategy;
import org.n52.sos.importer.model.Step7Model;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key;
import org.x52North.sensorweb.sos.importer.x05.KeyDocument.Key.Enum;
import org.x52North.sensorweb.sos.importer.x05.MetadataDocument.Metadata;
import org.x52North.sensorweb.sos.importer.x05.SosImportConfigurationDocument.SosImportConfiguration;

/**
 * <p>Step7ModelHandlerTest class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 0.5.0
 */
public class Step7ModelHandlerTest {

    @Test
    public void shouldAddBindingIfSetInModel() {
        final String binding = "test-binding";
        final Step7Model stepModel = new Step7Model(null, null, false, null, null, binding, false);
        final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
        new Step7ModelHandler().handleModel(stepModel, importConf);

        Assert.assertThat(importConf.getSosMetadata().isSetBinding(), Is.is(true));
        Assert.assertThat(importConf.getSosMetadata().getBinding(), Is.is(binding));
    }

    @Test
    public void shouldNotAddBindingIfEmptyOrNull() {
        final Step7Model stepModelEmpty = new Step7Model(null, null, false, null, null, "", false);
        final SosImportConfiguration importConfEmpty = SosImportConfiguration.Factory.newInstance();
        new Step7ModelHandler().handleModel(stepModelEmpty, importConfEmpty);

        final Step7Model stepModelNull = new Step7Model(null, null, false, null, null, null, false);
        final SosImportConfiguration importConfNull = SosImportConfiguration.Factory.newInstance();
        new Step7ModelHandler().handleModel(stepModelNull, importConfNull);

        Assert.assertThat(importConfEmpty.getSosMetadata().isSetBinding(), Is.is(false));
        Assert.assertThat(importConfNull.getSosMetadata().isSetBinding(), Is.is(false));
    }

    @Test
    public void shouldAddVersionIfSetInModel() {
        final String version = "test-version";
        final Step7Model stepModel = new Step7Model(null, null, false, null, version, null, false);
        final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
        new Step7ModelHandler().handleModel(stepModel, importConf);

        Assert.assertThat(importConf.getSosMetadata().getVersion(), Is.is(version));
    }

    @Test
    public void shouldNotAddVersionIfEmptyOrNull() {
        final Step7Model stepModelEmpty = new Step7Model(null, null, false, null, "", null, false);
        final SosImportConfiguration importConfEmpty = SosImportConfiguration.Factory.newInstance();
        new Step7ModelHandler().handleModel(stepModelEmpty, importConfEmpty);

        final Step7Model stepModelNull = new Step7Model(null, null, false, null, null, null, false);
        final SosImportConfiguration importConfNull = SosImportConfiguration.Factory.newInstance();
        new Step7ModelHandler().handleModel(stepModelNull, importConfNull);

        Assert.assertThat(importConfEmpty.getSosMetadata().getVersion(), Is.is(CoreMatchers.nullValue()));
        Assert.assertThat(importConfNull.getSosMetadata().getVersion(), Is.is(CoreMatchers.nullValue()));
    }

    @Test
    public void shouldSetImportStrategy() {
        final Step7Model stepModel = new Step7Model(null, null, false, null, "", null, false);
        stepModel.setImportStrategy(ImportStrategy.ResultHandling);
        final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
        new Step7ModelHandler().handleModel(stepModel, importConf);

        Assert.assertThat(importConf.getSosMetadata().getImporter().getStringValue(),
                Is.is("org.n52.sos.importer.feeder.importer.ResultHandlingImporter"));
    }

    @Test
    public void shouldSetHunkSize() {
        final int hunkSize = 42;
        final Step7Model stepModel = new Step7Model(null, null, false, null, "", null, false)
                .setImportStrategy(ImportStrategy.SweArrayObservationWithSplitExtension)
                .setHunkSize(hunkSize);
        final SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
        new Step7ModelHandler().handleModel(stepModel, importConf);

        Assert.assertThat(
                getAdditionalMetadata(importConf, Key.HUNK_SIZE),
                Is.is(Integer.toString(hunkSize)));
    }

    private String getAdditionalMetadata(final SosImportConfiguration importConf,
            final Enum key) {
        for (final Metadata metadata : importConf.getAdditionalMetadata().getMetadataArray()) {
            if (metadata.getKey().equals(key)) {
                return metadata.getValue();
            }
        }
        throw new NoSuchElementException(String.format("Element with Key '%s' not found", key));
    }

    @Test
    public void shouldSetIgnoreColumnMismatch() {
        Step7Model stepModel = new Step7Model(null, null, false, null, "", null, false);
        SosImportConfiguration importConf = SosImportConfiguration.Factory.newInstance();
        new Step7ModelHandler().handleModel(stepModel, importConf);

        Assert.assertThat(
                importConf.getCsvMetadata().getObservationCollector().isNil(),
                Is.is(false));
        Assert.assertThat(
                importConf.getCsvMetadata().getObservationCollector().isSetIgnoreColumnCountMismatch(),
                Is.is(true));
        Assert.assertThat(
                importConf.getCsvMetadata().getObservationCollector().getIgnoreColumnCountMismatch(),
                Is.is(false));

        stepModel = new Step7Model(null, null, false, null, "", null, true);
        importConf = SosImportConfiguration.Factory.newInstance();
        new Step7ModelHandler().handleModel(stepModel, importConf);
        Assert.assertThat(
                importConf.getCsvMetadata().getObservationCollector().getIgnoreColumnCountMismatch(),
                Is.is(true));
    }

}
