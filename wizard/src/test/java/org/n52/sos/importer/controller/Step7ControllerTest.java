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
package org.n52.sos.importer.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.n52.sos.importer.Constants.ImportStrategy;
import org.n52.sos.importer.model.Step7Model;
import org.n52.sos.importer.view.Step7Panel;

/**
 * <p>Step7ControllerTest class.</p>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 * @since 0.5.0
 */
@Ignore("Requires display which is not available on remote build servers")
public class Step7ControllerTest {

    private Step7Controller controller;

    /**
     * <p>init.</p>
     */
    @Before
    public void init() {
        controller = new Step7Controller();
    }

    /**
     * <p>shouldSetHunkSizeInModel.</p>
     */
    @Test
    public void shouldSetHunkSizeInModel() {
        controller.loadSettings();
        ((Step7Panel) controller.getStepPanel())
            .setHunkSize(42)
            .setImportStrategy(ImportStrategy.SweArrayObservationWithSplitExtension);
        controller.saveSettings();
        org.junit.Assert.assertThat(
                ((Step7Model) controller.getModel()).getHunkSize(),
                org.hamcrest.CoreMatchers.is(42));
    }

    /**
     * <p>shouldSetSendBufferInModel.</p>
     */
    @Test
    public void shouldSetSendBufferInModel() {
        controller.loadSettings();
        ((Step7Panel) controller.getStepPanel())
            .setSendBuffer(42)
            .setImportStrategy(ImportStrategy.SweArrayObservationWithSplitExtension);
        controller.saveSettings();
        org.junit.Assert.assertThat(
                ((Step7Model) controller.getModel()).getSendBuffer(),
                org.hamcrest.CoreMatchers.is(42));
    }

    /**
     * <p>shouldSetImportStrategyInModel.</p>
     */
    @Test
    public void shouldSetImportStrategyInModel() {
        controller.loadSettings();
        ((Step7Panel) controller.getStepPanel())
            .setImportStrategy(ImportStrategy.SweArrayObservationWithSplitExtension);
        controller.saveSettings();
        org.junit.Assert.assertThat(
                ((Step7Model) controller.getModel()).getImportStrategy(),
                org.hamcrest.CoreMatchers.is(ImportStrategy.SweArrayObservationWithSplitExtension));
    }

}
